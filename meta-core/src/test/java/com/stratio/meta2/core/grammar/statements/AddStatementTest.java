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

package com.stratio.meta2.core.grammar.statements;

import org.testng.annotations.Test;

import com.stratio.meta2.core.grammar.ParsingTest;

public class AddStatementTest extends ParsingTest {

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
    public void addStartingQuoteMissing1() {
        String inputText = "ADD /dir/jar_name-v1.0.jar;\"";
        testParserFails(inputText, "addStartingQuoteMissing1");
    }

    @Test
    public void addEndingQuoteMissing2() {
        String inputText = "ADD \"/dir/jar_name-v1.0.jar;";
        testParserFails(inputText, "addEndingQuoteMissing2");
    }

    @Test
    public void addInvalidPath() {
        String inputText = "ADD \"@jar_name-v1.0.jar;\"";
        testParserFails(inputText, "addInvalidPath");
    }

}
