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

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.common.statements.structures.selectors.GroupByFunction;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta.core.structures.LongTerm;
import com.stratio.meta.core.structures.Ordering;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.relationships.RelationBetween;
import com.stratio.meta.common.statements.structures.relationships.RelationCompare;
import com.stratio.meta.common.statements.structures.relationships.RelationIn;
import com.stratio.meta.core.structures.SelectionAsterisk;
import com.stratio.meta.core.structures.SelectionClause;
import com.stratio.meta.core.structures.SelectionList;
import com.stratio.meta.core.structures.SelectionSelector;
import com.stratio.meta.core.structures.SelectionSelectors;
import com.stratio.meta.common.statements.structures.selectors.SelectorGroupBy;
import com.stratio.meta.common.statements.structures.selectors.SelectorIdentifier;
import com.stratio.meta.core.structures.StringTerm;
import com.stratio.meta.common.statements.structures.terms.Term;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectStatementTest extends BasicPlannerTest {

  @Test
  public void testWhereIndexNonRelational() {
    // "SELECT name, age, info FROM demo.users WHERE age = 10";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")), new SelectionSelector(new SelectorIdentifier("info")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Relation relation = new RelationCompare("age", "=", new LongTerm("10"));
    List<Relation> whereClause = Arrays.asList(relation);
    ((SelectStatement) stmt).setWhere(whereClause);
    stmt.getPlan(_metadataManager, "demo");
    validateCassandraPath("testWhereIndexNonRelational");
  }

  @Test
  public void testWhereWithPartialPartitionKey() {
    // "SELECT name, age FROM demo.users WHERE name = 'name_5' AND age = 15;";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Relation relation1 = new RelationCompare("name", "=", new StringTerm("name_5"));
    Relation relation2 = new RelationCompare("age", "=", new LongTerm("15"));
    List<Relation> whereClause = Arrays.asList(relation1, relation2);
    ((SelectStatement) stmt).setWhere(whereClause);
    stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereWithPartialPartitionKey");
  }

  @Test
  public void testWhereIndexRelational() {
    // "SELECT name, age FROM users WHERE age > 13";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")), new SelectionSelector(new SelectorIdentifier("info")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Relation relation = new RelationCompare("age", ">", new LongTerm("13"));
    List<Relation> whereClause = Arrays.asList(relation);
    ((SelectStatement) stmt).setWhere(whereClause);
    stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereIndexRelational");
  }

  @Test
  public void testWhereNoIndex() {
    // "SELECT * FROM demo.types WHERE int_column=104;";
    SelectionClause selClause = new SelectionList(new SelectionAsterisk());
    stmt = new SelectStatement(selClause, "demo.types");
    Relation relation = new RelationCompare("int_column", "=", new LongTerm("104"));
    List<Relation> whereClause = Arrays.asList(relation);
    ((SelectStatement) stmt).setWhere(whereClause);
    stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereNoIndex");
  }

  @Test
  public void testSimpleJoin() {

    // "SELECT users.name, users.age, users_info.info FROM demo.users INNER JOIN demo.users_info ON users.name=users_info.link_name;";

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("users.name")),
            new SelectionSelector(new SelectorIdentifier("users.age")), new SelectionSelector(
                new SelectorIdentifier("users_info.info")));
    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    InnerJoin join = new InnerJoin("demo.users_info", "users.name", "users_info.link_name");
    ((SelectStatement) stmt).setJoin(join);
    ((SelectStatement) stmt).setSessionCatalog("demo");
    ((SelectStatement) stmt).validate(_metadataManager, null);
    validateDeepPath("testSimpleJoin");

  }

  @Test
  public void testComplexJoinNoMatch() {

    // "SELECT users.name, users.age, users_info.info FROM demo.users INNER JOIN demo.users_info ON users.name=users_info.link_name WHERE name = 'name_3';";

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("users.name")),
            new SelectionSelector(new SelectorIdentifier("users.age")), new SelectionSelector(
                new SelectorIdentifier("users_info.info")));
    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    InnerJoin join = new InnerJoin("demo.users_info", "users.name", "users_info.link_name");
    ((SelectStatement) stmt).setJoin(join);
    ((SelectStatement) stmt).setSessionCatalog("demo");
    ((SelectStatement) stmt).validate(_metadataManager, null);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("users.name", "=", new StringTerm("name_3"));
    clause.add(relation);
    ((SelectStatement) stmt).setWhere(clause);
    validateDeepPath("testComplexJoinNoMatch");
  }

  @Test
  public void testWhereWithInClause() {

    // "SELECT name, age FROM demo.users WHERE name IN ('name_5', 'name_11') AND age = 15;";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    List<Term<?>> termsList = new ArrayList<>();
    termsList.add(new StringTerm("name_5"));
    termsList.add(new StringTerm("name_11"));
    Relation relation1 = new RelationIn("name", termsList);
    Relation relation2 = new RelationCompare("age", "=", new LongTerm("15"));
    List<Relation> whereClause = Arrays.asList(relation1, relation2);
    ((SelectStatement) stmt).setWhere(whereClause);
    stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereWithInClause");
  }

  @Test
  public void testWhereWithBetweenClause() {
    // "SELECT name, age FROM demo.users WHERE name IN ('name_5', 'name_11') AND age = 15;";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Relation relation1 =
        new RelationBetween("name", new StringTerm("name_5"), new StringTerm("name_11"));
    Relation relation2 = new RelationCompare("age", "=", new LongTerm("15"));
    List<Relation> whereClause = Arrays.asList(relation1, relation2);
    ((SelectStatement) stmt).setWhere(whereClause);
    stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereWithBetweenClause");
  }

  @Test
  public void testGroupByWithCount() {

    // "SELECT gender, COUNT(*) FROM demo.users GROUP BY gender;";

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("gender")),
            new SelectionSelector(new SelectorGroupBy(GroupByFunction.COUNT,
                new SelectorIdentifier("*"))));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");

    List<GroupBy> groupClause = new ArrayList<>();
    groupClause.add(new GroupBy("gender"));

    ((SelectStatement) stmt).setGroup(groupClause);
    stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testGroupByWithCount");
  }

  @Test
  public void testSimpleOrderByOk() {

    // "SELECT age FROM demo.users ORDER BY age;";

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("age")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");

    // Order by clause
    List<Ordering> orderFieldsList = new ArrayList<>();
    Ordering order = new Ordering("users.age");
    orderFieldsList.add(order);

    ((SelectStatement) stmt).setOrder(orderFieldsList);
    stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testSimpleOrderByOk");
  }

}
