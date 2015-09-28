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

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}


object ServerActor {
  def props(cluster: Cluster): Props = Props(new ServerActor(cluster))
}

class ServerActor(cluster: Cluster) extends Actor with ServerConfig {

  import com.stratio.crossdata.server.actors.ExecutorActor._

  override lazy val logger = Logger.getLogger(classOf[ServerActor])

  val xdContext = new XDContext(initSparkContext)

  val executorActorRef = context.actorOf(Props(classOf[ExecutorActor], cluster, xdContext), "ExecutorActor")


  def receive: Receive = {

    case sqlCommand @ SQLCommand(query,_) =>
      logger.debug(s"Query received ${sqlCommand.queryId}: ${sqlCommand.query}")
      executorActorRef forward ExecuteQuery(sqlCommand)

    case any =>
      logger.error(s"Unknown message received by ServerActor: $any")

  }

  def initSparkContext: SparkContext =
    new SparkContext(new SparkConf()
      .setAppName("Crossdata")
      .setMaster(sparkMaster)
      .setAll(
        List(sparkDriverMemory, sparkExecutorMemory, sparkCores).filter(config.hasPath).map(k => k -> config.getString(k))
      ))


  override def preRestart(reason: Throwable, message: Option[Any]): Unit =
    xdContext.sc.stop()

  override def postStop(): Unit =
    xdContext.sc.stop()

}
