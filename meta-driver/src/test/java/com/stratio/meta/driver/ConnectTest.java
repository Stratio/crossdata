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

package com.stratio.meta.driver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.ManifestException;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta2.common.result.Result;
import com.stratio.meta2.common.api.datastore.DataStoreType;

public class ConnectTest extends DriverParentTest {

    private final static Logger logger = Logger.getLogger(ConnectTest.class);

   /* @BeforeClass
    public void executeDropTestBefore() {

        try {
            driver.setCurrentCatalog("ks_demo");
            driver.executeQuery("drop table demo ;");
        } catch (Exception e) {
            logger.info("Not removing table demo as it does not exists.");
        }

        try {
            driver.setCurrentCatalog("ks_demo");
            driver.executeQuery("drop keyspace ks_demo ;");
        } catch (Exception e) {
            logger.info("Not removing ks_demo as it does not exists.");
        }

        String msg =
                "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};";
        try {
            driver.setCurrentCatalog("ks_demo");
            driver.executeQuery(msg);
        } catch (Exception e) {
            System.err.println("Cannot create test keyspace.");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
*/
    @Test(groups = "connect")
    public void connect() {

        Result metaResult = null;
        try {
            metaResult = driver.connect("TEST_USER");
        } catch (ConnectionException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
        assertFalse(metaResult.hasError());
        ConnectResult r = ConnectResult.class.cast(metaResult);
        assertTrue(r.getSessionId() != null, "Invalid session identifier: " + r.getSessionId());

    }

    @Test(dependsOnGroups = { "connect" })
    public void sendManifest() {
        Result metaResult = null;

        // Create Manifest
        DataStoreType manifest = new DataStoreType();
        /*manifest.setName("string_name");
        manifest.setVersion("0.0.1");
        RequiredPropertiesType rp = new RequiredPropertiesType();
        ClusterType ct = new ClusterType();
        ct.setName("string_host");
        rp.setCluster(ct);
        manifest.setRequiredProperties(rp);
        OptionalPropertiesType op = new OptionalPropertiesType();
        manifest.setOptionalProperties(op);*/

        // API Call
        try {
            metaResult = driver.addManifest(manifest);
        } catch (ManifestException e) {
            logger.error("Manifest name doesn't match LETTER (LETTER | DIGIT | '_')*", e);
            fail("Manifest name doesn't match LETTER (LETTER | DIGIT | '_')*", e);
        }

        // Process result
        assertFalse(metaResult.hasError());
        CommandResult r = CommandResult.class.cast(metaResult);
        assertTrue(((String) r.getResult()).equalsIgnoreCase("OK"),
                "sendManifest: " + System.lineSeparator() + " Cannot add manifest.");
    }
/*
    @Test(groups = { "query", "create Ks" }, dependsOnGroups = { "connect" })
    public void ExecuteCreatewitherrorTest() {
        String msg = "create KEYSPAC ks_demo WITH replication = "
                + "{class: SimpleStrategy, replication_factor: 1};";
        try {
            driver.setCurrentCatalog("ks_demo");
            driver.executeQuery(msg);
            fail("Expecting ParsingException");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Expecting ParsingException");
        }
    }

    @Test(groups = { "query", "use" }, dependsOnGroups = { "create Ks" })
    public void executeUseKsest() {
        String msg = "use ks_demo ;";
        Result metaResult = null;
        try {
            driver.setCurrentCatalog("ks_demo");
            metaResult = driver.executeQuery(msg);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
        assertTrue(QueryResult.class.isInstance(metaResult), "Invalid result type");
        QueryResult r = QueryResult.class.cast(metaResult);
        assertTrue(r.isCatalogChanged(), "New keyspace should be used");
        assertEquals(r.getCurrentCatalog(), "ks_demo", "New keyspace should be used");
    }

    @Test(groups = { "query", "create Tb" }, dependsOnGroups = { "use" })
    public void executeCreateTableTest() {
        String msg = "create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);";
        Result metaResult = null;
        try {
            driver.setCurrentCatalog("ks_demo");
            metaResult = driver.executeQuery(msg);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
        assertFalse(metaResult.hasError(),
                "\n\nerror message is:\n" + getErrorMessage(metaResult) + "\n\n");
    }

    @Test(groups = { "query", "insert" }, dependsOnGroups = { "create Tb" })
    public void executeInsertTest() {
        String msg = "insert into demo (field1, field2) values ('test1','text2');";
        Result metaResult = null;
        try {
            driver.setCurrentCatalog("ks_demo");
            metaResult = driver.executeQuery(msg);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
        assertFalse(metaResult.hasError(),
                "\n\nerror message is:\n" + getErrorMessage(metaResult) + "\n\n");
    }

    @Test(groups = "disconnect", dependsOnGroups = { "query" })
    public void disconnect() {
        try {
            driver.disconnect();
        } catch (ConnectionException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }*/
}
