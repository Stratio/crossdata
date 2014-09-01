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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.ConnectionException;

/**
 * Asynchronous interface tests.
 */
public class AsyncDriverTest extends DriverParentTest {

  private final static Logger logger = Logger.getLogger(AsyncDriverTest.class);

  @Test
  public void basicSelect() {
    String query = "select * from system.schema_columns;";
    ResultHandlerWrapper rhw = new ResultHandlerWrapper();
    try {
      driver.asyncExecuteQuery("", query, rhw);
    } catch (ConnectionException e) {
      fail("Connect problem");
      logger.error(e);
    }
    try {
      Thread.sleep(9000);
    } catch (InterruptedException e) {
      logger.error(e);
    }
    // waitForAnswer();
    assertTrue(rhw.isAckReceived(), "ACK has not been received.");
    assertFalse(rhw.isErrorReceived(), "No error expected.");
    assertTrue(rhw.isResultReceived(), "Result has not been received.");
  }
}
