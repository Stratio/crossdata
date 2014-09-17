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

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

public class CoordinatorTest {

  private Grid initializeGrid() {
    GridInitializer gridInitializer = Grid.initializer();
    gridInitializer = gridInitializer.withContactPoint("127.0.0.1");
    return gridInitializer.withPort(7800)
        .withListenAddress("127.0.0.1")
        .withMinInitialMembers(1)
        .withJoinTimeoutInMs(3000)
        .withPersistencePath("/tmp/metadata-store-" + UUID.randomUUID()).init();
  }

  @BeforeMethod
  public void setUp() throws Exception {
    Grid grid = initializeGrid();
    Map<FirstLevelName, IMetadata> metadataMap = grid.map("meta-test");
    MetadataManager.MANAGER.init(metadataMap, grid.lock("meta-test"));
  }

  @AfterMethod
  public void tearDown() throws Exception {
    // TODO: Something with the MetadataManager or MetadataMap?
    GridInitializer gridInitializer = null;
  }

  @Test
  public void testCoordinate() throws Exception {

  }
}
