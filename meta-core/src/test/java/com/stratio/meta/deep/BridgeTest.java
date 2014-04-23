/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.deep;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.statements.InsertIntoStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.core.utils.*;
import com.stratio.meta.deep.exceptions.MetaDeepException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

public class BridgeTest extends BasicCoreCassandraTest {

    protected static Executor executor = null;

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        EngineConfig config = initConfig();
        executor = new Executor(_session, new DeepSparkContext(config.getSparkMaster(), config.getJobName()),config);
    }

    public static EngineConfig initConfig(){
        String [] cassandraHosts = {"127.0.0.1"};
        EngineConfig engineConfig = new EngineConfig();
        engineConfig.setCassandraHosts(cassandraHosts);
        engineConfig.setCassandraPort(9042);
        engineConfig.setSparkMaster("local");
        engineConfig.setJobName("testJob");
        return engineConfig;
    }

    public Result validateOk(MetaQuery metaQuery, String methodName){
        MetaQuery result = executor.executeQuery(metaQuery);
        assertNotNull(result.getResult(), "Result null - " + methodName);
        assertFalse(result.hasError(), "Deep execution failed - " + methodName);
        return result.getResult();
    }

    public Result validateRows(MetaQuery metaQuery, String methodName, int expectedNumber){
        QueryResult result = (QueryResult) validateOk(metaQuery, methodName);
        if(expectedNumber > 0){
            assertEquals(result.getResultSet().size(), expectedNumber, methodName + ":" + result.getResultSet().size() + " rows found, " + expectedNumber + " rows expected.");
        } else {
            assertNull(result.getResultSet(), methodName + ": Result should be null");
        }

        return result;
    }

    public void validateFail(MetaQuery metaQuery, String methodName){
        try {
            MetaQuery result = executor.executeQuery(metaQuery);
        } catch (MetaDeepException ex) {
            System.out.println(ex.getMessage());
        }
        //assertNotNull(result, "Result null - " + methodName);
        //assertTrue(result.hasError(), "Deep execution failed - " + methodName);
    }

    // TESTS FOR CORRECT PLANS
   @Test
    public void testInnerJoin(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name=users_info.link_name;");

        // ADD MAIN STATEMENT
        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users_info.info")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.age")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("users.name", "users_info.link_name");
        InnerJoin join = new InnerJoin("demo.users_info", fields);
        SelectStatement ss = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                true, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );
        metaQuery.setStatement(ss);
        System.out.println("DEEP TEST (Query): " + metaQuery.getQuery());
        System.out.println("DEEP TEST (Stmnt): "+metaQuery.getStatement().toString());

        // FIRST SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("age")));
        selectionClause = new SelectionList(selectionSelectors);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // SECOND SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("link_name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("info")));
        selectionClause = new SelectionList(selectionSelectors);
        SelectStatement secondSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users_info", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // INNER JOIN
        fields = new HashMap<String, String>();
        fields.put("users.name", "users_info.link_name");
        join = new InnerJoin("", fields);
        SelectStatement joinSelect = new SelectStatement(
                null, // SelectionClause selectionClause
                "", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

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
    public void testEqualsFind(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.email=name_1@domain.com;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("email", "=", new StringTerm("name_1@domain.com"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);
        QueryResult result = (QueryResult) validateOk(metaQuery, "testEqualsFind");
        assertEquals(result.getResultSet().size(),1);
    }

    @Test
    public void testNotEqual(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.email<>name_1@domain.com;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("email", "<>", new StringTerm("name_1@domain.com"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);
        QueryResult result = (QueryResult) validateOk(metaQuery, "testEqualsFind");
        assertEquals(result.getResultSet().size(),15);
    }

    @Test
    public void testGreaterThan(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age>100;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("age", ">", new IntegerTerm("100"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);
        validateOk(metaQuery, "testGreater");
    }

    @Test
    public void testGreaterEqualThan(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age>=100;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("age", ">=", new IntegerTerm("100"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);
        validateOk(metaQuery, "testGreaterEqualThan");
    }

    @Test
    public void testLessThan(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age<100;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("age", "<", new IntegerTerm("100"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);
        validateOk(metaQuery, "testLessThan");
    }

    @Test
    public void testLessEqualThan(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.age<=100;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("age", "<=", new IntegerTerm("100"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
        validateOk(metaQuery, "testLessEqualThan");
    }

    //@Test
    public void select_columns_inner_join_and_where(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, types.boolean_column, users.age " +
                "FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column WHERE types.int_column > 104;");

        // ADD MAIN STATEMENT
        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("types.boolean_column")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.age")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("users.name", "types.varchar_column");
        InnerJoin join = new InnerJoin("demo.types", fields);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("types.int_column", ">", new IntegerTerm("104"));
        clause.add(relation);

        SelectStatement ss = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                true, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );
        metaQuery.setStatement(ss);
        System.out.println("DEEP TEST (Query): " + metaQuery.getQuery());
        System.out.println("DEEP TEST (Stmnt): "+ metaQuery.getStatement().toString());

        // FIRST SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.age")));
        selectionClause = new SelectionList(selectionSelectors);

        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // SECOND SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("types.varchar_column")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("types.boolean_column")));
        selectionClause = new SelectionList(selectionSelectors);

        clause = new ArrayList<>();
        relation = new RelationCompare("int_column", ">", new IntegerTerm("104"));
        clause.add(relation);

        SelectStatement secondSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.types", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // INNER JOIN
        fields = new HashMap<String, String>();
        fields.put("users.name", "types.varchar_column");
        join = new InnerJoin("", fields);
        SelectStatement joinSelect = new SelectStatement(
                null, // SelectionClause selectionClause
                "", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // CREATE ROOT
        Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);
        validateRows(metaQuery, "select_columns_inner_join_and_where", 5);
    }

    @Test
    public void select_asterisk_inner_join_and_where(){
        MetaQuery metaQuery = new MetaQuery("SELECT * FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column" +
                " WHERE users.email = 'name_4@domain.com';");

        // ADD MAIN STATEMENT
        SelectionClause selectionClause = new SelectionList(new SelectionAsterisk());
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("users.name", "types.varchar_column");
        InnerJoin join = new InnerJoin("demo.types", fields);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("users.email", "=", new StringTerm("name_4@domain.com"));
        clause.add(relation);

        SelectStatement ss = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                true, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );
        metaQuery.setStatement(ss);
        System.out.println("DEEP TEST (Query): " + metaQuery.getQuery());
        System.out.println("DEEP TEST (Stmnt): "+ metaQuery.getStatement().toString());

        // FIRST SELECT
        clause = new ArrayList<>();
        relation = new RelationCompare("email", "=", new StringTerm("name_4@domain.com"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                true, clause, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // SECOND SELECT
        SelectStatement secondSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.types", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // INNER JOIN
        fields = new HashMap<String, String>();
        fields.put("users.name", "types.varchar_column");
        join = new InnerJoin("", fields);
        SelectStatement joinSelect = new SelectStatement(
                null, // SelectionClause selectionClause
                "", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // CREATE ROOT
        Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);
        validateRows(metaQuery, "select_asterisk_inner_join_and_where", 1);
    }

    // TESTS FOR WRONG PLANS
    /*
    @Test
    public void insert_into_with_deep(){
        MetaQuery metaQuery = new MetaQuery("INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES " +
                "('name_10', 'male', 'name_10@domain.com', 20, false, '');");

        List<ValueCell> cellValues = new ArrayList<>();
        cellValues.add(new StringTerm("name_10"));
        cellValues.add(new StringTerm("male"));
        cellValues.add(new StringTerm("name_10@domain.com"));
        cellValues.add(new IntegerTerm("20"));
        cellValues.add(new BooleanTerm("false"));
        cellValues.add(new StringTerm(""));

        InsertIntoStatement iis = new InsertIntoStatement(
                "demo.users", // String tableName
                Arrays.asList("name", "gender", "email", "age", "bool", "phrase"), // List<String> ids
                null, // SelectStatement selectStatement
                cellValues, // List<ValueCell> cellValues
                false, // boolean ifNotExists
                false, null, // boolean optsInc, List<Option> options
                InsertIntoStatement.TYPE_VALUES_CLAUSE // int typeValues
        );

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, iis));
        metaQuery.setPlan(tree);

        validateRows(metaQuery, "insert_into_with_deep", 0);
    }

    @Test
    public void select_columns_inner_join_with_wrong_selected_column(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, types.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = users_info.link_name;");

        // ADD MAIN STATEMENT
        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("types.info")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.age")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("users.name", "users_info.link_name");
        InnerJoin join = new InnerJoin("demo.users_info", fields);

        SelectStatement ss = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                true, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );
        metaQuery.setStatement(ss);
        System.out.println("DEEP TEST (Query): " + metaQuery.getQuery());
        System.out.println("DEEP TEST (Stmnt): "+ metaQuery.getStatement().toString());

        // FIRST SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("age")));
        selectionClause = new SelectionList(selectionSelectors);

        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // SECOND SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("link_name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("info")));
        selectionClause = new SelectionList(selectionSelectors);

        SelectStatement secondSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users_info", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // INNER JOIN
        fields = new HashMap<String, String>();
        fields.put("users.name", "users_info.link_name");
        join = new InnerJoin("", fields);
        SelectStatement joinSelect = new SelectStatement(
                null, // SelectionClause selectionClause
                "", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // CREATE ROOT
        Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);

        validateFail(metaQuery, "select_columns_inner_join_with_wrong_selected_column");
    }


    @Test
    public void select_columns_inner_join_with_wrong_table_in_map(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = types.varchar_column;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users_info.info")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("users.age")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("users.name", "types.varchar_column");
        InnerJoin join = new InnerJoin("demo.users_info", fields);
        SelectStatement ss = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                true, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );
        metaQuery.setStatement(ss);
        System.out.println("DEEP TEST (Query): " + metaQuery.getQuery());
        System.out.println("DEEP TEST (Stmnt): "+metaQuery.getStatement().toString());

        // FIRST SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("age")));
        selectionClause = new SelectionList(selectionSelectors);
        SelectStatement firstSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // SECOND SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("link_name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("info")));
        selectionClause = new SelectionList(selectionSelectors);
        SelectStatement secondSelect = new SelectStatement(
                selectionClause, // SelectionClause selectionClause
                "demo.users_info", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                false, null, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // INNER JOIN
        fields = new HashMap<String, String>();
        fields.put("users.name", "types.varchar_column");
        join = new InnerJoin("", fields);
        SelectStatement joinSelect = new SelectStatement(
                null, // SelectionClause selectionClause
                "", // String tableName
                false, null, // boolean windowInc, WindowSelect window
                true, join, // boolean joinInc, InnerJoin join
                false, null, // boolean whereInc, ArrayList<Relation> where
                false, null, // boolean orderInc, ArrayList<Ordering> order
                false, null, // boolean groupInc, GroupBy group
                false, 10000, // boolean limitInc, int limit
                false // boolean disableAnalytics
        );

        // CREATE ROOT
        Tree tree = new Tree(new MetaStep(MetaPath.DEEP, joinSelect));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));

        // ADD CHILD
        tree.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);

        validateFail(metaQuery, "select_columns_inner_join_with_wrong_columns");
    }

    @Test
    public void select_columns_inner_join_with_nonexistent_column(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.comment " +
                "FROM demo.users INNER JOIN demo.users_info ON types.varchar_column = users.name;");
        validateFail(metaQuery, "select_columns_inner_join_with_wrong_columns");
    }

    //@Test
    public void select_inner_join_with_nonexistent_where(){
        MetaQuery metaQuery = new MetaQuery("SELECT * FROM demo.users INNER JOIN demo.users_info" +
                " ON users_info.link_name = users.name " +
                "WHERE types.int_column = 105;");
        validateFail(metaQuery, "select_inner_join_with_nonexistent_where");
    }
    */
}
