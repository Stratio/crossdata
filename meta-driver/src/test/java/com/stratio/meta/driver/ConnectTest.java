/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.driver;

import static org.testng.Assert.*;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;

public class ConnectTest extends DriverParentTest {

  private final static Logger logger = Logger.getLogger(ConnectTest.class);

  @BeforeClass
  public void executeDropTestBefore() {

    try {
      driver.executeQuery("ks_demo", "drop table demo ;");
    } catch (Exception e) {
      logger.info("Not removing table demo as it does not exists.");
    }

    try {
      driver.executeQuery("ks_demo", "drop keyspace ks_demo ;");
    } catch (Exception e) {
      logger.info("Not removing ks_demo as it does not exists.");
    }

    String msg =
        "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};";
    try {
      driver.executeQuery( "ks_demo", msg);
    } catch (Exception e) {
      System.err.println("Cannot create test keyspace.");
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

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



  @Test(groups = {"query", "create Ks"}, dependsOnGroups = {"connect"})
  public void ExecuteCreatewitherrorTest() {
    String msg = "create KEYSPAC ks_demo WITH replication = "
                 + "{class: SimpleStrategy, replication_factor: 1};";
    try {
      driver.executeQuery("ks_demo", msg);
      fail("Expecting ParsingException");
    } catch (ParsingException e) {
      e.printStackTrace();
    } catch (Exception e){
      e.printStackTrace();
      fail("Expecting ParsingException");
    }
  }

  @Test(groups = {"query", "use"}, dependsOnGroups = {"create Ks"})
  public void executeUseKsest() {
    String msg = "use ks_demo ;";
    Result metaResult = null;
    try {
      metaResult = driver.executeQuery("ks_demo", msg);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception not expected");
    }
    assertTrue(QueryResult.class.isInstance(metaResult), "Invalid result type");
    QueryResult r = QueryResult.class.cast(metaResult);
    assertTrue(r.isCatalogChanged(), "New keyspace should be used");
    assertEquals(r.getCurrentCatalog(), "ks_demo", "New keyspace should be used");
  }

  @Test(groups = {"query", "create Tb"}, dependsOnGroups = {"use"})
  public void executeCreateTableTest() {
    String msg = "create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);";
    Result metaResult = null;
    try {
      metaResult = driver.executeQuery("ks_demo", msg);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception not expected");
    }
    assertFalse(metaResult.hasError(),
                "\n\nerror message is:\n" + getErrorMessage(metaResult) + "\n\n");
  }

  @Test(groups = {"query", "insert"}, dependsOnGroups = {"create Tb"})
  public void executeInsertTest() {
    String msg = "insert into demo (field1, field2) values ('test1','text2');";
    Result metaResult = null;
    try {
      metaResult = driver.executeQuery("ks_demo", msg);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception not expected");
    }
    assertFalse(metaResult.hasError(),
                "\n\nerror message is:\n" + getErrorMessage(metaResult) + "\n\n");
  }

  @Test(groups = "disconnect", dependsOnGroups = {"query"})
  public void disconnect() {
    try {
      driver.disconnect();
    } catch (ConnectionException e) {
      e.printStackTrace();
      fail("Exception not expected");
    }
  }
}
