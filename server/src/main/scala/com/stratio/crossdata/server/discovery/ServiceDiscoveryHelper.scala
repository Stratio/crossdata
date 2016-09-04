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
package com.stratio.crossdata.server.discovery

import com.typesafe.config.Config
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.recipes.leader.LeaderLatch

import scala.concurrent.Future
import scala.util.Try

object ServiceDiscoveryConfigHelper {
  val ServiceDiscoveryPrefix = "service-discovery"
  val ServiceDiscoveryUrl = "url"
  val ServiceDiscoveryDefaultUrl = "127.0.0.1:2181"
  val RetrySleep = 1000
  val Retries = 3
  val SubscriptionPrefix = "cluster-subscription"
  val SubscriptionPath = s"$SubscriptionPrefix.path"
  val DefaultSubscriptionPath = "/crossdata/discovery/subscription/leader"
  val SubscriptionTimeoutPath = s"$SubscriptionPrefix.timeout"
  val DefaultSubscriptionTimeout = 10
  val ClusterLeaderPrefix = "cluster-leader"
  val ClusterLeaderPath = s"$ClusterLeaderPrefix.path"
  val DefaultClusterLeaderPath = "/crossdata/discovery/cluster/leader"
  val SeedsPath = "seeds"
  val DefaultSeedsPath = "/crossdata/discovery/seeds"
  val DefaultSeedNodes = "akka.tcp://CrossdataServerCluster@127.0.0.1:13420"
  val ClusterDelayPath = s"$ClusterLeaderPrefix.scheduler.delay"
  val DefaultClusterDelay = 10
}

case class ServiceDiscoveryConfigHelper(sdConfig: Config) {

  def get(path: String, default: String) = {
    Try(sdConfig.getString(path)).getOrElse(default)
  }

}

case class ServiceDiscoveryHelper(
                                   curatorClient: CuratorFramework,
                                   finalConfig: Config,
                                   leadershipFuture: Future[Unit],
                                   clusterLeader: LeaderLatch,
                                   subscriptionLeader: LeaderLatch,
                                   sdch: ServiceDiscoveryConfigHelper){
}
