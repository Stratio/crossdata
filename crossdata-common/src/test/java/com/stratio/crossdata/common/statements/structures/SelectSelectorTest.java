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

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;


public class SelectSelectorTest {


    @Test
    public void testToSQLString(){

        String selectQuery = "SELECT SELECT";
        SelectSelector selectorType = new SelectSelector(selectQuery);

        //Experimentation
        String result = selectorType.toSQLString(false);

        //Expectations
        Assert.assertEquals(result, "(SELECT SELECT)");
    }


    @Test
    public void testEquals(){

        LogicalWorkflow logicalWorkflow = mock(LogicalWorkflow.class);
        SelectSelector selectorType = new SelectSelector("SELECT SELECT");
        selectorType.setQueryWorkflow(logicalWorkflow);

        TableName tableName = mock(TableName.class);
        SelectSelector selectorType2 = new SelectSelector(tableName, "SELECT SELECT");
        selectorType2.setQueryWorkflow(logicalWorkflow);

        //Experimentation
        Boolean result = selectorType.equals(selectorType2);

        //Expectations
        Assert.assertTrue(result);
    }

    

}
