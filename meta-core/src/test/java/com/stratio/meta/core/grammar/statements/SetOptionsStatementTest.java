/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
