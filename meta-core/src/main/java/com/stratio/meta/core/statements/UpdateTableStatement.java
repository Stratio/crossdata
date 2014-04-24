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

import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Assignment;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta.core.structures.Relation;
import com.stratio.meta.core.structures.Term;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.List;
import java.util.Map;

public class UpdateTableStatement extends MetaStatement {
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String tableName;
    private boolean optsInc;
    private List<Option> options;
    private List<Assignment> assignments;
    private List<Relation> whereClauses;
    private boolean condsInc;
    private Map<String, Term> conditions;

    public UpdateTableStatement(String tableName,
                                boolean optsInc, 
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<Relation> whereClauses,
                                boolean condsInc, 
                                Map<String, Term> conditions) {
        this.command = false;
        if(tableName.contains(".")){
            String[] ksAndTableName = tableName.split("\\.");
            keyspace = ksAndTableName[0];
            this.tableName = ksAndTableName[1];
            keyspaceInc = true;
        }else{
            this.tableName = tableName;
        }

        this.optsInc = optsInc;
        this.options = options;
        this.assignments = assignments;
        this.whereClauses = whereClauses;
        this.condsInc = condsInc;
        this.conditions = conditions;
    }        
    
    public UpdateTableStatement(String tableName,
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<Relation> whereclauses,
                                Map<String, Term> conditions) {
        this(tableName, true, options, assignments, whereclauses, true, conditions);
    }
    
    public UpdateTableStatement(String tableName,
                                List<Assignment> assignments, 
                                List<Relation> whereclauses,
                                Map<String, Term> conditions) {
        this(tableName, false, null, assignments, whereclauses, true, conditions);
    }
    
    public UpdateTableStatement(String tableName,
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<Relation> whereclauses) {
        this(tableName, true, options, assignments, whereclauses, false, null);
    }
    
    public UpdateTableStatement(String tableName,
                                List<Assignment> assignments, 
                                List<Relation> whereclauses) {
        this(tableName, false, null, assignments, whereclauses, false, null);
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        if(tableName.contains(".")){
            String[] ksAndTablename = tableName.split("\\.");
            keyspace = ksAndTablename[0];
            tableName = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tableName = tableName;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tableName);
        if(optsInc){
            sb.append(" ").append("USING ");
            sb.append(ParserUtils.stringList(options, " AND "));
        }
        sb.append(" ").append("SET ");
        sb.append(ParserUtils.stringList(assignments, ", "));
        sb.append(" ").append("WHERE ");
        sb.append(ParserUtils.stringList(whereClauses, " AND "));
        if(condsInc){
            sb.append(" ").append("IF ");
            sb.append(ParserUtils.stringMap(conditions, " = ", " AND "));
        }
        return sb.toString();
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
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }
    
    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
