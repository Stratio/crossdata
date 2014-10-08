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

package com.stratio.meta2.common.statements.structures.selectors;

import com.stratio.meta.common.statements.structures.relationships.Relation;

public class RelationSelector extends Selector {

    /**
     *
     */
    private final Relation relation;

    public RelationSelector(Relation relation) {
        this.relation = relation;
    }

    public Relation getRelation() {
        return relation;
    }

    @Override public SelectorType getType() {
        return SelectorType.RELATION;
    }

    @Override
    public String toString() {
        return relation.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelationSelector that = (RelationSelector) o;

        if (!alias.equals(that.alias)) {
            return false;
        }
        if (!relation.equals(that.relation)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (alias != null){
            result = alias.hashCode();
        }
        result = 31 * result + relation.hashCode();
        return result;
    }
}
