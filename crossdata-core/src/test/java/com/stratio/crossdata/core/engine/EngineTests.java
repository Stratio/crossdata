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

package com.stratio.crossdata.core.engine;

import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.core.grid.Grid;

public class EngineTests {
    private EngineConfig engineConfig;

    @BeforeClass
    public void setUp() {

        engineConfig = new EngineConfig();

        engineConfig.setGridListenAddress("localhost");
        engineConfig.setGridContactHosts(new String[] { });
        engineConfig.setGridMinInitialMembers(1);
        engineConfig.setGridPort(5900);
        engineConfig.setGridJoinTimeout(3000);
        engineConfig.setGridPersistencePath("/tmp/com.stratio.crossdata-test-" + new Random().nextInt(100000));
    }

    @Test(enabled = false)
    public void testGrid() {
        Engine engine = new Engine(engineConfig);
        Map<Object, Object> store = Grid.INSTANCE.map("test");
        store.put("k1", "v1");
        Assert.assertEquals("v1", store.get("k1"));
        store.remove("k1");
        Assert.assertNull(store.get("k1"));
        engine.shutdown();
    }

}
