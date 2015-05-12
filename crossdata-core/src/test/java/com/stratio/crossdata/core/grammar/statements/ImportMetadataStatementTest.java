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


import com.stratio.crossdata.core.grammar.ParsingTest;
import org.testng.annotations.Test;

public class ImportMetadataStatementTest extends ParsingTest{

    /**
     * NOTE: "Explain Plan" is an API call, so the parser should always fail.
     */

    @Test
    public void basicDiscoverMetadata() {
        String inputText = "DISCOVER METADATA ON CLUSTER prodCluster;";
        String expectedText = "DISCOVER METADATA ON CLUSTER cluster.prodCluster;";
        testRegularStatement(inputText, expectedText, "basicDiscoverMetadata");
    }

    @Test
    public void basicImportCatalogs() {
        String inputText = "IMPORT CATALOGS FROM CLUSTER prodCluster;";
        String expectedText = "IMPORT CATALOGS FROM CLUSTER cluster.prodCluster;";
        testRegularStatement(inputText, expectedText, "basicImportCatalogs");
    }

    @Test
    public void basicImportCatalog() {
        String inputText = "IMPORT CATALOG catName FROM CLUSTER prodCluster;";
        String expectedText = "IMPORT CATALOG catName FROM CLUSTER cluster.prodCluster;";
        testRegularStatement(inputText, expectedText, "basicImportCatalog");
    }

    @Test
    public void wrongImportCatalog() {
        String inputText = "IMPORT CATALOG catName.table FROM CLUSTER prodCluster;";
        testParserFails(inputText,"basicImportCatalog");
    }

    @Test
    public void basicImportTable() {
        String inputText = "IMPORT TABLE catName.tableName FROM CLUSTER prodCluster;";
        String expectedText = "IMPORT TABLE catName.tableName FROM CLUSTER cluster.prodCluster;";
        testRegularStatement(inputText, expectedText, "basicImportTable");
    }



}
