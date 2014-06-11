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

package com.stratio.meta.core.grammar.statements;

import org.testng.annotations.Test;

import com.stratio.meta.core.grammar.ParsingTest;

public class ExplainPlanStatementTest extends ParsingTest {

  @Test
  public void explainPlanForDropIndex() {
    String inputText = "EXPLAIN PLAN FOR DROP INDEX indexName;";
    testRegularStatement(inputText, "explainPlanForDropIndex");
  }

  @Test
  public void explainPlanForSimpleSelect() {
    String inputText = "EXPLAIN PLAN FOR SELECT users.name, users.age FROM demo.users;";
    testRegularStatement(inputText, "explainPlanForSimpleSelect");
  }

  @Test
  public void explainPlanForWrongPlanToken() {
    String inputText = "EXPLAIN PLAANS FOR DROP INDEX indexName;";
    testParseFails(inputText, "wrongPlanToken");
  }

  @Test
  public void explainPlanForWrongFromToken() {
    String inputText = "EXPLAIN PLAN FOR SELECT * FROMS demo.users;";
    testParseFails(inputText, "wrongPlanToken");
  }

}