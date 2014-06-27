/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.grammar.statements;

import org.testng.annotations.Test;

import com.stratio.meta.core.grammar.ParsingTest;

public class InsertIntoStatementTest extends ParsingTest {

  @Test
  public void insertInto() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES (term1, term2) "
            + "IF NOT EXISTS;";
    testRegularStatement(inputText, "insertInto");
  }

  @Test
  public void insertIntoUsing() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES (term1, term2) "
            + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    testRegularStatement(inputText, "insertInto");
  }

  @Test
  public void insertInto2() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (column1, column2) VALUES (value1, value2)"
            + " IF NOT EXISTS USING TTL = 10;";
    testRegularStatement(inputText, "insertInto2");
  }

  @Test
  public void insertIntoAllValueTypes() {
    String inputText =
        "INSERT INTO mykeyspace.tablename (c1, c2, c3, c4, c5) VALUES (text, 'quoted_text', 123, 1.23, true);";
    testRegularStatement(inputText, "insertIntoAllValueTypes");
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
        "INSERT INTO mykeyspace.tablename (ident1, ident2) SELECT c.a, c.b from c "
            + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
    testRegularStatement(inputText, "insertIntoSelect");
  }

  // T_USING opt1= getOption ( T_AND optN= getOption )*



}