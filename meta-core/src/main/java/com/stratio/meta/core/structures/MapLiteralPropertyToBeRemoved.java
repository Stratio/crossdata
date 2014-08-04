/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */package com.stratio.meta.core.structures;

import com.stratio.meta.core.utils.ParserUtils;

import java.util.HashMap;
import java.util.Map;

public class MapLiteralPropertyToBeRemoved extends ValuePropertyToBeRemoved {
    
    private Map<String, String> literals;

    public MapLiteralPropertyToBeRemoved() {
        literals = new HashMap<>();
        this.type = TYPE_MAPLT;
    }   
    
    public MapLiteralPropertyToBeRemoved(String key, String value){
        this();
        addLiteral(key, value);
    }
    
    public MapLiteralPropertyToBeRemoved(Map<String, String> literals){
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

    @Override
    public String getStringValue() {
        return toString();
    }

}
    
