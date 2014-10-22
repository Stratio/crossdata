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
}
