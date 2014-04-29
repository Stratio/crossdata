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

public class ListStatementTest extends ParsingTest {

    @Test
    public void listProcess() {
        String inputText = "LIST PROCESS;";
        testRegularStatement(inputText, "listProcess");
    }

    @Test
    public void listProcessLowercase() {
        String inputText = "LIST process;";
        testRegularStatement(inputText, "listProcessLowercase");
    }

    @Test
    public void listUdf() {
        String inputText = "LIST UDF;";
        testRegularStatement(inputText, "listUdf");
    }

    @Test
    public void listTrigger() {
        String inputText = "LIST TRIGGER;";
        testRegularStatement(inputText, "listTrigger");
    }

    @Test
    public void listReservedWordUse(){
        String inputText = "LIST PROCESS LAST;";
        testParseFails(inputText, "listReservedWordUse");
    }

    @Test
    public void listUnknownFail(){
        String inputText = "LIST UNKNOWN;";
        testParseFails(inputText, "listUnknownFail");
    }

}