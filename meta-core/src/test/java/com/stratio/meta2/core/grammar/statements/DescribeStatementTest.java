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

package com.stratio.meta2.core.grammar.statements;

import org.testng.annotations.Test;

import com.stratio.meta2.core.grammar.ParsingTest;

public class DescribeStatementTest extends ParsingTest {

    @Test
    public void describeCatalogBasic() {
        String inputText = "DESCRIBE CATALOG catalog1;";
        testRegularStatement(inputText, "describeCatalogBasic");
    }

    @Test
    public void describeMissingCatalog() {
        String inputText = "DESCRIBE CATALOG;";
        testParserFails(inputText, "describeMissingCatalog");
    }

    @Test
    public void describeTableBasic() {
        String inputText = "DESCRIBE TABLE catalog1.table1;";
        testRegularStatement(inputText, "describeTableBasic");
    }

    @Test
    public void describeTableFail() {
        String inputText = "DESCRIBE UNKNOWN catalog1.table1;";
        testParserFails(inputText, "describeTableFail");
    }

    @Test
    public void describeCatalogsBasic() {
        String inputText = "DESCRIBE CATALOGS;";
        testRegularStatement(inputText, "describeCatalogsBasic");
    }

    @Test
    public void describeClustersBasic() {
        String inputText = "DESCRIBE CLUSTERS;";
        testRegularStatement(inputText, "describeClustersBasic");
    }

    @Test
    public void describeClusterBasic() {
        String inputText = "DESCRIBE CLUSTER myCluster;";
        String expectedText = "DESCRIBE CLUSTER cluster.myCluster;";
        testRegularStatement(inputText, expectedText, "describeClusterBasic");
    }

    @Test
    public void describeDataStoresBasic() {
        String inputText = "DESCRIBE DATASTORES;";
        testRegularStatement(inputText, "describeDataStoresBasic");
    }

    @Test
    public void describeDataStoreBasic() {
        String inputText = "DESCRIBE DATASTORE myDatastore;";
        String expectedText = "DESCRIBE DATASTORE datastore.myDatastore;";
        testRegularStatement(inputText, expectedText, "describeDataStoreBasic");
    }

    @Test
    public void describeConnectorsBasic() {
        String inputText = "DESCRIBE CONNECTORS;";
        testRegularStatement(inputText, "describeConnectorsBasic");
    }

    @Test
    public void describeConnectorBasic() {
        String inputText = "DESCRIBE CONNECTOR myConnector;";
        String expectedText = "DESCRIBE CONNECTOR connector.myConnector;";
        testRegularStatement(inputText, expectedText, "describeConnectorBasic");
    }

}
