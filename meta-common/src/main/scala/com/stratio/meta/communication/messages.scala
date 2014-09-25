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
import com.stratio.meta.common.executionplan.ExecutionWorkflow
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.result.QueryStatus
import com.stratio.meta.common.security.ICredentials
import com.stratio.meta2.common.data.ClusterName
import com.stratio.meta2.common.metadata.{IndexMetadata, CatalogMetadata, TableMetadata}
import com.stratio.meta.common.data.Row
import java.util

case class ACK(queryId: String, status: QueryStatus)

case class Connect(credentials: ICredentials, connectorClusterConfig: ConnectorClusterConfig)

case class Reply(msg: String)

case class Disconnect(userId: String)

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
case class Insert(targetCluster: ClusterName, targetTable: TableMetadata, row: Row)

case class InsertBatch(targetCluster: ClusterName, targetTable: TableMetadata, rows: util.Collection[Row])

//IQueryEngine
case class Execute(workflow: LogicalWorkflow)

//IMetadataEngine
case class CreateCatalog(targetCluster: ClusterName, catalogMetadata: CatalogMetadata)

case class CreateTable(targetCluster: ClusterName, tableMetadata: TableMetadata)

case class DropCatalog(targetCluster: ClusterName, catalogName: String)

case class CreateIndex(targetCluster: ClusterName, indexMetadata: IndexMetadata)

case class DropIndex(targetCluster: ClusterName, indexMetadata: IndexMetadata)


