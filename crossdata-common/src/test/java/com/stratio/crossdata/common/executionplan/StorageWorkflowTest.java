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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.validation.CoordinationException;
import com.stratio.crossdata.communication.MetadataOperation;
import com.stratio.crossdata.communication.StorageOperation;

public class StorageWorkflowTest {

    @Test
    public void storageInsertTest(){
        StorageWorkflow sw=new StorageWorkflow("queryId","ActorRef",ExecutionType.INSERT,ResultType.RESULTS);
        StorageOperation so=null;
        try {
            so=sw.getStorageOperation();
        } catch (CoordinationException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(so.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void storageInsertBatchTest(){
        StorageWorkflow sw=new StorageWorkflow("queryId","ActorRef",ExecutionType.INSERT_BATCH,ResultType.RESULTS);
        StorageOperation so=null;
        try {
            so=sw.getStorageOperation();
        } catch (CoordinationException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(so.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void storageDeleteTest(){
        StorageWorkflow sw=new StorageWorkflow("queryId","ActorRef",ExecutionType.DELETE_ROWS,ResultType.RESULTS);
        StorageOperation so=null;
        try {
            so=sw.getStorageOperation();
        } catch (CoordinationException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(so.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void storageUpdateTest(){
        StorageWorkflow sw=new StorageWorkflow("queryId","ActorRef",ExecutionType.UPDATE_TABLE,ResultType.RESULTS);
        StorageOperation so=null;
        try {
            so=sw.getStorageOperation();
        } catch (CoordinationException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(so.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void storageTruncateTest(){
        StorageWorkflow sw=new StorageWorkflow("queryId","ActorRef",ExecutionType.TRUNCATE_TABLE,ResultType.RESULTS);
        StorageOperation so=null;
        try {
            so=sw.getStorageOperation();
        } catch (CoordinationException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(so.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void storageOtherTest(){
        Boolean res;
        StorageWorkflow sw=new StorageWorkflow("queryId","ActorRef",ExecutionType.DETACH_CLUSTER,ResultType.RESULTS);

        try {
            sw.getStorageOperation();
            Assert.fail("It is not possible to do an storage workflow with a DETACH CLUSTER operation.");
        } catch (CoordinationException e) {
            res=true;
            Assert.assertTrue(res);
        }
        
    }


}
