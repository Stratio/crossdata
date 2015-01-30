/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.crossdata.core.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.Status;
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
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.core.MetadataManagerTestHelper;

public class MetadataManagerTest {

    @BeforeClass
    public void init() {
        MetadataManagerTestHelper.HELPER.initHelper();
    }

    @Test
    public void testAttachCluster() throws Exception {

        DataStoreName dataStoreName = MetadataManagerTestHelper.HELPER.createTestDatastore();
        ClusterName clusterName = MetadataManagerTestHelper.HELPER.createTestCluster("clusterTest", dataStoreName);
        DataStoreMetadata dataStoreTest = MetadataManager.MANAGER.getDataStore(dataStoreName);

        Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefsTest =
                dataStoreTest.getClusterAttachedRefs();
        boolean found = false;
        for (ClusterName clusterNameTest : clusterAttachedRefsTest.keySet()) {
            ClusterAttachedMetadata clusterAttachedMetadata =
                    clusterAttachedRefsTest.get(clusterNameTest);
            if (clusterAttachedMetadata.getClusterRef().equals(clusterName)) {
                assertEquals(clusterAttachedMetadata.getDataStoreRef(), dataStoreName,
                        "Wrong attachment for clusterTest");
                found = true;
                break;
            }
        }
        assertTrue(found, "Attachment not found");
    }

    @Test
    public void testAttachConnector() throws Exception {

        // Create and add a test dataStore and cluster to the MetadataManager
        DataStoreName dataStoreRef = MetadataManagerTestHelper.HELPER.createTestDatastore();
        ClusterName clusterName = new ClusterName("clusterTest");
        Set<ClusterName> clusterList = new HashSet<>();
        clusterList.add(clusterName);
        // Create and add a test ConnectorManager metadata to the MetadataManager
        ConnectorName connectorName = MetadataManagerTestHelper.HELPER.createTestConnector("testConnector",
                dataStoreRef, clusterList,
                "coordinatorActorRef");
        MetadataManagerTestHelper.HELPER.createTestCluster(clusterName.getName(), dataStoreRef, connectorName);

        // Check persistence of ConnectorManager
        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
        assertEquals(connectorName, connectorMetadata.getName(), "Expected name: " + connectorName +
                System.lineSeparator() + "Result name:   " + connectorMetadata.getName());

        // Check that changes persisted in the MetadataManager ("clusterTest" cluster)
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);
        assertEquals(clusterMetadata.getConnectorAttachedRefs().get(connectorName).getConnectorRef(), connectorName,
                "Expected: " + connectorName + System.lineSeparator() +
                "Found:    " + clusterMetadata.getConnectorAttachedRefs().get(connectorName).getConnectorRef());

    }

    // CREATE CATALOG
    @Test
    public void testCreateCatalog() throws Exception {

        // Create and add a test dataStore and cluster to the MetadataManager
        DataStoreName dataStoreRef = MetadataManagerTestHelper.HELPER.createTestDatastore();
        ClusterName clusterName = new ClusterName("clusterTest");
        MetadataManagerTestHelper.HELPER.createTestCluster(clusterName.getName(), dataStoreRef);
        // Create catalog
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("testCatalog").getName();

        //Check catalog persistence
        assertEquals(MetadataManager.MANAGER.getCatalog(catalogName).getName(), catalogName,
                "Expected: " + catalogName + System.lineSeparator() +
                "Found:    " + MetadataManager.MANAGER.getCatalog(catalogName).getName());
    }

    // CREATE TABLE
    @Test
    public void testCreateTable() throws Exception {
        // Create and add a test dataStore and cluster to the MetadataManager
        DataStoreName dataStoreRef = MetadataManagerTestHelper.HELPER.createTestDatastore();
        ClusterName clusterName = new ClusterName("clusterTest");
        MetadataManagerTestHelper.HELPER.createTestCluster(clusterName.getName(), dataStoreRef);
        // Create catalog
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("testCatalog").getName();

        // Create and add test table to the metadatamanager
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes1 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        TableMetadata table = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(),
                "testTable", columnNames1,
                columnTypes1, partitionKeys1, clusteringKeys1, null);

        assertEquals(MetadataManager.MANAGER.getTable(table.getName()).getName(), table.getName(),
                "Expected: " + table.getName() + System.lineSeparator() +
                "Found:    " + MetadataManager.MANAGER.getTable(table.getName()).getName());
    }

    @Test
    public void testCreateConnector() {

        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = "akkaActorRefTest";
        MetadataManagerTestHelper.HELPER.createTestConnector(name, dataStoreName, actorRef);

        ConnectorName connectorName = new ConnectorName(name);
        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);

        assertEquals(connectorMetadata.getName(), connectorName,
                "Expected: " + connectorName + System.lineSeparator() +
                "Found:    " + connectorMetadata.getName());
        assertTrue(connectorMetadata.getActorRef().equalsIgnoreCase(actorRef),
                "Expected: " + actorRef + System.lineSeparator() +
                        "Found:    " + connectorMetadata.getActorRef());
        DataStoreName found = connectorMetadata.getDataStoreRefs().iterator().next();
        assertEquals(found, dataStoreName,
                "Expected: " + dataStoreName + System.lineSeparator() +
                "Found:    " + found);
    }

    @Test
    public void testAddExistingConnectorRef() {
        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = null;
        MetadataManagerTestHelper.HELPER.createTestConnector(name, dataStoreName, actorRef);

        ConnectorName connectorName = new ConnectorName("connectorName");
        actorRef = "akkaActorRefTest";
        try {
            MetadataManager.MANAGER.addConnectorRef(connectorName, actorRef);
        } catch (ManifestException e) {
            fail();
        }

        assertTrue(MetadataManager.MANAGER.getConnector(connectorName).getActorRef().equalsIgnoreCase(actorRef),
                "Expected: " + actorRef + System.lineSeparator() +
                "Found:    " + MetadataManager.MANAGER.getConnector(connectorName).getActorRef());
    }

    @Test
    public void testAddNonExistingConnectorRef() {

        ConnectorName connectorName = new ConnectorName("connectorName");
        String actorRef = "akkaActorRefTest";
        try {
            MetadataManager.MANAGER.addConnectorRef(connectorName, actorRef);
        } catch (ManifestException e) {
            fail();
        }

        assertTrue(MetadataManager.MANAGER.getConnector(connectorName).getActorRef().equalsIgnoreCase(actorRef),
                "Expected: " + actorRef + System.lineSeparator() +
                "Found:    " + MetadataManager.MANAGER.getConnector(connectorName).getActorRef());
    }

    @Test
    public void testSetConnectorStatus() {
        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = null;
        MetadataManagerTestHelper.HELPER.createTestConnector(name, dataStoreName, actorRef);

        List<ConnectorName> names = new ArrayList<>();
        ConnectorName connectorName = new ConnectorName("connectorTest");
        names.add(connectorName);
        Status status = Status.ONLINE;
        MetadataManager.MANAGER.setConnectorStatus(names, status);

        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
        assertEquals(connectorMetadata.getStatus(), status,
                "Expected: " + status + System.lineSeparator() +
                "Found:    " + connectorMetadata.getStatus());
    }

    @Test
    public void testGetConnectorRef() {

        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = "akkActorRefTest";
        MetadataManagerTestHelper.HELPER.createTestConnector(name, dataStoreName, actorRef);

        assertTrue(MetadataManager.MANAGER.getConnectorRef(new ConnectorName(name)).equalsIgnoreCase(actorRef),
                "Expected: " + actorRef + System.lineSeparator() +
                "Found:    " + MetadataManager.MANAGER.getConnectorRef(new ConnectorName(name)));
    }

    @Test
    public void testGetAttachedConnectors() {

        MetadataManagerTestHelper.HELPER.createTestDatastore();

        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String cluster = "clusterTest";
        String connector = "connectorTest";

        String actorRef = null;
        MetadataManagerTestHelper.HELPER.createTestConnector(connector, dataStoreName, actorRef);

        try {
            MetadataManagerTestHelper.HELPER.createTestCluster(cluster, dataStoreName, new ConnectorName(connector));
        } catch (ManifestException e) {
            fail();
        }

        List<ConnectorName> names = new ArrayList<>();
        ConnectorName connectorName = new ConnectorName("connectorTest");
        names.add(connectorName);
        Status status = Status.SHUTTING_DOWN;
        MetadataManager.MANAGER.setConnectorStatus(names, status);

        ClusterName clusterName = new ClusterName(cluster);
        List<ConnectorMetadata> attachedConnectors = MetadataManager.MANAGER
                .getAttachedConnectors(status, clusterName);

        assertTrue(attachedConnectors.size() == 1,
                "Number of attached connectors is wrong." + System.lineSeparator() +
                "Expected: " + 1 + System.lineSeparator() +
                "Found:    " + attachedConnectors.size());
        assertEquals(attachedConnectors.get(0).getName(), connectorName,
                "Expected: " + connectorName + System.lineSeparator() +
                "Found:    " + attachedConnectors.get(0).getName());
    }

    @Test
    public void testCheckConnectorStatus() {
        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = null;
        MetadataManagerTestHelper.HELPER.createTestConnector(name, dataStoreName, actorRef);

        List<ConnectorName> names = new ArrayList<>();
        ConnectorName connectorName = new ConnectorName("connectorTest");
        names.add(connectorName);
        Status status = Status.INITIALIZING;
        MetadataManager.MANAGER.setConnectorStatus(names, status);

        assertTrue(MetadataManager.MANAGER.checkConnectorStatus(connectorName, status),
                connectorName + " should have the status : " + status);
    }

    @Test(dependsOnMethods = { "testCreateCatalog" } )
    public void testGetCatalogs() {
        String catalog = "catalogTest";
        MetadataManagerTestHelper.HELPER.createTestCatalog(catalog);

        List<CatalogMetadata> catalogs = MetadataManager.MANAGER.getCatalogs();

        int expectedNumber = 7;

        assertTrue(catalogs.size() == expectedNumber,
                "Catalogs size is wrong." + System.lineSeparator() +
                "Expected: " + expectedNumber + System.lineSeparator() +
                "Found:    " + catalogs.size());
        /*
        assertTrue(catalogs.get(0).getName().getName().equalsIgnoreCase(catalog), System.lineSeparator() +
                "Expected: " + catalog + System.lineSeparator() +
                "Found:    " + catalogs.get(0).getName().getName());
        */
    }

    @Test
    public void testGetTables() {

        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<TableMetadata> tables = MetadataManager.MANAGER.getTables();

        int expectedSize = 6;

        assertTrue(tables.size() == expectedSize,
                "Tables size is wrong." + System.lineSeparator() +
                "Expected: " + expectedSize + System.lineSeparator() +
                "Found:    " + tables.size());
        assertTrue(tables.get(0).getName().getName().equalsIgnoreCase("testTable"),
                "Expected: " + "testTable" + System.lineSeparator() +
                "Found:    " + tables.get(0).getName().getName());
    }

    @Test
    public void testGetColumns() {
        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<ColumnMetadata> columns = MetadataManager.MANAGER.getColumns();

        int expectedSize = 16;

        assertTrue(columns.size() == expectedSize,
                "Columns size is wrong." + System.lineSeparator() +
                "Expected: " + expectedSize + System.lineSeparator() +
                "Found:    " + columns.size());
        /*
        assertTrue(columns.get(0).getName().getName().equalsIgnoreCase("id"),
                "Expected: " + "id" + System.lineSeparator() +
                "Found:    " + columns.get(0).getName().getName());
        assertTrue(columns.get(1).getName().getName().equalsIgnoreCase("user"),
                "Expected: " + "user" + System.lineSeparator() +
                "Found:    " + columns.get(1).getName().getName());
        */
    }

    @Test
    public void testGetTablesByCatalogName() {
        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<TableMetadata> tablesMetadata = MetadataManager.MANAGER.getTablesByCatalogName("testCatalog");

        assertTrue(tablesMetadata.get(0).getName().getName().equalsIgnoreCase("testTable"),
                "Expected: " + "testTable" + System.lineSeparator() +
                "Found:    " + tablesMetadata.get(0).getName().getName());
    }

    @Test
    public void testGetColumnByTable() {
        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<ColumnMetadata> columnsMetadata = MetadataManager.MANAGER.getColumnByTable("testCatalog", "testTable");

        assertTrue(columnsMetadata.size() == 2,
                "Size of the Metadata of columns is wrong." + System.lineSeparator() +
                "Expected: " + 2 + System.lineSeparator() +
                "Found:    " + columnsMetadata.size());
        assertTrue(columnsMetadata.get(0).getName().getName().equals("id"),
                "Expected: " + "id" + System.lineSeparator() +
                "Found:    " + columnsMetadata.get(0).getName().getName());
        assertTrue(columnsMetadata.get(1).getName().getName().equals("user"),
                "Expected: " + "user" + System.lineSeparator() +
                "Found:    " + columnsMetadata.get(1).getName().getName());
    }

    @Test
    public void testGetConnectors() {

        testCreateConnector();

        Status status = Status.ONLINE;
        List<ConnectorMetadata> connectors = MetadataManager.MANAGER.getConnectors(status);

        int expectedSize = 4;

        assertTrue(connectors.size() == expectedSize,
                "Connectors size is wrong." + System.lineSeparator() +
                "Expected: " + expectedSize + System.lineSeparator() +
                "Found:    " + connectors.size());
        assertTrue(connectors.get(0).getStatus() == status,
                "Expected: " + status + System.lineSeparator() +
                "Found:    " + connectors.get(0).getStatus());
    }

    @Test
    public void testGetConnectorNames() {

        testCreateConnector();

        List<ConnectorName> connectors = MetadataManager.MANAGER.getConnectorNames(Status.ONLINE);

        int expectedSize = 4;

        assertTrue(connectors.size() == expectedSize,
                "Connectors size is wrong." + System.lineSeparator() +
                "Expected: " + expectedSize + System.lineSeparator() +
                "Found:    " + connectors.size());
        /*
        assertTrue(connectors.get(0).getName().equalsIgnoreCase("connectorTest"),
                "Expected: " + "connectorTest" + System.lineSeparator() +
                "Found:    " + connectors.get(0).getName());
        */
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testShouldBeUniqueException() {
        DataStoreName name = MetadataManagerTestHelper.HELPER.createTestDatastore();
        String version = "0.2.0";
        Set<PropertyType> requiredProperties = new HashSet<>();
        Set<PropertyType> othersProperties = new HashSet<>();
        Set<String> behaviors = new HashSet<>();
        DataStoreMetadata dataStore = new DataStoreMetadata(name, version, requiredProperties, othersProperties,
                behaviors, null);
        MetadataManager.MANAGER.createDataStore(dataStore);
        fail();
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testShouldExistException() {
        MetadataManager.MANAGER.getCluster(new ClusterName("random"));
        fail();
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testCreateCatalogException() {
        MetadataManagerTestHelper.HELPER.createTestCatalog("catalogTest");
        MetadataManager.MANAGER.createCatalog(new CatalogMetadata(new CatalogName("catalogTest"),
                new HashMap<Selector, Selector>(), new HashMap<TableName, TableMetadata>()));
        fail();
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testCreateTableException() {

        ClusterName clusterName = new ClusterName("clusterTest");
        String catalogName = "catalogTest";
        String tableName = "tableTest";
        String[] columnNames = { "firstCol", "SecondCol" };
        ColumnType[] columnTypes = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys = { "firstCol" };
        String[] clusteringKeys = new String[0];
        MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName, tableName, columnNames, columnTypes,
                partitionKeys, clusteringKeys, null);

        TableName name = new TableName("catalogTest", "tableTest");
        Map<Selector, Selector> options = new HashMap<>();
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        TableMetadata tableMetadata = new TableMetadata(name, options, columns, indexes, clusterName, partitionKey,
                clusterKey);
        MetadataManager.MANAGER.createTable(tableMetadata);
        fail();
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testCreateClusterException() {
        try {
            MetadataManagerTestHelper.HELPER.createTestCluster("clusterTest", new DataStoreName("dataStoreTest"));
        } catch (ManifestException e) {
            fail();
        }

        ClusterName name = new ClusterName("clusterTest");
        DataStoreName dataStoreRef = new DataStoreName("dataStoreTest");
        Map<Selector, Selector> options = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        ClusterMetadata clusterMetadata = null;
        try {
            clusterMetadata = new ClusterMetadata(name, dataStoreRef, options, connectorAttachedRefs);
        } catch (ManifestException e) {
            fail();
        }
        MetadataManager.MANAGER.createCluster(clusterMetadata);
        fail();
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testCreateDataStoreException() {
        MetadataManagerTestHelper.HELPER.createTestDatastore();

        DataStoreName name = new DataStoreName("dataStoreTest");
        String version = "0.2.0";
        Set<PropertyType> requiredProperties = new HashSet<>();
        Set<PropertyType> othersProperties = new HashSet<>();
        Set<String> behaviors = new HashSet<>();
        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(name, version, requiredProperties,
                othersProperties, behaviors, null);

        MetadataManager.MANAGER.createDataStore(dataStoreMetadata);
        fail();
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testCreateConnectorException() {
        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = "akkaRefTest";
        MetadataManagerTestHelper.HELPER.createTestConnector(name, dataStoreName, actorRef);

        String version = "0.2.0";
        List<String> dataStoreRefs = new ArrayList<>();
        List<PropertyType> requiredProperties = new ArrayList<>();
        List<PropertyType> optionalProperties = new ArrayList<>();
        List<String> supportedOperations = new ArrayList<>();
        ConnectorMetadata connectorMetadata = null;
        try {
            connectorMetadata = new ConnectorMetadata(new ConnectorName(name), version, dataStoreRefs,
                    requiredProperties, optionalProperties, supportedOperations,null,null);
        } catch (ManifestException e) {
            fail(e.getMessage());
        }
        MetadataManager.MANAGER.createConnector(connectorMetadata);
        fail();
    }

}
