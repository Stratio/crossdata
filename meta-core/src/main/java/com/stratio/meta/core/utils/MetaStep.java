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
