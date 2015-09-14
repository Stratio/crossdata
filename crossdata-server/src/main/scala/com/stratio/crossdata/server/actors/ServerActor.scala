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
import akka.actor.{OneForOneStrategy, Actor, Props}
import akka.cluster.Cluster
import akka.routing.DefaultResizer
import com.stratio.crossdata.common.Message
import com.stratio.crossdata.server.config.ServerConfig

import org.apache.log4j.Logger
import akka.actor.SupervisorStrategy.{Resume, Stop}
import scala.concurrent.duration._


object ServerActor {
  def props(cluster: Cluster): Props = Props(new ServerActor(cluster))

}

class ServerActor(cluster: Cluster) extends Actor with ServerConfig {
  override lazy val logger = Logger.getLogger(classOf[ServerActor])
  val random = new Random
  val hostname = config.getString("akka.remote.netty.tcp.hostname")
  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)

  val executorActorRef = context.actorOf(Props(classOf[ExecutorActor], cluster), "ExecutorActor")


  //val sparkContextParameters:Map[String, String]= Map()
  val sparkContextParameters = Map("sparkMaster" -> sparkMaster, "sparkCores" -> sparkCores,
    "sparkDriverMemory" -> sparkDriverMemory, "sparkExecutorMemory" -> sparkExecutorMemory)

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: Any => Resume
    }

  def receive: Receive = {

    case Message(query) => {
      logger.info("Query received!")
      val queryAndParams = List(query, sparkContextParameters)
      executorActorRef forward queryAndParams
    }
    case _ => {
      logger.error("Unknown message received by ServerActor");
      //sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }
}
