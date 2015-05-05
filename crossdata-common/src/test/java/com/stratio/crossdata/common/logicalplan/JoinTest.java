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
import com.stratio.crossdata.common.data.JoinType;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.*;
import org.junit.Test;
import org.testng.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * Created by lcisneros on 4/05/15.
 */
public class JoinTest {

    @Test
    public void testToString(){

        Join join = new Join(Collections.singleton(Operations.SELECT_INNER_JOIN), "MyJoin");

        TableName tableName = new TableName("catalogName", "Employees");
        TableName tableName2 = new TableName("catalogName", "Orders");

        join.addSourceIdentifier(tableName.getQualifiedName());
        join.addSourceIdentifier(tableName2.getQualifiedName());

        Selector left = new ColumnSelector(new ColumnName(tableName, "EmployeeID"));
        Selector right = new ColumnSelector(new ColumnName(tableName, "EmployeeID"));
        Relation relation = new Relation(left, Operator.EQ, right);
        join.setType(JoinType.INNER);

        join.addJoinRelation(relation);

        //Experimentation
        String result = join.toString();

        //Expectations
        Assert.assertEquals(result,"JOIN ([catalogName.Employees, catalogName.Orders]) ON [catalogName.Employees.EmployeeID = catalogName.Employees.EmployeeID]");
    }
}
