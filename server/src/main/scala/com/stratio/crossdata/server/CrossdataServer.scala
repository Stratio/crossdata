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
import akka.contrib.pattern.ClusterReceptionistExtension
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.routing.{DefaultResizer, RoundRobinPool}
import akka.stream.ActorMaterializer
import com.stratio.crossdata.common.util.akka.keepalive.KeepAliveMaster
import com.stratio.crossdata.server.actors.{ResourceManagerActor, ServerActor}
import com.stratio.crossdata.server.config.ServerConfig
import com.stratio.crossdata.server.discovery.{ServiceDiscoveryConfigHelper => SDCH, ServiceDiscoveryHelper => SDH}
import com.typesafe.config.ConfigValueFactory
import org.apache.commons.daemon.{Daemon, DaemonContext}
import org.apache.curator.framework.recipes.leader.LeaderLatch
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, HazelcastSessionProvider, XDSessionProvider}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Future, Promise}
import scala.util.Try


class CrossdataServer extends Daemon with ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[CrossdataServer])

  var system: Option[ActorSystem] = None
  var sessionProviderOpt: Option[XDSessionProvider] = None
  var bindingFuture: Option[Future[ServerBinding]] = None

  override def init(p1: DaemonContext): Unit = ()

  def startDiscoveryClient(sdConfig: SDCH): CuratorFramework = {

    val curatorClient = CuratorFrameworkFactory.newClient(
      sdConfig.get[String](SDCH.ServiceDiscoveryUrl, SDCH.ServiceDiscoveryDefaultUrl),
      new ExponentialBackoffRetry(
        SDCH.RetrySleep,
        SDCH.Retries))
    curatorClient.start
    curatorClient
  }

  def requestSubscriptionLeadership(dClient: CuratorFramework, sdc: SDCH) = {

    val sLeader = new LeaderLatch(
      dClient,
      sdc.get[String](SDCH.SubscriptionPath, SDCH.DefaultSubscriptionPath))

    Try(sLeader.await(
      sdc.get[Long](SDCH.SubscriptionTimeoutPath, SDCH.DefaultSubscriptionTimeout), TimeUnit.SECONDS))
      .getOrElse(throw new RuntimeException(
      "Crossdata Server cannot be started because access to service discovery is blocked"))

    sLeader
  }

  def requestClusterLeadership(dClient: CuratorFramework, sdc: SDCH) = {

    val cLeader = new LeaderLatch(
      dClient,
      sdc.get[String](SDCH.ClusterLeaderPath, SDCH.DefaultClusterLeaderPath))

    val leadershipPromise = Promise[Unit]()

    leadershipPromise success cLeader.await

    (cLeader, leadershipPromise)
  }

  def generateFinalConfig(currentClusterLeader: Boolean, dClient: CuratorFramework, sdc: SDCH) = {
    if(currentClusterLeader){
      dClient.delete.forPath(sdc.get[String](SDCH.SeedsPath, SDCH.DefaultSeedsPath))
      config
    } else {
      val zkSeeds = Try(dClient.getData.forPath(sdc.get[String](SDCH.SeedsPath, SDCH.DefaultSeedsPath)))
        .getOrElse(SDCH.DefaultSeedNodes.getBytes)
      val sdSeeds = new String(zkSeeds)
      config.withValue("akka.cluster.seed-nodes", ConfigValueFactory.fromAnyRef(sdSeeds.split(",")))
    }
  }

  // TODO: hasLeadership is not absolutely reliable, a connection listener has to be added to the client
  def checkLeadership(cLeader: LeaderLatch) = {
    cLeader.hasLeadership
  }

  def startServiceDiscovery(sdch: SDCH) = {
    // Start ZK connection
    val curatorClient = startDiscoveryClient(sdch)

    // Take subscription leadership with an await call (with timeout)
    val csLeader = requestSubscriptionLeadership(curatorClient, sdch)

    // Get promise for cluster leadership
    val (clLeader, leadershipPromise) = requestClusterLeadership(curatorClient, sdch)

    // If hasLeadership of cluster, clean zk seeds and keep current config,
    //    otherwise, go to ZK and get seeds to modify the config
    val currentClusterLeader = checkLeadership(clLeader)

    val finalConfig = generateFinalConfig(currentClusterLeader, curatorClient, sdch)

    SDH(curatorClient, finalConfig, leadershipPromise, csLeader, sdch)
  }

  def writeSeeds(xCluster: Cluster, h: SDH) = {
    val currentMembers =
      xCluster.state.members.filter(_.roles.contains("server")).map(m => m.address.toString).toArray
    h.curatorClient.setData().forPath(
      h.sdch.get[String](SDCH.SeedsPath, SDCH.DefaultSeedsPath),
      currentMembers.mkString(",").getBytes)
  }

  def endServiceDiscovery(xCluster: Cluster, s: SDH, aSystem: ActorSystem) = {
    writeSeeds(xCluster, s)

    val delayedInit = new FiniteDuration(
      s.sdch.get[Long](SDCH.ClusterDelayPath, SDCH.DefaultClusterDelay), TimeUnit.SECONDS)

    aSystem.scheduler.scheduleOnce(delayedInit)(writeSeeds(xCluster, s))
  }

  override def start(): Unit = {

    val sparkParams = config.entrySet()
      .map(e => (e.getKey, e.getValue.unwrapped().toString))
      .toMap
      .filterKeys(_.startsWith("config.spark"))
      .map(e => (e._1.replace("config.", ""), e._2))

    val metricsPath = Option(sparkParams.get("spark.metrics.conf"))

    val filteredSparkParams = metricsPath.fold(sparkParams)(m => checkMetricsFile(sparkParams, m.get))

    val sparkContext = new SparkContext(new SparkConf().setAll(filteredSparkParams))

    sessionProviderOpt = Some {
      if (isHazelcastEnabled)
        new HazelcastSessionProvider(sparkContext, config)
      else
        new BasicSessionProvider(sparkContext, config)
    }

    val sessionProvider = sessionProviderOpt
      .getOrElse(throw new RuntimeException("Crossdata Server cannot be started because there is no session provider"))

    // Get service discovery configuration
    val sdConfig = Try(config.getConfig(SDCH.ServiceDiscoveryPrefix)).toOption

    val sdHelper = sdConfig.map{ discoveryConfig =>

      val sdch = new SDCH(discoveryConfig)

      startServiceDiscovery(sdch)

    }

    val finalConfig = sdHelper.fold(config)(_.finalConfig)

    system = Some(ActorSystem(clusterName, finalConfig))

    system.fold(throw new RuntimeException("Actor system cannot be started")) { actorSystem =>

      val xdCluster = Cluster(actorSystem)

      sdHelper.map{ sd =>
        sdConfig.map{ discoveryConfig =>

          // Complete promise and add current seeds
          // Release subscription leadership
          // PROBLEM: Currents seeds are just this current seed
          // SOLUTION: schedulerOnce and get current nodes to be added to zk seeds
          sd.leadershipPromise.future onSuccess {
            case _ =>
              endServiceDiscovery(xdCluster, sd, actorSystem)
          }

          sd.clusterLeader.close
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
      ClusterReceptionistExtension(actorSystem).registerService(clientMonitor)

      val resourceManagerActor = actorSystem.actorOf(ResourceManagerActor.props(Cluster(actorSystem), sessionProvider))
      ClusterReceptionistExtension(actorSystem).registerService(serverActor)
      ClusterReceptionistExtension(actorSystem).registerService(resourceManagerActor)

      implicit val httpSystem = actorSystem
      implicit val materializer = ActorMaterializer()
      val httpServerActor = new CrossdataHttpServer(finalConfig, serverActor, actorSystem)
      val host = finalConfig.getString(ServerConfig.Host)
      val port = finalConfig.getInt(ServerConfig.HttpServerPort)
      bindingFuture = Option(Http().bindAndHandle(httpServerActor.route, host, port))
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

  override def stop(): Unit = {
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

  override def destroy(): Unit = ()

}
