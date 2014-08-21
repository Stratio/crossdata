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
        testRegularStatement(inputText, "updateBasic");
    }

    @Test
    public void updateTablename() {
        String inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field1 = 25;";
        testRegularStatement(inputText, "updateTablename");
    }

    @Test
    public void updateWhere() {
        String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                + " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
        testRegularStatement(inputText, "updateWhere");
    }

    @Test
    public void updateFull() {
        String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                + " field2 = value2 WHERE field3 = value3 AND field4 = value4"
                + " IF field5 = transaction_value5;";
        testRegularStatement(inputText, "updateFull");
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
        testRegularStatement(inputText, "updateWhereUsingAnd");
    }

    @Test
    public void updateWhereWithCollectionMap() {
        String inputText = "UPDATE table1 SET emails[admin] = myemail@mycompany.org WHERE field3 = value3;";
        testRegularStatement(inputText, "updateWhereWithCollectionMap");
    }

    @Test
    public void updateWhereWithCollectionSet() {
        String inputText = "UPDATE table1 SET emails = emails + {myemail@mycompany.org} WHERE field3 = value3;";
        testRegularStatement(inputText, "updateWhereWithCollectionSet");
    }

    @Test
    public void updateWhereWithCollectionList() {
        String inputText = "UPDATE table1 SET emails = emails + [myemail@mycompany.org] WHERE field3 = value3;";
        testRegularStatement(inputText, "updateWhereWithCollectionList");
    }

    @Test
    public void updateTablenameIfAnd() {
        String inputText = "UPDATE tablename SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field3 = 26 AND field2 = 25;";
        testRegularStatement(inputText, "updateTablenameIfAnd");

        inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field3 = 26 AND field2 = 25;";
        testRegularStatement(inputText, "updateTablenameIfAnd");
    }

}
