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

public class AlterKeyspaceStatementTest extends ParsingTest{

    @Test
    public void alterKeyspace() {
        String inputText = "ALTER KEYSPACE mykeyspace WITH ident1 = value1 AND ident2 = 54;";
        testRegularStatement(inputText, "alterKeyspace");
    }

    @Test
    public void alterKeyspaceWithReplication() {
        String inputText = "ALTER KEYSPACE mykeyspace WITH replication = {'class': 'org.apache.cassandra.locator.NetworkTopologyStrategy'};";
        testRegularStatement(inputText, "alterKeyspaceWithReplication");
    }

    @Test
    public void alterWrongKeyspaceToken(){
        String inputText = "ALTER KEYSPACES mykeyspace WITH ident1 = value1;";
        testParseFails(inputText, "alterWrongKeyspaceToken");
    }

    @Test
    public void alterWrongPropertyFormat(){
        String inputText = "ALTER KEYSPACE mykeyspace WITH 'ident1': 'value1';";
        testParseFails(inputText, "alterWrongPropertyFormat");
    }

}
