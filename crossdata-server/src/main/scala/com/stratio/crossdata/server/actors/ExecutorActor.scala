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

import akka.actor.ActorLogging
import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import com.stratio.crossdata.common.ExecuteQuery
import com.stratio.crossdata.server.config.ServerConfig



import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.crossdata.XDContext

object ExecutorActor {
  def props(cluster: Cluster): Props = Props(new ExecutorActor(cluster))
}

class ExecutorActor(cluster: Cluster) extends Actor with ActorLogging with ServerConfig{
  lazy val logger = Logger.getLogger(classOf[ExecutorActor])

  def receive: Receive = {
    case ExecuteQuery(query)=> {
      val sc = initContext()
      val xdContext = new XDContext(sc)
      xdContext.sql(query)
    }
    case _=> logger.error("Something is going wrong!")

  }

  def initContext(): SparkContext = {
    new SparkContext(new SparkConf()
      .setMaster(sparkMaster)
      .setAll(List(
      sparkDriverMemory,
      sparkExecutorMemory,
      sparkCores).filter(config.hasPath).map(k => k -> config.getString(k))))
  }
}

