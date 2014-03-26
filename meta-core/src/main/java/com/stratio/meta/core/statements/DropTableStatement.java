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

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
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
    private String tablename;
    private boolean ifExists;

    public DropTableStatement(String tablename, boolean ifExists) {
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tablename = tablename;
        this.ifExists = ifExists;
    }
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tablename = tablename;
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
        sb.append(tablename);
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public MetaResult validate(MetadataManager metadata, String targetKeyspace) {
        MetaResult result = new MetaResult();

        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }

        //Check that the keyspace and table exists.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result.setErrorMessage("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result.setErrorMessage("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tablename);
                if (tableMetadata == null) {
                    result.setErrorMessage("Table " + tablename + " does not exists.");
                }
            }

        }

        return result;
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
