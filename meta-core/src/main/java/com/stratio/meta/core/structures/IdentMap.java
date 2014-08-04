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

public class IdentMap {
    
    private String identifier;
    private MapLiteralProperty mlp;

    public IdentMap(String identifier) {
        this.identifier = identifier;
        this.mlp = new MapLiteralProperty();
    }
    
    public IdentMap(String identifier, MapLiteralProperty mlp) {
        this(identifier);
        this.mlp = mlp;
    }   
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MapLiteralProperty getMlp() {
        return mlp;
    }

    public void setMlp(MapLiteralProperty mlp) {
        this.mlp = mlp;
    }   
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(identifier);
        sb.append(" + ");
        sb.append(mlp.toString());
        return sb.toString();
    }
    
}
