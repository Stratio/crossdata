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

public class AddStatementTest extends ParsingTest{

    @Test
    public void addBasic() {
        String inputText = "ADD \"jar_name-v1.0.jar\";";
        testRegularStatement(inputText, "addBasic");
    }

    @Test
    public void addRelative() {
        String inputText = "ADD \"dir/jar_name-v1.0.jar\";";
        testRegularStatement(inputText, "addRelative");
    }

    @Test
    public void addAbsolute() {
        String inputText = "ADD \"/dir/jar_name-v1.0.jar\";";
        testRegularStatement(inputText, "addAbsolute");
    }

    @Test
    public void addStartingQuoteMissing1(){
        String inputText = "ADD /dir/jar_name-v1.0.jar;\"";
        testRecoverableError(inputText, "addStartingQuoteMissing1");
    }

    @Test
    public void addEndingQuoteMissing2(){
        String inputText = "ADD \"/dir/jar_name-v1.0.jar;";
        testRecoverableError(inputText, "addEndingQuoteMissing2");
    }

    @Test
    public void addInvalidPath(){
        String inputText = "ADD \"@jar_name-v1.0.jar;\"";
        testParseFails(inputText, "addInvalidPath");
    }


}