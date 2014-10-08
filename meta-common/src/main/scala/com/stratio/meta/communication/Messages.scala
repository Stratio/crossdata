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

package com.stratio.meta.communication

import com.stratio.meta.common.connector.ConnectorClusterConfig
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.result.QueryStatus
import com.stratio.meta.common.security.ICredentials
import com.stratio.meta2.common.data._
import com.stratio.meta2.common.metadata.{IndexMetadata, CatalogMetadata, TableMetadata}
import com.stratio.meta2.common.statements.structures.selectors.Selector
import com.stratio.meta.common.data.Row
import java.util

case class ACK(queryId: String, status: QueryStatus)

case class Connect(credentials: ICredentials, connectorClusterConfig: ConnectorClusterConfig)

case class Reply(msg: String)

case class Disconnect(userId: String)

//CONNECTOR messages
case class ConnectToConnector(msg: String)

case class DisconnectFromConnector(msg: String)

case class Request(msg: String)

case class Response(msg: String)

case class MetadataStruct(clusterName: String, connectorName: String, metadata: String)

case class StorageQueryStruct(clusterName: String, connectorName: String, storageQuery: String)

case class WorkflowStruct(clusterName: String, connectorName: String, workFlow: LogicalWorkflow)

case class replyConnectorName(name: String)

case class getConnectorName()

/**
 * Parent class for all operations to be executed on CONNECTOR Actors.
 * @param queryId The query identifier.
 */
class Operation(val queryId: String)

// ============================================================================
//                                IStorageEngine
// ============================================================================


sealed abstract class StorageOperation(queryId: String) extends Operation(queryId)

case class Insert(override val queryId: String, targetCluster: ClusterName, targetTable: TableMetadata,
                  row: Row) extends StorageOperation(queryId)

case class InsertBatch(override val queryId: String, targetCluster: ClusterName, targetTable: TableMetadata,
                       rows: util.Collection[Row]) extends StorageOperation(queryId)

// ============================================================================
//                                IQueryEngine
// ============================================================================

case class Execute(override val queryId: String, workflow: LogicalWorkflow) extends Operation(queryId)

// ============================================================================
//                                IMetadataEngine
// ============================================================================

sealed abstract class MetadataOperation(queryId: String) extends Operation(queryId)

case class CreateCatalog(override val queryId: String, targetCluster: ClusterName, catalogMetadata: CatalogMetadata) extends
MetadataOperation(queryId)

case class DropCatalog(override val queryId: String, targetCluster: ClusterName, catalogName: CatalogName) extends MetadataOperation(queryId)

case class CreateTable(override val queryId: String, targetCluster: ClusterName, tableMetadata: TableMetadata) extends
MetadataOperation(queryId)

case class CreateTableAndCatalog(override val queryId: String, targetCluster: ClusterName, catalogMetadata: CatalogMetadata,
                                 tableMetadata: TableMetadata) extends
MetadataOperation(queryId)

case class DropTable(override val queryId: String, targetCluster: ClusterName, tableName: TableName) extends MetadataOperation(queryId)

case class CreateIndex(override val queryId: String, targetCluster: ClusterName, indexMetadata: IndexMetadata) extends
MetadataOperation(queryId)

case class DropIndex(override val queryId: String, targetCluster: ClusterName, indexMetadata: IndexMetadata) extends
MetadataOperation(queryId)

// ============================================================================
//                                ManagementOperation
// ============================================================================

sealed abstract class ManagementOperation(queryId: String) extends Operation(queryId)

case class AttachCluster(override val queryId: String, targetCluster: ClusterName, datastoreName: DataStoreName,
                         options: java.util.Map[Selector, Selector]) extends ManagementOperation(queryId)

case class DetachCluster(override val queryId: String, targetCluster: ClusterName) extends ManagementOperation(queryId)

case class AttachConnector(override val queryId: String, targetCluster: ClusterName,
                           connectorName: ConnectorName, options: java.util.Map[Selector, Selector]) extends ManagementOperation(queryId)

case class DetachConnector(override val queryId: String, targetCluster: ClusterName,
                           connectorName: ConnectorName) extends ManagementOperation(queryId)
