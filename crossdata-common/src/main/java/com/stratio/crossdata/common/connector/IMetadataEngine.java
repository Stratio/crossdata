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

package com.stratio.crossdata.common.connector;

import java.util.List;
import java.util.Map;

import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Interface provided by a connector to access metadata related operations such as creating new
 * catalogs or tables.
 */
public interface IMetadataEngine {

    /**
     * Create a catalog in the underlying datastore.
     *
     * @param targetCluster   Target cluster.
     * @param catalogMetadata CATALOG metadata.
     * @throws ConnectorException Use UnsupportedException If the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution fails.
     */
    void createCatalog(ClusterName targetCluster, CatalogMetadata catalogMetadata)
            throws ConnectorException;

    /**
     * Alter the definition of a catalog in a cluster.
     *
     * @param targetCluster The targer cluster.
     * @param catalogName   The {@link com.stratio.crossdata.common.data.CatalogName}.
     * @param options       A map of options as {@link com.stratio.crossdata.common.statements.structures.Selector}.
     * @throws ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                            connector, or ExecutionException if the execution fails.
     */
    void alterCatalog(ClusterName targetCluster, CatalogName catalogName, Map<Selector, Selector> options)
            throws ConnectorException;

    /**
     * Create a table in the underlying datastore.
     *
     * @param targetCluster Target cluster.
     * @param tableMetadata TABLE metadata.
     * @throws ConnectorException Use UnsupportedException If the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution fails.
     */
    void createTable(ClusterName targetCluster, TableMetadata tableMetadata)
            throws ConnectorException;

    /**
     * Drop an existing catalog.
     *
     * @param targetCluster Target cluster.
     * @param name          The name of the catalog.
     * @throws ConnectorException Use UnsupportedException If the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution fails.
     */
    void dropCatalog(ClusterName targetCluster, CatalogName name) throws ConnectorException;

    /**
     * Drop an existing table.
     *
     * @param targetCluster Target cluster.
     * @param name          The name of the table.
     * @throws ConnectorException Use UnsupportedException If the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution fails.
     */
    void dropTable(ClusterName targetCluster, TableName name) throws ConnectorException;

    /**
     * Alter an existing table.
     *
     * @param targetCluster Target cluster.
     * @param name          The table metadata.
     * @param alterOptions  The options for an alter table.
     * @throws ConnectorException Use UnsupportedException If the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution fails.
     */
    void alterTable(ClusterName targetCluster, TableName name, AlterOptions alterOptions) throws
            ConnectorException;

    /**
     * Create an INDEX in the underlying datastore.
     *
     * @param targetCluster Target cluster.
     * @param indexMetadata The index.
     * @throws ConnectorException Use UnsupportedException If the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution fails.
     */
    void createIndex(ClusterName targetCluster, IndexMetadata indexMetadata)
            throws ConnectorException;

    /**
     * Drop an existing index.
     *
     * @param targetCluster Target cluster.
     * @param indexMetadata The name of the table.
     * @throws ConnectorException Use UnsupportedException If the required set of operations are not
     *                            supported by the connector or ExecutionException if the execution fails.
     */
    void dropIndex(ClusterName targetCluster, IndexMetadata indexMetadata)
            throws ConnectorException;

    /**
     * Provide a list of catalog metadata that the connector is able to extract from the underlying datastore.
     *
     * @param clusterName The target cluster name.
     * @return A list of {@link com.stratio.crossdata.common.metadata.CatalogMetadata}.
     * @throws ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                            connector, or ExecutionException if the execution fails.
     */
    List<CatalogMetadata> provideMetadata(ClusterName clusterName) throws ConnectorException;

    /**
     * Provide the metadata information associated with a catalog that the connector is able to extract from the
     * underlying datastore.
     *
     * @param clusterName The target cluster.
     * @param catalogName The target catalog.
     * @return A {@link com.stratio.crossdata.common.metadata.CatalogMetadata}.
     * @throws ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                            connector, or ExecutionException if the execution fails.
     */
    CatalogMetadata provideCatalogMetadata(ClusterName clusterName, CatalogName catalogName) throws ConnectorException;

    /**
     * Provide the metadata information associated with a table that the connector is able to extract from the
     * underlying datastore.
     *
     * @param clusterName The target cluster.
     * @param tableName   The target catalog.
     * @return A {@link com.stratio.crossdata.common.metadata.TableMetadata}.
     * @throws ConnectorException A UnsupportedException is expected if the operation is not supported by a
     *                            connector, or ExecutionException if the execution fails.
     */
    TableMetadata provideTableMetadata(ClusterName clusterName, TableName tableName) throws ConnectorException;

}
