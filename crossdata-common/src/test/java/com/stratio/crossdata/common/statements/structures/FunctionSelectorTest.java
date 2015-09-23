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

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ColumnName;

public class FunctionSelectorTest {
    @Test
    public void toStringTest(){
        AsteriskSelector as = new AsteriskSelector();
        List<Selector> functionSelectors=new ArrayList<>();
        functionSelectors.add(as);

        FunctionSelector fs = new FunctionSelector("count", new SelectExpression(functionSelectors));

        Assert.assertEquals(fs.getFunctionName(), "count", "Bad function name");
        Assert.assertTrue(fs.hadAsteriskSelector(), "The selector must have an asterisk selector");

        Assert.assertEquals(fs.toStringWithoutAlias(),"count(*)", "Error in the string of the SQL query.");
    }

    @Test
    public void equalsTest(){
        AsteriskSelector as = new AsteriskSelector();
        List<Selector> functionSelectors=new ArrayList<>();
        functionSelectors.add(as);
        FunctionSelector fs1 = new FunctionSelector("count", new SelectExpression(functionSelectors));
        FunctionSelector fs2 = new FunctionSelector("count", new SelectExpression(functionSelectors));

        Assert.assertTrue(fs1.equals(fs2),"This selectors must be equals");
        Assert.assertTrue(fs1.hashCode()==fs2.hashCode(),"This selector must have the same hashcode");
    }

    @Test
    public void nonEqualsTest(){
        AsteriskSelector as = new AsteriskSelector();
        List<Selector> functionSelectors=new ArrayList<>();
        functionSelectors.add(as);
        FunctionSelector fs1 = new FunctionSelector("count", new SelectExpression(functionSelectors));
        FunctionSelector fs2 = new FunctionSelector("now", new SelectExpression(functionSelectors));

        Assert.assertFalse(fs1.equals(fs2),"This selectors mustn't be equals");
        Assert.assertFalse(fs1.hashCode()==fs2.hashCode(),"This selector mustn't have the same hashcode");
    }
    @Test
    public void nonEquals2Test(){
        AsteriskSelector as = new AsteriskSelector();
        List<Selector> functionSelectors=new ArrayList<>();
        functionSelectors.add(as);
        FunctionSelector fs1 = new FunctionSelector("count", new SelectExpression(functionSelectors));
        FunctionSelector fs2 = new FunctionSelector("count", SelectExpression.create(new ArrayList<ColumnName>()));

        Assert.assertFalse(fs1.equals(fs2),"This selectors mustn't be equals");
        Assert.assertFalse(fs1.hashCode()==fs2.hashCode(),"This selector mustn't have the same hashcode");
    }
}
