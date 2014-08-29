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

import com.stratio.meta.core.grammar.ParsingTest;
import org.testng.annotations.Test;

public class UpdateTableStatementTest extends ParsingTest {

  @Test
  public void updateBasic() {
    String inputText = "UPDATE table1 SET field1 = value1 WHERE field3 = value3;";
    String expectedText = "UPDATE <unknown_name>.table1 SET <unknown_name>.table1.field1 = value1 WHERE <unknown_name>.table1.field3 = value3;";
    testRegularStatement(inputText, expectedText, "updateBasic");
  }

  @Test
  public void updateTablename() {
    String inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                       + " WHERE ident3 IN (term3, term4) IF field1 = 25;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateTablename");
  }

  @Test
  public void updateWhere() {
    String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                       + " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateWhere");
  }

  @Test
  public void updateFull() {
    String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                       + " field2 = value2 WHERE field3 = value3 AND field4 = value4"
                       + " IF field5 = transaction_value5;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateFull");
  }

  @Test
  public void updateForInvalidAssignment(){
    String inputText = "UPDATE table1 SET field1 = value1 WHERE field3: value3;";
    testRecoverableError(inputText, "updateForInvalidAssignment");
  }

  @Test
  public void updateWrongSpelling(){
    String inputText = "UPDDATE table1 SET field1 = value1 WHERE field3: value3;";
    testParseFails(inputText, "updateWrongSpelling");
  }

  @Test
  public void updateWhereUsingAnd() {
    String inputText = "UPDATE table1 USING TTL = 400 AND TTL2 = 400 SET field1 = value1,"
                       + " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateWhereUsingAnd");
  }

  @Test
  public void updateWhereWithCollectionMap() {
    String inputText = "UPDATE table1 SET emails[admin] = myemail@mycompany.org WHERE field3 = value3;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateWhereWithCollectionMap");
  }

  @Test
  public void updateWhereWithCollectionSet() {
    String inputText = "UPDATE table1 SET emails = emails + {myemail@mycompany.org} WHERE field3 = value3;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateWhereWithCollectionSet");
  }

  @Test
  public void updateWhereWithCollectionList() {
    String inputText = "UPDATE table1 SET emails = emails + [myemail@mycompany.org] WHERE field3 = value3;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateWhereWithCollectionList");
  }

  @Test
  public void updateTablenameIfAnd() {
    String inputText = "UPDATE tablename SET ident1 = term1, ident2 = term2"
                       + " WHERE ident3 IN (term3, term4) IF field3 = 26 AND field2 = 25;";
    String expectedText = "";
    testRegularStatement(inputText, expectedText, "updateTablenameIfAnd");

    inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field3 = 26 AND field2 = 25;";
    expectedText = "";
    testRegularStatement(inputText, expectedText, "updateTablenameIfAnd");
  }

}
