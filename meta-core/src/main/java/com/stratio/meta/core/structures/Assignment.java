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

public class Assignment {

    private IdentifierAssignment ident; 
    private ValueAssignment value; 

    public Assignment(IdentifierAssignment ident, ValueAssignment value) {
        this.ident = ident;
        this.value = value;
    }   
    
    public IdentifierAssignment getIdent() {
        return ident;
    }

    public void setIdent(IdentifierAssignment ident) {
        this.ident = ident;
    }

    public ValueAssignment getValue() {
        return value;
    }

    public void setValue(ValueAssignment value) {
        this.value = value;
    }   
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(ident.toString());
        sb.append(" = ");
        sb.append(value.toString());
        return sb.toString();
    }
    
}
