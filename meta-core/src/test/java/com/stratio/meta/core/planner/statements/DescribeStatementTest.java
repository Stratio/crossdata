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
  public void testPlanForDescribeKeyspace() {
    String input = "DESCRIBE KEYSPACE demo;";
    stmt = new DescribeStatement(DescribeType.KEYSPACE);
    ((DescribeStatement) stmt).setKeyspace("demo");
    validateCommandPath("testPlanForDescribeKeyspace");
  }

  @Test
  public void testPlanForDescribeCurrentKeyspace() {
    String input = "DESCRIBE KEYSPACE;";
    stmt = new DescribeStatement(DescribeType.KEYSPACE);
    ((DescribeStatement) stmt).setKeyspace("demo");
    validateCommandPath("testPlanForDescribeKeyspace");
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
  public void testPlanForDescribeKeyspaces() {
    String input = "DESCRIBE KEYSPACES;";
    stmt = new DescribeStatement(DescribeType.KEYSPACES);
    ((DescribeStatement) stmt).setTableName("demo.users");
    validateCommandPath("testPlanForDescribeKeyspaces");
  }
}
