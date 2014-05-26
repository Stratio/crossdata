/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.structures;

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
