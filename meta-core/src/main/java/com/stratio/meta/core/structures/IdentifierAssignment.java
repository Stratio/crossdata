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

import com.stratio.meta.common.statements.structures.terms.Term;

public class IdentifierAssignment {
    
    public static final int TYPE_SIMPLE = 1;
    public static final int TYPE_COMPOUND = 2;
    
    private String identifier;
    private Term term;
    private int type;

    public IdentifierAssignment(String identifier, Term term, int type) {
        this.identifier = identifier;
        this.term = term;
        this.type = type;
    }
    
    public IdentifierAssignment(String identifier) {
        this(identifier, null, TYPE_SIMPLE);
    }
    
    public IdentifierAssignment(String identifier, Term term) {
        this(identifier, term, TYPE_COMPOUND);
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(identifier);
        if(type == TYPE_COMPOUND){
            sb.append("[").append(term.toString()).append("]");
        }
        return sb.toString();
    }
    
}
