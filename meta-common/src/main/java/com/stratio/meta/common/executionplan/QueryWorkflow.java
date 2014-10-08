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

package com.stratio.meta.common.executionplan;

import java.io.Serializable;

import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.communication.StorageOperation;

/**
 * Execution step for query operations.
 */
public class QueryWorkflow extends ExecutionWorkflow {

    /**
     * Workflow to be executed.
     */
    private final LogicalWorkflow workflow;

    /**
     * Class constructor.
     *
     * @param queryId Query identifer.
     * @param actorRef      Target actor reference.
     * @param executionType Type of execution.
     * @param type          Type of results.
     * @param workflow      The logical workflow.
     */
    public QueryWorkflow(String queryId, Serializable actorRef, ExecutionType executionType,
            ResultType type, LogicalWorkflow workflow) {
        super(queryId, actorRef, executionType, type);
        this.workflow = workflow;
    }

    public LogicalWorkflow getWorkflow() {
        return workflow;
    }

}
