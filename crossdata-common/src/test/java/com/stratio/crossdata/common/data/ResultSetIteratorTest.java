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

package com.stratio.crossdata.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;

public class ResultSetIteratorTest {

    ResultSet rSet;
    private Random rand;

    @BeforeClass
    public void setUp() {
        rand = new Random();
        rSet = buildRandomResultSet();
    }

    @Test
    public void testConstructor() {
        ResultSetIterator rSetIt = new ResultSetIterator(rSet);
        Assert.assertNotNull(rSetIt);
    }

    @Test
    public void testHasNext() {
        ResultSetIterator rSetIt = new ResultSetIterator(rSet);
        Assert.assertTrue(rSetIt.hasNext());
    }

    @Test
    public void testNext() {
        ResultSetIterator rSetIt = new ResultSetIterator(rSet);
        Row nextRow = rSetIt.next();
        Assert.assertNotNull(nextRow);
        Assert.assertTrue(((String) nextRow.getCell("str").getValue()).startsWith("comment"));
    }

    private ResultSet buildRandomResultSet() {
        ResultSet rSet = new ResultSet();

        Cell cellStr = new Cell(new String("comment" + rand.nextInt(100)));
        Cell cellInt = new Cell(new Integer(rand.nextInt(50)));
        Cell cellBool = new Cell(new Boolean(rand.nextBoolean()));
        Cell cellLong = new Cell(new Long(rand.nextLong()));
        rSet.add(new Row("str", cellStr));
        rSet.add(new Row("int", cellInt));
        rSet.add(new Row("bool", cellBool));
        rSet.add(new Row("long", cellLong));

        List<ColumnMetadata> columnMetadataList = new ArrayList<>();

        ColumnMetadata cmStr = new ColumnMetadata(new ColumnName("catalogTest", "tableTest", "str"), null,
                ColumnType.VARCHAR);
        columnMetadataList.add(cmStr);

        ColumnMetadata cmInt = new ColumnMetadata(new ColumnName("catalogTest", "tableTest", "int"), null,
                ColumnType.INT);
        columnMetadataList.add(cmInt);

        ColumnMetadata cmBool = new ColumnMetadata(new ColumnName("catalogTest", "tableTest", "bool"), null,
                ColumnType.BOOLEAN);

        columnMetadataList.add(cmBool);

        ColumnMetadata cmLong = new ColumnMetadata(new ColumnName("catalogTest", "tableTest", "long"), null,
                ColumnType.BIGINT);
        columnMetadataList.add(cmLong);

        rSet.setColumnMetadata(columnMetadataList);

        return rSet;
    }
}
