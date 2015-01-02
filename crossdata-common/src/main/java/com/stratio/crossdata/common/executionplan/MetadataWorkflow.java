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

import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.communication.AlterCatalog;
import com.stratio.crossdata.communication.AlterTable;
import com.stratio.crossdata.communication.CreateCatalog;
import com.stratio.crossdata.communication.CreateIndex;
import com.stratio.crossdata.communication.CreateTable;
import com.stratio.crossdata.communication.CreateTableAndCatalog;
import com.stratio.crossdata.communication.DropCatalog;
import com.stratio.crossdata.communication.DropIndex;
import com.stratio.crossdata.communication.DropTable;
import com.stratio.crossdata.communication.MetadataOperation;
import com.stratio.crossdata.communication.ProvideCatalogMetadata;
import com.stratio.crossdata.communication.ProvideCatalogsMetadata;
import com.stratio.crossdata.communication.ProvideMetadata;
import com.stratio.crossdata.communication.ProvideTableMetadata;

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

    private AlterOptions alterOptions = null;

    private boolean ifExists = false;

    private boolean ifNotExists = false;

    /**
     * Class constructor.
     *
     * @param queryId       Query identifier.
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

    public AlterOptions getAlterOptions() {
        return alterOptions;
    }

    public void setAlterOptions(AlterOptions alterOptions) {
        this.alterOptions = alterOptions;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public MetadataOperation createMetadataOperationMessage() {
        MetadataOperation result = null;

        switch (this.executionType) {
        case CREATE_CATALOG:
            result = new CreateCatalog(queryId, this.clusterName, this.catalogMetadata);
            break;
        case ALTER_CATALOG:
            result = new AlterCatalog(queryId, this.clusterName, this.catalogMetadata);
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
        case ALTER_TABLE:
            result= new AlterTable(queryId, this.clusterName, this.tableName, this.alterOptions);
            break;
        case CREATE_INDEX:
            result = new CreateIndex(queryId, this.clusterName, this.indexMetadata);
            break;
        case DROP_INDEX:
            result = new DropIndex(queryId, this.clusterName, this.indexMetadata);
            break;
        case DISCOVER_METADATA:
            result = new ProvideMetadata(queryId, this.clusterName);
            break;
        case IMPORT_CATALOGS:
            result = new ProvideCatalogsMetadata(queryId, this.clusterName);
            break;
        case IMPORT_CATALOG:
            result = new ProvideCatalogMetadata(queryId, this.clusterName, this.catalogName);
            break;
        case IMPORT_TABLE:
            result = new ProvideTableMetadata(queryId, this.clusterName, this.tableName);
            break;
        default:
            break;
        }

        return result;
    }

}

