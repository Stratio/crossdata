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

package com.stratio.meta.core.validator.statements;


import org.testng.annotations.Test;

import com.stratio.meta.core.validator.BasicValidatorTest;

public class DescribeStatementTest extends BasicValidatorTest {

  @Test
  public void testDescribeKeyspaceOk() {
    String input = "DESCRIBE KEYSPACE demo;";
    validateOk(input, "testDescribeKeyspaceOk");
  }

  @Test
  public void testDescribeUnknownKeyspaceFail() {
    String input = "DESCRIBE KEYSPACE unknown;";
    validateFail(input, "testDescribeUnknownKeyspaceFail");
  }

  @Test
  public void testDescribeCurrentKeyspaceOk() {
    String input = "DESCRIBE KEYSPACE;";
    validateOk(input, "testDescribeCurrentKeyspaceOk");
  }

  @Test
  public void testDescribeTableOk() {
    String input = "DESCRIBE TABLE demo.users;";
    validateOk(input, "testDescribeTableOk");
  }

  @Test
  public void testDescribeUnknownTableFail() {
    String input = "DESCRIBE TABLE demo.unknown;";
    validateFail(input, "testDescribeUnknownTableFail");
  }

  @Test
  public void testDescribeTableUnknownKeyspaceFail() {
    String input = "DESCRIBE TABLE demo.unknown;";
    validateFail(input, "testDescribeTableUnknownKeyspaceFail");
  }

  @Test
  public void testDescribeTablesOk() {
    String input = "DESCRIBE TABLES;";
    validateOk(input, "testDescribeTablesOk");
  }

  @Test
  public void testDescribeKeyspacesOk() {
    String input = "DESCRIBE KEYSPACES;";
    validateOk(input, "testDescribeKeyspacesOk");
  }
}
