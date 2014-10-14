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

/**
 * Create cluster parsing tests.
 */
public class AttachClusterStatementTest extends ParsingTest {

    @Test
    public void createClusterWithoutOptions() {
        String inputText = "ATTACH CLUSTER dev ON DATASTORE \"db\";";
        testParserFails(inputText, "createClusterWithoutOptions");
    }

    @Test
    public void createClusterBasicDoubleQuote() {
        String inputText = "ATTACH CLUSTER dev ON DATASTORE db"
                + " WITH OPTIONS {\"host\": \"127.0.0.1\", 'port': 1234};";
        String expectedText = "ATTACH CLUSTER cluster.dev ON DATASTORE datastore.db"
                + " WITH OPTIONS {'host'='127.0.0.1', 'port'=1234};";
        testRegularStatement(inputText, expectedText, "createClusterBasicDoubleQuote");
    }

    @Test
    public void createClusterBasicSingleQuote() {
        String inputText = "ATTACH CLUSTER dev ON DATASTORE db"
                + " WITH OPTIONS {\"host1\": \"127.0.0.1\"};";
        String expectedText = "ATTACH CLUSTER cluster.dev ON DATASTORE datastore.db"
                + " WITH OPTIONS {'host1'='127.0.0.1'};";
        testRegularStatement(inputText, expectedText, "createClusterBasicSingleQuote");
    }

    @Test
    public void createClusterBasicIfNotExists() {
        String inputText = "ATTACH CLUSTER IF NOT EXISTS dev ON DATASTORE db"
                + " WITH OPTIONS {\"host1\": \"127.0.0.1\"};";
        String expectedText = "ATTACH CLUSTER IF NOT EXISTS cluster.dev ON DATASTORE datastore.db"
                + " WITH OPTIONS {'host1'='127.0.0.1'};";
        testRegularStatement(inputText, expectedText, "createClusterBasicIfNotExists");
    }

    @Test
    public void attachClusterSimple() {
        String inputText = "ATTACH CLUSTER production_madrid ON DATASTORE cassandra WITH OPTIONS {'host':" +
                " '127.0.0.1', 'port': 9160, 'mode': \"random\"};";
        String expectedText = "ATTACH CLUSTER cluster.production_madrid ON DATASTORE datastore.cassandra WITH OPTIONS {'host'='127.0.0.1', 'port'=9160, 'mode'='random'};";
        testRegularStatement(inputText, expectedText, "attachClusterSimple");
    }

    @Test
    public void attachClusterIfNotExists() {
        String inputText = "ATTACH CLUSTER IF NOT EXISTS productionMadrid ON DATASTORE cassandra WITH OPTIONS " +
                "{'host': '127.0.0.1', \"port\": 9160, 'exhaustive': false};";
        String expectedText = "ATTACH CLUSTER IF NOT EXISTS cluster.productionMadrid ON DATASTORE datastore" +
                ".cassandra WITH " +
                "OPTIONS {'host'='127.0.0.1', 'port'=9160, 'exhaustive'=false};";
        testRegularStatement(inputText, expectedText, "attachClusterIfNotExists");
    }

    @Test
    public void attachClusterWrongClusterName() {
        String inputText = "ATTACH CLUSTER ^productionMadrid ON DATASTORE 'cassandra' WITH OPTIONS {'host': '127.0.0.1'};";
        testParserFails(inputText, "attachClusterWrongName");
    }

    @Test
    public void attachClusterWrongDataStore() {
        String inputText = "ATTACH CLUSTER productionMadrid ON DATASTORE {mongodb} WITH OPTIONS {'host': '127.0.0.1'};";
        testParserFails(inputText, "attachClusterWrongDataStore");
    }

    @Test
    public void attachClusterWrongJson() {
        String inputText = "ATTACH CLUSTER productionMadrid ON DATASTORE 'cassandra' WITH OPTIONS {mainClass: com.stratio.cluster.executor, 25};";
        testParserFails(inputText, "attachClusterWrongJson");
    }

    @Test
    public void createClusterMissingEndBracket() {
        String inputText = "ATTACH CLUSTER dev ON DATASTORE \"db\""
                + " WITH OPTIONS {host:127.0.0.1;";
        testParserFails(inputText, "createClusterMissingEndBracket");
    }

    @Test
    public void createClusterMissingBrackets() {
        String inputText = "ATTACH CLUSTER dev ON DATASTORE \"db\""
                + " WITH OPTIONS host:127.0.0.1;";
        testParserFails(inputText, "createClusterMissingBrackets");
    }

}
