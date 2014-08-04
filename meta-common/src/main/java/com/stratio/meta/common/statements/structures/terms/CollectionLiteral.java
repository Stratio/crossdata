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

package com.stratio.meta.common.statements.structures.terms;

import java.util.ArrayList;
import java.util.List;

public class CollectionLiteral<T extends Comparable<T>> extends ValueCell<T> {

    private List<Term<T>> literals;

    public CollectionLiteral() {
        literals = new ArrayList<>();
        this.type = TYPE_COLLECTION_LITERAL;
    }

    public CollectionLiteral(List<Term<T>> literals) {
        this();
        this.literals = literals;
    }

    public List<Term<T>> getLiterals() {
        return literals;
    }

    public void setLiterals(List<Term<T>> literals) {
        this.literals = literals;
    }

    public void addLiteral(Term<T> term) {
        literals.add(term);
    }

    public Term<T> getLiteral(int index) {
        return literals.get(index);
    }

    public void deleteLiteral(Term<T> term) {
        literals.remove(term);
    }

    @Override
    public String getStringValue() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Term<T> term : literals) {
            sb.append(term.toString()).append(", ");
        }
        return sb.substring(0, sb.length() - 2) + "}";
    }

}
