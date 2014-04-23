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
import com.stratio.meta.core.statements.MetaStatement;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CreateKeyspaceStatementTest extends ParsingTest {

    @Test
    public void createKeyspace_ifNotExists() {
        String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
                + "WITH replication = replicationLevel AND durable_writes = false;";
        testRegularStatement(inputText, "createKeyspace_ifNotExists");
    }

    //TODO: Should we support it in this way?
    @Test
    public void createKeyspace_nestedOptions() {
        String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
                + "WITH replication = {class: NetworkTopologyStrategy, DC1: 1, DC2: 3} "
                +"AND durable_writes = false;";
        Set<String> properties = new HashSet<>();
        properties.add("class: NetworkTopologyStrategy");
        properties.add("DC1: 1");
        properties.add("DC2: 3");
        MetaStatement st = parser.parseStatement(inputText).getStatement();
        String propResultStr = st.toString().substring(st.toString().indexOf("{")+1, st.toString().indexOf("}"));
        String[] str = propResultStr.split(",");
        Set<String> propertiesResult = new HashSet<>();
        for (String str1 : str) {
            propertiesResult.add(str1.trim());
        }
        assertNotNull(st, "Cannot parse create keyspace - nestedOptions");
        assertEquals("CREATE KEYSPACE IF NOT EXISTS key_space1 WITH replication = {",
                st.toString().substring(0, st.toString().indexOf("{")+1),
                "Cannot parse create keyspace - nestedOptions");
        assertEquals("} AND durable_writes = false;",
                st.toString().substring(st.toString().indexOf("}"))+";",
                "Cannot parse create keyspace - nestedOptions");
        assertTrue(propertiesResult.containsAll(properties), "Cannot parse create keyspace - nestedOptions");
        assertTrue(properties.containsAll(propertiesResult), "Cannot parse create keyspace - nestedOptions");
    }

    @Test
    public void createKeyspace_basicOptions() {
        String inputText = "CREATE KEYSPACE key_space1 WITH replication = {class: SimpleStrategy, replication_factor: 1}"
                + " AND durable_writes = false;";
        MetaStatement st = parser.parseStatement(inputText).getStatement();
        assertNotNull(st, "Cannot parse createKeyspace_basicOptions");

        boolean originalOK = false;
        boolean alternative1 = false;

        if(inputText.equalsIgnoreCase(st.toString()+";")){
            originalOK = true;
        }

        String alternative1Str = "CREATE KEYSPACE key_space1 WITH replication = {replication_factor: 1, class: SimpleStrategy}"
                + " AND durable_writes = false;";
        if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
            alternative1 = true;
        }

        assertTrue((originalOK || alternative1), "Cannot parse createKeyspace_basicOptions");
    }

    @Test
    public void createKeyspace_durable_writes() {
        String inputText = "CREATE KEYSPACE demo WITH replication = {class: SimpleStrategy, replication_factor: 1} "
                + "AND durable_writes = false;";
        MetaStatement st = parser.parseStatement(inputText).getStatement();
        assertNotNull(st, "Cannot parse createKeyspace_durable_writes");

        boolean originalOK = false;
        boolean alternative1 = false;

        if(inputText.equalsIgnoreCase(st.toString()+";")){
            originalOK = true;
        }

        String alternative1Str = "CREATE KEYSPACE demo WITH replication = {replication_factor: 1, class: SimpleStrategy} "
                + "AND durable_writes = false;";
        if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
            alternative1 = true;
        }

        assertTrue((originalOK || alternative1), "Cannot parse createKeyspace_durable_writes");
    }

    @Test
    public void create_keyspace_wrong_identifier(){
        String inputText = "CREATE KEYSPACE name.key_space1 WITH replication = replicationLevel;";
        testParseFails(inputText, "create_keyspace_wrong_identifier");
    }


}