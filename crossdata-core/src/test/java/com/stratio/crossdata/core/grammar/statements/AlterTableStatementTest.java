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

public class AlterTableStatementTest extends ParsingTest {

    @Test
    public void alterTableBasic() {
        String inputText = "ALTER TABLE demo.myTable ALTER column1 TYPE int;";
        String expectedText = "ALTER TABLE demo.myTable ALTER demo.myTable.column1 TYPE int;";
        testRegularStatement(inputText, expectedText, "alterTableBasic");
    }

    @Test
    public void alterTableCatalog() {
        String inputText = "ALTER TABLE demo.myTable ALTER column1 TYPE int;";
        String expectedText = "ALTER TABLE demo.myTable ALTER demo.myTable.column1 TYPE int;";
        testRegularStatement(inputText, expectedText, "alterTableCatalog");
    }

    @Test
    public void alterTableCatalogWithSession1() {
        String inputText = "ALTER TABLE demo.myTable ALTER column1 TYPE int;";
        String expectedText = "ALTER TABLE demo.myTable ALTER demo.myTable.column1 TYPE int;";
        testRegularStatementSession("clients", inputText, expectedText, "alterTableCatalogWithSession1");
    }

    @Test
    public void alterTableCatalogWithWrongColumnType() {
        String inputText = "ALTER TABLE demo.myTable ALTER column1 TYPE date;";
        testParserFails("clients", inputText, "alterTableCatalogWithWrongColumnType");
    }

    @Test
    public void alterTableCatalogWithSession2() {
        String inputText = "ALTER TABLE myTable ALTER column1 TYPE int;";
        String expectedText = "ALTER TABLE clients.myTable ALTER clients.myTable.column1 TYPE int;";
        testRegularStatementSession("clients", inputText, expectedText, "alterTableCatalogWithSession2");
    }

    @Test
    public void alterTableCatalogWithSession3() {
        String inputText = "ALTER TABLE myTable ALTER myTable.column1 TYPE int;";
        String expectedText = "ALTER TABLE clients.myTable ALTER clients.myTable.column1 TYPE int;";
        testRegularStatementSession("clients", inputText, expectedText, "alterTableCatalogWithSession3");
    }

    @Test
    public void alterTableBasic1() {
        String inputText = "ALTER TABLE demo.myTable ADD column1 int;";
        String expectedText = "ALTER TABLE demo.myTable ADD demo.myTable.column1 int;";
        testRegularStatement(inputText, expectedText, "alterTableBasic1");
    }

    @Test
    public void alterTableBasic2() {
        String inputText = "ALTER TABLE demo.myTable DROP column1;";
        String expectedText = "ALTER TABLE demo.myTable DROP demo.myTable.column1;";
        testRegularStatement(inputText, expectedText, "alterTableBasic2");
    }

    @Test
    public void alterTableWithOptionAndProperties() {
        String inputText = "ALTER TABLE demo.myTable DROP column1 WITH {column1: 'force'};";
        String expectedText = "ALTER TABLE demo.myTable DROP demo.myTable.column1 WITH {'column1'='force'};";
        testRegularStatement(inputText, expectedText, "alterTableWithOptionAndProperties");
    }

    @Test
    public void alterTableBasic3() {
        String inputText = "ALTER TABLE demo.myTable WITH {'property1': 'value1', 'property2': 2, 'property3': 3.0};";
        String expectedText = "ALTER TABLE demo.myTable WITH {'property1'='value1', 'property2'=2, 'property3'=3.0};";
        testRegularStatement(inputText, expectedText, "alterTableBasic3");
    }

    @Test
    public void alterWrongPropertyIdentifier() {
        String inputText = "ALTER TABLE demo.myTable WITH {2property1: 'value1'};";
        testParserFails(inputText, "alterWrongPropertyIdentifier");
    }

}
