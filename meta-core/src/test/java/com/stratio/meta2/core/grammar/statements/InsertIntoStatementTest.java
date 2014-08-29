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

public class InsertIntoStatementTest extends ParsingTest {

  @Test
  public void insertInto() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES (term1, term2) "
        + "IF NOT EXISTS;";
    String expectText = "INSERT INTO mykeyspace.tablename (mykeyspace.tablename.ident1, mykeyspace.tablename.ident2) VALUES (term1, term2) "
                        + "IF NOT EXISTS;";
    testRegularStatement(inputText, expectText, "insertInto");
  }

  @Test
  public void insertIntoUsing() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES (term1, term2) "
        + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    String expectText = "INSERT INTO mykeyspace.tablename (mykeyspace.tablename.ident1, mykeyspace.tablename.ident2) VALUES (term1, term2) "
                        + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    testRegularStatement(inputText, expectText, "insertInto");
  }

  @Test
  public void insertInto2() {
    String inputText =
        "INSERT INTO tablename (column1, column2) VALUES (value1, value2)"
        + " IF NOT EXISTS USING TTL = 10;";
    String expectText = "INSERT INTO <unknown_name>.tablename (<unknown_name>.tablename.column1, <unknown_name>.tablename.column2) VALUES (value1, value2)"
                        + " IF NOT EXISTS USING TTL = 10;";
    testRegularStatement(inputText, expectText, "insertInto2");
  }

  @Test
  public void insertIntoAllValueTypes() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (c1, c2, c3, c4, c5) VALUES (text, 'quoted_text', 123, 1.23, true);";
    String expectText = "INSERT INTO mykeyspace.tablename (mykeyspace.tablename.c1, mykeyspace.tablename.c2, mykeyspace.tablename.c3, mykeyspace.tablename.c4, mykeyspace.tablename.c5) VALUES (text, 'quoted_text', 123, 1.23, true);";
    testRegularStatementSession("demo", inputText, expectText, "insertIntoAllValueTypes");
  }

  @Test
  public void wrongIntoToken() {
    String inputText =
        "INSERT INTI mykeyspace.tablename (ident1, ident2) VALUES(term1, term2)"
        + " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    testRecoverableError(inputText, "wrongIntoToken");
  }

  @Test
  public void insertIntoWrongValuesToken() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUED (term1, term2)"
        + " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    testParseFails(inputText, "insertIntoWrongValuesToken");
  }

  @Test
  public void insertIntoSelect() {
    String inputText =
        "INSERT INTO tablename (ident1, ident2) SELECT c.a, c.b from c "
        + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    String expectText = "INSERT INTO demo.tablename (demo.tablename.ident1, demo.tablename.ident2) SELECT c.a, c.b from c "
                        + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    testRegularStatementSession("demo", inputText, expectText, "insertIntoSelect");
  }

}
