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

package com.stratio.connector.inmemory.datastore;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Class to test the InMemoryOperations.
 */
public class InMemoryOperationsTest {

    final int n1 = 1;
    final int n2 = 10;

    final boolean b1 = true;
    final boolean b2 = false;

    final String s1 = "a";
    final String s2 = "z";

    @Test
    public void eq(){
        assertTrue(InMemoryOperations.EQ.compare(n1, n1), "EQ fail on 1 = 1");
        assertFalse(InMemoryOperations.EQ.compare(n2, n1), "EQ fail on 10 = 1");
        assertFalse(InMemoryOperations.EQ.compare(n1, n2), "EQ fail on 1 = 10");

        assertTrue(InMemoryOperations.EQ.compare(b1, b1), "EQ fail on true = true");
        assertFalse(InMemoryOperations.EQ.compare(b2, b1), "EQ fail on false = true");
        assertFalse(InMemoryOperations.EQ.compare(b1, b2), "EQ fail on true = false");

        assertTrue(InMemoryOperations.EQ.compare(s1, s1), "EQ fail on aaaa = zzzz");
        assertFalse(InMemoryOperations.EQ.compare(s2, s1), "EQ fail on zzzz = aaaa");
        assertFalse(InMemoryOperations.EQ.compare(s1, s2), "EQ fail on aaaa = zzzz");
    }

    @Test
    public void gt(){
        assertFalse(InMemoryOperations.GT.compare(n1, n1), "GT fail on 1 = 1");
        assertTrue(InMemoryOperations.GT.compare(n2, n1), "GT fail on 10 = 1");
        assertFalse(InMemoryOperations.GT.compare(n1, n2), "GT fail on 1 = 10");

        assertFalse(InMemoryOperations.GT.compare(b1, b1), "GT fail on true = true");
        assertFalse(InMemoryOperations.GT.compare(b2, b1), "GT fail on false = true");
        assertTrue(InMemoryOperations.GT.compare(b1, b2), "GT fail on true = false");

        assertFalse(InMemoryOperations.GT.compare(s1, s1), "GT fail on aaaa = zzzz");
        assertTrue(InMemoryOperations.GT.compare(s2, s1), "GT fail on zzzz = aaaa");
        assertFalse(InMemoryOperations.GT.compare(s1, s2), "GT fail on aaaa = zzzz");
    }

}
