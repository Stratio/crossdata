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

package com.stratio.crossdata.common.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;

public class MetadataResultTest {
    @Test
    public void testMetadataResultCatalog() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_CATALOG);
        List<String> catalogList = new ArrayList<>();
        catalogList.add("catalogTest");
        result.setCatalogList(catalogList);

        Assert.assertEquals(result.toString(), "CATALOG created successfully");
    }

    @Test
    public void testMetadataResultTable() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_TABLE);
        List<TableMetadata> tableList = new ArrayList<>();
        TableMetadata tableMetadata = createTableMetadata();
        tableList.add(tableMetadata);
        result.setTableList(tableList);

        Assert.assertEquals(result.toString(), "TABLE created successfully");
    }

    @Test
    public void testMetadataResultIndex() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_CREATE_INDEX);

        List<ColumnMetadata> columnList = new ArrayList<>();
        ColumnMetadata columnMetadata = new ColumnMetadata(new ColumnName("catalogTest","tableTest", "columnTest"),
                null, ColumnType.VARCHAR);

        columnList.add(columnMetadata);

        result.setColumnList(columnList);

        Assert.assertEquals(result.toString(), "INDEX created successfully");
    }

    @Test
    public void testMetadataResultDropIndex() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_DROP_INDEX);
        Assert.assertEquals(result.toString(), "INDEX dropped successfully");
    }

    @Test
    public void testMetadataResultDropTable() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_DROP_TABLE);
        Assert.assertEquals(result.toString(), "TABLE dropped successfully");
    }

    @Test
    public void testMetadataResultDropCatalog() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_DROP_CATALOG);
        Assert.assertEquals(result.toString(), "CATALOG dropped successfully");
    }

    @Test
    public void testMetadataResultListCatalog() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_LIST_CATALOGS);
        List<String> catalogList = new ArrayList<>();
        catalogList.add("catalogTest");
        result.setCatalogList(catalogList);
        Assert.assertEquals(result.toString(), "[catalogTest]");
    }

    @Test
    public void testMetadataResultListTables() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_LIST_TABLES);
        List<TableMetadata> tableList = new ArrayList<>();
        TableMetadata tableMetadata = createTableMetadata();
        tableList.add(tableMetadata);
        result.setTableList(tableList);

        String expectedResult =
                "["+ System.lineSeparator() +
                "Table: demo.users" + System.lineSeparator() +
                "Cluster: cluster.cluster" + System.lineSeparator() +
                "Columns: " + System.lineSeparator() +
                "\t" + "demo.users.name: TEXT" + System.lineSeparator() +
                "\t" + "demo.users.gender: TEXT" + System.lineSeparator() +
                "\t" + "demo.users.age: INT" + System.lineSeparator() +
                "\t" + "demo.users.bool: BOOLEAN" + System.lineSeparator() +
                "\t" + "demo.users.phrase: TEXT" + System.lineSeparator() +
                "\t" + "demo.users.email: TEXT" + System.lineSeparator() +
                "Partition key: []" + System.lineSeparator() +
                "Cluster key: []" + System.lineSeparator() +
                "Indexes: " + System.lineSeparator() +
                "Options: " + System.lineSeparator() + "]";

        Assert.assertEquals(
                result.toString(),
                expectedResult,
                "testMetadataResultListTables failed.");
    }

    @Test
    public void testMetadataResultListColumns() throws Exception {
        MetadataResult result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_LIST_COLUMNS);
        List<ColumnMetadata> columnList = new ArrayList<>();
        ColumnMetadata columnMetadata = new ColumnMetadata(new ColumnName("catalogTest","tableTest", "columnTest"),
                null, ColumnType.BIGINT);

        columnList.add(columnMetadata);

        result.setColumnList(columnList);

        Assert.assertEquals(result.getColumnList().get(0).getColumnType().name(), "BIGINT");
    }

    private TableMetadata createTableMetadata() {
        TableName targetTable = new TableName("demo", "users");
        Map<Selector, Selector> options = new HashMap<>();
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        ClusterName clusterRef = new ClusterName("cluster");
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        Object[] parameters = { };
        columns.put(new ColumnName(new TableName("demo", "users"), "name"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users"), "name"), parameters,
                        ColumnType.TEXT));
        columns.put(new ColumnName(new TableName("demo", "users"), "gender"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users"), "gender"), parameters,
                        ColumnType.TEXT));
        columns.put(new ColumnName(new TableName("demo", "users"), "age"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users"), "age"), parameters,
                        ColumnType.INT));
        columns.put(new ColumnName(new TableName("demo", "users"), "bool"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users"), "bool"), parameters,
                        ColumnType.BOOLEAN));
        columns.put(new ColumnName(new TableName("demo", "users"), "phrase"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users"), "phrase"), parameters,
                        ColumnType.TEXT));
        columns.put(new ColumnName(new TableName("demo", "users"), "email"),
                new ColumnMetadata(new ColumnName(new TableName("demo", "users"), "email"), parameters,
                        ColumnType.TEXT));

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        TableMetadata table =
                new TableMetadata(targetTable, options, columns, indexes, clusterRef, partitionKey,
                        clusterKey);

        return table;
    }

}
