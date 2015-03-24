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

import java.net.URL;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.connector.inmemory.InMemoryConnector;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ConnectResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.connectors.ConnectorApp;
import com.stratio.crossdata.server.CrossdataServer;

public class BasicDriverIT {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(BasicDriverIT.class);

    private CrossdataServer server;
    private ConnectorApp connector;
    private BasicDriver driver;

    @BeforeClass
    public void setUp() throws Exception {
        server = new CrossdataServer();
        server.init(null);
        server.start();
        Thread.sleep(2000);
        connector = new ConnectorApp();
        InMemoryConnector inMemoryConnector = new InMemoryConnector(connector);
        connector.startup(inMemoryConnector);
    }

    @Test(timeOut = 8000)
    public void testConnect() throws Exception {
        driver = new BasicDriver();
        driver.setUserName("ITtest");
        Result result = driver.connect(driver.getUserName());

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
        Result result = driver.resetServerdata();

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
        URL url = Thread.currentThread().getContextClassLoader().getResource("InMemoryDataStore.xml");
        String path = url.getPath().replace("crossdata-driver", "crossdata-connector-inmemory");
        Result result = driver.addManifest(CrossdataManifest.TYPE_DATASTORE, path);

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
        Result result = driver.executeQuery("ATTACH CLUSTER InMemoryCluster ON DATASTORE InMemoryDatastore WITH OPTIONS {'TableRowLimit': 100};");

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
        URL url = Thread.currentThread().getContextClassLoader().getResource("InMemoryConnector.xml");
        String path = url.getPath().replace("crossdata-driver", "crossdata-connector-inmemory");
        Result result = driver.addManifest(CrossdataManifest.TYPE_CONNECTOR, path);

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
        Result result = driver.executeQuery("ATTACH CONNECTOR InMemoryConnector TO InMemoryCluster AND PAGINATION = 5;");

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
        Result result = driver.executeQuery("CREATE CATALOG catalogTest;");

        if(result instanceof ErrorResult){
            LOG.error(((ErrorResult) result).getErrorMessage());
        }
        assertFalse(result.hasError(), "Server returned an error");
        assertEquals(result.getClass(), MetadataResult.class, "CommandResult was expected");
        MetadataResult metadataResult = (MetadataResult) result;
        assertNotNull(metadataResult.getOperation(), "Server returned a null object");
        LOG.info(metadataResult.getOperation());
        driver.setCurrentCatalog("catalogTest");
    }

    @AfterClass
    public void tearDown() throws Exception {
        driver.resetServerdata();
        connector.stop();
        server.stop();
        server.destroy();
    }

}
