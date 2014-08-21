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

package com.stratio.meta.core.statements;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.statements.MetaStatement;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class SelectStatementTest extends BasicCoreCassandraTest {

  protected static MetadataManager _metadataManager = null;

  protected static final ParsingTest _pt = new ParsingTest();

  @BeforeClass
  public static void setUpBeforeClass() {
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    _metadataManager = new MetadataManager(_session, null);
    _metadataManager.loadMetadata();
  }


  public static String getLuceneQuery(String... clauses) {
    StringBuilder sb = new StringBuilder("{filter:{type:\"boolean\",must:[");
    for (String clause : clauses) {
      sb.append("{").append(clause).append("},");
    }
    return sb.substring(0, sb.length() - 1) + "]}}";
  }

  public MetaStatement testIndexStatement(String input, String expected, String keyspace,
      String methodName) {
    MetaStatement stmt = _pt.testRegularStatement(input, methodName);
    // Required to cache the metadata manager.
    stmt.setSessionCatalog(keyspace);
    stmt.validate(_metadataManager, null);
    assertEquals(stmt.getDriverStatement().toString(), expected,
        "Lucene query translation does not match - " + methodName);
    return stmt;
  }

  public MetaStatement testGetLuceneWhereClause(String inputText, String expected, String keyspace,
      String tablename, String methodName) {
    // Parse the statement
    MetaQuery mq = parser.parseStatement(inputText);
    MetaStatement st = mq.getStatement();
    assertNotNull(st, "Cannot parse " + methodName + " parser error: " + mq.hasError() + " -> "
        + getErrorMessage(mq.getResult()));
    assertFalse(mq.hasError(), "Parsing expecting '" + inputText + "' from '" + st.toString()
        + "' returned: " + getErrorMessage(mq.getResult()));

    _metadataManager.getTableMetadata(keyspace, tablename);

    String[] result =
        SelectStatement.class.cast(st).getLuceneWhereClause(_metadataManager,
            _metadataManager.getTableMetadata(keyspace, tablename));
    assertEquals(result[1], expected, "Lucene where clause does not match");

    return st;
  }

    /* Tests that concentrate on the generated Lucene syntax. */

  @Test
  public void translateToCQL1LuceneOk() {
    String inputText = "SELECT * FROM demo.users WHERE users.name MATCH 'name_1*';";
    String[] luceneClauses = {"type:\"wildcard\",field:\"name\",value:\"name_1*\""};
    String expectedText =
        "SELECT * FROM demo.users WHERE stratio_lucene_index_1='" + getLuceneQuery(luceneClauses)
        + "';";
    testIndexStatement(inputText, expectedText, "demo", "translateToCQL1LuceneOk");
  }

  @Test
  public void translateToCQL1Lucene1cOk() {
    String inputText =
        "SELECT * FROM demo.users WHERE users.name MATCH 'name_1*' AND users.age > 20;";
    String[] luceneClauses = {"type:\"wildcard\",field:\"name\",value:\"name_1*\""};
    String expectedText =
        "SELECT * FROM demo.users WHERE stratio_lucene_index_1='" + getLuceneQuery(luceneClauses)
        + "' AND age>20;";
    testIndexStatement(inputText, expectedText, "demo", "translateToCQL1Lucene1cOk");
  }

  @Test
  public void translateToCQL2LuceneOk() {
    String inputText =
        "SELECT * FROM demo.users WHERE users.name MATCH 'name_*' AND users.name MATCH 'name_1*';";
    String[] luceneClauses =
        {"type:\"wildcard\",field:\"name\",value:\"name_*\"",
         "type:\"wildcard\",field:\"name\",value:\"name_1*\""};
    String expectedText =
        "SELECT * FROM demo.users WHERE stratio_lucene_index_1='" + getLuceneQuery(luceneClauses)
        + "';";
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
                + " -> " + getErrorMessage(mq.getResult()));
        assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                + "' from '" + st.toString() + "' returned: " + getErrorMessage(mq.getResult()));
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
