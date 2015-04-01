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
package com.stratio.crossdata.sh;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.thread.ThreadTimeoutException;

import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.sh.help.HelpStatement;

public class ShellTest {

    @Test(timeOut = 8000, expectedExceptions = ThreadTimeoutException.class)
    public void testMain() {
        try {
            Shell.main(new String[] { "--sync", "--script", "/path/file.xdql" });
        } catch (Exception ex){
            assertTrue(true, "Impossible to happen");
        }
        fail("Connection with server should not have been established");
    }

    @Test(timeOut = 4000)
    public void testFlush() {
        boolean ok = true;
        try {
            Shell shell = new Shell(false);
            shell.flush();
        } catch (Exception ex) {
            fail("testFlush failed.");
        }
        assertEquals(true, ok, "testFlush failed.");
    }

    @Test(timeOut = 4000)
    public void testSetPrompt() {
        boolean ok = true;
        try {
            Shell shell = new Shell(false);
            shell.setPrompt("catalogTest");
        } catch (Exception e) {
            fail("testSetPrompt failed");
        }
        assertEquals(ok, true, "testSetPrompt failed.");
    }

    @Test(timeOut = 4000, expectedExceptions = ThreadTimeoutException.class)
    public void testShellConnectWithoutServer() {
        try {
            Shell crossdataSh = new Shell(false);
            boolean result = crossdataSh.connect("userTest","passwordTest");
        } catch (Exception e) {
            assertTrue(true, "Impossible to happen");
        }
        fail("Connection with server should not have been established");
    }

    @Test(timeOut = 4000)
    public void testRemoveResultsHandler() {
        boolean ok = true;
        try {
            Shell shell = new Shell(false);
            shell.removeResultsHandler("queryId25");
        } catch (Exception e) {
            fail("testRemoveResultsHandler failed.");
        }
        Assert.assertEquals(true, ok, "testRemoveResultsHandler failed.");
    }

    @Test(timeOut = 4000)
    public void testShellDisConnectWithoutServer() {
        boolean ok = true;
        Shell crossdataSh = new Shell(false);
        try {
            crossdataSh.closeConsole();
        } catch (Exception e) {
            fail("An error happened in sh");
        }
        assertEquals(true, ok, "testShellDisConnectWithoutServer failed.");
    }

    @Test(timeOut = 4000)
    public void testUpdatePrompt() {
        boolean ok = true;
        try {
            Shell shell = new Shell(false);
            QueryResult result = QueryResult.createQueryResult(UUID.randomUUID().toString(), new ResultSet(), 0, true);
            result.setCurrentCatalog("catalogTest");
            shell.updatePrompt(result);
        } catch (Exception e) {
            fail("testUpdatePrompt failed.");
        }
        assertEquals(true, ok, "testUpdatePrompt failed.");
    }

    @Test(timeOut = 4000)
    public void testPrintln() {
        boolean ok = true;
        Shell crossdataSh = new Shell(false);
        try {
            crossdataSh.println("test");
        } catch (Exception e) {
            fail("An error happened in sh");
        }
        assertEquals(ok, true, "An error happened in sh");
    }

    @Test(timeOut = 4000)
    public void testExecuteScript() {
        boolean ok = true;
        Shell shell = new Shell(false);
        try {
            shell.executeScript("/path/script.xdql");
        } catch (Exception e) {
            fail("testUpdatePrompt failed.");
        }
        assertEquals(true, ok, "testUpdatePrompt failed.");

    }

    @Test(timeOut = 4000)
    public void testParseHelp() {
        try {
            Shell shell = new Shell(false);
            Method method = Shell.class.getDeclaredMethod("parseHelp", new Class[] { String.class });
            method.setAccessible(true);
            Object param = "help create";
            Object obj = method.invoke(shell, param);
            HelpStatement helpStatement = (HelpStatement) obj;
            String result = helpStatement.toString();
            String expected = "HELP CREATE";
            assertEquals(result, expected,
                    "Result:   " + result +
                    System.lineSeparator() +
                    "Expected: " + expected);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail("ERROR: " + e.getMessage(), e);
        }
    }

    @Test(timeOut = 4000)
    public void testShowHelp() {
        try {
            Shell shell = new Shell(false);
            Method method = Shell.class.getDeclaredMethod("showHelp", String.class);
            method.setAccessible(true);
            Object param = "help insert";
            method.invoke(shell, param);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail("ERROR: " + e.getMessage(), e);
        }
    }

}
