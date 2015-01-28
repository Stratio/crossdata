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

package com.stratio.crossdata.common.statements.structures;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.TableName;

public class StringSelectorTest {

    private void compareStrings(String result, String expected, String testName){
        assertTrue(result.equalsIgnoreCase(expected),
                System.lineSeparator() +
                this.getClass().getCanonicalName() + "." + testName + " failed." + System.lineSeparator() +
                "Result:   " + result + System.lineSeparator() +
                "Expected: " + expected);
    }

    @Test
    public void testGetValue() throws Exception {
        StringSelector selector = new StringSelector("StringTest");
        compareStrings("StringTest", selector.getValue(), "testGetValue");
    }

    @Test
    public void testGetType() throws Exception {
        StringSelector selector = new StringSelector(new TableName("catalog1", "table1"), "StringTest");
        assertEquals(
                selector.getType(),
                SelectorType.STRING,
                System.lineSeparator() +
                this.getClass().getCanonicalName() + ".testGetType failed." + System.lineSeparator() +
                "Result:   " + selector.getType() + System.lineSeparator() +
                "Expected: " + SelectorType.STRING);
    }

    @Test
    public void testToString() throws Exception {
        StringSelector selector = new StringSelector("StringTest");
        compareStrings(selector.toString(), "'StringTest'", "testToString");
    }

    @Test
    public void testGetStringValue() throws Exception {
        StringSelector selector = new StringSelector("StringTest");
        compareStrings(selector.getStringValue(), "StringTest", "testToString");
    }

    @Test
    public void testEquals() throws Exception {
        StringSelector selector1 = new StringSelector(new TableName("catalog1", "table1"), "StringTest");
        StringSelector selector2 = new StringSelector(new TableName("catalog1", "table1"), "StringTest");
        assertEquals(selector1, selector2,
                System.lineSeparator() +
                this.getClass().getCanonicalName() + ".testEquals failed." + System.lineSeparator() +
                selector1 + " is not equals to " + selector2);
    }

    @Test
    public void testHashCode() throws Exception {
        StringSelector selector1 = new StringSelector(new TableName("catalog1", "table1"), "HashCode");
        StringSelector selector2 = new StringSelector(new TableName("catalog1", "table1"), "HashCode");
        assertEquals(selector1, selector2,
                System.lineSeparator() +
                this.getClass().getCanonicalName() + ".testHashCode failed." + System.lineSeparator() +
                selector1 + " has not the same hash code as " + selector2);
    }
}
