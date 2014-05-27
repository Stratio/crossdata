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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CellTest {

    @BeforeClass
    public void setUp(){
    }

    @Test
    public void testConstructor(){
        Cell cellStr = new Cell(new String("comment1"));
        Assert.assertNotNull(cellStr);
    }

    @Test
    public void testDataype(){
        Cell cellStr = new Cell(new String("comment1"));
        Assert.assertEquals(cellStr.getValue().getClass(), String.class);
    }

    @Test
    public void testGetValue(){
        Cell cellStr = new Cell(new String("comment1"));
        Assert.assertTrue(((String) cellStr.getValue()).equals("comment1"));
    }
}
