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

package com.stratio.meta.common.data;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CellTest {

    @BeforeClass
    public void setUp() {
    }

    @Test
    public void testConstructor() {
        Cell cellStr = new Cell(new String("comment1"));
        Assert.assertNotNull(cellStr);
    }

    @Test
    public void testDataype() {
        Cell cellStr = new Cell(new String("comment1"));
        Assert.assertEquals(cellStr.getValue().getClass(), String.class);
    }

    @Test
    public void testGetValue() {
        Cell cellStr = new Cell(new String("comment1"));
        Assert.assertTrue("comment1".equals((String) cellStr.getValue()));
    }
}
