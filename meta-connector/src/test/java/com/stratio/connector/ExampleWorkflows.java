/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.connector;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.logicalplan.Select;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.IntegerSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.common.statements.structures.selectors.StringSelector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Example workflows to test basic functionality of the different connectors.
 * This class assumes the existence of a catalog named {@code example} and with
 * two tables named {@code users}, and {@code information}.
 *
 * Table {@code users} contains the following fields:
 * <li>
 *   <ul>id: integer, PK</ul>
 *   <ul>name: text, PK, indexed</ul>
 *   <ul>age: integer</ul>
 *   <ul>bool: boolean</ul>
 * </li>
 *
 * Table {@code information} contains the following fields:
 * <li>
 *   <ul>id: integer, PK</ul>
 *   <ul>phrase: text, indexed</ul>
 *   <ul>email: text</ul>
 *   <ul>score: double</ul>
 * </li>
 */
public class ExampleWorkflows {

  /**
   * Get a project operator taking the table name from the first column. This operation
   * assumes all columns belong to the same table.
   * @param columnNames The list of columns.
   * @return A {@link com.stratio.meta.common.logicalplan.Project}.
   */
  public Project getProject(ColumnName ... columnNames){
    TableName table = new TableName(
        columnNames[0].getTableName().getCatalogName().getName(),
        columnNames[0].getTableName().getName());
    return new Project(Operations.PROJECT, table, Arrays.asList(columnNames));
  }

  /**
   * Get a select operator.
   * @param alias The alias
   * @param columnNames The list of columns.
   * @return A {@link com.stratio.meta.common.logicalplan.Select}.
   */
  public Select getSelect(String [] alias, ColumnName ... columnNames){
    Map<String, String> columnMap = new HashMap<>();
    int aliasIndex = 0;
    for(ColumnName column : columnNames){
      columnMap.put(column.getQualifiedName(), alias[aliasIndex]);
      aliasIndex++;
    }
    return new Select(Operations.SELECT_OPERATOR, columnMap);
  }

  /**
   * Get a filter operator.
   * @param filterOp The Filter operation.
   * @param column The column name.
   * @param op The relationship operator.
   * @param right The right selector.
   * @return A {@link com.stratio.meta.common.logicalplan.Filter}.
   */
  public Filter getFilter(Operations filterOp, ColumnName column, Operator op, Selector right){
    Selector left = new ColumnSelector(column);
    Relation r = new Relation(left, op, right);
    return new Filter(filterOp, r);
  }


  /**
   * Get a basic select.
   * SELECT name, users.age FROM example.users;
   * @return A {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}.
   */
  public LogicalWorkflow getBasicSelect(){
    ColumnName name = new ColumnName("example", "users", "name");
    ColumnName age = new ColumnName("example", "users", "age");
    String [] outputNames = {"name", "users.age"};
    LogicalStep project = getProject(name, age);
    LogicalStep select = getSelect(outputNames, name, age);
    project.setNextStep(select);
    LogicalWorkflow lw = new LogicalWorkflow(Arrays.asList(project));
    return lw;
  }

  /**
   * Get a basic select.
   * SELECT * FROM example.users;
   * @return A {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}.
   */
  public LogicalWorkflow getBasicSelectAsterisk(){
    ColumnName id = new ColumnName("example", "users", "id");
    ColumnName name = new ColumnName("example", "users", "name");
    ColumnName age = new ColumnName("example", "users", "age");
    ColumnName bool = new ColumnName("example", "users", "bool");
    String [] outputNames = {"id", "name", "age", "bool"};
    LogicalStep project = getProject(id, name, age, bool);
    LogicalStep select = getSelect(outputNames, id, name, age, bool);
    project.setNextStep(select);
    LogicalWorkflow lw = new LogicalWorkflow(Arrays.asList(project));
    return lw;
  }

  /**
   * Get a basic select with a single where clause on an indexed field.
   * SELECT users.id, users.name, users.age FROM example.users WHERE users.name='user1';
   * @return A {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}.
   */
  public LogicalWorkflow getSelectIndexedField(){
    ColumnName id = new ColumnName("example", "users", "id");
    ColumnName name = new ColumnName("example", "users", "name");
    ColumnName age = new ColumnName("example", "users", "age");
    String [] outputNames = {"users.id", "users.name", "users.age"};
    LogicalStep project = getProject(id, name, age);
    LogicalStep filter = getFilter(Operations.FILTER_INDEXED_EQ,
                                   name, Operator.EQ, new StringSelector("'user1'"));
    project.setNextStep(filter);
    LogicalStep select = getSelect(outputNames, name, age);
    filter.setNextStep(select);
    LogicalWorkflow lw = new LogicalWorkflow(Arrays.asList(project));
    return lw;
  }

  /**
   * Get a basic select with a single where clause on a non-indexed field.
   * SELECT users.id, users.name, users.age FROM example.users WHERE users.age=42;
   * @return A {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}.
   */
  public LogicalWorkflow getSelectNonIndexedField(){
    ColumnName id = new ColumnName("example", "users", "id");
    ColumnName name = new ColumnName("example", "users", "name");
    ColumnName age = new ColumnName("example", "users", "age");
    String [] outputNames = {"users.id", "users.name", "users.age"};
    LogicalStep project = getProject(id, name, age);
    LogicalStep filter = getFilter(Operations.FILTER_NON_INDEXED_EQ,
                                   name, Operator.EQ, new IntegerSelector(42));
    project.setNextStep(filter);
    LogicalStep select = getSelect(outputNames, name, age);
    filter.setNextStep(select);
    LogicalWorkflow lw = new LogicalWorkflow(Arrays.asList(project));
    return lw;
  }

  /**
   * Get a basic select with two where clauses.
   * SELECT users.id, users.name, users.age FROM example.users WHERE users.name='user1' AND users.age=42;
   * @return A {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}.
   */
  public LogicalWorkflow getSelectMixedWhere(){
    ColumnName id = new ColumnName("example", "users", "id");
    ColumnName name = new ColumnName("example", "users", "name");
    ColumnName age = new ColumnName("example", "users", "age");
    String [] outputNames = {"users.id", "users.name", "users.age"};
    LogicalStep project = getProject(id, name, age);
    LogicalStep filterName = getFilter(Operations.FILTER_INDEXED_EQ,
                                   name, Operator.EQ, new StringSelector("'user1'"));
    project.setNextStep(filterName);
    LogicalStep filterAge = getFilter(Operations.FILTER_NON_INDEXED_EQ,
                                   name, Operator.EQ, new IntegerSelector(42));
    filterName.setNextStep(filterAge);
    LogicalStep select = getSelect(outputNames, name, age);
    filterAge.setNextStep(select);
    LogicalWorkflow lw = new LogicalWorkflow(Arrays.asList(project));
    return lw;
  }

}
