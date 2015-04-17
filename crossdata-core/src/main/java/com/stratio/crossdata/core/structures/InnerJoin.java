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
import com.stratio.crossdata.common.statements.structures.RelationTerm;

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
        for(AbstractRelation r: relations){
            for(TableName tn: tableNames){
                List<Relation> ars = orderRelation(r, tn);
                if((ars != null) && (!ars.isEmpty())){
                    for(Relation ar: ars){
                        int pos = determinePosition(orderedRelations, ar);
                        orderedRelations.add(pos, ar);
                    }
                    break;
                }
            }
        }
        return orderedRelations;
    }

    private int determinePosition(List<Relation> orderedRelations, Relation ar) {
        int pos = 0;
        for(Relation r: orderedRelations){
            int index1 = tableNames.indexOf(r.getLeftTerm().getTableName());
            int index2 = tableNames.indexOf(ar.getLeftTerm().getTableName());
            if(index1 > index2){
                break;
            } else if(index1 == index2){
                int index3 = tableNames.indexOf(r.getRightTerm().getTableName());
                int index4 = tableNames.indexOf(ar.getRightTerm().getTableName());
                if(index3 > index4){
                    break;
                } else {
                    pos++;
                }
            } else {
                pos++;
            }
        }
        return pos;
    }

    private List<Relation> orderRelation(AbstractRelation abstractRelation, TableName tableName) {
        List<Relation> orderedRelations = new ArrayList<>();
        if(abstractRelation instanceof Relation){
            Relation relationConjunction = (Relation) abstractRelation;
            Relation orderedRelation = new Relation(
                    relationConjunction.getLeftTerm(),
                    relationConjunction.getOperator(),
                    relationConjunction.getRightTerm());
            String table = tableName.getQualifiedName();
            String leftTable = relationConjunction.getLeftTerm().getColumnName().getTableName().getQualifiedName();
            String rightTable = relationConjunction.getRightTerm().getColumnName().getTableName().getQualifiedName();
            if(table.equals(leftTable)){
                orderedRelations.add(orderedRelation);
            } else if (table.equals(rightTable)){
                orderedRelation = new Relation(
                        relationConjunction.getRightTerm(),
                        relationConjunction.getOperator(),
                        relationConjunction.getLeftTerm());
                orderedRelations.add(orderedRelation);
            }
        } else if(abstractRelation instanceof RelationDisjunction) {
            RelationDisjunction relationDisjunction = (RelationDisjunction) abstractRelation;
            for(RelationTerm rt: relationDisjunction.getTerms()){
                for(AbstractRelation ab: rt.getRelations()){
                    orderedRelations.addAll(orderRelation(ab, tableName));
                }
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
