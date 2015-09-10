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

import java.util.{Random, UUID}

import akka.actor.{OneForOneStrategy, Actor, Props, ReceiveTimeout}
import akka.cluster.Cluster
import akka.routing.{RoundRobinPool, DefaultResizer}

import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import akka.actor.SupervisorStrategy.{Resume, Stop}
import scala.concurrent.duration._

object ServerActor {
  def props(cluster: Cluster): Props = Props(new ServerActor(cluster))
}

class ServerActor(cluster: Cluster) extends Actor with ServerConfig {
  override lazy val logger = Logger.getLogger(classOf[ServerActor])
  val random=new Random
  val hostname=config.getString("akka.remote.netty.tcp.hostname")
  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)



  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: Any => Resume
    }

  def receive : Receive={
    /*case "watchload"=>
      loadWatcherActorRef forward "watchload"
    case query: Query =>{
      logger.info("Query received: " + query.statement.toString)
      parserActorRef forward query
    }
    case Connect(user,pass) => {
      logger.info(s"Welcome $user! from  ${sender.path.address} ( host =${sender.path.address}) ")
      sender ! ConnectResult.createConnectResult(UUID.randomUUID().toString)
    }
    case Disconnect(user) => {
      logger.info("Goodbye " + user + ".")
      sender ! DisconnectResult.createDisconnectResult(user)
    }
    case cmd: Command =>{
      logger.info("API Command call received " + cmd.commandType)
      APIActorRef forward cmd
    }
    case ReceiveTimeout => {
      logger.warn("ReceiveTimeout")
      //TODO Process ReceiveTimeout

    }*/
    case _ => {
      logger.error("Unknown message received by ServerActor");
      //sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }
}
