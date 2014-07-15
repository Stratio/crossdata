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

package com.stratio.meta.core.planner.statements;

import org.testng.annotations.Test;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.structures.DescribeType;

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
    ((DescribeStatement) stmt).setTableName("demo.users");
    validateCommandPath("testPlanForDescribeTable");
  }

  @Test
  public void testPlanForDescribeTables() {
    String input = "DESCRIBE TABLES;";
    stmt = new DescribeStatement(DescribeType.TABLES);
    ((DescribeStatement) stmt).setTableName("demo.users");
    validateCommandPath("testPlanForDescribeTables");
  }

  @Test
  public void testPlanForDescribeCatalogs() {
    String input = "DESCRIBE CATALOGS;";
    stmt = new DescribeStatement(DescribeType.CATALOGS);
    ((DescribeStatement) stmt).setTableName("demo.users");
    validateCommandPath("testPlanForDescribeCatalogs");
  }
}
