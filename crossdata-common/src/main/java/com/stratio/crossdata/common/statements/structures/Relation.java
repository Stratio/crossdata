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

import java.util.HashSet;
import java.util.Set;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;

/**
 * Class that models the different types of relationships that can be found on a WHERE clause.
 */
public class Relation extends AbstractRelation {

    private static final long serialVersionUID = -3836506017515944374L;

    /**
     * Identifier in the left part of the relationship.
     */
    protected Selector leftTerm;

    /**
     * Operator to be applied to solve the relationship.
     */
    protected Operator operator;

    /**
     * Right part of the relationship.
     */
    protected Selector rightTerm;

    /**
     * Class constructor.
     *
     * @param selector  The Selector found in the left-part of the relationship.
     * @param operator  The operator to be applied.
     * @param rightTerm The Selector found in the right-part of the relationship.
     */
    public Relation(Selector selector, Operator operator, Selector rightTerm) {
        this.leftTerm = selector;
        this.operator = operator;
        this.rightTerm = rightTerm;
    }

    /**
     * Get the left term of the relation.
     * @return {@link com.stratio.crossdata.common.statements.structures.Relation}.
     */
    public Selector getLeftTerm() {
        return leftTerm;
    }

    /**
     * Get the operator of the relation.
     * @return {@link com.stratio.crossdata.common.statements.structures.Operator}.
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Set a new operator for the relation.
     * @param operator {@link com.stratio.crossdata.common.statements.structures.Operator}.
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Get the right term of the relation.
     * @return {@link com.stratio.crossdata.common.statements.structures.Relation}.
     */
    public Selector getRightTerm() {
        return rightTerm;
    }

    public void setLeftTerm(Selector leftTerm) {
        this.leftTerm = leftTerm;
    }

    public void setRightTerm(Selector rightTerm) {
        this.rightTerm = rightTerm;
    }

    /**
     * Get the tables queried on the selector.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.TableName}.
     */
    public Set<TableName> getSelectorTables() {
        return leftTerm.getSelectorTables();
    }

    /**
     * Get the columns involved in the relations.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.ColumnName}.
     */
    public Set<ColumnName> getSelectorColumns() {
        Set<ColumnName> columns = new HashSet<>();

        if(leftTerm instanceof ColumnSelector){
            columns.add(leftTerm.getColumnName());
        }else if(leftTerm instanceof RelationSelector){
            columns.addAll(((RelationSelector)leftTerm).getRelation().getSelectorColumns());
        }

        if(rightTerm instanceof ColumnSelector){
            columns.add(rightTerm.getColumnName());
        }else if(rightTerm instanceof RelationSelector){
            columns.addAll(((RelationSelector) rightTerm).getRelation().getSelectorColumns());
        }

        return columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Relation relation = (Relation) o;

        if (leftTerm != null ? !leftTerm.equals(relation.leftTerm) : relation.leftTerm != null) {
            return false;
        }
        if (operator != relation.operator) {
            return false;
        }
        if (rightTerm != null ? !rightTerm.equals(relation.rightTerm) : relation.rightTerm != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = leftTerm != null ? leftTerm.hashCode() : 0;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (rightTerm != null ? rightTerm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isParenthesis()){
            sb.append("(");
        }
        sb.append(leftTerm.toString());
        sb.append(" ").append(operator).append(" ");
        sb.append(rightTerm.toString());
        if(isParenthesis()){
            sb.append(")");
        }
        return sb.toString();
    }

}
