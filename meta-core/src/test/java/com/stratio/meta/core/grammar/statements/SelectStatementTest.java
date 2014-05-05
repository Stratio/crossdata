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
    public void selectStatement() {
        String inputText = "SELECT ident1 AS name1, myfunction(innerIdent, anotherIdent) AS functionName "
                + "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE ident1 LIKE whatever"
                + " ORDER BY id1 ASC GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
        testRegularStatement(inputText, "selectStatement");
    }

    @Test
    public void selectStatementJoins() {
        for(String jp:new String[]{
                "field1=field2"
                ,"field3=field4 AND field1=field2"
        }){
            String inputText = "SELECT a, b FROM c INNER JOIN tablename ON "+jp+" WHERE x = y;";
            testRegularStatement(inputText, "selectStatement");
        }

    }

    @Test
    public void selectStatementCombineOrderby() {
        for(String s:new String[]{
                "ASC"
                ,"DESC"
                ,"ASC, anothercolumn ASC"
                ,"ASC, anothercolumn DESC"
                ,"DESC, anothercolumn DESC"
                ,"DESC, anothercolumn ASC"
        }){
            String inputText = "SELECT a FROM b ORDER BY id1 "+s+" GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
            testRegularStatement(inputText, "selectStatement");
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
    public void selectWithMatch(){
        String inputText = "SELECT * FROM demo.emp WHERE first_name MATCH s2o;";
        testRegularStatement(inputText, "selectWithMatch");
    }

    @Test
    public void selectWrongLikeWord(){
        String inputText = "SELECT ident1, myfunction(innerIdent, anotherIdent) LIKE ident1 FROM newks.newtb;";
        testParseFails(inputText, "selectWrongLikeWord");
    }


}