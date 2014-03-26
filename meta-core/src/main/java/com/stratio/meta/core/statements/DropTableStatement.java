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

import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropTableStatement extends MetaStatement {
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String ident;
    private boolean ifExists;

    public DropTableStatement(String ident, boolean ifExists) {
        this.command = false;
        if(ident.contains(".")){
            String[] ksAndTablename = ident.split("\\.");
            keyspace = ksAndTablename[0];
            ident = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.ident = ident;
        this.ifExists = ifExists;
    }
    
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        if(ident.contains(".")){
            String[] ksAndTablename = ident.split("\\.");
            keyspace = ksAndTablename[0];
            ident = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.ident = ident;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Drop table ");
        if(ifExists){
            sb.append("if exists ");
        }       
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(ident);                
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public MetaResult validate(MetadataManager metadata, String targetKeyspace) {
        return null;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }
            
//    @Override
//    public String parseResult(ResultSet resultSet) {
//        return "Executed successfully";
//    }

    @Override
    public Statement getDriverStatement() {
        Statement statement = null;
        return statement;
    }

    @Override
    public DeepResult executeDeep() {
        return new DeepResult("", new ArrayList<>(Arrays.asList("Not supported yet")));
    }
       
    @Override
    public Tree getPlan() {
        return new Tree();
    }
    
}
