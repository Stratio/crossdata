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
import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;

public class ConnectTest extends DriverParentTest {

  private final static Logger LOGGER = Logger.getLogger(ConnectTest.class);
  private final String keyspace = "ks_demo";
  private final String exceptionNotExpected = "Exception not expected";
  private final String query = "query";

  @BeforeClass
  public void executeDropTestBefore() {

    try {
      driver.executeQuery(keyspace, "drop table demo ;");
    } catch (ConnectionException | ParsingException | ValidationException | ExecutionException
        | UnsupportedException e) {
      LOGGER.info("Not removing table demo as it does not exists.");
    }

    try {
      driver.executeQuery(keyspace, "drop keyspace ks_demo ;");
    } catch (ConnectionException | ParsingException | ValidationException | ExecutionException
        | UnsupportedException e) {
      LOGGER.info("Not removing ks_demo as it does not exists.");
    }

    String msg =
        "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};";
    try {
      driver.executeQuery(keyspace, msg);
    } catch (ConnectionException | ParsingException | ValidationException | ExecutionException
        | UnsupportedException e) {
      LOGGER.info("Cannot create test keyspace.");
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOGGER.error("Exception", e);
    }

  }

  @Test(groups = "connect")
  public void connect() {

    Result metaResult = null;
    try {
      metaResult = driver.connect("TEST_USER");
    } catch (ConnectionException e) {
      LOGGER.error("Exception", e);
      fail(exceptionNotExpected);
    }
    assertFalse(metaResult.hasError());
    ConnectResult r = ConnectResult.class.cast(metaResult);
    assertTrue(r.getSessionId() != null, "Invalid session identifier: " + r.getSessionId());

  }



  @Test(groups = {query, "create Ks"}, dependsOnGroups = {"connect"})
  public void executeCreatewitherrorTest() {
    String msg =
        "create KEYSPAC ks_demo WITH replication = "
            + "{class: SimpleStrategy, replication_factor: 1};";
    try {
      driver.executeQuery(keyspace, msg);
      fail("Expecting ParsingException");
    } catch (ParsingException e) {
      LOGGER.info(e);
    } catch (ConnectionException | ValidationException | ExecutionException | UnsupportedException e) {
      LOGGER.error("Exception", e);
      fail("Expecting ParsingException");
    }
  }

  @Test(groups = {query, "use"}, dependsOnGroups = {"create Ks"})
  public void executeUseKsest() {
    String msg = "use ks_demo ;";
    Result metaResult = null;
    try {
      metaResult = driver.executeQuery(keyspace, msg);
    } catch (ConnectionException | ParsingException | ValidationException | ExecutionException
        | UnsupportedException e) {
      LOGGER.error("Exception", e);
      fail(exceptionNotExpected);
    }
    assertTrue(QueryResult.class.isInstance(metaResult), "Invalid result type");
    QueryResult r = QueryResult.class.cast(metaResult);
    assertTrue(r.isCatalogChanged(), "New keyspace should be used");
    assertEquals(r.getCurrentCatalog(), "ks_demo", "New keyspace should be used");
  }

  @Test(groups = {query, "create Tb"}, dependsOnGroups = {"use"})
  public void executeCreateTableTest() {
    String msg = "create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);";
    Result metaResult = null;
    try {
      metaResult = driver.executeQuery(keyspace, msg);
    } catch (ConnectionException | ParsingException | ValidationException | ExecutionException
        | UnsupportedException e) {
      LOGGER.error("Exception", e);
      fail(exceptionNotExpected);
    }
    assertFalse(metaResult.hasError(), "\n\nerror message is:\n" + getErrorMessage(metaResult)
        + "\n\n");
  }

  @Test(groups = {query, "insert"}, dependsOnGroups = {"create Tb"})
  public void executeInsertTest() {
    String msg = "insert into demo (field1, field2) values ('test1','text2');";
    Result metaResult = null;
    try {
      metaResult = driver.executeQuery(keyspace, msg);
    } catch (ConnectionException | ParsingException | ValidationException | ExecutionException
        | UnsupportedException e) {
      LOGGER.error("Exception", e);
      fail(exceptionNotExpected);
    }
    assertFalse(metaResult.hasError(), "\n\nerror message is:\n" + getErrorMessage(metaResult)
        + "\n\n");
  }

  @Test(groups = "disconnect", dependsOnGroups = {query})
  public void disconnect() {
    try {
      driver.disconnect();
    } catch (ConnectionException e) {
      LOGGER.error("Exception", e);
      fail(exceptionNotExpected);
    }
  }
}
