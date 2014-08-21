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

package com.stratio.meta.core.utils;

import com.stratio.meta.core.statements.MetaStatement;

public class MetaStep {
    private MetaPath path;
    private String query;
    private MetaStatement stmt;    

    public MetaStep(MetaPath path, String query) {
        this.path = path;
        this.query = query;
    }   

    public MetaStep(MetaPath path, MetaStatement stmt) {
        this.path = path;
        this.stmt = stmt;
    }        
    
    public MetaPath getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public MetaStatement getStmt() {
        return stmt;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(path.name()).append(") ");
        if(stmt != null){
            sb.append(stmt.toString());
        } else {
            sb.append(query);
        }            
        return sb.toString();
    }
 
}
