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

package com.stratio.meta2.core.planner;

import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.NormalizedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.query.SelectParsedQuery;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta2.core.statements.SelectStatement;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

public class PlannerWorkflowTest {

  ParsingTest helperPT = new ParsingTest();

  Planner planner = new Planner();

  public class NormalizedQueryWrapper extends NormalizedQuery{

    private SelectStatement stmt = null;

    public NormalizedQueryWrapper(SelectParsedQuery parsedQuery,
                                  SelectStatement stmt) {
      super(parsedQuery);
      this.stmt = stmt;
    }

    public NormalizedQueryWrapper(SelectParsedQuery parsedQuery) {
      super(parsedQuery);
    }

    public NormalizedQueryWrapper(SelectStatement stmt, SelectParsedQuery parsedQuery){
      super(parsedQuery);
      this.stmt = stmt;
    }

    @Override
    public List<TableName> getTables() {
      List<TableName> tableNames = new ArrayList<>();
      tableNames.add(stmt.getTableName());
      InnerJoin join = stmt.getJoin();
      if(join != null){
        tableNames.add(join.getTablename());
      }
      return tableNames;
    }

    @Override
    public List<ColumnName> getColumns() {
      List<ColumnName> columnNames = new ArrayList<>();
      for(Selector s: stmt.getSelectExpression().getSelectorList()){
        columnNames.addAll(getSelectorColumns(s));
      }
      InnerJoin join = stmt.getJoin();
      if(join != null){
        for(Relation r : join.getRelations()){
          columnNames.addAll(getRelationColumns(r));
        }
      }
      return columnNames;
    }

    private List<ColumnName> getSelectorColumns(Selector r) {
      List<ColumnName> result = new ArrayList<>();
      if(ColumnSelector.class.isInstance(r)){
        result.add(ColumnSelector.class.cast(r).getName());
      }
      return result;
    }

    private List<ColumnName> getRelationColumns(Relation r) {
      List<ColumnName> result = new ArrayList<>();
      result.addAll(getSelectorColumns(r.getIdentifier()));
      return result;
    }

    @Override
    public List<Relation> getRelationships() {
      return stmt.getWhere();
    }
  }

  public LogicalWorkflow getWorkflow(String statement, String methodName) {
    MetaStatement stmt = helperPT.testRegularStatement(statement, methodName);
    SelectStatement ss = SelectStatement.class.cast(stmt);
    NormalizedQuery nq = new NormalizedQueryWrapper(
            SelectStatement.class.cast(stmt), new SelectParsedQuery(new BaseQuery("42", statement, null), ss));
    return planner.buildWorkflow(nq);
  }

  public void assertNumberInitialSteps(LogicalWorkflow workflow, int expected) {
    assertNotNull(workflow, "Expecting workflow");
    assertEquals(workflow.getInitialSteps().size(), expected, "Expecting a single initial step.");
  }

  public void assertColumnsInProject(LogicalWorkflow workflow, String tableName, String [] columns){
    Project targetProject = null;
    Iterator<LogicalStep> initialSteps = workflow.getInitialSteps().iterator();
    while(targetProject == null && initialSteps.hasNext()){
      LogicalStep ls = initialSteps.next();
      Project p = Project.class.cast(ls);
      if(tableName.equalsIgnoreCase(p.getTableName().getQualifiedName())){
        targetProject = p;
      }
    }
    assertNotNull(targetProject, "Table " + tableName + " not found.");
    assertEquals(columns.length, targetProject.getColumnList().size(), "Number of columns differs.");
    List<String> columnList = Arrays.asList(columns);
    for(ColumnName cn : targetProject.getColumnList()){
      assertTrue(columnList.contains(cn.getQualifiedName()), "Column " + cn + " not found");
    }

  }

  @Test
  public void selectSingleColumn() {
    String inputText = "SELECT c.t.a FROM c.t;";
    String [] expectedColumns = {"c.t.a"};
    LogicalWorkflow workflow = getWorkflow(inputText, "selectSingleColumn");
    assertNumberInitialSteps(workflow, 1);
    assertColumnsInProject(workflow, "c.t", expectedColumns);
  }

  @Test
  public void selectMultipleColumns() {
    String inputText = "SELECT c.t.a, c.t.b, c.t.c FROM c.t;";
    String [] expectedColumns = {"c.t.a", "c.t.b", "c.t.c"};
    LogicalWorkflow workflow = getWorkflow(inputText, "selectSingleColumn");
    assertColumnsInProject(workflow, "c.t", expectedColumns);
  }

  @Test
  public void selectJoinMultipleColumns() {
    //TODO update on clause when fullyqualifed names are supported in the JOIN.
    String inputText = "SELECT c.t1.a, c.t1.b, c.t2.c, c.t2.d FROM c.t1 INNER JOIN c.t2 ON c.t1.aa = aa;";
    String [] expectedColumnsT1 = {"c.t1.a", "c.t1.b", "c.t1.aa"};
    String [] expectedColumnsT2 = {"c.t2.c", "c.t2.d"};

    LogicalWorkflow workflow = getWorkflow(inputText, "selectSingleColumn");
    assertNumberInitialSteps(workflow, 2);
    assertColumnsInProject(workflow, "c.t1", expectedColumnsT1);
    assertColumnsInProject(workflow, "c.t2", expectedColumnsT2);
  }

}