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
    public void setBasic() {
        String inputText = "SET OPTIONS ANALYTICS=true AND CONSISTENCY=LOCAL_ONE;";
        testRegularStatement(inputText, "setBasic");
    }

    @Test
    public void setWrongBoolean(){
        String inputText = "SET OPTIONS ANALYTICS=5;";
        testParseFails(inputText, "setWrongBoolean");
    }

    @Test
    public void setBasicConsistency1() {
        String inputText = "SET OPTIONS CONSISTENCY=EACH_QUORUM;";
        testRegularStatement(inputText, "setBasicConsistency1");
    }

    @Test
    public void setAllConsistencies() {
        for (String consistency: new String[]{"ALL", "ANY", "QUORUM", "ONE", "TWO", "THREE", "EACH_QUORUM", "LOCAL_ONE", "LOCAL_QUORUM"}){
            String inputText = "SET OPTIONS ANALYTICS=true AND CONSISTENCY="+consistency+";";
            testRegularStatement(inputText, "setAllConsistencies");
        }
    }

    @Test
    public void setAllConsistenciesWithoutAnalytics() {
        for (String consistency: new String[]{"ALL", "ANY", "QUORUM", "ONE", "TWO", "THREE", "EACH_QUORUM", "LOCAL_ONE", "LOCAL_QUORUM"}){
            String inputText = "SET OPTIONS CONSISTENCY="+consistency+";";
            testRegularStatement(inputText, "setAllConsistenciesWithoutAnalytics");
        }
    }

    @Test
    public void setAllConsistenciesWithAnalytics() {
        for (String consistency: new String[]{"ALL", "ANY", "QUORUM", "ONE", "TWO", "THREE", "EACH_QUORUM", "LOCAL_ONE", "LOCAL_QUORUM"}){
            for (String trueorfalse: new String[]{"true", "false"}){
                String inputText = "SET OPTIONS ANALYTICS="+trueorfalse+" AND CONSISTENCY="+consistency+";";
                testRegularStatement(inputText, "setAllConsistenciesWithAnalytics");

                inputText = "SET OPTIONS CONSISTENCY="+consistency+" AND ANALYTICS="+trueorfalse+";";
                testRegularStatement(inputText, "Set options analytics="+trueorfalse+" AND consistency="+consistency+";",  "setAllConsistenciesWithAnalytics");

            }
        }
    }


}