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

package com.stratio.connector.inmemory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.stratio.connector.inmemory.datastore.InMemoryDatastore;
import com.stratio.crossdata.common.connector.IStorageEngine;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Relation;

/**
 * Class that implements {@link com.stratio.crossdata.common.connector.IStorageEngine}.
 */
public class InMemoryStorageEngine implements IStorageEngine{

    /**
     * Link to the in memory connector.
     */
    private final InMemoryConnector connector;

    /**
     * Class constructor.
     * @param connector The linked {@link com.stratio.connector.inmemory.InMemoryConnector}.
     */
    public InMemoryStorageEngine(InMemoryConnector connector){
        this.connector = connector;
    }

    @Override
    public void insert(ClusterName targetCluster, TableMetadata targetTable, Row row, boolean ifNotExists)
            throws ConnectorException {
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            String catalogName = targetTable.getName().getCatalogName().getQualifiedName();
            String tableName = targetTable.getName().getName();
            Map<String, Object> toAdd = new HashMap<>();
            for(Map.Entry<String, Cell> col : row.getCells().entrySet()){
                toAdd.put(col.getKey(), col.getValue().getValue());
            }

            try {
                datastore.insert(catalogName, tableName, toAdd);
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }else{
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }

    @Override
    public void insert(ClusterName targetCluster, TableMetadata targetTable, Collection<Row> rows, boolean ifNotExists)
            throws ConnectorException {
        for(Row r : rows){
            insert(targetCluster, targetTable, r, ifNotExists);
        }
    }

    @Override
    public void delete(ClusterName targetCluster, TableName tableName, Collection<Filter> whereClauses)
            throws ConnectorException {
        throw new UnsupportedException("Deleting table rows is not supported.");
    }

    @Override
    public void update(ClusterName targetCluster, TableName tableName, Collection<Relation> assignments,
            Collection<Filter> whereClauses) throws ConnectorException {
        throw new UnsupportedException("Updating table rows is not supported.");
    }

    @Override
    public void truncate(ClusterName targetCluster, TableName tableName) throws ConnectorException {
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            try {
                String catalogName = tableName.getCatalogName().getQualifiedName();
                datastore.truncateTable(catalogName, tableName.getName());
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }else{
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }
}
