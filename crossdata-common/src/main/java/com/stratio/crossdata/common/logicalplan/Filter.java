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

import java.util.Set;

import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.Relation;

/**
 * Filter the results retrieved through a Project operation.
 */
public class Filter extends TransformationStep implements ITerm {

    private static final long serialVersionUID = -1527718582002849918L;
    /**
     * Relationship.
     */
    private final Relation relation;

    /**
     * Create filter operation to be executed over a existing dataset.
     *
     * @param operations The operations to be executed.
     * @param relation  The relationship.
     */
    public Filter(Set<Operations> operations, Relation relation) {
        super(operations);
        this.relation = relation;
    }

    /**
     * Get the relationship.
     *
     * @return A {@link com.stratio.crossdata.common.statements.structures.Relation}
     */
    public Relation getRelation() {
        return relation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FILTER - ");
        sb.append(getOperations()).append(" - ").append(relation);
        return sb.toString();
    }
}
