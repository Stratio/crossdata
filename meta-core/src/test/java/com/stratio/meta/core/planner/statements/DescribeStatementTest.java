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

package com.stratio.meta.core.planner.statements;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.structures.DescribeType;
import org.testng.annotations.Test;

public class DescribeStatementTest extends BasicPlannerTest {

    @Test
    public void testPlanForDescribeKeyspace(){
        String input = "DESCRIBE KEYSPACE demo;";
        stmt = new DescribeStatement(DescribeType.KEYSPACE);
        ((DescribeStatement) stmt).setKeyspace("demo");
        validateCommandPath("testPlanForDescribeKeyspace");
    }

    @Test
    public void testPlanForDescribeTablename(){
        String input = "DESCRIBE TABLENAME demo.users;";
        stmt = new DescribeStatement(DescribeType.TABLE);
        ((DescribeStatement) stmt).setTableName("demo.users");
        validateCommandPath("testPlanForDescribeTablename");
    }

}
