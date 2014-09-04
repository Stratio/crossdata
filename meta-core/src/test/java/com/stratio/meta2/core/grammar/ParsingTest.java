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

package com.stratio.meta2.core.grammar;

import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta2.core.parser.Parser;
import com.stratio.meta2.core.statements.MetaStatement;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;


/**
 * MetaParser tests that recognize the different options of each Statement.
 */
public class ParsingTest {

  protected final Parser parser = new Parser();

  public MetaStatement testRegularStatement(String inputText, String methodName) {
    MetaStatement st = null;
    try {
      st = parser.parseStatement("", inputText);
    } catch (ParsingException e) {
      StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
      sb.append(System.lineSeparator());
      if((e.getErrors() != null) && (!e.getErrors().isEmpty())){
        for(String errorStr: e.getErrors()){
          sb.append(" - "+errorStr);
          sb.append(System.lineSeparator());
        }
      }
      System.err.println(sb.toString());
      e.printStackTrace();
      fail(sb.toString(), e);
    }

    assertTrue(inputText.equalsIgnoreCase(st.toString() + ";"),
               "Cannot parse " + methodName
               + ": " + System.lineSeparator() +" expecting" + System.lineSeparator() + "'" + inputText
               + "' " + System.lineSeparator() + "from" + System.lineSeparator() + "'" + st.toString() + ";'");
    return st;
  }

  public MetaStatement testRegularStatement(String inputText, String expectedQuery,
                                            String methodName) {
    MetaStatement st = null;
    try {
      st = parser.parseStatement("", inputText);
    } catch (ParsingException e) {
      StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
      sb.append(System.lineSeparator());
      if((e.getErrors() != null) && (!e.getErrors().isEmpty())){
        for(String errorStr: e.getErrors()){
          sb.append(" - "+errorStr);
          sb.append(System.lineSeparator());
        }
      }
      System.err.println(sb.toString());
      e.printStackTrace();
      fail(sb.toString(), e);
    }

    assertTrue(expectedQuery.equalsIgnoreCase(st.toString() + ";"),
               "Cannot parse " + methodName
               + ": expecting " + System.lineSeparator() + "'" + expectedQuery
               + "' from " + System.lineSeparator() + "'" + st.toString() + ";");
    return st;
  }

  public MetaStatement testRegularStatementSession(String sessionCatalog, String inputText, String methodName){
    MetaStatement st = null;
    try {
      st = parser.parseStatement(sessionCatalog, inputText);
    } catch (ParsingException e) {
      StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
      sb.append(System.lineSeparator());
      if((e.getErrors() != null) && (!e.getErrors().isEmpty())){
        for(String errorStr: e.getErrors()){
          sb.append(" - "+errorStr);
          sb.append(System.lineSeparator());
        }
      }
      System.err.println(sb.toString());
      e.printStackTrace();
      fail(sb.toString(), e);
    }

    assertTrue(inputText.equalsIgnoreCase(st.toString() + ";"),
               "Cannot parse " + methodName
               + ": " + System.lineSeparator() + "expecting" + System.lineSeparator() + "'" + inputText
               + "' " + System.lineSeparator() + "from" + System.lineSeparator() + "'" + st.toString() + ";'");
    return st;
  }

  public MetaStatement testRegularStatementSession(String sessionCatalog, String inputText, String expectedText, String methodName){
    MetaStatement st = null;
    try {
      st = parser.parseStatement(sessionCatalog, inputText);
    } catch (ParsingException e) {
      StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
      sb.append(System.lineSeparator());
      if((e.getErrors() != null) && (!e.getErrors().isEmpty())){
        for(String errorStr: e.getErrors()){
          sb.append(" - "+errorStr);
          sb.append(System.lineSeparator());
        }
      }
      System.err.println(sb.toString());
      e.printStackTrace();
      fail(sb.toString(), e);
    }

    assertTrue(expectedText.equalsIgnoreCase(st.toString() + ";"),
               "Cannot parse " + methodName
               + ": expecting " + System.lineSeparator() + "'" + expectedText
               + "' from " + System.lineSeparator() + "'" + st.toString() + ";");
    return st;
  }

  public void testParserFails(String inputText, String methodName) {
    MetaStatement st = null;
    try {
      st = parser.parseStatement("parsing-test", inputText);
    } catch (ParsingException e) {
      StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER EXCEPTION: ").append(e.getMessage());
      sb.append(System.lineSeparator());
      if((e.getErrors() != null) && (!e.getErrors().isEmpty())){
        for(String errorStr: e.getErrors()){
          sb.append(" - "+errorStr);
          sb.append(System.lineSeparator());
        }
      }
      System.err.println(sb.toString());
      return;
    }

    if(st != null){
      try {
        st.toString();
      } catch (NullPointerException npe){
        System.err.println("[" + methodName + "] PARSER EXCEPTION: " + npe.getMessage());
        npe.printStackTrace();
        return;
      }
      assertFalse(inputText.equalsIgnoreCase(st.toString()+";"), "Test passed but it should have failed");
    }
  }

  public void testParserFails(String sessionCatalog, String inputText, String methodName) {
    MetaStatement st = null;
    try {
      st = parser.parseStatement(sessionCatalog, inputText);
    } catch (ParsingException e) {
      StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER EXCEPTION: ").append(e.getMessage());
      sb.append(System.lineSeparator());
      if((e.getErrors() != null) && (!e.getErrors().isEmpty())){
        for(String errorStr: e.getErrors()){
          sb.append(" - "+errorStr);
          sb.append(System.lineSeparator());
        }
      }
      System.err.println(sb.toString());
      return;
    }

    if(st != null){
      try {
        st.toString();
      } catch (NullPointerException npe){
        System.err.println("[" + methodName + "] PARSER EXCEPTION: " + npe.getMessage());
        npe.printStackTrace();
        return;
      }
      assertFalse(inputText.equalsIgnoreCase(st.toString()+";"), "Test passed but it should have failed");
    }
  }

  @Test
  public void unknownFirstWordOfStatement() {
    String inputText = "WINDOWS GO HOME;";
    testParserFails(inputText, "unknown_first_word_of_statement");
  }

}
