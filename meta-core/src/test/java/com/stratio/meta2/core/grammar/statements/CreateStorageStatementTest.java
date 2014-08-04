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
 * Create storage parsing tests.
 */
public class CreateStorageStatementTest extends ParsingTest{

  @Test
  public void createStorageWithoutConnector() {
    String inputText = "CREATE STORAGE dev ON DATASTORE \"db\";";
    testParseFails(inputText, "createStorageWithoutConnector");
  }

  @Test
  public void createStorageBasicDoubleQuote() {
    String inputText = "CREATE STORAGE dev ON DATASTORE \"db\""
                       + " USING CONNECTOR \"com....DBConnector\""
                       + " WITH OPTIONS {\"host\":\"127.0.0.1\",port:1234};";
    String expectedText = inputText.replace("\"", "")
        .replace("host", "'host'")
        .replace("127.0.0.1", "'127.0.0.1'");
    testRegularStatement(inputText, expectedText, "createStorageBasic");
  }

  @Test
  public void createStorageBasicSingleQuote() {
    String inputText = "CREATE STORAGE dev ON DATASTORE 'db'"
                       + " USING CONNECTOR 'com....DBConnector'"
                       + " WITH OPTIONS {\"host1\":\"127.0.0.1\"};";
    String expectedText = inputText.replace("\"", "").replace("'","")
        .replace("host1", "'host1'")
        .replace("127.0.0.1", "'127.0.0.1'");
    testRegularStatement(inputText, expectedText, "createStorageBasic");
  }

  @Test
  public void createStorageBasic2Connectors() {
    String inputText = "CREATE STORAGE dev ON DATASTORE \"db\""
                       + " USING CONNECTOR \"com....DBConnector\""
                       + " WITH OPTIONS {\"host1\":\"127.0.0.1\"}"
                       + " AND CONNECTOR \"com....DBConnector2\""
                       + " WITH OPTIONS {\"host2\":\"127.0.0.3\"};";
    String expectedText = inputText.replace("\"", "")
        .replace("host1", "'host1'")
        .replace("host2", "'host2'")
        .replace("127.0.0.1", "'127.0.0.1'")
        .replace("127.0.0.3", "'127.0.0.3'");
    testRegularStatement(inputText, expectedText, "createStorageBasic2Connectors");
  }

  @Test
  public void createStorageBasicIfNotExists() {
    String inputText = "CREATE STORAGE IF NOT EXISTS dev ON DATASTORE 'db'"
                       + " USING CONNECTOR 'com....DBConnector'"
                       + " WITH OPTIONS {\"host1\":\"127.0.0.1\"};";
    String expectedText = inputText.replace("\"", "").replace("'","")
        .replace("host1", "'host1'")
        .replace("127.0.0.1", "'127.0.0.1'");
    testRegularStatement(inputText, expectedText, "createStorageBasicIfNotExists");
  }

  @Test
  public void createStorageMissingEndBracket() {
    String inputText = "CREATE STORAGE dev ON DATASTORE \"db\""
                       + " USING CONNECTOR \"com....DBConnector\""
                       + " WITH OPTIONS {host:127.0.0.1;";
    testParseFails(inputText, "createStorageMissingEndBracket");
  }

  @Test
  public void createStorageMissingBrackets() {
    String inputText = "CREATE STORAGE dev ON DATASTORE \"db\""
                       + " USING CONNECTOR \"com....DBConnector\""
                       + " WITH OPTIONS host:127.0.0.1;";
    testParseFails(inputText, "createStorageMissingBrackets");
  }

}
