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

import java.util.HashMap;
import java.util.Map;

public class MapLiteralProperty extends ValueProperty {
    
    private Map<String, String> literals;

    public MapLiteralProperty() {
        literals = new HashMap<>();
        this.type = TYPE_MAPLT;
    }   
    
    public MapLiteralProperty(String key, String value){
        this();
        addLiteral(key, value);
    }
    
    public MapLiteralProperty(Map<String, String> literals){
        this();
        setLiterals(literals);
    }
    
    public Map<String, String> getLiterals() {
        return literals;
    }

    public void setLiterals(Map<String, String> literals) {
        this.literals = literals;
    }   
    
    public int size() {
        return this.literals.size();
    }
    
    public boolean isEmpty() {
        return this.literals.isEmpty();
    }    
    
    public void addLiteral(String key, String value){
        literals.put(key, value);
    }
    
    public String getLiteral(String key){
        return literals.get(key);
    }
    
    public void removeLiteral(String key){
        literals.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(ParserUtils.stringMap(literals, ": ", ", ")).append("}");        
        return sb.toString();
    }
    
}
    
