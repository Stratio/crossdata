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

package com.stratio.meta.common.connector;

import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;

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
     * @throws UnsupportedException If the operation is not supported.
     * @throws ExecutionException   If the execution fails.
     */
    public void createCatalog(ClusterName targetCluster, CatalogMetadata catalogMetadata)
            throws UnsupportedException,
            ExecutionException;

    /**
     * Create a table in the underlying datastore.
     *
     * @param targetCluster Target cluster.
     * @param tableMetadata TABLE metadata.
     * @throws UnsupportedException If the operation is not supported.
     * @throws ExecutionException   If the execution fails.
     */
    public void createTable(ClusterName targetCluster, TableMetadata tableMetadata)
            throws UnsupportedException,
            ExecutionException;

    /**
     * Drop an existing catalog.
     *
     * @param targetCluster Target cluster.
     * @param name          The name of the catalog.
     * @throws UnsupportedException If the operation is not supported.
     * @throws ExecutionException   If the execution fails.
     */
    public void dropCatalog(ClusterName targetCluster, CatalogName name) throws UnsupportedException,
            ExecutionException;

    /**
     * Drop an existing table.
     *
     * @param targetCluster Target cluster.
     * @param name          The name of the table.
     * @throws UnsupportedException If the operation is not supported.
     * @throws ExecutionException   If the execution fails.
     */
    public void dropTable(ClusterName targetCluster, TableName name) throws UnsupportedException,
            ExecutionException;

    /**
     * Create an INDEX in the underlying datastore.
     *
     * @param targetCluster Target cluster.
     * @param indexMetadata The index.
     * @throws UnsupportedException If the operation is not supported.
     * @throws ExecutionException   If the execution fails.
     */
    public void createIndex(ClusterName targetCluster, IndexMetadata indexMetadata)
            throws UnsupportedException,
            ExecutionException;

    /**
     * Drop an existing index.
     *
     * @param targetCluster Target cluster.
     * @param indexMetadata The name of the table.
     * @throws UnsupportedException If the operation is not supported.
     * @throws ExecutionException   If the execution fails.
     */
    public void dropIndex(ClusterName targetCluster, IndexMetadata indexMetadata)
            throws UnsupportedException,
            ExecutionException;

}
