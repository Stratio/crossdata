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

package com.stratio.crossdata.core.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.stratio.crossdata.common.data.JoinType;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.AbstractRelation;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.RelationDisjunction;

/**
 * InnerJoin metadata class.
 */
public class InnerJoin implements Serializable {

    private final List<TableName> tableNames = new ArrayList<>();

    private final List<AbstractRelation> relations = new ArrayList<>();

    private JoinType type;

    private InnerJoin(List<TableName> tableNames) {
        LinkedHashSet setOfTables = new LinkedHashSet<>();
        setOfTables.addAll(tableNames);
        this.tableNames.addAll(setOfTables);
    }

    /**
     * Constructor class.
     * @param tableNames The table names of the join.
     * @param joinRelations The list of relations of the join.
     */
    public InnerJoin(List<TableName> tableNames, List<AbstractRelation> joinRelations) {
        this(tableNames);
        this.relations.addAll(joinRelations);
        this.type=JoinType.INNER;
    }

    /**
     * Constructor class.
     * @param tableNames The table names of the join.
     * @param joinRelations The list of relations of the join.
     * @param type The type of the join.
     */
    public InnerJoin(List<TableName> tableNames, List<AbstractRelation> joinRelations, JoinType type) {
        this(tableNames);
        this.relations.addAll(joinRelations);
        this.type=type;
    }

    public List<TableName> getTableNames() {
        return tableNames;
    }

    public List<AbstractRelation> getRelations() {
        return relations;
    }

    public void addRelations(List<AbstractRelation> joinRelations){
        relations.addAll(joinRelations);
    }

    public void addRelation(AbstractRelation joinRelations){
        relations.add(joinRelations);
    }

    /**
     * Get the ordered relations of the Join according to the appearance order of the tables in the query.
     * @return A list with the ordered relations.
     */
    public List<Relation> getOrderedRelations() {
        List<Relation> orderedRelations = new ArrayList<>();
        for(TableName t: tableNames){
            for(AbstractRelation relation: relations){
                orderedRelations.addAll(orderRelation(relation, t));
            }
        }
        return orderedRelations;
    }

    private List<Relation> orderRelation(AbstractRelation abstractRelation, TableName tableName) {
        List<Relation> orderedRelations = new ArrayList<>();
        if(abstractRelation instanceof Relation){
            Relation relationConjunction = (Relation) abstractRelation;
            Relation orderedRelation = new Relation(
                    relationConjunction.getLeftTerm(),
                    relationConjunction.getOperator(),
                    relationConjunction.getRightTerm());
            String joinTable = tableName.getQualifiedName();
            String rightTable = relationConjunction.getRightTerm().getColumnName().getTableName().getQualifiedName();
            String leftTable = relationConjunction.getLeftTerm().getColumnName().getTableName().getQualifiedName();
            if(joinTable.equals(leftTable)){
                orderedRelation = new Relation(
                        relationConjunction.getRightTerm(),
                        relationConjunction.getOperator(),
                        relationConjunction.getLeftTerm());
                orderedRelations.add(orderedRelation);
            } else if (joinTable.equals(rightTable)){
                orderedRelations.add(orderedRelation);
            }
        } else if(abstractRelation instanceof RelationDisjunction) {
            RelationDisjunction relationDisjunction = (RelationDisjunction) abstractRelation;
            for(AbstractRelation innerRelation: relationDisjunction.getLeftRelations()){
                orderedRelations.addAll(orderRelation(innerRelation, tableName));
            }
            for(AbstractRelation innerRelation: relationDisjunction.getRightRelations()){
                orderedRelations.addAll(orderRelation(innerRelation, tableName));
            }
        }
        return orderedRelations;
    }

    /**
     * Get the type of the Join.
     * @return A {@link com.stratio.crossdata.common.data.JoinType} .
     */
    public JoinType getType() {
        return type;
    }

    /**
     * Set the type of the join.
     * @param type {@link com.stratio.crossdata.common.data.JoinType} .
     */
    public void setType(JoinType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<TableName> tables = new ArrayList<>();
        tables.addAll(tableNames);
        tables.remove(0);
        Iterator<TableName> iter = tables.iterator();
        while(iter.hasNext()){
            TableName tableName = iter.next();
            sb.append(tableName);
            if(iter.hasNext()){
                sb.append(", ");
            }
        }
        sb.append(" ON ");
        Iterator<AbstractRelation> it = relations.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }

}
