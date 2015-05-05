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

package com.stratio.crossdata.common.statements.structures;

import com.stratio.crossdata.common.data.ColumnName;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by lcisneros on 5/05/15.
 */
public class OrderByClauseTest {


    @Test
    public void testEquals(){

        Selector column = new ColumnSelector(new ColumnName("a", "b", "c"));
        Selector column1 = new ColumnSelector(new ColumnName("a", "b", "c"));
        OrderByClause orderByClause = new OrderByClause(OrderDirection.ASC, column);
        OrderByClause orderByClause1 = new OrderByClause(OrderDirection.ASC, column1);

        Assert.assertTrue(orderByClause.equals(orderByClause1));
        Assert.assertEquals(orderByClause.hashCode(), orderByClause1.hashCode());
    }


    @Test
    public void testToSQLStringASC(){

        Selector column = new ColumnSelector(new ColumnName("a", "b", "c"));
        OrderByClause orderByClause = new OrderByClause(OrderDirection.ASC, column);

        Assert.assertEquals(orderByClause.toSQLString(true), "a.b.c");
    }

    @Test
    public void testToSQLStringDESC(){

        Selector column = new ColumnSelector(new ColumnName("a", "b", "c"));
        OrderByClause orderByClause = new OrderByClause(OrderDirection.DESC, column);

        Assert.assertEquals(orderByClause.toSQLString(true), "a.b.c DESC");
    }
}
