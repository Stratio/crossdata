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
import com.stratio.crossdata.common.exceptions.validation.CoordinationException
import com.stratio.crossdata.common.executionplan.{ExecutionType, ManagementWorkflow, MetadataWorkflow, QueryWorkflow, ResultType, StorageWorkflow}
import com.stratio.crossdata.common.logicalplan.PartialResults
import com.stratio.crossdata.common.metadata.Operations
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.statements.structures.SelectorHelper
import com.stratio.crossdata.common.utils.StringUtils
import com.stratio.crossdata.communication._
import com.stratio.crossdata.core.coordinator.Coordinator
import com.stratio.crossdata.core.execution.{ExecutionInfo, ExecutionManager, ExecutionManagerException}
import com.stratio.crossdata.core.metadata.MetadataManager
import com.stratio.crossdata.core.query.IPlannedQuery
import scala.collection.JavaConversions._

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
        case metadataWorkflow: MetadataWorkflow => {

          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
          val queryId = plannedQuery.getQueryId
          executionInfo.setWorkflow(metadataWorkflow)

          if (metadataWorkflow.getExecutionType == ExecutionType.DROP_CATALOG) {

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(false)
            executionInfo.setRemoveOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
            val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_DROP_CATALOG)
            result.setQueryId(queryId)
            sender ! result

          } else if (metadataWorkflow.getExecutionType == ExecutionType.ALTER_CATALOG) {

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
            val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_ALTER_CATALOG)
            result.setQueryId(queryId)
            sender ! result

          } else if (metadataWorkflow.getExecutionType == ExecutionType.DROP_TABLE){

            // Drop table in the Crossdata servers through the MetadataManager
            coordinator.persistDropTable(metadataWorkflow.getTableName)

            // Send action to the connector
            val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
            actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

            // Prepare data for the reply of the connector
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(false)
            executionInfo.setRemoveOnSuccess(true)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
            executionInfo.setWorkflow(metadataWorkflow)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

          } else if (metadataWorkflow.getExecutionType == ExecutionType.ALTER_TABLE) {

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(true)
            executionInfo.setRemoveOnSuccess(true)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
            executionInfo.setWorkflow(metadataWorkflow)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

            val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
            log.info("ActorRef: " + actorRef.toString())
            actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

          } else if(metadataWorkflow.getExecutionType == ExecutionType.CREATE_INDEX) {

            if(metadataWorkflow.isIfNotExists && MetadataManager.MANAGER.exists(metadataWorkflow.getIndexName)){
              val result:MetadataResult = MetadataResult.createSuccessMetadataResult(
                MetadataResult.OPERATION_CREATE_INDEX, metadataWorkflow.isIfNotExists)
              result.setQueryId(queryId)
              sender ! result
            } else {
              executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
              executionInfo.setPersistOnSuccess(true)
              executionInfo.setRemoveOnSuccess(true)
              executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
              executionInfo.setWorkflow(metadataWorkflow)
              ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

              val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
              log.info("ActorRef: " + actorRef.toString())
              actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()
            }

          } else if(metadataWorkflow.getExecutionType == ExecutionType.DROP_INDEX) {

            if(metadataWorkflow.isIfExists && !MetadataManager.MANAGER.exists(metadataWorkflow.getIndexName)){
              val result:MetadataResult = MetadataResult.createSuccessMetadataResult(
                MetadataResult.OPERATION_DROP_INDEX, metadataWorkflow.isIfExists)
              result.setQueryId(queryId)
              sender ! result
            } else {
              executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
              executionInfo.setPersistOnSuccess(true)
              executionInfo.setRemoveOnSuccess(true)
              executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
              executionInfo.setWorkflow(metadataWorkflow)
              ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

              val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
              log.info("ActorRef: " + actorRef.toString())
              actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()
            }

          } else if (metadataWorkflow.getExecutionType == ExecutionType.DISCOVER_METADATA){

            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(false)
            executionInfo.setRemoveOnSuccess(true)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
            executionInfo.setWorkflow(metadataWorkflow)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

            val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
            log.info("ActorRef: " + actorRef.toString())
            actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

          } else if (metadataWorkflow.getExecutionType == ExecutionType.IMPORT_CATALOGS
                    || metadataWorkflow.getExecutionType == ExecutionType.IMPORT_CATALOG
                    || metadataWorkflow.getExecutionType == ExecutionType.IMPORT_TABLE) {

              executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
              executionInfo.setPersistOnSuccess(true)
              executionInfo.setRemoveOnSuccess(true)
              executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
              executionInfo.setWorkflow(metadataWorkflow)
              ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

              val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
              log.info("ActorRef: " + actorRef.toString())
              actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

          } else if (metadataWorkflow.getExecutionType == ExecutionType.CREATE_CATALOG ||
                metadataWorkflow.getExecutionType == ExecutionType.CREATE_TABLE_AND_CATALOG ||
                metadataWorkflow.getExecutionType == ExecutionType.CREATE_TABLE) {

            if(metadataWorkflow.getExecutionType != ExecutionType.CREATE_CATALOG
                && metadataWorkflow.isIfNotExists
                && MetadataManager.MANAGER.exists(metadataWorkflow.getTableName)){
              val result:MetadataResult = MetadataResult.createSuccessMetadataResult(
                MetadataResult.OPERATION_CREATE_TABLE, metadataWorkflow.isIfNotExists)
              result.setQueryId(queryId)
              sender ! result

            } else {
              if(metadataWorkflow.getActorRef != null && metadataWorkflow.getActorRef.length() > 0){

                val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
                executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
                executionInfo.setPersistOnSuccess(true)
                ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
                log.info("ActorRef: " + actorRef.toString())

                actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

              } else {

                var result:MetadataResult = null

                if(metadataWorkflow.getExecutionType == ExecutionType.CREATE_CATALOG){
                  coordinator.persistCreateCatalog(metadataWorkflow.getCatalogMetadata, metadataWorkflow.isIfNotExists)
                  executionInfo.setQueryStatus(QueryStatus.PLANNED)
                  ExecutionManager.MANAGER.createEntry(metadataWorkflow.getCatalogMetadata.getName.toString, queryId, true)
                  ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
                  result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_CATALOG)
                } else if(metadataWorkflow.getExecutionType == ExecutionType.CREATE_TABLE
                  || metadataWorkflow.getExecutionType == ExecutionType.CREATE_TABLE_AND_CATALOG){
                  coordinator.persistCreateTable(metadataWorkflow.getTableMetadata)
                  executionInfo.setQueryStatus(QueryStatus.PLANNED)
                  ExecutionManager.MANAGER.createEntry(metadataWorkflow.getTableMetadata.getName.toString, queryId, true)
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

        case storageWorkflow: StorageWorkflow => {
          log.debug("CoordinatorActor: StorageWorkflow received")
          val queryId = plannedQuery.getQueryId
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, false))
          executionInfo.setWorkflow(storageWorkflow)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)

          log.info("\nCoordinate workflow: " + storageWorkflow.toString)

          if ((storageWorkflow.getPreviousExecutionWorkflow == null)
            && (ResultType.RESULTS.equals(storageWorkflow.getResultType))) {

            ExecutionManager.MANAGER.createEntry(queryId, executionInfo)

            val actorRef = context.actorSelection(storageWorkflow.getActorRef())
            actorRef ! storageWorkflow.getStorageOperation()

          } else if ((storageWorkflow.getPreviousExecutionWorkflow != null)
            && (ResultType.TRIGGER_EXECUTION.equals(storageWorkflow.getPreviousExecutionWorkflow.getResultType))) {

            val actorRef = StringUtils.getAkkaActorRefUri(storageWorkflow.getActorRef(), false)
            val actorSelection = context.actorSelection(actorRef)

            //Register the top level workflow
            val operation = storageWorkflow.getPreviousExecutionWorkflow.asInstanceOf[QueryWorkflow].getExecuteOperation(
              queryId + CoordinatorActor.TriggerToken)
            executionInfo.setRemoveOnSuccess(Execute.getClass.isInstance(operation))
            ExecutionManager.MANAGER.createEntry(queryId + CoordinatorActor.TriggerToken, executionInfo)

            //Register the result workflow
            val nextExecutionInfo = new ExecutionInfo
            nextExecutionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, false))
            nextExecutionInfo.setWorkflow(storageWorkflow)
            nextExecutionInfo.setRemoveOnSuccess(executionInfo.isRemoveOnSuccess)
            ExecutionManager.MANAGER.createEntry(queryId, nextExecutionInfo)

            log.info("Sending operation: " + operation + " to: " + actorSelection.asInstanceOf[ActorSelection])

            actorSelection.asInstanceOf[ActorSelection] ! operation
            log.info("\nMessage sent to " + actorRef.toString())
          }

        }

        case managementWorkflow: ManagementWorkflow => {

          log.info("ManagementWorkflow received")
          var sendResultToClient = true

          val queryId = plannedQuery.getQueryId
          if (managementWorkflow.getExecutionType == ExecutionType.ATTACH_CONNECTOR) {

            val credentials = null
            val managementOperation = managementWorkflow.createManagementOperationMessage()
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

            val actorRefs = managementWorkflow.getActorRefs
            var count = 1
            for(actorRef <- actorRefs){
              val connectorSelection = context.actorSelection(StringUtils.getAkkaActorRefUri(actorRef, false))
              connectorSelection ! new Connect(queryId+"#"+count, credentials, connectorClusterConfig)
              count+=1
            }

            log.info("connectorOptions: " + connectorClusterConfig.getConnectorOptions.toString + " clusterOptions: " +
              connectorClusterConfig.getClusterOptions.toString)

            val executionInfo = new ExecutionInfo()
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, false))
            executionInfo.setWorkflow(managementWorkflow)
            executionInfo.setPersistOnSuccess(true)
            executionInfo.setRemoveOnSuccess(true)
            count = 1
            for(actorRef <- actorRefs){
              ExecutionManager.MANAGER.createEntry(queryId+"#"+count, executionInfo, true)
              count+=1
            }

            sendResultToClient = false

          } else if (managementWorkflow.getExecutionType == ExecutionType.DETACH_CONNECTOR) {
            val managementOperation = managementWorkflow.createManagementOperationMessage()
            val detachConnectorOperation = managementOperation.asInstanceOf[DetachConnector]

            val clusterName = detachConnectorOperation.targetCluster
            val connectorClusterConfig = new ConnectorClusterConfig(
              clusterName,
              SelectorHelper.convertSelectorMapToStringMap(
              MetadataManager.MANAGER.getConnector(
                new ConnectorName(detachConnectorOperation.connectorName.getName)).getClusterProperties.get(clusterName)),
              SelectorHelper.convertSelectorMapToStringMap(
                MetadataManager.MANAGER.getCluster(clusterName).getOptions)
            )
            val clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName)
            connectorClusterConfig.setDataStoreName(clusterMetadata.getDataStoreRef)

            val actorRefs = managementWorkflow.getActorRefs
            for(actorRef <- actorRefs){
              val connectorSelection = context.actorSelection(StringUtils.getAkkaActorRefUri(actorRef, false))
              connectorSelection ! new DisconnectFromCluster(queryId, connectorClusterConfig.getName.getName)
            }

            val executionInfo = new ExecutionInfo()
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, false))
            executionInfo.setWorkflow(managementWorkflow)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
          }

          if(sendResultToClient){
            sender ! coordinator.executeManagementOperation(managementWorkflow.createManagementOperationMessage())
          }

        }

        case queryWorkflow: QueryWorkflow => {
          log.info("\nCoordinatorActor: QueryWorkflow received")
          val queryId = plannedQuery.getQueryId
          val executionInfo = new ExecutionInfo
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
          executionInfo.setWorkflow(queryWorkflow)

          log.info("\nCoordinate workflow: " + queryWorkflow.toString)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          if (ResultType.RESULTS.equals(queryWorkflow.getResultType)) {

            val actorRef = StringUtils.getAkkaActorRefUri(queryWorkflow.getActorRef(), false)
            val actorSelection = context.actorSelection(actorRef)
            val operation = queryWorkflow.getExecuteOperation(queryId)
            executionInfo.setRemoveOnSuccess(Execute.getClass.isInstance(operation))
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo)

            actorSelection.asInstanceOf[ActorSelection] ! operation
            log.info("\nMessage sent to " + actorRef.toString())

          } else if (ResultType.TRIGGER_EXECUTION.equals(queryWorkflow.getResultType)) {

            val actorRef = StringUtils.getAkkaActorRefUri(queryWorkflow.getActorRef(), false)
            val actorSelection = context.actorSelection(actorRef)

            //Register the top level workflow
            val operation = queryWorkflow.getExecuteOperation(
              queryId + CoordinatorActor.TriggerToken)
            executionInfo.setRemoveOnSuccess(Execute.getClass.isInstance(operation))
            ExecutionManager.MANAGER.createEntry(queryId + CoordinatorActor.TriggerToken, executionInfo)

            //Register the result workflow
            val nextExecutionInfo = new ExecutionInfo
            nextExecutionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, true))
            nextExecutionInfo.setWorkflow(queryWorkflow.getNextExecutionWorkflow)
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

        if(queryId.contains("#")){
          if(!queryId.endsWith("#1")){
            sendResultToClient = false
          }
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
            val actorRef = StringUtils.getAkkaActorRefUri(executionInfo.getWorkflow.getActorRef(), false)
            val actorSelection = context.actorSelection(actorRef)

            var operation: Operation = new Operation(queryId)

            if(ExecutionType.INSERT_BATCH.equals(executionInfo.getWorkflow.getExecutionType)){
              executionInfo.getWorkflow.asInstanceOf[StorageWorkflow].setRows(partialResults.getRows)
              operation = executionInfo.getWorkflow.asInstanceOf[StorageWorkflow].getStorageOperation
            } else {
              if (executionInfo.getWorkflow.getTriggerStep!=null) {
                executionInfo.getWorkflow.getTriggerStep.asInstanceOf[PartialResults].setResults(partialResults)
              }else{
                val partialResultsStep: PartialResults = new PartialResults(Operations.PARTIAL_RESULTS)
                executionInfo.getWorkflow.setTriggerStep(partialResultsStep)
                executionInfo.getWorkflow.getTriggerStep.asInstanceOf[PartialResults].setResults(partialResults)
              }
              operation = executionInfo.getWorkflow.asInstanceOf[QueryWorkflow].getExecuteOperation(queryId)

            }
            log.info("Sending operation: " + operation + " to: " + actorSelection.asInstanceOf[ActorSelection])
            actorSelection.asInstanceOf[ActorSelection] ! operation
          }
        }

        if(sendResultToClient) {
          log.info("Send result to: " + target)
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
