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

package com.stratio.crossdata.common.statements.structures.relationships;

import java.io.Serializable;
import java.util.Set;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.selectors.Selector;

/**
 * Class that models the different types of relationships that can be found on a WHERE clause.
 */
public class Relation implements Serializable{

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

    public Selector getLeftTerm() {
        return leftTerm;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Selector getRightTerm() {
        return rightTerm;
    }

    /**
     * Get the tables queried on the selector.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.TableName}.
     */
    public Set<TableName> getSelectorTables() {
        return leftTerm.getSelectorTables();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(leftTerm.toString());
        sb.append(" ").append(operator).append(" ");
        sb.append(rightTerm);
        return sb.toString();
    }

}
