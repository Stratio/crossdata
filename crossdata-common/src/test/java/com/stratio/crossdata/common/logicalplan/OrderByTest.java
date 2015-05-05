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
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.OrderByClause;
import com.stratio.crossdata.common.statements.structures.OrderDirection;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by lcisneros on 5/05/15.
 */
public class OrderByTest {


    @Test
    public void testToString(){
        TableName tableName = new TableName("catalogName", "Employees");
        ColumnSelector selector  = new ColumnSelector(new ColumnName(tableName, "ammount"));

        OrderByClause orderByClause= new OrderByClause();
        orderByClause.setDirection(OrderDirection.ASC);
        orderByClause.setSelector(selector);

        List<OrderByClause> orderByClauses = new ArrayList<>();
        orderByClauses.add(orderByClause);

        OrderBy orderBy = new OrderBy(Collections.singleton(Operations.SELECT_ORDER_BY), orderByClauses);

        //Experimentation
        String result = orderBy.toString();
        System.out.println(result);
        //Expectations
        Assert.assertEquals(result, "ORDER BY catalogName.Employees.ammount");
    }


    @Test
    public void testToStringWithMultipleOrder(){
        TableName tableName = new TableName("catalogName", "Employees");
        ColumnSelector selector  = new ColumnSelector(new ColumnName(tableName, "ammount"));
        ColumnSelector selector2  = new ColumnSelector(new ColumnName(tableName, "orders"));

        List<OrderByClause> orderByClauses = new ArrayList<>();
        orderByClauses.add(new OrderByClause(OrderDirection.ASC, selector));
        orderByClauses.add(new OrderByClause(OrderDirection.DESC, selector2));

        OrderBy orderBy = new OrderBy(Collections.singleton(Operations.SELECT_ORDER_BY), orderByClauses);

        //Experimentation
        String result = orderBy.toString();
        System.out.println(result);
        //Expectations
        Assert.assertEquals(result, "ORDER BY catalogName.Employees.ammount, catalogName.Employees.orders DESC");
    }
}
