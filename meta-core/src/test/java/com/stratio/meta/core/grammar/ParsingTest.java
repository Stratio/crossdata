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
        assertNotNull(st, "Cannot parse "+methodName
                + " parser error: " + mq.hasError()
                + " -> " + mq.getResult().getErrorMessage());
        assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                + "' from '" + st.toString() + "' returned: " + mq.getResult().getErrorMessage());
        
        //System.out.println("inputText:"+inputText);
        //System.out.println("st.toStrg:"+st.toString()+";");
        
        assertTrue(inputText.equalsIgnoreCase(st.toString()+";"),
                "Cannot parse " + methodName
                        + ": expecting '" + inputText
                        + "' from '" + st.toString()+";'" );
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
    public void unknown_first_word_of_statement(){
        String inputText = "WINDOWS GO HOME;";
        testParseFails(inputText, "unknown_first_word_of_statement");
    }

}
