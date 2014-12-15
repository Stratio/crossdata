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

public class CreateIndexStatementTest extends ParsingTest {

    // CREATE <type_index>? INDEX (IF NOT EXISTS)? <identifier>? ON <tablename> '(' <identifier> (',' <identifier>)* ')'
    // ( USING <string> )? WITH OPTIONS? (<maps> AND <maps>...) ';'
    //DEFAULT → Usual inverted index, Hash index. (By default).
    //LUCENE → Full text index.
    //CUSTOM → custom index. (new feature for release 2)

    @Test
    public void createIndexDefaultBasic() {
        String inputText = "[demo], CREATE DEFAULT INDEX index1 ON table1(field1, field2);";
        String expectedText = "CREATE DEFAULT INDEX index1 ON demo.table1(demo.table1.field1, demo.table1.field2);";
        testRegularStatement(inputText, expectedText, "createIndexDefaultBasic");
    }

    @Test
    public void createIndexDefaultIfNotExist() {
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON tester.table1(field1, field2);";
        String expectedText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON tester.table1(tester.table1.field1, tester.table1.field2);";
        testRegularStatementSession("demo", inputText, expectedText, "createIndexDefaultIfNotExist");
    }

    @Test
    public void createIndexDefaultUsing() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1(field1, field2) " +
                "WITH {\"class\": \"com.company.INDEX.class\"};";
        String expectedText = "CREATE DEFAULT INDEX index1 ON test.table1(test.table1.field1, " +
                "test.table1.field2) WITH {'class'='com.company.INDEX.class'};";
        testRegularStatementSession("test", inputText, expectedText, "createIndexDefaultUsing");
    }

    @Test
    public void createIndexLucene() {
        String inputText = "CREATE FULL_TEXT INDEX demo_banks ON banks"
                + "(day, entry_id, latitude, longitude, name, address, tags)"
                + " WITH {'schema': "
                + "'{default_analyzer: \"org.apache.lucene.analysis.standard.StandardAnalyzer\","
                + "fields: "
                + "{day: {type: \"date\", pattern: \"yyyy-MM-dd\"},"
                + " entry_id: {type: \"uuid\"}, latitude: {type: \"double\"},"
                + " longitude: {type: \"double\"}, name: {type: \"text\"},"
                + " address: {type: \"string\"}, tags: {type: \"boolean\"}}}'};";
        String expectedText = "CREATE FULL_TEXT INDEX demo_banks " +
                "ON demo.banks"
                + "(demo.banks.day, demo.banks.entry_id, demo.banks.latitude, demo.banks.longitude, demo.banks.name, demo.banks.address, demo.banks.tags)"
                + " WITH {'schema'="
                + "'{default_analyzer: \"org.apache.lucene.analysis.standard.StandardAnalyzer\","
                + "fields: "
                + "{day: {type: \"date\", pattern: \"yyyy-MM-dd\"},"
                + " entry_id: {type: \"uuid\"}, latitude: {type: \"double\"},"
                + " longitude: {type: \"double\"}, name: {type: \"text\"},"
                + " address: {type: \"string\"}, tags: {type: \"boolean\"}}}'};";
        testRegularStatementSession("demo", inputText, expectedText, "createIndexLucene");
    }

    @Test
    public void createIndexDefaultAll() {
        String inputText = "[demo], CREATE DEFAULT INDEX IF NOT EXISTS index1 ON table1(field1, " +
                "field2) WITH {'class': 'com.company.INDEX.class', 'key1': 'val1'};";
        String expectedText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON demo.table1(demo.table1.field1, " +
                "demo.table1.field2) WITH {'class'='com.company.INDEX.class', 'key1'='val1'};";
        testRegularStatement(inputText, expectedText, "createIndexDefaultAll");
    }

    @Test
    public void createDefaultIndexWithOptions2() {
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON demo.table1(field1, field2)" +
                " WITH {'class': 'com.company.INDEX.class', 'key1': 'val1', 'key2': 'val2'};";
        String expectedText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON demo.table1(demo.table1.field1, " +
                "demo.table1.field2) WITH {'class'='com.company.INDEX.class', 'key1'='val1', 'key2'='val2'};";
        testRegularStatement(inputText, expectedText, "createIndexWithOptions2");
    }

    @Test
    public void createLuceneIndexWithOptions2() {
        String inputText = "CREATE FULL_TEXT INDEX IF NOT EXISTS index1 ON table1(field1, field2) WITH {'key1': 'val1', 'key2': 'val2'};";
        String expectedText =
                "CREATE FULL_TEXT INDEX IF NOT EXISTS index1 ON demo.table1(demo.table1.field1, " +
                        "demo.table1.field2) WITH {'key1'='val1', 'key2'='val2'};";
        testRegularStatementSession("demo", inputText, expectedText, "createIndexWithOptions2");
    }

    @Test
    public void createIndexWrongOptionAssignment() {
        String inputText = "CREATE LUCENE INDEX index1 ON table1(field1, field2) WITH OPTIONS opt1:val1;";
        testParserFails(inputText, "createIndexWrongOptionAssignment");
    }

    @Test
    public void createIndexDefaultBasicWithSpaceBeforeSemicolon() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1(field1; field2);";
        testParserFails(inputText, "createIndexDefaultBasicWithSpaceBeforeSemicolon");
    }

    @Test
    public void createDefaultIndexLowercase() {
        String inputText = "create default INDEX index1 ON table1(field1, field2);";
        String expectedText = "CREATE DEFAULT INDEX index1 ON test.table1(test.table1.field1, test.table1.field2);";
        testRegularStatementSession("test", inputText, expectedText, "createDefaultIndexLowercase");
    }

    @Test
    public void createLuceneIndexLowercase() {
        String inputText = "[DEMO], create full_text index index1 on table1(field1, field2);";
        String expectedText = "create full_text index index1 on " +
                "demo.table1(demo.table1.field1, demo.table1.field2);";
        testRegularStatement(inputText, expectedText, "createLuceneIndexLowercase");
    }

}
