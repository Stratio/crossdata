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

public class DropIndexStatementTest extends ParsingTest {

  @Test
  public void basic() {
    String inputText = "DROP INDEX demo.index_name;";
    testRegularStatement(inputText, "basic");
  }

  @Test
  public void noKsOk() {
    String inputText = "DROP INDEX index_name;";
    testRegularStatement(inputText, "noKsOk");
  }

  @Test
  public void ifExists() {
    String inputText = "DROP INDEX IF EXISTS demo.index_name;";
    testRegularStatement(inputText, "ifExists");
  }

  @Test
  public void tokenOk() {
    String inputText = "DROP INDEX lucene;";
    testRegularStatement(inputText, "tokenOk");
  }

  @Test
  public void wrongNotTokenFail(){
    String inputText = "DROP INDEX IF NOT EXISTS index_name;";
    testParserFails(inputText, "wrongNotTokenFail");
  }

  @Test
  public void invalidNameFail(){
    String inputText = "DROP INDEX IF NOT EXISTS 123name;";
    testParserFails(inputText, "invalidNameFail");
  }

}
