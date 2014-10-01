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

package com.stratio.meta2.core.normalizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.structures.OrderBy;

public class NormalizedFields {
    private Set<ColumnName> columnNames = new HashSet<>();
    private Set<TableName> tableNames = new HashSet<>();
    private Set<CatalogName> catalogNames = new HashSet<>();
    private List<Selector> selectors = new ArrayList<>(); // It can includes functions, column names, asterisks...
    private boolean distinctSelect = false;
    private List<Relation> relations = new ArrayList<>(); // Join relations
    private List<Relation> where = new ArrayList<>();
    private OrderBy orderBy = new OrderBy();
    private GroupBy groupBy = new GroupBy();

    public NormalizedFields() {
    }

    public Set<ColumnName> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Set<ColumnName> columnNames) {
        this.columnNames = columnNames;
    }

    public Set<TableName> getTableNames() {
        return tableNames;
    }

    public void setTableNames(Set<TableName> tableNames) {
        this.tableNames = tableNames;
    }

    public Set<CatalogName> getCatalogNames() {
        return catalogNames;
    }

    public void setCatalogNames(Set<CatalogName> catalogNames) {
        this.catalogNames = catalogNames;
    }

    public List<Selector> getSelectors() {
        return selectors;
    }

    public void setSelectors(List<Selector> selectors) {
        this.selectors = selectors;
    }

    public boolean isDistinctSelect() {
        return distinctSelect;
    }

    public void setDistinctSelect(boolean distinctSelect) {
        this.distinctSelect = distinctSelect;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<Relation> getWhere() {
        return where;
    }

    public void setWhere(List<Relation> where) {
        this.where = where;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public GroupBy getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
    }

}
