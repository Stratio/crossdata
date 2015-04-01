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

import akka.actor.{Actor, Props, ReceiveTimeout}
import akka.cluster.Cluster
import akka.routing.{RoundRobinPool, DefaultResizer, RoundRobinRouter}
import com.stratio.crossdata.common.ask.{Command, Connect, Query}
import com.stratio.crossdata.common.result.{ConnectResult, DisconnectResult, Result}
import com.stratio.crossdata.communication.Disconnect
import com.stratio.crossdata.core.engine.Engine
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger

object ServerActor {
  def props(engine: Engine,cluster: Cluster): Props = Props(new ServerActor(engine,cluster))
}

class ServerActor(engine: Engine,cluster: Cluster) extends Actor with ServerConfig {
  override lazy val logger = Logger.getLogger(classOf[ServerActor])
  val random=new Random
  val hostname=config.getString("akka.remote.netty.tcp.hostname")
  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)

  val loadWatcherActorRef = context.actorOf(LoadWatcherActor.props(hostname), "loadWatcherActor")
  val connectorManagerActorRef = context.actorOf( RoundRobinPool(num_connector_manag_actor, Some(resizer))
     .props(Props(classOf[ConnectorManagerActor], cluster)), "ConnectorManagerActor")
  val coordinatorActorRef = context.actorOf( RoundRobinPool(num_coordinator_actor, Some(resizer))
     .props(Props(classOf[CoordinatorActor], connectorManagerActorRef, engine.getCoordinator)), "CoordinatorActor")
  val plannerActorRef = context.actorOf( RoundRobinPool(num_planner_actor, Some(resizer))
     .props(Props(classOf[PlannerActor], coordinatorActorRef, engine.getPlanner)), "PlannerActor")
  val validatorActorRef = context.actorOf( RoundRobinPool(num_validator_actor, Some(resizer))
     .props(Props(classOf[ValidatorActor], plannerActorRef, engine.getValidator)), "ValidatorActor")
  val parserActorRef = context.actorOf( RoundRobinPool(num_parser_actor, Some(resizer))
     .props(Props(classOf[ParserActor], validatorActorRef, engine.getParser)), "ParserActor")
  val APIActorRef = context.actorOf( RoundRobinPool(num_api_actor, Some(resizer))
     .props(Props(classOf[APIActor], engine.getAPIManager)), "APIActor")

  def receive : Receive= {
    case "watchload"=>
      loadWatcherActorRef forward "watchload"
    case query: Query =>{
      logger.info("Query received: " + query.statement.toString)
      parserActorRef forward query
    }
    case Connect(user) => {
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
    }
    case _ => {
      logger.error("Unknown message received by ServerActor");
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }
}
