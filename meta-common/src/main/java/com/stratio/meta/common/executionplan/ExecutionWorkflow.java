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
     * The target actor reference associated with the connector.
     */
    private final Serializable actorRef;

    /**
     * Type of execution.
     */
    private final ResultType type;

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
     *
     * @param actorRef Target actor reference.
     * @param type     Type of execution.
     */
    public ExecutionWorkflow(Serializable actorRef, ResultType type) {
        this.actorRef = actorRef;
        this.type = type;
    }

    public Serializable getActorRef() {
        return actorRef;
    }

    public ResultType getType() {
        return type;
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
