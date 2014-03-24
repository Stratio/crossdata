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

public class RelationToken extends MetaRelation {
    
    private boolean righSideTokenType = false;
    
    public RelationToken(List<String> identifiers) {
        this.terms = new ArrayList<>();
        this.type = TYPE_TOKEN;
        this.identifiers = new ArrayList<>();
        this.identifiers = identifiers;
    }   
    
    public RelationToken(List<String> identifiers, String operator) {
        this(identifiers);
        this.operator = operator;
    }
    
    public RelationToken(List<String> identifiers, String operator, Term term) {
        this(identifiers, operator);        
        this.terms.add(term);
    }
    
    public RelationToken(List<String> identifiers, String operator, List<Term> terms) {
        this(identifiers, operator); 
        this.terms = terms;
        this.righSideTokenType = true;
    }

    public boolean isRighSideTokenType() {
        return righSideTokenType;
    }

    public void setRighSideTokenType(boolean righSideTokenType) {
        this.righSideTokenType = righSideTokenType;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOKEN(");
        sb.append(ParserUtils.stringList(identifiers, ", ")).append(")");
        sb.append(" ").append(operator).append(" ");
        if(righSideTokenType){
            sb.append("TOKEN(").append(ParserUtils.stringList(terms, ", ")).append(")");
        } else {
            sb.append(ParserUtils.stringList(terms, ", "));
        }
        return sb.toString();
    }
    
}
