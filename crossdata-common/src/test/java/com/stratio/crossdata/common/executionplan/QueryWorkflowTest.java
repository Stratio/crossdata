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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.logicalplan.Window;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.window.WindowType;
import com.stratio.crossdata.communication.AsyncExecute;
import com.stratio.crossdata.communication.Execute;
import com.stratio.crossdata.communication.PagedExecute;

public class QueryWorkflowTest {

    @Test
    public void checkStreamingFalseTest(){

        List<LogicalStep> initialSteps=new ArrayList<>();
        LogicalStep lastStep=null;
        LogicalWorkflow workflow=new LogicalWorkflow(initialSteps, lastStep, 10, new HashMap<Selector, Selector>());
        QueryWorkflow qw=new QueryWorkflow("queryId","actorRef",ExecutionType.SELECT,ResultType.TRIGGER_EXECUTION,
                workflow,true);
        Assert.assertFalse(qw.checkStreaming(lastStep), "It is not possible because the last step is null");
    }

    @Test
    public void checkStreamingTrueTest(){
        LogicalStep step=new Window(new HashSet<Operations>(), WindowType.NUM_ROWS);
        List<LogicalStep> initialSteps=new ArrayList<>();
        LogicalStep lastStep=step;
        LogicalWorkflow workflow=new LogicalWorkflow(initialSteps, lastStep, 10, new HashMap<Selector, Selector>());
        QueryWorkflow qw=new QueryWorkflow("queryId","actorRef",ExecutionType.SELECT,ResultType.TRIGGER_EXECUTION,
                workflow,true);
        Assert.assertTrue(qw.checkStreaming(lastStep), "It has to be a streaming select because the last step is "
                + "Window type.");
    }

    @Test
    public void getExecuteOperationPagedTest(){
        LogicalStep step=new Window(new HashSet<Operations>(), WindowType.NUM_ROWS);
        List<LogicalStep> initialSteps=new ArrayList<>();
        LogicalStep lastStep=step;
        LogicalWorkflow workflow=new LogicalWorkflow(initialSteps, lastStep, 10, new HashMap<Selector, Selector>());
        QueryWorkflow qw=new QueryWorkflow("queryId","actorRef",ExecutionType.SELECT,ResultType.TRIGGER_EXECUTION,
                workflow,true);
        Assert.assertTrue(PagedExecute.class.isInstance(qw.getExecuteOperation("queryId")),
                "The execution operation is a Paged Execute type");
    }

    @Test
    public void getExecuteOperationAsyncTest(){
        LogicalStep step=new Window(new HashSet<Operations>(), WindowType.NUM_ROWS);
        List<LogicalStep> initialSteps=new ArrayList<>();
        LogicalStep lastStep=step;
        LogicalWorkflow workflow=new LogicalWorkflow(initialSteps, lastStep, 0, new HashMap<Selector, Selector>());
        QueryWorkflow qw=new QueryWorkflow("queryId","actorRef",ExecutionType.SELECT,ResultType.TRIGGER_EXECUTION,
                workflow,true);
        Assert.assertTrue(AsyncExecute.class.isInstance(qw.getExecuteOperation("queryId")),
                "The execution operation is an Async Execute type");
    }

    @Test
    public void getExecuteOperationTest(){

        List<LogicalStep> initialSteps=new ArrayList<>();
        LogicalStep lastStep=null;
        LogicalWorkflow workflow=new LogicalWorkflow(initialSteps, lastStep, 0, new HashMap<Selector, Selector>());
        QueryWorkflow qw=new QueryWorkflow("queryId","actorRef",ExecutionType.SELECT,ResultType.RESULTS,
                workflow,false);
        Assert.assertTrue(Execute.class.isInstance(qw.getExecuteOperation("queryId")),
                "The execution operation is a Sync Execute type");
    }
}
