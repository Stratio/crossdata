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

package com.stratio.crossdata.core.metadata;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.FirstLevelName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterAttachedMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.IMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.core.execution.ExecutionManager;
import com.stratio.crossdata.core.grid.Grid;
import com.stratio.crossdata.core.grid.GridInitializer;

public class MetadataManagerTestHelper {

    Map<FirstLevelName, Serializable> metadataMap = new HashMap<>();
    Map<FirstLevelName, Serializable> executionMap = new HashMap<>();
    private String path = "";

    @BeforeClass
    public void setUp() throws ManifestException {
        initializeGrid();
        //MetadataManager
        Map<FirstLevelName, IMetadata> metadataMap = Grid.INSTANCE.map("crossdata-test");
        Lock lock = Grid.INSTANCE.lock("crossdata-test");
        TransactionManager tm = Grid.INSTANCE.transactionManager("crossdata-test");
        MetadataManager.MANAGER.init(metadataMap, lock, tm);
        //ExecutionManager
        Map<String, Serializable> executionMap = Grid.INSTANCE.map("crossdata.executionmanager.test");
        Lock executionLock = Grid.INSTANCE.lock("crossdata.executionmanager.test");
        TransactionManager executionTM = Grid.INSTANCE.transactionManager("crossdata.executionmanager.test");
        ExecutionManager.MANAGER.init(executionMap, executionLock, executionTM);
    }

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
        String version = "0.1.1";

        Set<PropertyType> requiredPropertiesForDataStore = new HashSet<>();
        Set<PropertyType> othersProperties = new HashSet<>();
        Set<String> behaviors = new HashSet<>();

        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(dataStoreName, version,
                requiredPropertiesForDataStore, othersProperties, behaviors);

        Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs = new HashMap<>();
        clusterAttachedRefs.put(new ClusterName(cluster), new ClusterAttachedMetadata(new ClusterName(cluster),
                new DataStoreName(dataStore), new HashMap<Selector, Selector>()));
        dataStoreMetadata.setClusterAttachedRefs(clusterAttachedRefs);

        MetadataManager.MANAGER.createDataStore(dataStoreMetadata, false);
        return dataStoreMetadata;
    }

    /**
     * Create a test dataStore named "dataStoreTest".
     *
     * @return A {@link com.stratio.crossdata.common.data.DataStoreName}.
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
     * @param dataStoreName The dataStore associated with this connector.
     * @return A {@link com.stratio.crossdata.common.data.ConnectorName}.
     */
    public ConnectorName createTestConnector(String name, DataStoreName dataStoreName,
            String actorRef) {
        final String version = "0.1.1";
        ConnectorName connectorName = new ConnectorName(name);
        ArrayList<String> dataStoreRefs = new ArrayList<>();
        dataStoreRefs.add(dataStoreName.getName());
        ConnectorMetadata connectorMetadata = null;
        try {
            connectorMetadata = new ConnectorMetadata(connectorName, version,
                    dataStoreRefs, new ArrayList<PropertyType>(), new ArrayList<PropertyType>(), new ArrayList<String>());
        } catch (ManifestException e) {
            fail(e.getMessage());
        }
        connectorMetadata.setActorRef(actorRef);
        MetadataManager.MANAGER.createConnector(connectorMetadata);
        return connectorName;
    }

    /**
     * Create a test connector.
     *
     * @param name          The connector name.
     * @param dataStoreName The dataStore associated with this connector.
     * @return A {@link com.stratio.crossdata.common.data.ConnectorName}.
     */
    public ConnectorName createTestConnector(String name, DataStoreName dataStoreName, Set<ClusterName> clusterList,
            String actorRef) throws ManifestException {
        final String version = "0.1.1";
        ConnectorName connectorName = new ConnectorName(name);
        Set<DataStoreName> dataStoreRefs = Collections.singleton(dataStoreName);
        Map<ClusterName, Map<Selector, Selector>> clusterProperties = new HashMap<>();
        ConnectorMetadata connectorMetadata = new ConnectorMetadata(connectorName, version, dataStoreRefs,
                clusterProperties, new HashSet<PropertyType>(), new HashSet<PropertyType>(), new HashSet<Operations>());
        connectorMetadata.setClusterRefs(clusterList);
        connectorMetadata.setActorRef(actorRef);
        MetadataManager.MANAGER.createConnector(connectorMetadata);
        return connectorName;
    }

    /**
     * Create a test connector.
     *
     * @param name          The connector name.
     * @param dataStoreName The dataStore associated with this connector.
     * @return A {@link com.stratio.crossdata.common.data.ConnectorName}.
     */
    public ConnectorMetadata createTestConnector(String name, DataStoreName dataStoreName, Set<ClusterName> clusterList,
            Set<Operations> options,
            String actorRef) throws ManifestException {
        final String version = "0.1.1";
        ConnectorName connectorName = new ConnectorName(name);
        Set<DataStoreName> dataStoreRefs = Collections.singleton(dataStoreName);
        Map<ClusterName, Map<Selector, Selector>> clusterProperties = new HashMap<>();
        ConnectorMetadata connectorMetadata = new ConnectorMetadata(connectorName, version, dataStoreRefs,
                clusterProperties, new HashSet<PropertyType>(), new HashSet<PropertyType>(), options);
        connectorMetadata.setClusterRefs(clusterList);
        connectorMetadata.setActorRef(actorRef);
        MetadataManager.MANAGER.createConnector(connectorMetadata);
        return connectorMetadata;

    }

    /**
     * Create a test cluster.
     *
     * @param name          The name of the cluster.
     * @param dataStoreName The backend dataStore.
     */
    public ClusterName createTestCluster(String name, DataStoreName dataStoreName) throws ManifestException {
        // Create & add Cluster
        ClusterName clusterName = new ClusterName(name);
        Map<Selector, Selector> options = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, dataStoreName, options,
                connectorAttachedRefs);
        MetadataManager.MANAGER.createClusterAndAttach(clusterMetadata, false);
        return clusterName;
    }

    /**
     * Create a test cluster.
     *
     * @param name          The name of the cluster.
     * @param dataStoreName The backend dataStore.
     */
    public ClusterName createTestCluster(String name, DataStoreName dataStoreName, ConnectorName ... connectorNames)
            throws ManifestException {
        // Create & add Cluster
        ClusterName clusterName = new ClusterName(name);
        Map<Selector, Selector> options = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        for(ConnectorName connectorName : connectorNames) {
            connectorAttachedRefs.put(connectorName,
                    new ConnectorAttachedMetadata(connectorName, clusterName, new HashMap<Selector, Selector>()));
        }
        ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, dataStoreName, options,
                connectorAttachedRefs);
        MetadataManager.MANAGER.createClusterAndAttach(clusterMetadata, false);
        return clusterName;
    }

    public CatalogName createTestCatalog(String name) {
        // Create & add Catalog
        CatalogName catalogName = new CatalogName(name);
        Map<TableName, TableMetadata> catalogTables = new HashMap<>();
        Map<Selector, Selector> options = new HashMap<>();
        CatalogMetadata catalogMetadata = new CatalogMetadata(catalogName, options, catalogTables);
        MetadataManager.MANAGER.createCatalog(catalogMetadata, false);
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
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            ColumnName columnName = new ColumnName(table, columnNames[columnIndex]);
            ColumnType columnType = columnTypes[columnIndex];
            ColumnMetadata columnMetadata = new ColumnMetadata(columnName, null, columnType);
            columns.put(columnName, columnMetadata);
        }

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();

        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        for (String pk : partitionKeys) {
            partitionKey.add(new ColumnName(table, pk));
        }

        LinkedList<ColumnName> clusterKey = new LinkedList<>();
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

    @AfterClass
    public void tearDown() throws Exception {
        metadataMap.clear();
        executionMap.clear();
        Grid.INSTANCE.close();
        FileUtils.deleteDirectory(new File(path));
    }

}
