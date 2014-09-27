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

import com.stratio.meta.common.logicalplan.LogicalStep;

/**
 * Execution step abstraction. This class contains all the information
 * required in order to execute a LogicalWorkflow in a specific connector.
 * Notice that a ExecutionStep may trigger further workflow execution.
 */
public class ExecutionWorkflow {

    /**
     * Query identifier.
     */
    private final String queryId;

    /**
     * The target actor reference associated with the connector.
     */
    private final Serializable actorRef;

    /**
     * The type of operation to be executed.
     */
    protected final ExecutionType executionType;

    /**
     * Type of execution.
     */
    private final ResultType resultType;

    /**
     * If the previous execution step triggers another execution step, this variable contains
     * the logical step where previous results should be stored.
     */
    private LogicalStep triggerStep;

    /**
     * Variable that defines the next execution step to be launched if the execution type
     * is {@code TRIGGER_EXECUTION}.
     */
    private ExecutionWorkflow nextExecutionWorkflow;

    /**
     * Class constructor.
     * @param queryId Query identifer.
     * @param actorRef Target actor reference.
     * @param executionType Type of execution.
     * @param type     Type of results.
     */
    public ExecutionWorkflow(String queryId, Serializable actorRef, ExecutionType executionType, ResultType type) {
        this.queryId = queryId;
        this.actorRef = actorRef;
        this.executionType = executionType;
        this.resultType = type;
    }

    public Serializable getActorRef() {
        return actorRef;
    }

    public String getQueryId() {
        return queryId;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public LogicalStep getTriggerStep() {
        return triggerStep;
    }

    public void setTriggerStep(LogicalStep triggerStep) {
        this.triggerStep = triggerStep;
    }

    public ExecutionWorkflow getNextExecutionWorkflow() {
        return nextExecutionWorkflow;
    }

    public void setNextExecutionWorkflow(ExecutionWorkflow nextExecutionWorkflow) {
        this.nextExecutionWorkflow = nextExecutionWorkflow;
    }
}
