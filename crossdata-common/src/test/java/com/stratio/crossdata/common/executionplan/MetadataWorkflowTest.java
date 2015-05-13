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

import com.stratio.crossdata.communication.MetadataOperation;

public class MetadataWorkflowTest {

    @Test
    public void createMetadataOperationAlterCatalogTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.ALTER_CATALOG,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationCreateCatalogTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.CREATE_CATALOG,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationDropCatalogTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.DROP_CATALOG,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationCreateTableTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.CREATE_TABLE,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationCreateTableCatalogTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.CREATE_TABLE_AND_CATALOG,
                ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationCreateTableRegisterCatalogTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.CREATE_TABLE_REGISTER_CATALOG,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationDropTableTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.DROP_TABLE,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationAlterTableTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.ALTER_TABLE,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationCreateIndexTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.CREATE_INDEX,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationDropIndexTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.DROP_INDEX,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationDiscoverMetadataTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.DISCOVER_METADATA,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationImportCatalogTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.IMPORT_CATALOG,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationImportCatalogsTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.IMPORT_CATALOGS,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }

    @Test
    public void createMetadataOperationImportTableTest(){
        MetadataWorkflow mw=new MetadataWorkflow("queryId","ActorRef",ExecutionType.IMPORT_TABLE,ResultType.RESULTS);
        MetadataOperation mo=mw.createMetadataOperationMessage();
        Assert.assertEquals(mo.queryId(),"queryId", "Error on validation of queryId");
    }
}
