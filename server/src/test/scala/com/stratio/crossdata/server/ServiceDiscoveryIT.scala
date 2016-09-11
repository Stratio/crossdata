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

import com.stratio.crossdata.server.discovery.ServiceDiscoveryConfigHelper
import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.apache.curator.CuratorZookeeperClient
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ServiceDiscoveryIT extends BaseXDTest with BeforeAndAfterAll {

  import ServiceDiscoveryConstants._

  val ZkConnectionString = sys.env("XD_ZOOKEEPER_CONNECTION_STRING")

  var testServer: CrossdataServer = _

  override def beforeAll(): Unit = {
    val testConfig = ConfigFactory.empty
      .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(TestHost))
      .withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(AkkaPort))
      .withValue("service-discovery.activated", ConfigValueFactory.fromAnyRef(true))

    testServer = new CrossdataServer(Some(testConfig), Some(Set(s"$TestHost:$HzPort")))

    testServer.start
  }

  override def afterAll(): Unit = {
    testServer.stop
  }

  "A Crossdata Server" should "write its hostname:port in ZK when service discovery is activated" in {
    val curatorClient = CuratorFrameworkFactory.newClient(
      ZkConnectionString,
      new ExponentialBackoffRetry(1000, 3))
    curatorClient.blockUntilConnected
    val currentSeeds = new String(curatorClient.getData.forPath(ServiceDiscoveryConfigHelper.DefaultSeedsPath))

    currentSeeds should be s"$TestHost:$AkkaPort"

    val currentMembers = new String(curatorClient.getData.forPath(ServiceDiscoveryConfigHelper.DefaultProviderPath))

    currentMembers should be s"$TestHost:$HzPort"
  }

}

object ServiceDiscoveryConstants {
  val TestHost = "127.0.0.1"
  val AkkaPort = 13456
  val HzPort = 5789
}
