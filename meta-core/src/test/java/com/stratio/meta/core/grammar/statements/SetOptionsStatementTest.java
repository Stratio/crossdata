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

public class SetOptionsStatementTest extends ParsingTest {


    @Test
    public void set_basic() {
        String inputText = "SET OPTIONS ANALYTICS=true AND CONSISTENCY=LOCAL_ONE;";
        testRegularStatement(inputText, "set_basic");
    }

    @Test
    public void set_wrong_boolean(){
        String inputText = "SET OPTIONS ANALYTICS=5;";
        testParseFails(inputText, "set_wrong_boolean");
    }

    @Test
    public void set_basic_consistency_1() {
        String inputText = "SET OPTIONS CONSISTENCY=EACH_QUORUM;";
        testRegularStatement(inputText, "set_basic_consistency_1");
    }

    @Test
    public void set_all_consistencies() {
        for (String consistency: new String[]{"ALL", "ANY", "QUORUM", "ONE", "TWO", "THREE", "EACH_QUORUM", "LOCAL_ONE", "LOCAL_QUORUM"}){
            String inputText = "SET OPTIONS ANALYTICS=true AND CONSISTENCY="+consistency+";";
            testRegularStatement(inputText, "set_basic");
        }
    }

    // Meta.g:552:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?


}