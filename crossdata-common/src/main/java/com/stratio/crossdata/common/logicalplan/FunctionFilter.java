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

import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.FunctionRelation;
import com.stratio.crossdata.common.statements.structures.Relation;

import java.util.Set;

/**
 * Filter the results retrieved through a Project operation using a Function.
 */
public class FunctionFilter extends TransformationStep implements ITerm {

    /**
     * Relationship.
     */
    private final FunctionRelation relation;

    /**
     * Create filter operation to be executed over a existing dataset.
     *
     * @param operations The operations to be executed.
     * @param relation  The relationship.
     */
    public FunctionFilter(Set<Operations> operations, FunctionRelation relation) {
        super(operations);
        this.relation = relation;
    }

    /**
     * Get the relationship.
     *
     * @return A {@link com.stratio.crossdata.common.statements.structures.Relation}
     */
    public FunctionRelation getRelation() {
        return relation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FUNCTION-FILTER - ");
        sb.append(getOperations()).append(" - ").append(relation);
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionFilter that = (FunctionFilter) o;

        if (relation != null ? !relation.equals(that.relation) : that.relation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return relation != null ? relation.hashCode() : 0;
    }
}
