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

import java.util.List;

public abstract class MetaRelation {
    
    public static final int TYPE_COMPARE = 1;
    public static final int TYPE_IN = 2;
    public static final int TYPE_TOKEN = 3;
    public static final int TYPE_BETWEEN = 4;
    
    protected List<String> identifiers;
    protected String operator;
    protected List<Term> terms;
    protected int type;   
    
    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }
    
    public int numberOfIdentifiers(){
        return this.identifiers.size();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<Term> getTerms() {
        return terms;
    }
    
    public int numberOfTerms() {
        return this.terms.size();
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }        
    
    @Override
    public abstract String toString();
    
}
