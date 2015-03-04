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
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.SelectorType;
import com.stratio.crossdata.core.normalizer.NormalizedFields;
import com.stratio.crossdata.core.structures.InnerJoin;

/**
 * Class that implement the logic to de select validated queries.
 */
public class SelectValidatedQuery extends SelectParsedQuery implements IValidatedQuery {

    private List<TableMetadata> tableMetadata = new ArrayList<>();
    private List<ColumnName> columns = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();
    private List<TableName> tables = new ArrayList<>();
    private InnerJoin join;
    private SelectValidatedQuery subqueryValidatedQuery;

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
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.Relation} .
     */
    public List<Relation> getRelations() {
        return relations;
    }

    /**
     * Set the Relations metadata to the select validated query.
     * @param relations The list of {@link com.stratio.crossdata.common.statements.structures.Relation} .
     */
    public void setRelations(List<Relation> relations) {
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
     * @return A  {@link com.stratio.crossdata.core.structures.InnerJoin} .
     */
    public InnerJoin getJoin() {
        return join;
    }

    /**
     * Set the join metadata to the select validated query.
     * @param join The {@link com.stratio.crossdata.core.structures.InnerJoin} .
     */
    public void setJoin(InnerJoin join) {
        this.join = join;
    }


    /**
     * Add a new subquery.
     *
     * @param subqueryValidatedQuery Subquery's normalized fields.
     */
    public void setSubqueryValidatedQuery(SelectValidatedQuery subqueryValidatedQuery) {
        this.subqueryValidatedQuery = subqueryValidatedQuery;
    }


    public SelectValidatedQuery getSubqueryValidatedQuery() {
        return subqueryValidatedQuery;
    }


    /** Try to optimize the query in order to reduce its complexity.
    */
    public void optimizeQuery() {
        if(join != null ) {
            optimizeJoins();
        }
        optimizeRelations();
    }

    private void optimizeRelations() {
        boolean trueRelationFound = false;
        for (Iterator<Relation> iterator = relations.iterator(); iterator.hasNext();) {
            Relation relation = iterator.next();
            if(relation.getOperator() == Operator.EQ){
                if(relation.getLeftTerm().equals(relation.getRightTerm())){
                    iterator.remove();
                    trueRelationFound = true;
                }
            }
        }
        if(trueRelationFound){
            this.getStatement().setWhere(relations);
        }
    }

    private void optimizeJoins() {

        //TODO when multiple joins supported => tables.size() = joins.size();
        if(tables.size() == 1 ){
            relations.addAll(join.getRelations());
            this.getStatement().setWhere(relations);
            this.getStatement().setJoin(null);
            join = null;
        }
    }

}

