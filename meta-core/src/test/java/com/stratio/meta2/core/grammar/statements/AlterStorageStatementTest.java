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
 * Alter storage parsing tests.
 */
public class AlterStorageStatementTest extends ParsingTest {

  @Test
  public void alterStorageEmptyJSON() {
    String inputText = "ALTER STORAGE dev_environment1 WITH {};";
    testRegularStatement(inputText, "alterStorageEmptyJSON");
  }

  @Test
  public void alterStorageBasic() {
    String inputText = "ALTER STORAGE dev_environment1 WITH {"
                       + "\"hosts\":[\"127.0.0.1\",\"127.0.0.2\"],"
                       + "\"port\":1234,"
                       + "\"connectors\":[\"com.stratio.connector.cassandra.CassandraConnector\"]};";
    testRegularStatement(inputText, "alterStorageBasic");
  }

}
