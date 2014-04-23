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

public class DeleteStatementTest extends ParsingTest {

    //DELETE ( <selection> ( ',' <selection> )* )?
    //FROM <tablename>
    //WHERE <where-clause>
    @Test
    public void delete_where() {
        String inputText = "DELETE FROM table1 WHERE field1 = value1;";
        testRegularStatement(inputText, "delete_where");
    }

    @Test
    public void delete_selection() {
        String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1;";
        testRegularStatement(inputText, "delete_selection");
    }

    @Test
    public void delete_full() {
        String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
        testRegularStatement(inputText, "delete_full");
    }

    @Test
    public void delete_tokenName_ok() {
        String inputText = "DELETE (lucene, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
        testRegularStatement(inputText, "delete_tokenName_ok");
    }

    @Test
    public void delete_invalidColumnName_fail() {
        String inputText = "DELETE (123col, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
        testRecoverableError(inputText, "delete_invalidColumnName_fail");
    }

    @Test
    public void delete_wrong_property_assignment(){
        String inputText = "DELETE (col1 AND col2) FROM table1 WHERE field1: value1;";
        testRecoverableError(inputText, "delete_wrong_property_assignment");
    }


}