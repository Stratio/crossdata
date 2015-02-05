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

package com.stratio.crossdata.core.execution;

import java.io.Serializable;

import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.result.QueryStatus;

/**
 * Execution info class with the workflow information.
 */
public class ExecutionInfo implements Serializable {

    private String sender = null;

    private ExecutionWorkflow workflow = null;

    private QueryStatus queryStatus = QueryStatus.NONE;

    /**
     * Persist in MetadataManager.
     */
    private boolean persistOnSuccess = false;

    /**
     * Remove from ExecutionManager.
     */
    private boolean removeOnSuccess = true;

    public ExecutionInfo() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ExecutionWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(ExecutionWorkflow workflow) {
        this.workflow = workflow;
    }

    public QueryStatus getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(QueryStatus queryStatus) {
        this.queryStatus = queryStatus;
    }

    public boolean isPersistOnSuccess() {
        return persistOnSuccess;
    }

    public void setPersistOnSuccess(boolean persistOnSuccess) {
        this.persistOnSuccess = persistOnSuccess;
    }

    public boolean isRemoveOnSuccess() {
        return removeOnSuccess;
    }

    public void setRemoveOnSuccess(boolean removeOnSuccess) {
        this.removeOnSuccess = removeOnSuccess;
    }
}
