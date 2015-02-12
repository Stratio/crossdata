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

import java.util.Collection;
import java.util.List;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.validation.CoordinationException;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.communication.DeleteRows;
import com.stratio.crossdata.communication.Insert;
import com.stratio.crossdata.communication.InsertBatch;
import com.stratio.crossdata.communication.StorageOperation;
import com.stratio.crossdata.communication.Truncate;
import com.stratio.crossdata.communication.Update;

/**
 * Storage related operations.
 */
public class StorageWorkflow extends ExecutionWorkflow {

    private static final long serialVersionUID = 3929350966531076463L;
    private ClusterName clusterName = null;

    private TableMetadata tableMetadata = null;

    private TableName tableName = null;

    private Row row = null;

    private Collection<Row> rows = null;

    private Collection<Filter> whereClauses = null;

    private Collection<Relation> assignments = null;

    private boolean ifNotExists;

    /**
     * Class constructor.
     *
     * @param queryId Query identifer.
     * @param actorRef      Target actor reference.
     * @param executionType Type of execution.
     * @param type          Type of results.
     */
    public StorageWorkflow(String queryId, String actorRef, ExecutionType executionType,
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
     * @return A {@link com.stratio.crossdata.communication.StorageOperation}.
     */
    public StorageOperation getStorageOperation() throws CoordinationException {
        StorageOperation result;
        if(ExecutionType.INSERT.equals(this.executionType)){
            result = new Insert(queryId, this.clusterName, this.tableMetadata, this.row, this.ifNotExists);
        } else if(ExecutionType.INSERT_BATCH.equals(this.executionType)){
            result = new InsertBatch(queryId, this.clusterName, this.tableMetadata, this.rows, this.ifNotExists);
        } else if(ExecutionType.DELETE_ROWS.equals(this.executionType)){
            result = new DeleteRows(queryId, this.clusterName, tableName, this.whereClauses);
        } else if(ExecutionType.UPDATE_TABLE.equals(this.executionType)){
            result = new Update(queryId, this.clusterName, tableName, this.assignments, this.whereClauses);
        } else if(ExecutionType.TRUNCATE_TABLE.equals(this.executionType)){
            result = new Truncate(queryId, this.clusterName, tableName);
        } else {
            throw new CoordinationException("Operation " + this.executionType + " not supported yet.");
        }
        return result;
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public TableMetadata getTableMetadata() {
        return tableMetadata;
    }

    public Row getRow() {
        return row;
    }

    public Collection<Row> getRows() {
        return rows;
    }

    public void setWhereClauses(List<Filter> whereClauses) {
        this.whereClauses = whereClauses;
    }

    public void setAssignments(List<Relation> assignments) {
        this.assignments = assignments;
    }

    public Collection<Filter> getWhereClauses() {
        return whereClauses;
    }

    public Collection<Relation> getAssignments() {
        return assignments;
    }

    public TableName getTableName() {
        return tableName;
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
}
