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

import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

public class CResultSetIteratorTest {

    private Random rand;
  MetaResultSet rSet;

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


    private MetaResultSet buildRandomResultSet(){
      MetaResultSet rSet = new MetaResultSet();

        Cell cellStr = new Cell(new String("comment" + rand.nextInt(100)));
        Cell cellInt = new Cell(new Integer(rand.nextInt(50)));
        Cell cellBool = new Cell(new Boolean(rand.nextBoolean()));
        Cell cellLong = new Cell(new Long(rand.nextLong()));
        rSet.add(new Row("str", cellStr));
        rSet.add(new Row("int", cellInt));
        rSet.add(new Row("bool", cellBool));
        rSet.add(new Row("long", cellLong));

        List<ColumnMetadata> columnMetadataList = new ArrayList<>();

        ColumnMetadata cmStr = new ColumnMetadata("table", "str");
        ColumnType str = ColumnType.VARCHAR;
        str.setDBMapping("VARCHAR", String.class);
        cmStr.setType(str);
        columnMetadataList.add(cmStr);

        ColumnMetadata cmInt = new ColumnMetadata("table", "int");
        ColumnType integer = ColumnType.INT;
        integer.setDBMapping("INT", Integer.class);
        cmInt.setType(integer);
        columnMetadataList.add(cmInt);

        ColumnMetadata cmBool = new ColumnMetadata("table", "bool");
        ColumnType bool = ColumnType.BOOLEAN;
        bool.setDBMapping("BOOLEAN", Boolean.class);
        cmBool.setType(bool);
        columnMetadataList.add(cmBool);

        ColumnMetadata cmLong = new ColumnMetadata("table", "long");
        ColumnType longInteger = ColumnType.INT;
        longInteger.setDBMapping("LONG", Long.class);
        cmLong.setType(longInteger);
        columnMetadataList.add(cmLong);

        rSet.setColumnMetadata(columnMetadataList);

        return rSet;
    }
}
