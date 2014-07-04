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

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.DropTableStatement;
import com.stratio.meta.core.statements.ExplainPlanStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.IntegerTerm;
import com.stratio.meta.core.structures.Relation;
import com.stratio.meta.core.structures.RelationCompare;
import com.stratio.meta.core.structures.SelectionClause;
import com.stratio.meta.core.structures.SelectionList;
import com.stratio.meta.core.structures.SelectionSelector;
import com.stratio.meta.core.structures.SelectionSelectors;
import com.stratio.meta.core.structures.SelectorIdentifier;

public class ExplainPlanStatementTest extends BasicPlannerTest {

  @Test
  public void testPlanForExplainDrop() {

    // EXPLAIN PLAN FOR DROP TABLE table1;
    MetaStatement dropTable = new DropTableStatement("demo.users", false);
    stmt = new ExplainPlanStatement(dropTable);
    validateCommandPath("testPlanForExplain");
  }

  @Test
  public void testPlanForExplainSelect() {

    // EXPLAIN PLAN FOR SELECT name, age, info FROM demo.users WHERE age = 10;
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")), new SelectionSelector(new SelectorIdentifier("info")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    SelectStatement selectStmt = new SelectStatement(selClause, "demo.users");
    Relation relation = new RelationCompare("age", "=", new IntegerTerm("10"));
    List<Relation> whereClause = Arrays.asList(relation);
    selectStmt.setWhere(whereClause);

    stmt = new ExplainPlanStatement(selectStmt);
    validateCommandPath("testPlanForExplainSelect");
  }
}
