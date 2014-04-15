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

package com.stratio.meta.core.deep;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.utils.MetaQuery;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BridgeTest extends BasicCoreCassandraTest {

    protected static Executor executor = null;

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        executor = new Executor(_session);
    }

    //@AfterClass

    public void validateOk(MetaQuery metaQuery, String methodName){
        MetaQuery result = executor.executeQuery(metaQuery);
        assertNotNull(result.getResult(), "Result null - " + methodName);
        assertFalse(result.hasError(), "Deep execution failed - " + methodName);
    }

    public void validateFail(MetaQuery metaQuery, String methodName){
        MetaQuery result = executor.executeQuery(metaQuery);
        assertNotNull(result, "Result null - " + methodName);
        assertTrue(result.hasError(), "Deep execution failed - " + methodName);
    }

    // CORRECT TESTS

    @Test
    public void select_columns_inner_join(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = users_info.link_name;");
        validateOk(metaQuery, "select_columns_inner_join");
    }

    @Test
    public void select_asterisk_inner_join(){
        MetaQuery metaQuery = new MetaQuery("SELECT * FROM demo.users INNER JOIN demo.users_info " +
                "ON users.name = users_info.link_name;");
        validateOk(metaQuery, "select_asterisk_inner_join");
    }

    @Test
    public void select_columns_inner_join_and_where(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, types.boolean_column, users.age " +
                "FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column WHERE types.int_column > 104;");
        validateOk(metaQuery, "select_columns_inner_join_and_where");
    }

    @Test
    public void select_asterisk_inner_join_and_where(){
        MetaQuery metaQuery = new MetaQuery("SELECT * FROM demo.users INNER JOIN demo.types ON users.name = types.varchar_column" +
                " WHERE users.email = 'name_4@domain.com';");
        validateOk(metaQuery, "select_columns_inner_join_and_where");
    }

    // WRONG TESTS
    @Test
    public void insert_into_with_deep(){
        MetaQuery metaQuery = new MetaQuery("INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES " +
                "('name_10', 'male', 'name_10@domain.com', 20, false, '');");
        validateOk(metaQuery, "insert_into_with_deep");
    }

    @Test
    public void select_columns_inner_join_with_wrong_selected_column(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, types.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = users_info.link_name;");
        validateOk(metaQuery, "select_columns_inner_join_with_wrong_selected_column");
    }

    @Test
    public void select_columns_inner_join_with_wrong_table_in_map(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.age " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = types.varchar_column;");
        validateOk(metaQuery, "select_columns_inner_join_with_wrong_columns");
    }

    @Test
    public void select_columns_inner_join_with_nonexistent_column(){
        MetaQuery metaQuery = new MetaQuery("SELECT users.gender, users_info.info, users.comment " +
                "FROM demo.users INNER JOIN demo.users_info ON users.name = types.varchar_column;");
        validateOk(metaQuery, "select_columns_inner_join_with_wrong_columns");
    }

}
