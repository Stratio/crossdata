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

import akka.actor.{ActorSystem, Address}
import akka.cluster.Cluster
import com.hazelcast.config.{XmlConfigBuilder, Config => HzConfig}
import com.stratio.crossdata.server.discovery.{ServiceDiscoveryConfigHelper => SDCH, ServiceDiscoveryHelper => SDH}
import com.typesafe.config.ConfigValueFactory
import org.apache.curator.framework.recipes.leader.LeaderLatch
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.utils.ZKPaths
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.session.{HazelcastSessionProvider, XDSessionProvider}

import scala.collection.JavaConversions._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Try}


trait ServiceDiscoveryProvider {

  protected def logger: Logger

  protected def serverConfig: com.typesafe.config.Config

  protected val hzConfig: HzConfig = new XmlConfigBuilder().build()
  protected[crossdata] var sessionProviderOpt: Option[XDSessionProvider] = None //TODO Remove [crossdata]

  private def getLocalSeed: String =
    s"${Try(serverConfig.getString("akka.remote.netty.tcp.hostname")).getOrElse("127.0.0.1")}:${Try(serverConfig.getInt("akka.remote.netty.tcp.port")).getOrElse("13420")}"

  private def getLocalMember: String = {
    val defaultAddr = "127.0.0.1"
    val defaultPort = "5701"
    hzConfig.getNetworkConfig.getJoin.getTcpIpConfig.getMembers.headOption flatMap {
      case addrStr if addrStr.isEmpty => None
      case addrStr if addrStr contains ':' => Some(addrStr)
      case addrStr => Some(s"$addrStr:$defaultPort")
    } getOrElse s"$defaultAddr:$defaultPort"
  }

  protected def startServiceDiscovery(sdch: SDCH) = {
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
    * Create and start the curator client
    *
    * @param sdConfig
    * @return
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
    * Waiting to try to be the leader???
    *
    * @param dClient
    * @param sdc
    * @return
    */
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


  /**
    * Trying to get who is the leader???
    *
    * @param dClient
    * @param sdc
    * @return
    */
  private def requestSubscriptionLeadership(dClient: CuratorFramework, sdc: SDCH) = {

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

    logger.info(s"Service discovery config - Cluster seeds: ${newSeeds.mkString(",")}")

    val protocol = s"akka.${
      if (Try(serverConfig.getBoolean("akka.remote.netty.ssl.enable-ssl")).getOrElse(false)) "ssl." else ""
    }tcp"

    val modifiedAkkaConfig = serverConfig.withValue(
      "akka.cluster.seed-nodes",
      ConfigValueFactory.fromIterable(newSeeds.map { s =>
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
      currentMembers.split(",").toSet + localMember
    } else {
      Set(localMember)
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

  private def updateClusterMembers(h: SDH, hsp: HazelcastSessionProvider) = {

    val pathForMembers = h.sdch.getOrElse(SDCH.ProviderPath, SDCH.DefaultProviderPath)
    ZKPaths.mkdirs(h.curatorClient.getZookeeperClient.getZooKeeper, pathForMembers)

    val updatedMembers = Set(getLocalMember) ++ sessionProviderOpt.map {
      case hzSP: HazelcastSessionProvider =>
        hzSP.getHzMembers.to[Set].map { m =>
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

  private def updateClusterSeeds(xCluster: Cluster, h: SDH) = {
    val currentSeeds = getLocalSeed + xCluster.state.members.filter(_.roles.contains("server")).map(
      m => s"${m.address.host.getOrElse("127.0.0.1")}:${m.address.port.getOrElse("13420")}")
    val pathForSeeds = h.sdch.getOrElse(SDCH.SeedsPath, SDCH.DefaultSeedsPath)
    ZKPaths.mkdirs(h.curatorClient.getZookeeperClient.getZooKeeper, pathForSeeds)
    logger.info(s"Updating seeds: ${currentSeeds.mkString(",")}")
    h.curatorClient.setData.forPath(pathForSeeds, currentSeeds.mkString(",").getBytes)
    currentSeeds
  }

  protected def updateServiceDiscovery(xCluster: Cluster, hsp: HazelcastSessionProvider, s: SDH, aSystem: ActorSystem) = {
    val delay = new FiniteDuration(
      s.sdch.getOrElse(SDCH.ClusterDelayPath, SDCH.DefaultClusterDelay.toString).toLong, TimeUnit.SECONDS)

    import scala.concurrent.ExecutionContext.Implicits.global
    aSystem.scheduler.schedule(delay, delay)(updateSeeds(xCluster, hsp, s))
  }

}
