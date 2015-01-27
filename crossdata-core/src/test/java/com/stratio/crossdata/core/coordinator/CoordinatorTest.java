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

package com.stratio.crossdata.core.coordinator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.executionplan.ExecutionType;
import com.stratio.crossdata.common.executionplan.ManagementWorkflow;
import com.stratio.crossdata.common.executionplan.MetadataWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.manifest.FunctionType;
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
import com.stratio.crossdata.common.metadata.IndexType;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.communication.ManagementOperation;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.execution.MetadataManagerTestHelper;

public class CoordinatorTest extends MetadataManagerTestHelper {

    /**
     * Testing an API operation (attaching a cluster to a datastore)
     *
     * @throws Exception
     */
    @Test
    public void testAttachCluster() throws Exception {

        // Create and add a test datastore metadata to the metadataManager
        DataStoreMetadata datastoreTest = insertDataStore("datastoreTest", "production");

        ManagementWorkflow workflow = new ManagementWorkflow("", null, ExecutionType.ATTACH_CLUSTER,
                ResultType.RESULTS);
        workflow.setClusterName(new ClusterName("clusterTest"));
        workflow.setConnectorName(new ConnectorName("myConnector"));
        workflow.setDatastoreName(new DataStoreName("dataStoreTest"));
        Coordinator coordinator = new Coordinator();
        ManagementOperation op = workflow.createManagementOperationMessage();
        coordinator.executeManagementOperation(op);
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

    /**
     * Testing an API operation (detaching a cluster to a datastore).
     *
     * @throws Exception
     */
    @Test
    public void testDetachCluster() throws Exception {

        // Create and add a test datastore metadata to the metadataManager
        DataStoreMetadata datastoreTest = insertDataStore("datastoreTest", "production");

        ManagementWorkflow workflow = new ManagementWorkflow("", null, ExecutionType.DETACH_CLUSTER,
                ResultType.RESULTS);
        workflow.setClusterName(new ClusterName("clusterTest"));
        workflow.setConnectorName(new ConnectorName("myConnector"));
        workflow.setDatastoreName(new DataStoreName("dataStoreTest"));
        Coordinator coordinator = new Coordinator();
        ManagementOperation op = workflow.createManagementOperationMessage();
        coordinator.executeManagementOperation(op);
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
        assertFalse(found, "Cluster detachment failed.");
    }

    @Test
    public void testAttachConnector() throws Exception {

        // Create and add a test datastore metadata to the metadataManager
        DataStoreMetadata datastoreTest = insertDataStore("datastoreTest", "preProduction");

        MetadataManager.MANAGER.createDataStore(datastoreTest, false);

        // Create and add a test cluster metadata to the metadataManager
        ClusterName clusterName = new ClusterName("clusterTest");
        DataStoreName dataStoreRef = new DataStoreName("dataStoreTest");
        Map<Selector, Selector> options = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        ClusterMetadata clusterTest =
                new ClusterMetadata(clusterName, dataStoreRef, options, connectorAttachedRefs);
        MetadataManager.MANAGER.createClusterAndAttach(clusterTest, false);

        // Create and add a test connector metadata to the metadataManager
        ConnectorName connectorName = new ConnectorName("connectorTest");
        String connectorVersion = "0.2.0";
        List<String> dataStoreRefs = new ArrayList<>();
        List<PropertyType> requiredProperties = new ArrayList<>();
        List<PropertyType> optionalProperties = new ArrayList<>();
        List<String> supportedOperations = new ArrayList<>();
        List<FunctionType> connectorFunctions = new ArrayList<>();
        List<String> excludedFunctions = new ArrayList<>();

        ConnectorMetadata connectorTest = new ConnectorMetadata(
                connectorName,
                connectorVersion,
                dataStoreRefs,
                requiredProperties,
                optionalProperties,
                supportedOperations,
                connectorFunctions,
                excludedFunctions);
        MetadataManager.MANAGER.createConnector(connectorTest, false);

        // Create workflow
        String queryId = "testAttachConnectorId";
        String actorRef = "testAttachConnectorActorRef";
        ExecutionType executionType = ExecutionType.ATTACH_CONNECTOR;
        ResultType type = ResultType.RESULTS;
        ManagementWorkflow managementWorkflow = new ManagementWorkflow(queryId, actorRef, executionType, type);

        managementWorkflow.setClusterName(clusterName);
        managementWorkflow.setConnectorName(connectorName);
        options.put(new StringSelector("Limit"), new IntegerSelector(100));
        managementWorkflow.setOptions(options);

        Coordinator coordinator = new Coordinator();

        coordinator.executeManagementOperation(managementWorkflow.createManagementOperationMessage());

        // Check that changes persisted in the MetadataManager ("clusterTest" cluster)
        clusterTest = MetadataManager.MANAGER.getCluster(new ClusterName("clusterTest"));

        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefsTest =
                clusterTest.getConnectorAttachedRefs();

        boolean found = false;

        for (ConnectorName connectorNameTest: connectorAttachedRefsTest.keySet()) {
            ConnectorAttachedMetadata connectorAttachedMetadataTest =
                    connectorAttachedRefsTest.get(connectorNameTest);
            if (connectorAttachedMetadataTest.getClusterRef().equals(new ClusterName("clusterTest"))) {
                assertEquals(connectorAttachedMetadataTest.getConnectorRef(), connectorName,
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
        Map<TableName, TableMetadata> catalogTables = new HashMap<>();
        Map<Selector, Selector> options = new HashMap<>();
        CatalogMetadata catalogMetadata = new CatalogMetadata(catalogName, options, catalogTables);

        String queryId = "testCreateCatalogCheckNameId";
        String actorRef = "testCreateCatalogCheckNameActorRef";
        ExecutionType executionType = ExecutionType.CREATE_CATALOG;
        ResultType type = ResultType.RESULTS;
        MetadataWorkflow metadataWorkflow = new MetadataWorkflow(queryId, actorRef, executionType, type);
        metadataWorkflow.setCatalogName(catalogMetadata.getName());
        metadataWorkflow.setCatalogMetadata(catalogMetadata);
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_CATALOG);

        Coordinator coordinator = new Coordinator();
        coordinator.persist(metadataWorkflow, result);

        assertEquals(MetadataManager.MANAGER.getCatalog(catalogMetadata.getName()).getName(),
                catalogMetadata.getName()," CheckName Error Test");
    }

    // CREATE TABLE
    @Test
    public void testCreateTable() throws Exception {

        String datastore = "datastoreTest";
        String cluster = "clusterTest";
        String catalog = "catalogTest4";
        String tableString = "tableTest";

        createTestDatastore();
        createTestCluster(cluster, new DataStoreName(datastore));
        createTestCatalog(catalog);

        TableName tableName = new TableName(catalog, tableString);
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys = { "id" };
        String[] clusteringKeys = { };
        TableMetadata tableMetadata = defineTable(
                new ClusterName(cluster),
                catalog,
                tableString,
                columnNames1,
                columnTypes,
                partitionKeys,
                clusteringKeys);

        String queryId = "testCreateTableQueryId";
        String actorRef = "testCreateTableActorRef";
        ExecutionType executionType = ExecutionType.CREATE_TABLE;
        ResultType type = ResultType.RESULTS;
        MetadataWorkflow metadataWorkflow = new MetadataWorkflow(queryId, actorRef, executionType, type);
        metadataWorkflow.setTableName(tableName);
        metadataWorkflow.setTableMetadata(tableMetadata);

        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_TABLE);
        Coordinator coordinator = new Coordinator();
        coordinator.persist(metadataWorkflow, result);

        assertEquals(MetadataManager.MANAGER.getTable(tableName).getName(), tableMetadata.getName(),
                "Test create table fail");
    }


    //CREATE INDEX
    @Test
    public void testCreateIndex() throws Exception {

        String datastore = "datastoreTest";
        String cluster = "clusterTest";
        String catalog = "catalogTest5";
        String table = "tableTest";
        String index = "indexTest";

        createTestDatastore();
        createTestCluster(cluster, new DataStoreName(datastore));
        createTestCatalog(catalog);

        TableName tableName = new TableName(catalog, table);
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys = { "id" };
        String[] clusteringKeys = { };

        Set<IndexMetadata> indexes = new HashSet<>();
        IndexName indexName = new IndexName(new ColumnName(catalog, table, index));
        Map<ColumnName, ColumnMetadata> columns = new HashMap<>();
        ColumnMetadata column = new ColumnMetadata(new ColumnName(catalog, table, "user"), null, ColumnType.TEXT);
        columns.put(column.getName(), column);
        IndexType indexType = IndexType.DEFAULT;
        Map<Selector, Selector> options = new HashMap<>();
        IndexMetadata indexMetadata = new IndexMetadata(indexName, columns, indexType, options);

        createTestTable(new ClusterName(cluster), catalog, table, columnNames1, columnTypes,
                partitionKeys, clusteringKeys, indexes);

        String queryId = "testCreateIndexQueryId";
        String actorRef = "testCreateIndexActorRef";
        ExecutionType executionType = ExecutionType.CREATE_INDEX;
        ResultType type = ResultType.RESULTS;
        MetadataWorkflow metadataWorkflow = new MetadataWorkflow(queryId, actorRef, executionType, type);
        metadataWorkflow.setIndexName(indexMetadata.getName());
        metadataWorkflow.setIndexMetadata(indexMetadata);

        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_INDEX);
        Coordinator coordinator = new Coordinator();
        coordinator.persist(metadataWorkflow, result);

        String storedIndexName =
                MetadataManager.MANAGER.getTable(tableName).getIndexes().keySet().iterator().next().getName();
        assertTrue(index.equalsIgnoreCase(storedIndexName),
                "Expected: " + index + System.lineSeparator() +
                        "Found:    " + storedIndexName);
    }

}
