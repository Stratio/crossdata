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

package com.stratio.crossdata.common.connector;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ClusterName;

public class ConnectorClusterConfigTest {

    @Test
    public void testConnectorClusterConfig() throws Exception {
        Map<String, String> options = new HashMap<>();

        ConnectorClusterConfig connectorClusterConfig = new ConnectorClusterConfig(new ClusterName("myCluster"),
                options);

        Assert.assertTrue(connectorClusterConfig.getName().getName().equals("mycluster"));
        Assert.assertTrue(connectorClusterConfig.getOptions().isEmpty());
    }
}
