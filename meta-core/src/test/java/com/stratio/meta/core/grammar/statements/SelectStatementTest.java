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

public class SelectStatementTest extends ParsingTest {

    @Test
    public void select_statement() {
        String inputText = "SELECT ident1 AS name1, myfunction(innerIdent, anotherIdent) AS functionName "
                + "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE ident1 LIKE whatever"
                + " ORDER BY id1 ASC GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
        testRegularStatement(inputText, "select_statement");
    }

    @Test
    public void select_statement_2() {
        String inputText = "SELECT lucene FROM newks.newtb;";
        testRegularStatement(inputText, "select_statement_2");
    }

    @Test
    public void select_withTimeWindow() {
        String inputText = "SELECT column1 FROM table1 WITH WINDOW 5 SECONDS WHERE column2 = 3;";
        testRegularStatement(inputText, "select_withTimeWindow");
    }

    @Test
    public void select_with_match(){
        String inputText = "SELECT * FROM demo.emp WHERE first_name MATCH s2o;";
        testRegularStatement(inputText, "select_with_match");
    }

    @Test
    public void select_wrong_like_word(){
        String inputText = "SELECT ident1, myfunction(innerIdent, anotherIdent) LIKE ident1 FROM newks.newtb;";
        testParseFails(inputText, "select_wrong_like_word");
    }


}