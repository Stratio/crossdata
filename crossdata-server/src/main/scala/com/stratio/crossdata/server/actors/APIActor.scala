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

import akka.actor.{ActorSelection, Actor, Props}
import akka.routing.Broadcast
import com.stratio.crossdata.common.data.Status
import com.stratio.crossdata.common.ask.{APICommand, Command}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.utils.{StringUtils, Constants}
import com.stratio.crossdata.communication.{ACK,StopProcess, Request}
import com.stratio.crossdata.core.api.APIManager
import com.stratio.crossdata.core.execution.{ExecutionInfo, ExecutionManager}
import com.stratio.crossdata.core.metadata.MetadataManager
import org.apache.log4j.Logger
import akka.actor._

import scala.annotation.tailrec
import scala.util.{Success, Try}

object APIActor {
  def props(apiManager: APIManager): Props = Props(new APIActor(apiManager))
}

class APIActor(apiManager: APIManager) extends Actor with TimeTracker {
  override lazy val timerName = this.getClass.getName
  val log = Logger.getLogger(classOf[APIActor])

  def receive:Receive = {
    case cmd: Command => {
      log.debug("command received " + cmd.toString)
      val timer = initTimer()
      if (cmd.commandType == APICommand.STOP_PROCESS){
        forwardStopProcess(cmd, sender) match {
          case Some(result) => sender ! result
          case None => log.debug("Sending stop process to the connectors")
        }
      }else{
        if(cmd.commandType == APICommand.CLEAN_METADATA) {
          forwardCleanMetadata
        }
        sender ! apiManager.processRequest(cmd)
      }
      finishTimer(timer)
    }


    case ACK( queryId , QueryStatus.EXECUTED) => {
      Try(ExecutionManager.MANAGER.getValue(queryId)) match {
        case Success((targetQueryId: String, originalSender: ActorRef)) => {
          ExecutionManager.MANAGER.deleteEntry(targetQueryId)
          if(!targetQueryId.endsWith(Constants.TRIGGER_TOKEN)){
            ExecutionManager.MANAGER.deleteEntry(queryId)
            log.info("Stop process ACK received. Forwarding to "+originalSender)
            originalSender ! CommandResult.createCommandResult("The process ["+targetQueryId+"] was successfully stopped")
          }
        }
        case _ => log.debug("Unexpected ack received with queryId: "+queryId)
      }
    }

    case result: Result => {
      Try(ExecutionManager.MANAGER.getValue(result.getQueryId)) match {
        case Success((targetQueryId: String, originalSender: ActorRef))=> {
          originalSender ! result
          ExecutionManager.MANAGER.deleteEntry(result.getQueryId)
        }
        case _ => log.debug("Unexpected result received with queryId: "+result.getQueryId)
      }
    }

    case _ => {
      log.info("command _ received ")
      sender ! Result.createUnsupportedOperationErrorResult("Unsupported command")
    }
  }

  private def forwardCleanMetadata: Unit = {
    val connectorMetadataIterator = MetadataManager.MANAGER.getConnectors(Status.ONLINE).iterator()
    while (connectorMetadataIterator.hasNext) {
      context.actorSelection(connectorMetadataIterator.next().getActorRef) ! Request(APICommand.CLEAN_METADATA.toString)
    }
  }

  private def forwardStopProcess(cmd: Command, proxyActor: ActorRef): Option[Result] = {

    def stopTriggerQueries(baseQueryId: String): Unit = {
      val topLevelQueryId = baseQueryId + Constants.TRIGGER_TOKEN

        Try(ExecutionManager.MANAGER.getValue(topLevelQueryId)) match {
        case Success(exInfo:ExecutionInfo) => context.actorSelection(exInfo.getWorkflow.getActorRef) match {
          case connectorActor: ActorSelection =>
            log.info("Stopping top level queryId: "+topLevelQueryId)
            stopTriggerQueries(topLevelQueryId)
            connectorActor ! StopProcess(cmd.queryId, topLevelQueryId)
          case _ =>
        }
        case _ =>
      }
    }

    if (cmd.params != null && cmd.params.size == 1) {
      stopTriggerQueries(cmd.params.get(0).toString)
      Try(ExecutionManager.MANAGER.getValue(cmd.params.get(0).toString)) match {
        case Success(exInfo:ExecutionInfo) => context.actorSelection(exInfo.getWorkflow.getActorRef) match {
          case connectorActor: ActorSelection =>
            ExecutionManager.MANAGER.createEntry(cmd.queryId, (cmd.params.get(0).toString, proxyActor) )
            connectorActor ! StopProcess(cmd.queryId, cmd.params.get(0).toString)
            None
          case _ => Some(Result.createUnsupportedOperationErrorResult("There is no connector related to query "+cmd.params.get(0).toString).withQueryId(cmd.queryId))
        }
        case _ => Some(Result.createUnsupportedOperationErrorResult("The process ["+ cmd.params.get(0).toString +"] doesn't exist").withQueryId(cmd.queryId))
      }
    }else {
       Some(Result.createUnsupportedOperationErrorResult("Invalid number of parameters invoking stop process"))
    }

  }


}
