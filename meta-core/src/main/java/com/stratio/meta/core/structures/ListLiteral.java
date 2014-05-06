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

public class ListLiteral extends IdentIntOrLiteral {
    
    private List<String> literals;

    public ListLiteral() {
        literals = new ArrayList<>();
    }
    
    public ListLiteral(String identifier, char operator, List<String> literals) {
        this();
        this.identifier = identifier;
        this.operator = operator;
        this.literals = literals;
    }
    
    public ListLiteral(List<String> literals) {
        this();
        this.literals = literals;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public void setLiterals(List<String> literals) {
        this.literals = literals;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(string());
        sb.append(ParserUtils.stringList(literals, ", "));
        return sb.toString();
    }
    
    
    
}
