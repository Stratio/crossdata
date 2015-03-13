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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationConjunction extends AbstractRelation {

    private static final long serialVersionUID = 2085700590246602145L;

    private final List<AbstractRelation> leftRelations = new ArrayList<>();

    private final List<AbstractRelation> rightRelations = new ArrayList<>();

    public RelationConjunction(List<AbstractRelation> leftRelations, List<AbstractRelation> rightRelations) {
        this.leftRelations.addAll(leftRelations);
        this.rightRelations.addAll(rightRelations);
    }

    public RelationConjunction(AbstractRelation leftRelation, AbstractRelation rightRelation) {
        this.leftRelations.add(leftRelation);
        this.rightRelations.add(rightRelation);
    }

    public List<AbstractRelation> getLeftRelations() {
        return leftRelations;
    }

    public List<AbstractRelation> getRightRelations() {
        return rightRelations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<AbstractRelation> leftIter = leftRelations.iterator();
        while(leftIter.hasNext()){
            AbstractRelation leftRelation = leftIter.next();
            sb.append(leftRelation);
            if(leftIter.hasNext()){
                sb.append(" AND ");
            }
        }
        sb.append(" OR ");
        Iterator<AbstractRelation> rightIter = rightRelations.iterator();
        while(rightIter.hasNext()){
            AbstractRelation rightRelation = rightIter.next();
            sb.append(rightRelation);
            if(rightIter.hasNext()){
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }
}
