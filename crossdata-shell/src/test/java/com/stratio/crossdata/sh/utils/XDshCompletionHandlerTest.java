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

import java.util.ArrayList;

import org.testng.annotations.Test;

import jline.console.ConsoleReader;

public class XDshCompletionHandlerTest {

    @Test
    public void testCompleteOK() throws Exception {
        XDshCompletionHandler handler = new XDshCompletionHandler();
        ArrayList<CharSequence> candidates = new ArrayList<>();
        candidates.add("test");
        candidates.add("word");
        boolean result = handler.complete(new ConsoleReader(), candidates, 0);
        assertTrue(result, "Handler didn't complete properly");
    }

    @Test
    public void testCompleteNoCandidates() throws Exception {
        XDshCompletionHandler handler = new XDshCompletionHandler();
        boolean result = handler.complete(new ConsoleReader(), new ArrayList<CharSequence>(), 0);
        assertTrue(result, "Handler didn't complete properly");
    }

    @Test
    public void testCompleteFail() throws Exception {
        XDshCompletionHandler handler = new XDshCompletionHandler();
        try {
            handler.complete(null, null, -1);
            fail("NullPointerException was expected");
        } catch (NullPointerException npe) {
            assertTrue(true, "NullPointerException was expected");
        }

    }

}
