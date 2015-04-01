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

import com.stratio.crossdata.common.data.TableName;

/**
 * Relation selector.
 */
public class RelationSelector extends Selector {

    private static final long serialVersionUID = 5420806618352269424L;

    /**
     * The relation associated with the selector.
     */
    private final Relation relation;

    /**
     * Class constructor.
     *
     * @param relation A {@link com.stratio.crossdata.common.statements.structures.Relation}.
     */
    public RelationSelector(Relation relation) {
        this(null, relation);
    }

    /**
     * Class constructor.
     *
     * @param tableName  The table name.
     * @param relation A {@link com.stratio.crossdata.common.statements.structures.Relation}.
     */
    public RelationSelector(TableName tableName, Relation relation) {
        super(tableName);
        this.relation = relation;
    }

    /**
     * Get the relation associated with the selector.
     *
     * @return A {@link com.stratio.crossdata.common.statements.structures.Relation}.
     */
    public Relation getRelation() {
        return relation;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.RELATION;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(parenthesis){
            sb.append("(");
        }
        sb.append(relation);
        if(parenthesis){
            sb.append(")");
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

        RelationSelector that = (RelationSelector) o;

        if (!relation.equals(that.relation)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (alias != null) {
            result = alias.hashCode();
        }
        result = 31 * result + relation.hashCode();
        return result;
    }
}
