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

public class SelectorIdentifier extends SelectorMeta {

    private String identifier;

    public SelectorIdentifier(String identifier) {
        this.type = TYPE_IDENT;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }        
    
    public boolean isColumnSelector(){
        return identifier.contains(".");
    }
    
    public String getTablename(){
        if(identifier.contains(".")){
            return identifier.split("\\.")[0];
        }
        return identifier;
    }
    
    public String getColumnName(){
        if(identifier.contains(".")){
            return identifier.split("\\.")[1];
        }
        return identifier;
    }
    
    @Override
    public String toString() {
        return identifier;
    }
    
}
