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

import akka.actor.{Props, Actor, ReceiveTimeout}
import akka.cluster.{Cluster, MemberStatus}
import akka.routing.{RoundRobinPool, DefaultResizer}
import com.stratio.crossdata.common.ask.{Command, Connect, Query}
import com.stratio.crossdata.common.result.{ConnectResult, DisconnectResult, Result}
import com.stratio.crossdata.communication.{Disconnect, ReroutedCommand, ReroutedQuery}
import com.stratio.crossdata.core.engine.Engine
import com.stratio.crossdata.core.loadWatcher.LoadWatcherManager
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import collection.JavaConversions._

object ServerActor {
  def props(engine: Engine,cluster: Cluster): Props = Props(new ServerActor(engine,cluster))
}

class ServerActor(engine: Engine, cluster: Cluster) extends Actor with ServerConfig {
  override lazy val logger = Logger.getLogger(classOf[ServerActor])
  val random=new Random
  val hostname=config.getString("akka.remote.netty.tcp.hostname")

  val balancing: String = config.getString("config.cluster.balancing")

  val loadWatcherActorRef = context.actorOf(LoadWatcherActor.props(hostname), "loadWatcherActor")

  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)

  val connectorManagerActorRef = context.actorOf(
    RoundRobinPool(num_connector_manag_actor, Some(resizer))
      .props(Props(classOf[ConnectorManagerActor], cluster)),
    "ConnectorManagerActor")
  val coordinatorActorRef = context.actorOf(
    RoundRobinPool(num_coordinator_actor, Some(resizer))
      .props(Props(classOf[CoordinatorActor], connectorManagerActorRef, engine.getCoordinator)),
    "CoordinatorActor")
  val plannerActorRef = context.actorOf(
    RoundRobinPool(num_planner_actor, Some(resizer))
      .props(Props(classOf[PlannerActor], coordinatorActorRef, engine.getPlanner)),
    "PlannerActor")
  val validatorActorRef = context.actorOf(
    RoundRobinPool(num_validator_actor, Some(resizer))
      .props(Props(classOf[ValidatorActor], plannerActorRef, engine.getValidator)),
    "ValidatorActor")
  val parserActorRef = context.actorOf(
    RoundRobinPool(num_parser_actor, Some(resizer))
      .props(Props(classOf[ParserActor], validatorActorRef, engine.getParser)),
    "ParserActor")
  val APIActorRef = context.actorOf(
    RoundRobinPool(num_api_actor, Some(resizer))
      .props(Props(classOf[APIActor], engine.getAPIManager)),
    "APIActor")

  def chooseReroutee(): String={
    logger.info("choosing reroutee")
    val n=cluster.state.members.collect{
        case m if m.status == MemberStatus.Up => s"${m.address}/user/crossdata-server"
    }.toSeq
    val infinispanNodes=LoadWatcherManager.MANAGER.getData.filter(i=>n.exists(nd=>nd.contains(i._1.toString)))
    val min=infinispanNodes.minBy(_._2.toString.toDouble)
    //n(random.nextInt(n.length))
    n.filter(_.contains(min._1))(0)
  }


  def reroute(message: Command): Unit ={
    if(balancing.equals("on")){
      val reroutee = chooseReroutee()
      logger.info("reroutee=" + reroutee)
      context.actorSelection(reroutee) forward ReroutedCommand(message)
    } else {
      APIActorRef forward message
    }
  }

  def reroute(message: Query): Unit ={
    if(balancing.equals("on")){
      val reroutee = chooseReroutee()
      logger.info("reroutee=" + reroutee)
      context.actorSelection(reroutee) forward ReroutedQuery(message)
    } else {
      parserActorRef forward message
    }
  }

  def receive : Receive= {
    case query: Query =>{
      logger.info("Query rerouted : " + query.statement.toString)
      reroute(query)
    } 
    case ReroutedQuery(query)=> {
      logger.info("query: " + query + " sender: " + sender.path.address)
      parserActorRef forward query
    }
    case Connect(user) => {
      logger.info("Welcome " + user + "! from " + sender.path.address)
      sender ! ConnectResult.createConnectResult(UUID.randomUUID().toString)
    }
    case Disconnect(user) => {
      logger.info("Goodbye " + user + ".")
      sender ! DisconnectResult.createDisconnectResult(user)
    }
    case cmd: Command =>{
      logger.info("API Command call rerouted " + cmd.commandType)
      reroute(cmd)
    }
    case ReroutedCommand(cmd) => {
      logger.info("API Command call " + cmd.commandType)
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
