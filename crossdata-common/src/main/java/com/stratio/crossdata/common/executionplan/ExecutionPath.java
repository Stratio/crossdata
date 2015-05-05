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

import java.io.Serializable;
import java.util.List;

import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;

/**
 * Execution path of a logical workflow.
 */
public class ExecutionPath implements Serializable {

    private static final long serialVersionUID = -2712382478581678360L;
    private final LogicalStep initial;

    private LogicalStep last;

    private final List<ConnectorMetadata> availableConnectors;

    /**
     * Main constructor to execution path.
     * @param initial First logical step for the workflow.
     * @param last Last logical step for the workflow.
     * @param availableConnectors List of the connectors.
     */
    public ExecutionPath(LogicalStep initial, LogicalStep last, List<ConnectorMetadata> availableConnectors){
        this.initial = initial;
        this.last = last;
        this.availableConnectors = availableConnectors;
    }

    public LogicalStep getInitial(){
        return initial;
    }

    public LogicalStep getLast(){
        return last;
    }

    public List<ConnectorMetadata> getAvailableConnectors() {
        return availableConnectors;
    }

    public void setLast(LogicalStep last) {
        this.last = last;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Execution path with connectors [");
        for(ConnectorMetadata m : availableConnectors){
            sb.append(m.getName()).append(", ");
        }
        sb.append("]");
        sb.append(initial).append(System.lineSeparator());
        LogicalStep pointer = initial;
        while(pointer != null && pointer.equals(last)){
            sb.append(pointer).append(System.lineSeparator());
            pointer = pointer.getNextStep();
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExecutionPath that = (ExecutionPath) o;

        if (availableConnectors != null ?
                !availableConnectors.equals(that.availableConnectors) :
                that.availableConnectors != null) {
            return false;
        }
        if (initial != null ? !initial.equals(that.initial) : that.initial != null) {
            return false;
        }
        if (last != null ? !last.equals(that.last) : that.last != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = initial != null ? initial.hashCode() : 0;
        result = 31 * result + (last != null ? last.hashCode() : 0);
        result = 31 * result + (availableConnectors != null ? availableConnectors.hashCode() : 0);
        return result;
    }
}
