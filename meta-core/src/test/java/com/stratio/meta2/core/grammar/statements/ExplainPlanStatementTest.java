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

public class ExplainPlanStatementTest extends ParsingTest {

    @Test
    public void explainPlanForDropIndex() {
        String inputText = "EXPLAIN PLAN FOR DROP INDEX table1.indexName;";
        String expectedText = "EXPLAIN PLAN FOR DROP INDEX demo.table1.index[indexName];";
        testRegularStatementSession("demo", inputText, expectedText, "explainPlanForDropIndex");
    }

    @Test
    public void explainPlanForSimpleSelect() {
        String inputText = "EXPLAIN PLAN FOR SELECT users.name, users.age FROM demo.users;";
        String expectedText = "EXPLAIN PLAN FOR SELECT demo.users.name, demo.users.age FROM demo.users;";
        testRegularStatementSession("demo", inputText, expectedText, "explainPlanForSimpleSelect");
    }

    @Test
    public void explainPlanForWrongPlanToken() {
        String inputText = "EXPLAIN PLAANS FOR DROP INDEX indexName;";
        testParserFails(inputText, "wrongPlanToken");
    }

    @Test
    public void explainPlanForWrongFromToken() {
        String inputText = "EXPLAIN PLAN FOR SELECT * FROMS demo.users;";
        testParserFails(inputText, "wrongPlanToken");
    }

}
