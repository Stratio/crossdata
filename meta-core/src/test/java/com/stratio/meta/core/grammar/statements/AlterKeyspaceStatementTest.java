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