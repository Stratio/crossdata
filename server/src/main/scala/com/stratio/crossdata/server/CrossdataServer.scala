/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.server

import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Address, Props}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.http.scaladsl.Http.ServerBinding
import akka.routing.{DefaultResizer, RoundRobinPool}
import com.hazelcast.config.{XmlConfigBuilder, Config => HzConfig}
import com.stratio.crossdata.common.util.akka.keepalive.KeepAliveMaster
import com.stratio.crossdata.server.actors.{ResourceManagerActor, ServerActor}
import com.stratio.crossdata.server.config.ServerConfig
import com.stratio.crossdata.server.discovery.{ServiceDiscoveryConfigHelper => SDCH, ServiceDiscoveryHelper => SDH}
import com.typesafe.config.{Config, ConfigValueFactory}
import org.apache.curator.framework.recipes.leader.LeaderLatch
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.utils.ZKPaths
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, HazelcastSessionProvider, XDSessionProvider}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Try}


class CrossdataServer(progrConfig: Option[Config] = None) extends ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[CrossdataServer])

  var system: Option[ActorSystem] = None
  var sessionProviderOpt: Option[XDSessionProvider] = None
  var bindingFuture: Option[Future[ServerBinding]] = None

  private val serverConfig = progrConfig map (_.withFallback(config)) getOrElse (config)

  private val hzConfig: HzConfig = new XmlConfigBuilder().build()


  def start(): Unit = {

    val sparkParams = serverConfig.entrySet()
      .map(e => (e.getKey, e.getValue.unwrapped().toString))
      .toMap
      .filterKeys(_.startsWith("config.spark"))
      .map(e => (e._1.replace("config.", ""), e._2))

    val metricsPath = Option(sparkParams.get("spark.metrics.conf"))

    val filteredSparkParams = metricsPath.fold(sparkParams)(m => checkMetricsFile(sparkParams, m.get))

    val sparkContext = new SparkContext(new SparkConf().setAll(filteredSparkParams))

    // Get service discovery configuration
    val sdConfig = Try(serverConfig.getConfig(SDCH.ServiceDiscoveryPrefix)).toOption

    val sdHelper: Option[SDH] = sdConfig flatMap { serConfig =>
      Try(serConfig.getBoolean("activated")).toOption collect {
        case true =>
          logger.info("Service discovery enabled")
          startServiceDiscovery(new SDCH(sdConfig.get))
      }
    }

    val finalConfig = sdHelper.fold(serverConfig)(_.finalConfig)

    val finalHzConfig = sdHelper.fold(hzConfig)(_.hzConfig)

    sessionProviderOpt = Some {
      if (isHazelcastEnabled)
        new HazelcastSessionProvider(sparkContext, serverConfig, finalHzConfig)
      else
        new BasicSessionProvider(sparkContext, serverConfig)
    }

    val sessionProvider = sessionProviderOpt
      .getOrElse(throw new RuntimeException("Crossdata Server cannot be started because there is no session provider"))

    assert(
      sdHelper.nonEmpty || sessionProvider.isInstanceOf[HazelcastSessionProvider],
      "Service Discovery needs to have the Hazelcast session provider enabled")

    finalConfig.entrySet.filter{ e =>
      e.getKey.contains("seed-nodes")
    }.foreach{ e =>
      logger.info(s"Seed nodes: ${e.getValue}")
    }

    system = Some(ActorSystem(clusterName, finalConfig))

    system.fold(throw new RuntimeException("Actor system cannot be started")) { actorSystem =>

      val xdCluster = Cluster(actorSystem)

      sdHelper.map{ sd =>

        // Once the Cluster has been started and the cluster leadership is gotten,
        // this sever will update the list of cluster seeds and provider members periodically
        // according to the Akka members.
        import scala.concurrent.ExecutionContext.Implicits.global
        sd.leadershipFuture onSuccess {
          case _ =>
            updateServiceDiscovery(xdCluster, sessionProvider.asInstanceOf[HazelcastSessionProvider], sd, actorSystem)
        }
      }

      val resizer = DefaultResizer(lowerBound = minServerActorInstances, upperBound = maxServerActorInstances)
      val serverActor = actorSystem.actorOf(
        RoundRobinPool(minServerActorInstances, Some(resizer)).props(
          Props(
            classOf[ServerActor],
            xdCluster,
            sessionProvider)),
        actorName)

      val clientMonitor = actorSystem.actorOf(KeepAliveMaster.props(serverActor), "client-monitor")
      ClusterClientReceptionist(actorSystem).registerService(clientMonitor)

      val resourceManagerActor = actorSystem.actorOf(ResourceManagerActor.props(Cluster(actorSystem), sessionProvider))
      ClusterClientReceptionist(actorSystem).registerService(serverActor)
      ClusterClientReceptionist(actorSystem).registerService(resourceManagerActor)

      //TODO
      /*implicit val httpSystem = actorSystem
      implicit val materializer = ActorMaterializer()
      val httpServerActor = new CrossdataHttpServer(finalConfig, serverActor, actorSystem)
      val host = finalConfig.getString(ServerConfig.Host)
      val port = finalConfig.getInt(ServerConfig.HttpServerPort)
      bindingFuture = Option(Http().bindAndHandle(httpServerActor.route, host, port))*/
    }

    logger.info(s"Crossdata Server started --- v${crossdata.CrossdataVersion}")
  }

  /**
    * Just for test purposes
    */
  def stop(): Unit = {
    sessionProviderOpt.foreach(_.close())

    sessionProviderOpt.foreach(_.sc.stop())

    system.foreach { actSystem =>
      implicit val exContext = actSystem.dispatcher
      bindingFuture.foreach { bFuture =>
        bFuture.flatMap(_.unbind()).onComplete(_ => actSystem.shutdown())
      }
    }

    logger.info("Crossdata Server stopped")
  }


  private def startDiscoveryClient(sdConfig: SDCH): CuratorFramework = {

    val curatorClient = CuratorFrameworkFactory.newClient(
      sdConfig.getOrElse(SDCH.ServiceDiscoveryUrl, SDCH.ServiceDiscoveryDefaultUrl),
      new ExponentialBackoffRetry(
        SDCH.RetrySleep,
        SDCH.Retries))
    curatorClient.start
    curatorClient.blockUntilConnected
    curatorClient
  }

  private def requestSubscriptionLeadership(dClient: CuratorFramework, sdc: SDCH) = {

    val sLeaderPath = sdc.getOrElse(SDCH.SubscriptionPath, SDCH.DefaultSubscriptionPath)

    logger.debug(s"Service discovery - subscription leadership path: $sLeaderPath")

    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, sLeaderPath)

    val sLeader = new LeaderLatch(dClient, sLeaderPath)

    sLeader.start

    Try {
      if(sLeader.await(sdc.getOrElse(
        SDCH.SubscriptionTimeoutPath, SDCH.DefaultSubscriptionTimeout.toString).toLong, TimeUnit.SECONDS)){
        logger.info("Subscription leadership acquired")
        sLeader
      } else {
        throw new RuntimeException("Timeout acquiring subscription leadership")
      }
    } recoverWith {
      case e => Failure(new RuntimeException(s"Subscription leadership couldn't be acquired: ${e.getMessage}" ))
    }
  }

  private def requestClusterLeadership(dClient: CuratorFramework, sdc: SDCH) = {

    val cLeaderPath = sdc.getOrElse(SDCH.ClusterLeaderPath, SDCH.DefaultClusterLeaderPath)

    logger.debug(s"Service discovery - cluster leadership path: $cLeaderPath")

    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, cLeaderPath)

    val randomId = UUID.randomUUID.toString

    logger.debug(s"My cluster leadership ID: $randomId")

    val cLeader = new LeaderLatch(
      dClient,
      cLeaderPath,
      randomId)

    cLeader.start

    import com.stratio.crossdata.server.actors.JobActor.ProlificExecutor
    implicit val _: ExecutionContext = ExecutionContext.fromExecutor(new ProlificExecutor)

    val leadershipFuture = Future {
      cLeader.await
    }

    leadershipFuture onSuccess {
      case _ => logger.info("Cluster leadership acquired")
    }

    leadershipFuture
  }

  private def getLocalSeed: String =
    s"${Try(serverConfig.getString("akka.remote.netty.tcp.hostname")).getOrElse("127.0.0.1")}:${Try(serverConfig.getInt("akka.remote.netty.tcp.port")).getOrElse("13420")}"

  private def getLocalSeed(xdCluster: Cluster): String = {
    val selfAddress = xdCluster.selfAddress
    s"${selfAddress.host.getOrElse("127.0.0.1")}:${selfAddress.port.getOrElse("13420")}"
  }

  private def getLocalMember: String =
    s"${Try(hzConfig.getNetworkConfig.getJoin.getTcpIpConfig.getMembers.head).getOrElse("127.0.0.1:5701")}"

  private def getLocalMember(hsp: HazelcastSessionProvider): String = {
    val selfAddress = hsp.gelLocalMember.getAddress
    s"${selfAddress.getHost}:${selfAddress.getPort}"
  }

  private def generateFinalConfig(dClient: CuratorFramework, sdc: SDCH) = {

    val pathForSeeds = sdc.getOrElse(SDCH.SeedsPath, SDCH.DefaultSeedsPath)
    logger.debug(s"Service Discovery - seeds path: $pathForSeeds")

    val pathForMembers = sdc.getOrElse(SDCH.ProviderPath, SDCH.DefaultProviderPath)
    logger.debug(s"Service Discovery - members path: $pathForMembers")

    val localSeed = getLocalSeed
    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, pathForSeeds)
    val currentSeeds = new String(dClient.getData.forPath(pathForSeeds))
    val newSeeds = (Set(localSeed) ++ currentSeeds.split(",").toSet).map(m => m.trim).filter(_.nonEmpty)
    dClient.setData.forPath(pathForSeeds, newSeeds.mkString(",").getBytes)

    val protocol = s"akka.${
      if(Try(serverConfig.getBoolean("akka.remote.netty.ssl.enable-ssl")).getOrElse(false)) "ssl." else ""
    }tcp"

    val modifiedAkkaConfig = serverConfig.withValue(
      "akka.cluster.seed-nodes",
      ConfigValueFactory.fromIterable(newSeeds.map{ s =>
        val hostPort = s.split(":")
        new Address(protocol,
          serverConfig.getString("config.cluster.name"),
          hostPort(0),
          hostPort(1).toInt).toString
      })
    )

    val localMember = getLocalMember
    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, pathForMembers)

    val currentMembers = new String(dClient.getData.forPath(pathForMembers))

    val newMembers = (if(localMember.split(":").head != "127.0.0.1"){
      currentMembers.split(",").toSet + localMember
    } else {
      Set(localMember)
    }).map(m => m.trim).filter(_.nonEmpty)

    dClient.setData.forPath(pathForMembers, newMembers.mkString(",").getBytes)
    val modifiedHzConfig = hzConfig.setNetworkConfig(
      hzConfig.getNetworkConfig.setJoin(
        hzConfig.getNetworkConfig.getJoin.setTcpIpConfig(
          hzConfig.getNetworkConfig.getJoin.getTcpIpConfig.setMembers(newMembers.toList)))
        .setPublicAddress(localMember).setPort(localMember.split(":").last.toInt))

    (modifiedAkkaConfig, modifiedHzConfig)
  }

  private def startServiceDiscovery(sdch: SDCH) = {
    // Start ZK connection
    val curatorClient = startDiscoveryClient(sdch)

    // Take subscription leadership with an await call (with timeout)
    val subscriptionLeader = requestSubscriptionLeadership(curatorClient, sdch).get

    // Create future for cluster leadership
    val leadershipFuture = requestClusterLeadership(curatorClient, sdch)

    val (finalConfig, hzConfig) = generateFinalConfig(curatorClient, sdch)

    subscriptionLeader.close

    SDH(curatorClient, finalConfig, hzConfig, leadershipFuture, sdch)
  }

  private def updateClusterSeeds(xCluster: Cluster, h: SDH) = {
    val currentSeeds = xCluster.state.members.filter(_.roles.contains("server")).map(
      m => s"${m.address.host.getOrElse("127.0.0.1")}:${m.address.port.getOrElse("13420")}") + getLocalSeed(xCluster)
    val pathForSeeds = h.sdch.getOrElse(SDCH.SeedsPath, SDCH.DefaultSeedsPath)
    ZKPaths.mkdirs(h.curatorClient.getZookeeperClient.getZooKeeper, pathForSeeds)
    logger.info(s"Updating seeds: ${currentSeeds.mkString(",")}")
    h.curatorClient.setData.forPath(pathForSeeds, currentSeeds.mkString(",").getBytes)
    currentSeeds
  }

  private def updateClusterMembers(h: SDH, hsp: HazelcastSessionProvider) = {

    val pathForMembers = h.sdch.getOrElse(SDCH.ProviderPath, SDCH.DefaultProviderPath)
    ZKPaths.mkdirs(h.curatorClient.getZookeeperClient.getZooKeeper, pathForMembers)

    val updatedMembers = Set(getLocalMember(hsp)) ++ sessionProviderOpt.map{
        case hzSP: HazelcastSessionProvider =>
          hzSP.getHzMembers.to[Set].map{ m =>
            s"${m.getAddress.getHost}:${m.getAddress.getPort}"
          }
        case _ => Set.empty
    }.getOrElse(Set.empty)

    logger.info(s"Updating members: ${updatedMembers.mkString(",")}")
    h.curatorClient.setData.forPath(pathForMembers, updatedMembers.mkString(",").getBytes)

  }

  private def updateSeeds(xCluster: Cluster, hsp: HazelcastSessionProvider, h: SDH) = {

    val sll = new LeaderLatch(h.curatorClient, h.sdch.getOrElse(SDCH.SubscriptionPath, SDCH.DefaultSubscriptionPath))
    sll.start
    sll.await

    updateClusterSeeds(xCluster, h)

    updateClusterMembers(h, hsp)

    sll.close
  }

  private def updateServiceDiscovery(xCluster: Cluster, hsp: HazelcastSessionProvider, s: SDH, aSystem: ActorSystem) = {
    val delay = new FiniteDuration(
      s.sdch.getOrElse(SDCH.ClusterDelayPath, SDCH.DefaultClusterDelay.toString).toLong, TimeUnit.SECONDS)

    import scala.concurrent.ExecutionContext.Implicits.global
    aSystem.scheduler.schedule(delay, delay)(updateSeeds(xCluster, hsp, s))
  }


  def checkMetricsFile(params: Map[String, String], metricsPath: String): Map[String, String] = {
    val metricsFile = new File(metricsPath)
    if(!metricsFile.exists){
      logger.warn(s"Metrics configuration file not found: ${metricsFile.getPath}")
      params - "spark.metrics.conf"
    } else {
      params
    }
  }

}
