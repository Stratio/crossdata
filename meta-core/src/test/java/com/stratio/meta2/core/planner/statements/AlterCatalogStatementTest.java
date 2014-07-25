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
import com.stratio.meta2.core.statements.AlterCatalogStatement;
import com.stratio.meta.core.structures.IdentifierProperty;
import com.stratio.meta.core.structures.ValueProperty;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.fail;

public class AlterCatalogStatementTest extends BasicPlannerTest {

  //TODO Validate alter catalog path

    @Test
    public void planForAlterKeyspace(){
      fail("Not implemented yet");
        //String inputText = "ALTER KEYSPACE demo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};";

        //Map<String, ValueProperty> properties = new HashMap();
        //properties.put("REPLICATION", new IdentifierProperty("{'class': 'SimpleStrategy', 'replication_factor': 1}"));

        //stmt = new AlterCatalogStatement("demo", properties);

        //validateCassandraPath("planForAlterKeyspace");
    }
}
