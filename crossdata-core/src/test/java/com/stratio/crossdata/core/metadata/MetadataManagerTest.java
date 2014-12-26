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

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

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

public class MetadataManagerTest extends MetadataManagerTestHelper {

    @Test
    public void testAttachCluster() throws Exception {

        DataStoreName dataStoreName = createTestDatastore();
        ClusterName clusterName = createTestCluster("clusterTest", dataStoreName);
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
        DataStoreName dataStoreRef = createTestDatastore();
        ClusterName clusterName = new ClusterName("clusterTest");
        Set<ClusterName> clusterList = new HashSet<ClusterName>();
        clusterList.add(clusterName);
        // Create and add a test connectormanager metadata to the MetadataManager
        ConnectorName connectorName = createTestConnector("testConnector", dataStoreRef, clusterList,
                "coordinatorActorRef");
        createTestCluster(clusterName.getName(), dataStoreRef, connectorName);

        // Check persistence of connectormanager
        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
        assertEquals(connectorName, connectorMetadata.getName());

        // Check that changes persisted in the MetadataManager ("clusterTest" cluster)
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);
        assertEquals(clusterMetadata.getConnectorAttachedRefs().get(connectorName).getConnectorRef(), connectorName);

    }

    // CREATE CATALOG
    @Test
    public void testCreateCatalog() throws Exception {

        // Create and add a test dataStore and cluster to the MetadataManager
        DataStoreName dataStoreRef = createTestDatastore();
        ClusterName clusterName = new ClusterName("clusterTest");
        createTestCluster(clusterName.getName(), dataStoreRef);
        // Create catalog
        CatalogName catalogName = createTestCatalog("testCatalog");

        //Check catalog persistence
        assertEquals(MetadataManager.MANAGER.getCatalog(catalogName).getName(), catalogName);
    }

    // CREATE TABLE
    @Test
    public void testCreateTable() throws Exception {
        // Create and add a test dataStore and cluster to the MetadataManager
        DataStoreName dataStoreRef = createTestDatastore();
        ClusterName clusterName = new ClusterName("clusterTest");
        createTestCluster(clusterName.getName(), dataStoreRef);
        // Create catalog
        CatalogName catalogName = createTestCatalog("testCatalog");

        // Create and add test table to the metadatamanager
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes1 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        TableMetadata table = createTestTable(clusterName, catalogName.getName(), "testTable", columnNames1,
                columnTypes1,
                partitionKeys1,
                clusteringKeys1);

        assertEquals(MetadataManager.MANAGER.getTable(table.getName()).getName(), table.getName());
    }

    @Test
    public void testCreateConnector() {

        try {
            MetadataManager.MANAGER.clear();
        } catch (HeuristicRollbackException | HeuristicMixedException | NotSupportedException | RollbackException |
                SystemException e) {
            fail();
        }

        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = "akkaActorRefTest";
        createTestConnector(name, dataStoreName, actorRef);

        ConnectorName connectorName = new ConnectorName(name);
        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);

        assertEquals(connectorMetadata.getName(), connectorName);
        assertTrue(connectorMetadata.getActorRef().equalsIgnoreCase(actorRef));
        assertEquals(connectorMetadata.getDataStoreRefs().iterator().next(), dataStoreName);
    }

    @Test
    public void testAddExistingConnectorRef() {
        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = null;
        createTestConnector(name, dataStoreName, actorRef);

        ConnectorName connectorName = new ConnectorName("connectorName");
        actorRef = "akkaActorRefTest";
        try {
            MetadataManager.MANAGER.addConnectorRef(connectorName, actorRef);
        } catch (ManifestException e) {
            fail();
        }

        assertTrue(MetadataManager.MANAGER.getConnector(connectorName).getActorRef().equalsIgnoreCase(actorRef));
    }

    @Test
    public void testAddNonExistingConnectorRef() {
        try {
            MetadataManager.MANAGER.clear();
        } catch (HeuristicRollbackException | HeuristicMixedException | NotSupportedException | RollbackException |
                SystemException e) {
            fail();
        }

        ConnectorName connectorName = new ConnectorName("connectorName");
        String actorRef = "akkaActorRefTest";
        try {
            MetadataManager.MANAGER.addConnectorRef(connectorName, actorRef);
        } catch (ManifestException e) {
            fail();
        }

        assertTrue(MetadataManager.MANAGER.getConnector(connectorName).getActorRef().equalsIgnoreCase(actorRef));
    }

    @Test
    public void testSetConnectorStatus() {
        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = null;
        createTestConnector(name, dataStoreName, actorRef);

        List<ConnectorName> names = new ArrayList<>();
        ConnectorName connectorName = new ConnectorName("connectorTest");
        names.add(connectorName);
        Status status = Status.ONLINE;
        MetadataManager.MANAGER.setConnectorStatus(names, status);

        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
        assertEquals(connectorMetadata.getStatus(), status);
    }

    @Test
    public void testGetConnectorRef() {

        try {
            MetadataManager.MANAGER.clear();
        } catch (HeuristicRollbackException | HeuristicMixedException | NotSupportedException | RollbackException |
                SystemException e) {
            fail();
        }

        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = "akkActorRefTest";
        createTestConnector(name, dataStoreName, actorRef);

        assertTrue(MetadataManager.MANAGER.getConnectorRef(new ConnectorName(name)).equalsIgnoreCase(actorRef));
    }

    @Test
    public void testGetAttachedConnectors() {
        try {
            MetadataManager.MANAGER.clear();
        } catch (HeuristicRollbackException | HeuristicMixedException | NotSupportedException | RollbackException |
                SystemException e) {
            fail();
        }

        createTestDatastore();

        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String cluster = "clusterTest";
        String connector = "connectorTest";

        String actorRef = null;
        createTestConnector(connector, dataStoreName, actorRef);

        try {
            createTestCluster(cluster, dataStoreName, new ConnectorName(connector));
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

        assertTrue(attachedConnectors.size() == 1);
        assertEquals(attachedConnectors.get(0).getName(), connectorName);
    }

    @Test
    public void testCheckConnectorStatus() {
        String name = "connectorTest";
        DataStoreName dataStoreName = new DataStoreName("dataStoreTest");
        String actorRef = null;
        createTestConnector(name, dataStoreName, actorRef);

        List<ConnectorName> names = new ArrayList<>();
        ConnectorName connectorName = new ConnectorName("connectorTest");
        names.add(connectorName);
        Status status = Status.INITIALIZING;
        MetadataManager.MANAGER.setConnectorStatus(names, status);

        assertTrue(MetadataManager.MANAGER.checkConnectorStatus(connectorName, status));
    }

    @Test
    public void testGetCatalogs() {
        String catalog = "catalogTest";
        createTestCatalog(catalog);

        List<CatalogMetadata> catalogs = MetadataManager.MANAGER.getCatalogs();

        assertTrue(catalogs.size() == 1);
        assertTrue(catalogs.get(0).getName().getName().equalsIgnoreCase(catalog));
    }

    @Test
    public void testGetTables() {

        try {
            MetadataManager.MANAGER.clear();
        } catch (HeuristicRollbackException | HeuristicMixedException | NotSupportedException | RollbackException |
                SystemException e) {
            fail();
        }

        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<TableMetadata> tables = MetadataManager.MANAGER.getTables();

        assertTrue(tables.size() == 1);
        assertTrue(tables.get(0).getName().getName().equalsIgnoreCase("testTable"));
    }

    @Test
    public void testGetColumns() {
        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<ColumnMetadata> columns = MetadataManager.MANAGER.getColumns();

        assertTrue(columns.size() == 2);
        assertTrue(columns.get(0).getName().getName().equalsIgnoreCase("id"));
        assertTrue(columns.get(1).getName().getName().equalsIgnoreCase("user"));
    }

    @Test
    public void testGetTablesByCatalogName() {
        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<TableMetadata> tablesMetadata = MetadataManager.MANAGER.getTablesByCatalogName("testCatalog");

        assertTrue(tablesMetadata.get(0).getName().getName().equalsIgnoreCase("testTable"));
    }

    @Test
    public void testGetColumnByTable() {
        try {
            testCreateTable();
        } catch (Exception e) {
            fail();
        }

        List<ColumnMetadata> columnsMetadata = MetadataManager.MANAGER.getColumnByTable("testCatalog", "testTable");

        assertTrue(columnsMetadata.size() == 2);
        assertTrue(columnsMetadata.get(0).getName().getName().equals("id"));
        assertTrue(columnsMetadata.get(1).getName().getName().equals("user"));
    }

    @Test
    public void testGetConnectors() {

        try {
            MetadataManager.MANAGER.clear();
        } catch (HeuristicRollbackException | HeuristicMixedException | NotSupportedException | RollbackException |
                SystemException e) {
            fail();
        }

        testCreateConnector();

        Status status = Status.ONLINE;
        List<ConnectorMetadata> connectors = MetadataManager.MANAGER.getConnectors(status);

        assertTrue(connectors.size() == 1);
        assertTrue(connectors.get(0).getStatus() == status);
    }

    @Test
    public void testGetConnectorNames() {

        try {
            MetadataManager.MANAGER.clear();
        } catch (HeuristicRollbackException | HeuristicMixedException | NotSupportedException | RollbackException |
                SystemException e) {
            fail();
        }

        testCreateConnector();

        List<ConnectorName> connectors = MetadataManager.MANAGER.getConnectorNames(Status.ONLINE);

        assertTrue(connectors.size() == 1);
        assertTrue(connectors.get(0).getName().equalsIgnoreCase("connectorTest"));
    }

    @Test(expectedExceptions = MetadataManagerException.class)
    public void testShouldBeUniqueException() {
        DataStoreName name = createTestDatastore();
        String version = "0.1.1";
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
        createTestCatalog("catalogTest");
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
        createTestTable(clusterName, catalogName, tableName, columnNames, columnTypes, partitionKeys, clusteringKeys);

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
            createTestCluster("clusterTest", new DataStoreName("dataStoreTest"));
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
        createTestDatastore();

        DataStoreName name = new DataStoreName("dataStoreTest");
        String version = "0.1.1";
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
        createTestConnector(name, dataStoreName, actorRef);

        String version = "0.1.1";
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
