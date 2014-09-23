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

package com.stratio.meta2.core.coordinator;

import com.stratio.meta2.common.api.generated.connector.SupportedOperationsType;
import com.stratio.meta2.common.api.generated.datastore.OptionalPropertiesType;
import com.stratio.meta2.common.api.generated.datastore.RequiredPropertiesType;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterAttachedMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.metadata.MetadataManagerTests;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataInProgressQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.MetadataPlannedQuery;
import com.stratio.meta2.core.query.MetadataValidatedQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;
import com.stratio.meta2.core.statements.AttachConnectorStatement;
import com.stratio.meta2.core.statements.CreateCatalogStatement;
import com.stratio.meta2.core.statements.CreateIndexStatement;
import com.stratio.meta2.core.statements.CreateTableStatement;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class CoordinatorTest extends MetadataManagerTests {

  @Test
  public void testAttachCluster() throws Exception {

    // Create and add a test datastore metadata to the metadatamanager
    DataStoreName name = new DataStoreName("datastoreTest");
    String version = "0.1.0";
    RequiredPropertiesType requiredProperties = null;
    OptionalPropertiesType othersProperties = null;
    DataStoreMetadata datastoreTest =
        new DataStoreMetadata(name, version, requiredProperties, othersProperties);
    MetadataManager.MANAGER.createDataStore(datastoreTest, false);

    // Add information about the cluster attachment to the metadatamanager
    BaseQuery baseQuery =
        new BaseQuery(UUID.randomUUID().toString(),
            "ATTACH CLUSTER cassandra_prod ON DATASTORE cassandra WITH OPTIONS {}",
            new CatalogName("test"));

    AttachClusterStatement attachClusterStatement =
        new AttachClusterStatement("clusterTest", false, "datastoreTest", "{}");

    MetadataParsedQuery metadataParsedQuery =
        new MetadataParsedQuery(baseQuery, attachClusterStatement);

    MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

    MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery);

    Coordinator coordinator = new Coordinator();
    coordinator.coordinate(plannedQuery);

    // Check that changes persisted in the MetadataManager ("datastoreTest" datastore)
    datastoreTest = MetadataManager.MANAGER.getDataStore(new DataStoreName("dataStoreTest"));
    Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefsTest =
        datastoreTest.getClusterAttachedRefs();
    boolean found = false;
    for (ClusterName clusterNameTest : clusterAttachedRefsTest.keySet()) {
      ClusterAttachedMetadata clusterAttachedMetadata =
          clusterAttachedRefsTest.get(clusterNameTest);
      if (clusterAttachedMetadata.getClusterRef().equals(new ClusterName("clusterTest"))) {
        assertEquals(clusterAttachedMetadata.getDataStoreRef(), new DataStoreName("datastoreTest"),
            "Wrong attachment for clusterTest");
        found = true;
        break;
      }
    }
    assertTrue(found, "Attachment not found");
  }

  @Test
  public void testAttachConnector() throws Exception {

    // Create and add a test datastore metadata to the metadatamanager
    DataStoreName dataStoreName = new DataStoreName("datastoreTest");
    String dataStoreVersion = "0.1.0";
    RequiredPropertiesType requiredProperties = null;
    OptionalPropertiesType othersProperties = null;
    DataStoreMetadata datastoreTest =
        new DataStoreMetadata(dataStoreName, dataStoreVersion, requiredProperties, othersProperties);
    MetadataManager.MANAGER.createDataStore(datastoreTest, false);

    // Create and add a test cluster metadata to the metadatamanager
    ClusterName clusterName = new ClusterName("clusterTest");
    DataStoreName dataStoreRef = new DataStoreName("dataStoreTest");
    Map<String, Object> options = new HashMap<>();
    Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
    ClusterMetadata clusterTest =
        new ClusterMetadata(clusterName, dataStoreRef, options, connectorAttachedRefs);
    MetadataManager.MANAGER.createCluster(clusterTest, false);

    // Create and add a test connector metadata to the metadatamanager
    ConnectorName connectorName = new ConnectorName("connectorTest");
    String connectorVersion = "0.1.0";
    Set<DataStoreName> dataStoreRefs = new HashSet<>();
    com.stratio.meta2.common.api.generated.connector.RequiredPropertiesType connectorRequiredProperties =
        null;
    com.stratio.meta2.common.api.generated.connector.OptionalPropertiesType connectorOptionalProperties =
        null;
    SupportedOperationsType supportedOperations = null;
    ConnectorMetadata connectorTest =
        new ConnectorMetadata(connectorName, connectorVersion, dataStoreRefs,
            connectorRequiredProperties, connectorOptionalProperties, supportedOperations);
    MetadataManager.MANAGER.createConnector(connectorTest, false);

    // Add information about the connector attachment to the metadatamanager
    BaseQuery baseQuery =
        new BaseQuery(UUID.randomUUID().toString(),
            "ATTACH CONNECTOR cassandra_connector TO cassandra_prod WITH OPTIONS {}",
            new CatalogName("test"));

    ConnectorName connectorRef = new ConnectorName("connectorTest");
    ClusterName clusterRef = new ClusterName("clusterTest");
    Map<Selector, Selector> properties = new HashMap<>();
    ConnectorAttachedMetadata connectorAttachedMetadata =
        new ConnectorAttachedMetadata(connectorRef, clusterRef, properties);

    AttachConnectorStatement attachConnectorStatement =
        new AttachConnectorStatement("connectorTest", "clusterTest", "{}");

    MetadataParsedQuery metadataParsedQuery =
        new MetadataParsedQuery(baseQuery, attachConnectorStatement);

    MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

    MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery);

    Coordinator coordinator = new Coordinator();
    coordinator.coordinate(plannedQuery);

    // Check that changes persisted in the MetadataManager ("clusterTest" cluster)
    clusterTest = MetadataManager.MANAGER.getCluster(new ClusterName("clusterTest"));

    Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefsTest =
        clusterTest.getConnectorAttachedRefs();

    boolean found = false;

    for (ConnectorName connectorNameTest : connectorAttachedRefsTest.keySet()) {
      ConnectorAttachedMetadata connectorAttachedMetadataTest =
          connectorAttachedRefsTest.get(connectorNameTest);
      if (connectorAttachedMetadataTest.getClusterRef().equals(new ClusterName("clusterTest"))) {
        assertEquals(connectorAttachedMetadata.getClusterRef(), new ClusterName("clusterTest"),
            "Wrong attachment for connectorTest");
        found = true;
        break;
      }
    }
    assertTrue(found, "Attachment not found");
  }

  // CREATE CATALOG
  @Test
  public void testCreateCatalogCheckName() throws Exception {
    CatalogName catalogName = new CatalogName("testCatalog");

    BaseQuery baseQuery =
        new BaseQuery(UUID.randomUUID().toString(), "CREATE CATALOG testCatalog", catalogName);

    CreateCatalogStatement createCatalogStatement =
        new CreateCatalogStatement(catalogName.getName(), false, "{}");

    MetadataParsedQuery metadataParsedQuery =
        new MetadataParsedQuery(baseQuery, createCatalogStatement);

    MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

    MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery);


    Coordinator coordinator = new Coordinator();
    if (coordinator.coordinate(plannedQuery) instanceof MetadataInProgressQuery) {
      assertTrue(true);
      coordinator.persist(plannedQuery);
    } else
      fail("Coordinator.coordinate not creating new MetadataInProgressQuery");

    assertEquals(MetadataManager.MANAGER.getCatalog(catalogName).getName(), catalogName);
  }

  @Test
  public void testCreateCatalogCheckNameNotEquals() throws Exception {
    CatalogName catalogName = new CatalogName("testCatalog2");

    BaseQuery baseQuery =
        new BaseQuery(UUID.randomUUID().toString(), "CREATE CATALOG testCatalog2", catalogName);

    CreateCatalogStatement createCatalogStatement =
        new CreateCatalogStatement(catalogName.getName(), false, "{}");

    MetadataParsedQuery metadataParsedQuery =
        new MetadataParsedQuery(baseQuery, createCatalogStatement);

    MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

    MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery);


    Coordinator coordinator = new Coordinator();
    if (coordinator.coordinate(plannedQuery) instanceof MetadataInProgressQuery) {
      assertTrue(true);
      coordinator.persist(plannedQuery);
    } else
      fail("Coordinator.coordinate not creating new MetadataInProgressQuery");

    assertNotEquals(MetadataManager.MANAGER.getCatalog(catalogName).getName(), new CatalogName(
        "notSameName"));
  }

  // CREATE TABLE
  @Test
  public void testCreateTable() throws Exception {

    String datastore = "datastoreTest";
    String cluster = "clusterTest";
    String catalog = "catalogTest4";
    String tableString = "tableTest";


    createTestDatastoreAndPersist(datastore, "0.1.0");
    createTestClusterAndPersist(cluster, datastore);
    createTestsCatalogAndPersist(catalog);

    // Create and add test table to the metadatamanager
    TableName tableName = new TableName(catalog, tableString);
    TableMetadata table =
        new TableMetadata(tableName, new HashMap<Selector, Selector>(),
            new HashMap<ColumnName, ColumnMetadata>(), new HashMap<IndexName, IndexMetadata>(),
            new ClusterName(cluster), new ArrayList<ColumnName>(), new ArrayList<ColumnName>());

    BaseQuery baseQuery =
        new BaseQuery(UUID.randomUUID().toString(), "CREATE TABLE testTable", new CatalogName(
            catalog));

    Map<ColumnName, ColumnType> columns = new HashMap<ColumnName, ColumnType>();
    for (Entry<ColumnName, ColumnMetadata> c : table.getColumns().entrySet()) {
      columns.put(c.getKey(), c.getValue().getColumnType());
    }

    CreateTableStatement createTableStatement =
        new CreateTableStatement(tableName, table.getClusterRef(), columns, table.getPrimaryKey(),
            table.getPartitionKey());
    createTableStatement.setTableMetadata(table);

    MetadataParsedQuery metadataParsedQuery =
        new MetadataParsedQuery(baseQuery, createTableStatement);

    MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

    MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery);

    Coordinator coordinator = new Coordinator();
    if (coordinator.coordinate(plannedQuery) instanceof MetadataInProgressQuery) {
      assertTrue(true);
      coordinator.persist(plannedQuery);
    } else
      fail("Coordinator.coordinate not creating new MetadataInProgressQuery");


    assertEquals(MetadataManager.MANAGER.getTable(tableName), table);
  }
  
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

    MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery);

    Coordinator coordinator = new Coordinator();
    if (coordinator.coordinate(plannedQuery) instanceof MetadataInProgressQuery) {
      assertTrue(true);
      coordinator.persist(plannedQuery);
    } else
      fail("Coordinator.coordinate not creating new MetadataInProgressQuery");
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
    Map<String, Object> options = new HashMap<>();
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
  }



}
