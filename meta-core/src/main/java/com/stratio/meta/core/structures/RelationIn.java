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
import java.util.List;

public class RelationIn extends MetaRelation {    

    public RelationIn(String identifier) {
        this.terms = new ArrayList<>();
        this.type = TYPE_IN;
        this.operator = "IN";
        this.identifiers = new ArrayList<>();
        this.identifiers.add(identifier);
    }
    
    public RelationIn(String identifier, List<Term> terms) {
        this(identifier);
        this.terms = terms;
    }   
       
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(identifiers.get(0));
        sb.append(" ").append(operator).append(" ").append("(").append(ParserUtils.stringList(terms, ", ")).append(")");
        return sb.toString();
    }
    
}
