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

package com.stratio.crossdata.common.statements.structures;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class FunctionRelationTest {

    private String functionName;
    private List<Selector> functionSelectors;
    private TableName tableName;
    private ColumnSelector columnSelector;
    private FunctionRelation functionRelation;


    @BeforeTest
    public void init() {
        functionName = "functionName";
        functionSelectors = new ArrayList<>();
        tableName = new TableName("catalog", "function");
        columnSelector = new ColumnSelector(new ColumnName(tableName, "ColunName"));
        functionSelectors.add(columnSelector);
        functionRelation = new FunctionRelation(functionName, functionSelectors, tableName);
        columnSelector.setAlias("columnAlias");
    }


    @Test
    public void testConstructor() {

        //Experimentation
        functionRelation = new FunctionRelation(functionName, functionSelectors, tableName);

        //Expectations
        assertEquals(functionRelation.getTableName(), tableName.getQualifiedName());
        assertEquals(functionRelation.getFunctionName(), functionName);
        assertEquals(functionRelation.getFunctionSelectors(), functionSelectors);
    }


    @Test
    public void testSQLString() {

        //Experimentation
        String result = functionRelation.toSQLString(false);

        //Expetations
        assertEquals(result, "functionName(catalog.function.ColunName)");

    }

    @Test
    public void testSQLStringWithAlias() {

        //Experimentation
        String result = functionRelation.toSQLString(true);

        //Expetations
        assertEquals(result, "functionName(catalog.function.ColunName AS columnAlias)");

    }

    @Test
    public void testToString() {

        //Experimentation
        String result = functionRelation.toString();

        //Expetations
        assertEquals(result, "functionName(catalog.function.ColunName AS columnAlias)");

    }

}
