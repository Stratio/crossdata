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

import java.util.HashSet;
import java.util.Set;

public class SetLiteral extends IdentIntOrLiteral {
    
    public Set<String> literals;

    public SetLiteral() {
        literals = new HashSet<>();
    }
    
    public SetLiteral(String identifier, char operator, Set<String> literals) {
        this();
        this.identifier = identifier;
        this.operator = operator;
        this.literals = literals;
    }
    
    public SetLiteral(Set<String> literals) {
        this();
        this.literals = literals;
    }
       
    public Set<String> getLiterals() {
        return literals;
    }

    public void setLiterals(Set<String> literals) {
        this.literals = literals;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(string());        
        sb.append(ParserUtils.stringSet(literals, ", "));        
        return sb.toString();
    }
    
}
