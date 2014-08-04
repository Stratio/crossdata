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

package com.stratio.meta.core.grammar.statements;

import org.testng.annotations.Test;

import com.stratio.meta.core.grammar.ParsingTest;

public class DescribeStatementTest extends ParsingTest {

  @Test
  public void describeKeyspaceBasic() {
    String inputText = "DESCRIBE KEYSPACE keyspace1;";
    testRegularStatement(inputText, "describeKeyspaceBasic");
  }

  @Test
  public void describeCurrentKeyspaceBasic() {
    String inputText = "DESCRIBE KEYSPACE;";
    testRegularStatement(inputText, "describeCurrentKeyspaceBasic");
  }

  @Test
  public void describeTableBasic() {
    String inputText = "DESCRIBE TABLE keyspace1.table1;";
    testRegularStatement(inputText, "describeTableBasic");
  }

  @Test
  public void describeTableFail() {
    String inputText = "DESCRIBE UNKNOWN keyspace1.table1;";
    testParseFails(inputText, "describeTableFail");
  }

  @Test
  public void describeKeyspacesBasic() {
    String inputText = "DESCRIBE KEYSPACES;";
    testRegularStatement(inputText, "describeKeyspaceBasic");
  }

  @Test
  public void describeTablesBasic() {
    String inputText = "DESCRIBE TABLES;";
    testRegularStatement(inputText, "describeTableBasic");
  }
}
