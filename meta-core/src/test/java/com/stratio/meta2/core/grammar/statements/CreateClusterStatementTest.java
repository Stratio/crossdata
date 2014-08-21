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

import com.stratio.meta.core.grammar.ParsingTest;

import org.testng.annotations.Test;

/**
 * Create cluster parsing tests.
 */
public class CreateClusterStatementTest extends ParsingTest{

  @Test
  public void createClusterWithoutOptions() {
    String inputText = "CREATE CLUSTER dev ON DATASTORE \"db\";";
    testParseFails(inputText, "createClusterWithoutOptions");
  }

  @Test
  public void createClusterBasicDoubleQuote() {
    String inputText = "CREATE CLUSTER dev ON DATASTORE \"db\""
                       + " WITH OPTIONS {\"host\":\"127.0.0.1\",port:1234};";
    String expectedText = inputText;
    testRegularStatement(inputText, expectedText, "createClusterBasicDoubleQuote");
  }

  @Test
  public void createClusterBasicSingleQuote() {
    String inputText = "CREATE CLUSTER dev ON DATASTORE 'db'"
                       + " WITH OPTIONS {\"host1\":\"127.0.0.1\"};";
    String expectedText = inputText;
    testRegularStatement(inputText, expectedText, "createClusterBasicSingleQuote");
  }

  @Test
  public void createClusterBasicIfNotExists() {
    String inputText = "CREATE CLUSTER IF NOT EXISTS dev ON DATASTORE 'db'"
                       + " WITH OPTIONS {\"host1\":\"127.0.0.1\"};";
    String expectedText = inputText;
    testRegularStatement(inputText, expectedText, "createClusterBasicIfNotExists");
  }

  @Test
  public void createClusterMissingEndBracket() {
    String inputText = "CREATE CLUSTER dev ON DATASTORE \"db\""
                       + " WITH OPTIONS {host:127.0.0.1;";
    testParseFails(inputText, "createClusterMissingEndBracket");
  }

  @Test
  public void createClusterMissingBrackets() {
    String inputText = "CREATE CLUSTER dev ON DATASTORE \"db\""
                       + " WITH OPTIONS host:127.0.0.1;";
    testParseFails(inputText, "createClusterMissingBrackets");
  }

}
