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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ColumnName;

public class CaseWhenSelectorTest {

    @Test
    public void getSelectorTables() {
        ColumnSelector columnSelector=new ColumnSelector(new ColumnName("c","t1","c"));
        StringSelector stringSelector=new StringSelector("columnValue");
        StringSelector caseValue = new StringSelector("caseValue");
        StringSelector defaultValue = new StringSelector("elseValue");

        CaseWhenSelector cws = createBasicCaseWhenSelector(columnSelector,Operator.EQ,stringSelector, caseValue, defaultValue);
        Assert.assertEquals(cws.getSelectorTables().size(), 1 ,  "The table size should be 1");
    }

    @Test
    public void getSelectorTablesMultipleTables() {
        ColumnSelector columnSelectorT1=new ColumnSelector(new ColumnName("c","t1","c"));
        ColumnSelector columnSelectorT2=new ColumnSelector(new ColumnName("c","t2","c"));
        StringSelector caseValue = new StringSelector("caseValue");
        ColumnSelector columnSelectorT3=new ColumnSelector(new ColumnName("c","t3","c"));

        CaseWhenSelector cws = createBasicCaseWhenSelector(columnSelectorT1,Operator.EQ,columnSelectorT2, caseValue, columnSelectorT3);
        Assert.assertEquals(cws.getSelectorTables().size(), 3 ,  "The table size should be 3");
    }


    private CaseWhenSelector createBasicCaseWhenSelector( Selector leftTerm, Operator op, Selector rightTerm, Selector value, Selector defaultValue ){
        List<Pair<List<AbstractRelation>, Selector>> restrictions=new ArrayList<>();
        AbstractRelation ar=new Relation(leftTerm,op,rightTerm);
        ArrayList list=new ArrayList();
        list.add(ar);
        Pair pair=new ImmutablePair(list,value);

        restrictions.add(pair);
        CaseWhenSelector cws=new CaseWhenSelector(restrictions);
        cws.setDefaultValue(defaultValue);
        return cws;
    }


    @Test
    public void toSqlStringTest(){

        List<Pair<List<AbstractRelation>, Selector>> restrictions=new ArrayList<>();
        ColumnSelector columnSelector=new ColumnSelector(new ColumnName("catalog","table","column"));
        StringSelector stringSelector=new StringSelector("columnValue");
        AbstractRelation ar=new Relation(columnSelector,Operator.EQ,stringSelector);

        StringSelector ss=new StringSelector("hola");

        ArrayList list=new ArrayList();
        list.add(ar);
        Pair pair=new ImmutablePair(list,ss);

        restrictions.add(pair);

        CaseWhenSelector cws=new CaseWhenSelector(restrictions);
        cws.setDefaultValue(new StringSelector("adios"));
        Assert.assertEquals(cws.toSQLString(false),"CASE WHEN catalog.table.column = 'columnValue' THEN 'hola' ELSE 'adios' END", "Error in the expected String");

    }

    @Test
    public void testEquals(){
        List<Pair<List<AbstractRelation>, Selector>> restrictions=new ArrayList<>();
        ColumnSelector columnSelector=new ColumnSelector(new ColumnName("catalog","table","column"));
        StringSelector stringSelector=new StringSelector("columnValue");
        AbstractRelation ar=new Relation(columnSelector,Operator.EQ,stringSelector);

        StringSelector ss=new StringSelector("hola");

        ArrayList list=new ArrayList();
        list.add(ar);
        Pair pair=new ImmutablePair(list,ss);

        restrictions.add(pair);

        CaseWhenSelector cws=new CaseWhenSelector(restrictions);
        CaseWhenSelector cws2=new CaseWhenSelector(restrictions);
        cws.setDefaultValue(new StringSelector("adios"));
        cws2.setDefaultValue(new StringSelector("adios"));
        Assert.assertTrue(cws.equals(cws2),"The selectors must be equals");
        Assert.assertTrue(cws.hashCode()==cws2.hashCode(),"The selectors must be equals");

    }

    @Test
    public void testNonEquals(){
        List<Pair<List<AbstractRelation>, Selector>> restrictions=new ArrayList<>();
        ColumnSelector columnSelector=new ColumnSelector(new ColumnName("catalog","table","column"));
        StringSelector stringSelector=new StringSelector("columnValue");
        AbstractRelation ar=new Relation(columnSelector,Operator.EQ,stringSelector);

        StringSelector ss=new StringSelector("hola");

        ArrayList list=new ArrayList();
        list.add(ar);
        Pair pair=new ImmutablePair(list,ss);

        restrictions.add(pair);

        CaseWhenSelector cws=new CaseWhenSelector(restrictions);
        CaseWhenSelector cws2=null;
        cws.setDefaultValue(new StringSelector("adios"));

        Assert.assertFalse(cws.equals(cws2),"The selectors mustn't be equals");


    }

}
