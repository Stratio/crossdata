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

package com.stratio.meta.core.planner.statements;

import org.testng.annotations.Test;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta.common.statements.structures.TableName;

public class DescribeStatementTest extends BasicPlannerTest {

  @Test
  public void testPlanForDescribeCatalog() {
    String input = "DESCRIBE CATALOG demo;";
    stmt = new DescribeStatement(DescribeType.CATALOG);
    ((DescribeStatement) stmt).setCatalog("demo");
    validateCommandPath("testPlanForDescribeCatalog");
  }

  @Test
  public void testPlanForDescribeCurrentCatalog() {
    String input = "DESCRIBE CATALOG;";
    stmt = new DescribeStatement(DescribeType.CATALOG);
    ((DescribeStatement) stmt).setCatalog("demo");
    validateCommandPath("testPlanForDescribeCatalog");
  }

  @Test
  public void testPlanForDescribeTable() {
    String input = "DESCRIBE TABLE demo.users;";
    stmt = new DescribeStatement(DescribeType.TABLE);
    ((DescribeStatement) stmt).setTableName(new TableName("demo.users"));
    validateCommandPath("testPlanForDescribeTable");
  }

  @Test
  public void testPlanForDescribeTables() {
    String input = "DESCRIBE TABLES;";
    stmt = new DescribeStatement(DescribeType.TABLES);
    ((DescribeStatement) stmt).setTableName(new TableName("demo.users"));
    validateCommandPath("testPlanForDescribeTables");
  }

  @Test
  public void testPlanForDescribeCatalogs() {
    String input = "DESCRIBE CATALOGS;";
    stmt = new DescribeStatement(DescribeType.CATALOGS);
    ((DescribeStatement) stmt).setTableName(new TableName("demo.users"));
    validateCommandPath("testPlanForDescribeCatalogs");
  }
}
