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

package com.stratio.crossdata.core.grammar.statements.sql;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.core.grammar.ParsingTest;
import com.stratio.crossdata.core.query.SqlParsedQuery;

public class SelectSqlStatementTest extends ParsingTest {

    @Test
    public void testBasicSelectSQL() throws Exception {
        String inputText =
                "SQL: SELECT * FROM mycatalog.tablename;";
        String expectText =
                "SELECT * FROM mycatalog.tablename;";
        SqlParsedQuery parsedQuery = (SqlParsedQuery) testRegularStatement(inputText, expectText, "testBasicSelectSQL");
        assertEquals(new TableName("mycatalog", "tablename"), parsedQuery.getStatement().getTableName(), "TableNames are not equal");
    }

    @Test
    public void testImplicitSelectSQL() throws Exception {
        String inputText =
                "SQL: SELECT * FROM mycatalog.tablename, mycatalog.bigworld;";
        String expectText =
                "SELECT * FROM mycatalog.tablename, mycatalog.bigworld;";
        SqlParsedQuery parsedQuery = (SqlParsedQuery) testRegularStatement(inputText, expectText, "testImplicitSelectSQL");

        List<TableName> expectedTables = new ArrayList<>();
        expectedTables.add(new TableName("mycatalog", "tablename"));
        expectedTables.add(new TableName("mycatalog", "bigworld"));

        assertEquals(
                expectedTables,
                parsedQuery.getStatement().getTables(),
                "TableNames are not equal");
    }

    @Test
    public void testMultipleImplicitSelectSQL() throws Exception {
        String inputText =
                "SQL: SELECT * FROM mycatalog.tablename, mycatalog.bigworld, mycatalog.cars;";
        String expectText =
                "SELECT * FROM mycatalog.tablename, mycatalog.bigworld, mycatalog.cars;";
        SqlParsedQuery parsedQuery = (SqlParsedQuery) testRegularStatement(inputText, expectText, "testMultipleImplicitSelectSQL");

        List<TableName> expectedTables = new ArrayList<>();
        expectedTables.add(new TableName("mycatalog", "tablename"));
        expectedTables.add(new TableName("mycatalog", "bigworld"));
        expectedTables.add(new TableName("mycatalog", "cars"));

        assertEquals(
                expectedTables,
                parsedQuery.getStatement().getTables(),
                "TableNames are not equal");
    }

    @Test
    public void testJoinSelectSQL() throws Exception {
        String inputText =
                "SQL: SELECT * FROM mycatalog.tablename FULL OUTER JOIN mycatalog.bigworld " +
                        "ON tablename.id = bigworld.id;";
        String expectText =
                "SELECT * FROM mycatalog.tablename FULL OUTER JOIN mycatalog.bigworld " +
                        "ON tablename.id = bigworld.id;";
        SqlParsedQuery parsedQuery = (SqlParsedQuery) testRegularStatement(inputText, expectText, "testJoinSelectSQL");

        List<TableName> expectedTables = new ArrayList<>();
        expectedTables.add(new TableName("mycatalog", "tablename"));
        expectedTables.add(new TableName("mycatalog", "bigworld"));

        assertEquals(
                expectedTables,
                parsedQuery.getStatement().getTables(),
                "TableNames are not equal");
    }

    @Test
    public void testMultipleJoinSelectSQL() throws Exception {
        String inputText =
                "SQL: SELECT * FROM mycatalog.tablename " +
                        "FULL OUTER JOIN mycatalog.bigworld ON tablename.id = bigworld.id " +
                        "FULL OUTER JOIN mycatalog.cars ON bigworld.id = cars.id;";
        String expectText =
                "SELECT * FROM mycatalog.tablename " +
                        "FULL OUTER JOIN mycatalog.bigworld ON tablename.id = bigworld.id " +
                        "FULL OUTER JOIN mycatalog.cars ON bigworld.id = cars.id;";
        SqlParsedQuery parsedQuery = (SqlParsedQuery) testRegularStatement(inputText, expectText, "testMultipleJoinSelectSQL");

        List<TableName> expectedTables = new ArrayList<>();
        expectedTables.add(new TableName("mycatalog", "tablename"));
        expectedTables.add(new TableName("mycatalog", "bigworld"));
        expectedTables.add(new TableName("mycatalog", "cars"));

        assertEquals(
                expectedTables,
                parsedQuery.getStatement().getTables(),
                "TableNames are not equal");
    }
}
