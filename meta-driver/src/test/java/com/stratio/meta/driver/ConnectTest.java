/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.driver;

import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.Result;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ConnectTest extends DriverParentTest {

  @BeforeClass
  public void ExecuteDropTestBefore() {

    try {
      driver.executeQuery("TEST_USER", "ks_demo", "drop table demo ;");
    } catch (Exception e) {
      System.out.println("Not removing table demo as it does not exists.");
    }

    try {
      driver.executeQuery("TEST_USER", "ks_demo", "drop keyspace ks_demo ;");
    } catch (Exception e) {
      System.out.println("Not removing ks_demo as it does not exists.");
    }

    String
        msg =
        "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};";
    try {
      Result metaResult = driver.executeQuery("TEST_USER", "ks_demo", msg);
    } catch (Exception e) {
      System.err.println("Cannot create test keyspace.");
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

//  @AfterClass
//  public void ExecuteDropTestAfter(){
//    try{
//      driver.executeQuery("TEST_USER","ks_demo","drop table demo ;");
//      driver.executeQuery("TEST_USER","ks_demo","drop keyspace ks_demo ;");
//    } catch (Exception e) {
//      System.out.println("Cannot perform test cleanup.");
//    }
//  }

  @Test
  public void connect() {
    Result metaResult = driver.connect("TEST_USER");
    assertFalse(metaResult.hasError());
    ConnectResult r = ConnectResult.class.cast(metaResult);
    assertTrue(r.getSessionId() != -1, "Invalid session identifier: " + r.getSessionId());
  }

  @Test(groups = "create Ks", expectedExceptions = ParsingException.class)
  public void ExecuteCreatewitherrorTest() {
    String msg = "create KEYSPAC ks_demo WITH replication = "
                 + "{class: SimpleStrategy, replication_factor: 1};";
    Result metaResult = driver.executeQuery("TEST_USER", "ks_demo", msg);
  }

  @Test(groups = "use", dependsOnGroups = {"create Ks"})
  public void ExecuteUseKsest() {
    String msg = "use ks_demo ;";
    Result metaResult = driver.executeQuery("TEST_USER", "ks_demo", msg);
    assertFalse(metaResult.hasError(),
                "\n\nerror message is:\n" + metaResult.getErrorMessage() + "\n\n");
    assertTrue(metaResult.isKsChanged(), "Expecting new keyspace.");
  }

  @Test(groups = "create Tb", dependsOnGroups = {"use"})
  public void ExecuteCreateTableTest() {
    String msg = "create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);";
    Result metaResult = driver.executeQuery("TEST_USER", "ks_demo", msg);
    assertFalse(metaResult.hasError(),
                "\n\nerror message is:\n" + metaResult.getErrorMessage() + "\n\n");
  }

  @Test(groups = "insert", dependsOnGroups = {"create Tb"})
  public void ExecuteInsertTest() {
    String msg = "insert into demo (field1, field2) values ('test1','text2');";
    Result metaResult = driver.executeQuery("TEST_USER", "ks_demo", msg);
    assertFalse(metaResult.hasError(),
                "\n\nerror message is:\n" + metaResult.getErrorMessage() + "\n\n");
  }

}
