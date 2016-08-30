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

import com.stratio.crossdata.server.CrossdataServer
import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.JavaConversions._
import scala.sys.process._

object ServiceDiscoveryIT {
  val baseConfigMap = Map(
    "service-discovery.url" -> "127.0.0.1:2181",
    "service-discovery.cluster-subscription.path" -> "/crossdata/discovery/subscription/leader",
    "service-discovery.cluster-subscription.timeout" -> 10,
    "service-discovery.cluster-leader.path" -> "/crossdata/discovery/cluster/leader",
    "service-discovery.cluster-leader.scheduler.delay" -> 10,
    "service-discovery.seeds.path" -> "/crossdata/discovery/seeds",
    "config.spark.master" -> "spark://Miguels-MacBook-Pro.local:7077",
    "config.spark.jars" -> "/Users/miguelangelfernandezdiaz/workspace/myCrossdata/server/target/2.11/crossdata-server_2.11-1.6.0-SNAPSHOT-jar-with-dependencies.jar",
    "config.spark.driver.allowMultipleContexts" -> true
  )

}

@RunWith(classOf[JUnitRunner])
class ServiceDiscoveryIT extends BaseXDTest {

  import ServiceDiscoveryIT._

  lazy val logger = Logger.getLogger(classOf[ServiceDiscoveryIT])

  "3 Crossdata servers" should "form a Cluster through the Service Discovery" in {

    val line = s"""mvn exec:java
                   |-Dexec.mainClass=\"com.stratio.crossdata.server.CrossdataApplication\"
                   |-Dexec.args=\"'--service-discovery.url' '127.0.0.1:2181'\""""
      .replace(System.lineSeparator, " ").replace("|", " ").replace("\t", " ").replaceAll("( )+", " ")

    println(s"COMMAND: $line")

    val process1 = Process(line).lines

    process1.foreach(println)

    /*
    val server1 = new CrossdataServer(
      Some(
        ConfigFactory.parseMap(
          baseConfigMap ++ Map(
            "akka.remote.netty.tcp.port" -> 13420,
            "config.spark.ui.port" -> 4040))))
    server1.start

    val server2 = new CrossdataServer(
      Some(
        ConfigFactory.parseMap(
          baseConfigMap ++ Map(
            "akka.remote.netty.tcp.port" -> 13421,
            "config.spark.ui.port" -> 4041))))
    server2.start

    val server3 = new CrossdataServer(
      Some(
        ConfigFactory.parseMap(
          baseConfigMap ++ Map(
            "akka.remote.netty.tcp.port" -> 13422,
            "config.spark.ui.port" -> 4042))))
    server3.start

    Thread.sleep(10000)

    server1.crossdataCluster.map { c =>
      val clusterMembers = c.state.members
      println(s"MEMBER'S SIZE: ${clusterMembers.size}")
      clusterMembers.foreach{ m =>
        println(s"MEMBER: ${m.uniqueAddress}")
      }
    }

    Thread.sleep(10000)

    */

  }

}
