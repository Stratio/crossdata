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

public class DeleteStatementTest extends ParsingTest {

    @Test
    public void deleteWhere() {
        String inputText = "DELETE FROM table1 WHERE field1 = value1;";
        testRegularStatement(inputText, "deleteWhere");
    }

    @Test
    public void deleteSelection() {
        String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1;";
        testRegularStatement(inputText, "deleteSelection");
    }

    @Test
    public void deleteFull() {
        String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
        testRegularStatement(inputText, "deleteFull");
    }

    @Test
    public void deleteTokenNameOk() {
        String inputText = "DELETE (lucene, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
        testRegularStatement(inputText, "deleteTokenNameOk");
    }

    @Test
    public void deleteInvalidColumnNameFail() {
        String inputText = "DELETE (123col, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
        testRecoverableError(inputText, "deleteInvalidColumnNameFail");
    }

    @Test
    public void deleteWrongPropertyAssignment(){
        String inputText = "DELETE (col1 AND col2) FROM table1 WHERE field1: value1;";
        testRecoverableError(inputText, "deleteWrongPropertyAssignment");
    }


}
