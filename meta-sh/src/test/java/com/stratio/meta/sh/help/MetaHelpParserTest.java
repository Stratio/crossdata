/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.sh.help;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.meta.sh.help.generated.MetaHelpLexer;
import com.stratio.meta.sh.help.generated.MetaHelpParser;

public class MetaHelpParserTest {

  /**
   * Class logger.
   */
  private final Logger logger = Logger.getLogger(MetaHelpParserTest.class);

  protected static String[][] supportedHelpCommands = { {"exit", "EXIT"}, {"quit", "EXIT"},
      {"datatypes", "DATATYPES"}, {"create", "CREATE"}, {"create keyspace", "CREATE_KEYSPACE"},
      {"create table", "CREATE_TABLE"}, {"create index", "CREATE_INDEX"},
      {"create default index", "CREATE_INDEX"}, {"create lucene index", "CREATE_LUCENE_INDEX"},
      {"update", "UPDATE"}, {"insert", "INSERT_INTO"}, {"insert into", "INSERT_INTO"},
      {"truncate", "TRUNCATE"}, {"drop", "DROP"}, {"drop index", "DROP_INDEX"},
      {"drop table", "DROP_TABLE"}, {"drop keyspace", "DROP_KEYSPACE"},
      {"drop trigger", "DROP_TRIGGER"}, {"select", "SELECT"}, {"add", "ADD"}, {"list", "LIST"},
      {"list process", "LIST_PROCESS"}, {"list udf", "LIST_UDF"}, {"list trigger", "LIST_TRIGGER"},
      {"remove udf", "REMOVE_UDF"}, {"delete", "DELETE"}, {"set options", "SET_OPTIONS"},
      {"explain plan", "EXPLAIN_PLAN"}, {"alter", "ALTER"}, {"alter keyspace", "ALTER_KEYSPACE"},
      {"alter table", "ALTER_TABLE"}, {"stop", "STOP"}, {"describe", "DESCRIBE"},
      {"describe keyspace", "DESCRIBE_KEYSPACE"}, {"describe table", "DESCRIBE_TABLE"},};


  /**
   * Parse a input text and return the equivalent HelpStatement.
   * 
   * @param inputText The input text.
   * @return A Statement or null if the process failed.
   */
  private HelpStatement parseStatement(String inputText) {
    HelpStatement result = null;
    ANTLRStringStream input = new ANTLRStringStream(inputText);
    MetaHelpLexer lexer = new MetaHelpLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MetaHelpParser parser = new MetaHelpParser(tokens);
    try {
      result = parser.query();
    } catch (RecognitionException e) {
      logger.error("Cannot parse statement", e);
    }
    return result;
  }

  @Test
  public void help() {
    String inputText = "HELP;";
    String expectedText = "HELP CONSOLE_HELP";
    HelpStatement st = parseStatement(inputText);
    assertNotNull(st, "Cannot parse help - basic");
    assertEquals(st.toString(), expectedText, "Cannot parse help - basic");
  }

  @Test
  public void helpTypes() {

    for (String[] test : supportedHelpCommands) {
      HelpStatement st = parseStatement("HELP " + test[0] + ";");
      assertNotNull(st, "Cannot parse help - type " + test[1]);
      assertEquals(st.toString(), "HELP " + test[1], "Cannot parse help - basic");
    }
  }

}
