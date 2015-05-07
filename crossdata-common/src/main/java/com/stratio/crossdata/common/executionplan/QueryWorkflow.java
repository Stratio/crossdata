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

import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.Window;
import com.stratio.crossdata.communication.AsyncExecute;
import com.stratio.crossdata.communication.Execute;
import com.stratio.crossdata.communication.ExecuteOperation;
import com.stratio.crossdata.communication.PagedExecute;

/**
 * Execution step for query operations.
 */
public class QueryWorkflow extends ExecutionWorkflow {

    private static final long serialVersionUID = -600963332114323650L;
    /**
     * Workflow to be executed.
     */
    private final LogicalWorkflow workflow;

    /**
     * Whether the target supports asynchronous queries.
     */
    private final boolean isAsync;

    /**
     * Class constructor.
     *
     * @param queryId Query identifier.
     * @param actorRef      Target actor reference.
     * @param executionType Type of execution.
     * @param type          Type of results.
     * @param workflow      The logical workflow.
     * @param isAsync       Whether the target supports asynchronous queries.
     */
    public QueryWorkflow(String queryId, String actorRef, ExecutionType executionType,
            ResultType type, LogicalWorkflow workflow, boolean isAsync) {
        super(queryId, actorRef, executionType, type);
        this.workflow = workflow;
        this.isAsync = isAsync;
    }

    public LogicalWorkflow getWorkflow() {
        return workflow;
    }

    /**
     * Check whether the last step is a streaming new query.
     * @param lastStep The last step
     * @return Whether is expected a streaming query.
     */
    public static boolean checkStreaming(LogicalStep lastStep){
        boolean result = false;
        if(lastStep != null){
            if(Window.class.isInstance(lastStep)){
                return true;
            }else{
                for(LogicalStep p: lastStep.getPreviousSteps()){
                    result |= checkStreaming(p);
                }
            }
        }
        return result;
    }

    /**
     * Return the operation.
     * @param queryId The query id.
     * @return A {@link com.stratio.crossdata.communication.ExecuteOperation}
     */
    public ExecuteOperation getExecuteOperation(String queryId){

        ExecuteOperation executeOperation;
        //Look for window operators.
        if(workflow.getPagination() > 0){
            executeOperation = new PagedExecute(queryId, workflow, workflow.getPagination());
        }else if(isAsync || checkStreaming(workflow.getLastStep()) ) {
            executeOperation =  new AsyncExecute(queryId, workflow);
        }else {
            executeOperation = new Execute(queryId, workflow);
        }
        return executeOperation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(System.lineSeparator()).append(workflow);
        if(getNextExecutionWorkflow() != null){
            sb.append(System.lineSeparator()).append("TRIGGERS ").append(getNextExecutionWorkflow().toString());
        }
        return sb.toString();
    }
}
