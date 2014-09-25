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

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RowTest {

    @Test
    public void testSize() {
        Row row = new Row("str1", new Cell(new String("comment1")));
        Assert.assertEquals(row.size(), 1);
    }

    @Test
    public void testAddCell() {
        Row row = new Row("str1", new Cell(new String("comment1")));
        row.addCell("str2", new Cell(new String("comment2")));
        Assert.assertEquals(row.size(), 2);
    }

    @Test
    public void testSetCells() {
        Map<String, Cell> map = new HashMap<>();
        map.put("str1", new Cell(new String("comment1")));
        map.put("str2", new Cell(new String("comment2")));
        Row row = new Row();
        row.setCells(map);
        Assert.assertEquals(row.size(), 2);
    }

    @Test
    public void testGetCells() {
        Row row = new Row("str1", new Cell(new String("comment1")));
        Map<String, Cell> cells = row.getCells();
        Assert.assertEquals(cells.size(), 1);
    }
}
