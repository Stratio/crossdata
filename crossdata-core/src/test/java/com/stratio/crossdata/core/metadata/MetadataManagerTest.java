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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.metadata.ClusterAttachedMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;

public class MetadataManagerTest extends MetadataManagerTestHelper {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(MetadataManagerTest.class);

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
        TableMetadata table = createTestTable(clusterName, catalogName.getName(), "testTable", columnNames1, columnTypes1,
                partitionKeys1,
                clusteringKeys1);

        assertEquals(MetadataManager.MANAGER.getTable(table.getName()).getName(), table.getName());
    }
/*
    //CREATE INDEX
    @Test
    public void testCreateIndex() throws Exception {

        String datastore = "datastoreTest";
        String cluster = "clusterTest";
        String catalog = "catalogTest5";
        String table = "tableTest";

        createTestDatastoreAndPersist(datastore, "0.1.0");
        createTestClusterAndPersist(cluster, datastore);
        createTestsCatalogAndPersist(catalog);
        createTestTableAndPersist(cluster, catalog, table);

        BaseQuery baseQuery =
                new BaseQuery(UUID.randomUUID().toString(), "CREATE INDEX testIndex ON testTable",
                        new CatalogName(catalog));

        CreateIndexStatement createIndexStatement = new CreateIndexStatement();
        createIndexStatement.setTableName(new TableName("catalogTest5", "tableTest"));
        new MetadataParsedQuery(baseQuery, createIndexStatement);

        MetadataParsedQuery metadataParsedQuery =
                new MetadataParsedQuery(baseQuery, createIndexStatement);

        MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

        MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery, null);

        Coordinator coordinator = new Coordinator();
        if (coordinator.coordinate(plannedQuery) instanceof MetadataInProgressQuery) {
            assertTrue(true);
            coordinator.persist(plannedQuery);
        } else {
            fail("Coordinator.coordinate not creating new MetadataInProgressQuery");
        }
    }

    //INSERT INTO
    @Test
    public void testInsertInto() throws Exception {
        String catalog = "catalogTest6";
        String table = "tableTest";

        BaseQuery baseQuery =
                new BaseQuery(UUID.randomUUID().toString(),
                        "INSERT INTO catalogTest6.tableTest ('col1', 'col2') VALUES (1, 2)",
                        new CatalogName(catalog));

        InsertIntoStatement insert = new InsertIntoStatement(new TableName(catalog, table), new ArrayList<ColumnName>(),
                new SelectStatement(new TableName(catalog, table)), false);

        StorageParsedQuery metadataParsedQuery =
                new StorageParsedQuery(baseQuery, insert);

        StorageValidatedQuery metadataValidatedQuery = new StorageValidatedQuery(metadataParsedQuery);

        StoragePlannedQuery plannedQuery = new StoragePlannedQuery(metadataValidatedQuery, null);

        Coordinator coordinator = new Coordinator();
        if (coordinator.coordinate(plannedQuery) instanceof StorageInProgressQuery) {
            assertTrue(true);
            coordinator.persist(plannedQuery);
        } else {
            fail("Coordinator.coordinate not creating new StorageInProgressQuery");
        }
    }

    private void createTestDatastoreAndPersist(String name, String version) {
        // Create and add a test datastore metadata to the metadatamanager
        DataStoreName dataStoreName = new DataStoreName(name);
        String dataStoreVersion = version;
        RequiredPropertiesType requiredProperties = null;
        OptionalPropertiesType othersProperties = null;
        DataStoreMetadata datastoreTest =
                new DataStoreMetadata(dataStoreName, dataStoreVersion, requiredProperties, othersProperties);
        MetadataManager.MANAGER.createDataStore(datastoreTest, false);
    }

    private void createTestClusterAndPersist(String cluster, String datastore) {
        // Create and add a test cluster metadata to the metadatamanager
        ClusterName clusterName = new ClusterName(cluster);
        DataStoreName dataStoreRef = new DataStoreName(datastore);
        Map<Selector, Selector> options = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        ClusterMetadata clusterTest =
                new ClusterMetadata(clusterName, dataStoreRef, options, connectorAttachedRefs);
        MetadataManager.MANAGER.createCluster(clusterTest, false);
    }

    private void createTestsCatalogAndPersist(String catalog) {
        // Create and add test catalog to the metadatamanager
        CatalogName catalogName = new CatalogName(catalog);
        CatalogMetadata catalogMetadata =
                new CatalogMetadata(catalogName, new HashMap<Selector, Selector>(),
                        new HashMap<TableName, TableMetadata>());
        MetadataManager.MANAGER.createCatalog(catalogMetadata);
    }

    private void createTestTableAndPersist(String clusterName, String catalogName,
            String tableNameString) {
        // Create and add test table to the metadatamanager
        TableName tableName = new TableName(catalogName, tableNameString);
        TableMetadata table =
                new TableMetadata(tableName, new HashMap<Selector, Selector>(),
                        new HashMap<ColumnName, ColumnMetadata>(), new HashMap<IndexName, IndexMetadata>(),
                        new ClusterName(clusterName), new ArrayList<ColumnName>(), new ArrayList<ColumnName>());

        Map<ColumnName, ColumnType> columns = new HashMap<ColumnName, ColumnType>();
        for (Entry<ColumnName, ColumnMetadata> c : table.getColumns().entrySet()) {
            columns.put(c.getKey(), c.getValue().getColumnType());
        }
        MetadataManager.MANAGER.createTable(table);
    }*/
}
