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

import java.util.Random;

public class CResultSetIteratorTest {

    private Random rand;
    CassandraResultSet rSet;

    @BeforeClass
    public void setUp(){
        rand = new Random();
        rSet = buildRandomResultSet();
    }

    @Test
    public void testConstructor(){
        CResultSetIterator rSetIt = new CResultSetIterator(rSet);
        Assert.assertNotNull(rSetIt);
    }

    @Test
    public void testHasNext(){
        CResultSetIterator rSetIt = new CResultSetIterator(rSet);
        Assert.assertTrue(rSetIt.hasNext());
    }

    @Test
    public void testNext(){
        CResultSetIterator rSetIt = new CResultSetIterator(rSet);
        Row nextRow = rSetIt.next();
        Assert.assertNotNull(nextRow);
        Assert.assertTrue(((String)nextRow.getCell("str").getValue()).startsWith("comment"));
    }


    private CassandraResultSet buildRandomResultSet(){
        Cell cellStr = new Cell(String.class, new String("comment" + rand.nextInt(100)));
        Cell cellInt = new Cell(Integer.class, new Integer(rand.nextInt(50)));
        Cell cellBool = new Cell(Boolean.class, new Boolean(rand.nextBoolean()));
        Cell cellLong = new Cell(Long.class, new Long(rand.nextLong()));
        CassandraResultSet rSet = new CassandraResultSet();
        rSet.add(new Row("str", cellStr));
        rSet.add(new Row("int", cellInt));
        rSet.add(new Row("bool", cellBool));
        rSet.add(new Row("long", cellLong));

        return rSet;
    }
}
