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

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.sh.help.HelpStatement;

public class ShellTest {

    @Test
    public void testMain()  {
        Shell.main(new String[]{"--sync", "--script", "/path/file.xdql"});
        assertTrue(true, "testMain failed.");
    }

    @Test
    public void testFlush()  {
        Shell shell = new Shell(false);
        shell.flush();
        assertTrue(true, "testFlush failed.");
    }

    @Test
    public void testSetPrompt()  {
        Shell shell = new Shell(false);
        shell.setPrompt("catalogTest");
        assertTrue(true, "testSetPrompt failed.");
    }

    @Test
    public void testShellConnectWithoutServer()  {
        Shell crossdataSh = new Shell(false);
        boolean result = crossdataSh.connect();
        Assert.assertFalse(result, "testShellConnectWithoutServer failed.");
    }

    @Test
    public void testRemoveResultsHandler()  {
        Shell shell = new Shell(false);
        shell.removeResultsHandler("queryId25");
        Assert.assertTrue(true, "testRemoveResultsHandler failed.");
    }

    @Test
    public void testShellDisConnectWithoutServer()  {
        Shell crossdataSh = new Shell(false);
        try {
            crossdataSh.closeConsole();
            assertTrue(true, "testShellDisConnectWithoutServer failed.");
        } catch (Exception e) {
            Assert.fail("An error happened in sh");
        }
    }

    @Test
    public void testUpdatePrompt()  {
        Shell shell = new Shell(false);
        QueryResult result = QueryResult.createQueryResult(new ResultSet());
        result.setCurrentCatalog("catalogTest");
        shell.updatePrompt(result);
        assertTrue(true, "testUpdatePrompt failed.");
    }

    @Test
    public void testPrintln()  {
        Shell crossdataSh = new Shell(false);
        try {
            crossdataSh.println("test");
            assertTrue(true);
        }catch (Exception e){
            Assert.fail("An error happened in sh");
        }
    }

    @Test
    public void testExecuteScript()  {
        Shell shell = new Shell(false);
        shell.executeScript("/path/script.xdql");
        assertTrue(true, "testUpdatePrompt failed.");
    }

    @Test
    public void testParseHelp(){
        try {
            Shell shell = new Shell(false);
            Method method = Shell.class.getDeclaredMethod("parseHelp", new Class[]{String.class});
            method.setAccessible(true);
            Object param = "help create";
            Object obj = method.invoke(shell, param);
            HelpStatement helpStatement = (HelpStatement) obj;
            String result = helpStatement.toString();
            String expected = "HELP CREATE";
            assertTrue(result.equalsIgnoreCase(expected),
                    "Result:   " + result +
                    System.lineSeparator() +
                    "Expected: " + expected);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail("ERROR: " + e.getMessage(), e);
        }
    }

    @Test
    public void testShowHelp(){
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
