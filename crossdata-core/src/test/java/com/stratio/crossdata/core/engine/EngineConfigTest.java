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

import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.core.MetadataManagerTestHelper;

public class EngineConfigTest {

    @BeforeClass(dependsOnGroups = {"CoordinatorTest"})
    public void setUp() throws Exception {
        MetadataManagerTestHelper.HELPER.initHelper();
    }

    @Test
    public void testToString() throws Exception {
        String path = "/tmp/com.stratio.crossdata-test-" + new Random().nextInt(100000);
        EngineConfig engineConfig = new EngineConfig();
        engineConfig.setGridContactHosts(new String[]{"localhost"});
        engineConfig.setGridJoinTimeout(5000);
        engineConfig.setGridListenAddress("localhost");
        engineConfig.setGridMinInitialMembers(1);
        engineConfig.setGridPersistencePath(path);
        engineConfig.setGridPort(7810);
        String result = engineConfig.toString();
        StringBuilder sb = new StringBuilder("EngineConfig{");
        sb.append(", gridListenAddress='").append("localhost").append('\'');
        sb.append(", gridContactHosts=").append(Arrays.toString(new String[]{"localhost"}));
        sb.append(", gridPort=").append(7810);
        sb.append(", gridMinInitialMembers=").append(1);
        sb.append(", gridJoinTimeout=").append(5000);
        sb.append(", gridPersistencePath='").append(path).append('\'');
        sb.append('}');
        String expected = sb.toString();
        assertTrue(result.equalsIgnoreCase(expected),
                "Result:   " + System.lineSeparator() +
                "Expected: ");
    }

    @AfterClass(groups = {"EngineConfigTest"})
    public void tearDown() throws Exception {
        System.out.println(this.getClass().getCanonicalName() + ": tearDown");
    }
}
