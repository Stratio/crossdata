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

package com.stratio.meta.core.statements;

import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SelectStatementTest extends BasicCoreCassandraTest {

    protected static MetadataManager _metadataManager = null;

    protected static final ParsingTest _pt = new ParsingTest();

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        _metadataManager = new MetadataManager(_session);
        _metadataManager.loadMetadata();
        //for(String k : _metadataManager.getKeyspacesNames()){
        //    System.out.println("Keyspace: " + k);
        //}
    }


    public static String getLuceneQuery(String ... clauses){
        StringBuilder sb = new StringBuilder("{filter:{type:\"boolean\",must:[");
        for(String clause : clauses){
            sb.append("{").append(clause).append("},");
        }
        return sb.substring(0, sb.length()-1) + "]}}";
    }

    public MetaStatement testIndexStatement(String input, String expected, String keyspace, String methodName){
        MetaStatement stmt = _pt.testRegularStatement(input, methodName);
        //Required to cache the metadata manager.
        stmt.validate(_metadataManager, keyspace);
        assertEquals(stmt.getDriverStatement().toString(), expected, "Lucene query translation does not match - " + methodName);
        return stmt;
    }

    public MetaStatement testGetLuceneWhereClause(String inputText, String expected, String keyspace, String tablename, String methodName){
        //Parse the statement
        MetaQuery mq = parser.parseStatement(inputText);
        MetaStatement st = mq.getStatement();
        assertNotNull(st, "Cannot parse "+methodName
                + " parser error: " + mq.hasError()
                + " -> " + mq.getResult().getErrorMessage());
        assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                + "' from '" + st.toString() + "' returned: " + mq.getResult().getErrorMessage());

        TableMetadata tableMetadata = _metadataManager.getTableMetadata(keyspace, tablename);

        String [] result = SelectStatement.class.cast(st)
                .getLuceneWhereClause(_metadataManager, _metadataManager.getTableMetadata(keyspace,tablename));
        assertEquals(result[1], expected, "Lucene where clause does not match");

        return st;
    }

    /* Tests that concentrate on the generated Lucene syntax. */

    @Test
    public void getLuceneWhereClause1LuceneOk(){
        String inputText = "SELECT * FROM demo.users WHERE name MATCH 'name_1*';";
        String [] luceneClauses = {"type:\"wildcard\",field:\"name\",value:\"name_1*\""};
        String expectedText = getLuceneQuery(luceneClauses);
        testGetLuceneWhereClause(inputText, expectedText, "demo", "users", "getLuceneWhereClause1LuceneOk");
    }

    /* Tests with complete queries. */

    @Test
    public void translateToCQL1LuceneOk(){
        String inputText = "SELECT * FROM demo.users WHERE name MATCH 'name_1*';";
        String [] luceneClauses = {"type:\"wildcard\",field:\"name\",value:\"name_1*\""};
        String expectedText = "SELECT * FROM demo.users WHERE stratio_lucene_index_1='" + getLuceneQuery(luceneClauses)+ "';";
        testIndexStatement(inputText, expectedText, "demo", "translateToCQL1LuceneOk");
    }

    @Test
    public void translateToCQL1Lucene1cOk(){
        String inputText = "SELECT * FROM demo.users WHERE name MATCH 'name_1*' AND age > 20;";
        String [] luceneClauses = {"type:\"wildcard\",field:\"name\",value:\"name_1*\""};
        String expectedText = "SELECT * FROM demo.users WHERE stratio_lucene_index_1='" + getLuceneQuery(luceneClauses)+ "' AND age>20;";
        testIndexStatement(inputText, expectedText, "demo", "translateToCQL1Lucene1cOk");
    }

    @Test
    public void translateToCQL2LuceneOk(){
        String inputText = "SELECT * FROM demo.users WHERE name MATCH 'name_*' AND name MATCH 'name_1*';";
        String [] luceneClauses = {
                "type:\"wildcard\",field:\"name\",value:\"name_*\"",
                "type:\"wildcard\",field:\"name\",value:\"name_1*\""};
        String expectedText = "SELECT * FROM demo.users WHERE stratio_lucene_index_1='" + getLuceneQuery(luceneClauses)+ "';";
        testIndexStatement(inputText, expectedText, "demo", "translateToCQL2LuceneOk");
    }

    @Test
    public void processLuceneQueryType(){

        String inputText = "SELECT * FROM demo.users WHERE name MATCH 'name_1*' AND age > 20;";
        String methodName = "processLuceneQueryType";
        MetaQuery mq = parser.parseStatement(inputText);
        MetaStatement st = mq.getStatement();
        assertNotNull(st, "Cannot parse "+methodName
                + " parser error: " + mq.hasError()
                + " -> " + mq.getResult().getErrorMessage());
        assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                + "' from '" + st.toString() + "' returned: " + mq.getResult().getErrorMessage());
        SelectStatement ss = SelectStatement.class.cast(st);

        String [][] queries = {
                //Input    Type       parsed
                {"?",     "wildcard", "?"},
                {"*",     "wildcard", "*"},
                {"\\?",   "match",    "?"},
                {"\\*",   "match",    "*"},
                {"\\?sf", "match",    "?sf"},
                {"af\\?", "match",    "af?"},
                {"s\\?f", "match",    "s?f"},
                {"sdf",   "match",    "sdf"},
                {"*asd*", "wildcard", "*asd*"},
                {"?as?",  "wildcard", "?as?"},
                {"?as*",  "wildcard", "?as*"},
                {"[asd",  "regex",    "[asd"},
                {"fa]",   "regex",    "fa]"},
                {"]*sf",  "regex",    "]*sf"},
                {"~as",   "match",    "~as"},
                {"as~2",  "fuzzy",    "as~2"}};

        for(String [] query : queries) {
            String [] result = ss.processLuceneQueryType(query[0]);
            assertEquals(result[0], query[1], "Query type does not match");
            assertEquals(result[1], query[2], "Parsed does not match");
        }

    }
}
