/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.server.actors

import java.util.Random

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.routing.DefaultResizer
import com.stratio.crossdata.common.{ExecuteQuery, Message}
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.crossdata.XDContext


object ServerActor {
  def props(cluster: Cluster): Props = Props(new ServerActor(cluster))
}

class ServerActor(cluster: Cluster) extends Actor with ServerConfig {
  override lazy val logger = Logger.getLogger(classOf[ServerActor])
  val random = new Random
  val hostname = config.getString("akka.remote.netty.tcp.hostname")
  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)

  // spark configuration parameters
  val sparkContextParameters = Map("sparkMaster" -> sparkMaster, "sparkCores" -> sparkCores,
    "sparkDriverMemory" -> sparkDriverMemory, "sparkExecutorMemory" -> sparkExecutorMemory)
  lazy val sc = initContext()
  lazy val xdContext = new XDContext(sc)

  val executorActorRef = context.actorOf(Props(classOf[ExecutorActor], cluster, xdContext), "ExecutorActor")

  // For testing purposes
  cluster.subscribe(self, classOf[MemberUp], classOf[CurrentClusterState], classOf[UnreachableMember],
    classOf[MemberRemoved], classOf[MemberExited])


  def receive: Receive = {

    case Message(query) => {
      logger.debug("Query received!")
      //val queryAndParams = List(query, sparkContextParameters)
      executorActorRef forward ExecuteQuery(query)
    }
    case c: ClusterDomainEvent => {
      println("INFO: " + c)
    }
    case s: CurrentClusterState => {
      println("INFO: " + s)
    }
    case a: Any => {
      println("Error: " + a)
      logger.error("Unknown message received by ServerActor");
      //sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

  def initContext(): SparkContext = {
    new SparkContext(new SparkConf()
      .setAppName("Crossdata")
      .setMaster(sparkMaster)
      .setAll(List(
      sparkDriverMemory,
      sparkExecutorMemory,
      sparkCores).filter(config.hasPath).map(k => k -> config.getString(k))))
  }


  override def postStop(): Unit = xdContext.sc.stop()

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = xdContext.sc.stop()
}
