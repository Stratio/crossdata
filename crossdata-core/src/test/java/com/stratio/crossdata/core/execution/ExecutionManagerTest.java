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
package com.stratio.crossdata.core.execution;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ExecutionManagerTest /*extends MetadataManagerTestHelper*/ {

    @Test
    public void testExist() throws Exception {
        ExecutionManager.MANAGER.createEntry("key", "testKey");
        ExecutionManager.MANAGER.createEntry("key", "testKey", true);
        Assert.assertTrue(ExecutionManager.MANAGER.exists("key"));

    }

    @Test
    public void testClear() throws Exception {
        ExecutionManager.MANAGER.createEntry("key", "testKey");
        ExecutionManager.MANAGER.clear();
        Assert.assertFalse(ExecutionManager.MANAGER.exists("key"));
    }

    @Test
    public void testClearWithDelete() throws Exception {
        ExecutionManager.MANAGER.createEntry("key", "testKey");
        ExecutionManager.MANAGER.deleteEntry("key");
        Assert.assertFalse(ExecutionManager.MANAGER.exists("key"));
    }

    @Test
    public void testGetValue() throws Exception {
        ExecutionManager.MANAGER.createEntry("key2", "testKey");
        Assert.assertEquals(ExecutionManager.MANAGER.getValue("key2"), "testKey");
    }
}
