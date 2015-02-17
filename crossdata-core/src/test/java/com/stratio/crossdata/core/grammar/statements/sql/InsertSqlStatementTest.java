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

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.core.grammar.ParsingTest;
import com.stratio.crossdata.core.query.SqlParsedQuery;

public class InsertSqlStatementTest extends ParsingTest {

    @Test
    public void testInsertSQL() throws Exception {
        String inputText =
                "SQL: INSERT INTO mycatalog.tablename (ident1, ident2) VALUES ('term1', 'term2') "
                        + "IF NOT EXISTS;";
        String expectText =
                "INSERT INTO mycatalog.tablename (ident1, ident2) VALUES ('term1', 'term2') "
                        + "IF NOT EXISTS;";
        SqlParsedQuery parsedQuery = (SqlParsedQuery) testRegularStatement(inputText, expectText, "testInsertSQL");
        assertEquals(
                new TableName("mycatalog", "tablename"),
                parsedQuery.getStatement().getTableName(),
                "TableNames differ");
    }
}
