/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.common.executionplan;

import java.io.Serializable;

import com.stratio.meta.communication.CreateCatalog;
import com.stratio.meta.communication.CreateIndex;
import com.stratio.meta.communication.CreateTable;
import com.stratio.meta.communication.DropCatalog;
import com.stratio.meta.communication.DropIndex;
import com.stratio.meta.communication.DropTable;
import com.stratio.meta.communication.MetadataOperation;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;

/**
 * Execute a {@link com.stratio.meta.common.connector.IMetadataEngine} operation.
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
     * @param queryId Query identifer.
     * @param actorRef      Target actor reference.
     * @param executionType Type of execution.
     * @param type          Type of results.
     */
    public MetadataWorkflow(String queryId, Serializable actorRef, ExecutionType executionType,
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

    public MetadataOperation getMetadataOperation(){
        MetadataOperation result = null;
        if(ExecutionType.CREATE_CATALOG.equals(this.executionType)){
            result = new CreateCatalog(this.getQueryId(),this.clusterName, this.catalogMetadata);
        }else if(ExecutionType.DROP_CATALOG.equals(this.executionType)){
            result = new DropCatalog(this.getQueryId(),this.clusterName, this.catalogName);
        }else if(ExecutionType.CREATE_TABLE.equals(this.executionType)){
            result = new CreateTable(this.getQueryId(),this.clusterName, this.tableMetadata);
        }else if(ExecutionType.DROP_TABLE.equals(this.executionType)){
            result = new DropTable(this.getQueryId(),this.clusterName, this.tableName);
        }else if(ExecutionType.CREATE_INDEX.equals(this.executionType)){
            result = new CreateIndex(this.getQueryId(),this.clusterName, this.indexMetadata);
        }else if(ExecutionType.DROP_INDEX.equals(this.executionType)){
            result = new DropIndex(this.getQueryId(),this.clusterName, this.indexMetadata);
        }
        return result;
    }


}

