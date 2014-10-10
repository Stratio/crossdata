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

import java.util.Arrays;
import java.util.List;

import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta2.common.metadata.ConnectorMetadata;

/**
 * Execution path of a logical workflow.
 */
public class ExecutionPath {

    private final LogicalStep initial;

    private LogicalStep last;

    private final List<ConnectorMetadata> availableConnectors;

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
        LogicalStep pointer = initial;
        do{
            sb.append(last).append(System.lineSeparator());
            pointer = pointer.getNextStep();
        }while(pointer != last);

        return sb.toString();
    }
}
