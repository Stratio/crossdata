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

public class InnerJoin {

    private String keyspace = null;

    private boolean keyspaceInc = false;

    private String tableName;

    private Map<String, String> fields;

    public InnerJoin(String tableName, Map<String, String> fields) {
        this.tableName = tableName;

        if(this.tableName.contains(".")){
            String[] ksAndTablename = this.tableName.split("\\.");
            keyspace = ksAndTablename[0];
            this.tableName = ksAndTablename[1];
            keyspaceInc = true;
        }

        this.fields = fields;
    }   
    
    public String getTablename() {
        return tableName;
    }

    public String getKeyspace(){
        return keyspace;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public Map<String, String> getColNames(){
        Map<String, String> colNames = new HashMap<String, String>();
        for(String key: fields.keySet()){
            String field = fields.get(key);
            String[] ksAndTablenameValue= field.split("\\.");
            String[] ksAndTablenameKey = key.split("\\.");
            colNames.put(ksAndTablenameKey[1], ksAndTablenameValue[1]);
        }
        return colNames;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(keyspaceInc){
            sb.append(keyspace);
            sb.append(".");
        }
        sb.append(tableName);
        sb.append(" ON ").append(ParserUtils.stringMap(fields, "=", " AND "));
        return sb.toString();
    }

    public boolean isKeyspaceInc() {
        return keyspaceInc;
    }
}
