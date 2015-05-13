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

/**
 * Used to combine two conditions with the OR operator
 */
public class Disjunction extends TransformationStep implements ITerm {

    private final List<List<ITerm>> terms = new ArrayList<>();

    /**
     * Build a Disjunction using parametters.
     * @param operations a java.util.Set of Operations
     * @param terms a java.util.List<java.util.List<ITerm>>
     */
    public Disjunction(Set<Operations> operations, List<List<ITerm>> terms) {
        super(operations);
        this.terms.addAll(terms);
    }

    public List<List<ITerm>> getTerms() {
        return terms;
    }

    /**
     *
     * @return The string representations os the Disjunction
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DISJUNCTION - ");
        sb.append(getOperations()).append(" - ");
        Iterator<List<ITerm>> iter = terms.iterator();
        while(iter.hasNext()){
            List<ITerm> term = iter.next();
            Iterator<ITerm> innerIter = term.iterator();
            while(innerIter.hasNext()){
                ITerm innerTerm = innerIter.next();
                sb.append(innerTerm);
                if(innerIter.hasNext()){
                    sb.append(" AND ");
                }
            }
            if(iter.hasNext()){
                sb.append(" OR ");
            }
        }
        return sb.toString();
    }
}
