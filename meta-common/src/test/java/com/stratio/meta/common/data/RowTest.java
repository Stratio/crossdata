/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.common.data;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class RowTest {

    @Test
    public void testSize(){
        Row row = new Row("str1",new Cell(String.class, new String("comment1")));
        Assert.assertEquals(row.size(),1);
    }

    @Test
    public void testAddCell(){
        Row row = new Row("str1",new Cell(String.class, new String("comment1")));
        row.addCell("str2",new Cell(String.class, new String("comment2")));
        Assert.assertEquals(row.size(),2);
    }

    @Test
    public void testSetCells(){
        Map<String, Cell> map = new HashMap();
        map.put("str1",new Cell(String.class, new String("comment1")));
        map.put("str2",new Cell(String.class, new String("comment2")));
        Row row = new Row();
        row.setCells(map);
        Assert.assertEquals(row.size(),2);
    }

    @Test
    public void testGetCells(){
        Row row = new Row("str1",new Cell(String.class, new String("comment1")));
        Map<String, Cell> cells = row.getCells();
        Assert.assertEquals(cells.size(),1);
    }
}
