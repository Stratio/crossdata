/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
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

public class DetachConnectorStatementTest extends ParsingTest {

    @Test
    public void detachConnectorSimple() {
        String inputText = "DETACH CONNECTOR myConnector FROM myCluster;";
        String expectedText = "DETACH CONNECTOR connector.myConnector FROM cluster.myCluster;";
        testRegularStatement(inputText, expectedText, "detachConnectorSimple");
    }

    @Test
    public void detachConnectorWrongConnector() {
        String inputText = "DETACH CONNECTOR $myConnector FROM myCLuster;";
        testParserFails(inputText, "detachConnectorWrongConnector");
    }

    @Test
    public void detachConnectorWrongCluster() {
        String inputText = "DETACH CONNECTOR myConnector FROM =myCluster;";
        testParserFails(inputText, "detachConnectorWrongCluster");
    }

    @Test
    public void detachConnectorMissingCluster() {
        String inputText = "DETACH CONNECTOR myConnector;";
        testParserFails(inputText, "detachConnectorMissingCluster");
    }

}

