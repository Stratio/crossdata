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

package com.stratio.meta2.server.actors

import akka.actor._
import com.stratio.meta.common.exceptions.ExecutionException
import com.stratio.meta.common.executionplan._
import com.stratio.meta.common.result._
import com.stratio.meta.common.utils.StringUtils
import com.stratio.meta.communication.{ACK, ConnectToConnector, DisconnectFromConnector}
import com.stratio.meta2.common.data.{ConnectorName, Status}
import com.stratio.meta2.common.result.Result
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.execution.{ExecutionInfo, ExecutionManager}
import com.stratio.meta2.core.metadata.MetadataManager
import com.stratio.meta2.core.query.PlannedQuery

object CoordinatorActor {
  def props(connectorMgr: ActorRef, coordinator: Coordinator): Props = Props(new CoordinatorActor
  (connectorMgr, coordinator))
}

class CoordinatorActor(connectorMgr: ActorRef, coordinator: Coordinator) extends Actor with ActorLogging {
  log.info("Lifting coordinator actor")

  def receive = {

      case plannedQuery: PlannedQuery => {
      val workflow = plannedQuery.getExecutionWorkflow()

      workflow match {
        case workflow: MetadataWorkflow => {
          log.info("\n\n\n\n>>>>>> TRACE: MetadataWorkflow from "+workflow.getActorRef)
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          val queryId = plannedQuery.getQueryId
          executionInfo.setWorkflow(workflow)
          if(workflow.getActorRef() != null && workflow.getActorRef().length()>0){
            val connectorSelection=context.actorSelection(StringUtils.getAkkaActorRefUri(workflow.getActorRef()))
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo)
            connectorSelection ! workflow.createMetadataOperationMessage(queryId)
          } else if(workflow.getExecutionType==ExecutionType.CREATE_CATALOG || workflow
            .getExecutionType==ExecutionType.CREATE_TABLE_AND_CATALOG) {
            coordinator.persistCreateCatalog(workflow.getCatalogMetadata)
            executionInfo.setQueryStatus(QueryStatus.PLANNED)
            ExecutionManager.MANAGER.createEntry(workflow.getCatalogMetadata.getName.toString, queryId)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo)
            sender ! ACK(queryId, QueryStatus.EXECUTED)
          }
        }

        case workflow: StorageWorkflow => {
          log.debug("CoordinatorActor: StorageWorkflow received")
          val queryId = plannedQuery.getQueryId
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          executionInfo.setWorkflow(workflow)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          ExecutionManager.MANAGER.createEntry(queryId, executionInfo)
          val actorRef=context.actorSelection(workflow.getActorRef())
          actorRef ! workflow.getStorageOperation(queryId)
        }

        case workflow: ManagementWorkflow => {
          log.debug("CoordinatorActor: ManagementWorkflow received")
          //val requestSender = sender
          log.info(">>>>>> TRACE: ManagementWorkflow ")
          val queryId = plannedQuery.getQueryId
          sender ! coordinator.executeManagementOperation(workflow.createManagementOperationMessage(queryId))
        }

        case workflow: QueryWorkflow => {
          log.debug("CoordinatorActor: QueryWorkflow received")
          val queryId = plannedQuery.getQueryId
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          executionInfo.setWorkflow(workflow)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          if(ResultType.RESULTS.equals(workflow.getResultType)){
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo)
            val connectorSelection=context.actorSelection(StringUtils.getAkkaActorRefUri(workflow.getActorRef()))
            connectorSelection ! workflow.getWorkflow()
          }else if(ResultType.TRIGGER_EXECUTION.equals(workflow.getResultType)){
            //TODO Trigger next step execution.
            throw new UnsupportedOperationException("Trigger execution not supported")
          }
        }
        case _ =>{
          log.error("non recognized workflow")
        }
      }
    }

    case result: Result => {
      val queryId = result.getQueryId
      println("receiving result from "+sender+"; queryId="+queryId)
      val executionInfo = ExecutionManager.MANAGER.getValue(queryId)
      val clientActor = context.actorSelection(StringUtils.getAkkaActorRefUri(executionInfo
        .asInstanceOf[ExecutionInfo].getSender))
      if(executionInfo.asInstanceOf[ExecutionInfo].isPersistOnSuccess){
        coordinator.persist(executionInfo.asInstanceOf[ExecutionInfo].getWorkflow.asInstanceOf[MetadataWorkflow ])
        ExecutionManager.MANAGER.deleteEntry(queryId)
      }
      clientActor ! result
    }

    case ctc: ConnectToConnector =>
      MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(ctc.msg),Status.ONLINE)
      log.info("connected to connector ")

    case ctc: DisconnectFromConnector =>
      MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(ctc.msg),Status.OFFLINE)
      log.info("disconnected from connector ")

    case _ => {
      //sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
      sender ! new ExecutionException("Non recognized workflow")
    }

  }

}
