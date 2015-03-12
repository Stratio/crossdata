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

import com.codahale.metrics.Metric;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectionStatus;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;

/**
 * Connector app interface to provided extended connector information.
 */
public interface IConnectorApp {

    /**
     * Get the table metadata.
     * @param cluster target cluster.
     * @param tableName target tablename.
     * @return A {@link com.stratio.crossdata.common.metadata.TableMetadata}.
     */
    TableMetadata getTableMetadata(ClusterName cluster, TableName tableName);

    /**
     * Get the catalog metadata.
     * @param catalogName target catalog.
     * @return A {@link com.stratio.crossdata.common.metadata.CatalogMetadata}.
     */
    CatalogMetadata getCatalogMetadata(CatalogName catalogName);

    /**
     * Get the list of existing catalogs in a cluster.
     * @param cluster target cluster.
     * @return A list of {@link com.stratio.crossdata.common.metadata.CatalogMetadata}.
     */
    List<CatalogMetadata> getCatalogs(ClusterName cluster);

    /**
     * Get the connection status with the Crossdata server.
     * @return A {@link com.stratio.crossdata.common.data.ConnectionStatus}.
     */
    ConnectionStatus getConnectionStatus();

    void subscribeToMetadataUpdate(IMetadataListener metadataListener);

    Metric registerMetric(String name, Metric metric);
}
