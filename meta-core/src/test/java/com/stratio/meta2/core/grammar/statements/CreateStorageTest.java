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
public class CreateStorageTest extends ParsingTest{

  @Test
  public void createStorageEmptyJSON() {
    String inputText = "CREATE STORAGE dev_environment1 WITH {};";
    testRegularStatement(inputText, "createStorageEmptyJSON");
  }

  @Test
  public void createStorageBasic() {
    String inputText = "CREATE STORAGE dev_environment1 WITH {"
                       + "\"hosts\":[\"127.0.0.1\",\"127.0.0.2\"],"
                       + "\"port\":1234,"
                       + "\"connectors\":[\"com.stratio.connector.cassandra.CassandraConnector\"]};";
    testRegularStatement(inputText, "createStorageBasic");
  }

  @Test
  public void createStorageBasic2Connectors() {
    String inputText = "CREATE STORAGE dev_environment1 WITH {\n"
                       + "\"hosts\":[\"127.0.0.1\",\"127.0.0.2\"],"
                       + "\"port\":1234,"
                       + "\"connectors\":["
                       + "\"com.stratio.connector.cassandra.CassandraConnector\","
                       + "\"com.stratio.connector.cassandra.DeepCassandraConnector\""
                       + "]};";
    testRegularStatement(inputText, "createStorageBasic2Connectors");
  }

  @Test
  public void createStorageBasicIfNotExists() {
    String inputText = "CREATE STORAGE IF NOT EXISTS dev_environment1 WITH {\n"
                       + "\"hosts\":[\"127.0.0.1\",\"127.0.0.2\"],"
                       + "\"port\":1234,"
                       + "\"connectors\":["
                       + "\"com.stratio.connector.cassandra.CassandraConnector\","
                       + "\"com.stratio.connector.cassandra.DeepCassandraConnector\""
                       + "]};";
    testRegularStatement(inputText, "createStorageBasic");
  }

  @Test
  public void createStorageMissingEndBracket() {
    String inputText = "CREATE STORAGE dev_environment1 WITH {empty;";
    testParseFails(inputText, "createStorageMissingEndBracket");
  }

  @Test
  public void createStorageMissingBrackets() {
    String inputText = "CREATE STORAGE dev_environment1 WITH empty;";
    testParseFails(inputText, "createStorageMissingBrackets");
  }

}
