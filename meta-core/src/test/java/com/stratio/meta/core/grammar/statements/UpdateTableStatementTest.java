/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
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
        String inputText = "UPDATE table1 SET emails = emails + { myemail@mycompany.org } WHERE field3 = value3;";
        testRegularStatement(inputText, "updateWhereWithCollectionSet");
    }

    @Test
    public void updateWhereWithCollectionList() {
        String inputText = "UPDATE table1 SET emails = emails + [ myemail@mycompany.org ] WHERE field3 = value3;";
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