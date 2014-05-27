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

import java.util.*;

public class CassandraResultSetTest {

    private Random rand;
    CassandraResultSet rSet;

    @BeforeClass
    public void setUp(){
        rand = new Random();
    }

    @Test
    public void testConstructor(){
        rSet = new CassandraResultSet();
        Assert.assertNotNull(rSet);
    }

    @Test
    public void testConstructorWithList(){
        rSet = new CassandraResultSet(buildRowList());
        rSet.setColumnDefinitions(buildColumnDefinitions());
        Assert.assertNotNull(rSet);
        Assert.assertEquals(rSet.size(), 4);
    }

    @Test
    public void testGetRows(){
        rSet = new CassandraResultSet();
        rSet.add(new Row("str",new Cell(new String("comment" + rand.nextInt(100)))));
        rSet.add(new Row("int", new Cell(new Integer(rand.nextInt(50)))));

        Assert.assertEquals(rSet.getRows().size(), 2);
    }

    @Test
    public void testColDefs(){
        rSet = new CassandraResultSet();

        Map colDefs = new HashMap<String, ColumnDefinition>();
        colDefs.put("str", new ColumnDefinition(String.class));
        colDefs.put("int", new ColumnDefinition(Integer.class));

        rSet.setColumnDefinitions(colDefs);

        Assert.assertEquals(rSet.getColumnDefinitions().get("int").getDatatype(), Integer.class);
    }

    @Test
    public void testNext(){

    }

    private List<Row> buildRowList(){
        Cell cellStr = new Cell(new String("comment" + rand.nextInt(100)));
        Cell cellInt = new Cell(new Integer(rand.nextInt(50)));
        Cell cellBool = new Cell(new Boolean(rand.nextBoolean()));
        Cell cellLong = new Cell(new Long(rand.nextLong()));
        List<Row> list = new ArrayList();
        list.add(new Row("str", cellStr));
        list.add(new Row("int", cellInt));
        list.add(new Row("bool", cellBool));
        list.add(new Row("long", cellLong));

        return list;
    }

    private Map<String, ColumnDefinition> buildColumnDefinitions() {
        Map colDefs = new HashMap<String, ColumnDefinition>();
        colDefs.put("str", new ColumnDefinition(String.class));
        colDefs.put("int", new ColumnDefinition(Integer.class));
        colDefs.put("bool", new ColumnDefinition(Boolean.class));
        colDefs.put("long", new ColumnDefinition(Long.class));
        return colDefs;
    }

}
