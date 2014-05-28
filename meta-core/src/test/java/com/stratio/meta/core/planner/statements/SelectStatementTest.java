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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.core.structures.GroupByFunction;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta.core.structures.LongTerm;
import com.stratio.meta.core.structures.Relation;
import com.stratio.meta.core.structures.RelationBetween;
import com.stratio.meta.core.structures.RelationCompare;
import com.stratio.meta.core.structures.RelationIn;
import com.stratio.meta.core.structures.SelectionAsterisk;
import com.stratio.meta.core.structures.SelectionClause;
import com.stratio.meta.core.structures.SelectionList;
import com.stratio.meta.core.structures.SelectionSelector;
import com.stratio.meta.core.structures.SelectionSelectors;
import com.stratio.meta.core.structures.SelectorGroupBy;
import com.stratio.meta.core.structures.SelectorIdentifier;
import com.stratio.meta.core.structures.SelectorMeta;
import com.stratio.meta.core.structures.StringTerm;
import com.stratio.meta.core.structures.Term;
import com.stratio.meta.core.utils.Tree;

public class SelectStatementTest extends BasicPlannerTest {

  @Test
  public void testWhereIndexNonRelational() {
    String inputText = "SELECT name, age, info FROM demo.users WHERE age = 10";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")), new SelectionSelector(new SelectorIdentifier("info")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Relation relation = new RelationCompare("age", "=", new LongTerm("10"));
    List<Relation> whereClause = Arrays.asList(relation);
    ((SelectStatement) stmt).setWhere(whereClause);
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    validateCassandraPath("testWhereIndexNonRelational");
  }

  @Test
  public void testWhereWithPartialPartitionKey() {
    String inputText = "SELECT name, age FROM demo.users WHERE name = 'name_5' AND age = 15;";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Relation relation1 = new RelationCompare("name", "=", new StringTerm("name_5"));
    Relation relation2 = new RelationCompare("age", "=", new LongTerm("15"));
    List<Relation> whereClause = Arrays.asList(relation1, relation2);
    ((SelectStatement) stmt).setWhere(whereClause);
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereWithPartialPartitionKey");
  }

  @Test
  public void testWhereIndexRelational() {
    String inputText = "SELECT name, age FROM users WHERE age > 13";
    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("name")), new SelectionSelector(
            new SelectorIdentifier("age")), new SelectionSelector(new SelectorIdentifier("info")));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Relation relation = new RelationCompare("age", ">", new LongTerm("13"));
    List<Relation> whereClause = Arrays.asList(relation);
    ((SelectStatement) stmt).setWhere(whereClause);
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereIndexRelational");
  }

  @Test
  public void testWhereNoIndex() {
    String inputText = "SELECT * FROM demo.types WHERE int_column=104;";
    SelectionClause selClause = new SelectionList(new SelectionAsterisk());
    stmt = new SelectStatement(selClause, "demo.types");
    Relation relation = new RelationCompare("int_column", "=", new LongTerm("104"));
    List<Relation> whereClause = Arrays.asList(relation);
    ((SelectStatement) stmt).setWhere(whereClause);
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereNoIndex");
  }

  @Test
  public void testSimpleJoin() {
    String inputText =
        "SELECT users.name, users.age, users_info.info FROM demo.users "
            + "INNER JOIN demo.users_info ON users.name=users_info.link_name;";

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("users.name")),
            new SelectionSelector(new SelectorIdentifier("users.age")), new SelectionSelector(
                new SelectorIdentifier("users_info.info")));
    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Map<String, String> fields = new HashMap<String, String>();
    fields.put("users.name", "users_info.link_name");
    InnerJoin join = new InnerJoin("demo.users_info", fields);
    ((SelectStatement) stmt).setJoin(join);
    ((SelectStatement) stmt).setSessionKeyspace("demo");
    ((SelectStatement) stmt).validate(_metadataManager);
    validateDeepPath("testSimpleJoin");

  }

  @Test
  public void testComplexJoinNoMatch() {
    String inputText =
        "SELECT users.name, users.age, users_info.info FROM demo.users "
            + "INNER JOIN demo.users_info ON users.name=users_info.link_name "
            + "WHERE name = 'name_3';";

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("users.name")),
            new SelectionSelector(new SelectorIdentifier("users.age")), new SelectionSelector(
                new SelectorIdentifier("users_info.info")));
    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    Map<String, String> fields = new HashMap<String, String>();
    fields.put("users.name", "users_info.link_name");
    InnerJoin join = new InnerJoin("demo.users_info", fields);
    ((SelectStatement) stmt).setJoin(join);
    ((SelectStatement) stmt).setSessionKeyspace("demo");
    ((SelectStatement) stmt).validate(_metadataManager);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("users.name", "=", new StringTerm("name_3"));
    clause.add(relation);
    ((SelectStatement) stmt).setWhere(clause);
    validateDeepPath("testComplexJoinNoMatch");
  }

  @Test
  public void testWhereWithInClause() {
    String inputText =
        "SELECT name, age FROM demo.users WHERE name IN ('name_5', 'name_11') AND age = 15;";
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
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereWithPartialPartitionKey");
  }

  @Test
  public void testWhereWithBetweenClause() {
    String inputText =
        "SELECT name, age FROM demo.users WHERE name IN ('name_5', 'name_11') AND age = 15;";
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
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereWithPartialPartitionKey");
  }

  @Test
  public void testGroupByWithCount() {

    String inputText = "SELECT gender, COUNT(*) FROM demo.users GROUP BY gender;";

    List<SelectorMeta> metaParams = new ArrayList<>();
    metaParams.add(new SelectorIdentifier("*"));

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier("gender")),
            new SelectionSelector(new SelectorGroupBy(GroupByFunction.COUNT, metaParams)));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    stmt = new SelectStatement(selClause, "demo.users");
    GroupBy groupClause = new GroupBy(Arrays.asList("gender"));
    ((SelectStatement) stmt).setGroup(groupClause);
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    validateDeepPath("testWhereWithPartialPartitionKey");
  }
}
