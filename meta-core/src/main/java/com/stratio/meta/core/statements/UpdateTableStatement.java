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

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Assignment;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta.core.structures.Relation;
import com.stratio.meta.core.structures.Term;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.List;
import java.util.Map;

/**
 * Class that models an {@code UPDATE} statement from the META language.
 */
public class UpdateTableStatement extends MetaStatement {

    /**
     * The name of the table.
     */
    private String tableName;

    /**
     * Whether options are included.
     */
    private boolean optsInc;

    /**
     * The list of options.
     */
    private List<Option> options;

    /**
     * The list of assignments.
     */
    private List<Assignment> assignments;

    /**
     * The list of relations.
     */
    private List<Relation> whereClauses;

    /**
     * Whether conditions are included.
     */
    private boolean condsInc;

    /**
     * Map of conditions.
     */
    private Map<String, Term> conditions;

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param optsInc Whether options are included.
     * @param options The list of options.
     * @param assignments The list of assignments.
     * @param whereClauses The list of relations.
     * @param condsInc Whether conditions are included.
     * @param conditions The map of conditions.
     */
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

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param options The list of options.
     * @param assignments The list of assignments.
     * @param whereClauses The list of relations.
     * @param conditions The map of conditions.
     */
    public UpdateTableStatement(String tableName,
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<Relation> whereClauses,
                                Map<String, Term> conditions) {
        this(tableName, true, options, assignments, whereClauses, true, conditions);
    }

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param assignments The list of assignments.
     * @param whereClauses The list of relations.
     * @param conditions The map of conditions.
     */
    public UpdateTableStatement(String tableName,
                                List<Assignment> assignments, 
                                List<Relation> whereClauses,
                                Map<String, Term> conditions) {
        this(tableName, false, null, assignments, whereClauses, true, conditions);
    }

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param options The list of options.
     * @param assignments The list of assignments.
     * @param whereClauses The list of relations.
     */
    public UpdateTableStatement(String tableName,
                                List<Option> options, 
                                List<Assignment> assignments, 
                                List<Relation> whereClauses) {
        this(tableName, true, options, assignments, whereClauses, false, null);
    }

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param assignments The list of assignments.
     * @param whereClauses The list of relations.
     */
    public UpdateTableStatement(String tableName,
                                List<Assignment> assignments, 
                                List<Relation> whereClauses) {
        this(tableName, false, null, assignments, whereClauses, false, null);
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
    public String translateToCQL() {
        return this.toString();
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
