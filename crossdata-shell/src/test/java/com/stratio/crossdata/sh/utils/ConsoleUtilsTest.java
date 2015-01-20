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

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.result.StorageResult;
import com.stratio.crossdata.sh.Shell;

import jline.console.ConsoleReader;

public class ConsoleUtilsTest {
    @Test
    public void testStringResultWithError() throws Exception {
        String errorMessage = "Connection Error";
        Result result = MetadataResult.createConnectionErrorResult(errorMessage);
        String queryId = "testStringResultWithError";
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
        StorageResult result = StorageResult.createSuccessFulStorageResult("Success");
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
