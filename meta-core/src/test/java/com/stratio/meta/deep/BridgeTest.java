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

package com.stratio.meta.deep;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.core.structures.GroupByFunction;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta.core.structures.IntegerTerm;
import com.stratio.meta.core.structures.LongTerm;
import com.stratio.meta.core.structures.OrderDirection;
import com.stratio.meta.core.structures.Ordering;
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
import com.stratio.meta.core.structures.StringTerm;
import com.stratio.meta.core.structures.Term;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BridgeTest extends BasicCoreCassandraTest {

  private final static String CONSTANT_USERS_GENDER = "users.gender";

  private final static String CONSTANT_USERS_AGE = "users.age";

  private final static String CONSTANT_USERS_NAME = "users.name";

  private final static String CONSTANT_GENDER = "gender";

  private final static String CONSTANT_AGE = "age";

  private final static String CONSTANT_NAME = "name";

  private final static String CONSTANT_USERS_INFO_LINK_NAME = "users_info.link_name";

  private final static String CONSTANT_DEMO_USERS = "demo.users";

  private final static String CONSTANT_DEMO_USERS_INFO = "demo.users_info";

  protected static Executor executor = null;

  protected static DeepSparkContext deepContext = null;

  protected static MetadataManager metadataManager = null;

  private final static Logger LOG = Logger.getLogger(BridgeTest.class);

  @BeforeClass
  public static void setUpBeforeClass() {
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    EngineConfig config = initConfig();
    deepContext = new DeepSparkContext(config.getSparkMaster(), config.getJobName());
    executor = new Executor(_session, null, deepContext, config);
    metadataManager = new MetadataManager(_session, null);
    metadataManager.loadMetadata();
  }

  @AfterClass
  public static void tearDownAfterClass() {
    deepContext.stop();
  }

  public static EngineConfig initConfig() {
    String[] cassandraHosts = {"127.0.0.1"};
    EngineConfig engineConfig = new EngineConfig();
    engineConfig.setCassandraHosts(cassandraHosts);
    engineConfig.setCassandraPort(9042);
    engineConfig.setSparkMaster("local");
    return engineConfig;
  }

  private Result validateOk(MetaQuery metaQuery, String methodName) {
    MetaQuery result = executor.executeQuery(metaQuery, null);
    assertNotNull(result.getResult(), "Result null - " + methodName);
    assertFalse(result.hasError(), "Deep execution failed - " + methodName + ": "
        + getErrorMessage(result.getResult()));
    return result.getResult();
  }

  private Result validateRows(MetaQuery metaQuery, String methodName, int expectedNumber) {
    QueryResult result = (QueryResult) validateOk(metaQuery, methodName);
    if (expectedNumber > 0) {
      assertFalse(result.getResultSet().isEmpty(), "Expecting non-empty resultset");
      assertEquals(result.getResultSet().size(), expectedNumber, methodName + ":"
          + result.getResultSet().size() + " rows found, " + expectedNumber + " rows expected.");
    } else {
      assertTrue(result.getResultSet().isEmpty(), "Expecting empty resultset.");
    }

    return result;
  }

  private Result validateRowsAndCols(MetaQuery metaQuery, String methodName, int expectedRows,
      int expectedCols) {
    QueryResult result = (QueryResult) validateOk(metaQuery, methodName);
    if (expectedRows > 0) {
      assertFalse(result.getResultSet().isEmpty(), "Expecting non-empty resultset");
      assertEquals(result.getResultSet().size(), expectedRows, methodName + ":"
          + result.getResultSet().size() + " rows found, " + expectedRows + " rows expected.");

      Row firstRow = result.getResultSet().iterator().next();
      assertEquals(firstRow.getCellList().size(), expectedCols, methodName + ":"
          + firstRow.getCellList().size() + " cols found, " + expectedCols + " cols expected.");
    } else {
      assertTrue(result.getResultSet().isEmpty(), "Expecting empty resultset.");
    }

    return result;
  }

  private void validateFail(MetaQuery metaQuery) {
    try {
      executor.executeQuery(metaQuery, null);
    } catch (Exception ex) {
      LOG.info("Correctly catched exception");
    }
  }

  private Result validateError(MetaQuery metaQuery, String methodName) {

    return executor.executeQuery(metaQuery, null).getResult();
  }

  // TESTS FOR CORRECT PLANS
  @Test
  public void testInnerJoin() {
    MetaQuery metaQuery =
        new MetaQuery("SELECT users.gender, users_info.info, users.age "
            + "FROM demo.users INNER JOIN demo.users_info ON users.name=users_info.link_name;");

    // ADD MAIN STATEMENT
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "users_info.info")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_AGE)));
    SelectionClause mainSelectionClause = new SelectionList(selectionSelectors);
    InnerJoin join =
        new InnerJoin(CONSTANT_DEMO_USERS_INFO, CONSTANT_USERS_NAME, CONSTANT_USERS_INFO_LINK_NAME);
    SelectStatement ss = new SelectStatement(mainSelectionClause, CONSTANT_DEMO_USERS);
    ss.setJoin(join);
    ss.setLimit(10000);

    metaQuery.setStatement(ss);
    LOG.info("DEEP TEST (Query): " + metaQuery.getQuery());
    LOG.info("DEEP TEST (Stmnt): " + metaQuery.getStatement().toString());

    // FIRST SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_AGE)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);

    // SECOND SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "link_name")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("info")));
    selectionClause = new SelectionList(selectionSelectors);
    SelectStatement secondSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS_INFO);
    secondSelect.setLimit(10000);

    // INNER JOIN
    join = new InnerJoin("", CONSTANT_USERS_NAME, CONSTANT_USERS_INFO_LINK_NAME);
    SelectStatement joinSelect = new SelectStatement(mainSelectionClause, "");
    joinSelect.setJoin(join);
    joinSelect.setLimit(10000);

    // CREATE ROOT
    Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

    // ADD CHILD
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

    // ADD CHILD
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    validateOk(metaQuery, "testInnerJoin");
  }

  @Test
  public void testEqualsFind() {
    MetaQuery metaQuery =
        new MetaQuery("SELECT users.name FROM demo.users WHERE users.email=name_1@domain.com;");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("email", "=", new StringTerm("name_1@domain.com"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    QueryResult result = (QueryResult) validateOk(metaQuery, "testEqualsFind");
    assertEquals(result.getResultSet().size(), 1);
  }

  @Test
  public void testGreaterThan() {
    MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age>100;");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare(CONSTANT_AGE, ">", new LongTerm("100"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    validateOk(metaQuery, "testGreater");
  }

  @Test
  public void testGreaterEqualThan() {
    MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age>=100;");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare(CONSTANT_AGE, ">=", new LongTerm("100"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    validateOk(metaQuery, "testGreaterEqualThan");
  }

  @Test
  public void testLessThan() {
    MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age<100;");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare(CONSTANT_AGE, "<", new LongTerm("100"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    validateOk(metaQuery, "testLessThan");
  }

  @Test
  public void testLessEqualThan() {
    MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age<=100;");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare(CONSTANT_AGE, "<=", new LongTerm("100"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    validateOk(metaQuery, "testLessEqualThan");
  }

  @Test
  public void testInnerJoinAndWhere() {
    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.gender, types.boolean_column, users.age "
                + "FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column WHERE types.int_column > 104;");

    // ADD MAIN STATEMENT
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "types.boolean_column")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_AGE)));
    SelectionClause mainSelectionClause = new SelectionList(selectionSelectors);

    InnerJoin join = new InnerJoin("demo.types", CONSTANT_USERS_NAME, "types.varchar_column");

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("types.int_column", ">", new LongTerm("104"));
    clause.add(relation);

    SelectStatement ss = new SelectStatement(mainSelectionClause, CONSTANT_DEMO_USERS);
    ss.setLimit(10000);
    ss.setWhere(clause);
    ss.setJoin(join);

    metaQuery.setStatement(ss);
    LOG.info("DEEP TEST (Query): " + metaQuery.getQuery());
    LOG.info("DEEP TEST (Stmnt): " + metaQuery.getStatement().toString());

    // FIRST SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_NAME)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_AGE)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);

    // SECOND SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "types.varchar_column")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "types.boolean_column")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "types.int_column")));
    selectionClause = new SelectionList(selectionSelectors);

    clause = new ArrayList<>();
    relation = new RelationCompare("int_column", ">", new LongTerm("104"));
    clause.add(relation);

    SelectStatement secondSelect = new SelectStatement(selectionClause, "demo.types");
    secondSelect.setLimit(10000);
    secondSelect.setWhere(clause);

    // INNER JOIN
    join = new InnerJoin("", CONSTANT_USERS_NAME, "types.varchar_column");
    SelectStatement joinSelect = new SelectStatement(mainSelectionClause, "");
    joinSelect.setJoin(join);
    joinSelect.setLimit(10000);

    // CREATE ROOT
    Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

    // ADD CHILD
    firstSelect.validate(metadataManager, null);
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

    // ADD CHILD
    secondSelect.validate(metadataManager, null);
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    validateRows(metaQuery, "testInnerJoinAndWhere", 5);
  }

  @Test
  public void testSelectAllAndWhere() {
    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT * FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column"
                + " WHERE users.email = 'name_4@domain.com';");

    // ADD MAIN STATEMENT
    SelectionClause selectionClause = new SelectionList(new SelectionAsterisk());
    InnerJoin join = new InnerJoin("demo.types", CONSTANT_USERS_NAME, "types.varchar_column");

    List<Relation> clause = new ArrayList<>();
    Relation relation =
        new RelationCompare("users.email", "=", new StringTerm("name_4@domain.com"));
    clause.add(relation);

    SelectStatement ss = new SelectStatement(selectionClause, // SelectionClause
        // selectionClause
        CONSTANT_DEMO_USERS);
    ss.setJoin(join);
    ss.setWhere(clause);
    ss.setLimit(10000);

    metaQuery.setStatement(ss);
    LOG.info("DEEP TEST (Query): " + metaQuery.getQuery());
    LOG.info("DEEP TEST (Stmnt): " + metaQuery.getStatement().toString());

    // FIRST SELECT
    clause = new ArrayList<>();
    relation = new RelationCompare("email", "=", new StringTerm("name_4@domain.com"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, // SelectionClause
        // selectionClause
        CONSTANT_DEMO_USERS);
    firstSelect.setWhere(clause);
    firstSelect.setLimit(10000);

    // SECOND SELECT
    SelectStatement secondSelect = new SelectStatement(selectionClause, // SelectionClause
        // selectionClause
        "demo.types");
    secondSelect.setLimit(10000);

    // INNER JOIN
    join = new InnerJoin("", CONSTANT_USERS_NAME, "types.varchar_column");
    SelectStatement joinSelect = new SelectStatement(selectionClause, // SelectionClause
        // selectionClause
        "");
    joinSelect.setJoin(join);
    joinSelect.setLimit(10000);

    // CREATE ROOT
    Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

    // ADD CHILD
    firstSelect.validate(metadataManager, null);
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

    // ADD CHILD
    secondSelect.validate(metadataManager, null);
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    validateRows(metaQuery, "testSelectAllAndWhere", 1);
  }

  // TESTS FOR WRONG PLANS
  @Test
  public void testInnerJoinWrongSelectedColumn() {
    MetaQuery metaQuery =
        new MetaQuery("SELECT users.gender, types.info, users.age "
            + "FROM demo.users INNER JOIN demo.users_info ON users.name = users_info.link_name;");

    // ADD MAIN STATEMENT
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "types.info")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_AGE)));
    SelectionClause mainSelectionClause = new SelectionList(selectionSelectors);

    InnerJoin join =
        new InnerJoin(CONSTANT_DEMO_USERS_INFO, CONSTANT_USERS_NAME, CONSTANT_USERS_INFO_LINK_NAME);

    SelectStatement ss = new SelectStatement(mainSelectionClause, // SelectionClause
        // selectionClause
        CONSTANT_DEMO_USERS);
    ss.setJoin(join);
    ss.setLimit(10000);

    metaQuery.setStatement(ss);
    LOG.info("DEEP TEST (Query): " + metaQuery.getQuery());
    LOG.info("DEEP TEST (Stmnt): " + metaQuery.getStatement().toString());

    // FIRST SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_AGE)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    SelectStatement firstSelect = new SelectStatement(selectionClause, // SelectionClause
        // selectionClause
        CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);

    // SECOND SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "link_name")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("info")));
    selectionClause = new SelectionList(selectionSelectors);

    SelectStatement secondSelect = new SelectStatement(selectionClause, // SelectionClause
        // selectionClause
        CONSTANT_DEMO_USERS_INFO);
    secondSelect.setLimit(10000);

    // INNER JOIN
    join = new InnerJoin("", CONSTANT_USERS_NAME, CONSTANT_USERS_INFO_LINK_NAME);
    SelectStatement joinSelect = new SelectStatement(mainSelectionClause, // SelectionClause
        // selectionClause
        "");
    joinSelect.setJoin(join);
    joinSelect.setLimit(10000);

    // CREATE ROOT
    Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

    // ADD CHILD
    firstSelect.validate(metadataManager, null);
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

    // ADD CHILD
    secondSelect.validate(metadataManager, null);
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateFail(metaQuery);
  }

  @Test
  public void testInnerJoinWithOrderBy() {
    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.gender, users_info.info, users.age "
                + "FROM demo.users INNER JOIN demo.users_info ON users.name=users_info.link_name ORDER BY users.age DESC;");

    // ADD MAIN STATEMENT
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "users_info.info")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_AGE)));
    SelectionClause mainSelectionClause = new SelectionList(selectionSelectors);
    InnerJoin join =
        new InnerJoin(CONSTANT_DEMO_USERS_INFO, CONSTANT_USERS_NAME, CONSTANT_USERS_INFO_LINK_NAME);
    SelectStatement ss = new SelectStatement(mainSelectionClause, CONSTANT_DEMO_USERS);
    ss.setJoin(join);
    ss.setLimit(10000);

    metaQuery.setStatement(ss);
    LOG.info("DEEP TEST (Query): " + metaQuery.getQuery());
    LOG.info("DEEP TEST (Stmnt): " + metaQuery.getStatement().toString());

    // FIRST SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_GENDER)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_AGE)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);

    // SECOND SELECT
    selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        "link_name")));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("info")));
    selectionClause = new SelectionList(selectionSelectors);
    SelectStatement secondSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS_INFO);
    secondSelect.setLimit(10000);

    // INNER JOIN
    join = new InnerJoin("", CONSTANT_USERS_NAME, CONSTANT_USERS_INFO_LINK_NAME);
    SelectStatement joinSelect = new SelectStatement(mainSelectionClause, "");
    joinSelect.setJoin(join);
    joinSelect.setLimit(10000);

    // ORDERING
    List<Ordering> orderings = new ArrayList<>();
    Ordering order1 = new Ordering(CONSTANT_AGE, true, OrderDirection.DESC);
    orderings.add(order1);
    joinSelect.setOrder(orderings);

    // CREATE ROOT
    Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

    // ADD CHILD
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

    // ADD CHILD
    tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    validateOk(metaQuery, "testInnerJoin");
  }

  @Test
  public void testBasicInClauseWithStrings() {

    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.name FROM demo.users WHERE users.email IN ('name_11@domain.com','name_9@domain.com');");

    // Fields to retrieve
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    // IN clause
    List<Relation> clause = new ArrayList<>();
    List<Term<?>> inTerms = new ArrayList<>();
    inTerms.add(new StringTerm("name_11@domain.com"));
    inTerms.add(new StringTerm("name_9@domain.com"));
    Relation relation = new RelationIn("email", inTerms);

    clause.add(relation);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRows(metaQuery, "testBasicInClauseWithStrings", 2);

    results.toString();
  }

  @Test
  public void testBasicInClauseWithIntegers() {

    MetaQuery metaQuery =
        new MetaQuery("SELECT users.name FROM demo.users WHERE users.age IN (19,31);");

    // Fields to retrieve
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    // IN clause
    List<Relation> clause = new ArrayList<>();
    List<Term<?>> inTerms = new ArrayList<>();
    inTerms.add(new IntegerTerm("19"));
    inTerms.add(new IntegerTerm("31"));
    Relation relation = new RelationIn(CONSTANT_AGE, inTerms);

    clause.add(relation);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRows(metaQuery, "testBasicInClauseWithIntegers", 1);

    results.toString();
  }

  @Test
  public void testBasicBetweenClauseWithStringData() {

    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'aaaa_00@domain.com' AND 'zzzz_99@domain.com';");

    // Fields to retrieve
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    // IN clause
    List<Relation> clause = new ArrayList<>();
    Relation relation =
        new RelationBetween("email", new StringTerm("aaaa_00@domain.com"), new StringTerm(
            "zzzz_99@domain.com"));

    clause.add(relation);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRows(metaQuery, "testBasicBetweenClauseWithStringData", 16);

    results.toString();
  }

  @Test
  public void testBasicBetweenClauseWithoutResults() {

    MetaQuery metaQuery =
        new MetaQuery("SELECT users.name FROM demo.users WHERE users.email BETWEEN 'a' AND 'b';");

    // Fields to retrieve
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    // IN clause
    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationBetween("email", new StringTerm("a"), new StringTerm("b"));

    clause.add(relation);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRows(metaQuery, "testBasicBetweenClauseWithoutResults", 0);

    results.toString();
  }

  @Test
  public void testNotEqual() {
    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.name, users.age FROM demo.users WHERE users.email<>name_1@domain.com;");

    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_AGE)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("email", "<>", new StringTerm("name_1@domain.com"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    QueryResult result = (QueryResult) validateOk(metaQuery, "testNotEqual");
    assertEquals(result.getResultSet().size(), 15);
  }

  @Test
  public void testBasicBetweenClauseWithIntegerData() {

    MetaQuery metaQuery =
        new MetaQuery("SELECT users.name FROM demo.users WHERE users.age BETWEEN 10 AND 25;");

    // Fields to retrieve
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_NAME)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    // IN clause
    List<Relation> clause = new ArrayList<>();
    IntegerTerm integerTermLower = new IntegerTerm("10");
    IntegerTerm integerTermUpper = new IntegerTerm("25");
    Relation relation = new RelationBetween(CONSTANT_AGE, integerTermLower, integerTermUpper);

    clause.add(relation);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRows(metaQuery, "testBasicBetweenClauseWithIntegerData", 10);

    results.toString();
  }

  @Test
  public void testBasicGroupByClauseOk() {

    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.gender,count(*),sum(users.age) FROM demo.users GROUP BY users.gender;");

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier(CONSTANT_USERS_GENDER)),
            new SelectionSelector(new SelectorGroupBy(GroupByFunction.COUNT,
                new SelectorIdentifier("*"))), new SelectionSelector(new SelectorGroupBy(
                GroupByFunction.SUM, new SelectorIdentifier(CONSTANT_USERS_AGE))));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    List<GroupBy> groupClause = new ArrayList<>();
    groupClause.add(new GroupBy(CONSTANT_USERS_GENDER));

    SelectStatement firstSelect = new SelectStatement(selClause, CONSTANT_DEMO_USERS);
    firstSelect.setGroup(groupClause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRowsAndCols(metaQuery, "testBasicGroupByClauseOk", 2, 3);

    results.toString();
  }

  @Test
  public void testFullGroupByClauseOk() {

    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.gender,count(*),sum(users.age),avg(users.age),min(users.age),max(users.age) FROM demo.users GROUP BY users.gender;");

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier(CONSTANT_USERS_GENDER)),
            new SelectionSelector(new SelectorGroupBy(GroupByFunction.COUNT,
                new SelectorIdentifier("*"))), new SelectionSelector(new SelectorGroupBy(
                GroupByFunction.SUM, new SelectorIdentifier(CONSTANT_USERS_AGE))),
            new SelectionSelector(new SelectorGroupBy(GroupByFunction.AVG, new SelectorIdentifier(
                CONSTANT_USERS_AGE))), new SelectionSelector(new SelectorGroupBy(
                GroupByFunction.MIN, new SelectorIdentifier(CONSTANT_USERS_AGE))),
            new SelectionSelector(new SelectorGroupBy(GroupByFunction.MAX, new SelectorIdentifier(
                CONSTANT_USERS_AGE))));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    List<GroupBy> groupClause = new ArrayList<>();
    groupClause.add(new GroupBy(CONSTANT_USERS_GENDER));

    SelectStatement firstSelect = new SelectStatement(selClause, CONSTANT_DEMO_USERS);
    firstSelect.setGroup(groupClause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRowsAndCols(metaQuery, "testFullGroupByClauseOk", 2, 6);

    results.toString();
  }

  @Test
  public void testGroupByWithWrongAggregationFunctionFail() {

    MetaQuery metaQuery =
        new MetaQuery(
            "SELECT users.gender,sum(users.gender) FROM demo.users GROUP BY users.gender;");

    List<SelectionSelector> selectionSelectors =
        Arrays.asList(new SelectionSelector(new SelectorIdentifier(CONSTANT_USERS_GENDER)),
            new SelectionSelector(new SelectorGroupBy(GroupByFunction.SUM, new SelectorIdentifier(
                CONSTANT_USERS_GENDER))));

    SelectionClause selClause = new SelectionList(new SelectionSelectors(selectionSelectors));
    List<GroupBy> groupClause = new ArrayList<>();
    groupClause.add(new GroupBy(CONSTANT_USERS_GENDER));

    SelectStatement firstSelect = new SelectStatement(selClause, CONSTANT_DEMO_USERS);
    firstSelect.setGroup(groupClause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    Result result = validateError(metaQuery, "testGroupByWithWrongAggregationFunctionFail");
    assertTrue(result.hasError(), "Error expected");
  }

  @Test
  public void testSelectAllOk() {

    MetaQuery metaQuery =
        new MetaQuery("SELECT * FROM demo.users WHERE email = 'name_2@domain.com';");

    // Fields to retrieve
    SelectionClause selectionClause = new SelectionList(new SelectionAsterisk());

    // Where clause
    List<Relation> clause = new ArrayList<>();
    Relation relation = new RelationCompare("email", "=", new StringTerm("name_2@domain.com"));
    clause.add(relation);
    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setWhere(clause);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRowsAndCols(metaQuery, "testSelectAllOk", 1, 6);

    results.toString();
  }

  @Test
  public void testSimpleOrderByOk() {

    MetaQuery metaQuery = new MetaQuery("SELECT * FROM demo.users ORDER BY users.age;");

    // Fields to retrieve
    SelectionClause selectionClause = new SelectionList(new SelectionAsterisk());

    // Order by clause
    List<Ordering> orderFieldsList = new ArrayList<>();
    Ordering order = new Ordering(CONSTANT_USERS_AGE);
    orderFieldsList.add(order);

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setOrder(orderFieldsList);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRowsAndCols(metaQuery, "testSimpleOrderByOk", 16, 6);

    results.toString();
  }

  @Test
  public void testMultipleOrderByOk() {

    MetaQuery metaQuery =
        new MetaQuery("SELECT * FROM demo.users ORDER BY users.gender, users.age;");

    // Fields to retrieve
    SelectionClause selectionClause = new SelectionList(new SelectionAsterisk());

    // Order by clause
    List<Ordering> orderFieldsList = new ArrayList<>();
    orderFieldsList.add(new Ordering(CONSTANT_USERS_GENDER));
    orderFieldsList.add(new Ordering(CONSTANT_USERS_AGE));

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setOrder(orderFieldsList);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRowsAndCols(metaQuery, "testMultipleOrderByOk", 16, 6);

    results.toString();
  }

  @Test
  public void testComplexQueryWithSimpleOrderByOk() {

    MetaQuery metaQuery = new MetaQuery("SELECT * FROM demo.users ORDER BY users.name;");

    // Fields to retrieve
    SelectionClause selectionClause = new SelectionList(new SelectionAsterisk());

    // Order by clause
    List<Ordering> orderFieldsList = new ArrayList<>();
    orderFieldsList.add(new Ordering(CONSTANT_USERS_GENDER));
    orderFieldsList.add(new Ordering(CONSTANT_USERS_AGE));

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setOrder(orderFieldsList);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRowsAndCols(metaQuery, "testComplexQueryWithSimpleOrderByOk", 16, 6);

    results.toString();
  }

  @Test
  public void testDescOrderByOk() {

    MetaQuery metaQuery =
        new MetaQuery("SELECT users.gender FROM demo.users ORDER BY users.gender DESC;");

    // Fields to retrieve
    SelectionSelectors selectionSelectors = new SelectionSelectors();
    selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier(
        CONSTANT_USERS_GENDER)));
    SelectionClause selectionClause = new SelectionList(selectionSelectors);

    // Order by clause
    List<Ordering> orderFieldsList = new ArrayList<>();
    orderFieldsList.add(new Ordering(CONSTANT_USERS_GENDER, true, OrderDirection.DESC));

    SelectStatement firstSelect = new SelectStatement(selectionClause, CONSTANT_DEMO_USERS);
    firstSelect.setLimit(10000);
    firstSelect.setOrder(orderFieldsList);
    firstSelect.validate(metadataManager, null);

    // Query execution
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);
    Result results = validateRowsAndCols(metaQuery, "testDescOrderByOk", 16, 1);

    results.toString();
  }
}
