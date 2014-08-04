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

import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

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
        CassandraResultSet rSet = new CassandraResultSet();

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
