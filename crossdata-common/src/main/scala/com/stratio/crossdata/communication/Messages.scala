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

package com.stratio.crossdata.communication

import java.util

import com.stratio.crossdata.common.ask.{Command, Query}
import com.stratio.crossdata.common.connector.ConnectorClusterConfig
import com.stratio.crossdata.common.logicalplan.Filter
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow
import com.stratio.crossdata.common.metadata.{CatalogMetadata, IndexMetadata, TableMetadata}
import com.stratio.crossdata.common.result.QueryStatus
import com.stratio.crossdata.common.security.ICredentials
import com.stratio.crossdata.common.statements.structures.{Relation, Selector}

/**
 * Parent class for all operations to be executed on CONNECTOR Actors.
 * @param queryId The query identifier.
 */

@SerialVersionUID(-415564237894752659L)
class Operation(val queryId: String) extends Serializable

@SerialVersionUID(-485628367894852859L)
case class ReroutedQuery(msg:Query)

@SerialVersionUID(-487562836787485759L)
case class ReroutedCommand(msg:Command)

@SerialVersionUID(-415562236789472659L)
case class ACK(queryId: String, status: QueryStatus)

@SerialVersionUID(-422564236794752659L)
case class Connect(queryId: String, credentials: ICredentials, connectorClusterConfig: ConnectorClusterConfig)

@SerialVersionUID(-225564237894752659L)
case class Reply(msg: String)

@SerialVersionUID(-415562367894752622L)
case class Disconnect(userId: String)

@SerialVersionUID(-315643667894592648L)
case class DisconnectFromCluster(override val queryId: String, clusterName: String) extends Operation(queryId)

//CONNECTOR messages
@SerialVersionUID(-415564236789422659L)
case class ConnectToConnector(msg: String)

@SerialVersionUID(-335564237894752659L)
case class DisconnectFromConnector(msg: String)

@SerialVersionUID(-413364236784752659L)
case class Request(msg: String)

@SerialVersionUID(-415564236789472633L)
case class Response(msg: String)

@SerialVersionUID(-555564236789475265L)
case class MetadataStruct(clusterName: String, connectorName: String, metadata: String)

@SerialVersionUID(-415554367894752659L)
case class StorageQueryStruct(clusterName: String, connectorName: String, storageQuery: String)

@SerialVersionUID(-415542367855752659L)
case class WorkflowStruct(clusterName: String, connectorName: String, workFlow: LogicalWorkflow)

@SerialVersionUID(-415564236794755559L)
case class replyConnectorName(name: String)

@SerialVersionUID(-665564236789475259L)
case class getConnectorName()

@SerialVersionUID(-665564236789475258L)
case class GetCatalogs(clusterName: ClusterName)

@SerialVersionUID(-665564236789475257L)
case class GetTableMetadata(clusterName: ClusterName, tableName: TableName)

@SerialVersionUID(-665564236789475256L)
case class GetCatalogMetadata(catalogName: CatalogName)


// ============================================================================
//                                IStorageEngine
// ============================================================================


sealed abstract class StorageOperation(queryId: String) extends Operation(queryId)

case class Insert(override val queryId: String, targetCluster: ClusterName, targetTable: TableMetadata,
                  row: Row, ifNotExists: Boolean) extends StorageOperation(queryId)

case class InsertBatch(override val queryId: String, targetCluster: ClusterName, targetTable: TableMetadata,
                       rows: util.Collection[Row], ifNotExists: Boolean) extends StorageOperation(queryId)

case class DeleteRows(override val queryId: String, targetCluster: ClusterName, targetTable: TableName,
                      whereClauses: util.Collection[Filter]) extends StorageOperation(queryId)

case class Update(override val queryId: String, targetCluster: ClusterName, targetTable: TableName,
                  assignments: util.Collection[Relation], whereClauses: util.Collection[Filter])
    extends StorageOperation(queryId)

case class Truncate(override val queryId: String, targetCluster: ClusterName, targetTable: TableName) extends
    StorageOperation(queryId)

// ============================================================================
//                                IQueryEngine
// ============================================================================

sealed abstract class ExecuteOperation(queryId: String) extends Operation(queryId)

case class Execute(override val queryId: String, workflow: LogicalWorkflow) extends ExecuteOperation(queryId)

case class AsyncExecute(override val queryId: String, workflow: LogicalWorkflow) extends ExecuteOperation(queryId)

case class PagedExecute(override val queryId: String, workflow: LogicalWorkflow, pageSize: Int) extends ExecuteOperation(queryId)

// ============================================================================
//                                IMetadataEngine
// ============================================================================

sealed abstract class MetadataOperation(queryId: String) extends Operation(queryId)

case class CreateCatalog(override val queryId: String, targetCluster: ClusterName, catalogMetadata: CatalogMetadata) extends
  MetadataOperation(queryId)

case class AlterCatalog(override val queryId: String, targetCluster: ClusterName, catalogMetadata: CatalogMetadata)
  extends MetadataOperation(queryId)

case class DropCatalog(override val queryId: String, targetCluster: ClusterName, catalogName: CatalogName) extends MetadataOperation(queryId)

case class CreateTable(override val queryId: String, targetCluster: ClusterName, tableMetadata: TableMetadata) extends
MetadataOperation(queryId)

case class CreateTableAndCatalog(override val queryId: String, targetCluster: ClusterName, catalogMetadata: CatalogMetadata,
                                 tableMetadata: TableMetadata) extends
MetadataOperation(queryId)

case class DropTable(override val queryId: String, targetCluster: ClusterName, tableName: TableName) extends MetadataOperation(queryId)

case class AlterTable(override val queryId: String, targetCluster: ClusterName,
                      tableName: TableName, alterOptions: AlterOptions) extends MetadataOperation(queryId)

case class CreateIndex(override val queryId: String, targetCluster: ClusterName, indexMetadata: IndexMetadata) extends
MetadataOperation(queryId)

case class DropIndex(override val queryId: String, targetCluster: ClusterName, indexMetadata: IndexMetadata) extends
MetadataOperation(queryId)

case class ProvideMetadata(override val queryId: String, targetCluster: ClusterName) extends MetadataOperation(queryId)

case class ProvideCatalogsMetadata(override val queryId: String, targetCluster: ClusterName) extends MetadataOperation(queryId)

case class ProvideCatalogMetadata(override val queryId: String, targetCluster: ClusterName,
                                 catalogName: CatalogName) extends MetadataOperation(queryId)

case class ProvideTableMetadata(override val queryId: String, targetCluster: ClusterName,
                               tableName: TableName) extends MetadataOperation(queryId)


case class SMetadata(override val queryId: String, targetCluster: ClusterName) extends MetadataOperation(queryId)
// ============================================================================
//                                ManagementOperation
// ============================================================================

sealed abstract class ManagementOperation(queryId: String) extends Operation(queryId)

case class AttachCluster(override val queryId: String, targetCluster: ClusterName, datastoreName: DataStoreName,
                         options: java.util.Map[Selector, Selector]) extends ManagementOperation(queryId)

case class AlterCluster(override val queryId: String, targetCluster: ClusterName, datastoreName: DataStoreName,
                         options: java.util.Map[Selector, Selector]) extends ManagementOperation(queryId)

case class DetachCluster(override val queryId: String, targetCluster: ClusterName,
                         datastoreName:DataStoreName) extends ManagementOperation(queryId)

case class AttachConnector(override val queryId: String, targetCluster: ClusterName, connectorName: ConnectorName, options: java.util.Map[Selector, Selector],
                           priority: Int, pageSize: Int) extends ManagementOperation(queryId)


case class DetachConnector(override val queryId: String, targetCluster: ClusterName,
                           connectorName: ConnectorName) extends ManagementOperation(queryId)


