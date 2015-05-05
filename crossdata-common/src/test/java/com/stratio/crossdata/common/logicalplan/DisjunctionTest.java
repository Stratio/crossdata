/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.logicalplan;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Created by lcisneros on 4/05/15.
 */
public class DisjunctionTest {



    @Test
    public void testToString(){
        Set<Operations> operations = new LinkedHashSet<>();
        List<List<ITerm>> terms = new ArrayList<>();

        List<List<ITerm>> filters = new ArrayList<>();

        TableName tableName = new TableName("catalogName", "tableName");

        Selector left = new ColumnSelector(new ColumnName(tableName, "colName"));
        Selector right = new StringSelector("colValue");
        Relation relation = new Relation(left, Operator.EQ, right);
        Filter filter = new Filter(Collections.singleton(Operations.FILTER_PK_EQ), relation);


        Selector left2 = new ColumnSelector(new ColumnName(tableName, "colName2"));
        Selector right2 = new StringSelector("colValue2");
        Relation relation2 = new Relation(left2, Operator.EQ, right2);
        Filter filter2 = new Filter(Collections.singleton(Operations.FILTER_NON_INDEXED_EQ), relation2);

        filters.add(Arrays.asList((ITerm)filter));
        filters.add(Arrays.asList((ITerm) filter2));

        Disjunction disjunction = new Disjunction(Collections.singleton(Operations.FILTER_DISJUNCTION), filters);

        //Experimentation
        String result = disjunction.toString();
        //Expectations

        String expected = "DISJUNCTION - [FILTER_DISJUNCTION] - "
                            +"FILTER - [FILTER_PK_EQ] - catalogName.tableName.colName = 'colValue' "
                            +"OR FILTER - [FILTER_NON_INDEXED_EQ] - catalogName.tableName.colName2 = 'colValue2'";
        Assert.assertEquals(result, expected);
    }


    @Test
    public void testToStringWithAND(){
        Set<Operations> operations = new LinkedHashSet<>();
        List<List<ITerm>> terms = new ArrayList<>();

        List<List<ITerm>> filters = new ArrayList<>();

        TableName tableName = new TableName("catalogName", "tableName");

        Selector left = new ColumnSelector(new ColumnName(tableName, "colName"));
        Selector right = new StringSelector("colValue");
        Relation relation = new Relation(left, Operator.EQ, right);
        ITerm filter = new Filter(Collections.singleton(Operations.FILTER_PK_EQ), relation);


        Selector left2 = new ColumnSelector(new ColumnName(tableName, "colName2"));
        Selector right2 = new StringSelector("colValue2");
        Relation relation2 = new Relation(left2, Operator.EQ, right2);
        ITerm filter2 = new Filter(Collections.singleton(Operations.FILTER_NON_INDEXED_EQ), relation2);

        Selector left3 = new ColumnSelector(new ColumnName(tableName, "colName3"));
        Selector right3 = new StringSelector("colValue3");
        Relation relation3 = new Relation(left3, Operator.GET, right3);
        ITerm filter3 = new Filter(Collections.singleton(Operations.FILTER_NON_INDEXED_GET), relation3);

        filters.add(Arrays.asList(filter));
        filters.add(Arrays.asList(filter2, filter3));

        Disjunction disjunction = new Disjunction(Collections.singleton(Operations.FILTER_DISJUNCTION), filters);

        //Experimentation
        String result = disjunction.toString();

        //Expectations

        String expected = "DISJUNCTION - [FILTER_DISJUNCTION] - "
                +"FILTER - [FILTER_PK_EQ] - catalogName.tableName.colName = 'colValue' " +
                "OR FILTER - [FILTER_NON_INDEXED_EQ] - catalogName.tableName.colName2 = 'colValue2' "
                + "AND FILTER - [FILTER_NON_INDEXED_GET] - catalogName.tableName.colName3 >= 'colValue3'";

        Assert.assertEquals(result, expected);
    }
}
