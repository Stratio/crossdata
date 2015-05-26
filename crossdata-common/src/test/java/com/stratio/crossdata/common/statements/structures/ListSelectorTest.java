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
import java.util.Arrays;
import java.util.List;

import com.stratio.crossdata.common.data.ColumnName;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.TableName;

public class ListSelectorTest {

    @Test
    public void getSelectorTablesIntegerList() {
        List<Selector> selectors= Arrays.asList((Selector) new IntegerSelector(1), (Selector)new IntegerSelector(5));
        ListSelector listSelector=new ListSelector(new TableName("catalog","table"),selectors);
        Assert.assertEquals(listSelector.getSelectorTables().size(), 0 ,  "The table size should be 0");
    }

    @Test
    public void getSelectorTablesColumnList() {
        List<Selector> selectors= Arrays.asList((Selector) new ColumnSelector(new ColumnName("c", "t1", "c")), (Selector)new ColumnSelector(new ColumnName("c", "t2", "c")));
        ListSelector listSelector=new ListSelector(new TableName("catalog","table"),selectors);
        Assert.assertEquals(listSelector.getSelectorTables().size(), 2 ,  "The table size should be 2");
    }


    @Test
    public void equalsTest() {
        List<Selector> selectors= new ArrayList<>();
        IntegerSelector is1=new IntegerSelector(1);
        IntegerSelector is2=new IntegerSelector(5);
        selectors.add(is1);
        selectors.add(is2);
        ListSelector listSelector=new ListSelector(new TableName("catalog","table"),selectors);
        ListSelector listSelector2=new ListSelector(new TableName("catalog","table"),selectors);

        Assert.assertTrue(listSelector.equals(listSelector2), "This selectors must be equals");
        Assert.assertTrue(listSelector.hashCode() == listSelector2.hashCode(), "This selector must have the same hashcode");

    }

    @Test
    public void nonEqualsTest() {
        List<Selector> selectors= new ArrayList<>();
        List<Selector> selectors2= new ArrayList<>();
        IntegerSelector is1=new IntegerSelector(1);
        IntegerSelector is2=new IntegerSelector(5);
        IntegerSelector is3=new IntegerSelector(1);
        IntegerSelector is4=new IntegerSelector(7);
        selectors.add(is1);
        selectors.add(is2);
        selectors2.add(is3);
        selectors2.add(is4);
        ListSelector listSelector=new ListSelector(new TableName("catalog","table"),selectors);
        ListSelector listSelector2=new ListSelector(new TableName("catalog","table"),selectors2);

        Assert.assertFalse(listSelector.equals(listSelector2), "This selectors mustn't be equals");
        Assert.assertFalse(listSelector.hashCode() == listSelector2.hashCode(), "This selector mustn't have the same " +
                "hashcode");

    }

    @Test
    public void nonEquals2Test() {
        List<Selector> selectors= new ArrayList<>();
        List<Selector> selectors2= new ArrayList<>();
        IntegerSelector is1=new IntegerSelector(1);
        IntegerSelector is2=new IntegerSelector(5);

        selectors.add(is1);
        selectors.add(is2);

        ListSelector listSelector=new ListSelector(new TableName("catalog","table"),selectors);
        ListSelector listSelector2=null;

        Assert.assertFalse(listSelector.equals(listSelector2), "This selectors mustn't be equals");

    }
}
