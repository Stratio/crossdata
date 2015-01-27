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

package com.stratio.crossdata.core.normalizer;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.OrderByClause;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.SelectExpression;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.metadata.MetadataManagerTestHelper;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;
import com.stratio.crossdata.core.structures.GroupByClause;
import com.stratio.crossdata.core.structures.InnerJoin;

public class NormalizerTest extends MetadataManagerTestHelper {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(MetadataManagerTestHelper.class);

    @Test(groups = "putData")
    public void putData() throws Exception {

        // DATASTORE
        insertDataStore("Cassandra", "production");

        // CLUSTER
        ClusterName clusterName = new ClusterName("testing");
        DataStoreName dataStoreRef = new DataStoreName("Cassandra");
        Map<Selector, Selector> clusterOptions = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();

        ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, dataStoreRef, clusterOptions,
                connectorAttachedRefs);

        MetadataManager.MANAGER.createCluster(clusterMetadata);

        // CATALOG 1
        HashMap<TableName, TableMetadata> tables = new HashMap<>();

        TableName tableName = new TableName("demo", "tableClients");
        Map<Selector, Selector> options = new HashMap<>();

        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();

        ColumnMetadata columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "clientId"),
                new Object[] { },
                ColumnType.TEXT);
        columns.put(new ColumnName(tableName, "clientId"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colSales"),
                new Object[] { },
                ColumnType.INT);
        columns.put(new ColumnName(tableName, "colSales"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "gender"),
                new Object[] { },
                ColumnType.TEXT);
        columns.put(new ColumnName(tableName, "gender"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colExpenses"),
                new Object[] { },
                ColumnType.INT);
        columns.put(new ColumnName(tableName, "colExpenses"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "year"),
                new Object[] { },
                ColumnType.INT);
        columns.put(new ColumnName(tableName, "year"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colPlace"),
                new Object[] { },
                ColumnType.TEXT);
        columns.put(new ColumnName(tableName, "colPlace"), columnMetadata);

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        ClusterName clusterRef = new ClusterName("testing");

        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        partitionKey.add(new ColumnName("demo", "tableClients", "clientId"));

        LinkedList<ColumnName> clusterKey = new LinkedList<>();

        TableMetadata tableMetadata = new TableMetadata(
                tableName,
                options,
                columns,
                indexes,
                clusterRef,
                partitionKey,
                clusterKey
        );

        tables.put(new TableName("demo", "tableClients"), tableMetadata);

        CatalogMetadata catalogMetadata = new CatalogMetadata(
                new CatalogName("demo"), // name
                new HashMap<Selector, Selector>(), // options
                tables // tables
        );

        MetadataManager.MANAGER.createCatalog(catalogMetadata);

        // CATALOG 2
        tables = new HashMap<>();

        tableName = new TableName("myCatalog", "tableCostumers");
        options = new HashMap<>();

        columns = new LinkedHashMap<>();

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "assistantId"),
                new Object[] { },
                ColumnType.TEXT);
        columns.put(new ColumnName(tableName, "assistantId"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "age"),
                new Object[] { },
                ColumnType.INT);
        columns.put(new ColumnName(tableName, "age"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colFee"),
                new Object[] { },
                ColumnType.INT);
        columns.put(new ColumnName(tableName, "colFee"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colCity"),
                new Object[] { },
                ColumnType.TEXT);
        columns.put(new ColumnName(tableName, "colCity"), columnMetadata);

        indexes = new HashMap<>();
        clusterRef = new ClusterName("myCluster");
        partitionKey.clear();
        partitionKey.add(new ColumnName("myCatalog", "tableCostumers", "assistantId"));

        clusterKey = new LinkedList<>();

        tableMetadata = new TableMetadata(
                tableName,
                options,
                columns,
                indexes,
                clusterRef,
                partitionKey,
                clusterKey
        );

        tables.put(new TableName("myCatalog", "tableCostumers"), tableMetadata);

        catalogMetadata = new CatalogMetadata(
                new CatalogName("myCatalog"), // name
                new HashMap<Selector, Selector>(), // options
                tables // tables
        );

        MetadataManager.MANAGER.createCatalog(catalogMetadata);

        LOG.info("Data inserted in the MetadataManager for the NormalizedTest");
    }

    public void testSelectedParserQuery(SelectParsedQuery selectParsedQuery, String expectedText, String methodName) {
        Normalizer normalizer = new Normalizer();

        SelectValidatedQuery result = null;
        try {
            result = normalizer.normalize(selectParsedQuery);
        } catch (ValidationException e) {
            fail("Test failed: " + methodName + System.lineSeparator(), e);
        }

        assertTrue(result.toString().equalsIgnoreCase(expectedText),
                "Test failed: " + methodName + System.lineSeparator() +
                "Result:   " + result.toString() + System.lineSeparator() +
                "Expected: " + expectedText);
    }

    @Test(dependsOnGroups = "putData")
    public void testNormalizeWhereOrderGroup() throws Exception {

        String methodName = "testNormalizeWhereOrderGroup";

        String inputText = "SELECT colSales, colExpenses FROM tableClients "
                + "WHERE colCity = 'Madrid' "
                + "ORDER BY age "
                + "GROUP BY colSales, colExpenses;";

        String expectedText = "SELECT demo.tableClients.colSales, demo.tableClients.colExpenses FROM demo.tableClients "
                + "WHERE demo.tableClients.colPlace = 'Madrid' "
                + "ORDER BY [demo.tableClients.year] "
                + "GROUP BY demo.tableClients.colSales, demo.tableClients.colExpenses";

        // BASE QUERY
        BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("demo"));

        // SELECTORS
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
        selectorList.add(new ColumnSelector(new ColumnName(null, "colExpenses")));

        SelectExpression selectExpression = new SelectExpression(selectorList);

        // SELECT STATEMENT
        SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));

        // WHERE CLAUSES
        List<Relation> where = new ArrayList<>();
        where.add(new Relation(new ColumnSelector(new ColumnName(null, "colPlace")), Operator.EQ,
                new StringSelector("Madrid")));
        selectStatement.setWhere(where);

        // ORDER BY
        List<Selector> selectorListOrder = new ArrayList<>();
        selectorListOrder.add(new ColumnSelector(new ColumnName(null, "year")));
        OrderByClause orderBy = new OrderByClause(new ColumnSelector(new ColumnName(null, "year")));
        List<OrderByClause> orderByClauses = new ArrayList<>();
        orderByClauses.add(orderBy);
        selectStatement.setOrderByClauses(orderByClauses);

        // GROUP BY
        List<Selector> groupBy = new ArrayList<>();
        groupBy.add(new ColumnSelector(new ColumnName(null, "colSales")));
        groupBy.add(new ColumnSelector(new ColumnName(null, "colExpenses")));
        selectStatement.setGroupByClause(new GroupByClause(groupBy));

        SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        testSelectedParserQuery(selectParsedQuery, expectedText, methodName);
    }

    @Test(dependsOnGroups = "putData")
    public void testNormalizeInnerJoin() throws Exception {

        String methodName = "testNormalizeInnerJoin";

        String inputText =
                "SELECT colSales, colFee FROM tableClients "
                        + "INNER JOIN tableCostumers ON assistantId = clientId "
                        + "WHERE colCity = 'Madrid' "
                        + "ORDER BY age "
                        + "GROUP BY colSales, colFee;";

        String expectedText =
                "SELECT demo.tableClients.colSales, myCatalog.tableCostumers.colFee FROM demo.tableClients "
                        + "INNER JOIN myCatalog.tableCostumers ON myCatalog.tableCostumers.assistantId = demo.tableClients.clientId "
                        + "WHERE myCatalog.tableCostumers.colCity = 'Madrid' "
                        + "ORDER BY [myCatalog.tableCostumers.age] "
                        + "GROUP BY demo.tableClients.colSales, myCatalog.tableCostumers.colFee";

        // BASE QUERY
        BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("demo"));

        // SELECTORS
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
        selectorList.add(new ColumnSelector(new ColumnName(null, "colFee")));

        SelectExpression selectExpression = new SelectExpression(selectorList);

        // SELECT STATEMENT
        SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));

        List<Relation> joinRelations = new ArrayList<>();
        Relation relation = new Relation(
                new ColumnSelector(new ColumnName(null, "assistantId")),
                Operator.EQ,
                new ColumnSelector(new ColumnName(null, "clientId")));
        joinRelations.add(relation);
        InnerJoin innerJoin = new InnerJoin(new TableName("myCatalog", "tableCostumers"), joinRelations);
        selectStatement.setJoin(innerJoin);

        // WHERE CLAUSES
        List<Relation> where = new ArrayList<>();
        where.add(new Relation(new ColumnSelector(new ColumnName(null, "colCity")), Operator.EQ,
                new StringSelector("Madrid")));
        selectStatement.setWhere(where);

        // ORDER BY
        List<Selector> selectorListOrder = new ArrayList<>();
        selectorListOrder.add(new ColumnSelector(new ColumnName(null, "age")));
        OrderByClause orderBy = new OrderByClause(new ColumnSelector(new ColumnName(null, "age")));
        List<OrderByClause> orderByClauses = new ArrayList<>();
        orderByClauses.add(orderBy);
        selectStatement.setOrderByClauses(orderByClauses);

        // GROUP BY
        List<Selector> groupBy = new ArrayList<>();
        groupBy.add(new ColumnSelector(new ColumnName(null, "colSales")));
        groupBy.add(new ColumnSelector(new ColumnName(null, "colFee")));
        selectStatement.setGroupByClause(new GroupByClause(groupBy));

        SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        testSelectedParserQuery(selectParsedQuery, expectedText, methodName);

    }

}
