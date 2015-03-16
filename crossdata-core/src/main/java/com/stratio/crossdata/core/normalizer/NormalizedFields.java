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

package com.stratio.crossdata.core.normalizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.AbstractRelation;
import com.stratio.crossdata.common.statements.structures.OrderByClause;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.structures.GroupByClause;
import com.stratio.crossdata.core.structures.InnerJoin;

/**
 * Collection of fields that are processed by a {@link com.stratio.crossdata.core.normalizer.Normalizator} process.
 */
public class NormalizedFields {

    /**
     * Set of {@link com.stratio.crossdata.common.data.ColumnName} involved in a query.
     */
    private Set<ColumnName> columnNames = new LinkedHashSet<>();

    /**
     * Set of {@link com.stratio.crossdata.common.data.TableName} involved in a query.
     */
    private Set<TableName> tableNames = new LinkedHashSet<>();

    private Set<TableName> preferredTableNames = new LinkedHashSet<>();

    /**
     * Set of {@link com.stratio.crossdata.common.data.CatalogName} involved in a query.
     */
    private Set<CatalogName> catalogNames = new LinkedHashSet<>();

    /**
     * List of {@link com.stratio.crossdata.common.statements.structures.Selector} involved in a query including
     * column names, functions, asterisks, etc.
     */
    private List<Selector> selectors = new ArrayList<>();

    /**
     * List of Inner join information.
     */
    private List<InnerJoin> joinList=new ArrayList<>();

    /**
     * List of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation} in the where clause.
     */
    private List<AbstractRelation> where = new ArrayList<>();

    /**
     * List of {@link com.stratio.crossdata.common.statements.structures.OrderByClause}.
     */
    private List<OrderByClause> orderByClauses = new ArrayList<>();

    /**
     * List of {@link com.stratio.crossdata.core.structures.GroupByClause}.
     */
    private GroupByClause groupByClause = new GroupByClause();

    /**
     * List of {@link com.stratio.crossdata.common.metadata.TableMetadata} involved in a query.
     */
    private List<TableMetadata> tablesMetadata = new ArrayList<>();

    /**
     * Map of tables alias associating alias with TableNames.
     */
    private Map<String, TableName> tableAlias = new HashMap<>();

    /**
     * Map of column alias associating alias with ColumnNames.
     */
    private Map<String, ColumnName> columnNameAlias = new HashMap<>();

    /**
     * Class constructor.
     */
    public NormalizedFields() {
    }

    /**
     * Add a new column.
     *
     * @param columnName The {@link com.stratio.crossdata.common.data.ColumnName}.
     * @param alias      The associated alias.
     */
    public void addColumnName(ColumnName columnName, String alias) {
        this.columnNames.add(columnName);
        if (alias != null) {
            this.columnNameAlias.put(alias, columnName);
        }
    }

    /**
     * Get the set of column names.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.ColumnName}.
     */
    public Set<ColumnName> getColumnNames() {
        return columnNames;
    }

    /**
     * Add a table name to the list. If an alias is found the alias mapping is also stored.
     *
     * @param tableName A {@link com.stratio.crossdata.common.data.TableName}.
     */
    public void addTableName(TableName tableName) {
        this.tableNames.add(tableName);
        if (tableName.getAlias() != null) {
            tableAlias.put(tableName.getAlias(), tableName);
        }
    }

    /**
     * Get the table names.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.TableName}.
     */
    public Set<TableName> getTableNames() {
        return tableNames;
    }

    /**
     * Get the catalogs.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.CatalogName}.
     */
    public Set<CatalogName> getCatalogNames() {
        return catalogNames;
    }

    /**
     * Get the list of selectors.
     *
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.Selector}.
     */
    public List<Selector> getSelectors() {
        return selectors;
    }

    /**
     * Get the inner join information.
     *
     * @return A {@link com.stratio.crossdata.core.structures.InnerJoin}.
     */
    public List<InnerJoin> getJoinList() {
        return joinList;
    }

    /**
     * Set the inner join information.
     *
     * @param join A {@link com.stratio.crossdata.core.structures.InnerJoin}.
     */
    public void addJoin(InnerJoin join) {
        this.joinList.add(join);
    }

    /**
     * Get the list of relation on the where clause.
     *
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     */
    public List<AbstractRelation> getWhere() {
        return where;
    }

    /**
     * Set the where clause.
     *
     * @param where A list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     */
    public void setWhere(List<AbstractRelation> where) {
        this.where = where;
    }

    /**
     * Set the order by clauses.
     *
     * @param orderByClauses A list of {@link com.stratio.crossdata.common.statements.structures.OrderByClause}.
     */
    public void setOrderByClauses(List<OrderByClause> orderByClauses) {
        this.orderByClauses = orderByClauses;
    }

    /**
     * Get the group by clause.
     *
     * @return A {@link com.stratio.crossdata.core.structures.GroupByClause}.
     */
    public GroupByClause getGroupByClause() {
        return groupByClause;
    }

    /**
     * Set the group by clause.
     *
     * @param groupByClause A {@link com.stratio.crossdata.core.structures.GroupByClause}.
     */
    public void setGroupByClause(GroupByClause groupByClause) {
        this.groupByClause = groupByClause;
    }

    /**
     * Get the list of tables.
     *
     * @return A list of {@link com.stratio.crossdata.common.metadata.TableMetadata}.
     */
    public List<TableMetadata> getTablesMetadata() {

            //recover all Metadata about a tableName
            for (TableName tableName : tableNames) {
                if(!tableName.isVirtual()) {
                    tablesMetadata.add(MetadataManager.MANAGER.getTable(tableName));
                }
            }

        return tablesMetadata;
    }

    /**
     * Get whether an alias for a given table name exists.
     *
     * @param name The alias name.
     * @return Whether it exists or not.
     */
    public boolean existTableAlias(final String name) {
        return tableAlias.containsKey(name);
    }

    /**
     * Get whether an alias for a given column name exists.
     *
     * @param name The alias name.
     * @return Whether it exists or not.
     */
    public boolean existColumnAlias(final String name) {
        return columnNameAlias.containsKey(name);
    }

    /**
     * Get the table name associated with an alias.
     *
     * @param alias The alias.
     * @return A {@link com.stratio.crossdata.common.data.TableName} or null if not exists.
     */
    public TableName getTableName(final String alias) {
        return tableAlias.get(alias);
    }

    /**
     * Get the column name associated with an alias.
     *
     * @param alias The alias.
     * @return A {@link com.stratio.crossdata.common.data.ColumnName} or null if not exists.
     */
    public ColumnName getColumnName(final String alias) {
        return columnNameAlias.get(alias);
    }

    public Set<TableName> getPreferredTableNames() {
        return preferredTableNames;
    }

    public void setPreferredTableNames(Set<TableName> preferredTableNames) {
        this.preferredTableNames = preferredTableNames;
    }
}
