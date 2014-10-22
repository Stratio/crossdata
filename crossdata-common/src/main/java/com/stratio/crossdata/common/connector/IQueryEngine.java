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

import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.result.QueryResult;

/**
 * Interface provided by a connector to access query related operations such as retrieving as set of
 * results.
 */
public interface IQueryEngine {

    /**
     * Execute a workflow to retrieve a subset of data.
     *
     * @param workflow The {@link com.stratio.crossdata.common.logicalplan.LogicalWorkflow} that
     *                 contains the {@link com.stratio.crossdata.common.logicalplan.LogicalStep} to be
     *                 executed.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     * @throws UnsupportedException If the required set of operations are not supported by the
     *                              connector.
     * @throws ExecutionException   If the execution of the required steps fails.
     */
    public QueryResult execute(LogicalWorkflow workflow)
            throws UnsupportedException,
            ExecutionException;

    /**
     * Execute a workflow asynchronously to retrieve a subset of data. Each time new data becomes available, the
     * result handler is informed to process the data. This method should return immediately after being called.
     *
     * @param queryId       Query identifier
     * @param workflow      The {@link com.stratio.crossdata.common.logicalplan.LogicalWorkflow} that
     *                      contains the {@link com.stratio.crossdata.common.logicalplan.LogicalStep} to be
     *                      executed.
     * @param resultHandler A result handler to receive incoming results.
     * @throws ExecutionException If the execution of the required steps fails.
     */
    public void asyncExecute(String queryId, LogicalWorkflow workflow, IResultHandler resultHandler)
            throws UnsupportedException,
            ExecutionException;

    /**
     * Stop an asynchronous query.
     *
     * @param queryId The query identifier.
     * @throws UnsupportedException If the required operation is not supported.
     * @throws ExecutionException   If the execution of the required steps fails.
     */
    public void stop(String queryId) throws UnsupportedException,
            ExecutionException;

}
