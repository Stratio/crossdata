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

package com.stratio.crossdata.core.query;

import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.result.QueryStatus;

/**
 * Sql Planned Query with a previously validates query.
 */
public class SqlPlannedQuery extends SqlValidatedQuery implements IPlannedQuery {

    private final ExecutionWorkflow executionWorkflow;

    /**
     * Connector Class.
     *
     * @param sqlValidatedQuery the validated query
     * @param executionWorkflow     the execution workflow
     */
    public SqlPlannedQuery(SqlValidatedQuery sqlValidatedQuery, ExecutionWorkflow executionWorkflow) {
        super(sqlValidatedQuery);
        setQueryStatus(QueryStatus.PLANNED);
        this.executionWorkflow = executionWorkflow;
    }

    /**
     * Connector Class.
     *
     * @param sqlPlannedQuery The planned Query
     */
    public SqlPlannedQuery(SqlPlannedQuery sqlPlannedQuery) {
        this(sqlPlannedQuery, sqlPlannedQuery.getExecutionWorkflow());
    }

    @Override
    public ExecutionWorkflow getExecutionWorkflow() {
        return executionWorkflow;
    }

}
