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

package com.stratio.meta.core.statements;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.Tree;
import java.util.ArrayList;
import java.util.Arrays;

public class DescribeStatement extends MetaStatement {

    private DescribeType type;
    private String keyspace;
    private String tablename;

    public DescribeStatement(DescribeType type) {
        this.type = type;
        this.command = true;
    }
    
    public DescribeStatement(DescribeType type, String keyspace, String tablename) {
        this(type);
        this.keyspace = keyspace;
        this.tablename = tablename;
    }        
    
    public DescribeType getType() {
        return type;
    }

    public void setType(DescribeType type) {
        this.type = type;
    }        

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
        }
        this.tablename = tablename;
    }   
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DESCRIBE ");
        sb.append(type.name()).append(" ");
        if(type == DescribeType.KEYSPACE){
            sb.append(keyspace);            
        } else {
            if(keyspace != null){
                sb.append(keyspace).append(".");
            }
            sb.append(tablename);
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        return QueryResult.CreateSuccessQueryResult();
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }

    @Override
    public Statement getDriverStatement() {
        return null;
    }

    @Override
    public DeepResult executeDeep() {
        return new DeepResult("", new ArrayList<>(Arrays.asList("Not supported yet")));
    }

    @Override
    public Tree getPlan() {
        return new Tree();
    }
    
    public String execute(Session session){
        MetadataManager mm = new MetadataManager(session);
        mm.loadMetadata();
        String result;
        if(type == DescribeType.KEYSPACE){            
            result =  mm.getKeyspaceMetadata(keyspace).exportAsString();
            if(result == null){
               result = "KEYSPACE "+keyspace+" was not found"; 
            }
        } else {
            result = mm.getTableMetadata(keyspace, tablename).exportAsString();
            if(result == null){
                result = "TABLE "+tablename+" was not found";
            }
        }        
        return result;
    }
    
}
