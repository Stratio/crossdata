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

package com.stratio.crossdata.core.grammar;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.UUID;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.core.parser.Parser;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;

/**
 * XDshParser tests that recognize the different options of each Statement.
 */
public class ParsingTest {

    protected final Parser parser = new Parser();

    public IParsedQuery testRegularStatement(String inputText, String methodName) {
        IParsedQuery st = null;
        try {
            BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName(""));
            st = parser.parse(baseQuery);
        } catch (ParsingException e) {
            StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
            sb.append(System.lineSeparator());
            if ((e.getErrors() != null) && (!e.getErrors().isEmpty())) {
                for (String errorStr : e.getErrors()) {
                    sb.append(" - " + errorStr);
                    sb.append(System.lineSeparator());
                }
            }
            System.err.println(sb.toString());
            e.printStackTrace();
            fail(sb.toString(), e);
        }

        assertTrue(inputText.replaceAll("\"","").equalsIgnoreCase(st.toString() + ";"),
                "Cannot parse " + methodName
                        + ": " + System.lineSeparator() + " expecting" + System.lineSeparator() + "'" + inputText
                        + "' " + System.lineSeparator() + "from" + System.lineSeparator() + "'" + st.toString() + ";'");
        return st;
    }

    public IParsedQuery testRegularStatement(String inputText, String expectedQuery,
            String methodName) {
        IParsedQuery st = null;
        try {
            BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName(""));
            //st = parser.parse("", inputText);
            st = parser.parse(baseQuery);
        } catch (ParsingException e) {
            StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
            sb.append(System.lineSeparator());
            if ((e.getErrors() != null) && (!e.getErrors().isEmpty())) {
                for (String errorStr : e.getErrors()) {
                    sb.append(" - " + errorStr);
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

    public IParsedQuery testRegularStatementSession(String sessionCatalog, String inputText,
            String methodName) {
        IParsedQuery st = null;
        try {
            BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText,
                    new CatalogName(sessionCatalog));
            st = parser.parse(baseQuery);
        } catch (ParsingException e) {
            StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
            sb.append(System.lineSeparator());
            if ((e.getErrors() != null) && (!e.getErrors().isEmpty())) {
                for (String errorStr : e.getErrors()) {
                    sb.append(" - " + errorStr);
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

    public IParsedQuery testRegularStatementSession(String sessionCatalog, String inputText,
            String expectedText, String methodName) {
        IParsedQuery st = null;
        try {
            BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText,
                    new CatalogName(sessionCatalog));
            //st = parser.parse(sessionCatalog, inputText);
            st = parser.parse(baseQuery);
        } catch (ParsingException e) {
            StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER TEST FAILED: ").append(e.getMessage());
            sb.append(System.lineSeparator());
            if ((e.getErrors() != null) && (!e.getErrors().isEmpty())) {
                for (String errorStr : e.getErrors()) {
                    sb.append(" - " + errorStr);
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
        IParsedQuery st;
        try {
            BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName(""));
            st = parser.parse(baseQuery);
        } catch (ParsingException e) {
            StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER EXCEPTION: ").append(e.getMessage());
            sb.append(System.lineSeparator());
            if ((e.getErrors() != null) && (!e.getErrors().isEmpty())) {
                for (String errorStr : e.getErrors()) {
                    sb.append(" - " + errorStr);
                    sb.append(System.lineSeparator());
                }
            }
            System.err.println(sb.toString());
            assertNotNull(e);
            return;
        }

        if (st != null) {
            try {
                st.toString();
            } catch (NullPointerException npe) {
                System.err.println("[" + methodName + "] PARSER EXCEPTION: " + npe.getMessage());
                npe.printStackTrace();
                assertNotNull(npe);
                return;
            }
            assertFalse(inputText.equalsIgnoreCase(st.toString() + ";"), "Test passed but it should have failed");
        }
    }

    public void testParserFails(String sessionCatalog, String inputText, String methodName) {
        IParsedQuery st;
        try {
            BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName(""));
            st = parser.parse(baseQuery);
        } catch (ParsingException e) {
            StringBuilder sb = new StringBuilder("[" + methodName + "] PARSER EXCEPTION: ").append(e.getMessage());
            sb.append(System.lineSeparator());
            if ((e.getErrors() != null) && (!e.getErrors().isEmpty())) {
                for (String errorStr : e.getErrors()) {
                    sb.append(" - " + errorStr);
                    sb.append(System.lineSeparator());
                }
            }
            System.err.println(sb.toString());
            assertNotNull(e);
            return;
        }

        if (st != null) {
            try {
                st.toString();
            } catch (NullPointerException npe) {
                System.err.println("[" + methodName + "] PARSER EXCEPTION: " + npe.getMessage());
                npe.printStackTrace();
                assertNotNull(npe);
                return;
            }
            assertFalse(inputText.equalsIgnoreCase(st.toString() + ";"), "Test passed but it should have failed");
        }
    }

    @Test
    public void unknownFirstWordOfStatement() {
        String inputText = "WINDOWS GO HOME;";
        testParserFails(inputText, "unknown_first_word_of_statement");
    }

}
