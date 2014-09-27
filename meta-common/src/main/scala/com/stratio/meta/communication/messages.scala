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

import java.util

import com.stratio.meta.common.connector.ConnectorClusterConfig
import com.stratio.meta.common.data.Row
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.result.QueryStatus
import com.stratio.meta.common.security.ICredentials
import com.stratio.meta2.common.data.{CatalogName, ClusterName, TableName}
import com.stratio.meta2.common.metadata.{CatalogMetadata, IndexMetadata, TableMetadata}

case class ACK(queryId: String, status: QueryStatus)

case class Connect(credentials: ICredentials, connectorClusterConfig: ConnectorClusterConfig)

case class Reply(msg: String)

case class Disconnect(userId: String)

case class ExecutionError(queryId: String, exception: Exception)

//Connector messages
case class ConnectToConnector(msg: String)

case class DisconnectFromConnector(msg: String)

case class Request(msg: String)

case class Response(msg: String)

case class MetadataStruct(clusterName: String, connectorName: String, metadata: String)

case class StorageQueryStruct(clusterName: String, connectorName: String, storageQuery: String)

case class WorkflowStruct(clusterName: String, connectorName: String, workFlow: LogicalWorkflow)

case class replyConnectorName(name: String)

case class getConnectorName()

//IStorageEngine

sealed abstract class StorageOperation

case class Insert(queryId:String,targetCluster: ClusterName, targetTable: TableMetadata,
row: Row) extends StorageOperation

case class InsertBatch(queryId:String,targetCluster: ClusterName, targetTable: TableMetadata,
                       rows: util.Collection[Row]) extends StorageOperation

//IQueryEngine
case class Execute(workflow: LogicalWorkflow)

//IMetadataEngine

sealed abstract class MetadataOperation

case class CreateCatalog(queryId:String,targetCluster: ClusterName, catalogMetadata: CatalogMetadata) extends
MetadataOperation

case class DropCatalog(queryId:String,argetCluster: ClusterName, catalogName: CatalogName) extends MetadataOperation

case class CreateTable(queryId:String,targetCluster: ClusterName, tableMetadata: TableMetadata) extends
MetadataOperation

case class DropTable(queryId:String,targetCluster: ClusterName, tableName: TableName) extends MetadataOperation

case class CreateIndex(queryId:String,targetCluster: ClusterName, indexMetadata: IndexMetadata) extends
MetadataOperation

case class DropIndex(queryId:String,targetCluster: ClusterName, indexMetadata: IndexMetadata) extends MetadataOperation


