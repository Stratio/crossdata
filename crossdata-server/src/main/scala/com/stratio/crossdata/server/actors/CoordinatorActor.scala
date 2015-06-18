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
import akka.routing.RoundRobinPool
import com.stratio.crossdata.common.connector.ConnectorClusterConfig
import com.stratio.crossdata.common.data
import com.stratio.crossdata.common.data.{ClusterName, ConnectorName, Status}
import com.stratio.crossdata.common.exceptions.ExecutionException
import com.stratio.crossdata.common.exceptions.validation.CoordinationException
import com.stratio.crossdata.common.executionplan._
import com.stratio.crossdata.common.logicalplan.PartialResults
import com.stratio.crossdata.common.metadata.{CatalogMetadata, ConnectorMetadata, TableMetadata, UpdatableMetadata}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.statements.structures.SelectorHelper
import com.stratio.crossdata.common.utils.{Constants, StringUtils}
import com.stratio.crossdata.communication._
import com.stratio.crossdata.core.coordinator.Coordinator
import com.stratio.crossdata.core.execution.{ExecutionManager, ExecutionManagerException}
import com.stratio.crossdata.core.metadata.MetadataManager
import com.stratio.crossdata.core.query.IPlannedQuery

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

object CoordinatorActor {

  /**
   * Token attached to query identifiers when the query is part of a trigger execution workflow.
   */
  val TriggerToken = Constants.TRIGGER_TOKEN

  def props(connectorMgr: ActorRef, coordinator: Coordinator): Props = Props(new CoordinatorActor
  (connectorMgr, coordinator))
}

class CoordinatorActor(connectorMgr: ActorRef, coordinator: Coordinator) extends Actor with ActorLogging {

  log.info("Lifting coordinator actor")
  val host = coordinator.getHost

  def receive: Receive = {

    case plannedQuery: IPlannedQuery => {
      val workflow = plannedQuery.getExecutionWorkflow()
      log.debug("Workflow for " + workflow.getActorRef)
      manageWorkflow(plannedQuery.getQueryId, workflow, None)
    }

    case workflow: ExecutionWorkflow => {
      log.debug(s"Retrying workflow ${workflow.toString}")
      Option(ExecutionManager.MANAGER.getValue(workflow.getQueryId)) match {
        case Some(exInfo: ExecutionInfo) => manageWorkflow(workflow.getQueryId, workflow, Some(exInfo.getSender))
        case _ => log.error("The retried query has not been found in MetadataManager")
      }
    }

    case connectResult: ConnectToConnectorResult =>
      val queryId = connectResult.getQueryId
      log.info("Receiving result from " + sender + " with queryId = " + queryId + " result: " + connectResult)
      if(queryId.contains("#")){
        if(queryId.endsWith("#1")){
          connectResult.setQueryId(queryId.split("#")(0))
        }
      }
      try {
        val executionInfo = ExecutionManager.MANAGER.getValue(queryId)
        val sendResultToClient = executionInfo.asInstanceOf[ExecutionInfo].getSender != null

        if(!connectResult.hasError) {
          val storedWorkflow = executionInfo.asInstanceOf[ExecutionInfo].getWorkflow
          if (storedWorkflow.isInstanceOf[ManagementWorkflow]) {
            val managementWorkflow = storedWorkflow.asInstanceOf[ManagementWorkflow]

            require(managementWorkflow.getExecutionType == ExecutionType.ATTACH_CONNECTOR)

            if (executionInfo.asInstanceOf[ExecutionInfo].isPersistOnSuccess) {
              coordinator.executeManagementOperation(managementWorkflow.createManagementOperationMessage())
            }
            if (executionInfo.asInstanceOf[ExecutionInfo].isUpdateOnSuccess) {
              for (catalogName <- MetadataManager.MANAGER.getCluster(managementWorkflow.getClusterName).getPersistedCatalogs.asScala.toList){
                for (tableMetadata <- MetadataManager.MANAGER.getTablesByCatalogName(catalogName.getName)){
                  sender ! UpdateMetadata(tableMetadata, remove = false)
                }
              }
            }
            if (executionInfo.asInstanceOf[ExecutionInfo].isRemoveOnSuccess) {
              ExecutionManager.MANAGER.deleteEntry(queryId)
            }

          }else log.error( "QueryId refering to ConnectResult should have an ManagementWorkflow associated")
        }

        if(sendResultToClient) {
          //TODO Add two methods to StringUtils to retrieve AkkaActorRefUri tokening with # for connectors,and $ for clients
          val target = executionInfo.asInstanceOf[ExecutionInfo].getSender
            .replace("Actor[", "").replace("]", "").split("#")(0)
          val clientActor = context.actorSelection(target)
          log.info("Send result to: " + target)
          clientActor ! connectResult
        }

      } catch {
        case ex: ExecutionManagerException => {
          log.error("Cannot access queryId actorRef associated value:" + System.lineSeparator() + ex.getMessage)
        }
      }

    case ACKResult(queryId, status) => {
      if(ExecutionManager.MANAGER.exists(queryId) && status == QueryStatus.EXECUTED &&
        ExecutionManager.MANAGER.getValue(queryId).asInstanceOf[ExecutionInfo].isRemoveOnSuccess
        && !ExecutionManager.MANAGER.getValue(queryId).asInstanceOf[ExecutionInfo].isTriggeredByStreaming){
        log.error("Query " + queryId + " failed")
        val executionInfo = ExecutionManager.MANAGER.getValue(queryId)
        if(executionInfo != null){
          val target = executionInfo.asInstanceOf[ExecutionInfo].getSender
            .replace("Actor[", "").replace("]", "").split("#")(0)
          val clientActor = context.actorSelection(target)
          ExecutionManager.MANAGER.deleteEntry(queryId)
          val result = Result.createErrorResult(new ExecutionException("Query failed"))
          result.setQueryId(queryId)
          clientActor ! result
        }
      }
    }

    case result: Result => {
      val queryId = result.getQueryId
      var retryQuery = false
      log.info(s"Receiving result from $sender with queryId = $queryId result: $result")

      try{

        lazy val executionInfo = ExecutionManager.MANAGER.getValue(queryId)

        if(result.isInstanceOf[ErrorResult]){
          log.warning(result.asInstanceOf[ErrorResult].getErrorMessage)
          if (executionInfo != null ){
            val ew = executionInfo.asInstanceOf[ExecutionInfo].getWorkflow
            val nextCandidateQueryWorkflow = ew.getLowPriorityExecutionWorkflow
            if(nextCandidateQueryWorkflow.isPresent){
              log.info(s"Query failed in connector:${ew.getActorRef}. Retrying in another connector.")
              retryQuery = true
              self ! nextCandidateQueryWorkflow.get()
            }
          }
        }

        if(!retryQuery){
          //TODO Add two methods to StringUtils to retrieve AkkaActorRefUri tokening with # for connectors,
          // and $ for clients
          val target = executionInfo.asInstanceOf[ExecutionInfo].getSender
            .replace("Actor[", "").replace("]", "").split("#")(0)
          val clientActor = context.actorSelection(target)

          var sendResultToClient = true

          if(queryId.contains("#")){
            if(queryId.endsWith("#1")){
              result.setQueryId(queryId.split("#")(0))
            } else {
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
            if (executionInfo.asInstanceOf[ExecutionInfo].isUpdateOnSuccess) {
              executionInfo.asInstanceOf[ExecutionInfo].getWorkflow match {

                case mw: MetadataWorkflow =>
                  processUpdateMetadataWorkflow(mw, result)

                case managementWorkflow: ManagementWorkflow => managementWorkflow.getExecutionType match {
                  case ExecutionType.DETACH_CONNECTOR => {
                    for (catalogName <- MetadataManager.MANAGER.getCluster(managementWorkflow.getClusterName).getPersistedCatalogs.asScala.toList){
                      sender ! UpdateMetadata(MetadataManager.MANAGER.getCatalog(catalogName), remove = true)
                    }
                  }
                  case message => log.warning ("Sending metadata updates cannot be performed for the ExecutionType :" + message.toString)
                }

                case _ => log.warning ("Attempt to update the metadata after an operation which is not expected :" +executionInfo.asInstanceOf[ExecutionInfo].getWorkflow.getClass)
              }

            }

            if (executionInfo.asInstanceOf[ExecutionInfo].isRemoveOnSuccess ||
              (result.isInstanceOf[QueryResult] && result.asInstanceOf[QueryResult].isLastResultSet && !executionInfo.asInstanceOf[ExecutionInfo].isTriggeredByStreaming)) {
              ExecutionManager.MANAGER.deleteEntry(queryId)
            }
          }

          if(sendResultToClient) {
            if (executionInfo.asInstanceOf[ExecutionInfo].isTriggeredByStreaming) {
              result match {
                case res: QueryResult => res.setLastResultSet(false)
                case _ =>
              }
            }
            log.info("Send result to: " + target)
            clientActor ! result
          }
        }

      } catch {
        case ex: ExecutionManagerException => {
          log.error("Cannot access queryId actorRef associated value:" + System.lineSeparator() + ex.getMessage)
        }
      }
    }

    //TODO both cases below seem to be unused (ConnectResult and DisconnectResult are sent instead)
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



  def manageWorkflow(queryId: String, workflow: ExecutionWorkflow, explicitSender: Option[String]) = workflow match {

    case metadataWorkflow: MetadataWorkflow => {

      val executionInfo = new ExecutionInfo
      executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
      executionInfo.setWorkflow(metadataWorkflow)

      //Getting the connector name to tell sender
      val connectorsMetadata=MetadataManager.MANAGER.getConnectors(Status.ONLINE);
      val connector=connectorsMetadata.filter(connectorMetadata => connectorMetadata.getActorRefs.contains
        (metadataWorkflow.getActorRef))
      if (connector.size>0)
        sender ! InfoResult(connector.apply(0).getName.getName, metadataWorkflow.getQueryId)
      else
        //Special case of first create catalog statement
        sender ! InfoResult("none", metadataWorkflow.getQueryId)

      if (metadataWorkflow.getExecutionType == ExecutionType.DROP_CATALOG) {

        executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
        executionInfo.setPersistOnSuccess(false)
        executionInfo.setRemoveOnSuccess(true)
        executionInfo.setUpdateOnSuccess(true)
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
        val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_DROP_CATALOG)
        result.setQueryId(queryId)


        explicitSender.fold{
          sender ! result
        }{
          explSender =>
            context.actorSelection(explSender) ! result
        }


      } else if (metadataWorkflow.getExecutionType == ExecutionType.ALTER_CATALOG) {

        executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
        executionInfo.setPersistOnSuccess(true)
        executionInfo.setUpdateOnSuccess(true)
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
        val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_ALTER_CATALOG)
        result.setQueryId(queryId)
        explicitSender.fold{
          sender ! result
        }{
          explSender =>
            context.actorSelection(explSender) ! result
        }

      } else if (metadataWorkflow.getExecutionType == ExecutionType.DROP_TABLE) {

        // Drop table in the Crossdata servers through the MetadataManager
        coordinator.persistDropTable(metadataWorkflow.getTableName)

        // Send action to the connector
        val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
        actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

        // Prepare data for the reply of the connector
        executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
        executionInfo.setPersistOnSuccess(false)
        executionInfo.setRemoveOnSuccess(true)
        executionInfo.setUpdateOnSuccess(true)
        executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
        executionInfo.setWorkflow(metadataWorkflow)
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

      } else if(metadataWorkflow.getExecutionType == ExecutionType.UNREGISTER_TABLE) {
        // Drop table in the Crossdata servers through the MetadataManager
        coordinator.persistDropTable(metadataWorkflow.getTableName)

        var result:MetadataResult = null

        val tableMetadata = metadataWorkflow.getTableMetadata
        updateMetadata(tableMetadata, tableMetadata.getClusterRef, toRemove = true)
        executionInfo.setQueryStatus(QueryStatus.PLANNED)
        result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_UNREGISTER_TABLE)
        result.setQueryId(queryId)
        explicitSender.fold{
          sender ! result
        }{
          explSender =>
            context.actorSelection(explSender) ! result
        }

      } else if (metadataWorkflow.getExecutionType == ExecutionType.ALTER_TABLE) {

        executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
        executionInfo.setPersistOnSuccess(true)
        executionInfo.setRemoveOnSuccess(true)
        executionInfo.setUpdateOnSuccess(true)
        executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
        executionInfo.setWorkflow(metadataWorkflow)
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

        val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
        log.info("ActorRef: " + actorRef.toString())
        actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

      } else if (metadataWorkflow.getExecutionType == ExecutionType.CREATE_INDEX) {

        if (metadataWorkflow.isIfNotExists && MetadataManager.MANAGER.exists(metadataWorkflow.getIndexName)) {
          val result: MetadataResult = MetadataResult.createSuccessMetadataResult(
            MetadataResult.OPERATION_CREATE_INDEX, metadataWorkflow.isIfNotExists)
          result.setQueryId(queryId)
          explicitSender.fold{
            sender ! result
          }{
            explSender =>
              context.actorSelection(explSender) ! result
          }
        } else {
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          executionInfo.setPersistOnSuccess(true)
          executionInfo.setRemoveOnSuccess(true)
          executionInfo.setUpdateOnSuccess(true)
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
          executionInfo.setWorkflow(metadataWorkflow)
          ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

          val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
          log.info("ActorRef: " + actorRef.toString())
          actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()
        }

      } else if (metadataWorkflow.getExecutionType == ExecutionType.DROP_INDEX) {

        if (metadataWorkflow.isIfExists && !MetadataManager.MANAGER.exists(metadataWorkflow.getIndexName)) {
          val result: MetadataResult = MetadataResult.createSuccessMetadataResult(
            MetadataResult.OPERATION_DROP_INDEX, metadataWorkflow.isIfExists)
          result.setQueryId(queryId)
          explicitSender.fold{
            sender ! result
          }{
            explSender =>
              context.actorSelection(explSender) ! result
          }
        } else {
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          executionInfo.setPersistOnSuccess(true)
          executionInfo.setRemoveOnSuccess(true)
          executionInfo.setUpdateOnSuccess(true)
          executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
          executionInfo.setWorkflow(metadataWorkflow)
          ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

          val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
          log.info("ActorRef: " + actorRef.toString())
          actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()
        }

      } else if (metadataWorkflow.getExecutionType == ExecutionType.DISCOVER_METADATA) {

        executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
        executionInfo.setPersistOnSuccess(false)
        executionInfo.setRemoveOnSuccess(true)
        executionInfo.setUpdateOnSuccess(true)
        executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
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
        executionInfo.setUpdateOnSuccess(true)
        executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
        executionInfo.setWorkflow(metadataWorkflow)
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

        val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
        log.info("ActorRef: " + actorRef.toString())
        actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

      } else if (metadataWorkflow.getExecutionType == ExecutionType.CREATE_CATALOG){
        coordinator.persistCreateCatalog(metadataWorkflow.getCatalogMetadata, metadataWorkflow.isIfNotExists)
        executionInfo.setQueryStatus(QueryStatus.EXECUTED)
        val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_CATALOG)
        result.setQueryId(queryId)
        explicitSender.fold{
          sender ! result
        }{
          explSender =>
            context.actorSelection(explSender) ! result
        }

      }else if (metadataWorkflow.getExecutionType == ExecutionType.CREATE_TABLE_AND_CATALOG ||
        metadataWorkflow.getExecutionType == ExecutionType.CREATE_TABLE) {

        if (metadataWorkflow.isIfNotExists && MetadataManager.MANAGER.exists(metadataWorkflow.getTableName)) {
          val result: MetadataResult = MetadataResult.createSuccessMetadataResult(
            MetadataResult.OPERATION_CREATE_TABLE, metadataWorkflow.isIfNotExists)
          result.setQueryId(queryId)
          explicitSender.fold{
            sender ! result
          }{
            explSender =>
              context.actorSelection(explSender) ! result
          }

        } else {

          if (metadataWorkflow.getActorRef != null && metadataWorkflow.getActorRef.length() > 0) {

            val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
            executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
            executionInfo.setPersistOnSuccess(true)
            executionInfo.setUpdateOnSuccess(true)
            ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
            log.info("ActorRef: " + actorRef.toString())

            actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()

          } else {
            throw new CoordinationException("Actor ref URI is null");
          }

        }

      } else if (metadataWorkflow.getExecutionType == ExecutionType.CREATE_TABLE_REGISTER_CATALOG) {
        //Connector is able to create the catalog of the registered table
        if (metadataWorkflow.getActorRef != null && metadataWorkflow.getActorRef.length() > 0) {
          val actorRef = context.actorSelection(metadataWorkflow.getActorRef)
          executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
          executionInfo.setPersistOnSuccess(true)
          executionInfo.setUpdateOnSuccess(true)
          ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
          log.info("ActorRef: " + actorRef.toString())

          actorRef.asInstanceOf[ActorSelection] ! metadataWorkflow.createMetadataOperationMessage()
        } else {
          throw new CoordinationException("Actor ref URI is null");

        }
      }else if (metadataWorkflow.getExecutionType == ExecutionType.REGISTER_TABLE
        || metadataWorkflow.getExecutionType == ExecutionType.REGISTER_TABLE_AND_CATALOG) {

        if (metadataWorkflow.isIfNotExists && MetadataManager.MANAGER.exists(metadataWorkflow.getTableName)) {
          val result: MetadataResult = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_REGISTER_TABLE, metadataWorkflow.isIfNotExists)
          result.setQueryId(queryId)
          explicitSender.fold{
            sender ! result
          }{
            explSender =>
              context.actorSelection(explSender) ! result
          }

        } else {

          val result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_REGISTER_TABLE)
          if (metadataWorkflow.getExecutionType == ExecutionType.REGISTER_TABLE_AND_CATALOG) {
            coordinator.persistCreateCatalogInCluster(metadataWorkflow.getCatalogName, metadataWorkflow.getClusterName)
          }
          //Connector is not able to create the catalog of the registered table
          coordinator.persistCreateTable(metadataWorkflow.getTableMetadata)
          val tableMetadata = metadataWorkflow.getTableMetadata
          updateMetadata(tableMetadata, tableMetadata.getClusterRef, toRemove = false)
          executionInfo.setQueryStatus(QueryStatus.EXECUTED)

          result.setQueryId(queryId)
          explicitSender.fold{
            sender ! result
          }{
            explSender =>
              context.actorSelection(explSender) ! result
          }

        }

      } else {
        throw new CoordinationException("Operation not supported yet");
      }
    }



    case storageWorkflow: StorageWorkflow => {
      log.debug("CoordinatorActor: StorageWorkflow received")
      val executionInfo = new ExecutionInfo
      executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), false))
      executionInfo.setWorkflow(storageWorkflow)
      executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)

      log.info("\nCoordinate workflow: " + storageWorkflow.toString)

      if ((storageWorkflow.getPreviousExecutionWorkflow == null)
        && (ResultType.RESULTS.equals(storageWorkflow.getResultType))) {

        executionInfo.setRemoveOnSuccess(true)
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo)

        //Getting the connector name to tell sender
        val connectorsMetadata=MetadataManager.MANAGER.getConnectors(Status.ONLINE);
        val connector=connectorsMetadata.filter(connectorMetadata => connectorMetadata.getActorRefs.contains(storageWorkflow.getActorRef))
        sender ! InfoResult(connector.apply(0).getName.getName, storageWorkflow.getQueryId)

        val actorRef = context.actorSelection(storageWorkflow.getActorRef())
        actorRef ! storageWorkflow.getStorageOperation()

      } else if ((storageWorkflow.getPreviousExecutionWorkflow != null)
        && (ResultType.TRIGGER_EXECUTION.equals(storageWorkflow.getPreviousExecutionWorkflow.getResultType))) {


        val storedExInfo = new ExecutionInfo
        storedExInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), false))
        val previousExecutionWorkflow = storageWorkflow.getPreviousExecutionWorkflow.asInstanceOf[QueryWorkflow]
        storedExInfo.setWorkflow(previousExecutionWorkflow)
        storedExInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
        storedExInfo.setRemoveOnSuccess(Execute.getClass.isInstance(previousExecutionWorkflow.getExecuteOperation("")))
        storedExInfo.setTriggeredByStreaming(true)
        ExecutionManager.MANAGER.createEntry(queryId, storedExInfo)

        val actorRef = StringUtils.getAkkaActorRefUri(previousExecutionWorkflow.getActorRef, false)
        val firstConnectorRef = context.actorSelection(actorRef)

        log.info(s"Sending init trigger operation: ${queryId} to $firstConnectorRef")
        firstConnectorRef ! TriggerExecution(previousExecutionWorkflow, executionInfo)

      }
    }


    case managementWorkflow: ManagementWorkflow => {

      log.info("ManagementWorkflow received")
      var sendResultToClient = true

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
        executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), false))
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

        createExecutionInfoForDetach(managementWorkflow, queryId)

      } else if (managementWorkflow.getExecutionType == ExecutionType.FORCE_DETACH_CONNECTOR) {

        val actorRefs = managementWorkflow.getActorRefs
        for(actorRef <- actorRefs){
          if(actorRef != null){
            val connectorSelection = context.actorSelection(StringUtils.getAkkaActorRefUri(actorRef, false))
            connectorSelection ! new DisconnectFromCluster(queryId, managementWorkflow.getConnectorClusterConfig.getName.getName)
          }
        }

        createExecutionInfoForDetach(managementWorkflow, queryId)
      }

      if(sendResultToClient){
        explicitSender.fold{
          sender ! coordinator.executeManagementOperation(managementWorkflow.createManagementOperationMessage())
        }{
          explSender =>
            context.actorSelection(explSender) ! coordinator.executeManagementOperation(managementWorkflow.createManagementOperationMessage)
        }
      }

    }


    case queryWorkflow: QueryWorkflow => {
      log.info("\nCoordinatorActor: QueryWorkflow received")
      val executionInfo = new ExecutionInfo
      executionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))
      executionInfo.setWorkflow(queryWorkflow)

      log.info("\nCoordinate workflow: " + queryWorkflow.toString)
      executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
      //Getting the connector name to tell sender
      val connectorsMetadata=MetadataManager.MANAGER.getConnectors(Status.ONLINE);
      val connector=connectorsMetadata.filter(connectorMetadata => connectorMetadata.getActorRefs.contains(executionInfo.getWorkflow.getActorRef))

      if (ResultType.RESULTS.equals(queryWorkflow.getResultType)) {

        //TODO AkkaRefURI should be stored. Indeed, it is likely to be stored instead of toString.
        val actorRef = StringUtils.getAkkaActorRefUri(queryWorkflow.getActorRef, false)
        val actorSelection = context.actorSelection(actorRef)
        val operation = queryWorkflow.getExecuteOperation(queryId)
        executionInfo.setRemoveOnSuccess(operation.isInstanceOf[Execute])
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

        //Send to sender in which connector will be executed the query
        sender ! InfoResult(connector.apply(0).getName.getName , executionInfo.getWorkflow.getQueryId)

        actorSelection.asInstanceOf[ActorSelection] ! operation
        log.info("\nMessage sent to " + actorRef.toString())

      } else if (ResultType.TRIGGER_EXECUTION.equals(queryWorkflow.getResultType)) {

        val actorRef = StringUtils.getAkkaActorRefUri(queryWorkflow.getActorRef, false)
        val firstConnectorActorSelection = context.actorSelection(actorRef)
        val isWindowFound = QueryWorkflow.checkStreaming(queryWorkflow.getWorkflow.getLastStep)

        executionInfo.setTriggeredByStreaming(isWindowFound)
        executionInfo.setRemoveOnSuccess(!isWindowFound)
        ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)

        val nextExecutionInfo = new ExecutionInfo
        nextExecutionInfo.setSender(StringUtils.getAkkaActorRefUri(explicitSender.getOrElse(sender), true))

        nextExecutionInfo.setWorkflow(queryWorkflow.getNextExecutionWorkflow)
        nextExecutionInfo.getWorkflow.setTriggerStep(executionInfo.getWorkflow.getTriggerStep)
        nextExecutionInfo.setRemoveOnSuccess(!isWindowFound)
        nextExecutionInfo.setTriggeredByStreaming(isWindowFound)

        //Send to sender in which connector will be executed the query
        sender ! InfoResult(connector.apply(0).getName.getName , executionInfo.getWorkflow.getQueryId)
        firstConnectorActorSelection ! TriggerExecution(queryWorkflow, nextExecutionInfo)
        log.info(s"Sending init trigger operation: ${queryId} to $firstConnectorActorSelection")

      }
    }

    case _ => {
      log.error("Non recognized workflow")
    }
  }



  def createExecutionInfoForDetach(managementWorkflow: ManagementWorkflow, queryId: String): Unit = {
    val executionInfo = new ExecutionInfo()
    executionInfo.setQueryStatus(QueryStatus.IN_PROGRESS)
    executionInfo.setSender(StringUtils.getAkkaActorRefUri(sender, false))
    executionInfo.setWorkflow(managementWorkflow)
    executionInfo.setUpdateOnSuccess(true)
    ExecutionManager.MANAGER.createEntry(queryId, executionInfo, true)
  }

  private def processUpdateMetadataWorkflow(mw: MetadataWorkflow, result: Result) = mw.getExecutionType match {

    case ExecutionType.CREATE_TABLE |  ExecutionType.ALTER_TABLE => {
      val tableMetadata = mw.getTableMetadata
      updateMetadata(tableMetadata, tableMetadata.getClusterRef, toRemove = false)
    }

    case ExecutionType.CREATE_TABLE_AND_CATALOG | ExecutionType.CREATE_TABLE_REGISTER_CATALOG => {
      val tableMetadata = mw.getTableMetadata
      //TODO updateMetadata(mw.getCatalogMetadata, ...)?
      updateMetadata(tableMetadata, tableMetadata.getClusterRef, toRemove = false)
    }

    case ExecutionType.CREATE_INDEX | ExecutionType.DROP_INDEX => ()

    case ExecutionType.DROP_TABLE => {
      val tableMetadata = mw.getTableMetadata
      updateMetadata(tableMetadata, tableMetadata.getClusterRef, toRemove = true)
    }

    case ExecutionType.ALTER_CATALOG | ExecutionType.CREATE_CATALOG  => {
      val catalogMetadata = mw.getCatalogMetadata

      for (tableMetadata <- mw.getCatalogMetadata.getTables.values.asScala.toList){
        updateMetadata(catalogMetadata, tableMetadata.asInstanceOf[TableMetadata].getClusterRef, toRemove = false)
      }
    }

    case ExecutionType.DROP_CATALOG => {
      val catalogMetadata = mw.getCatalogMetadata
      for (tableMetadata <- mw.getCatalogMetadata.getTables.values.asScala.toList)
      yield updateMetadata(catalogMetadata, tableMetadata.asInstanceOf[TableMetadata].getClusterRef, toRemove = true)
    }

    case ExecutionType.IMPORT_TABLE => {
      for {
        tableMetadata <- result.asInstanceOf[MetadataResult].getTableList.asScala.toList
      } yield updateMetadata(tableMetadata, tableMetadata.asInstanceOf[TableMetadata].getClusterRef, toRemove = false)
    }

    case ExecutionType.IMPORT_CATALOGS | ExecutionType.IMPORT_CATALOG => {
      for {
        catalogMetadata <- result.asInstanceOf[MetadataResult].getCatalogMetadataList.asScala.toList
      } yield updateMetadata(catalogMetadata, toRemove = false)
    }

    case message => log.warning ("Sending metadata updates cannot be performed for the ExecutionType :" + message.toString)
  }



  /**
   * Send a message to the connectors attached to a cluster to update its metadata. It is called any time an update's operation which persist data is finished.
   * @param uMetadata the new metadata to be updated
   * @param clusterInvolved the cluster which contains the metadata to update
   * @param toRemove whether the metadata has been created or deleted
   */
  private def updateMetadata(uMetadata: UpdatableMetadata, clusterInvolved: ClusterName, toRemove: Boolean): Unit = {

    val listConnectorMetadata = MetadataManager.MANAGER.getAttachedConnectors(Status.ONLINE, clusterInvolved)
    listConnectorMetadata.asScala.toList.flatMap(actorToBroadcast).foreach(actor => broadcastMetadata(actor, uMetadata))

    def actorToBroadcast(cMetadata: ConnectorMetadata): List[ActorSelection] =
      StringUtils.getAkkaActorRefUri(cMetadata.getActorRef(host), false) match {
        case null => List()
        case strActorRefUri => List(context.actorSelection(strActorRefUri))
      }
    def broadcastMetadata(bcConnectorActor: ActorSelection, uMetadata: UpdatableMetadata) = {
      log.debug("Updating metadata in " + bcConnectorActor.toString)
      bcConnectorActor ! UpdateMetadata(uMetadata, toRemove)
    }
  }

  /**
   * Send a message to the connectors attached to a cluster to update its metadata. It is called any time an update's operation which persist data is finished.
   * @param cMetadata the new metadata to be updated
   * @param toRemove whether the metadata has been created or deleted
   */
  private def updateMetadata(cMetadata: CatalogMetadata, toRemove: Boolean): Unit = {

    val setConnectorMetadata = cMetadata.getTables.values().asScala.toList.flatMap( tableMetadata => MetadataManager.MANAGER.getAttachedConnectors(Status.ONLINE, tableMetadata.getClusterRef).asScala.toList).toSet

    setConnectorMetadata.flatMap(actorToBroadcast).foreach(actor => broadcastMetadata(actor, cMetadata))

    def actorToBroadcast(cMetadata: ConnectorMetadata): List[ActorSelection] =
      StringUtils.getAkkaActorRefUri(cMetadata.getActorRef(host), false) match {
        case null => List()
        case strActorRefUri => List(context.actorSelection(strActorRefUri))
      }
    def broadcastMetadata(bcConnectorActor: ActorSelection, uMetadata: UpdatableMetadata) = {
      log.debug("Updating metadata in " + bcConnectorActor.toString)
      bcConnectorActor ! UpdateMetadata(uMetadata, toRemove)
    }
  }

}
