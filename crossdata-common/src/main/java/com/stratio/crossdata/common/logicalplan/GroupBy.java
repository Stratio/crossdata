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
import java.util.Set;

import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.AbstractRelation;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * GroupBy class implements the step of a group by in a workflow.
 */
public class GroupBy extends TransformationStep {

    private static final long serialVersionUID = 7228005355186801643L;
    /**
     * Identifiers.
     */
    private List<Selector> ids = new ArrayList<>();

    /**
     * Having Identifiers.
     */
    private List<AbstractRelation> havingIds = new ArrayList<>();

    /**
     * Whether a having clause has been specified.
     */
    private boolean havingInc=false;

    /**
     * Class constructor.
     *
     * @param operations The operations to be applied.
     * @param ids Identifiers.
     */
    public GroupBy(Set<Operations> operations, List<Selector> ids) {
        super(operations);
        this.ids = ids;
    }

    /**
     * Class constructor.
     *
     * @param operations The operations to be applied.
     * @param ids Identifiers.
     */
    public GroupBy(Set<Operations> operations, List<Selector> ids, List<AbstractRelation> havingIds) {
        super(operations);
        this.ids = ids;
        this.havingIds=havingIds;
        this.havingInc=true;
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

    /**
     * Get Having Identifiers.
     * @return Identifiers.
     */
    public List<AbstractRelation> getHavingIds() {
        return havingIds;
    }

    /**
     * Set identifiers.
     * @param havingIds Identifiers to be assigned.
     */
    public void setHavingIds(List<AbstractRelation> havingIds) {
        this.havingIds = havingIds;
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

        if (havingInc){
            sb.append(" HAVING ");
            boolean first=true;
            for (AbstractRelation relation:havingIds){
                if (!first){
                    sb.append(" AND ");
                }
                first=false;
                sb.append(relation.toString());

            }
        }
        return sb.toString();
    }
}
