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

import com.stratio.meta.core.utils.ParserUtils;

import java.util.ArrayList;

public class RelationCompare extends Relation {

    public RelationCompare(String identifier) {
        this.terms = new ArrayList<>();
        this.type = TYPE_COMPARE;
        this.identifiers = new ArrayList<>();
        this.identifiers.add(identifier);
    }   
    
    public RelationCompare(String identifier, String operator) {
        this(identifier);
        this.operator = operator;
    }
    
    public RelationCompare(String identifier, String operator, Term term) {
        this(identifier, operator);
        // TODO: Fix problem with text fields for Lucene indexes (demo.users.phrase)
        // If we are dealing with a Lucene indexed column and the column type is text, then
        // we need to lower-case the input text to comply with the Lucene semantics.
        this.terms.add(term);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifiers.get(0));
        sb.append(" ").append(operator).append(" ").append(ParserUtils.stringList(terms, ", "));
        return sb.toString();
    }
    
}
