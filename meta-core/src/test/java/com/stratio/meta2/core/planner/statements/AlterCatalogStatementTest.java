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
