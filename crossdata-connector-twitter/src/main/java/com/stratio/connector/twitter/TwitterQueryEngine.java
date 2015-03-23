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

import com.stratio.connector.twitter.listener.TweetsListener;
import com.stratio.crossdata.common.connector.IQueryEngine;
import com.stratio.crossdata.common.connector.IResultHandler;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.logicalplan.Window;
import com.stratio.crossdata.common.result.QueryResult;

import twitter4j.TwitterStream;

public class TwitterQueryEngine implements IQueryEngine {

    private final TwitterConnector connector;

    public TwitterQueryEngine(TwitterConnector connector) {
        this.connector = connector;
    }

    /**
     * Execute a workflow to retrieve a subset of data.
     *
     * @param workflow The {@link com.stratio.crossdata.common.logicalplan.LogicalWorkflow} that
     *                 contains the {@link com.stratio.crossdata.common.logicalplan.LogicalStep} to be
     *                 executed.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution of the required
     *                                                                    steps fails.
     */
    @Override
    public QueryResult execute(LogicalWorkflow workflow) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Execute a workflow asynchronously to retrieve a subset of data. Each time new data becomes available, the
     * result handler is informed to process the data. This method should return immediately after being called.
     *
     * @param queryId       Query identifier
     * @param workflow      The {@link com.stratio.crossdata.common.logicalplan.LogicalWorkflow} that
     *                      contains the {@link com.stratio.crossdata.common.logicalplan.LogicalStep} to be
     *                      executed.
     * @param resultHandler A result handler to receive incoming results.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution of the required
     *                                                                    steps fails.
     */
    @Override
    public void asyncExecute(String queryId, LogicalWorkflow workflow, IResultHandler resultHandler)
            throws ConnectorException {
        Project project = (Project) workflow.getInitialSteps().get(0);
        Window window = (Window) project.getNextStep();
        Select select = (Select) window.getNextStep();
        TwitterStream session = connector.getSession(project.getClusterName());
        TweetsListener listener = new TweetsListener(
                queryId,
                resultHandler,
                select.getOutputSelectorOrder(),
                window.getDurationInMilliseconds());
        session.addListener(listener);
        session.sample();
        /*
        FilterQuery fq = new FilterQuery();
        String keywords[] = {"sport", "politics", "health"};
        fq.track(keywords);
        session.filter(fq);
         */
    }

    /**
     * Execute query that returns partial results according to a page size.
     *
     * @param queryId       Query identifier
     * @param workflow      The {@link com.stratio.crossdata.common.logicalplan.LogicalWorkflow} that
     *                      contains the {@link com.stratio.crossdata.common.logicalplan.LogicalStep} to be
     *                      executed.
     * @param resultHandler A result handler to receive incoming results.
     * @param pageSize      Size of the pagination.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException
     */
    @Override
    public void pagedExecute(String queryId, LogicalWorkflow workflow, IResultHandler resultHandler,
            int pageSize) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Stop an asynchronous query.
     *
     * @param queryId The query identifier.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void stop(String queryId) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }
}
