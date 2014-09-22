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

package com.stratio.meta2.core.validator;


import com.stratio.meta2.common.api.generated.connector.SupportedOperationsType;
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
import com.stratio.meta2.core.grammar.ParsingTest;
import com.stratio.meta2.core.grid.Grid;
import com.stratio.meta2.core.grid.GridInitializer;
import com.stratio.meta2.core.metadata.MetadataManager;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

public class BasicValidatorTest extends BasicCoreCassandraTest {

  static Map<FirstLevelName, IMetadata> metadataMap;

  protected static MetadataManager metadataManager = null;

  protected static final ParsingTest pt = new ParsingTest();

    private static String path = "";


    @BeforeClass
    public static void setUpBeforeClass() {
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        //initializeGrid();

        GridInitializer gridInitializer = Grid.initializer();
        gridInitializer = gridInitializer.withContactPoint("127.0.0.1");
        path = "/tmp/metadata-store-" + UUID.randomUUID();
        gridInitializer.withPort(7800)
            .withListenAddress("127.0.0.1")
            .withMinInitialMembers(1)
            .withJoinTimeoutInMs(3000)
            .withPersistencePath(path).init();

        metadataMap = Grid.getInstance().map("meta-test");
        Lock lock = Grid.getInstance().lock("meta-test");
        TransactionManager tm = Grid.getInstance().transactionManager("meta-test");
        MetadataManager.MANAGER.init(metadataMap, lock, tm);
        MetadataManager.MANAGER.createDataStore(createDataStoreMetadata());
        MetadataManager.MANAGER.createConnector(createConnectorMetadata());
        MetadataManager.MANAGER.createCluster(createClusterMetadata());
        MetadataManager.MANAGER.createCatalog(generateCatalogsMetadata());
        MetadataManager.MANAGER.createTable(createTable());

    }


    @BeforeMethod
    public void setUp() throws Exception {

    }


    private void initializeGrid() {

    }

    private static CatalogMetadata generateCatalogsMetadata(){
        CatalogMetadata catalogMetadata;
        CatalogName catalogName=new CatalogName("demo");
        Map<Selector, Selector> options=new HashMap<>();
        Map<TableName, TableMetadata> tables=new HashMap<>();
        catalogMetadata=new CatalogMetadata(catalogName,options,tables);
        return catalogMetadata;
    }

    private static TableMetadata createTable() {
        TableMetadata tableMetadata;
        TableName targetTable=new TableName("demo", "users");
        Map<Selector, Selector> options=new HashMap<>();
        Map<ColumnName, ColumnMetadata> columns=new HashMap<>();
        ClusterName clusterRef=new ClusterName("cluster");
        List<ColumnName> partitionKey=new ArrayList<>();
        List<ColumnName> clusterKey=new ArrayList<>();
        Object[] parameters=null;
        columns.put(new ColumnName(new TableName("demo","users"),"name"),new ColumnMetadata(new ColumnName(new TableName("demo","users"),"name"),parameters, ColumnType.TEXT));
        columns.put(new ColumnName(new TableName("demo","users"),"gender"),new ColumnMetadata(new ColumnName(new TableName("demo","users"),"gender"),parameters, ColumnType.TEXT));
        columns.put(new ColumnName(new TableName("demo","users"),"age"),new ColumnMetadata(new ColumnName(new TableName("demo","users"),"age"),parameters, ColumnType.INT));
        columns.put(new ColumnName(new TableName("demo","users"),"bool"),new ColumnMetadata(new ColumnName(new TableName("demo","users"),"bool"),parameters, ColumnType.BOOLEAN));
        columns.put(new ColumnName(new TableName("demo","users"),"phrase"),new ColumnMetadata(new ColumnName(new TableName("demo","users"),"phrase"),parameters, ColumnType.TEXT));
        columns.put(new ColumnName(new TableName("demo","users"),"email"),new ColumnMetadata(new ColumnName(new TableName("demo","users"),"email"),parameters, ColumnType.TEXT));


        Map<IndexName, IndexMetadata> indexes=new HashMap<>();
        tableMetadata=new TableMetadata(targetTable,options,columns,indexes,clusterRef,partitionKey,clusterKey);

        return tableMetadata;
    }

    private static ConnectorMetadata createConnectorMetadata() {
        Set<DataStoreName> dataStoreRefs=new HashSet<>();
        dataStoreRefs.add(new DataStoreName("Casssandra"));
        SupportedOperationsType supportedOperations=new SupportedOperationsType();
        ConnectorMetadata connectorMetadata=new ConnectorMetadata(new ConnectorName("CassandraConnector"),"1.0",dataStoreRefs,null,null,supportedOperations);
        return connectorMetadata;
    }

    private static DataStoreMetadata createDataStoreMetadata() {
        DataStoreMetadata dataStoreMetadata=new DataStoreMetadata(new DataStoreName("Cassandra"),"1.0",null,null);
        return dataStoreMetadata;
    }

    private static ClusterMetadata createClusterMetadata() {
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs=new HashMap<>();
        ConnectorAttachedMetadata connectorAttachedMetadata=new ConnectorAttachedMetadata(new ConnectorName("CassandraConnector"),new ClusterName("cluster"), null);
        connectorAttachedRefs.put(new ConnectorName("CassandraConnector"),connectorAttachedMetadata);
        ClusterMetadata clusterMetadata=new ClusterMetadata(new ClusterName("cluster"),new DataStoreName("Cassandra"),null,connectorAttachedRefs);
        return clusterMetadata;
    }

    @AfterClass
    public void tearDown() throws Exception {
        TransactionManager tm = Grid.getInstance().transactionManager("meta-test");
        tm.begin();
        metadataMap.clear();
        tm.commit();
        Grid.getInstance().close();
        FileUtils.deleteDirectory(new File(path));
    }
}
