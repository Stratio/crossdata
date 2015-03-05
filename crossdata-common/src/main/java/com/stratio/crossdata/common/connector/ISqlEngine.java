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

package com.stratio.crossdata.common.connector;

import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.result.Result;

/**
 * Interface provided by a connector to access to SQL queries.
 */
public interface ISqlEngine {

    /**
     * Execute a SQL query to retrieve a subset of data.
     *
     * @param sqlQuery The SQL query to be executed.
     * @return A {@link com.stratio.crossdata.common.result.Result}.
     * @throws ConnectorException Use UnsupportedException if the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution of the required
     *                            query fails.
     */
    Result execute(String sqlQuery) throws ConnectorException;

    /**
     * Execute a SQL query asynchronously to retrieve a subset of data. Each time new data becomes available, the
     * result handler is informed to process the data. This method should return immediately after being called.
     *
     * @param queryId       Query identifier.
     * @param sqlQuery      The SQL query to be executed.
     * @param resultHandler A result handler to receive incoming results.
     * @throws ConnectorException Use UnsupportedException if the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution of the required
     *                            query fails.
     */
    void asyncExecute(String queryId, String sqlQuery, IResultHandler resultHandler) throws ConnectorException;

    /**
     * Execute a SQL query asynchronously and pagination to retrieve a subset of data. Each time new data becomes
     * available, the result handler is informed to process the data. This method should return immediately after
     * being called.
     *
     * @param queryId       Query identifier.
     * @param sqlQuery      The SQL query to be executed.
     * @param resultHandler A result handler to receive incoming results.
     * @param pageSize Size of the pagination.
     * @throws ConnectorException Use UnsupportedException if the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution of the required
     *                            query fails.
     */
    void pagedExecute(String queryId, String sqlQuery, IResultHandler resultHandler, int pageSize) throws ConnectorException;

}
