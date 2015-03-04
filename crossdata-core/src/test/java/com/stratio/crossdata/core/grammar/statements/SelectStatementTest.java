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

package com.stratio.crossdata.core.grammar.statements;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.core.grammar.ParsingTest;

public class SelectStatementTest extends ParsingTest {

    //
    // Basic tests without WHERE clauses
    //

    @Test
    public void basicSelectAsterisk() {
        String inputText = "[test], SELECT * FROM table1;";
        String expectedText = "SELECT * FROM test.table1;";
        testRegularStatement(inputText, expectedText, "basicSelectAsterisk");
    }

    @Test
    public void basicSelectAsteriskWithLimit() {
        String inputText = "[test], SELECT * FROM table1 LIMIT 1;";
        String expectedText = "SELECT * FROM test.table1 LIMIT 1;";
        testRegularStatement(inputText, expectedText, "basicSelectAsterisk");
    }

    @Test
    public void basicSelectAsteriskWithCatalog() {
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
    public void functionSingleColumn() {
        String inputText = "SELECT sum(newtb.lucene) FROM newks.newtb;";
        String expectedText = "SELECT sum(<unknown_name>.newtb.lucene) AS sum FROM newks.newtb;";
        testRegularStatement(inputText, expectedText, "functionSingleColumn");
    }

    @Test
    public void function2SingleColumn() {
        String inputText = "SELECT myfunction(newtb.lucene) FROM newks.newtb;";
        String expectedText = "SELECT myfunction(<unknown_name>.newtb.lucene) AS myfunction FROM newks.newtb;";
        testRegularStatement(inputText, expectedText, "function2SingleColumn");
    }

    @Test
    public void singleColumnWithSessionCatalog() {
        String inputText = "SELECT newtb.lucene FROM newks.newtb;";
        String expectedText = "SELECT newks.newtb.lucene FROM newks.newtb;";
        testRegularStatementSession("newks", inputText, expectedText, "singleColumnWithSessionCatalog");
    }

    @Test
    public void singleColumnWithAliasWithSessionCatalog() {
        String inputText = "SELECT newtb.lucene AS c FROM newks.newtb;";
        String expectedText = "SELECT newks.newtb.lucene AS c FROM newks.newtb;";
        testRegularStatementSession("newks", inputText, expectedText, "singleColumnWithSessionCatalog");
    }

    @Test
    public void singleColumnWithAliasWithReservedWord() {
        String inputText = "SELECT newtb.lucene AS limit FROM newks.newtb;";
        String expectedText = "SELECT newks.newtb.lucene AS limit FROM newks.newtb;";
        testRegularStatementSession("newks", inputText, expectedText, "singleColumnWithAliasWithReservedWord");
    }

    @Test
    public void selectWithDistinctPlusAsterisk() {
        String inputText = "SELECT DISTINCT * FROM newks.newtb;";
        testParserFails("newks", inputText, "selectWithDistinctPlusAsterisk");
    }

    @Test
    public void selectAliasWithoutColumn() {
        String inputText = "SELECT tab2.col1 AS myAlis, AS aliasCol FROM table1;";
        testParserFails("newks", inputText, "selectAliasWithoutColumn");
    }

    @Test
    public void innerJoinWithTableAliases() {
        String inputText = "SELECT field1, field2 FROM demo.clients AS table1 INNER JOIN sales AS table2 ON identifier = codeID;";
        String expectedText = "SELECT <unknown_name>.<unknown_name>.field1, <unknown_name>.<unknown_name>.field2 FROM demo.clients AS table1 INNER JOIN demo.sales AS table2 ON <unknown_name>.<unknown_name>.identifier = <unknown_name>.<unknown_name>.codeID;";
        testRegularStatementSession("demo", inputText, expectedText, "innerJoinWithTableAliases");
    }

    @Test
    public void testSimpleQueryWithAliasesOk() {
        String inputText = "SELECT demo.users.gender as genero FROM demo.users;";
        Assert.assertNotNull(testRegularStatement(inputText,
                "testSimpleGroupQueryWithAliasesOk"), "regular statement error");
    }

    @Test
    public void basicSelectIntColumn() {
        String inputText = "SELECT 1 FROM test.table1;";
        String expectedText = "SELECT 1 FROM test.table1;";
        testRegularStatement(inputText, expectedText, "basicSelectIntColumn");
    }

    @Test
    public void basicSelectNegativeIntColumn() {
        String inputText = "SELECT -99 FROM test.table1;";
        String expectedText = "SELECT -99 FROM test.table1;";
        testRegularStatement(inputText, expectedText, "basicSelectNegativeIntColumn");
    }

    @Test
    public void basicSelectDoubleColumn() {
        String inputText = "SELECT 1.1234 FROM test.table1;";
        String expectedText = "SELECT 1.1234 FROM test.table1;";
        testRegularStatement(inputText, expectedText, "basicSelectDoubleColumn");
    }

    @Test
    public void basicSelectNegativeDoubleColumn() {
        String inputText = "SELECT -9.9876 FROM test.table1;";
        String expectedText = "SELECT -9.9876 FROM test.table1;";
        testRegularStatement(inputText, expectedText, "basicSelectNegativeDoubleColumn");
    }

    @Test
    public void basicSelectBooleanColumn() {
        String inputText = "[test], SELECT true FROM table1;";
        String expectedText = "SELECT true FROM test.table1;";
        testRegularStatement(inputText, expectedText, "basicSelectBooleanColumn");
    }

    @Test
    public void basicSelectQuotedLiteralColumn() {
        String inputText = "[test], SELECT \"literal\" FROM table1;";
        String expectedText = "SELECT 'literal' FROM test.table1;";
        testRegularStatement(inputText, expectedText, "basicSelectBooleanColumn");
    }

    //
    // Select with where clauses
    //

    @Test
    public void selectWithCompareRelationships() {
        String[] relationships = { "=", ">", "<", ">=", "<=", "MATCH" };
        for (String r : relationships) {
            String inputText = "SELECT * FROM demo.emp WHERE a " + r + " 5;";
            String expectedText = "SELECT * FROM demo.emp WHERE <unknown_name>.<unknown_name>.a " + r + " 5;";
            testRegularStatement(inputText, expectedText, "selectWithMatch");
        }
    }

    @Test
    public void selectWith2CompareRelationships() {
        String[] relationships = { "=", ">", "<", ">=", "<=", "MATCH" };
        for (String r : relationships) {
            String inputText = "SELECT * FROM demo.emp WHERE a " + r + " 5 AND b " + r + " 10;";
            String expectedText = "SELECT * FROM demo.emp WHERE <unknown_name>.<unknown_name>.a " + r
                    + " 5 AND <unknown_name>.<unknown_name>.b " + r + " 10;";
            testRegularStatement(inputText, expectedText, "selectWithMatch");
        }
    }

    @Test
    public void selectSimpleWithWhere() {
        String inputText = "SELECT * FROM demo.emp WHERE a = 10 AND b = 20 AND c = 30 AND d = 40;";
        String expectedText = "SELECT * FROM demo.emp WHERE <unknown_name>.<unknown_name>.a = 10 AND <unknown_name>.<unknown_name>.b = 20 AND <unknown_name>.<unknown_name>.c = 30 AND <unknown_name>.<unknown_name>.d = 40;";
        testRegularStatement(inputText, expectedText, "selectSimpleWithWhere");
    }

    @Test
    public void selectSimpleWithWhere2() {
        String inputText = "SELECT * FROM demo.emp WHERE a = 10 AND b = 20 AND c = 30 AND d = 40 AND e = 50;";
        String expectedText = "SELECT * FROM demo.emp WHERE <unknown_name>.<unknown_name>.a = 10 AND <unknown_name>.<unknown_name>.b = 20 AND <unknown_name>.<unknown_name>.c = 30 AND <unknown_name>.<unknown_name>.d = 40 AND <unknown_name>.<unknown_name>.e = 50;";
        testRegularStatement(inputText, expectedText, "selectSimpleWithWhere2");
    }

    //
    // Select with window
    //

    @Test
    public void selectWithTimeWindow() {
        String inputText = "[test], " +
                "SELECT table1.column1 FROM table1 WITH WINDOW 5 SECONDS WHERE table1.column2 = 3;";
        String expectedText =
                "SELECT test.table1.column1 FROM test.table1 WITH WINDOW 5 SECONDS WHERE test.table1.column2 = 3;";
        testRegularStatement(inputText, expectedText, "selectWithTimeWindow");
    }

    @Test
    public void selectWithTimeWindow2() {
        String inputText = "[test], " +
                "SELECT table1.column1 FROM table1 WITH WINDOW 1 min WHERE table1.column2 = 3;";
        String expectedText =
                "SELECT test.table1.column1 FROM test.table1 WITH WINDOW 1 MINUTES WHERE test.table1.column2 = 3;";
        testRegularStatement(inputText, expectedText, "selectWithTimeWindow2");
    }

    @Test
    public void selectStatementWindows() {
        for (String w : new String[] { "5 ROWS", "LAST", "5 SECONDS" }) {
            String inputText =
                    "SELECT newks.newtb.ident1 FROM newks.newtb WITH WINDOW " + w
                            + " WHERE newks.newtb.ident1 LIKE \"whatever\";";
            String expectedText =
                    "SELECT newks.newtb.ident1 FROM newks.newtb WITH WINDOW " + w
                            + " WHERE newks.newtb.ident1 LIKE 'whatever';";
            testRegularStatement(inputText, expectedText, "selectStatementWindows");
        }

        // TODO: add "S","M","H","D","s","m","h" and "d"
        // for(String t:new String[]{"S","M","H","D","s","m","h","d"}){
        for (String t : new String[] { "SECONDS", "MINUTES", "HOURS", "DAYS" }) {
            for (int i = 10; i-- > 2; ) {
                String inputText =
                        "SELECT newks.newtb.ident1 FROM newks.newtb WITH WINDOW " + i + " " + t
                                + " WHERE newks.newtb.ident1 LIKE \"whatever\";";
                String expectedText =
                        "SELECT newks.newtb.ident1 FROM newks.newtb WITH WINDOW " + i + " " + t
                                + " WHERE newks.newtb.ident1 LIKE 'whatever';";
                testRegularStatement(inputText, expectedText, "selectStatementWindows");
            }

        }
    }

    //
    // Select with JOIN
    //

    @Test
    public void selectStatementJoins() {
        String inputText =
                "SELECT c.t1.a, c.t2.b FROM c.t1 INNER JOIN c.t2 ON c.t1.a = c.t2.aa WHERE c.t1.a = \"y\";";
        String expectedText =
                "SELECT c.t1.a, c.t2.b FROM c.t1 INNER JOIN c.t2 ON c.t1.a = c.t2.aa WHERE c.t1.a = 'y';";
        testRegularStatement(inputText, expectedText, "selectStatementJoins");
    }

    @Test
    public void selectStatementJoinComplex() {
        String inputText = "[test], " +
                "SELECT colSales, colRevenues FROM tableClients "
                + "INNER JOIN tableCostumers ON AssistantId = clientId "
                + "WHERE colCity = 'Madrid' "
                + "ORDER BY age "
                + "GROUP BY gender;";
        String expectedText =
                "SELECT <unknown_name>.<unknown_name>.colSales, <unknown_name>.<unknown_name>.colRevenues FROM test.tableClients "
                        + "INNER JOIN test.tableCostumers ON <unknown_name>.<unknown_name>.AssistantId = <unknown_name>.<unknown_name>.clientId "
                        + "WHERE <unknown_name>.<unknown_name>.colCity = 'Madrid' "
                        + "ORDER BY [<unknown_name>.<unknown_name>.age] "
                        + "GROUP BY <unknown_name>.<unknown_name>.gender;";
        testRegularStatement(inputText, expectedText, "selectStatementJoinComplex");
    }

    @Test
    public void selectStatementJoinWithParenthesis() {
        String inputText = "[catalogTest], " +
                "SELECT c.a, c.b FROM c INNER JOIN tablename t ON field1=field2 WHERE c.x = y;";
        String expectedText =
                "SELECT catalogTest.c.a, catalogTest.c.b FROM catalogTest.c INNER JOIN catalogTest.tablename AS t " +
                        "ON <unknown_name>.<unknown_name>.field1 = <unknown_name>.<unknown_name>.field2 " +
                        "WHERE catalogTest.c.x = <unknown_name>.<unknown_name>.y;";
        testRegularStatement(inputText, expectedText, "selectStatementJoins");
    }

    @Test
    public void selectStatementAliasedColumnsJoin() {
        String inputText = "[test], " +
                "SELECT c.a, c.b FROM c INNER JOIN tablename t ON c.field1=tablename.field2 WHERE c.x = 5;";
        String expectedText =
                "SELECT test.c.a, test.c.b FROM test.c " +
                        "INNER JOIN test.tablename AS t " +
                        "ON test.c.field1 = test.tablename.field2 " +
                        "WHERE test.c.x = 5;";
        testRegularStatement(inputText, expectedText, "selectStatementAliasedColumnsJoin");
    }

    @Test
    public void selectStatementAliasedInversedColumnsJoins() {
        String inputText = "[test], " +
                "SELECT c.a, c.b FROM c INNER JOIN tablename t ON tablename.field2=c.field1 WHERE c.x = 5;";
        String expectedText =
                "SELECT test.c.a, test.c.b FROM test.c " +
                        "INNER JOIN test.tablename AS t " +
                        "ON test.tablename.field2 = test.c.field1 " +
                        "WHERE test.c.x = 5;";

        testRegularStatement(inputText, expectedText, "selectStatementJoins");
    }

    @Test
    public void selectStatementAliasedTableJoins() {
        String inputText = "[test], " +
                "SELECT c.a, c.b FROM table_c c JOIN tablename t ON t.field2=c.field1 WHERE c.x = 5;";
        String expectedText =
                "SELECT test.c.a, test.c.b FROM test.table_c AS c " +
                        "INNER JOIN test.tablename AS t " +
                        "ON test.t.field2 = test.c.field1 " +
                        "WHERE test.c.x = 5;";

        testRegularStatement(inputText, expectedText, "selectStatementAliasedTableJoins");
    }

    //
    // Select with order by
    //

    @Test
    public void selectStatementCombineOrderby() {
        for (String s : new String[] { " ASC", " DESC", " ASC, demo.b.extracol ASC",
                " ASC, demo.b.extracol DESC", " DESC, demo.b.extracol DESC",
                " DESC, demo.b.extracol ASC" }) {
            String inputText =
                    "SELECT demo.b.a FROM demo.b ORDER BY demo.b.id1 " + s + " GROUP BY demo.b.col1 LIMIT 50;";
            String expectedText =
                    "SELECT demo.b.a FROM demo.b ORDER BY [demo.b.id1" + (s.replace(" ASC", "")) + "]"
                            + " GROUP BY demo.b.col1 LIMIT 50;";
            testRegularStatement(inputText, expectedText, "selectStatementCombineOrderby");
        }
    }

    @Test
    public void selectWrongLikeWord() {
        String inputText =
                "SELECT newtb.ident1, myfunction(newtb.innerIdent, newtb.anotherIdent) LIKE ident1 FROM newks.newtb;";
        testParserFails(inputText, "selectWrongLikeWord");
    }


    @Test
    public void selectSelectorsCOUNT() {
        for (String c : new String[] { "COUNT(*)", "COUNT(1)", "COUNT(c.table0.col25)"}) {
            String inputText = "SELECT " + c + " AS COUNT from c.table0;";
            testRegularStatement(inputText, "selectSelectors");
        }
    }

    @Test
    public void selectSelectorsUDF() {
        for (String c : new String[] { "myUDF(c.table0.field0)", "myUDF(c.table0.field0, c.table0.field1)" }) {
            String inputText = "SELECT " + c + " AS myUDF from c.table0;";
            testRegularStatement(inputText, "selectSelectors");
        }
    }

  /*
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
    testParserFails(inputText, "selectWithBetweenClauseThreeValuesFail");
  }

  @Test
  public void selectWithBetweenClauseOneValueFail() {

    String inputText =
        "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'aaaa_00@domain.com';";
    testParserFails(inputText, "selectWithInClauseOneValueOk");
  }
*/

    @Test
    public void selectGroupedWithCountOk() {
        String inputText = "SELECT users.gender, COUNT(*) FROM demo.users GROUP BY users.gender;";
        String expectedText = "SELECT <unknown_name>.users.gender, COUNT(*) AS COUNT FROM demo.users " +
                "GROUP BY <unknown_name>.users.gender;";
        testRegularStatement(inputText, expectedText, "selectGroupedWithCountOk");
    }

    /*
    @Test
    public void selectAliasGroupedWithCountOk() {
        String inputText = "SELECT users.gender as g, COUNT(*) FROM demo.users GROUP BY users.gender;";
        testRegularStatement(inputText, "selectAliasGroupedWithCountOk");
    }
    */

    /*
    @Test
    public void selectGroupedWithSumOk() {
        String inputText = "SELECT users.gender, SUM(users.age) FROM demo.users GROUP BY users.gender;";
        testRegularStatement(inputText, "selectGroupedWithSumOk");
    }
    */

    @Test
    public void selectSimpleOrderByOk() {
        String inputText = "SELECT users.gender FROM myCatalog.users ORDER BY users.age;";
        String expectedText = "SELECT myCatalog.users.gender FROM myCatalog.users ORDER BY [myCatalog.users.age];";
        testRegularStatementSession("myCatalog", inputText, expectedText, "selectSimpleOrderByOk");
    }

    @Test
    public void selectMultipleOrderByOk() {
        String inputText = "[myCatalog], SELECT users.gender FROM myCatalog.users ORDER BY users.age, users.gender;";
        String expectedText = "SELECT myCatalog.users.gender FROM myCatalog.users " +
                "ORDER BY [myCatalog.users.age, myCatalog.users.gender];";
        testRegularStatement(inputText, expectedText, "selectMultipleOrderByOk");
    }

    @Test
    public void selectSimpleOrderByWithoutTableOk() {
        String inputText = "[demo], SELECT users.gender FROM demo.users ORDER BY users.age;";
        String expectedText = "SELECT demo.users.gender FROM demo.users ORDER BY [demo.users.age];";
        testRegularStatement(inputText, expectedText, "selectSimpleOrderByOk");
    }

    @Test
    public void selectMultipleOrderByWithoutTableOk() {
        String inputText = "[demo], SELECT users.gender FROM demo.users ORDER BY users.age, users.gender;";
        String expectedText = "SELECT demo.users.gender FROM demo.users ORDER BY [demo.users.age, demo.users.gender];";
        testRegularStatement(inputText, expectedText, "selectSimpleOrderByOk");
    }

    @Test
    public void selectMultipleOrderByWithoutTableMultipleDirectionOk() {
        String inputText = "[demo], " +
                "SELECT users.gender FROM demo.users ORDER BY users.age ASC, users.gender DESC;";
        String expectedText =
                "SELECT demo.users.gender FROM demo.users ORDER BY [demo.users.age, demo.users.gender DESC];";
        testRegularStatement(inputText, expectedText, "selectSimpleOrderByOk");
    }

    @Test
    public void selectSimpleOrderByWithAscDirectionOk() {
        String inputText = "SELECT users.gender FROM demo.users ORDER BY users.age ASC;";
        String expectedText = "SELECT demo.users.gender FROM demo.users ORDER BY [demo.users.age];";
        testRegularStatementSession("demo", inputText, expectedText, "selectSimpleOrderByWithAscDirectionOk");
    }

    @Test
    public void selectSimpleOrderByWithDescDirectionOk() {
        String inputText = "[demo], SELECT users.gender FROM demo.users ORDER BY users.age DESC;";
        String expectedText = "SELECT demo.users.gender FROM demo.users ORDER BY [demo.users.age DESC];";
        testRegularStatement(inputText, expectedText, "selectSimpleOrderByWithDescDirectionOk");
    }

    @Test
    public void selectSimpleOrderByFail() {
        String inputText = "SELECT users.gender FROM demo.users ORDER BY DESC users.age;";
        testParserFails(inputText, "selectSimpleOrderByFail");
    }

/*
  @Test
  public void testComplexQueryWithAliasesOk() {

    String inputText =
        "SELECT users.age AS edad, users.gender AS genero, sum(users.age) AS suma, min(users.gender) AS minimo, count(*) AS contador FROM demo.users "
            + "WHERE users.age > 13 AND users.gender IN ('male', 'female') ORDER BY users.age DESC GROUP BY users.gender;";

    testRegularStatement(inputText, "testComplexQueryWithAliasesOk");
  }
*/

    @Test
    public void testSimpleGroupQueryWithAliasesOk() {
        String inputText =
                "SELECT users.gender, min(users.age) as minimo FROM demo.users GROUP BY users.gender;";
        String expectedText =
                "SELECT <unknown_name>.users.gender, min(<unknown_name>.users.age) as minimo "
                        + "FROM demo.users GROUP BY <unknown_name>.users.gender;";
        Assert.assertNotNull(testRegularStatement(inputText, expectedText, "testSimpleGroupQueryWithAliasesOk"),
                "regular statement error");
    }

    //
    // Complex cases
    //

  /*
  @Test
  public void complexSelect() {
    String inputText =
        "SELECT newtb.ident1 AS name1, myfunction(newtb.innerIdent, newtb.anotherIdent) AS functionName "
        + "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE newtb.ident1 LIKE whatever"
        + " ORDER BY newtb.id1 ASC GROUP BY newtb.col1 LIMIT 50;";
    testRegularStatement(inputText, "complexSelect");
  }
  */

    @Test
    public void selectComplex() {
        String inputText =
                "[test], SELECT colSales, colRevenues FROM tableClients "
                        + "WHERE colCity = 'Madrid' "
                        + "ORDER BY age DESC, rating ASC "
                        + "GROUP BY gender;";
        String expectedText =
                "SELECT <unknown_name>.<unknown_name>.colSales, <unknown_name>.<unknown_name>.colRevenues FROM test.tableClients "
                        + "WHERE <unknown_name>.<unknown_name>.colCity = 'Madrid' "
                        + "ORDER BY [<unknown_name>.<unknown_name>.age DESC, " +
                        "<unknown_name>.<unknown_name>.rating] "
                        + "GROUP BY <unknown_name>.<unknown_name>.gender;";
        testRegularStatement(inputText, expectedText, "selectComplex");
    }

    @Test
    public void selectWithWrongLeftTermTypeInWhere() {
        String inputText = "SELECT * FROM demo.emp WHERE myUDF(comment) < 10;";
        testParserFails("demo", inputText, "selectWithWrongLeftTermTypeInWhere");
    }

    @Test
    public void implicitJoin() {
        String inputText = "SELECT * FROM table1, table2 WHERE table1.id = table2.id;";
        String expectedText = "SELECT * FROM myCatalog.table1 INNER JOIN myCatalog.table2 " +
                "ON myCatalog.table1.id = myCatalog.table2.id;";
        testRegularStatementSession("myCatalog", inputText, expectedText, "implicitJoin");
    }

    @Test
    public void testJoinAndStreaming() {
        String inputText = "SELECT id, name, amount FROM demo.table1 WITH WINDOW 5 Mins " +
                "JOIN demo.table2 ON table1.id = table2.id;";
        String expectedText = "SELECT <UNKNOWN_NAME>.<UNKNOWN_NAME>.id, <UNKNOWN_NAME>.<UNKNOWN_NAME>.name, <UNKNOWN_NAME>.<UNKNOWN_NAME>.amount" +
                " FROM demo.table1 WITH WINDOW 5 MINUTES INNER JOIN demo.table2 ON demo.table1.id = demo.table2.id;";
        testRegularStatementSession("demo", inputText, expectedText, "testJoinAndStreaming");
    }

}
