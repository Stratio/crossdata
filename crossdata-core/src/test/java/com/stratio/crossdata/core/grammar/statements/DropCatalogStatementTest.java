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

public class DropCatalogStatementTest extends ParsingTest {

    @Test
    public void dropCatalog() {
        String inputText = "drop catalog IF EXISTS mycatalog;";
        testRegularStatement(inputText, "dropCatalog");
    }

    @Test
    public void dropCatalogSimple() {
        String inputText = "DROP CATALOG myCatalog;";
        testRegularStatement(inputText, "dropCatalogSimple");
    }

    @Test
    public void dropCatalogIfExistsSimple() {
        String inputText = "DROP CATALOG IF EXISTS myCatalog;";
        testRegularStatement(inputText, "dropCatalogSimple");
    }

    @Test
    public void dropCatalogError() {
        String inputText = "DROP CATALOG _myCatalog";
        testParserFails(inputText, "dropCatalogError");
    }

    @Test
    public void dropWrongPlaceForIfExists() {
        String inputText = "DROP CATALOG mycatalog IF EXISTS;";
        testParserFails(inputText, "dropWrongPlaceForIfExists");
    }

}
