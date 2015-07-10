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

package com.stratio.crossdata.common.metadata;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.Selector;

import java.io.Serializable;
import java.util.Map;

/**
 * Index Metadata class.
 */
public class GlobalIndexMetadata extends IndexMetadata {

    private static final long serialVersionUID = -3324957898671791688L;

    private ClusterName clusterRef;

    private TableMetadata tableMetadata;

    /**
     * Constructor class.
     * @param name The name of the index.
     * @param columns The column to be indexed.
     * @param type The type of the index.
     * @param options The options of the creation of the index.
     */
    public GlobalIndexMetadata(IndexName name, Map<ColumnName, ColumnMetadata> columns, IndexType type,
                               Map<Selector, Selector> options, ClusterName clusterRef, TableMetadata tableMetadata) {
        super(name, columns, type, options);
        this.clusterRef = clusterRef;
        this.tableMetadata = tableMetadata;
    }

    public void setClusterRef(ClusterName clusterRef) {
        this.clusterRef = clusterRef;
    }

    public ClusterName getClusterRef() {
        return clusterRef;
    }

    public TableName getTableName(){
        return new TableName(getName().getTableName().getCatalogName().getName(), getName().getName());
    }

    public void setTableMetadata(TableMetadata tableMetadata) {
        this.tableMetadata = tableMetadata;
    }

    public TableMetadata getTableMetadata() {
        return tableMetadata;
    }
}
