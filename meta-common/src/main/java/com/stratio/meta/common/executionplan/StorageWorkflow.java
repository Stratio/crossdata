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
import java.util.Collection;

import com.stratio.meta.common.data.Row;
import com.stratio.meta.communication.Insert;
import com.stratio.meta.communication.InsertBatch;
import com.stratio.meta.communication.StorageOperation;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.metadata.TableMetadata;

/**
 * Storage related operations.
 */
public class StorageWorkflow extends ExecutionWorkflow{

    private ClusterName clusterName = null;

    private TableMetadata tableMetadata = null;

    private Row row = null;

    private Collection<Row> rows = null;

    /**
     * Class constructor.
     *
     * @param queryId Query identifer.
     * @param actorRef      Target actor reference.
     * @param executionType Type of execution.
     * @param type          Type of results.
     */
    public StorageWorkflow(String queryId, Serializable actorRef, ExecutionType executionType,
            ResultType type) {
        super(queryId, actorRef, executionType, type);
    }

    public void setClusterName(ClusterName clusterName) {
        this.clusterName = clusterName;
    }

    public void setTableMetadata(TableMetadata tableMetadata) {
        this.tableMetadata = tableMetadata;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public void setRows(Collection<Row> rows) {
        this.rows = rows;
    }

    /**
     * Get the storage operation to be execution.
     * @return A {@link com.stratio.meta.communication.StorageOperation}.
     */
    public StorageOperation getStorageOperation(){
        StorageOperation result = null;
        if(ExecutionType.INSERT.equals(this.executionType)){
            result = new Insert(this.clusterName, this.tableMetadata, this.row);
        }else if(ExecutionType.INSERT_BATCH.equals(this.executionType)){
            result = new InsertBatch(this.clusterName, this.tableMetadata, this.rows);
        }
        return result;
    }
}
