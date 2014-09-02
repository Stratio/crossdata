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

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.BooleanSelector;
import com.stratio.meta2.common.statements.structures.selectors.IntegerSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.common.statements.structures.selectors.StringSelector;
import com.stratio.meta2.core.statements.InsertIntoStatement;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertIntoStatementTest extends BasicPlannerTest {

  @Test
  public void testPlanForInsert() {
    String inputText = "INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
    TableName tablename = new TableName("demo", "users");
    List<ColumnName> ids = Arrays.asList(new ColumnName(tablename, "name"),
                                         new ColumnName(tablename, "gender"),
                                         new ColumnName(tablename, "email"),
                                         new ColumnName(tablename, "age"),
                                         new ColumnName(tablename, "bool"),
                                         new ColumnName(tablename, "phrase"));
    List<Selector> list = new ArrayList<>();
    list.add(new StringSelector("name_0"));
    list.add(new StringSelector("male"));
    list.add(new IntegerSelector("10"));
    list.add(new BooleanSelector("false"));
    list.add(new StringSelector(""));
    stmt = new InsertIntoStatement(tablename, ids, list, false, new ArrayList<Option>());
    validateCassandraPath("testPlanForInsert");
  }
}
