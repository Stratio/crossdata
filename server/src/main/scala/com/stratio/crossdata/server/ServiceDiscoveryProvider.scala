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

import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Address, Cancellable}
import akka.cluster.Cluster
import com.hazelcast.config.{XmlConfigBuilder, Config => HzConfig}
import com.stratio.crossdata.server.discovery.{ServiceDiscoveryConfigHelper => SDCH, ServiceDiscoveryHelper => SDH}
import com.typesafe.config.{Config, ConfigValueFactory}
import org.apache.curator.framework.recipes.leader.LeaderLatch
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.utils.ZKPaths
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.session.{HazelcastSessionProvider, XDSessionProvider}
import org.apache.zookeeper.data.Stat

import scala.collection.JavaConversions._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Try}


trait ServiceDiscoveryProvider {

  protected def logger: Logger

  protected def serverConfig: com.typesafe.config.Config

  protected val hzConfig: HzConfig = new XmlConfigBuilder().build()
  protected[crossdata] var sessionProviderOpt: Option[XDSessionProvider] = None //TODO Remove [crossdata]

  /**
    * Get public address according to the initial configuration of the servers' cluster.
    *
    */
  private def getLocalSeed: String =
    s"${Try(serverConfig.getString("akka.remote.netty.tcp.hostname")).getOrElse("127.0.0.1")}:${Try(serverConfig.getInt("akka.remote.netty.tcp.port")).getOrElse("13420")}"

  /**
    * Get public address according to the initial configuration of the service provider.
    *
    */
  private def getLocalMember: String = {
    val defaultAddr = "127.0.0.1"
    val defaultPort = "5701"
    hzConfig.getNetworkConfig.getJoin.getTcpIpConfig.getMembers.headOption flatMap {
      case addrStr if addrStr.isEmpty => None
      case addrStr if addrStr contains ':' => Some(addrStr)
      case addrStr => Some(s"$addrStr:$defaultPort")
    } getOrElse s"$defaultAddr:$defaultPort"
  }

  /**
    * It starts the subscription to the Crossdata cluster according to the initial configuration
    * and the information provided in the remote server of the service discovery.
    *
    */
  protected def startServiceDiscovery(sdch: SDCH): SDH = {
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

  /**
    * Create and start the curator client.
    *
    */
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

  /**
    * Non-blocking call to acquire cluster leadership. In every moment, there is only one cluster leader.
    * This cluster leader updates the list of current members of the cluster every x seconds (300 by default).
    *
    */
  private def requestClusterLeadership(dClient: CuratorFramework, sdc: SDCH): Future[Unit] = {
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

  /**
    * Wait until subscription leadership is acquired in order to: write down this node as part of the seeds
    * and join to the cluster.
    *
    */
  private def requestSubscriptionLeadership(dClient: CuratorFramework, sdc: SDCH): Try[LeaderLatch] = {

    val sLeaderPath = sdc.getOrElse(SDCH.SubscriptionPath, SDCH.DefaultSubscriptionPath)

    logger.debug(s"Service discovery - subscription leadership path: $sLeaderPath")

    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, sLeaderPath)
    val sLeader = new LeaderLatch(dClient, sLeaderPath)

    sLeader.start

    Try {
      if (sLeader.await(sdc.getOrElse(
        SDCH.SubscriptionTimeoutPath, SDCH.DefaultSubscriptionTimeout.toString).toLong, TimeUnit.SECONDS)) {
        logger.info("Subscription leadership acquired")
        sLeader
      } else {
        throw new RuntimeException("Timeout acquiring subscription leadership")
      }
    } recoverWith {
      case e => Failure(new RuntimeException(s"Subscription leadership couldn't be acquired: ${e.getMessage}"))
    }
  }

  /**
    * Generate contact points in the config according to the content of the remote server of the service discovery.
    * In addition, it adds itself to the content of the contact points before generating the final config.
    *
    */
  private def generateFinalConfig(dClient: CuratorFramework, sdc: SDCH): (Config, HzConfig) = {

    val pathForSeeds = sdc.getOrElse(SDCH.SeedsPath, SDCH.DefaultSeedsPath)
    logger.debug(s"Service Discovery - seeds path: $pathForSeeds")

    val pathForMembers = sdc.getOrElse(SDCH.ProviderPath, SDCH.DefaultProviderPath)
    logger.debug(s"Service Discovery - members path: $pathForMembers")

    val localSeed = getLocalSeed
    ZKPaths.mkdirs(dClient.getZookeeperClient.getZooKeeper, pathForSeeds)
    val currentSeeds = new String(dClient.getData.forPath(pathForSeeds))
    val newSeeds = (localSeed +: currentSeeds.split(",")).map(m => m.trim).filter(_.nonEmpty)
    dClient.setData.forPath(pathForSeeds, newSeeds.mkString(",").getBytes)

    logger.info(s"Service discovery config - Cluster seeds: ${newSeeds.mkString(",")}")

    val protocol = s"akka.${
      if (Try(serverConfig.getBoolean("akka.remote.netty.ssl.enable-ssl")).getOrElse(false)) "ssl." else ""
    }tcp"

    val modifiedAkkaConfig = serverConfig.withValue(
      "akka.cluster.seed-nodes",
      ConfigValueFactory.fromIterable(newSeeds.toSeq.map { s =>
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

    val newMembers = (if (localMember.split(":").head != "127.0.0.1") {
      localMember +: currentMembers.split(",")
    } else {
      Array(localMember)
    }).map(m => m.trim).filter(_.nonEmpty)

    logger.info(s"Service discovery config - Provider members: ${newMembers.mkString(",")}")

    dClient.setData.forPath(pathForMembers, newMembers.mkString(",").getBytes)
    val modifiedHzConfig = hzConfig.setNetworkConfig(
      hzConfig.getNetworkConfig.setJoin(
        hzConfig.getNetworkConfig.getJoin.setTcpIpConfig(
          hzConfig.getNetworkConfig.getJoin.getTcpIpConfig.setMembers(newMembers.toList)))
        .setPublicAddress(localMember).setPort(localMember.split(":").last.toInt))

    (modifiedAkkaConfig, modifiedHzConfig)
  }

  /**
    * It creates a scheduled task (every x seconds, 300 by default) that updates the members of the cluster
    * on the remote server of the server discovery.
    *
    */
  protected def updateServiceDiscovery(xCluster: Cluster, hsp: HazelcastSessionProvider, s: SDH, aSystem: ActorSystem): Cancellable = {
    val delay = new FiniteDuration(
      s.sdch.getOrElse(SDCH.ClusterDelayPath, SDCH.DefaultClusterDelay.toString).toLong, TimeUnit.SECONDS)

    import scala.concurrent.ExecutionContext.Implicits.global
    aSystem.scheduler.schedule(delay, delay)(updateSeeds(xCluster, hsp, s))
  }

  /**
    * It acquires the subscription leadership (in order to avoid race conditions with new members
    * joining to the cluster at the same time) and triggers the methods to update the contact points for
    * the members of the Crossdata cluster and the members of the service provider.
    *
    */
  private def updateSeeds(xCluster: Cluster, hsp: HazelcastSessionProvider, h: SDH): Unit = {
    val sll = new LeaderLatch(h.curatorClient, h.sdch.getOrElse(SDCH.SubscriptionPath, SDCH.DefaultSubscriptionPath))
    sll.start
    sll.await
    updateClusterSeeds(xCluster, h)
    updateClusterMembers(h, hsp)
    sll.close
  }

  /**
    * Overrides the cluster seeds on the remote server of the service discovery according to the cluster state.
    *
    */
  private def updateClusterSeeds(xCluster: Cluster, h: SDH): String = {
    val currentSeeds = getLocalSeed + xCluster.state.members.filter(_.roles.contains("server")).map(
      m => s"${m.address.host.getOrElse("127.0.0.1")}:${m.address.port.getOrElse("13420")}")
    val pathForSeeds = h.sdch.getOrElse(SDCH.SeedsPath, SDCH.DefaultSeedsPath)
    ZKPaths.mkdirs(h.curatorClient.getZookeeperClient.getZooKeeper, pathForSeeds)
    logger.info(s"Updating seeds: ${currentSeeds.mkString(",")}")
    h.curatorClient.setData.forPath(pathForSeeds, currentSeeds.mkString(",").getBytes)
    currentSeeds
  }

  /**
    * Overrides the service provider members on the remote server of the service discovery according to
    * the current members.
    *
    */
  private def updateClusterMembers(h: SDH, hsp: HazelcastSessionProvider): Stat = {

    val pathForMembers = h.sdch.getOrElse(SDCH.ProviderPath, SDCH.DefaultProviderPath)
    ZKPaths.mkdirs(h.curatorClient.getZookeeperClient.getZooKeeper, pathForMembers)

    val updatedMembers = getLocalMember +: sessionProviderOpt.map {
      case hzSP: HazelcastSessionProvider =>
        hzSP.getHzMembers.to[Seq].map { m =>
          s"${m.getAddress.getHost}:${m.getAddress.getPort}"
        }
      case _ => Seq.empty
    }.getOrElse(Seq.empty)

    logger.info(s"Updating members: ${updatedMembers.mkString(",")}")
    h.curatorClient.setData.forPath(pathForMembers, updatedMembers.mkString(",").getBytes)
  }

}
