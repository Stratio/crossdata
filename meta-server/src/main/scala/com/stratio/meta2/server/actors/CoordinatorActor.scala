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
import com.stratio.meta.communication._
import com.stratio.meta2.common.data.{ConnectorName, Status}
import com.stratio.meta2.common.result.Result
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.execution.{ExecutionInfo, ExecutionManager}
import com.stratio.meta2.core.metadata.MetadataManager
import com.stratio.meta2.core.query.PlannedQuery
import com.stratio.meta.common.connector.ConnectorClusterConfig
import com.stratio.meta.communication.ConnectToConnector
import com.stratio.meta.communication.DisconnectFromConnector
import com.stratio.meta.communication.ACK
import com.stratio.meta.communication.Connect
import com.stratio.meta2.common.statements.structures.selectors.SelectorHelper

object CoordinatorActor {
  def props(connectorMgr: ActorRef, coordinator: Coordinator): Props = Props(new CoordinatorActor
  (connectorMgr, coordinator))
}

class CoordinatorActor(connectorMgr: ActorRef, coordinator: Coordinator) extends Actor with ActorLogging {
  log.info("Lifting coordinator actor")

  def receive = {
      case plannedQuery: PlannedQuery => {
      val workflow = plannedQuery.getExecutionWorkflow()
      log.debug("Workflow for "+workflow.getActorRef)
      workflow match {
        case workflow: MetadataWorkflow => {
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          val queryId = plannedQuery.getQueryId
          executionInfo.setWorkflow(workflow)
          if(workflow.getActorRef() != null && workflow.getActorRef().length()>0){
            val actorRef = context.actorSelection(workflow.getActorRef())
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
            log.info("ActorRef: " + actorRef.toString())
            actorRef.asInstanceOf[ActorSelection] ! workflow.createMetadataOperationMessage()
          } else if(workflow.getExecutionType==ExecutionType.CREATE_CATALOG || workflow
            .getExecutionType==ExecutionType.CREATE_TABLE_AND_CATALOG) {
            coordinator.persistCreateCatalog(workflow.getCatalogMetadata)
            executionInfo.setQueryStatus(QueryStatus.PLANNED)
            ExecutionManager.MANAGER.createEntry(workflow.getCatalogMetadata.getName.toString, queryId, true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
            val result = MetadataResult.createSuccessMetadataResult()
            result.setQueryId(queryId)
            sender ! result
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
          actorRef ! workflow.getStorageOperation()
        }

        case workflow: ManagementWorkflow => {
          log.debug("CoordinatorActor: ManagementWorkflow received")
          val queryId = plannedQuery.getQueryId
          if(workflow.getExecutionType == ExecutionType.ATTACH_CONNECTOR){

            val credentials = null
            val managementOperation = workflow.createManagementOperationMessage()
            val attachConnectorOperation = managementOperation.asInstanceOf[AttachConnector]
            val connectorClusterConfig = new ConnectorClusterConfig(
              attachConnectorOperation.targetCluster, SelectorHelper.convertSelectorMapToStringMap
                (MetadataManager.MANAGER.getCluster(attachConnectorOperation.targetCluster).getOptions))
            val connectorSelection = context.actorSelection(StringUtils.getAkkaActorRefUri(workflow.getActorRef()))
            connectorSelection ! new Connect(credentials, connectorClusterConfig)

            val executionInfo = new ExecutionInfo()
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
            executionInfo.setWorkflow(workflow)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

          }
          sender ! coordinator.executeManagementOperation(workflow.createManagementOperationMessage())
        }

        case workflow: QueryWorkflow => {
          log.info("CoordinatorActor: QueryWorkflow received")
          val queryId = plannedQuery.getQueryId
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          executionInfo.setWorkflow(workflow)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          if(ResultType.RESULTS.equals(workflow.getResultType)){
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo)
            val actorRef = context.actorSelection(workflow.getActorRef())
            log.info("ActorRef: " + actorRef.toString())
            actorRef.asInstanceOf[ActorSelection] ! workflow.getExecuteOperation(queryId)
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
      log.info("Receiving result from " + sender + " with queryId = " + queryId + " result: " + result)
      val executionInfo = ExecutionManager.MANAGER.getValue(queryId)
      val clientActor = context.actorSelection(StringUtils.getAkkaActorRefUri(executionInfo
        .asInstanceOf[ExecutionInfo].getSender))
      if(executionInfo.asInstanceOf[ExecutionInfo].isPersistOnSuccess){
        coordinator.persist(executionInfo.asInstanceOf[ExecutionInfo].getWorkflow.asInstanceOf[MetadataWorkflow])
      }
      ExecutionManager.MANAGER.deleteEntry(queryId)
      clientActor ! result
    }

    case ctc: ConnectToConnector =>
      MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(ctc.msg),Status.ONLINE)
      log.info("connected to connector ")

    case dfc: DisconnectFromConnector =>
      MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(dfc.msg),Status.OFFLINE)
      log.info("disconnected from connector ")

    case _ => {
      //sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
      sender ! new ExecutionException("Non recognized workflow")
    }

  }

}
