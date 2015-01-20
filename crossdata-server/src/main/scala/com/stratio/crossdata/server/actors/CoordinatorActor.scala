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

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSelection, Props}
import com.stratio.crossdata.common.connector.ConnectorClusterConfig
import com.stratio.crossdata.common.data
import com.stratio.crossdata.common.data.ConnectorName
import com.stratio.crossdata.common.exceptions.ExecutionException
import com.stratio.crossdata.common.executionplan.{ExecutionType, ManagementWorkflow, MetadataWorkflow, QueryWorkflow, ResultType, StorageWorkflow}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.utils.StringUtils
import com.stratio.crossdata.communication._
import com.stratio.crossdata.core.coordinator.Coordinator
import com.stratio.crossdata.core.execution.{ExecutionManagerException, ExecutionInfo, ExecutionManager}
import com.stratio.crossdata.core.metadata.MetadataManager
import com.stratio.crossdata.core.query.IPlannedQuery
import com.stratio.crossdata.common.logicalplan.PartialResults
import com.stratio.crossdata.common.statements.structures.SelectorHelper
import com.stratio.crossdata.common.exceptions.validation.CoordinationException
import com.stratio.crossdata.common.metadata.TableMetadata

object CoordinatorActor {

  /**
   * Token attached to query identifiers when the query is part of a trigger execution workflow.
   */
  val TriggerToken = "_T"

  def props(connectorMgr: ActorRef, coordinator: Coordinator): Props = Props(new CoordinatorActor
  (connectorMgr, coordinator))
}

class CoordinatorActor(connectorMgr: ActorRef, coordinator: Coordinator) extends Actor with ActorLogging {

  log.info("Lifting coordinator actor")

  def receive: Receive = {

    case plannedQuery: IPlannedQuery => {
      val workflow = plannedQuery.getExecutionWorkflow()
      log.debug("Workflow for " + workflow.getActorRef)

      workflow match {
        case workflow1: MetadataWorkflow => {

          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          val queryId = plannedQuery.getQueryId
          executionInfo.setWorkflow(workflow1)

          if (workflow1.getExecutionType == ExecutionType.DROP_CATALOG) {

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(false)
            executionInfo.setRemoveOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
            val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_DROP_CATALOG)
            result.setQueryId(queryId)
            sender ! result

          } else if (workflow1.getExecutionType == ExecutionType.ALTER_CATALOG) {

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
            val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_ALTER_CATALOG)
            result.setQueryId(queryId)
            sender ! result

          } else if (workflow1.getExecutionType == ExecutionType.DROP_TABLE){

            // Drop table in the Crossdata servers through the MetadataManager
            coordinator.persistDropTable(workflow1.getTableName)

            // Send action to the connector
            val actorRef = context.actorSelection(workflow1.getActorRef)
            actorRef.asInstanceOf[ActorSelection] ! workflow1.createMetadataOperationMessage()

            // Prepare data for the reply of the connector
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(false)
            executionInfo.setRemoveOnSuccess(true)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
            executionInfo.setWorkflow(workflow1)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

          } else if (workflow1.getExecutionType == ExecutionType.ALTER_TABLE) {

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(true)
            executionInfo.setRemoveOnSuccess(true)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
            executionInfo.setWorkflow(workflow1)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

            val actorRef = context.actorSelection(workflow1.getActorRef)
            log.info("ActorRef: " + actorRef.toString())
            actorRef.asInstanceOf[ActorSelection] ! workflow1.createMetadataOperationMessage()

          } else if(workflow1.getExecutionType == ExecutionType.CREATE_INDEX) {

            if(workflow1.isIfNotExists && MetadataManager.MANAGER.exists(workflow1.getIndexName)){
              val result:MetadataResult = MetadataResult.createSuccessMetadataResult(
                MetadataResult.OPERATION_CREATE_INDEX, workflow1.isIfNotExists)
              result.setQueryId(queryId)
              sender ! result
            } else {
              executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
              executionInfo.setPersistOnSuccess(true)
              executionInfo.setRemoveOnSuccess(true)
              executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
              executionInfo.setWorkflow(workflow1)
              ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

              val actorRef = context.actorSelection(workflow1.getActorRef)
              log.info("ActorRef: " + actorRef.toString())
              actorRef.asInstanceOf[ActorSelection] ! workflow1.createMetadataOperationMessage()
            }

          } else if(workflow1.getExecutionType == ExecutionType.DROP_INDEX) {

            if(workflow1.isIfExists && !MetadataManager.MANAGER.exists(workflow1.getIndexName)){
              val result:MetadataResult = MetadataResult.createSuccessMetadataResult(
                MetadataResult.OPERATION_DROP_INDEX, workflow1.isIfExists)
              result.setQueryId(queryId)
              sender ! result
            } else {
              executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
              executionInfo.setPersistOnSuccess(true)
              executionInfo.setRemoveOnSuccess(true)
              executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
              executionInfo.setWorkflow(workflow1)
              ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

              val actorRef = context.actorSelection(workflow1.getActorRef)
              log.info("ActorRef: " + actorRef.toString())
              actorRef.asInstanceOf[ActorSelection] ! workflow1.createMetadataOperationMessage()
            }

          } else if (workflow1.getExecutionType == ExecutionType.DISCOVER_METADATA){

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(false)
            executionInfo.setRemoveOnSuccess(true)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
            executionInfo.setWorkflow(workflow1)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

            val actorRef = context.actorSelection(workflow1.getActorRef)
            log.info("ActorRef: " + actorRef.toString())
            actorRef.asInstanceOf[ActorSelection] ! workflow1.createMetadataOperationMessage()

          } else if (workflow1.getExecutionType == ExecutionType.IMPORT_CATALOGS
                    || workflow1.getExecutionType == ExecutionType.IMPORT_CATALOG
                    || workflow1.getExecutionType == ExecutionType.IMPORT_TABLE) {

              executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
              executionInfo.setPersistOnSuccess(true)
              executionInfo.setRemoveOnSuccess(true)
              executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
              executionInfo.setWorkflow(workflow1)
              ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

              val actorRef = context.actorSelection(workflow1.getActorRef)
              log.info("ActorRef: " + actorRef.toString())
              actorRef.asInstanceOf[ActorSelection] ! workflow1.createMetadataOperationMessage()

          } else if (workflow1.getExecutionType == ExecutionType.CREATE_CATALOG ||
                workflow1.getExecutionType == ExecutionType.CREATE_TABLE_AND_CATALOG ||
                workflow1.getExecutionType == ExecutionType.CREATE_TABLE) {

            if(workflow1.getExecutionType != ExecutionType.CREATE_CATALOG
                && workflow1.isIfNotExists
                && MetadataManager.MANAGER.exists(workflow1.getTableName)){
              val result:MetadataResult = MetadataResult.createSuccessMetadataResult(
                MetadataResult.OPERATION_CREATE_TABLE, workflow1.isIfNotExists)
              result.setQueryId(queryId)
              sender ! result

            } else {
              if(workflow1.getActorRef != null && workflow1.getActorRef.length() > 0){

                val actorRef = context.actorSelection(workflow1.getActorRef)
                executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
                executionInfo.setPersistOnSuccess(true)
                ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
                log.info("ActorRef: " + actorRef.toString())

                actorRef.asInstanceOf[ActorSelection] ! workflow1.createMetadataOperationMessage()

              } else {

                var result:MetadataResult = null

                if(workflow1.getExecutionType == ExecutionType.CREATE_CATALOG){
                  coordinator.persistCreateCatalog(workflow1.getCatalogMetadata, workflow1.isIfNotExists)
                  executionInfo.setQueryStatus(QueryStatus.PLANNED)
                  ExecutionManager.MANAGER.createEntry(workflow1.getCatalogMetadata.getName.toString, queryId, true)
                  ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
                  result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_CATALOG)
                } else if(workflow1.getExecutionType == ExecutionType.CREATE_TABLE
                  || workflow1.getExecutionType == ExecutionType.CREATE_TABLE_AND_CATALOG){
                  coordinator.persistCreateTable(workflow1.getTableMetadata)
                  executionInfo.setQueryStatus(QueryStatus.PLANNED)
                  ExecutionManager.MANAGER.createEntry(workflow1.getTableMetadata.getName.toString, queryId, true)
                  ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
                  result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_TABLE)
                } else {
                  throw new CoordinationException("Invalid operation");
                }

                result.setQueryId(queryId)
                sender ! result

              }
            }

          } else {
            throw new CoordinationException("Operation not supported yet");
          }

        }

        case workflow1: StorageWorkflow => {
          log.debug("CoordinatorActor: StorageWorkflow received")
          val queryId = plannedQuery.getQueryId
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          executionInfo.setWorkflow(workflow1)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          ExecutionManager.MANAGER.createEntry(queryId, executionInfo)

          val actorRef = context.actorSelection(workflow1.getActorRef())
          actorRef ! workflow1.getStorageOperation()
        }

        case workflow1: ManagementWorkflow => {

          log.info("ManagementWorkflow received")
          var sendResultToClient = true

          val queryId = plannedQuery.getQueryId
          if (workflow1.getExecutionType == ExecutionType.ATTACH_CONNECTOR) {

            val credentials = null
            val managementOperation = workflow1.createManagementOperationMessage()
            val attachConnectorOperation = managementOperation.asInstanceOf[AttachConnector]
            val clusterName = attachConnectorOperation.targetCluster

            val clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName)

            val datastoreName = clusterMetadata.getDataStoreRef
            val datastoreMetadata = MetadataManager.MANAGER.getDataStore(datastoreName)
            val clusterAttachedOpts = datastoreMetadata.getClusterAttachedRefs.get(clusterName)
            val clusterOptions = SelectorHelper.convertSelectorMapToStringMap(clusterAttachedOpts.getProperties)

            val connectorOptions = SelectorHelper.convertSelectorMapToStringMap(attachConnectorOperation.options)

            val connectorClusterConfig = new ConnectorClusterConfig(
              clusterName, connectorOptions, clusterOptions)

            connectorClusterConfig.setDataStoreName(datastoreName)

            val connectorSelection = context.actorSelection(StringUtils.getAkkaActorRefUri(workflow1.getActorRef()))
            connectorSelection ! new Connect(queryId, credentials, connectorClusterConfig)

            log.info("connectorOptions: " + connectorClusterConfig.getConnectorOptions.toString + " clusterOptions: " +
              connectorClusterConfig.getClusterOptions.toString)

            val executionInfo = new ExecutionInfo()
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
            executionInfo.setWorkflow(workflow1)
            executionInfo.setPersistOnSuccess(true)
            executionInfo.setRemoveOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

            sendResultToClient = false

          } else if (workflow1.getExecutionType == ExecutionType.DETACH_CONNECTOR) {
            val managementOperation = workflow1.createManagementOperationMessage()
            val detachConnectorOperation = managementOperation.asInstanceOf[DetachConnector]

            val clusterName = detachConnectorOperation.targetCluster
            val connectorClusterConfig = new ConnectorClusterConfig(
              clusterName, SelectorHelper.convertSelectorMapToStringMap
                (MetadataManager.MANAGER.getCluster(detachConnectorOperation.targetCluster).getOptions))
            val clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName)
            connectorClusterConfig.setDataStoreName(clusterMetadata.getDataStoreRef)

            val connectorSelection = context.actorSelection(StringUtils.getAkkaActorRefUri(workflow1.getActorRef()))
            connectorSelection ! new DisconnectFromCluster(queryId, connectorClusterConfig.getName.getName)

            val executionInfo = new ExecutionInfo()
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
            executionInfo.setWorkflow(workflow1)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
          }

          if(sendResultToClient){
            sender ! coordinator.executeManagementOperation(workflow1.createManagementOperationMessage())
          }

        }

        case workflow1: QueryWorkflow => {
          log.info("\nCoordinatorActor: QueryWorkflow received")
          val queryId = plannedQuery.getQueryId
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
          executionInfo.setWorkflow(workflow1)

          log.info("\nCoordinate workflow: " + workflow1.toString)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          if (ResultType.RESULTS.equals(workflow1.getResultType)) {

            val actorRef = StringUtils.getAkkaActorRefUri(workflow1.getActorRef())
            val actorSelection = context.actorSelection(actorRef)
            val operation = workflow1.getExecuteOperation(queryId)
            executionInfo.setRemoveOnSuccess(Execute.getClass.isInstance(operation))
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo)

            actorSelection.asInstanceOf[ActorSelection] ! operation
            log.info("\nMessage sent to " + actorRef.toString())

          } else if (ResultType.TRIGGER_EXECUTION.equals(workflow1.getResultType)) {

            val actorRef = StringUtils.getAkkaActorRefUri(workflow1.getActorRef())
            val actorSelection = context.actorSelection(actorRef)

            //Register the top level workflow
            val operation = workflow1.getExecuteOperation(queryId + CoordinatorActor
              .TriggerToken)
            executionInfo.setRemoveOnSuccess(Execute.getClass.isInstance(operation))
            ExecutionManager.MANAGER.createEntry(queryId + CoordinatorActor.TriggerToken, executionInfo)

            //Register the result workflow
            val nextExecutionInfo = new ExecutionInfo
            nextExecutionInfo.setSender(StringUtils.getAkkaActorRefUri(sender))
            nextExecutionInfo.setWorkflow(workflow1.getNextExecutionWorkflow)
            nextExecutionInfo.setRemoveOnSuccess(executionInfo.isRemoveOnSuccess)
            ExecutionManager.MANAGER.createEntry(queryId, nextExecutionInfo)

            actorSelection.asInstanceOf[ActorSelection] ! operation
            log.info("\nMessage sent to " + actorRef.toString())

          }
        }
        case _ => {
          log.error("Non recognized workflow")
        }
      }
    }

    case result: Result => {
      val queryId = result.getQueryId
      log.info("Receiving result from " + sender + " with queryId = " + queryId + " result: " + result)
      try {
        val executionInfo = ExecutionManager.MANAGER.getValue(queryId)
        //TODO Add two methods to StringUtils to retrieve AkkaActorRefUri tokening with # for connectors,
        // and $ for clients
        val target = executionInfo.asInstanceOf[ExecutionInfo].getSender
          .replace("Actor[", "").replace("]", "").split("#")(0)
        val clientActor = context.actorSelection(target)

        var sendResultToClient = true

        val it = MetadataManager.MANAGER.getTables.iterator()
        while(it.hasNext){
          log.info("TableMetadata.name = " + it.next().getName)
        }

        if(!result.hasError){
          if (executionInfo.asInstanceOf[ExecutionInfo].isPersistOnSuccess) {
            val storedWorkflow = executionInfo.asInstanceOf[ExecutionInfo].getWorkflow
            if(storedWorkflow.isInstanceOf[MetadataWorkflow]){
              coordinator.persist(storedWorkflow.asInstanceOf[MetadataWorkflow], result.asInstanceOf[MetadataResult])
            } else if (storedWorkflow.isInstanceOf[ManagementWorkflow]) {
              coordinator.executeManagementOperation(storedWorkflow.asInstanceOf[ManagementWorkflow].createManagementOperationMessage())
            }
          }
          if (executionInfo.asInstanceOf[ExecutionInfo].isRemoveOnSuccess) {
            ExecutionManager.MANAGER.deleteEntry(queryId)
          }
          if (queryId.endsWith(CoordinatorActor.TriggerToken)) {
            sendResultToClient = false
            val triggerQueryId = queryId.substring(0, queryId.length - CoordinatorActor.TriggerToken.length)
            log.info("Retrieving Triggering queryId: " + triggerQueryId);
            val executionInfo = ExecutionManager.MANAGER.getValue(triggerQueryId).asInstanceOf[ExecutionInfo]
            val partialResults = result.asInstanceOf[QueryResult].getResultSet
            executionInfo.getWorkflow.getTriggerStep.asInstanceOf[PartialResults].setResults(partialResults)
            val actorRef = StringUtils.getAkkaActorRefUri(executionInfo.getWorkflow.getActorRef())
            val actorSelection = context.actorSelection(actorRef)
            actorSelection.asInstanceOf[ActorSelection] ! executionInfo.getWorkflow.asInstanceOf[QueryWorkflow]
              .getExecuteOperation(queryId + CoordinatorActor.TriggerToken)
          }
        }

        if(sendResultToClient) {
          log.info("Send result to: " + clientActor.toString())
          clientActor ! result
        }

      } catch {
        case ex: ExecutionManagerException => {
          log.error("Cannot access queryId actorRef associated value:" + System.lineSeparator() + ex.getMessage)
        }
      }
    }

    case ctc: ConnectToConnector =>
      MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(ctc.msg), data.Status.ONLINE)
      log.info("Connected to connector")

    case dfc: DisconnectFromConnector =>
      MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(dfc.msg), data.Status.OFFLINE)
      log.info("Disconnected from connector")

    case _ => {
      sender ! new ExecutionException("Non recognized workflow")
    }

  }

}
