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

package com.stratio.crossdata.core.grammar.statements;

import org.testng.annotations.Test;

import com.stratio.crossdata.core.grammar.ParsingTest;

public class InsertIntoStatementTest extends ParsingTest {

    @Test
    public void insertInto() {
        String inputText =
                "INSERT INTO mycatalog.tablename (ident1, ident2) VALUES ('term1', 'term2') "
                        + "IF NOT EXISTS;";
        String expectText =
                "INSERT INTO mycatalog.tablename (mycatalog.tablename.ident1, mycatalog.tablename.ident2) VALUES ('term1', 'term2') "
                        + "IF NOT EXISTS;";
        testRegularStatement(inputText, expectText, "insertInto");
    }

    @Test
    public void insertIntoNegativeInteger() {
        String inputText =
                "INSERT INTO mycatalog.tablename (ident1, ident2) VALUES (1, -4);";
        String expectText = "INSERT INTO mycatalog.tablename (mycatalog.tablename.ident1, mycatalog.tablename.ident2) VALUES (1, -4);";
        testRegularStatement(inputText, expectText, "insertIntoNegativeInteger");
    }

    @Test
    public void insertIntoUsing() {
        String inputText =
                "INSERT INTO mycatalog.tablename (ident1, ident2) VALUES (-3.75, 'term2') "
                        + "IF NOT EXISTS WITH {'COMPACT_STORAGE': true, 'prop1': '{innerTerm: result}'};";
        String expectText =
                "INSERT INTO mycatalog.tablename (mycatalog.tablename.ident1, mycatalog.tablename.ident2) VALUES (-3.75, 'term2') "
                        + "IF NOT EXISTS WITH {'COMPACT_STORAGE'=true, 'prop1'='{innerTerm: result}'};";
        testRegularStatement(inputText, expectText, "insertInto");
    }

    @Test
    public void insertInto2() {
        String inputText = "[test], " +
                "INSERT INTO tablename (column1, column2) VALUES ('value1', 60)"
                + " IF NOT EXISTS WHEN column2 > 10;";
        String expectText =
                "INSERT INTO test.tablename (test.tablename.column1, test.tablename.column2) VALUES ('value1', 60)"
                        + " IF NOT EXISTS WHEN test.tablename.column2 > 10;";
        testRegularStatement(inputText, expectText, "insertInto2");
    }

    @Test
    public void insertIntoAllValueTypes() {
        String inputText =
                "INSERT INTO mycatalog.tablename (c1, c2, c3, c4, c5) VALUES ('text', 'quoted_text', 123, 1.23, true);";
        String expectText = "INSERT INTO mycatalog.tablename (mycatalog.tablename.c1, mycatalog.tablename.c2, mycatalog.tablename.c3, mycatalog.tablename.c4, mycatalog.tablename.c5) VALUES ('text', 'quoted_text', 123, 1.23, true);";
        testRegularStatementSession("demo", inputText, expectText, "insertIntoAllValueTypes");
    }

    @Test
    public void wrongIntoToken() {
        String inputText =
                "INSERT INTI mycatalog.tablename (ident1, ident2) VALUES(term1, term2)"
                        + " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
        testParserFails(inputText, "wrongIntoToken");
    }

    @Test
    public void insertIntoWrongValuesToken() {
        String inputText =
                "INSERT INTO mycatalog.tablename (ident1, ident2) VALUED (term1, term2)"
                        + " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
        testParserFails(inputText, "insertIntoWrongValuesToken");
    }

    @Test
    public void insertIntoWrongNumberOfValues() {
        String inputText =
                "INSERT INTO mycatalog.tablename (ident1, ident2, ident3) VALUES ('term1', 'term2', 55, false);";
        testParserFails(inputText, "insertIntoWrongNumberOfValues");
    }

    @Test
    public void insertIntoWithDecimalNumber() {
        String inputText =
                "INSERT INTO mycatalog.tablename (ident1, ident2, ident3) VALUES ('term1', true, -55.0);";
        String expectedText = "INSERT INTO mycatalog.tablename (mycatalog.tablename.ident1, mycatalog.tablename.ident2, mycatalog.tablename.ident3) VALUES ('term1', true, -55.0);";
        testRegularStatementSession("demo", inputText, expectedText, "insertIntoWithDecimalNumber");
    }

    @Test
    public void insertIntoSelect() {
        String inputText =
                "INSERT INTO tablename (ident1, ident2) SELECT c.a, c.b from c "
                        + "IF NOT EXISTS WHEN ident3 = true WITH {'innerTerm': 'result'};";
        String expectText =
                "INSERT INTO demo.tablename (demo.tablename.ident1, demo.tablename.ident2) "
                        + "SELECT demo.c.a, demo.c.b FROM demo.c "
                        + "IF NOT EXISTS WHEN demo.tablename.ident3 = true WITH {'innerTerm'='result'};";
        testRegularStatementSession("demo", inputText, expectText, "insertIntoSelect");
    }

    @Test
    public void insertIntoWithListType() {
        String inputText =
                "INSERT INTO table1 (id, options) VALUES (25, ['payment', 'shipment']);";
        String expectText =
                "INSERT INTO demo.table1 (demo.table1.id, demo.table1.options) VALUES (25, ('payment', 'shipment'));";
        testRegularStatementSession("demo", inputText, expectText, "insertIntoSelect");
    }

}
