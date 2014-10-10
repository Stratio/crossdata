/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.core.metadata;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta2.common.api.PropertyType;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.IMetadata;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.grid.Grid;
import com.stratio.meta2.core.grid.GridInitializer;

public class MetadataManagerTestHelper {

    Map<FirstLevelName, Serializable> metadataMap = new HashMap<>();
    private String path = "";

    private void initializeGrid() {
        GridInitializer gridInitializer = Grid.initializer();
        gridInitializer = gridInitializer.withContactPoint("127.0.0.1");
        path = "/tmp/metadata-store-" + UUID.randomUUID();
        gridInitializer.withPort(7800)
                .withListenAddress("127.0.0.1")
                .withMinInitialMembers(1)
                .withJoinTimeoutInMs(3000)
                .withPersistencePath(path).init();
    }

    protected DataStoreMetadata insertDataStore(String dataStore, String cluster) {
        DataStoreName dataStoreName = new DataStoreName(dataStore);
        String version = "0.1.0";

        Set<PropertyType> requiredPropertiesForDataStore = new HashSet<>();
        Set<PropertyType> othersProperties = new HashSet<>();

        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(dataStoreName, version,
                requiredPropertiesForDataStore, othersProperties);
        MetadataManager.MANAGER.createDataStore(dataStoreMetadata, false);
        return dataStoreMetadata;
    }

    @BeforeClass
    public void setUp() {
        initializeGrid();
        Map<FirstLevelName, IMetadata> metadataMap = Grid.getInstance().map("meta-test");
        Lock lock = Grid.getInstance().lock("meta-test");
        TransactionManager tm = Grid.getInstance().transactionManager("meta-test");
        MetadataManager.MANAGER.init(metadataMap, lock, tm);
    }

    @AfterClass
    public void tearDown() throws Exception {
        metadataMap.clear();
        Grid.getInstance().close();
        FileUtils.deleteDirectory(new File(path));
    }

    /**
     * Create a test dataStore named "dataStoreTest".
     *
     * @return A {@link com.stratio.meta2.common.data.DataStoreName}.
     */
    public DataStoreName createTestDatastore() {
        // Create & add DataStore
        final String DATASTORE_NAME = "dataStoreTest";
        DataStoreName dataStoreName = new DataStoreName(DATASTORE_NAME);
        insertDataStore(DATASTORE_NAME, "production");
        return dataStoreName;
    }

    /**
     * Create a test connector.
     *
     * @param name          The connector name.
     * @param dataStoreName The datastore associated with this connector.
     * @return A {@link com.stratio.meta2.common.data.ConnectorName}.
     */
    public ConnectorName createTestConnector(String name, DataStoreName dataStoreName,
            String actorRef) {
        final String version = "0.1.0";
        ConnectorName connectorName = new ConnectorName(name);
        Set<DataStoreName> dataStoreRefs = Collections.singleton(dataStoreName);
        ConnectorMetadata connectorMetadata = new ConnectorMetadata(connectorName, version, new ArrayList<String>(),
                new ArrayList<PropertyType>(), new ArrayList<PropertyType>(), new ArrayList<String>());
        connectorMetadata.setActorRef(actorRef);
        MetadataManager.MANAGER.createConnector(connectorMetadata);
        return connectorName;
    }

    /**
     * Create a test connector.
     *
     * @param name          The connector name.
     * @param dataStoreName The datastore associated with this connector.
     * @return A {@link com.stratio.meta2.common.data.ConnectorName}.
     */
    public ConnectorName createTestConnector(String name, DataStoreName dataStoreName, Set<ClusterName> clusterList,
            String actorRef) {
        final String version = "0.1.0";
        ConnectorName connectorName = new ConnectorName(name);
        Set<DataStoreName> dataStoreRefs = Collections.singleton(dataStoreName);
        Map<ClusterName, Map<Selector, Selector>> clusterProperties = new HashMap<>();
        ConnectorMetadata connectorMetadata = new ConnectorMetadata(connectorName, version, dataStoreRefs,
                clusterList, clusterProperties,
                new HashSet<PropertyType>(), new HashSet<PropertyType>(), new HashSet<Operations>());
        connectorMetadata.setActorRef(actorRef);
        MetadataManager.MANAGER.createConnector(connectorMetadata);
        return connectorName;
    }

    /**
     * Create a test connector.
     *
     * @param name          The connector name.
     * @param dataStoreName The datastore associated with this connector.
     * @return A {@link com.stratio.meta2.common.data.ConnectorName}.
     */
    public ConnectorName createTestConnector(String name, DataStoreName dataStoreName, Set<ClusterName> clusterList,
            Set<Operations> options,
            String actorRef) {
        final String version = "0.1.0";
        ConnectorName connectorName = new ConnectorName(name);
        Set<DataStoreName> dataStoreRefs = Collections.singleton(dataStoreName);
        ConnectorMetadata connectorMetadata = new ConnectorMetadata(connectorName, version, dataStoreRefs,
                new HashSet<PropertyType>(), new HashSet<PropertyType>(), options);
        connectorMetadata.setActorRef(actorRef);
        MetadataManager.MANAGER.createConnector(connectorMetadata);
        return connectorName;
    }

    /**
     * Create a test cluster.
     *
     * @param name          The name of the cluster.
     * @param dataStoreName The backend datastore.
     */
    public ClusterName createTestCluster(String name, DataStoreName dataStoreName) {
        // Create & add Cluster
        ClusterName clusterName = new ClusterName(name);
        Map<Selector, Selector> options = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, dataStoreName, options,
                connectorAttachedRefs);
        MetadataManager.MANAGER.createCluster(clusterMetadata, true, true);
        return clusterName;
    }

    public CatalogName createTestCatalog(String name) {
        // Create & add Catalog
        CatalogName catalogName = new CatalogName(name);
        Map<TableName, TableMetadata> catalogTables = new HashMap<>();
        Map<Selector, Selector> options = new HashMap<>();
        CatalogMetadata catalogMetadata = new CatalogMetadata(catalogName, options, catalogTables);
        MetadataManager.MANAGER.createCatalog(catalogMetadata);
        return catalogName;
    }

    public TableMetadata defineTable(
            ClusterName clusterName,
            String catalogName,
            String tableName, String[] columnNames,
            ColumnType[] columnTypes, String[] partitionKeys, String[] clusteringKeys) {

        TableName table = new TableName(catalogName, tableName);
        Map<Selector, Selector> options = new HashMap<>();

        //Create columns
        Map<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        int columnIndex = 0;
        for (columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            ColumnName columnName = new ColumnName(table, columnNames[columnIndex]);
            ColumnType columnType = columnTypes[columnIndex];
            ColumnMetadata columnMetadata = new ColumnMetadata(columnName, null, columnType);
            columns.put(columnName, columnMetadata);
        }

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();

        List<ColumnName> partitionKey = new ArrayList<>();
        for (String pk : partitionKeys) {
            partitionKey.add(new ColumnName(table, pk));
        }

        List<ColumnName> clusterKey = new ArrayList<>();
        for (String ck : clusteringKeys) {
            partitionKey.add(new ColumnName(table, ck));
        }

        TableMetadata tableMetadata = new TableMetadata(table, options, columns, indexes, clusterName,
                partitionKey, clusterKey);
        return tableMetadata;
    }

    public TableMetadata createTestTable(
            ClusterName clusterName,
            String catalogName,
            String tableName, String[] columnNames,
            ColumnType[] columnTypes, String[] partitionKeys, String[] clusteringKeys) {
        TableMetadata tableMetadata = defineTable(clusterName, catalogName, tableName, columnNames, columnTypes,
                partitionKeys, clusteringKeys);
        MetadataManager.MANAGER.createTable(tableMetadata);
        return tableMetadata;
    }

}
