/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.common.statements.structures.relationships;

import java.util.LinkedList;
import java.util.List;

import com.stratio.meta2.common.statements.structures.selectors.Selector;

public class ComplexRelation extends Relation {

    private List<Operator> moreOperators;

    private List<Selector> moreTerms;

    /**
     * Class constructor.
     *
     * @param selector  The Selector found in the left-part of the relationship.
     * @param operator  The operator to be applied.
     * @param rightTerm The Selector found in the right-part of the relationship.
     */
    public ComplexRelation(Selector selector,
            Operator operator,
            Selector rightTerm) {
        super(selector, operator, rightTerm);
        moreOperators = new LinkedList<>();
        moreTerms = new LinkedList<>();
    }

    public ComplexRelation(Relation relation) {
        super(relation.getLeftTerm(), relation.getOperator(), relation.rightTerm);
        moreOperators = new LinkedList<>();
        moreTerms = new LinkedList<>();
    }

    public List<Operator> getMoreOperators() {
        return moreOperators;
    }

    public List<Selector> getMoreTerms() {
        return moreTerms;
    }

    public void addRelation(Operator op, Selector term) {
        moreOperators.add(op);
        moreTerms.add(term);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        for (int i = 0; i < moreOperators.size(); i++) {
            sb.append(" ").append(moreOperators.get(i)).append(" ");
            sb.append(moreTerms.get(i));
        }
        return sb.toString();
    }
}
