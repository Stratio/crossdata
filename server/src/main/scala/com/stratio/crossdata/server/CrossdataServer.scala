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
import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.http.scaladsl.Http.ServerBinding
import akka.routing.{DefaultResizer, RoundRobinPool}
import com.stratio.crossdata.common.util.akka.keepalive.KeepAliveMaster
import com.stratio.crossdata.server.actors.{ResourceManagerActor, ServerActor}
import com.stratio.crossdata.server.config.ServerConfig
import com.stratio.crossdata.server.discovery.{ZkConnectionState, ServiceDiscoveryConfigHelper => SDCH, ServiceDiscoveryHelper => SDH}
import com.typesafe.config.{Config, ConfigValueFactory}
import org.apache.curator.framework.recipes.leader.{LeaderLatch, LeaderLatchListener}
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.utils.ZKPaths
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, HazelcastSessionProvider, XDSessionProvider}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._
import scala.collection.immutable.SortedSet
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.{Random, Try}


class CrossdataServer(progrConfig: Option[Config] = None) extends ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[CrossdataServer])

  var system: Option[ActorSystem] = None
  var sessionProviderOpt: Option[XDSessionProvider] = None
  var bindingFuture: Option[Future[ServerBinding]] = None

  val serverConfig = progrConfig match {
    case Some(c) => c.withFallback(config)
    case None => config
  }

  private def startDiscoveryClient(sdConfig: SDCH): CuratorFramework = {

    val curatorClient = CuratorFrameworkFactory.newClient(
      sdConfig.get(SDCH.ServiceDiscoveryUrl, SDCH.ServiceDiscoveryDefaultUrl),
      new ExponentialBackoffRetry(
        SDCH.RetrySleep,
        SDCH.Retries))
    curatorClient.start
    curatorClient.blockUntilConnected
    curatorClient
  }

  private def requestSubscriptionLeadership(dClient: CuratorFramework, sdc: SDCH) = {

    val sLeaderPath = sdc.get(SDCH.SubscriptionPath, SDCH.DefaultSubscriptionPath)

    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, sLeaderPath)

    val sLeader = new LeaderLatch(
      dClient,
      sLeaderPath)

    sLeader.start

    Try(sLeader.await(
      sdc.get(SDCH.SubscriptionTimeoutPath, SDCH.DefaultSubscriptionTimeout.toString).toLong, TimeUnit.SECONDS))
      .getOrElse(throw new RuntimeException(
      "Crossdata Server cannot be started because access to service discovery is blocked"))

    logger.info("Subscription leadership acquired")

    sLeader
  }

  private def requestClusterLeadership(dClient: CuratorFramework, sdc: SDCH) = {

    val cLeaderPath = sdc.get(SDCH.ClusterLeaderPath, SDCH.DefaultClusterLeaderPath)

    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, cLeaderPath)

    val randomId = math.abs(Random.nextLong())

    logger.debug(s"My cluster leadership ID: $randomId")

    val cLeader = new LeaderLatch(
      dClient,
      cLeaderPath,
      randomId.toString)

    cLeader.start

    import scala.concurrent.ExecutionContext.Implicits.global
    val leadershipFuture = Future {
      cLeader.await
    }

    leadershipFuture onSuccess {
      case _ => logger.info("Cluster leadership acquired")
    }

    (cLeader, leadershipFuture)
  }

  private def generateFinalConfig(clusterLeader: LeaderLatch, dClient: CuratorFramework, sdc: SDCH) = {

    // If hasLeadership of cluster, clean zk seeds and keep current config,
    //    otherwise, go to ZK and get seeds to modify the config
    val currentClusterLeader = gotLeadership(clusterLeader)

    if(currentClusterLeader){
      val pathForSeeds = sdc.get(SDCH.SeedsPath, SDCH.DefaultSeedsPath)
      Try(dClient.delete.deletingChildrenIfNeeded.forPath(pathForSeeds))
        .getOrElse(logger.debug(s"ZK path '$pathForSeeds' wasn't deleted because it doesn't exist"))
      serverConfig
    } else {
      val seedsPath = sdc.get(SDCH.SeedsPath, SDCH.DefaultSeedsPath)
      ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, seedsPath)
      val zkSeeds = Try(dClient.getData.forPath(seedsPath)).getOrElse(SDCH.DefaultSeedNodes.getBytes)
      val sdSeeds = new String(zkSeeds)
      serverConfig.withValue("akka.cluster.seed-nodes", ConfigValueFactory.fromIterable(sdSeeds.split(",").toList))
    }
  }

  // hasLeadership is not absolutely reliable, a connection listener has to be used to check connection state
  private def gotLeadership(cLeader: LeaderLatch) = {
    cLeader.hasLeadership && ZkConnectionState.isConnected
  }

  private def startServiceDiscovery(sdch: SDCH) = {
    // Start ZK connection
    val curatorClient = startDiscoveryClient(sdch)

    // Take subscription leadership with an await call (with timeout)
    val csLeader = requestSubscriptionLeadership(curatorClient, sdch)

    // Get promise for cluster leadership
    val (clLeader, leadershipFuture) = requestClusterLeadership(curatorClient, sdch)

    val finalConfig = generateFinalConfig(clLeader, curatorClient, sdch)

    SDH(curatorClient, finalConfig, leadershipFuture, clLeader, csLeader, sdch)
  }

  private def writeSeeds(xCluster: Cluster, h: SDH) = {

    logger.debug(s"Subscription leadership state: ${h.subscriptionLeader.getState}")

    val sll = if(h.subscriptionLeader.getState == LeaderLatch.State.CLOSED){
      val l = new LeaderLatch(h.curatorClient, h.sdch.get(SDCH.SubscriptionPath, SDCH.DefaultSubscriptionPath))
      l.start
      l.await
      l
    } else {
      h.subscriptionLeader
    }

    val currentMembers =
      (xCluster.state.members.filter(_.roles.contains("server")).map(m => m.address.toString)
        ++ SortedSet(xCluster.selfAddress.toString))

    val pathForSeeds = h.sdch.get(SDCH.SeedsPath, SDCH.DefaultSeedsPath)

    ZKPaths.mkdirs(h.curatorClient.getZookeeperClient.getZooKeeper, pathForSeeds)

    logger.info(s"Updating seeds: ${currentMembers.mkString(",")}")

    h.curatorClient.setData().forPath(pathForSeeds, currentMembers.mkString(",").getBytes)

    sll.close
  }

  private def endServiceDiscovery(xCluster: Cluster, s: SDH, aSystem: ActorSystem) = {
    writeSeeds(xCluster, s)

    val delayedInit = new FiniteDuration(
      s.sdch.get(SDCH.ClusterDelayPath, SDCH.DefaultClusterDelay.toString).toLong, TimeUnit.SECONDS)

    import scala.concurrent.ExecutionContext.Implicits.global

    aSystem.scheduler.scheduleOnce(delayedInit)(writeSeeds(xCluster, s))
  }

  def start(): Unit = {

    val sparkParams = serverConfig.entrySet()
      .map(e => (e.getKey, e.getValue.unwrapped().toString))
      .toMap
      .filterKeys(_.startsWith("config.spark"))
      .map(e => (e._1.replace("config.", ""), e._2))

    val metricsPath = Option(sparkParams.get("spark.metrics.conf"))

    val filteredSparkParams = metricsPath.fold(sparkParams)(m => checkMetricsFile(sparkParams, m.get))

    val sparkContext = new SparkContext(new SparkConf().setAll(filteredSparkParams))

    sessionProviderOpt = Some {
      if (isHazelcastEnabled)
        new HazelcastSessionProvider(sparkContext, serverConfig)
      else
        new BasicSessionProvider(sparkContext, serverConfig)
    }

    val sessionProvider = sessionProviderOpt
      .getOrElse(throw new RuntimeException("Crossdata Server cannot be started because there is no session provider"))

    // Get service discovery configuration
    val sdConfig = Try(serverConfig.getConfig(SDCH.ServiceDiscoveryPrefix)).toOption

    val sdEnabled = sdConfig.fold(false){ c => Try(c.getBoolean("activated")).getOrElse(false) }

    val sdHelper = if(sdEnabled){
      logger.info("Service discovery enabled")
      val sdch = new SDCH(sdConfig.get)
      Some(startServiceDiscovery(sdch))
    } else {
      None
    }

    val finalConfig = sdHelper.fold(serverConfig)(_.finalConfig)

    finalConfig.entrySet.filter{ e =>
      e.getKey.contains("seed-nodes")
    }.foreach{ e =>
      logger.info(s"SEED NODES: ${e.getValue}")
    }

    system = Some(ActorSystem(clusterName, finalConfig))

    system.fold(throw new RuntimeException("Actor system cannot be started")) { actorSystem =>

      val xdCluster = Cluster(actorSystem)

      sdHelper.map{ sd =>

        // Complete future and add current seeds
        // Release subscription leadership
        // PROBLEM: Currents seeds are just this current seed
        // SOLUTION: schedulerOnce and get current nodes to be added to zk seeds
        import scala.concurrent.ExecutionContext.Implicits.global
        sd.leadershipFuture onSuccess {
          case _ =>
            endServiceDiscovery(xdCluster, sd, actorSystem)
        }

        if(!gotLeadership(sd.clusterLeader)){
          sd.subscriptionLeader.close
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

  def checkMetricsFile(params: Map[String, String], metricsPath: String): Map[String, String] = {
    val metricsFile = new File(metricsPath)
    if(!metricsFile.exists){
      logger.warn(s"Metrics configuration file not found: ${metricsFile.getPath}")
      params - "spark.metrics.conf"
    } else {
      params
    }
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

}
