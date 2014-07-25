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

package com.stratio.meta2.core.planner.statements;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta2.core.statements.DropCatalogStatement;

import org.testng.annotations.Test;

import static org.testng.Assert.fail;

public class DropCatalogStatementTest extends BasicPlannerTest {

  //TODO Validate create catalog path

    @Test
    public void testPlanForDropKeyspace(){
      fail("Not implemented yet");
        //String inputText = "DROP KEYSPACE demo;";
        //stmt = new DropCatalogStatement("demo", false);
        //validateCassandraPath("testPlanForDropKeyspace");
    }
}
