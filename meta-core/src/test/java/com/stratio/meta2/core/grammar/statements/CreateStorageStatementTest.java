/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
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
