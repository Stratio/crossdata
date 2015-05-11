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

package com.stratio.crossdata.common.metadata;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelectorTest;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.common.utils.Constants;

public class ConnectorAttachedMetadataTest {

    private ConnectorAttachedMetadata cam;

    @BeforeClass
    private void createConnectorAttachedMetadata(){
        ConnectorName connectorRef = new ConnectorName("testConnector");
        ClusterName clusterRef = new ClusterName("testCluster");
        Map<Selector, Selector> properties = new HashMap<>();
        Integer priority = Constants.DEFAULT_PRIORITY;
        properties.put(new StringSelector("DefaultLimit"), new IntegerSelector(100));
        cam = new ConnectorAttachedMetadata(connectorRef, clusterRef, properties, priority);
    }

    @Test
    public void testGetConnectorRef() throws Exception {
        ConnectorName expected = new ConnectorName("testConnector");
        assertTrue(expected.equals(cam.getConnectorRef()),
                System.lineSeparator() +
                "Result:   " + cam.getConnectorRef() + System.lineSeparator() +
                "Expected: " + expected);
    }

    @Test
    public void testGetClusterRef() throws Exception {
        ClusterName expected = new ClusterName("testCluster");
        assertTrue(expected.equals(cam.getClusterRef()),
                System.lineSeparator() +
                "Result:   " + cam.getConnectorRef() + System.lineSeparator() +
                "Expected: " + expected);
    }

    @Test
    public void testGetProperties() throws Exception {
        Map<Selector, Selector> expected = new HashMap<>();
        expected.put(new StringSelector("DefaultLimit"), new IntegerSelector(100));
        assertTrue(expected.equals(cam.getProperties()),
                System.lineSeparator() +
                "Result:   " + cam.getConnectorRef() + System.lineSeparator() +
                "Expected: " + expected);
    }

    @Test
    public void testGetPriority() throws Exception {
        Integer expected = Constants.DEFAULT_PRIORITY;
        assertTrue(expected.equals(cam.getPriority()),
                        System.lineSeparator() +
                                        "Result:   " + cam.getConnectorRef() + System.lineSeparator() +
                                        "Expected: " + expected);
    }
}
