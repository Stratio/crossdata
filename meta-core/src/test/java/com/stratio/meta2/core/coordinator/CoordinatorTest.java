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

package com.stratio.meta2.core.coordinator;

import com.stratio.meta2.common.api.generated.datastore.OptionalPropertiesType;
import com.stratio.meta2.common.api.generated.datastore.RequiredPropertiesType;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.common.metadata.ClusterAttachedMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.IMetadata;
import com.stratio.meta2.core.grid.Grid;
import com.stratio.meta2.core.grid.GridInitializer;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetaDataParsedQuery;
import com.stratio.meta2.core.query.MetaDataValidatedQuery;
import com.stratio.meta2.core.query.MetadataPlannedQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CoordinatorTest {

  private String path = "";

  Map<FirstLevelName, IMetadata> metadataMap =  new HashMap<>();

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

  @BeforeMethod
  public void setUp() throws Exception {
    initializeGrid();
    Map<FirstLevelName, IMetadata> metadataMap = Grid.getInstance().map("meta-test");
    Lock lock = Grid.getInstance().lock("meta-test");
    TransactionManager tm = Grid.getInstance().transactionManager("meta-test");
    MetadataManager.MANAGER.init(metadataMap, lock, tm);
  }

  @Test
  public void testAttachCluster() throws Exception {

    // Create and add a test datastore information to the metadatamanager
    DataStoreName name = new DataStoreName("datastoreTest");
    String version = "0.1.0";
    RequiredPropertiesType requiredProperties = null;
    OptionalPropertiesType othersProperties = null;
    DataStoreMetadata datastoreTest = new DataStoreMetadata(name, version, requiredProperties, othersProperties);
    MetadataManager.MANAGER.createDataStore(datastoreTest);

    // Add information about the cluster attachment to the metadatamanager
    Coordinator coordinator = new Coordinator();
    BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), "ATTACH CLUSTER cassandra_prod ON DATASTORE cassandra WITH OPTIONS {}", new CatalogName("test"));

    AttachClusterStatement
        attachClusterStatement = new AttachClusterStatement("clusterTest", false, "datastoreTest", "{}");

    MetaDataParsedQuery metadataParsedQuery = new MetaDataParsedQuery(baseQuery, attachClusterStatement);

    MetaDataValidatedQuery metadataValidatedQuery = new MetaDataValidatedQuery(metadataParsedQuery);

    MetadataPlannedQuery plannedQuery = new MetadataPlannedQuery(metadataValidatedQuery);

    coordinator.coordinate(plannedQuery);

    /*
    Method
        attachClusterMethod =
        coordinator.getClass().getMethod("attachCluster", AttachClusterStatement.class);
    attachClusterMethod.setAccessible(true);
    String clusterName = "clusterTest";
    boolean ifNotExists = false;
    String datastoreName = "datastoreTest";
    String options = "{}";
    AttachClusterStatement attachClusterStatement = new AttachClusterStatement(clusterName, ifNotExists, datastoreName, options);
    attachClusterMethod.invoke(attachClusterStatement);
    */

    // Check that changes persisted in the MetadataManager ("datastoreTest" datastore)
    datastoreTest = MetadataManager.MANAGER.getDataStore(new DataStoreName("dataStoreTest"));
    Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefsTest = datastoreTest.getClusterAttachedRefs();
    boolean found = false;
    for(ClusterName clusterNameTest: clusterAttachedRefsTest.keySet()){
      ClusterAttachedMetadata clusterAttachedMetadata = clusterAttachedRefsTest.get(clusterNameTest);
      if(clusterAttachedMetadata.getClusterRef().equals(new ClusterName("clusterTest"))){
        assertEquals(clusterAttachedMetadata.getDataStoreRef(), new DataStoreName("datastoreTest"), "Wrong attachment for clusterTest");
        found = true;
        break;
      }
    }
    assertTrue(found, "Attachment not found");
  }

  @AfterMethod
  public void tearDown() throws Exception {
    metadataMap.clear();
    Grid.getInstance().close();
    FileUtils.deleteDirectory(new File(path));
  }

}
