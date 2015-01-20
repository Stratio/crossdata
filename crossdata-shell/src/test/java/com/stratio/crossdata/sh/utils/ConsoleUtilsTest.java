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

package com.stratio.crossdata.sh.utils;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ConnectResult;
import com.stratio.crossdata.common.result.InProgressResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.result.StorageResult;
import com.stratio.crossdata.sh.Shell;

import jline.console.ConsoleReader;

public class ConsoleUtilsTest {

    @Test
    public void testStringResultWithErrorResult() throws Exception {
        String errorMessage = "Connection Error";
        Result result = MetadataResult.createConnectionErrorResult(errorMessage);
        String queryId = "testStringResultWithErrorResult";
        result.setQueryId(queryId);
        String message = ConsoleUtils.stringResult(result);
        String expected = "The operation for query " + queryId + " cannot be executed:" +
                System.lineSeparator() +
                errorMessage +
                System.lineSeparator();
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultWithQueryResultEmpty() throws Exception {
        Result result = QueryResult.createSuccessQueryResult();
        String queryId = "testStringResultWithQueryResult";
        result.setQueryId(queryId);
        String message = ConsoleUtils.stringResult(result);
        String expected = System.lineSeparator() + "0 results returned";
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultWithQueryResult() throws Exception {
        ResultSet resultSet = new ResultSet();
        List<ColumnMetadata> columnMetadata = new ArrayList<>();
        ColumnName firstColumn = new ColumnName("catalogTest", "tableTest", "Id");
        columnMetadata.add(new ColumnMetadata(firstColumn, new Object[]{}, ColumnType.TEXT));
        ColumnName secondColumn = new ColumnName("catalogTest", "tableTest", "Number");
        columnMetadata.add(new ColumnMetadata(secondColumn, new Object[]{}, ColumnType.INT));
        resultSet.setColumnMetadata(columnMetadata);
        Row row = new Row();
        row.addCell("id", new Cell("Stratio"));
        row.addCell("number", new Cell(25));
        resultSet.add(row);
        Result result = QueryResult.createSuccessQueryResult(resultSet, "catalogTest");
        String queryId = "testStringResultWithQueryResult";
        result.setQueryId(queryId);
        String message = ConsoleUtils.stringResult(result);
        String expected = System.lineSeparator() +
                "Partial result: true" + System.lineSeparator() +
                "--------------------" + System.lineSeparator() +
                "| id      | number | " + System.lineSeparator() +
                "--------------------" + System.lineSeparator() +
                "| Stratio | 25     | " + System.lineSeparator() +
                "--------------------" + System.lineSeparator();
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultWithConnectResult() throws Exception {
        String sessionId = "12345";
        Result result = ConnectResult.createConnectResult(sessionId);
        String queryId = "testStringResultWithConnectResult";
        result.setQueryId(queryId);
        String message = ConsoleUtils.stringResult(result);
        String expected = "Connected with SessionId=" + sessionId;
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultWithMetadataResult() throws Exception {
        Result result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_CATALOG);
        String queryId = "testStringResultWithMetadataResult";
        result.setQueryId(queryId);
        String message = ConsoleUtils.stringResult(result);
        String expected = "Catalog created successfully";
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultWithInProgressResult() throws Exception {
        String queryId = "testStringResultWithInProgressResult";
        Result result = InProgressResult.createInProgressResult(queryId);
        result.setQueryId(queryId);
        String message = ConsoleUtils.stringResult(result);
        String expected = "Query " + queryId + " in progress";
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultNull() throws Exception {
        String expected = "Unknown result";
        String message = ConsoleUtils.stringResult(null);
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultWithShellOK() throws Exception {
        StorageResult result = StorageResult.createSuccessfulStorageResult("Success");
        String message = ConsoleUtils.stringResult(result, new Shell(false));
        String expected = "Success";
        assertTrue(message.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + message);
    }

    @Test
    public void testStringResultWithShellException() throws Exception {
        CommandResult result = CommandResult.createCommandResult(new CatalogName("catalogTest"));
        try {
            ConsoleUtils.stringResult(result, null);
            fail("NullPointerException was expected");
        } catch (NullPointerException npe) {
            assertTrue(true, "NullPointerException was expected");
        }
    }

    @Test
    public void testRetrieveHistoryOK() throws Exception {
        File file = ConsoleUtils.retrieveHistory(new ConsoleReader(), Shell.dateFormat);
        String result = file.getName();
        String expected = "history.txt";
        assertTrue(result.equalsIgnoreCase(expected),
                System.lineSeparator() +
                        "Expected: " + expected +
                        System.lineSeparator() +
                        "Found:    " + result);
    }

    @Test
    public void testRetrieveHistoryWrongDate() throws Exception {
        File file = ConsoleUtils.retrieveHistory(new ConsoleReader(), new SimpleDateFormat());
        String result = file.getName();
        String expected = "history.txt";
        assertTrue(result.equalsIgnoreCase(expected),
                System.lineSeparator() +
                "Expected: " + expected +
                System.lineSeparator() +
                "Found:    " + result);
    }

    @Test
    public void testRetrieveHistoryFail() throws Exception {
        try {
            ConsoleUtils.retrieveHistory(null, Shell.dateFormat);
            fail("NullPointerException was expected");
        } catch (NullPointerException npe) {
            assertTrue(true, "NullPointerException was expected");
        }
    }

    @Test
    public void testSaveHistoryOK() throws Exception {
        File fileTest = new File("fileTest.temp");
        ConsoleUtils.saveHistory(new ConsoleReader(), fileTest, Shell.dateFormat);
        assertTrue(fileTest.delete(), "Save file history failed");
    }

    @Test
    public void testSaveHistoryFail() throws Exception {
        try {
            ConsoleUtils.saveHistory(new ConsoleReader(), null, Shell.dateFormat);
            fail("NullPointerException was expected");
        } catch (NullPointerException npe) {
            assertTrue(true, "NullPointerException was expected");
        }
    }
}
