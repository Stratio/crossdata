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

package com.stratio.crossdata.driver.querybuilder;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.testng.annotations.Test;

/**
 * Test for the Java QueryBuilder.
 */
public class QueryBuilderJavaTest {

    @Test
    public void selectFrom(){
        String expected = "SELECT * FROM table";
        Select s = QueryBuilder.selectAll().from("table");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectFrom2Columns(){
        String expected = "SELECT col1, col2 FROM table";
        Select s = QueryBuilder.select("col1", "col2").from("table");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectSelection(){
        String expected = "SELECT col1, col2, col3 AS alias3 FROM table";
        Selection selection = new Selection("col1").and("col2").and("col3", "alias3");
        Select s = QueryBuilder.select(selection).from("table");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectFromWhere(){
        String expected = "SELECT * FROM table WHERE id = 42";
        Query s = QueryBuilder.selectAll().from("table").where("id = 42");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectFromWhere2(){
        String expected = "SELECT * FROM table WHERE id = 42 AND name = 'crossdata'";
        Query s = QueryBuilder.selectAll().from("table").where("id = 42").and("name = 'crossdata'");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectWindowTime(){
        String expected = "SELECT * FROM table WITH WINDOW 1 min WHERE id = 42";
        Query s = QueryBuilder.selectAll().from("table").withWindow("1 min").where("id = 42");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectJoin(){
        String expected = "SELECT * FROM table1 "
                + "INNER JOIN table2 ON id1 = id2 "
                + "WHERE name = 'crossdata'";
        Query s = QueryBuilder.selectAll().from("table1")
                .join("table2").on("id1 = id2")
                .where("name = 'crossdata'");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectInnerJoin(){
        String expected = "SELECT * FROM table1 "
                + "INNER JOIN table2 ON id1 = id2 "
                + "WHERE name = 'crossdata'";
        Query s = QueryBuilder.selectAll().from("table1")
                .innerJoin("table2").on("id1 = id2")
                .where("name = 'crossdata'");
        assertEquals(s.toString(), expected, "Query does not match");
    }

    @Test
    public void selectComplex(){
        String expected = "SELECT col1, col2 FROM table1 "
                + "INNER JOIN table2 ON id1 = id2 "
                + "WHERE col1 = 'value1' "
                + "ORDER BY col3 "
                + "GROUP BY col4";
        Query s = QueryBuilder.select("col1", "col2")
                .from("table1")
                .join("table2").on("id1 = id2")
                .where("col1 = 'value1'")
                .orderBy("col3")
                .groupBy("col4");
        assertEquals(s.toString(), expected, "Query does not match");
    }


}
