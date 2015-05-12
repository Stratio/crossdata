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

package com.stratio.crossdata.core.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.statements.structures.AbstractRelation;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.RelationDisjunction;
import com.stratio.crossdata.common.statements.structures.RelationTerm;
import com.stratio.crossdata.core.structures.Join;

/**
 * Class that implement the logic to de select validated queries.
 */
public class SelectValidatedQuery extends SelectParsedQuery implements IValidatedQuery {

    private List<TableMetadata> tableMetadata = new ArrayList<>();
    private List<ColumnName> columns = new ArrayList<>();
    private List<AbstractRelation> relations = new ArrayList<>();
    private List<TableName> tables = new ArrayList<>();
    private SelectValidatedQuery subqueryValidatedQuery;
    private List<Join> joinList=new ArrayList<>();

    /**
     * Constructor class.
     * @param selectParsedQuery A Select parsed query.
     */
    public SelectValidatedQuery(SelectParsedQuery selectParsedQuery) {
        super(selectParsedQuery);
        setQueryStatus(QueryStatus.VALIDATED);
    }

    /**
     * Constructor class.
     * @param selectValidatedQuery A select validated query.
     */
    public SelectValidatedQuery(SelectValidatedQuery selectValidatedQuery) {
        this((SelectParsedQuery) selectValidatedQuery);
    }

    /**
     * Get the list of tables of the validated select query.
     * @return A list of {@link com.stratio.crossdata.common.metadata.TableMetadata} .
     */
    public List<TableMetadata> getTableMetadata() {
        return tableMetadata;
    }

    /**
     * Set the table metadata to the select validated query.
     * @param tableMetadata The {@link com.stratio.crossdata.common.metadata.TableMetadata} .
     */
    public void setTableMetadata(List<TableMetadata> tableMetadata) {
        this.tableMetadata = tableMetadata;
    }

    /**
     * Get the list of columns of the validated select query.
     * @return A list of {@link com.stratio.crossdata.common.data.ColumnName} .
     */
    public List<ColumnName> getColumns() {
        return columns;
    }

    /**
     * Set the Column names to the select validated query.
     * @param columns The list of {@link com.stratio.crossdata.common.data.ColumnName} .
     */
    public void setColumns(List<ColumnName> columns) {
        this.columns = columns;
    }

    /**
     * Get the list of relations of the validated select query.
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation} .
     */
    public List<AbstractRelation> getRelations() {
        return relations;
    }

    /**
     * Get the list of relations of the validated select query which involve a single table.
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation} .
     */
    public List<AbstractRelation> getBasicRelations() {
        List<AbstractRelation> relationList = new ArrayList<>();
        for (AbstractRelation relation : relations) {
            if( relation.isBasicRelation()){
                relationList.add(relation);
            }
        }
        return relationList;
    }

    /**
     * Get the list of relations of the validated select query which involves joined tables.
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation} .
     */
    public List<AbstractRelation> getComposeRelations() {
        List<AbstractRelation> relationList = new ArrayList<>();
        for (AbstractRelation relation : relations) {
            if( !relation.isBasicRelation()){
                relationList.add(relation);
            }
        }
        return relationList;
    }

    /**
     * Set the Relations metadata to the select validated query.
     * @param relations The list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation} .
     */
    public void setRelations(List<AbstractRelation> relations) {
        this.relations = relations;
    }

    /**
     * Get the list of tables of the validated select query.
     * @return A list of {@link com.stratio.crossdata.common.data.TableName} .
     */
    public List<TableName> getTables() {
        return tables;
    }

    /**
     * Set the table names to the select validated query.
     * @param tables The list of {@link com.stratio.crossdata.common.data.TableName} .
     */
    public void setTables(List<TableName> tables) {
        this.tables = tables;
    }

    /**
     * Get the list of Joins of the validated select query.
     * @return A  list of {@link com.stratio.crossdata.core.structures.Join} .
     */
    public List<Join> getJoinList() {
        return joinList;
    }

    /**
     * Add a join metadata to the select validated query.
     * @param join The {@link com.stratio.crossdata.core.structures.Join} .
     */
    public void addJoin(Join join) {
        this.joinList.add(join);
    }

    /**
     * Set the subquery to the select validated query.
     *
     * @param subqueryValidatedQuery the inner select validated query.
     */
    public void setSubqueryValidatedQuery(SelectValidatedQuery subqueryValidatedQuery) {
        this.subqueryValidatedQuery = subqueryValidatedQuery;
    }

    /**
     * Get the inner select validated query.
     * @return A {@link com.stratio.crossdata.core.query.SelectValidatedQuery} .
     */
    public SelectValidatedQuery getSubqueryValidatedQuery() {
        return subqueryValidatedQuery;
    }


    /**
     * Set a complete list of inner joins.
     * @param joinList The inner join list.
     */
    public void setJoinList(List<Join> joinList){
        this.joinList= joinList;
    }

    /** Try to optimize the query in order to reduce its complexity.
    */
    public void optimizeQuery() {
        if(!joinList.isEmpty()) {
            optimizeJoins();
        }
        optimizeRelations();
    }

    private void optimizeRelations() {
        boolean trueRelationFound = false;
        trueRelationFound = optimizeRelations(relations);
        if(trueRelationFound){
            this.getStatement().setWhere(relations);
        }
    }

    private boolean optimizeRelations(List<AbstractRelation> relations){
        boolean trueRelationFound = false;
        for (Iterator<AbstractRelation> iterator = relations.iterator(); iterator.hasNext();) {
            AbstractRelation abstractRelation = iterator.next();
            if(abstractRelation instanceof Relation){
                Relation relationConjunction = (Relation) abstractRelation;
                if(relationConjunction.getOperator() == Operator.EQ){
                    if(relationConjunction.getLeftTerm().equals(relationConjunction.getRightTerm())){
                        iterator.remove();
                        trueRelationFound = true;
                    }
                }
            } else if(abstractRelation instanceof RelationDisjunction){
                RelationDisjunction rd = (RelationDisjunction) abstractRelation;
                for(RelationTerm rt: rd.getTerms()){
                    optimizeRelations(rt.getRelations());
                }
            }
        }
        return trueRelationFound;
    }

    private void optimizeJoins() {
        if(tables.size() == 1){
            convertJoinConditionsToRelations();
        } else if(tables.size() > 1) {
            convertJoinsToCrossJoins();
        }
    }

    private void convertJoinsToCrossJoins() {
        for(Join join: joinList) {
            List<AbstractRelation> joinRelations = join.getRelations();
            Join crossJoin = null;
            for(AbstractRelation ar: joinRelations){
                if(ar.getAbstractRelationTables().size() == 1){
                    crossJoin = createCrossJoin(join);
                    break;
                }
            }
            if(crossJoin != null){
                this.getStatement().removeJoin(join);
                this.getStatement().addJoin(crossJoin);
                this.joinList.remove(join);
                this.joinList.add(crossJoin);
            }
        }
    }

    private Join createCrossJoin(Join join) {
        List<TableName> tableNames = new ArrayList<>();
        tableNames.addAll(join.getTableNames());
        Join crossJoin = new Join(tableNames);
        return crossJoin;
    }

    private void convertJoinConditionsToRelations() {
        for(Join join: joinList) {
            relations.addAll(join.getRelations());
            this.getStatement().setWhere(relations);
            this.getStatement().removeJoin(join);
        }
        joinList.clear();
    }

}

