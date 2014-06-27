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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.CreateTableStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.structures.BooleanProperty;
import com.stratio.meta.core.structures.Property;
import com.stratio.meta.core.structures.PropertyNameValue;

public class CreateTableStatementTest extends BasicPlannerTest {

  /**
   * Class logger.
   */
  private static final Logger logger = Logger.getLogger(CreateTableStatementTest.class);

  @Test
  public void testPlanForCreateTable() {
    String inputText =
        "CREATE TABLE demo.new_table (id INT, name VARCHAR, check BOOLEAN, PRIMARY KEY (id, name));";
    Map<String, String> columns = new HashMap();
    columns.put("id", "INT");
    columns.put("name", "VARCHAR");
    columns.put("check", "BOOLEAN");
    stmt =
        new CreateTableStatement("demo.new_table", columns, Arrays.asList("id"),
            Arrays.asList("name"), 1, 1);
    stmt.setSessionKeyspace("demo");

    try {
      Class<? extends MetaStatement> clazz = stmt.getClass();
      Field field = clazz.getDeclaredField("createTable");
      field.setAccessible(true);
      field.setBoolean(stmt, true);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      logger.error(e.getStackTrace());
    }

    validateCassandraPath("testPlanForCreateTable");
  }

  @Test
  public void testPlanForEphemeralCreateTable() {
    String inputText =
        "CREATE TABLE demo.table_temporal (id INT, name VARCHAR, check BOOLEAN, PRIMARY KEY (id)) WITH ephemeral = true;";
    Map<String, String> columns = new HashMap();
    columns.put("id", "INT");
    columns.put("name", "VARCHAR");
    columns.put("check", "BOOLEAN");
    stmt =
        new CreateTableStatement("demo.new_table", columns, Arrays.asList("id"),
            Arrays.asList("name"), 1, 1);
    stmt.setSessionKeyspace("demo");

    Property prop = new PropertyNameValue("ephemeral", new BooleanProperty(true));

    ((CreateTableStatement) stmt).setProperties(Collections.singletonList(prop));

    try {
      Class<? extends MetaStatement> clazz = stmt.getClass();
      Field field = clazz.getDeclaredField("createTable");
      field.setAccessible(true);
      field.setBoolean(stmt, true);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      logger.error(e.getStackTrace());
    }

    validateStreamingPath("testPlanForEphemeralCreateTable");
  }

}
