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

package com.stratio.crossdata.connectors

import akka.actor._
import com.stratio.crossdata.common.exceptions.ExecutionException
import com.stratio.crossdata.common.executionplan._
import com.stratio.crossdata.common.logicalplan.PartialResults
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.utils.StringUtils
import com.stratio.crossdata.communication._
import com.stratio.crossdata.connectors.config.ConnectConfig
import org.apache.log4j.Logger


object ConnectorCoordinatorActor {
  def props(connectorWorkerActor: ActorRef): Props =
    Props(new ConnectorCoordinatorActor(connectorWorkerActor))
}

/**
 * Coordinate trigger execution requested by the server.
 */
class ConnectorCoordinatorActor(connectorWorkerActorRef: ActorRef) extends Actor with ActorLogging with ConnectConfig {

  override lazy val logger = Logger.getLogger(classOf[ConnectorCoordinatorActor])
  logger.info("Lifting ConnectorCoordinatorActor actor")
  var runningJobs = Map.empty[String, (ActorRef, ExecutionInfo)]

  override def receive: Receive = {

    case TriggerExecution(exWorkflow, triggeredExecution) => exWorkflow match {
      case q :QueryWorkflow =>{
        connectorWorkerActorRef ! q.getExecuteOperation(q.getQueryId)
        runningJobs = runningJobs.updated(q.getQueryId, (sender,triggeredExecution))
        logger.debug(s"New trigger execution with queryId: ${q.getQueryId}. Executing ${q.getExecutionType}")
      }
      case q :StorageWorkflow =>{
        connectorWorkerActorRef ! q.getStorageOperation;
        runningJobs = runningJobs.updated(q.getQueryId, (sender,triggeredExecution))
        logger.debug(s"New trigger execution with queryId: ${q.getQueryId}. Executing ${q.getExecutionType}")
      }


    }

    case results: Result => {
      logger.debug(s"Receiving results for queryId: ${results.getQueryId}")

      val queryId = results.getQueryId
      val triggeredExecution = runningJobs.get(queryId)

      if (results.hasError) {
        log.warning(s"Execution error: ${results.asInstanceOf[ErrorResult].getErrorMessage}. Forwarding to the server")
        triggeredExecution.fold{
          log.error(s"Unknown error result received with queryId: ${results.getQueryId}")
        } { case (server, exInfo ) =>
          log.warning(s"Execution error: ${results.asInstanceOf[ErrorResult].getErrorMessage}. Forwarding to the server")
          runningJobs = runningJobs - queryId
          server ! results
        }
      } else {
        triggeredExecution.fold{
          log.error(s"Unknown result received with queryId: ${results.getQueryId}")
        } {  case (server, exInfo ) =>

          logger.debug(s"Received trigger result: $queryId")


          val nextConnectorActorRef = StringUtils.getAkkaActorRefUri(exInfo.getWorkflow.getActorRef(), false)
          val nextConnectorActorSelection = context.actorSelection(nextConnectorActorRef)

          var operation: Operation = null

          if (ExecutionType.INSERT_BATCH.equals(exInfo.getWorkflow.getExecutionType)) {
            val partialResults = results.asInstanceOf[QueryResult].getResultSet
            exInfo.getWorkflow.asInstanceOf[StorageWorkflow].setRows(partialResults.getRows)
            operation = exInfo.getWorkflow.asInstanceOf[StorageWorkflow].getStorageOperation
          } else if (ExecutionType.INSERT.equals(exInfo.getWorkflow.getExecutionType)) {
            operation = exInfo.getWorkflow.asInstanceOf[StorageWorkflow].getStorageOperation
           }else {
            val partialResults = results.asInstanceOf[QueryResult].getResultSet
            exInfo.getWorkflow.getTriggerStep.asInstanceOf[PartialResults].setResults(partialResults)
            operation = exInfo.getWorkflow.asInstanceOf[QueryWorkflow].getExecuteOperation(queryId)
          }
          log.debug("Sending operation: " + operation + " to: " + nextConnectorActorSelection)
          nextConnectorActorSelection.tell(operation, server)

        }
      }

    }

    case ACKResult(queryId, status) => {
      //TODO forward to server in order to update the web interface. The server receives the result, so it should receive the ack as well.
      logger.debug(s"ack result $queryId -> status: $status")
      def isRemoveOnSuccess(queryId: String) = runningJobs.get(queryId).fold(false)(_._2.asInstanceOf[ExecutionInfo].isRemoveOnSuccess)
      def isTriggeredByStreaming(queryId: String) = runningJobs.get(queryId).fold(false)(_._2.asInstanceOf[ExecutionInfo].isTriggeredByStreaming)

      if(QueryStatus.EXECUTED == status){
        runningJobs = runningJobs - queryId
      }
    }

    case msg => {
      log.error(s"Unexpected message: $msg")
    }

  }
}
