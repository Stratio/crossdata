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

package com.stratio.crossdata.core.validator;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.IndexType;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.metadata.MetadataManager;

public class BasicValidatorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws ManifestException {
        MetadataManagerTestHelper.HELPER.initHelper();
        MetadataManagerTestHelper.HELPER.createTestEnvironment();
        MetadataManager.MANAGER.createDataStore(createDataStoreMetadata(), false);
        ConnectorMetadata connectorMetadata = createConnectorMetadata();
        MetadataManager.MANAGER.createConnector(connectorMetadata, false);
        MetadataManager.MANAGER.setConnectorStatus(connectorMetadata.getName(), Status.ONLINE);
        MetadataManager.MANAGER.createCluster(createClusterMetadata(), false);
        MetadataManager.MANAGER.createCatalog(generateCatalogMetadata("demo"), false);
        MetadataManager.MANAGER.createTable(createTable("demo", "users"), false);
        MetadataManager.MANAGER.createTable(createJoinTable(), false);
        MetadataManager.MANAGER.createCatalog(generateCatalogMetadata("sales"), false);
        MetadataManager.MANAGER.createTable(createTable("sales", "customers"), false);
        MetadataManager.MANAGER.createCatalog(generateCatalogMetadata("test"), false);
        MetadataManager.MANAGER.createTable(createTable("test", "table1"), false);
        MetadataManager.MANAGER.createTable(createTable2("test", "table2"), false);
        MetadataManager.MANAGER.createTable(createTable2("demo", "table3"), false);
        MetadataManager.MANAGER.createTable(createTable3("demo", "table4"), false);
    }

    @AfterClass
    public void tearDown() throws Exception {
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    private static CatalogMetadata generateCatalogMetadata(String catalog) {
        CatalogMetadata catalogMetadata;
        CatalogName catalogName = new CatalogName(catalog);
        Map<Selector, Selector> options = new HashMap<>();
        Map<TableName, TableMetadata> tables = new HashMap<>();
        catalogMetadata = new CatalogMetadata(catalogName, options, tables);
        return catalogMetadata;
    }

    private static TableMetadata createTable(String catalog, String table) {
        TableMetadata tableMetadata;
        TableName targetTable = new TableName(catalog, table);
        Map<Selector, Selector> options = new HashMap<>();
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        ClusterName clusterRef = new ClusterName("cluster");
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        Object[] parameters = null;
        columns.put(new ColumnName(new TableName(catalog, table), "name"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "name"), parameters,
                        new ColumnType(DataType.TEXT)));
        columns.put(new ColumnName(new TableName(catalog, table), "gender"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "gender"), parameters,
                        new ColumnType(DataType.TEXT)));
        columns.put(new ColumnName(new TableName(catalog, table), "age"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "age"), parameters,
                        new ColumnType(DataType.INT)));
        columns.put(new ColumnName(new TableName(catalog, table), "bool"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "bool"), parameters,
                        new ColumnType(DataType.BOOLEAN)));
        columns.put(new ColumnName(new TableName(catalog, table), "phrase"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "phrase"), parameters,
                        new ColumnType(DataType.TEXT)));
        columns.put(new ColumnName(new TableName(catalog, table), "email"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "email"), parameters,
                        new ColumnType(DataType.TEXT)));

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        Map<ColumnName, ColumnMetadata> columnsIndex = new HashMap<>();
        ColumnMetadata columnMetadataIndex = new ColumnMetadata(
                new ColumnName(new TableName(catalog, table), "gender"),
                parameters, new ColumnType(DataType.TEXT));
        columnsIndex.put(new ColumnName(new TableName(catalog, table), "gender"), columnMetadataIndex);

        IndexMetadata indexMetadata = new IndexMetadata(new IndexName(catalog, table, "gender_idx"), columnsIndex,
                IndexType.DEFAULT, options);

        indexes.put(new IndexName(catalog, table, "gender_idx"), indexMetadata);

        tableMetadata = new TableMetadata(targetTable, options, columns, indexes, clusterRef, partitionKey, clusterKey);

        return tableMetadata;
    }

    private static TableMetadata createTable2(String catalog, String table) {
        TableMetadata tableMetadata;
        TableName targetTable = new TableName(catalog, table);
        Map<Selector, Selector> options = new HashMap<>();
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        ClusterName clusterRef = new ClusterName("cluster");
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        Object[] parameters = null;
        columns.put(new ColumnName(new TableName(catalog, table), "name"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "name"), parameters,
                        new ColumnType(DataType.TEXT)));
        columns.put(new ColumnName(new TableName(catalog, table), "surname"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "surname"), parameters,
                        new ColumnType(DataType.TEXT)));
        columns.put(new ColumnName(new TableName(catalog, table), "rating"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "rating"), parameters,
                        new ColumnType(DataType.FLOAT)));
        columns.put(new ColumnName(new TableName(catalog, table), "member"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "member"), parameters,
                        new ColumnType(DataType.BOOLEAN)));
        columns.put(new ColumnName(new TableName(catalog, table), "address"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "address"), parameters,
                        new ColumnType(DataType.TEXT)));
        columns.put(new ColumnName(new TableName(catalog, table), "comment"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "comment"), parameters,
                        new ColumnType(DataType.TEXT)));

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        Map<ColumnName, ColumnMetadata> columnsIndex = new HashMap<>();
        ColumnMetadata columnMetadataIndex = new ColumnMetadata(
                new ColumnName(new TableName(catalog, table), "gender"),
                parameters, new ColumnType(DataType.TEXT));
        columnsIndex.put(new ColumnName(new TableName(catalog, table), "gender"), columnMetadataIndex);

        IndexMetadata indexMetadata = new IndexMetadata(new IndexName(catalog, table, "gender_idx"), columnsIndex,
                IndexType.DEFAULT, options);

        indexes.put(new IndexName(catalog, table, "gender_idx"), indexMetadata);

        tableMetadata = new TableMetadata(targetTable, options, columns, indexes, clusterRef, partitionKey, clusterKey);

        return tableMetadata;
    }

    private static TableMetadata createTable3(String catalog, String table) {
        TableMetadata tableMetadata;
        TableName targetTable = new TableName(catalog, table);
        Map<Selector, Selector> options = new HashMap<>();
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        ClusterName clusterRef = new ClusterName("cluster");
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        Object[] parameters = null;
        columns.put(new ColumnName(new TableName(catalog, table), "id"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "id"), parameters,
                        new ColumnType(DataType.INT)));
        ColumnType ct = new ColumnType(DataType.LIST);
        ct.setDBCollectionType(new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(new TableName(catalog, table), "animals"),
                new ColumnMetadata(new ColumnName(new TableName(catalog, table), "animals"), parameters, ct));
        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        tableMetadata = new TableMetadata(targetTable, options, columns, indexes, clusterRef, partitionKey, clusterKey);
        return tableMetadata;
    }

    private static TableMetadata createJoinTable() {
        TableMetadata tableMetadata;
        TableName targetTable = new TableName("demo", "users_info");
        Map<Selector, Selector> options = new HashMap<>();
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        ClusterName clusterRef = new ClusterName("cluster");
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        Object[] parameters = null;
        columns.put(new ColumnName(new TableName("demo", "users_info"), "name"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users_info"), "name"), parameters,
                        new ColumnType(DataType.TEXT)));
        columns.put(new ColumnName(new TableName("demo", "users_info"), "info"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users_info"), "info"), parameters,
                        new ColumnType(DataType.TEXT)));

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        tableMetadata = new TableMetadata(targetTable, options, columns, indexes, clusterRef, partitionKey, clusterKey);

        return tableMetadata;
    }

    private static ConnectorMetadata createConnectorMetadata() {
        DataStoreName dataStoreName = new DataStoreName("Cassandra");
        List<String> dataStoreRefs = Arrays.asList(dataStoreName.getName());

        ArrayList<String> supportedOperations = new ArrayList();

        ConnectorMetadata connectorMetadata = null;
        try {
            connectorMetadata = new ConnectorMetadata(new ConnectorName("CassandraConnector"), "1.0", false,
                    dataStoreRefs, null, null, supportedOperations, null, null);
        } catch (ManifestException e) {
            fail(e.getMessage());
        }
        return connectorMetadata;
    }

    private static DataStoreMetadata createDataStoreMetadata() {
        Set<FunctionType> functions = new HashSet<>();
        FunctionType function = new FunctionType();
        function.setFunctionName("getYear");
        function.setSignature("getYear(Tuple[Int]):Tuple[Any]");
        function.setFunctionType("simple");
        functions.add(function);
        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(new DataStoreName("Cassandra"), "1.0",
                new HashSet<PropertyType>(), new HashSet<PropertyType>(), null, functions);
        return dataStoreMetadata;
    }

    private static ClusterMetadata createClusterMetadata() throws ManifestException {
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();
        ConnectorAttachedMetadata connectorAttachedMetadata = new ConnectorAttachedMetadata(
                new ConnectorName("CassandraConnector"), new ClusterName("cluster"), null,5);
        connectorAttachedRefs.put(new ConnectorName("CassandraConnector"), connectorAttachedMetadata);
        ClusterMetadata clusterMetadata = new ClusterMetadata(new ClusterName("cluster"),
                new DataStoreName("Cassandra"), null, connectorAttachedRefs);
        return clusterMetadata;
    }

}
