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

public class DropIndexStatementTest extends ParsingTest {

    @Test
    public void basic() {
        String inputText = "DROP INDEX demo.index_name;";
        testRegularStatement(inputText, "basic");
    }

    @Test
    public void noKsOk() {
        String inputText = "DROP INDEX index_name;";
        testRegularStatement(inputText, "noKsOk");
    }

    @Test
    public void ifExists() {
        String inputText = "DROP INDEX IF EXISTS demo.index_name;";
        testRegularStatement(inputText, "ifExists");
    }

    @Test
    public void tokenOk() {
        String inputText = "DROP INDEX lucene;";
        testRegularStatement(inputText, "tokenOk");
    }

    @Test
    public void wrongNotTokenFail(){
        String inputText = "DROP INDEX IF NOT EXISTS index_name;";
        testRecoverableError(inputText, "wrongNotTokenFail");
    }

    @Test
    public void invalidNameFail(){
        String inputText = "DROP INDEX IF NOT EXISTS 123name;";
        testRecoverableError(inputText, "invalidNameFail");
    }


}