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

package com.stratio.crossdata.common.logicalplan;


import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.FunctionRelation;
import com.stratio.crossdata.common.statements.structures.Selector;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filter the results retrieved through a Project operation.
 */
public class FunctionFilterTest {



    @Test
    private void testConstructor(){
        Set<Operations> operations = new HashSet<>();
        operations.add(Operations.FILTER_FUNCTION);

        List<Selector> selectors = new ArrayList<>();

        FunctionRelation functionRelation = new FunctionRelation("FunctionName", selectors);

        //Experimentation
        FunctionFilter filter = new FunctionFilter(operations, functionRelation);


        //Expectations
        Assert.assertEquals(filter.getRelation(), functionRelation);
        Assert.assertEquals(filter.getOperations(), operations);

    }
}
