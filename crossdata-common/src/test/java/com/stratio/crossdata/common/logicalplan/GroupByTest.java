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

public class GroupByTest {

    @Test
    public void testToString(){

        TableName tableName = new TableName("catalogName", "tableName");
        ColumnSelector column = new ColumnSelector(new ColumnName(tableName, "colName"));

        List<Selector> selectors = new ArrayList<>();
        selectors.add(column);

        GroupBy groupBy = new GroupBy(Collections.singleton(Operations.SELECT_GROUP_BY), selectors);
        //Experimentation
        String result = groupBy.toString();

        //Expectations
        Assert.assertEquals(result, "GROUP BY catalogName.tableName.colName");
    }

    @Test
    public void testToStringMultipleFields(){

        TableName tableName = new TableName("catalogName", "Employees");
        ColumnSelector column = new ColumnSelector(new ColumnName(tableName, "LastName"));

        TableName tableName2 = new TableName("catalogName", "Shippers");
        ColumnSelector column2 = new ColumnSelector(new ColumnName(tableName, "ShipperName"));

        List<Selector> selectors = new ArrayList<>();
        selectors.add(column);
        selectors.add(column2);

        GroupBy groupBy = new GroupBy(Collections.singleton(Operations.SELECT_GROUP_BY), selectors);
        //Experimentation
        String result = groupBy.toString();

        //Expectations
        Assert.assertEquals(result, "GROUP BY catalogName.Employees.LastName, catalogName.Employees.ShipperName");
    }

    @Test
    public void testToStringWithHavving(){


        TableName tableName = new TableName("catalogName", "users");
        Selector column = new ColumnSelector(new ColumnName(tableName, "userId"));

        TableName tableHaving = new TableName("catalogName", "orders");
        Selector columnHaving = new ColumnSelector(new ColumnName(tableHaving, "ordersId"));

        List<Selector> selectors = new ArrayList<>();
        selectors.add(column);

        SelectExpression se = new SelectExpression(Arrays.asList(columnHaving));
        Selector left = new FunctionSelector("COUNT", se);
        Selector right = new IntegerSelector(5);
        Relation relation = new Relation(left, Operator.GT, right);

        List<AbstractRelation> havingIds = new ArrayList<>();
        havingIds.add(relation);

        GroupBy groupBy = new GroupBy(Collections.singleton(Operations.SELECT_GROUP_BY), selectors);
        groupBy.setHavingIds(havingIds);
        //Experimentation
        String result = groupBy.toString();

        //Expectations
        Assert.assertEquals(result, "GROUP BY catalogName.users.userId HAVING COUNT(catalogName.orders.ordersId) AS COUNT > 5");
    }


    @Test
    public void testToStringWithMultipleHavving(){


        TableName tableName = new TableName("catalogName", "users");
        Selector column = new ColumnSelector(new ColumnName(tableName, "userId"));
        List<Selector> selectors = Arrays.asList(column);

        TableName tableHaving = new TableName("catalogName", "orders");
        Selector columnHaving = new ColumnSelector(new ColumnName(tableHaving, "ordersId"));
        Selector columnHaving2 = new ColumnSelector(new ColumnName(tableHaving, "amount"));

        //Selector left = new FunctionSelector("COUNT", Arrays.asList(columnHaving));
        SelectExpression se1 = new SelectExpression(Arrays.asList(columnHaving));
        Selector left = new FunctionSelector("COUNT", se1);
        Relation relation = new Relation(left, Operator.GT, new IntegerSelector(5));

        SelectExpression se2 = new SelectExpression(Arrays.asList(columnHaving2));
        left = new FunctionSelector("SUM", se2);
        Relation relation2 = new Relation(left, Operator.GET, new IntegerSelector(10000));

        List<AbstractRelation> havingIds = new ArrayList<>();
        havingIds.add(relation);
        havingIds.add(relation2);

        GroupBy groupBy = new GroupBy(Collections.singleton(Operations.SELECT_GROUP_BY), selectors, havingIds);

        //Experimentation
        String result = groupBy.toString();

        //Expectations
        Assert.assertEquals(result, "GROUP BY catalogName.users.userId HAVING COUNT(catalogName.orders.ordersId) AS COUNT > 5 AND SUM(catalogName.orders.amount) AS SUM >= 10000");
    }
}
