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
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class AlterTableStatement extends MetaStatement{
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String name_table;
    private int prop;
    private String column;
    private String type;
    private LinkedHashMap<String, ValueProperty> option;
        
        
    public AlterTableStatement(String name_table, String column, String type, LinkedHashMap<String, ValueProperty> option, int prop) {
        this.command = false;
        if(name_table.contains(".")){
            String[] ksAndTablename = name_table.split("\\.");
            keyspace = ksAndTablename[0];
            name_table = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.name_table = name_table;
        this.column = column;
        this.type = type;
        this.option = option;
        this.prop = prop;          
    }
    
    //Setters and getters Name table
    public String getName_table() {
        return name_table;
    }
    
    public void setName_table(String name_table) {
        if(name_table.contains(".")){
            String[] ksAndTablename = name_table.split("\\.");
            keyspace = ksAndTablename[0];
            name_table = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.name_table = name_table;
    }

    public boolean isKeyspaceInc() {
        return keyspaceInc;
    }

    public void setKeyspaceInc(boolean keyspaceInc) {
        this.keyspaceInc = keyspaceInc;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    //Seeters and getters columns
    public String getColumn() {
        return column;
    }  
    
    public void setColumn(String column) {
        this.column = column;
    }
    
    //Setter and getter type 
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    //Setter and getter option
     public LinkedHashMap<String, ValueProperty> getOption() {
        return option;
    }

    public void setOption(LinkedHashMap<String, ValueProperty> option) {
        this.option = option;
    }
    
    //Setter and getter  prop
    public int getProp() {
        return prop;
    }
    
    public void setProp(int prop) {
        this.prop = prop;
    }  

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Alter table ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(name_table);
        switch(prop){
            case 1: {
                sb.append(" alter ");
                sb.append(column);
                sb.append(" type ");
                sb.append(type);
            }break;
            case 2: {
                sb.append(" add ");
                sb.append(column).append(" ");
                sb.append(type);
            }break;
            case 3: {
                sb.append(" drop ");
                sb.append(column);
            }break;
            case 4: {
                Set<String> keySet = option.keySet();
                sb.append(" with");
                for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
                    String key = it.next();
                    ValueProperty vp = option.get(key);
                    sb.append(" ").append(key).append("=").append(String.valueOf(vp));
                    if(it.hasNext()) sb.append(" AND");
                }
            }break;
            default:{
                sb.append("bad option");
            }break;
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
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }
    
    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
