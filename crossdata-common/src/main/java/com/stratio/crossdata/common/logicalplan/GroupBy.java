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
package com.stratio.crossdata.common.logicalplan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * GroupBy class implements the step of a group by in a workflow.
 */
public class GroupBy extends TransformationStep {

    /**
     * Identifiers.
     */
    private List<Selector> ids = new ArrayList<>();

    /**
     * Class constructor.
     *
     * @param operation The operation to be applied.
     * @param ids Identifiers.
     */
    public GroupBy(Operations operation, List<Selector> ids) {
        super(operation);
        this.ids = ids;
    }

    /**
     * Get Identifiers.
     * @return Identifiers.
     */
    public List<Selector> getIds() {
        return ids;
    }

    /**
     * Set identifiers.
     * @param ids Identifiers to be assigned.
     */
    public void setIds(List<Selector> ids) {
        this.ids = ids;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GROUP BY ");
        Iterator<Selector> iter = ids.iterator();
        while(iter.hasNext()){
            Selector selector = iter.next();
            sb.append(selector);
            if(iter.hasNext()){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
