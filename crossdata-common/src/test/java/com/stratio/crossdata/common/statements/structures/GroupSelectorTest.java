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

import org.testng.Assert;
import org.testng.annotations.Test;

public class GroupSelectorTest {
    @Test
    public void toSQLStringTest() {
        StringSelector is1=new StringSelector("hola");
        StringSelector is2=new StringSelector("adios");
        GroupSelector gs = new GroupSelector(is1,is2);
        GroupSelector gs2 = new GroupSelector(is1, is2);
        Assert.assertEquals(gs.toSQLString(false), "'hola' AND 'adios'", "Error in th sql string");

    }

    @Test
    public void equalsTest() {
        IntegerSelector is1=new IntegerSelector(1);
        IntegerSelector is2=new IntegerSelector(5);
        GroupSelector gs = new GroupSelector(is1,is2);
        GroupSelector gs2 = new GroupSelector(is1, is2);
        Assert.assertTrue(gs.equals(gs2), "This selectors must be equals");
        Assert.assertTrue(gs.hashCode() == gs2.hashCode(), "This selector must have the same hashcode");

    }

    @Test
    public void nonEqualsTest() {
        GroupSelector gs = new GroupSelector(new IntegerSelector(1), new IntegerSelector(5));
        GroupSelector gs2 = new GroupSelector(new IntegerSelector(1), new IntegerSelector(7));
        Assert.assertFalse(gs.equals(gs2), "This selectors mustn't be equals");
        Assert.assertFalse(gs.hashCode() == gs2.hashCode(), "This selector mustn't have the same hashcode");

    }
}
