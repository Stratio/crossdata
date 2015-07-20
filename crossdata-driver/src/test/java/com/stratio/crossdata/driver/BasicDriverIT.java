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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.connector.inmemory.InMemoryConnector;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ConnectToConnectorResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.InProgressResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.result.StorageResult;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.connectors.ConnectorApp;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.server.CrossdataServer;

import akka.actor.ActorRef;

public class BasicDriverIT {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(BasicDriverIT.class);

    private CrossdataServer server;
    private ConnectorApp connector;
    private BasicDriver driver;
    private DriverConnection xdConnection;

    @BeforeClass
    public void setUp() throws Exception {
        server = new CrossdataServer();
        server.init(null);
        server.start();
        Thread.sleep(4000);
        connector = new ConnectorApp();
        InMemoryConnector inMemoryConnector = new InMemoryConnector(connector);
        ActorRef actorSelection = connector.startup(inMemoryConnector).get();
        Thread.sleep(4000);
    }

    @Test(timeOut = 8000)
    public void testConnect() throws Exception {
        driver = new BasicDriver();
        driver.setUserName("ITtest");
        xdConnection = driver.connect(driver.getUserName(), driver.getPassword());
        assertNotNull(xdConnection, "Driver connection shouldn't be null");
        assertNotNull(xdConnection.sessionId(), "Server returned a null session identifier");
        LOG.info(xdConnection.sessionId());

    }

    @Test(timeOut = 8000, dependsOnMethods = {"testConnect"})
    public void testResetServerdata() throws Exception {
        Thread.sleep(500);
        Result result = xdConnection.resetServerdata();

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
        Result result = xdConnection.addManifest(CrossdataManifest.TYPE_DATASTORE, path);

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
        Map<String, Object> options = new HashMap<>();
        options.put("TableRowLimit", 100);
        Result result = xdConnection.attachCluster("InMemoryCluster", "InMemoryDatastore", false,  options);

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
        Result result = xdConnection.addManifest(CrossdataManifest.TYPE_CONNECTOR, path);

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

        Result result = xdConnection.attachConnector("InMemoryConnector", "InMemoryCluster",
                        Collections.<String, Object>emptyMap(), 5, Constants.DEFAULT_PRIORITY);

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), ConnectToConnectorResult.class, "CommandResult was expected");
        ConnectToConnectorResult connectResult = (ConnectToConnectorResult) result;
        assertNotNull(connectResult.getQueryId(), "Server returned a null session identifier");
        LOG.info(connectResult.getQueryId());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testAttachConnector"})
    public void testCreateCatalog() throws Exception {
        Thread.sleep(500);
        Result result = xdConnection.createCatalog("catalogTest", false, Collections.<String,Object>emptyMap());

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), MetadataResult.class, "CommandResult was expected");
        MetadataResult metadataResult = (MetadataResult) result;
        assertNotNull(metadataResult.getOperation(), "Server returned a null object");
        LOG.info(metadataResult.getOperation());
        Thread.sleep(500);
        xdConnection.setCurrentCatalog("catalogTest");
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testCreateCatalog"})
    public void testCreateTable() throws Exception {
        Thread.sleep(500);
        Map<String, String> colTypes = new HashMap<>();
        colTypes.put("id", "INT");
        colTypes.put("name", "TEXT");
        colTypes.put("description", "TEXT");
        colTypes.put("rating", "FLOAT");

        Result result = xdConnection.createTable("catalogTest", "tableTest", "InMemoryCluster", colTypes, Arrays.asList("id"),Collections.<String>emptyList(), false, false, Collections.<String, Object>emptyMap());

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
        Map<String, Object> colValues = new HashMap<>();
        colValues.put("id", 1);
        colValues.put("name", "stratio1");
        colValues.put("description", "Big Data");
        colValues.put("rating", 5.0f);

        Result result = xdConnection.insert("catalogTest", "tableTest", colValues, false, Collections.<String, Object>emptyMap());

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
        Map<String, Object> colValues = new HashMap<>();
        colValues.put("id", 2);
        colValues.put("name", "stratio2");
        colValues.put("description", "Crossdata");
        colValues.put("rating", 8.5f);

        Result result = xdConnection.insert("catalogTest", "tableTest", colValues, false, Collections.<String, Object>emptyMap());

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
    public void testInsert3() throws Exception {

        Map<String, Object> colValues = new HashMap<>();
        colValues.put("id", 3);
        colValues.put("name", "stratio3");
        colValues.put("description", "One framework to rule all the databases");
        colValues.put("rating", 4.0f);

        Result result = xdConnection.insert("catalogTest", "tableTest", colValues, false, Collections.<String, Object>emptyMap());

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), StorageResult.class, "StorageResult was expected");
        StorageResult storageResult = (StorageResult) result;
        assertNotNull(storageResult.getResult(), "Server returned a null object");
        LOG.info(storageResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert3"})
    public void testInsert4() throws Exception {
        Map<String, Object> colValues = new HashMap<>();
        colValues.put("id", 4);
        colValues.put("name", "worker");
        colValues.put("description", "Happy");
        colValues.put("rating", 9.2f);

        Result result = xdConnection.insert("catalogTest", "tableTest", colValues, false, Collections.<String, Object>emptyMap());

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), StorageResult.class, "StorageResult was expected");
        StorageResult storageResult = (StorageResult) result;
        assertNotNull(storageResult.getResult(), "Server returned a null object");
        LOG.info(storageResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert4"})
    public void testInsert5() throws Exception {

        Map<String, Object> colValues = new HashMap<>();
        colValues.put("id", 5);
        colValues.put("name", "worker");
        colValues.put("description", "Learning");
        colValues.put("rating", 6.5f);

        Result result = xdConnection.insert("catalogTest", "tableTest", colValues, false, Collections.<String, Object>emptyMap());

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), StorageResult.class, "StorageResult was expected");
        StorageResult storageResult = (StorageResult) result;
        assertNotNull(storageResult.getResult(), "Server returned a null object");
        LOG.info(storageResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert5"})
    public void testInsert6() throws Exception {

        Result result = xdConnection.executeQuery("INSERT INTO tableTest(id, name, description, rating) VALUES (6, 'employee', 'Working', 7.0);");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), StorageResult.class, "StorageResult was expected");
        StorageResult storageResult = (StorageResult) result;
        assertNotNull(storageResult.getResult(), "Server returned a null object");
        LOG.info(storageResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert6"})
    public void testInsert7() throws Exception {
        Result result = xdConnection.executeQuery("INSERT INTO tableTest(id, name, description, rating) VALUES (7, " +
                "'employee', 'Improving', 2);");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), StorageResult.class, "StorageResult was expected");
        StorageResult storageResult = (StorageResult) result;
        assertNotNull(storageResult.getResult(), "Server returned a null object");
        LOG.info(storageResult.getResult());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert7"})
    public void testAsyncSelect() throws Exception {
        Thread.sleep(500);
        TestResultHandler testResultHandler = new TestResultHandler(2);
        Result result = xdConnection.asyncExecuteQuery("SELECT * FROM tableTest;", testResultHandler);

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), InProgressResult.class, "InProgressResult was expected");
        InProgressResult inProgressResult = (InProgressResult) result;
        assertNotNull(inProgressResult.getQueryId(), "Query Identifier can't be null");
        LOG.info(inProgressResult.getQueryId() + " in progress");

        while(!testResultHandler.isResultAvailable()){
            Thread.sleep(500);
            if(testResultHandler.isResultAvailable()){
                assertTrue(testResultHandler.wasSuccessfully(), "testAsyncSelect failed");
            }
        }
        xdConnection.removeResultHandler(result.getQueryId());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert7"})
    public void testAsyncSelectFail1() throws Exception {
        Thread.sleep(500);
        TestResultHandler testResultHandler = new TestResultHandler(1);
        Result result = xdConnection.asyncExecuteQuery("SELECT * FROM tableTest;", testResultHandler);

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), InProgressResult.class, "InProgressResult was expected");
        InProgressResult inProgressResult = (InProgressResult) result;
        assertNotNull(inProgressResult.getQueryId(), "Query Identifier can't be null");
        LOG.info(inProgressResult.getQueryId() + " in progress");

        while(!testResultHandler.isResultAvailable()){
            Thread.sleep(500);
            if(testResultHandler.isResultAvailable()){
                assertFalse(testResultHandler.wasSuccessfully(), "testAsyncSelect failed");
            }
        }
        xdConnection.removeResultHandler(result.getQueryId());
    }

    @Test(timeOut = 8000, dependsOnMethods = {"testInsert7"})
    public void testAsyncSelectFail2() throws Exception {
        Thread.sleep(500);
        TestResultHandler testResultHandler = new TestResultHandler(3);
        Result result = xdConnection.asyncExecuteQuery("SELECT * FROM tableTest;", testResultHandler);

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), InProgressResult.class, "InProgressResult was expected");
        InProgressResult inProgressResult = (InProgressResult) result;
        assertNotNull(inProgressResult.getQueryId(), "Query Identifier can't be null");
        LOG.info(inProgressResult.getQueryId() + " in progress");

        while(!testResultHandler.isResultAvailable()){
            Thread.sleep(500);
            if(testResultHandler.isResultAvailable()){
                assertFalse(testResultHandler.wasSuccessfully(), "testAsyncSelect failed");
            }
        }
        xdConnection.removeResultHandler(result.getQueryId());
    }

    @Test(timeOut = 8000, dependsOnMethods = { "testInsert7" })
    public void testSyncPaginatedSelect() throws Exception {
        Thread.sleep(500);
        Result result = xdConnection.executeQuery("SELECT * FROM tableTest;", true);

        if (result instanceof ErrorResult) {
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertTrue(result instanceof QueryResult, "Driver should return a query result");
        QueryResult queryResult = (QueryResult) result;
        assertTrue(queryResult.isLastResultSet(), "Result should be the last result");
        assertEquals(7, queryResult.getResultSet().size(), "The result should contain 7 result");

    }

    @AfterClass
    public void tearDown() throws Exception {
        xdConnection.resetServerdata();
        connector.stop();
        MetadataManager.MANAGER.clear();
        driver.close();
        server.stop();
        server.destroy();
    }

}
