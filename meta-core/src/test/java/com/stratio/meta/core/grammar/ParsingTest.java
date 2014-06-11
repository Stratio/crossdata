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
      if(ErrorResult.class.isInstance(mq.getResult())){
        er = ErrorResult.class.cast(mq.getResult());
      }

        assertNotNull(st, "Cannot parse "+methodName
                + " parser error: " + mq.hasError()
                + " -> " + er.getErrorMessage());
        assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                + "' from '" + st.toString() + "' returned: " + er.getErrorMessage());
        
        assertTrue(inputText.equalsIgnoreCase(st.toString()+";"),
                "Cannot parse " + methodName
                        + ": \nexpecting\n'" + inputText
                        + "' \nfrom\n'" + st.toString()+";'" );
        return st;
    }

    public MetaStatement testRegularStatement(String inputText, String expectedQuery, String methodName) {
        MetaQuery mq = parser.parseStatement(inputText);
        MetaStatement st = mq.getStatement();
      ErrorResult er = Result.createErrorResult(ErrorType.NOT_SUPPORTED, "null");
      if(ErrorResult.class.isInstance(mq.getResult())){
        er = ErrorResult.class.cast(mq.getResult());
      }
        assertNotNull(st, "Cannot parse "+methodName
                + " parser error: " + mq.hasError()
                + " -> " + er.getErrorMessage());
        assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                + "' from '" + st.toString() + "' returned: " + er.getErrorMessage());

        assertTrue(expectedQuery.equalsIgnoreCase(st.toString() + ";"),
                "Cannot parse " + methodName
                        + ": expecting '" + expectedQuery
                        + "' from '" + st.toString()+";");
        return st;
    }

    public void testParseFails(String inputText, String methodName){
        MetaQuery mq = parser.parseStatement(inputText);
        assertNotNull(mq, "Parser should return a query");
        assertTrue(mq.hasError(), "Parser should return and error for " + methodName);
        assertNull(mq.getStatement(), "Null statement expected. Returned: " + mq.getStatement());
    }

    public void testRecoverableError(String inputText, String methodName){
        MetaQuery metaQuery = parser.parseStatement(inputText);
        assertTrue(metaQuery.hasError(), "No errors reported in "+methodName);
    }

    @Test
    public void unknownFirstWordOfStatement(){
        String inputText = "WINDOWS GO HOME;";
        testParseFails(inputText, "unknown_first_word_of_statement");
    }

}
