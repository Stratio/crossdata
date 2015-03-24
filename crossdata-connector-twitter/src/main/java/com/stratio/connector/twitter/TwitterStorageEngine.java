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

package com.stratio.connector.twitter;

import java.util.Collection;

import com.stratio.crossdata.common.connector.IStorageEngine;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Relation;

public class TwitterStorageEngine implements IStorageEngine {

    private final TwitterConnector connector;

    public TwitterStorageEngine(TwitterConnector connector) {
        this.connector = connector;
    }

    /**
     * Insert a single row in a table.
     *
     * @param targetCluster Target cluster.
     * @param targetTable   Target table metadata including fully qualified including catalog.
     * @param row           The row to be inserted.
     * @param isNotExists   Insert only if primary key doesn't exist yet.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void insert(ClusterName targetCluster, TableMetadata targetTable, Row row, boolean isNotExists)
            throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Insert a collection of rows in a table.
     *
     * @param targetCluster Target cluster.
     * @param targetTable   Target table metadata including fully qualified including catalog.
     * @param rows          Collection of rows to be inserted.
     * @param isNotExists   Insert only if primary key doesn't exist yet.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void insert(ClusterName targetCluster, TableMetadata targetTable, Collection<Row> rows,
            boolean isNotExists) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Delete rows, on the indicated cluster, that meet the conditions of the where clauses.
     *
     * @param targetCluster Target cluster.
     * @param tableName     Target table name including fully qualified including catalog.
     * @param whereClauses  Where clauses.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException
     */
    @Override
    public void delete(ClusterName targetCluster, TableName tableName, Collection<Filter> whereClauses)
            throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Update data of a table according to some conditions.
     *
     * @param targetCluster Target cluster.
     * @param tableName     Target table name including fully qualified including catalog.
     * @param assignments   Operations to be executed for every row.
     * @param whereClauses  Where clauses.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException
     */
    @Override
    public void update(ClusterName targetCluster, TableName tableName, Collection<Relation> assignments,
            Collection<Filter> whereClauses) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Delete all the rows of a table.
     *
     * @param targetCluster Target cluster.
     * @param tableName     Target table name including fully qualified including catalog.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException
     */
    @Override
    public void truncate(ClusterName targetCluster, TableName tableName) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }
}
