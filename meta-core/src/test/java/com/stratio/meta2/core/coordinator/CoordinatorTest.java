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

import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.common.metadata.IMetadata;
import com.stratio.meta2.core.grid.Grid;
import com.stratio.meta2.core.grid.GridInitializer;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.statements.AttachClusterStatement;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    MetadataManager.MANAGER.init(metadataMap, Grid.getInstance().lock("meta-test"));
  }

  @Test
  public void testAttachCluster() throws Exception {

    // TODO: Create DataStore "datastoreTest"

    Coordinator coordinator = new Coordinator();
    Method
        attachClusterMethod =
        coordinator.getClass().getDeclaredMethod("attachCluster", AttachClusterStatement.class);
    attachClusterMethod.setAccessible(true);
    String clusterName = "catalogTest";
    boolean ifNotExists = false;
    String datastoreName = "datastoreTest";
    String options = "";
    AttachClusterStatement attachClusterStatement = new AttachClusterStatement(clusterName, ifNotExists, datastoreName, options);
    attachClusterMethod.invoke(attachClusterStatement);

    // TODO: Check that changes persisted in the MetadataManager ("datastoreTest" metadata)

  }

  @AfterMethod
  public void tearDown() throws Exception {
    metadataMap.clear();
    Grid.getInstance().close();
    FileUtils.deleteDirectory(new File(path));
  }

}
