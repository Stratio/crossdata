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

import com.stratio.crossdata.common.data.TableName;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class AliasSelectorTest {

    @Test
    public void getSelectorTables() {
        StringSelector stringSelector = new StringSelector("str");
        AliasSelector aliasSelector = new AliasSelector(stringSelector);
        Assert.assertEquals(aliasSelector.getSelectorTables(), stringSelector.getSelectorTables() ,  "The tables should be the same as referenced selector tables");
    }

    @Test
    public void equalsTest(){
        AliasSelector bs=new AliasSelector(new AsteriskSelector());
        AliasSelector bs2=new AliasSelector(new AsteriskSelector());
        Assert.assertTrue(bs.equals(bs2), "This selectors must be equals");
        Assert.assertTrue(bs.hashCode()==bs2.hashCode(),"This selector must have the same hashcode");
    }

    @Test
    public void nonEqualsTest(){
        AliasSelector as=new AliasSelector(new AsteriskSelector());
        AliasSelector as2=null;
        Assert.assertFalse(as.equals(as2),"This selectors mustn't be equals");

    }

    @Test
    public void referencedSelectorTest(){
        AliasSelector as=new AliasSelector(new AsteriskSelector());
        Assert.assertTrue(AsteriskSelector.class.isInstance(as.getReferencedSelector()),
                "The referenced selector is wrong.");

    }
}
