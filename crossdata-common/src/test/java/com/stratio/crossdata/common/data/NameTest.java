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
package com.stratio.crossdata.common.data;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NameTest {

    @Test
    public void catalogNameEqualsTest() {
        Name name1 = new CatalogName("catalog");
        Name name2 = new CatalogName("catalog");
        Name name3 = new CatalogName("catalog2");
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
    }

    @Test
    public void tableNameEqualsTest() {
        Name name1 = new TableName("catalog", "table");
        Name name2 = new TableName("catalog", "table");
        Name name3 = new TableName("catalog", "table2");
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
    }



    @Test
    public void columnNameEqualsTest() {
        Name name1 = new ColumnName("catalog", "table", "column");
        Name name2 = new ColumnName("catalog", "table", "column");
        Name name3 = new ColumnName("catalog", "table", "column2");
        Name name4 = new ColumnName("catalog", "table2", "column");
        Name name5 = new ColumnName("catalog2", "table", "column");
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
        Assert.assertFalse(name1.equals(name4));
        Assert.assertFalse(name1.equals(name5));
    }



    @Test
    public void clusterNameTest() {
        Name name1 = new ClusterName("cluster");
        Name name2 = new ClusterName("cluster");
        Name name3 = new ClusterName("cluster2");
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
    }

    @Test
    public void connectorNameTest(){
        Name name1 = new ConnectorName("connector");
        Name name2 = new ConnectorName("connector");
        Name name3 = new ConnectorName("connector3");
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
    }

    @Test
    public void datastoreNameTest(){
        Name name1 = new DataStoreName("ds");
        Name name2 = new DataStoreName("ds");
        Name name3 = new DataStoreName("ds3");
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
    }

    @Test
    public void indexNameTest(){
        Name name1 = new IndexName(new ColumnName("catalog","table","column"));
        Name name2 = new IndexName(new ColumnName("catalog","table","column"));
        Name name3 = new IndexName(new ColumnName("catalog","table","column2"));
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
    }

    @Test
    public void indexName2Test(){
        Name name1 = new IndexName(new TableName("catalog","table"),"column");
        Name name2 = new IndexName(new ColumnName("catalog","table","column"));
        Assert.assertTrue(name1.equals(name2));

    }



    @Test
    public void nodeNameTest(){
        Name name1 = new NodeName("n1");
        Name name2 = new NodeName("n1");
        Name name3 = new NodeName("n3");
        Assert.assertTrue(name1.equals(name2));
        Assert.assertFalse(name1.equals(name3));
    }

    

}
