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

import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;

/**
 * Metadata Statement Class.
 */
public abstract class MetadataStatement extends CrossdataStatement {

    protected ClusterMetadata clusterMetadata = null;

    protected ConnectorMetadata connectorMetadata = null;

    protected DataStoreMetadata dataStoreMetadata = null;

    protected TableMetadata tableMetadata = null;

    protected ColumnMetadata columnMetadata = null;

    /**
     * Class constructor.
     */
    public MetadataStatement() {
        super();
    }

    /**
     * Get the cluster metadata of a statement.
     *
     * @return {@link com.stratio.crossdata.common.metadata.ClusterMetadata}
     */
    public ClusterMetadata getClusterMetadata() {
        return clusterMetadata;
    }

    /**
     * Set the cluster metadata of the statement.
     *
     * @param clusterMetadata The cluster metadata
     */
    public void setClusterMetadata(ClusterMetadata clusterMetadata) {
        this.clusterMetadata = clusterMetadata;
    }

    /**
     * Get de connector metadata of the statement.
     *
     * @return {@link com.stratio.crossdata.common.metadata.ConnectorMetadata}
     */
    public ConnectorMetadata getConnectorMetadata() {
        return connectorMetadata;
    }

    /**
     * Set the connector metadata of the statement.
     *
     * @param connectorMetadata The connector metadata.
     */
    public void setConnectorMetadata(ConnectorMetadata connectorMetadata) {
        this.connectorMetadata = connectorMetadata;
    }

    /**
     * Get de data store metadata of the statement.
     *
     * @return {@link com.stratio.crossdata.common.metadata.DataStoreMetadata}
     */
    public DataStoreMetadata getDataStoreMetadata() {
        return dataStoreMetadata;
    }

    /**
     * Set the data store metadata of the statement.
     *
     * @param dataStoreMetadata The data store metadata-
     */
    public void setDataStoreMetadata(DataStoreMetadata dataStoreMetadata) {
        this.dataStoreMetadata = dataStoreMetadata;
    }

    /**
     * Get the table metadata of the statement.
     *
     * @return {@link com.stratio.crossdata.common.metadata.TableMetadata}
     */
    public TableMetadata getTableMetadata() {
        return tableMetadata;
    }

    /**
     * Set the Table metadata of the statement.
     *
     * @param tableMetadata The table metadata.
     */
    public void setTableMetadata(TableMetadata tableMetadata) {
        this.tableMetadata = tableMetadata;
    }

    /**
     * Get the column metadata of the statement.
     *
     * @return {@link com.stratio.crossdata.common.metadata.ColumnMetadata}
     */
    public ColumnMetadata getColumnMetadata() {
        return columnMetadata;
    }

    /**
     * Set de Column metadata of the statement.
     *
     * @param columnMetadata The column metadata.
     */
    public void setColumnMetadata(ColumnMetadata columnMetadata) {
        this.columnMetadata = columnMetadata;
    }

}
