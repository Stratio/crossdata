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

public class RemoveUDFStatementTest extends ParsingTest {

    //REMOVE UDF
    @Test
    public void basic() {
        String inputText = "REMOVE UDF \"jar.name\";";
        testRegularStatement(inputText, "removeUDF");
    }

    @Test
    public void unexpectedWordFail() {
        String inputText = "REMOVE UDF \"jar.name\" NOW;";
        testParseFails(inputText, "unexpectedWordFail");
    }

    @Test
    public void startingQuoteMissing1Fail(){
        String inputText = "REMOVE UDF /dir/jar_name-v1.0.jar;\"";
        testRecoverableError(inputText, "startingQuoteMissing1Fail");
    }

    @Test
    public void fatalErrorInParser(){
        String inputText = "REMOVE UDF \";\";";
        testParseFails(inputText, "fatalErrorInParser");
    }

    @Test
    public void startingQuoteMissing2Fail(){
        String inputText = "REMOVE UDF \"/dir/jar_name-v1.0.jar;";
        testRecoverableError(inputText, "startingQuoteMissing2Fail");
    }

}