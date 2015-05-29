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

package com.stratio.crossdata.driver;

import akka.actor.ActorSelection;
import com.stratio.connector.inmemory.InMemoryConnector;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.result.*;
import com.stratio.crossdata.connectors.ConnectorApp;
import com.stratio.crossdata.server.CrossdataServer;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;

import static org.testng.Assert.*;

public class BasicDriverPlanningIT {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(BasicDriverPlanningIT.class);

    private CrossdataServer server;
    private ConnectorApp connector;
    private ConnectorApp connectorFake;
    private BasicDriver driver;

    @BeforeClass
    public void setUp() throws Exception {
        server = new CrossdataServer();
        server.init(null);
        server.start();
        Thread.sleep(4000);
        connector = new ConnectorApp();
        connectorFake = new ConnectorApp();
        InMemoryConnector inMemoryConnector = new InMemoryConnector(connector);
        InMemoryConnectorFake inMemoryConnectorFake = new InMemoryConnectorFake(connectorFake);
        ActorSelection actorSelection = connector.startup(inMemoryConnector);
        ActorSelection actorSelectionFake = connectorFake.startup(inMemoryConnectorFake);
        Thread.sleep(4000);
    }

    @AfterClass
    public void clean(){
        driver.resetServerdata("testSession");

    }

    @Test(timeOut = 8000)
    public void testConnect() throws Exception {
        driver = new BasicDriver();
        driver.setUserName("ITtest");
        Result result = driver.connect(driver.getUserName(), driver.getPassword());

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), ConnectResult.class, "ConnectResult was expected");
        ConnectResult connectResult = (ConnectResult) result;
        assertNotNull(connectResult.getSessionId(), "Server returned a null session identifier");
        LOG.info(connectResult.getSessionId());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testConnect"})
    public void testResetServerdata() throws Exception {
        Thread.sleep(500);
        Result result = driver.resetServerdata("testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), CommandResult.class, "CommandResult was expected");
        CommandResult commandResult = (CommandResult) result;
        assertNotNull(commandResult.getResult(), "Server returned a null object");
        LOG.info(commandResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testResetServerdata"})
    public void testAddDatastore() throws Exception {
        Thread.sleep(500);
        URL url = this.getClass().getClassLoader().getResource("InMemoryDataStore.xml");
        //URL url = Thread.currentThread().getContextClassLoader().getResource("InMemoryDataStore.xml");
        //String path = url.getPath().replace("crossdata-driver", "crossdata-connector-inmemory");
        String path = url.getPath();
        Result result = driver.addManifest(CrossdataManifest.TYPE_DATASTORE, path, "testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), CommandResult.class, "CommandResult was expected");
        CommandResult commandResult = (CommandResult) result;
        assertNotNull(commandResult.getResult(), "Server returned a null object");
        LOG.info(commandResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testAddDatastore"})
    public void testAttachCluster() throws Exception {
        Thread.sleep(500);
        Result result = driver.executeQuery("ATTACH CLUSTER InMemoryCluster ON DATASTORE InMemoryDatastore WITH " +
                "OPTIONS {'TableRowLimit': 100};","testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), CommandResult.class, "CommandResult was expected");
        CommandResult commandResult = (CommandResult) result;
        assertNotNull(commandResult.getResult(), "Server returned a null object");
        LOG.info(commandResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testAttachCluster"})
    public void testAddConnector() throws Exception {
        Thread.sleep(500);
        URL url = Thread.currentThread().getContextClassLoader().getResource("InMemoryConnector.xml");
        //String path = url.getPath().replace("crossdata-driver", "crossdata-connector-inmemory");
        String path = url.getPath();
        Result result = driver.addManifest(CrossdataManifest.TYPE_CONNECTOR, path,"testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), CommandResult.class, "CommandResult was expected");
        CommandResult commandResult = (CommandResult) result;
        assertNotNull(commandResult.getResult(), "Server returned a null object");
        LOG.info(commandResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testAddConnector"})
    public void testAttachConnector() throws Exception {
        Thread.sleep(500);
        Result result = driver.executeQuery("ATTACH CONNECTOR InMemoryConnector TO InMemoryCluster;","testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), ConnectResult.class, "CommandResult was expected");
        ConnectResult connectResult = (ConnectResult) result;
        assertNotNull(connectResult.getSessionId(), "Server returned a null session identifier");
        LOG.info(connectResult.getSessionId());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testAttachConnector"})
    public void testCreateCatalog() throws Exception {
        Thread.sleep(500);
        Result result = driver.executeQuery("CREATE CATALOG catalogTest;","testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), MetadataResult.class, "CommandResult was expected");
        MetadataResult metadataResult = (MetadataResult) result;
        assertNotNull(metadataResult.getOperation(), "Server returned a null object");
        LOG.info(metadataResult.getOperation());
        Thread.sleep(500);
        driver.setCurrentCatalog("catalogTest");
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testCreateCatalog"})
    public void testCreateTable() throws Exception {
        Thread.sleep(500);
        Result result = driver.executeQuery("CREATE TABLE tableTest ON CLUSTER InMemoryCluster" +
                " (id INT PRIMARY KEY, name TEXT, description TEXT, rating FLOAT);","testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), MetadataResult.class, "CommandResult was expected");
        MetadataResult metadataResult = (MetadataResult) result;
        assertNotNull(metadataResult.getOperation(), "Server returned a null object");
        LOG.info(metadataResult.getOperation());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testCreateTable"})
    public void testInsert1() throws Exception {
        Thread.sleep(500);
        Result result = driver.executeQuery("INSERT INTO tableTest(id, name, description, rating) VALUES (1, 'stratio1', 'Big Data', 5.0);","testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), StorageResult.class, "StorageResult was expected");
        StorageResult storageResult = (StorageResult) result;
        assertNotNull(storageResult.getResult(), "Server returned a null object");
        LOG.info(storageResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert1"})
    public void testInsert2() throws Exception {
        Result result = driver.executeQuery("INSERT INTO tableTest(id, name, description, rating) VALUES (2, 'stratio2', 'Crossdata', 8.5);","testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), StorageResult.class, "StorageResult was expected");
        StorageResult storageResult = (StorageResult) result;
        assertNotNull(storageResult.getResult(), "Server returned a null object");
        LOG.info(storageResult.getResult());
    }


    @Test(timeOut = 8000, dependsOnMethods = {"testInsert2"})
    public void testSyncSelect() throws Exception {
        Thread.sleep(500);
        TestResultHandler testResultHandler = new TestResultHandler(2);
        Result result = driver.executeQuery("SELECT * FROM tableTest;", "testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertNotNull(result.getQueryId(), "Query Identifier can't be null");
        assertTrue(result instanceof QueryResult, "The result should be an instance of QueryResult");

        assertEquals(( (QueryResult) result).getResultSet().getRows().size(),2 , "The connector should return two rows");
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testSyncSelect"})
    public void testAddFakeConnector() throws Exception {
        Thread.sleep(500);
        URL url = Thread.currentThread().getContextClassLoader().getResource("InMemoryConnectorFake.xml");
        //String path = url.getPath().replace("crossdata-driver", "crossdata-connector-inmemory");
        String path = url.getPath();
        Result result = driver.addManifest(CrossdataManifest.TYPE_CONNECTOR, path,"testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), CommandResult.class, "CommandResult was expected");
        CommandResult commandResult = (CommandResult) result;
        assertNotNull(commandResult.getResult(), "Server returned a null object");
        LOG.info(commandResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testAddFakeConnector"})
    public void testAttachFakeConnector() throws Exception {
        Thread.sleep(500);
        Result result = driver.executeQuery("ATTACH CONNECTOR InMemoryConnectorFake TO InMemoryCluster AND PRIORITY = 1;","testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), ConnectResult.class, "CommandResult was expected");
        ConnectResult connectResult = (ConnectResult) result;
        assertNotNull(connectResult.getSessionId(), "Server returned a null session identifier");
        LOG.info(connectResult.getSessionId());
    }

    /**
     * The query is executed in the fake connector. After it fails the coordinator should retry the query in the real connector.
     */
    @Test(timeOut = 8000, dependsOnMethods = {"testAttachFakeConnector"})
    public void testFakeSyncSelect() throws Exception {
        Thread.sleep(500);
        TestResultHandler testResultHandler = new TestResultHandler(2);
        Result result = driver.executeQuery("SELECT * FROM tableTest;", "testSession");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertNotNull(result.getQueryId(), "Query Identifier can't be null");
        assertTrue(result instanceof QueryResult, "The result should be an instance of QueryResult");

        assertEquals(( (QueryResult) result).getResultSet().getRows().size(),2 , "The connector should return two rows");
    }

    @AfterClass
    public void tearDown() throws Exception {
        connector.stop();
        server.stop();
        server.destroy();
    }

}
