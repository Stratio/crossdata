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

package com.stratio.meta2.core.metadata;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.transaction.TransactionManager;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.stratio.meta2.common.api.generated.datastore.ClusterType;
import com.stratio.meta2.common.api.generated.datastore.HostsType;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.IMetadata;
import com.stratio.meta2.core.grid.Grid;
import com.stratio.meta2.core.grid.GridInitializer;

public class MetadataManagerTests {

    Map<FirstLevelName, Serializable> metadataMap = new HashMap<>();
    private String path = "";

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

    protected DataStoreMetadata insertDataStore(String dataStore, String cluster){
        DataStoreName dataStoreName = new DataStoreName(dataStore);
        String version = "0.1.0";
        com.stratio.meta2.common.api.generated.datastore.RequiredPropertiesType requiredPropertiesForDataStore = new
                com.stratio.meta2.common.api.generated.datastore.RequiredPropertiesType();
        ClusterType clusterType = new ClusterType();
        clusterType.setName(cluster);
        List<HostsType> hosts = new ArrayList<>();
        HostsType hostsType = new HostsType();
        hostsType.setHost("127.0.0.1");
        hostsType.setPort("9090");
        hosts.add(hostsType);
        clusterType.setHosts(hosts);
        requiredPropertiesForDataStore.setCluster(clusterType);
        com.stratio.meta2.common.api.generated.datastore.OptionalPropertiesType othersProperties = new com.stratio.meta2.common.api.generated.datastore.OptionalPropertiesType();
        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(dataStoreName, version, requiredPropertiesForDataStore, othersProperties);
        MetadataManager.MANAGER.createDataStore(dataStoreMetadata, false);
        return dataStoreMetadata;
    }

    @BeforeClass
    public void setUp() throws Exception {
        initializeGrid();
        Map<FirstLevelName, IMetadata> metadataMap = Grid.getInstance().map("meta-test");
        Lock lock = Grid.getInstance().lock("meta-test");
        TransactionManager tm = Grid.getInstance().transactionManager("meta-test");
        MetadataManager.MANAGER.init(metadataMap, lock, tm);
    }

    @AfterClass
    public void tearDown() throws Exception {
        metadataMap.clear();
        Grid.getInstance().close();
        FileUtils.deleteDirectory(new File(path));
    }

}
