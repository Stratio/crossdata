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

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.core.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class BridgeTest extends BasicCoreCassandraTest {

    protected static Executor executor = null;

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        executor = new Executor(_session);
    }

    public Result validateOk(MetaQuery metaQuery, String methodName){
        MetaQuery result = executor.executeQuery(metaQuery);
        assertNotNull(result.getResult(), "Result null - " + methodName);
        assertFalse(result.hasError(), "Deep execution failed - " + methodName);
        return result.getResult();
    }

    public void validateFail(MetaQuery metaQuery, String methodName){
        MetaQuery result = executor.executeQuery(metaQuery);
        assertNotNull(result, "Result null - " + methodName);
        assertTrue(result.hasError(), "Deep execution failed - " + methodName);
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
        SelectStatement ss = new SelectStatement(selectionClause, "demo.users");
        ss.setJoin(join);
        ss.setLimit(10000);

        metaQuery.setStatement(ss);
        System.out.println("DEEP TEST (Query): " + metaQuery.getQuery());
        System.out.println("DEEP TEST (Stmnt): "+metaQuery.getStatement().toString());

        // FIRST SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("gender")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("age")));
        selectionClause = new SelectionList(selectionSelectors);

       SelectStatement firstSelect = new SelectStatement(selectionClause, "demo.users");;
       firstSelect.setLimit(10000);

        // SECOND SELECT
        selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("link_name")));
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("info")));
        selectionClause = new SelectionList(selectionSelectors);
       SelectStatement secondSelect = new SelectStatement(selectionClause, "demo.users_info");;
       ss.setLimit(10000);

        // INNER JOIN
        fields = new HashMap<String, String>();
        fields.put("users.name", "users_info.link_name");
        join = new InnerJoin("", fields);
        SelectStatement joinSelect = new SelectStatement("");
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
    public void testEqualsFind(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.name FROM demo.users WHERE users.email=name_1@domain.com;");

        SelectionSelectors selectionSelectors = new SelectionSelectors();
        selectionSelectors.addSelectionSelector(new SelectionSelector(new SelectorIdentifier("name")));
        SelectionClause selectionClause = new SelectionList(selectionSelectors);

        List<Relation> clause = new ArrayList<>();
        Relation relation = new RelationCompare("email", "=", new StringTerm("name_1@domain.com"));
        clause.add(relation);
        SelectStatement firstSelect = new SelectStatement(selectionClause, "demo.users");;
        firstSelect.setLimit(10000);
        firstSelect.setWhere(clause);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
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
        SelectStatement firstSelect = new SelectStatement(selectionClause, "demo.users");;
        firstSelect.setLimit(10000);
        firstSelect.setWhere(clause);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
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
        SelectStatement firstSelect = new SelectStatement(selectionClause, "demo.users");;
        firstSelect.setLimit(10000);
        firstSelect.setWhere(clause);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
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
        SelectStatement firstSelect = new SelectStatement(selectionClause, "demo.users");;
        firstSelect.setLimit(10000);
        firstSelect.setWhere(clause);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
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
        SelectStatement firstSelect = new SelectStatement(selectionClause, "demo.users");;
        firstSelect.setLimit(10000);
        firstSelect.setWhere(clause);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
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
        SelectStatement firstSelect = new SelectStatement(selectionClause, "demo.users");;
        firstSelect.setLimit(10000);
        firstSelect.setWhere(clause);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.DEEP, firstSelect));
        metaQuery.setPlan(tree);
        validateOk(metaQuery, "testLessEqualThan");
    }

    //@Test
    public void select_columns_inner_join_and_where(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, types.boolean_column, users.age " +
                "FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column WHERE types.int_column > 104;");
        validateOk(metaQuery, "select_columns_inner_join_and_where");
    }

    //@Test
    public void select_asterisk_inner_join_and_where(){
        MetaQuery metaQuery = new MetaQuery("SELECT * FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column" +
                " WHERE users.email = 'name_4@domain.com';");
        validateOk(metaQuery, "select_columns_inner_join_and_where");
    }

    // TESTS FOR WRONG PLANS

    //@Test
    public void insert_into_with_deep(){
        MetaQuery metaQuery = new MetaQuery("INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES " +
                "('name_10', 'male', 'name_10@domain.com', 20, false, '');");
        validateOk(metaQuery, "insert_into_with_deep");
    }

    //@Test
    public void select_columns_inner_join_with_wrong_selected_column(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, types.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = users_info.link_name;");
        validateOk(metaQuery, "select_columns_inner_join_with_wrong_selected_column");
    }

    //@Test
    public void select_columns_inner_join_with_wrong_table_in_map(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = types.varchar_column;");
        validateOk(metaQuery, "select_columns_inner_join_with_wrong_columns");
    }

    //@Test
    public void select_columns_inner_join_with_nonexistent_column(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.comment " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = types.varchar_column;");
        validateOk(metaQuery, "select_columns_inner_join_with_wrong_columns");
    }

}
