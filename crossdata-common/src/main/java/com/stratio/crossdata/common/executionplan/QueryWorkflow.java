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
     * Class constructor.
     *
     * @param queryId Query identifier.
     * @param actorRef      Target actor reference.
     * @param executionType Type of execution.
     * @param type          Type of results.
     * @param workflow      The logical workflow.
     */
    public QueryWorkflow(String queryId, String actorRef, ExecutionType executionType,
            ResultType type, LogicalWorkflow workflow) {
        super(queryId, actorRef, executionType, type);
        this.workflow = workflow;
    }

    public LogicalWorkflow getWorkflow() {
        return workflow;
    }

    public static boolean checkStreaming(LogicalStep lastStep){
        boolean result = false;
        if(lastStep != null){
            if(Window.class.isInstance(lastStep)){
                return true;
            }else{
                for(LogicalStep p : lastStep.getPreviousSteps()){
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
        //Look for window operators.
        if(checkStreaming(workflow.getLastStep())) {
            return new AsyncExecute(queryId, workflow);
        }
        if(workflow.getPagination() > 0){
            return new PagedExecute(queryId, workflow, workflow.getPagination());
        }
        //TODO: Either sever configuration should include an option or client
        return new Execute(queryId, workflow);
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
