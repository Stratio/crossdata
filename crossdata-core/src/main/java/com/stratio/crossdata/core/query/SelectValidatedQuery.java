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
import java.util.List;

import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.statements.structures.relationships.Relation;
import com.stratio.crossdata.core.structures.InnerJoin;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.TableMetadata;

public class SelectValidatedQuery extends SelectParsedQuery implements ValidatedQuery {

    private List<TableMetadata> tableMetadata=new ArrayList<>();
    private List<ColumnName> columns=new ArrayList<>();
    private List<Relation> relationships=new ArrayList<>();
    private List<TableName> tables=new ArrayList<>();
    private InnerJoin join;

    public SelectValidatedQuery(SelectParsedQuery selectParsedQuery) {
        super(selectParsedQuery);
        setQueryStatus(QueryStatus.VALIDATED);
    }

    public SelectValidatedQuery(SelectValidatedQuery selectValidatedQuery) {
        this((SelectParsedQuery) selectValidatedQuery);
    }

    public List<TableMetadata> getTableMetadata() {
        return tableMetadata;
    }

    public void setTableMetadata(List<TableMetadata> tableMetadata) {
        this.tableMetadata = tableMetadata;
    }

    public List<ColumnName> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnName> columns) {
        this.columns = columns;
    }

    public List<Relation> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relation> relationships) {
        this.relationships = relationships;
    }

    public List<TableName> getTables() {
        return tables;
    }

    public void setTables(List<TableName> tables) {
        this.tables = tables;
    }

    public InnerJoin getJoin() {
        return join;
    }

    public void setJoin(InnerJoin join) {
        this.join = join;
    }

}

