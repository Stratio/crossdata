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
import com.stratio.crossdata.common.data.Status
import com.stratio.crossdata.common.ask.{APICommand, Command}
import com.stratio.crossdata.common.executionplan.ExecutionInfo
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.communication.{ACK,StopProcess, Request}
import com.stratio.crossdata.core.api.APIManager
import com.stratio.crossdata.core.execution.ExecutionManager
import com.stratio.crossdata.core.metadata.MetadataManager
import org.apache.log4j.Logger
import akka.actor._

import scala.concurrent.duration._
import scala.util.{Success, Try}

object APIActor {
  def props(apiManager: APIManager, validatorActor: ActorRef): Props =
    Props(new APIActor(apiManager, validatorActor))
  val StopProcessTimeout = 4 seconds
}

class APIActor(apiManager: APIManager, coordinatorActor: ActorRef) extends Actor with TimeTracker {
  override lazy val timerName = this.getClass.getName
  val host = apiManager.getHost
  val log = Logger.getLogger(classOf[APIActor])

  def receive:Receive = {
    case cmd: Command => {
      log.debug("command received " + cmd.toString)
      val timer = initTimer()
      if (cmd.commandType == APICommand.STOP_PROCESS){
        forwardStopProcess(cmd, sender).fold{
          log.debug("Sending stop process to connector")
        } {
          result => sender ! result
        }
      }else{
        if(cmd.commandType == APICommand.CLEAN_METADATA) {
          forwardCleanMetadata
        }

        val result = apiManager.processRequest(cmd)
        result match {
          case resetServerData : ResetServerDataResult => {
            log.debug(resetServerData)

            for (forceDetachCommand <- resetServerData.getResetCommands().toArray){
              log.debug(forceDetachCommand)
              coordinatorActor forward forceDetachCommand
            }
            sender ! resetServerData.getResult
          }
          case result =>{
            sender ! result
          }
        }
      }
      finishTimer(timer)
    }


    case ACK( queryId , QueryStatus.EXECUTED) => {
      Try(ExecutionManager.MANAGER.getValue(queryId)) match {
        case Success((targetQueryId: String, originalSender: ActorRef)) => {
          log.info("Stop process ACK received. Forwarding to "+originalSender)
          ExecutionManager.MANAGER.deleteEntry(targetQueryId)
          originalSender ! CommandResult.createCommandResult("The process ["+targetQueryId+"] was successfully stopped")
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
      context.actorSelection(connectorMetadataIterator.next().getActorRef(host)) ! Request(
        APICommand.CLEAN_METADATA.toString)
    }
  }

  private def forwardStopProcess(cmd: Command, proxyActor: ActorRef): Option[Result] = {

    def stopProcessWithoutACK(stopProcessId: String, processQueryId: String) = {

      Try(ExecutionManager.MANAGER.getValue(stopProcessId)) match {
        case Success((targetQueryId: String, originalSender: ActorRef)) => {
          if (ExecutionManager.MANAGER.exists(processQueryId)){
            log.warn(s"Stop process ACK not received. Removing the execution info")
            ExecutionManager.MANAGER.deleteEntry(processQueryId)
            originalSender ! CommandResult.createCommandResult(s"Timeout stopping the process [$targetQueryId]. Results with queryId $processQueryId will be discarded.")
          }
        }
        case _ => log.error(s"The stop process with query id: $stopProcessId should exists")
      }

      ExecutionManager.MANAGER.deleteEntry(stopProcessId)

    }

    if (cmd.params != null && cmd.params.size == 1) {
      Try(ExecutionManager.MANAGER.getValue(cmd.params.get(0).toString)) match {
        case Success(exInfo:ExecutionInfo) => context.actorSelection(exInfo.getWorkflow.getActorRef) match {
          case connectorActor: ActorSelection =>
            ExecutionManager.MANAGER.createEntry(cmd.queryId, (cmd.params.get(0).toString, proxyActor) )
            connectorActor ! StopProcess(cmd.queryId, cmd.params.get(0).toString)
            context.system.scheduler.scheduleOnce(APIActor.StopProcessTimeout)(stopProcessWithoutACK(cmd.queryId, cmd.params.get(0).toString))(context.system.dispatcher)
            None
          case _ => Some(Result.createUnsupportedOperationErrorResult("There is no online connector related to query "+cmd.params.get(0).toString).withQueryId(cmd.queryId))
        }
        case _ => Some(Result.createUnsupportedOperationErrorResult("The process ["+ cmd.params.get(0).toString +"] doesn't exist").withQueryId(cmd.queryId))
      }
    }else {
       Some(Result.createUnsupportedOperationErrorResult("Invalid number of parameters invoking stop process"))
    }

  }


}
