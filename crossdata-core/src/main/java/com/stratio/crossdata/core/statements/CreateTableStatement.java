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

package com.stratio.crossdata.core.statements;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.structures.TableType;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * Class that models a {@code CREATE TABLE} statement of the CROSSDATA language.
 */
public class CreateTableStatement extends AbstractMetadataTableStatement implements ITableStatement {

    private TableType tableType = TableType.DATABASE;

    private ClusterName clusterName;

    /**
     * A map with the name of the columns in the table and the associated data type.
     */
    private Map<ColumnName, ColumnType> columnsWithType = new LinkedHashMap<>();

    /**
     * The list of columns that are part of the primary key.
     */
    private Set<ColumnName> primaryKey = new LinkedHashSet<>();

    /**
     * The list of columns that are part of the partition key.
     */
    private Set<ColumnName> partitionKey = new LinkedHashSet<>();

    /**
     * The list of columns that are part of the clustering key.
     */
    private Set<ColumnName> clusterKey = new LinkedHashSet<>();

    /**
     * The list of {@link com.stratio.crossdata.core.structures.Property} of the table.
     */
    private Map<Selector, Selector> properties = new LinkedHashMap<>();

    /**
     * Whether the table should be created only if not exists.
     */
    private boolean ifNotExists;

    /**
     * Whether the table has been created previously or not
     */
    private boolean isExternal;

    /**
     * Class constructor.
     *
     * @param tableType    TABLE type {@link com.stratio.crossdata.common.metadata.structures.TableType}.
     * @param tableName    The name of the table.
     * @param clusterName  The cluster name that correspond with the table.
     * @param columns      A map with the name of the columns in the table and the associated data type.
     * @param partitionKey The list of columns that are part of the primary key.
     * @param clusterKey   The list of columns that are part of the clustering key.
     * @param isExternal   Whether the table has been created previously or not.
     */
    public CreateTableStatement(TableType tableType, TableName tableName, ClusterName clusterName,
            LinkedHashMap<ColumnName, ColumnType> columns,
            LinkedHashSet<ColumnName> partitionKey, LinkedHashSet<ColumnName> clusterKey, boolean isExternal) {
        this.command = false;
        this.tableType = tableType;
        this.tableStatement.setTableName(tableName);
        this.clusterName = clusterName;
        this.columnsWithType = columns;
        this.partitionKey = partitionKey;
        this.clusterKey = clusterKey;
        this.isExternal = isExternal;
        if (partitionKey != null) {
            this.primaryKey.addAll(partitionKey);
        }
        if (clusterKey != null) {
            this.primaryKey.addAll(clusterKey);
        }

    }

    /**
     * Class constructor.
     *
     * @param tableName    The name of the table.
     * @param columns      A map with the name of the columns in the table and the associated data type.
     * @param clusterName  The cluster name that correspond with the table.
     * @param partitionKey The list of columns that are part of the primary key.
     * @param clusterKey   The list of columns that are part of the clustering key.
     * @param isExternal   Whether the table has been created previously or not.
     */
    public CreateTableStatement(TableName tableName, ClusterName clusterName,
            LinkedHashMap<ColumnName, ColumnType> columns,
            LinkedHashSet<ColumnName> partitionKey, LinkedHashSet<ColumnName> clusterKey, boolean isExternal) {
        this(TableType.DATABASE, tableName, clusterName, columns, partitionKey, clusterKey, isExternal);
    }

    /**
     * Get the partition key.
     *
     * @return The set with {@link com.stratio.crossdata.common.data.ColumnName} with the partition key.
     */
    public Set<ColumnName> getPartitionKey() {
        return partitionKey;
    }

    /**
     * Get the cluster key.
     *
     * @return The set with {@link com.stratio.crossdata.common.data.ColumnName} with the cluster key.
     */
    public Set<ColumnName> getClusterKey() {
        return clusterKey;
    }

    /**
     * Get the columns and its types.
     *
     * @return A map of {@link com.stratio.crossdata.common.data.ColumnName} and
     * {@link com.stratio.crossdata.common.metadata.ColumnType} .
     */
    public Map<ColumnName, ColumnType> getColumnsWithTypes() {
        return columnsWithType;
    }

    /**
     * Gte the cluster name of a table.
     *
     * @return The {@link com.stratio.crossdata.common.data.ClusterName} .
     */
    public ClusterName getClusterName() {
        return clusterName;
    }

    /**
     * Set the catalog specified in the create table statement.
     *
     * @param catalog The name of the catalog.
     */
    public void setCatalog(CatalogName catalog) {
        this.catalog = catalog;
    }

    /**
     * Set the list of {@link com.stratio.crossdata.core.structures.Property}.
     *
     * @param properties The list.
     */
    public void setProperties(String properties) {
        this.properties = StringUtils.convertJsonToOptions(tableStatement.getTableName(), properties);
    }

    /**
     * Set if the table will be created if exists previously.
     *
     * @param ifNotExists The condition of creation.
     */
    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    /**
     * The list of properties of the table.
     */
    public Map<Selector, Selector> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( (isExternal) ? "REGISTER " :"CREATE ");
        if (tableType != TableType.DATABASE) {
            sb.append(tableType);
        }
        sb.append("TABLE ");
        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }
        sb.append(tableStatement.getTableName().getQualifiedName());
        sb.append(" ON CLUSTER ").append(clusterName);

        sb.append("(");
        sb.append(columnsWithType.toString().replace("{", "").replace("}", ""));
        sb.append(", PRIMARY KEY((").append(partitionKey.toString().replace("[", "").replace("]", "")).append(")");
        if (!clusterKey.isEmpty()) {
            sb.append(", ").append(clusterKey.toString().replace("[", "").replace("]", ""));
        }
        sb.append("))");

        if (hasProperties()) {
            sb.append(" WITH ").append(properties);
        }
        return sb.toString();
    }

    /**
     * Return if the table has properties.
     *
     * @return The result check.
     */
    private boolean hasProperties() {
        return ((properties != null) && (!properties.isEmpty()));
    }

    /**
     * Get the conditions of validations that are needed to create the table.
     *
     * @return The {@link com.stratio.crossdata.core.validator.requirements.ValidationRequirements} .
     */
    public ValidationRequirements getValidationRequirements() {
        ValidationRequirements requirements = new ValidationRequirements()
                .add(ValidationTypes.MUST_EXIST_CATALOG)
                .add(ValidationTypes.MUST_EXIST_CLUSTER);
        if (!isIfNotExists()) {
            requirements = requirements.add(ValidationTypes.MUST_NOT_EXIST_TABLE);
        }
        return requirements;
    }

    /**
     * Get isIfNotExists value.
     *
     * @return A boolean.
     */
    public boolean isIfNotExists() {
        return ifNotExists;
    }

    /**
     * Get isExternal value.
     *
     * @return true if the table has been created previously.
     */
    public boolean isExternal() {
        return isExternal;
    }
}
