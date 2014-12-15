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

import org.testng.annotations.Test;

import com.stratio.crossdata.core.grammar.ParsingTest;

public class UpdateTableStatementTest extends ParsingTest {

    @Test
    public void updateBasic() {
        String inputText = "UPDATE demo.table1 SET field1 = 'value1' WHERE catalog1.table1.field3 = 'value3' AND table2.name = 'stratio';";
        String expectedText = "UPDATE demo.table1 SET demo.table1.field1 = 'value1' WHERE catalog1.table1.field3 = 'value3' AND demo.table2.name = 'stratio';";
        testRegularStatement(inputText, expectedText, "updateBasic");
    }

    @Test
    public void updateBasicWithoutWhere() {
        String inputText = "UPDATE demo.table1 SET field1 = 'value1';";
        String expectedText = "UPDATE demo.table1 SET demo.table1.field1 = 'value1';";
        testRegularStatement(inputText, expectedText, "updateBasicWithoutWhere");
    }

    @Test
    public void updateTableName() {
        String inputText = "[demo], UPDATE myTable SET ident1 = 'term1', ident2 = 'term2'"
                + " WHERE ident3 > 25 WITH {'prop1': 342, 'replication': 2};";
        String expectedText =
                "UPDATE demo.myTable SET demo.myTable.ident1 = 'term1', demo.myTable.ident2 = 'term2'"
                        + " WHERE demo.myTable.ident3 > 25 WITH {'prop1'=342, 'replication'=2};";
        testRegularStatement(inputText, expectedText, "updateTableName");
    }

    @Test
    public void updateWhere() {
        String inputText = "UPDATE table1 SET field1 = 'value1',"
                + " field2 = 'value2' WHERE field3 = 'value3' AND field4 = 'value4' WITH {'TTL': 400};";
        String expectedText = "UPDATE demo.table1 SET demo.table1.field1 = 'value1',"
                + " demo.table1.field2 = 'value2' WHERE demo.table1.field3 = 'value3' AND demo.table1.field4 = 'value4'" +
                " WITH {'TTL'=400};";
        testRegularStatementSession("demo", inputText, expectedText, "updateWhere");
    }

    @Test
    public void updateFull() {
        String inputText = "UPDATE table1 SET field1 = 'value1',"
                + " field2 = 'value2' WHERE field3 = 'value3' AND field4 = 'value4'"
                + " WITH {'TTL': 400, 'class': 'transaction_value5'};";
        String expectedText =
                "UPDATE test.table1 SET test.table1.field1 = 'value1',"
                        + " test.table1.field2 = 'value2' WHERE test.table1.field3 = 'value3' AND test.table1.field4 = 'value4'"
                        + " WITH {'TTL'=400, 'class'='transaction_value5'};";
        testRegularStatementSession("test", inputText, expectedText, "updateFull");
    }

    @Test
    public void updateForInvalidAssignment() {
        String inputText = "UPDATE table1 SET field1 = value1 WHERE field3: value3;";
        testParserFails(inputText, "updateForInvalidAssignment");
    }

    @Test
    public void updateWrongSpelling() {
        String inputText = "UPDDATE table1 SET field1 = value1 WHERE field3: value3;";
        testParserFails(inputText, "updateWrongSpelling");
    }

    @Test
    public void updateWhereUsingAnd() {
        String inputText = "UPDATE demo.table1 SET field1 = 'value1',"
                + " field2 = 'value2' WHERE field3 = 'value3' AND field4 = 'value4' WITH {'TTL': 400, 'TTL2': 500};";
        String expectedText = "UPDATE demo.table1 SET demo.table1.field1 = 'value1',"
                + " demo.table1.field2 = 'value2' WHERE demo.table1.field3 = 'value3' AND demo.table1.field4 = 'value4'" +
                " WITH {'TTL'=400, 'TTL2'=500};";
        testRegularStatement(inputText, expectedText, "updateWhereUsingAnd");
    }

    @Test
    public void updateSetWithSeveralOperators1() {
        String inputText = "[demo], UPDATE table1 SET count = count * 2 + expenses, " +
                "estimation = true  WHERE field3 = 'value3';";
        String expectedText = "UPDATE demo.table1 SET demo.table1.count = demo.table1.count * 2 + demo.table1" +
                ".expenses, demo.table1.estimation = true WHERE demo.table1.field3 = 'value3';";
        testRegularStatement(inputText, expectedText, "updateSetWithSeveralOperators1");
    }

    @Test
    public void updateSetWithSeveralOperators2() {
        String inputText = "[demo], UPDATE table1 SET count = count * 2 / expenses, " +
                "estimation = true WHERE field3 = 'value3';";
        String expectedText = "UPDATE demo.table1 SET demo.table1.count = demo.table1.count * 2 / demo.table1" +
                ".expenses, demo.table1.estimation = true WHERE demo.table1.field3 = 'value3';";
        testRegularStatement(inputText, expectedText, "updateSetWithSeveralOperators2");
    }

  /*@Test
  public void updateWhereWithCollectionMap() {
    String inputText = "UPDATE demo.table1 SET emails[admin] = myemail@mycompany.org WHERE field3 = value3;";
    String expectedText = "UPDATE demo.table1 SET demo.table1.emails[admin] = myemail@mycompany.org WHERE demo.table1.field3 = value3;";
    testRegularStatement(inputText, expectedText, "updateWhereWithCollectionMap");
  }

  @Test
  public void updateWhereWithCollectionSet() {
    String inputText = "UPDATE table1 SET emails = emails + {myemail@mycompany.org} WHERE field3 = value3;";
    String expectedText = "UPDATE <unknown_name>.table1 SET <unknown_name>.table1.emails = emails + {myemail@mycompany.org} WHERE <unknown_name>.table1.field3 = value3;";
    testRegularStatement(inputText, expectedText, "updateWhereWithCollectionSet");
  }

  @Test
  public void updateWhereWithCollectionList() {
    String inputText = "[demo], UPDATE table1 SET emails = emails + [myemail@mycompany.org] WHERE field3 = value3;";
    String expectedText = "UPDATE demo.table1 SET demo.table1.emails = emails + [myemail@mycompany.org] WHERE demo.table1.field3 = value3;";
    testRegularStatement(inputText, expectedText, "updateWhereWithCollectionList");
  }*/

    @Test
    public void updateTableNameIfAnd1() {
        String inputText = "UPDATE myTable SET ident1 = 'term1', ident2 = 'term2'"
                + " WHERE ident3 = 34 WITH {field3: 86, field2: 25};";
        String expectedText = "UPDATE demo.myTable SET demo.myTable.ident1 = 'term1', demo.myTable.ident2 = 'term2'"
                + " WHERE demo.myTable.ident3 = 34 WITH {'field3'=86, 'field2'=25};";
        testRegularStatementSession("demo", inputText, expectedText, "updateTableNameIfAnd1");
    }

    @Test
    public void updateTableNameIfAnd2() {
        String inputText = "UPDATE tester.myTable SET ident1 = 'term1', ident2 = 'term2'"
                + " WHERE ident3 = 'Big Data' WITH {field3: 86, field2: 25};";
        String expectedText =
                "UPDATE tester.myTable SET tester.myTable.ident1 = 'term1', tester.myTable.ident2 = 'term2'"
                        + " WHERE tester.myTable.ident3 = 'Big Data' " +
                        "WITH {'field3'=86, 'field2'=25};";
        testRegularStatementSession("demo", inputText, expectedText, "updateTableNameIfAnd2");
    }

    @Test
    public void updateTableNameWrongLeftTermTypeInWhere1() {
        String inputText = "UPDATE myTable SET ident1 = 'term1' WHERE 'string' = 34;";
        testParserFails("demo", inputText, "updateTableNameWrongLeftTermTypeInWhere1");
    }

    @Test
    public void updateTableNameWrongLeftTermTypeInWhere2() {
        String inputText = "UPDATE myTable SET ident1 = 'term1' WHERE true = 34;";
        testParserFails("demo", inputText, "updateTableNameWrongLeftTermTypeInWhere2");
    }

    @Test
    public void updateTableNameWrongLeftTermTypeInWhere3() {
        String inputText = "UPDATE myTable SET ident1 = 'term1' WHERE 54 > 34;";
        testParserFails("demo", inputText, "updateTableNameWrongLeftTermTypeInWhere3");
    }

    @Test
    public void updateTableNameWrongLeftTermTypeInWhere4() {
        String inputText = "UPDATE myTable SET ident1 = 'term1' WHERE * = 34;";
        testParserFails("demo", inputText, "updateTableNameWrongLeftTermTypeInWhere4");
    }

    @Test
    public void updateTableNameWrongLeftTermTypeInWhere5() {
        String inputText = "UPDATE myTable SET ident1 = 'term1' WHERE 54.9 > 34;";
        testParserFails("demo", inputText, "updateTableNameWrongLeftTermTypeInWhere5");
    }

    @Test
    public void updateTableNameWrongLeftTermTypeInWhere6() {
        String inputText = "UPDATE myTable SET ident1 = 'term1' WHERE myFunction(ident2) > 34;";
        testParserFails("demo", inputText, "updateTableNameWrongLeftTermTypeInWhere6");
    }

}
