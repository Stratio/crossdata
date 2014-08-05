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

package com.stratio.meta.core.grammar;

import com.stratio.meta.common.result.ErrorResult;
import com.stratio.meta.common.result.ErrorType;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.MetaQuery;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


/**
 * MetaParser tests that recognize the different options of each Statement.
 */
public class ParsingTest {

  protected final Parser parser = new Parser();

  public MetaStatement testRegularStatement(String inputText, String methodName) {
    MetaQuery mq = parser.parseStatement(inputText);
    MetaStatement st = mq.getStatement();
    ErrorResult er = Result.createErrorResult(ErrorType.NOT_SUPPORTED, "null");
    if (ErrorResult.class.isInstance(mq.getResult())) {
      er = ErrorResult.class.cast(mq.getResult());
    }

    assertNotNull(st, "Cannot parse " + methodName
                      + " parser error: " + mq.hasError()
                      + " -> " + er.getErrorMessage());
    assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                               + "' from '" + st.toString() + "' returned: " + er
        .getErrorMessage());

    assertTrue(inputText.equalsIgnoreCase(st.toString() + ";"),
               "Cannot parse " + methodName
               + ": \nexpecting\n'" + inputText
               + "' \nfrom\n'" + st.toString() + ";'"
    );
    return st;
  }

  public MetaStatement testRegularStatement(String inputText, String expectedQuery,
                                            String methodName) {
    MetaQuery mq = parser.parseStatement(inputText);
    MetaStatement st = mq.getStatement();
    ErrorResult er = Result.createErrorResult(ErrorType.NOT_SUPPORTED, "null");
    if (ErrorResult.class.isInstance(mq.getResult())) {
      er = ErrorResult.class.cast(mq.getResult());
    }
    assertNotNull(st, "Cannot parse " + methodName
                      + " parser error: " + mq.hasError()
                      + " -> " + er.getErrorMessage());
    assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                               + "' from '" + st.toString() + "' returned: " + er
        .getErrorMessage());

    assertTrue(expectedQuery.equalsIgnoreCase(st.toString() + ";"),
               "Cannot parse " + methodName
               + ": expecting '" + expectedQuery
               + "' from '" + st.toString() + ";"
    );
    return st;
  }

  public void testParseFails(String inputText, String methodName) {
    MetaQuery mq = parser.parseStatement(inputText);
    assertNotNull(mq, "Parser should return a query");
    assertTrue(mq.hasError(), "Parser should return and error for " + methodName);
    assertNull(mq.getStatement(), "Null statement expected. Returned: " + mq.getStatement());
  }

  public void testRecoverableError(String inputText, String methodName) {
    MetaQuery metaQuery = parser.parseStatement(inputText);
    assertTrue(metaQuery.hasError(), "No errors reported in " + methodName);
  }

  @Test
  public void unknownFirstWordOfStatement() {
    String inputText = "WINDOWS GO HOME;";
    testParseFails(inputText, "unknown_first_word_of_statement");
  }

}
