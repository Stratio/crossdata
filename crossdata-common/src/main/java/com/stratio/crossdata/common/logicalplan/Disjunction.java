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

public class Disjunction extends TransformationStep implements IOperand {

    private final List<IOperand> leftOperand = new ArrayList<>();

    private final List<IOperand> rightOperand = new ArrayList<>();

    /**
     * Class constructor.
     *
     * @param operations The operations to be applied.
     */
    public Disjunction(Set<Operations> operations, List<IOperand> leftOperand, List<IOperand> rightOperand) {
        super(operations);
        this.leftOperand.addAll(leftOperand);
        this.rightOperand.addAll(rightOperand);
    }

    public List<IOperand> getLeftOperand() {
        return leftOperand;
    }

    public List<IOperand> getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DISJUNCTION - ");
        sb.append(getOperations()).append(" - ");
        Iterator<IOperand> iter = leftOperand.iterator();
        while(iter.hasNext()){
            IOperand operand = iter.next();
            sb.append(operand);
            if(iter.hasNext()){
                sb.append(" AND ");
            }
        }
        sb.append(" OR ");
        iter = rightOperand.iterator();
        while(iter.hasNext()){
            IOperand operand = iter.next();
            sb.append(operand);
            if(iter.hasNext()){
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }
}
