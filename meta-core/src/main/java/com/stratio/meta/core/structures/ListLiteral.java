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
        sb.append("[ ").append(ParserUtils.stringList(literals, ", ")).append(" ]");
        return sb.toString();
    }
    
    
    
}
