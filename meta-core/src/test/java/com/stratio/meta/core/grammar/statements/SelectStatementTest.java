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

public class SelectStatementTest extends ParsingTest {

  @Test
  public void selectStatement() {
    String inputText =
        "SELECT newtb.ident1 AS name1, myfunction(newtb.innerIdent, newtb.anotherIdent) AS functionName "
            + "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE newtb.ident1 LIKE whatever"
            + " ORDER BY newtb.id1 ASC GROUP BY newtb.col1 LIMIT 50 DISABLE ANALYTICS;";
    testRegularStatement(inputText, "selectStatement");
  }

  @Test
  public void selectStatementWindows() {
    for (String w : new String[] {"5 ROWS", "LAST", "5 SECONDS"}) {
      String inputText =
          "SELECT newtb.ident1 FROM newks.newtb WITH WINDOW " + w
              + " WHERE newtb.ident1 LIKE whatever;";
      testRegularStatement(inputText, "selectStatementWindows");
    }

    // TODO: add "S","M","H","D","s","m","h" and "d"
    // for(String t:new String[]{"S","M","H","D","s","m","h","d"}){
    for (String t : new String[] {"SECONDS", "MINUTES", "HOURS", "DAYS"}) {
      for (int i = 10; i-- > 2;) {
        String inputText =
            "SELECT newtb.ident1 FROM newks.newtb WITH WINDOW " + i + " " + t
                + " WHERE newtb.ident1 LIKE whatever;";
        testRegularStatement(inputText, "selectStatementWindows");
      }

    }
  }

  @Test
  public void selectStatementJoin() {

    String inputText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename t ON field1=field2 WHERE c.x = y;";

    String expectedText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename ON field1=field2 WHERE c.x = y;";

    testRegularStatement(inputText, expectedText, "selectStatementJoins");
  }

  @Test
  public void selectStatementJoinWithParenthesis() {

    String inputText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename t ON (field1=field2) WHERE c.x = y;";

    String expectedText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename ON field1=field2 WHERE c.x = y;";

    testRegularStatement(inputText, expectedText, "selectStatementJoins");
  }

  @Test
  public void selectStatementAliasedColumnsJoin() {

    String inputText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename t ON c.field1=tablename.field2 WHERE c.x = y;";

    String expectedText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename ON c.field1=tablename.field2 WHERE c.x = y;";

    testRegularStatement(inputText, expectedText, "selectStatementJoins");
  }

  @Test
  public void selectStatementAliasedInversedColumnsJoins() {

    String inputText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename t ON tablename.field2=c.field1 WHERE c.x = y;";

    String expectedText =
        "SELECT c.a, c.b FROM c INNER JOIN tablename ON tablename.field2=c.field1 WHERE c.x = y;";

    testRegularStatement(inputText, expectedText, "selectStatementJoins");

  }

  @Test
  public void selectStatementAliasedTableJoins() {

    String inputText =
        "SELECT c.a, c.b FROM table_c c INNER JOIN tablename t ON t.field2=c.field1 WHERE c.x = y;";

    String expectedText =
        "SELECT table_c.a, table_c.b FROM table_c INNER JOIN tablename ON tablename.field2=table_c.field1 WHERE table_c.x = y;";

    testRegularStatement(inputText, expectedText, "selectStatementJoins");
  }

  @Test
  public void selectStatementAliasedTableAndInversedColumnsJoins() {

    String inputText =
        "SELECT c.a, c.b FROM table_c c INNER JOIN tablename t ON c.field2=t.field1 WHERE c.x = y;";
    String expectedText =
        "SELECT table_c.a, table_c.b FROM table_c INNER JOIN tablename ON table_c.field2=tablename.field1 WHERE table_c.x = y;";
    testRegularStatement(inputText, expectedText, "selectStatementJoins");

  }

  @Test
  public void selectStatementCombineOrderby() {
    for (String s : new String[] {"ASC", "DESC", "ASC, b.anothercolumn ASC",
        "ASC, b.anothercolumn DESC", "DESC, b.anothercolumn DESC", "DESC, b.anothercolumn ASC"}) {
      String inputText =
          "SELECT b.a FROM b ORDER BY b.id1 " + s + " GROUP BY b.col1 LIMIT 50 DISABLE ANALYTICS;";
      testRegularStatement(inputText, "selectStatementCombineOrderby");
    }

  }

  @Test
  public void basicSelectAsterisk(){
    String inputText = "SELECT * FROM table1;";
    String expectedText = "SELECT * FROM <unknown_name>.table1;";
    testRegularStatement(inputText, expectedText, "basicSelectAsterisk");
  }

  @Test
  public void basicSelectAsteriskWithCatalog(){
    String inputText = "SELECT * FROM catalog1.table1;";
    testRegularStatement(inputText, "basicSelectAsteriskWithCatalog");
  }

  @Test
  public void singleColumn() {
    String inputText = "SELECT newtb.lucene FROM newks.newtb;";
    String expectedText = "SELECT <unknown_name>.newtb.lucene FROM newks.newtb;";
    testRegularStatement(inputText, expectedText, "singleColumn");
  }

  @Test
  public void singleColumnWithCatalog() {
    String inputText = "SELECT newks.newtb.lucene FROM newks.newtb;";
    testRegularStatement(inputText, "singleColumnWithCatalog");
  }

  @Test
  public void singleColumnWithSessionCatalog() {
    String inputText = "SELECT newtb.lucene FROM newks.newtb;";
    String expectedText = "SELECT newks.newtb.lucene FROM newks.newtb;";
    testRegularStatementSession("newks", inputText, expectedText, "singleColumnWithSessionCatalog");
  }

  @Test
  public void selectWithTimeWindow() {
    String inputText =
        "SELECT table1.column1 FROM table1 WITH WINDOW 5 SECONDS WHERE table1.column2 = 3;";
    testRegularStatement(inputText, "selectWithTimeWindow");
  }

  @Test
  public void selectWithMatch() {
    String inputText = "SELECT * FROM demo.emp WHERE emp.first_name MATCH s2o;";
    testRegularStatement(inputText, "selectWithMatch");
  }

  @Test
  public void selectWrongLikeWord() {
    String inputText =
        "SELECT newtb.ident1, myfunction(newtb.innerIdent, newtb.anotherIdent) LIKE ident1 FROM newks.newtb;";
    testParseFails(inputText, "selectWrongLikeWord");
  }

  @Test
  public void selectSelectors() {
    for (String c : new String[] {"COUNT(*)", "myUDF(table0.field0)", "table0.field0"}) {
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

  @Test
  public void selectGroupedWithCountOk() {

    String inputText = "SELECT users.gender, COUNT(*) FROM demo.users GROUP BY users.gender;";
    testRegularStatement(inputText, "selectGroupedWithCountOk");
  }

  @Test
  public void selectAliasGroupedWithCountOk() {

    String inputText = "SELECT users.gender as g, COUNT(*) FROM demo.users GROUP BY users.gender;";
    testRegularStatement(inputText, "selectAliasGroupedWithCountOk");
  }

  @Test
  public void selectGroupedWithSumOk() {

    String inputText = "SELECT users.gender, SUM(users.age) FROM demo.users GROUP BY users.gender;";
    testRegularStatement(inputText, "selectGroupedWithSumOk");
  }

  @Test
  public void selectSimpleOrderByOk() {

    String inputText = "SELECT users.gender FROM demo.users ORDER BY users.age;";
    testRegularStatement(inputText, "selectSimpleOrderByOk");
  }

  @Test
  public void selectMultipleOrderByOk() {

    String inputText = "SELECT users.gender FROM demo.users ORDER BY users.age, users.gender;";
    testRegularStatement(inputText, "selectSimpleOrderByOk");
  }

  @Test
  public void selectSimpleOrderByWithoutTableOk() {

    String inputText = "SELECT users.gender FROM demo.users ORDER BY users.age;";
    testRegularStatement(inputText, "selectSimpleOrderByOk");
  }

  @Test
  public void selectMultipleOrderByWithoutTableOk() {

    String inputText = "SELECT users.gender FROM demo.users ORDER BY users.age, users.gender;";
    testRegularStatement(inputText, "selectSimpleOrderByOk");
  }

  @Test
  public void selectMultipleOrderByWithoutTableMultipleDirectionOk() {

    String inputText =
        "SELECT users.gender FROM demo.users ORDER BY users.age ASC, users.gender DESC;";
    testRegularStatement(inputText, "selectSimpleOrderByOk");
  }

  @Test
  public void selectSimpleOrderByWithAscDirectionOk() {

    String inputText = "SELECT users.gender FROM demo.users ORDER BY users.age ASC;";
    testRegularStatement(inputText, "selectSimpleOrderByWithAscDirectionOk");
  }

  @Test
  public void selectSimpleOrderByWithDescDirectionOk() {

    String inputText = "SELECT users.gender FROM demo.users ORDER BY users.age DESC;";
    testRegularStatement(inputText, "selectSimpleOrderByWithDescDirectionOk");
  }

  @Test
  public void selectSimpleOrderByFail() {

    String inputText = "SELECT users.gender FROM demo.users ORDER BY sum(users.age);";
    testRecoverableError(inputText, "selectGroupedWithSumOk");
  }

  @Test
  public void testSimpleQueryWithAliasesOk() {

    String inputText = "SELECT users.gender as genero FROM demo.users;";

    testRegularStatement(inputText, "testSimpleGroupQueryWithAliasesOk");
  }

  @Test
  public void testSimpleGroupQueryWithAliasesOk() {

    String inputText =
        "SELECT users.gender, min(users.age) as minimo FROM demo.users GROUP BY users.gender;";

    testRegularStatement(inputText, "testSimpleGroupQueryWithAliasesOk");
  }


  @Test
  public void testComplexQueryWithAliasesOk() {

    String inputText =
        "SELECT users.age AS edad, users.gender AS genero, sum(users.age) AS suma, min(users.gender) AS minimo, count(*) AS contador FROM demo.users "
            + "WHERE users.age > 13 AND users.gender IN ('male', 'female') ORDER BY users.age DESC GROUP BY users.gender;";

    testRegularStatement(inputText, "testComplexQueryWithAliasesOk");
  }
}
