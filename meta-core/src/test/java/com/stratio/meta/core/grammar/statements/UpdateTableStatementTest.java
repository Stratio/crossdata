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
    public void update_basic() {
        String inputText = "UPDATE table1 SET field1 = value1 WHERE field3 = value3;";
        testRegularStatement(inputText, "update_basic");
    }

    @Test
    public void update_tablename() {
        String inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field1 = 25;";
        testRegularStatement(inputText, "update_tablename");
    }

    @Test
    public void update_where() {
        String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                + " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
        testRegularStatement(inputText, "update_where");
    }

    @Test
    public void update_full() {
        String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                + " field2 = value2 WHERE field3 = value3 AND field4 = value4"
                + " IF field5 = transaction_value5;";
        testRegularStatement(inputText, "update_full");
    }

    @Test
    public void update_for_invalid_assignment(){
        String inputText = "UPDATE table1 SET field1 = value1 WHERE field3: value3;";
        testRecoverableError(inputText, "update_for_invalid_assignment");
    }

    @Test
    public void update_wrong_spelling(){
        String inputText = "UPDDATE table1 SET field1 = value1 WHERE field3: value3;";
        testParseFails(inputText, "update_wrong_spelling");
    }


    @Test
    public void update_where_using_and() {
        String inputText = "UPDATE table1 USING TTL = 400 AND TTL2 = 400 SET field1 = value1,"
                + " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
        testRegularStatement(inputText, "update_where_using_and");
    }

    //@Test
    public void update_tablename_if_and() {
        String inputText = "UPDATE tablename SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field1 = 25 AND field2 = 26;";
        testRegularStatement(inputText, "update_tablename_if_and");

        inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field1 = 25 AND field2 = 26;";
        testRegularStatement(inputText, "update_tablename_if_and");
    }

}