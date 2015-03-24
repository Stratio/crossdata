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

package com.stratio.connector.twitter;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.stratio.crossdata.common.connector.IMetadataEngine;
import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;

import twitter4j.TwitterStream;

public class TwitterMetadataEngine implements IMetadataEngine {

    private final TwitterConnector connector;

    public TwitterMetadataEngine(TwitterConnector connector) {
        this.connector = connector;
    }

    /**
     * Create a catalog in the underlying datastore.
     *
     * @param targetCluster   Target cluster.
     * @param catalogMetadata CATALOG metadata.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void createCatalog(ClusterName targetCluster, CatalogMetadata catalogMetadata)
            throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Alter the definition of a catalog in a cluster.
     *
     * @param targetCluster The target cluster.
     * @param catalogName   The {@link com.stratio.crossdata.common.data.CatalogName}.
     * @param options       A map of options as {@link com.stratio.crossdata.common.statements.structures.Selector}.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                                                                    connector, or ExecutionException if the execution fails.
     */
    @Override
    public void alterCatalog(ClusterName targetCluster, CatalogName catalogName,
            Map<Selector, Selector> options) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Create a table in the underlying datastore.
     *
     * @param targetCluster Target cluster.
     * @param tableMetadata TABLE metadata.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void createTable(ClusterName targetCluster, TableMetadata tableMetadata)
            throws ConnectorException {
        for(Map.Entry<ColumnName, ColumnMetadata> entry: tableMetadata.getColumns().entrySet()){
            ColumnName columnName = entry.getKey();
            ColumnType columnType = entry.getValue().getColumnType();
            checkColumn(columnName, columnType);
        }
        connector.addTableMetadata(targetCluster.getName(), tableMetadata);
    }

    private void checkColumn(ColumnName columnName, ColumnType actualColumnType) throws ExecutionException {
        String name = WordUtils.capitalize(columnName.getName());
        Map<String, ColumnType> twitterColumns = connector.getAllowedColumns();
        if(twitterColumns.containsKey(name)){
            ColumnType expectedColumnType = twitterColumns.get(name);
            if(actualColumnType.getDataType() != expectedColumnType.getDataType()){
                throw new ExecutionException(
                        "Field " + columnName.getName() + " is expected to be of type " + expectedColumnType.getDataType());
            }
        } else {
            throw new ExecutionException("Field " + columnName.getName() + " is not included in the tweets metadata");
        }
    }

    /**
     * Drop an existing catalog.
     *
     * @param targetCluster Target cluster.
     * @param name          The name of the catalog.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void dropCatalog(ClusterName targetCluster, CatalogName name) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Drop an existing table.
     *
     * @param targetCluster Target cluster.
     * @param name          The name of the table.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void dropTable(ClusterName targetCluster, TableName name) throws ConnectorException {
        TwitterStream session = connector.getSession(targetCluster.getName());
        session.cleanUp();
    }

    /**
     * Alter an existing table.
     *
     * @param targetCluster Target cluster.
     * @param name          The table metadata.
     * @param alterOptions  The options for an alter table.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void alterTable(ClusterName targetCluster, TableName name, AlterOptions alterOptions)
            throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Create an INDEX in the underlying datastore.
     *
     * @param targetCluster Target cluster.
     * @param indexMetadata The index.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void createIndex(ClusterName targetCluster, IndexMetadata indexMetadata)
            throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Drop an existing index.
     *
     * @param targetCluster Target cluster.
     * @param indexMetadata The name of the table.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException Use UnsupportedException If the required set of operations are not
     *                                                                    supported by the connector or ExecutionException if the execution fails.
     */
    @Override
    public void dropIndex(ClusterName targetCluster, IndexMetadata indexMetadata) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Provide a list of catalog metadata that the connector is able to extract from the underlying datastore.
     *
     * @param clusterName The target cluster name.
     * @return A list of {@link com.stratio.crossdata.common.metadata.CatalogMetadata}.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                                                                    connector, or ExecutionException if the execution fails.
     */
    @Override
    public List<CatalogMetadata> provideMetadata(ClusterName clusterName) throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Provide the metadata information associated with a catalog that the connector is able to extract from the
     * underlying datastore.
     *
     * @param clusterName The target cluster.
     * @param catalogName The target catalog.
     * @return A {@link com.stratio.crossdata.common.metadata.CatalogMetadata}.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                                                                    connector, or ExecutionException if the execution fails.
     */
    @Override
    public CatalogMetadata provideCatalogMetadata(ClusterName clusterName, CatalogName catalogName)
            throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }

    /**
     * Provide the metadata information associated with a table that the connector is able to extract from the
     * underlying datastore.
     *
     * @param clusterName The target cluster.
     * @param tableName   The target catalog.
     * @return A {@link com.stratio.crossdata.common.metadata.TableMetadata}.
     * @throws com.stratio.crossdata.common.exceptions.ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                                                                    connector, or ExecutionException if the execution fails.
     */
    @Override
    public TableMetadata provideTableMetadata(ClusterName clusterName, TableName tableName)
            throws ConnectorException {
        throw new UnsupportedException("Operation not supported");
    }
}
