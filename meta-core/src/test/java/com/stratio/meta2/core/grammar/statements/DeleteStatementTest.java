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

public class DeleteStatementTest extends ParsingTest {

  @Test
  public void deleteWhere() {
    String inputText = "[demo], DELETE FROM table1 WHERE field1 = 'value1';";
    String expectedText = "DELETE FROM demo.table1 WHERE demo.table1.field1 = 'value1';";
    testRegularStatement(inputText, expectedText, "deleteWhere");
  }

  @Test
  public void deleteSelection() {
    String inputText = "[demo], DELETE FROM table1 WHERE field1 = 'value1';";
    String expectedText = "DELETE FROM demo.table1 WHERE demo.table1.field1 = 'value1';";
    testRegularStatement(inputText, expectedText, "deleteSelection");
  }

  @Test
  public void deleteFull() {
    String inputText = "[demo], DELETE FROM table1 WHERE field1 = 'value1' AND field2 = 'value2';";
    String expectedText = "DELETE FROM demo.table1 WHERE demo.table1.field1 = 'value1' AND demo.table1.field2 = 'value2';";
    testRegularStatement(inputText, expectedText, "deleteFull");
  }

  @Test
  public void deleteInvalidOperator() {
    String inputText = "DELETE FROM table1 WHERE field1 = value1 OR field2 = value2;";
    testParserFails("demo", inputText, "deleteInvalidOperator");
  }

  @Test
  public void deleteWrongPropertyAssignment(){
    String inputText = "DELETE FROM table1 WHERE field1: value1;";
    testParserFails("demo", inputText, "deleteWrongPropertyAssignment");
  }

  @Test
  public void deleteWrongLeftTermTypeInWhere(){
    String inputText = "DELETE FROM table1 WHERE \"col1\" = value1;";
    testParserFails("demo", inputText, "deleteWrongLeftTermTypeInWhere");
  }

}
