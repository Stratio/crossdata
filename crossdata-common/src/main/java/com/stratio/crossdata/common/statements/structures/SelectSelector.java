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

package com.stratio.crossdata.common.statements.structures;

import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;

public class SelectSelector extends Selector {

    private String selectQuery;

    private LogicalWorkflow logicalWorkflow;

    public SelectSelector(String selectQuery) {
        this(null, selectQuery);
    }

    public SelectSelector(TableName tableName, String selectQuery) {
        super(tableName);
        this.selectQuery = selectQuery.replaceAll(Name.UNKNOWN_NAME+".", "");
    }

    public String getSelectQuery() {
        return selectQuery;
    }

    public void setSelectQuery(String selectQuery) {
        this.selectQuery = selectQuery;
    }

    public LogicalWorkflow getQueryWorkflow() {
        return logicalWorkflow;
    }

    public void setQueryWorkflow(LogicalWorkflow queryWorkflow) {
        this.logicalWorkflow = queryWorkflow;
    }

    /**
     * Get the selector type.
     *
     * @return A {@link com.stratio.crossdata.common.statements.structures.SelectorType}.
     */
    @Override
    public SelectorType getType() {
        return SelectorType.SELECT;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder(selectQuery);
        if(logicalWorkflow != null){
            sb.append(System.lineSeparator()).append(logicalWorkflow.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
