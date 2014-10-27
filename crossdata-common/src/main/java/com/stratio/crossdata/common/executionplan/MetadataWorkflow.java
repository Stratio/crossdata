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

package com.stratio.crossdata.common.executionplan;


import com.stratio.crossdata.communication.CreateCatalog;
import com.stratio.crossdata.communication.CreateIndex;
import com.stratio.crossdata.communication.CreateTable;
import com.stratio.crossdata.communication.CreateTableAndCatalog;
import com.stratio.crossdata.communication.DropCatalog;
import com.stratio.crossdata.communication.DropIndex;
import com.stratio.crossdata.communication.DropTable;
import com.stratio.crossdata.communication.MetadataOperation;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;

/**
 * Execute a {@link com.stratio.crossdata.common.connector.IMetadataEngine} operation.
 */
public class MetadataWorkflow extends ExecutionWorkflow {

    private CatalogName catalogName = null;

    private ClusterName clusterName = null;

    private TableName tableName = null;

    private CatalogMetadata catalogMetadata = null;

    private TableMetadata tableMetadata = null;

    private IndexMetadata indexMetadata = null;

    /**
     * Class constructor.
     *
     * @param queryId       Query identifer.
     * @param actorRef      Target actor reference.
     * @param executionType Type of execution.
     * @param type          Type of results.
     */
    public MetadataWorkflow(String queryId, String actorRef, ExecutionType executionType,
            ResultType type) {
        super(queryId, actorRef, executionType, type);
    }

    public void setCatalogName(CatalogName catalogName) {
        this.catalogName = catalogName;
    }

    public void setClusterName(ClusterName clusterName) {
        this.clusterName = clusterName;
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    public void setCatalogMetadata(CatalogMetadata catalogMetadata) {
        this.catalogMetadata = catalogMetadata;
    }

    public void setTableMetadata(TableMetadata tableMetadata) {
        this.tableMetadata = tableMetadata;
    }

    public void setIndexMetadata(IndexMetadata indexMetadata) {
        this.indexMetadata = indexMetadata;
    }

    public CatalogName getCatalogName() {
        return catalogName;
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public TableName getTableName() {
        return tableName;
    }

    public CatalogMetadata getCatalogMetadata() {
        return catalogMetadata;
    }

    public TableMetadata getTableMetadata() {
        return tableMetadata;
    }

    public IndexMetadata getIndexMetadata() {
        return indexMetadata;
    }

    public MetadataOperation createMetadataOperationMessage() {
        MetadataOperation result = null;

        switch (this.executionType) {
        case CREATE_CATALOG:
            result = new CreateCatalog(queryId, this.clusterName, this.catalogMetadata);
            break;
        case DROP_CATALOG:
            result = new DropCatalog(queryId, this.clusterName, this.catalogName);
            break;
        case CREATE_TABLE:
            result = new CreateTable(queryId, this.clusterName, this.tableMetadata);
            break;
        case CREATE_TABLE_AND_CATALOG:
            result = new CreateTableAndCatalog(queryId, this.clusterName, this.catalogMetadata, this.tableMetadata);
            break;
        case DROP_TABLE:
            result = new DropTable(queryId, this.clusterName, this.tableName);
            break;
        case CREATE_INDEX:
            result = new CreateIndex(queryId, this.clusterName, this.indexMetadata);
            break;
        case DROP_INDEX:
            result = new DropIndex(queryId, this.clusterName, this.indexMetadata);
            break;
        default: break;
        }

        return result;
    }

}

