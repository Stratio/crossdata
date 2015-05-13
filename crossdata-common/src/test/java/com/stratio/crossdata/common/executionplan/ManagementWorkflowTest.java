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

package com.stratio.crossdata.common.executionplan;

import java.util.HashSet;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ManagementWorkflowTest {

    @Test
    public void settingWorkflowDetachConnectorTest() {
        ManagementWorkflow mw = new ManagementWorkflow("queryId", new HashSet<String>(),
                ExecutionType.DETACH_CONNECTOR, ResultType.RESULTS);

        Assert.assertEquals(mw.createManagementOperationMessage().queryId(), "queryId", "Error in queryId validation");

    }

    @Test
    public void settingWorkflowForceDetachConnectorTest() {
        ManagementWorkflow mw = new ManagementWorkflow("queryId", new HashSet<String>(),
                ExecutionType.FORCE_DETACH_CONNECTOR, ResultType.RESULTS);

        Assert.assertEquals(mw.createManagementOperationMessage().queryId(), "queryId", "Error in queryId validation");

    }

    @Test
    public void settingWorkflowAlterClusterTest() {
        ManagementWorkflow mw = new ManagementWorkflow("queryId", new HashSet<String>(),
                ExecutionType.ALTER_CLUSTER, ResultType.RESULTS);

        Assert.assertEquals(mw.createManagementOperationMessage().queryId(), "queryId", "Error in queryId validation");

    }


}
