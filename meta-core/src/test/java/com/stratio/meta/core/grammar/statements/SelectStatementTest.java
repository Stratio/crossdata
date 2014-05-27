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

public class SelectStatementTest extends ParsingTest {

  @Test
  public void selectStatement() {
    String inputText =
        "SELECT ident1 AS name1, myfunction(innerIdent, anotherIdent) AS functionName "
            + "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE ident1 LIKE whatever"
            + " ORDER BY id1 ASC GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
    testRegularStatement(inputText, "selectStatement");
  }

  @Test
  public void selectStatementWindows() {
    for (String w : new String[] {"5 ROWS", "LAST", "5 SECONDS"}) {
      String inputText =
          "SELECT ident1 FROM newks.newtb WITH WINDOW " + w + " WHERE ident1 LIKE whatever;";
      testRegularStatement(inputText, "selectStatementWindows");
    }

    // TODO: add "S","M","H","D","s","m","h" and "d"
    // for(String t:new String[]{"S","M","H","D","s","m","h","d"}){
    for (String t : new String[] {"SECONDS", "MINUTES", "HOURS", "DAYS"}) {
      for (int i = 10; i-- > 2;) {
        String inputText =
            "SELECT ident1 FROM newks.newtb WITH WINDOW " + i + " " + t
                + " WHERE ident1 LIKE whatever;";
        testRegularStatement(inputText, "selectStatementWindows");
      }

    }
  }


  @Test
  public void selectStatementJoins() {
    for (String jp : new String[] {"field1=field2", "field3=field4 AND field1=field2"}) {
      String inputText = "SELECT a, b FROM c INNER JOIN tablename ON " + jp + " WHERE x = y;";
      testRegularStatement(inputText, "selectStatementJoins");
    }

  }

  @Test
  public void selectStatementCombineOrderby() {
    for (String s : new String[] {"ASC", "DESC", "ASC, anothercolumn ASC",
        "ASC, anothercolumn DESC", "DESC, anothercolumn DESC", "DESC, anothercolumn ASC"}) {
      String inputText =
          "SELECT a FROM b ORDER BY id1 " + s + " GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
      testRegularStatement(inputText, "selectStatementCombineOrderby");
    }

  }

  @Test
  public void selectStatement2() {
    String inputText = "SELECT lucene FROM newks.newtb;";
    testRegularStatement(inputText, "selectStatement2");
  }

  @Test
  public void selectWithTimeWindow() {
    String inputText = "SELECT column1 FROM table1 WITH WINDOW 5 SECONDS WHERE column2 = 3;";
    testRegularStatement(inputText, "selectWithTimeWindow");
  }

  @Test
  public void selectWithMatch() {
    String inputText = "SELECT * FROM demo.emp WHERE first_name MATCH s2o;";
    testRegularStatement(inputText, "selectWithMatch");
  }

  @Test
  public void selectWrongLikeWord() {
    String inputText =
        "SELECT ident1, myfunction(innerIdent, anotherIdent) LIKE ident1 FROM newks.newtb;";
    testParseFails(inputText, "selectWrongLikeWord");
  }

  @Test
  public void selectSelectors() {
    for (String c : new String[] {"COUNT(*)", "myUDF(field0)", "field0"}) {
      String inputText = "SELECT " + c + " from table0;";
      testRegularStatement(inputText, "selectSelectors");
    }
  }

  @Test
  public void selectWithInClauseOk() {

    String inputText = "SELECT users.name FROM demo.users WHERE users.age IN (19, 31);";
    testRegularStatement(inputText, "selectWithInClauseOk");
  }

  @Test
  public void selectWithInClauseLongerOk() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.age IN (19, 31, 23, 90, 100);";
    testRegularStatement(inputText, "selectWithInClauseLongerOk");
  }

  @Test
  public void selectWithInClauseOneValueOk() {

    String inputText = "SELECT users.name FROM demo.users WHERE users.age IN (19);";
    testRegularStatement(inputText, "selectWithInClauseOneValueOk");
  }

  @Test
  public void selectWithBetweenClauseOk() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'aaaa_00@domain.com' AND 'zzzz_99@domain.com';";
    testRegularStatement(inputText, "selectWithBetweenClauseOk");
  }

  @Test
  public void selectWithBetweenClauseThreeValuesFail() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'aaaa_00@domain.com' AND 'zzzz_99@domain.com' AND 'wrong@domain.com';";
    testRecoverableError(inputText, "selectWithBetweenClauseThreeValuesFail");
  }

  @Test
  public void selectWithBetweenClauseOneValueFail() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'aaaa_00@domain.com';";
    testRecoverableError(inputText, "selectWithInClauseOneValueOk");
  }
}