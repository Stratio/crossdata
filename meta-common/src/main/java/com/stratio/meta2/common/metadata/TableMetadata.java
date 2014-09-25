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

package com.stratio.meta2.common.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;

public class TableMetadata implements IMetadata {
    private static final long serialVersionUID = 937637791215246279L;
    private final TableName name;

    private final Map<Selector, Selector> options;

    private final Map<ColumnName, ColumnMetadata> columns;

    private final Map<IndexName, IndexMetadata> indexes;

    private final ClusterName clusterRef;

    private final List<ColumnName> partitionKey;
    private final List<ColumnName> clusterKey;

    public TableMetadata(TableName name, Map<Selector, Selector> options,
            Map<ColumnName, ColumnMetadata> columns, Map<IndexName, IndexMetadata> indexes,
            ClusterName clusterRef,
            List<ColumnName> partitionKey, List<ColumnName> clusterKey) {
        this.name = name;
        this.options = options;
        this.columns = columns;
        this.indexes = indexes;
        this.clusterRef = clusterRef;

        this.partitionKey = partitionKey;
        this.clusterKey = clusterKey;
    }

    public TableName getName() {
        return name;
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }

    public Map<ColumnName, ColumnMetadata> getColumns() {
        return columns;
    }

    public ClusterName getClusterRef() {
        return clusterRef;
    }

    public List<ColumnName> getPartitionKey() {
        return partitionKey;
    }

    public List<ColumnName> getClusterKey() {
        return clusterKey;
    }

    public List<ColumnName> getPrimaryKey() {
        List<ColumnName> result = new ArrayList<>();
        result.addAll(partitionKey);
        result.addAll(clusterKey);
        return result;
    }

    public Map<IndexName, IndexMetadata> getIndexes() {
        return indexes;
    }

    /**
     * Determine whether the selected column is part of the primary key or not.
     *
     * @param columnName The column name.
     * @return Whether is part of the primary key.
     */
    public boolean isPK(ColumnName columnName) {
        return partitionKey.contains(columnName) || clusterKey.contains(columnName);
    }

    /**
     * Determine whether the selected column has an associated index.
     *
     * @param columnName The column name.
     * @return Whether is indexed or not.
     */
    public boolean isIndexed(ColumnName columnName) {
        for (IndexMetadata indexMetadata : indexes.values()) {
            if (indexMetadata.getColumns().contains(columnName)) {
                return true;
            }
        }
        return false;
    }

    public void addIndex(IndexName name, IndexMetadata data) {
        indexes.put(name, data);
    }

}
