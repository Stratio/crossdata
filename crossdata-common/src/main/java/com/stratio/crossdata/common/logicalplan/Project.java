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

package com.stratio.crossdata.common.logicalplan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;

/**
 * Project operation to retrieve a list of columns from the datastore.
 */
public class Project extends TransformationStep {

    private static final long serialVersionUID = 413155415353651161L;
    /**
     * CLUSTER name.
     */
    private final ClusterName clusterName;

    /**
     * TABLE name.
     */
    private final TableName tableName;

    /**
     * List of columns.
     */
    private final List<ColumnName> columnList = new ArrayList<>();

    /**
     * Create a projection.
     *
     * @param operation   The operation to be applied.
     * @param tableName   The table name.
     * @param clusterName The cluster name.
     */
    public Project(Operations operation, TableName tableName, ClusterName clusterName) {
        super(operation);
        this.tableName = tableName;
        this.clusterName = clusterName;
    }

    /**
     * Create a projection.
     *
     * @param operation   The operation to be applied.
     * @param tableName   TABLE name.
     * @param clusterName The cluster name.
     * @param columnList  List of columns.
     */
    public Project(Operations operation, TableName tableName, ClusterName clusterName, List<ColumnName> columnList) {
        super(operation);
        this.tableName = tableName;
        this.clusterName = clusterName;
        this.columnList.addAll(columnList);
    }

    /**
     * Add a column to the list of columns to be projected.
     *
     * @param column The column.
     */
    public void addColumn(ColumnName column) {
        if(!columnList.contains(column)) {
            this.columnList.add(column);
        }
    }

    /**
     * Get the name of the target catalog.
     *
     * @return The name.
     */
    public String getCatalogName() {
        return this.tableName.getCatalogName().getName();
    }

    /**
     * Get the name of the target table.
     *
     * @return The name.
     */
    public TableName getTableName() {
        return tableName;
    }

    /**
     * Get the cluster name.
     *
     * @return The name.
     */
    public ClusterName getClusterName() {
        return clusterName;
    }

    /**
     * Get the list of columns to be retrieved.
     *
     * @return A list of {@link com.stratio.crossdata.common.data.ColumnName}.
     */
    public List<ColumnName> getColumnList() {
        return columnList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PROJECT ");
        sb.append(tableName.getQualifiedName());
        sb.append(" ON ").append(clusterName);
        sb.append(" (");
        Iterator<ColumnName> it = columnList.iterator();
        while (it.hasNext()) {
            sb.append(it.next().getQualifiedName());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
