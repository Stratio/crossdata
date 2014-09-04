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

import com.stratio.meta2.core.grammar.ParsingTest;
import org.testng.annotations.Test;

public class TruncateStatementTest extends ParsingTest {

  @Test
  public void truncateTable1() {
    String inputText = "TRUNCATE usersTable;";
    String expectedText = "TRUNCATE <unknown_name>.usersTable;";
    testRegularStatement(inputText, expectedText, "truncateTable1");
  }

  @Test
  public void truncateTable2() {
    String inputText = "[demo], TRUNCATE usersTable;";
    String expectedText = "TRUNCATE demo.usersTable;";
    testRegularStatement(inputText, expectedText, "truncateTable2");
  }

  @Test
  public void truncateWrongIdentifier(){
    String inputText = "TRUNCATE companyKS..usersTable;";
    testParserFails(inputText, "truncateWrongIdentifier");
  }

}
