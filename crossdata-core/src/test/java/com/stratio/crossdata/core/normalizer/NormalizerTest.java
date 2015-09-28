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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.stratio.crossdata.common.exceptions.validation.BadFormatException;
import com.stratio.crossdata.core.structures.Join;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.AbstractRelation;
import com.stratio.crossdata.common.statements.structures.AsteriskSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.OrderByClause;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.SelectExpression;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;
import com.stratio.crossdata.core.structures.GroupByClause;

public class NormalizerTest {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(MetadataManagerTestHelper.class);

    @BeforeClass
    public void setUp() throws ManifestException {
        MetadataManagerTestHelper.HELPER.initHelper();
        MetadataManagerTestHelper.HELPER.createTestEnvironment();
    }

    @AfterClass
    public void tearDown() throws Exception {
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    @Test
    public void insertData() throws Exception {

        // DATASTORE
        MetadataManagerTestHelper.HELPER.insertDataStore("Cassandra", "production");

        // CLUSTER
        ClusterName clusterName = new ClusterName("testing");
        DataStoreName dataStoreRef = new DataStoreName("Cassandra");
        Map<Selector, Selector> clusterOptions = new HashMap<>();
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();

        ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, dataStoreRef, clusterOptions,
                connectorAttachedRefs);

        MetadataManager.MANAGER.createCluster(clusterMetadata, false);

        // CATALOG 1
        HashMap<TableName, TableMetadata> tables = new HashMap<>();

        TableName tableName = new TableName("demo", "tableClients");
        Map<Selector, Selector> options = new HashMap<>();

        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();

        ColumnMetadata columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "clientId"),
                new Object[] { },
                new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(tableName, "clientId"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colSales"),
                new Object[] { },
                new ColumnType(DataType.INT));
        columns.put(new ColumnName(tableName, "colSales"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "gender"),
                new Object[] { },
                new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(tableName, "gender"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colExpenses"),
                new Object[] { },
                new ColumnType(DataType.INT));
        columns.put(new ColumnName(tableName, "colExpenses"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "year"),
                new Object[] { },
                new ColumnType(DataType.INT));
        columns.put(new ColumnName(tableName, "year"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colPlace"),
                new Object[] { },
                new ColumnType(DataType.TEXT));
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

        MetadataManager.MANAGER.createCatalog(catalogMetadata, false);

        assertTrue(MetadataManager.MANAGER.exists(catalogMetadata.getName()), System.lineSeparator() +
                "Catalog: " + catalogMetadata.getName() + " not found in the Metadata Manager");

        // CATALOG 2
        tables = new HashMap<>();

        tableName = new TableName("myCatalog", "tableCostumers");
        options = new HashMap<>();

        columns = new LinkedHashMap<>();

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "assistantId"),
                new Object[] { },
                new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(tableName, "assistantId"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "age"),
                new Object[] { },
                new ColumnType(DataType.INT));
        columns.put(new ColumnName(tableName, "age"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colFee"),
                new Object[] { },
                new ColumnType(DataType.INT));
        columns.put(new ColumnName(tableName, "colFee"), columnMetadata);

        columnMetadata = new ColumnMetadata(
                new ColumnName(tableName, "colCity"),
                new Object[] { },
                new ColumnType(DataType.TEXT));
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

        MetadataManager.MANAGER.createCatalog(catalogMetadata, false);

        LOG.info("Data inserted in the MetadataManager for the NormalizedTest");

        assertTrue(MetadataManager.MANAGER.exists(catalogMetadata.getName()), System.lineSeparator() +
                "Catalog: " + catalogMetadata.getName() + " not found in the Metadata Manager");
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

    @Test
    public void testNormalizeWhereOrderGroup() throws Exception {

        insertData();

        String methodName = "testNormalizeWhereOrderGroup";

        String inputText = "SELECT colSales, colExpenses FROM tableClients "
                + "WHERE colCity = 'Madrid' "
                + "GROUP BY colSales, colExpenses "
                + "ORDER BY age;";

        String expectedText = "SELECT demo.tableClients.colSales AS colSales, "
                + "demo.tableClients.colExpenses AS colExpenses "
                + "FROM demo.tableClients "
                + "WHERE demo.tableClients.colPlace = 'Madrid' "
                + "GROUP BY demo.tableClients.colSales, demo.tableClients.colExpenses "
                + "ORDER BY demo.tableClients.year";

        // BASE QUERY
        BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("demo"),"sessionTest");

        // SELECTORS
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
        selectorList.add(new ColumnSelector(new ColumnName(null, "colExpenses")));

        SelectExpression selectExpression = new SelectExpression(selectorList);

        // SELECT STATEMENT
        SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));

        // WHERE CLAUSES
        List<AbstractRelation> where = new ArrayList<>();
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

        Normalizer normalizer = new Normalizer();

        SelectValidatedQuery result = null;
        try {
            result = normalizer.normalize(selectParsedQuery);
        } catch (ValidationException e) {
            fail("Test failed: " + methodName + System.lineSeparator(), e);
        }

        assertEquals(
                result.toString(),
                expectedText,
                "Test failed: " + methodName);
    }

    @Test
    public void testNormalizeInnerJoin() throws Exception {

        insertData();

        String methodName = "testNormalizeInnerJoin";

        String inputText =
                "SELECT colSales, colFee FROM tableClients "
                        + "INNER JOIN tableCostumers ON assistantId = clientId "
                        + "WHERE colCity = 'Madrid' "
                        + "GROUP BY colSales, colFee "
                        + "ORDER BY age;";

        String expectedText =
                "SELECT demo.tableClients.colSales AS colSales, myCatalog.tableCostumers.colFee AS colFee "
                        + "FROM demo.tableClients "
                        + "INNER JOIN myCatalog.tableCostumers ON myCatalog.tableCostumers.assistantId = demo.tableClients.clientId "
                        + "WHERE myCatalog.tableCostumers.colCity = 'Madrid' "
                        + "GROUP BY demo.tableClients.colSales, myCatalog.tableCostumers.colFee "
                        + "ORDER BY myCatalog.tableCostumers.age";

        // BASE QUERY
        BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("demo"),"sessionTest");

        // SELECTORS
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
        selectorList.add(new ColumnSelector(new ColumnName(null, "colFee")));

        SelectExpression selectExpression = new SelectExpression(selectorList);

        // SELECT STATEMENT
        SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));

        List<AbstractRelation> joinRelations = new ArrayList<>();
        Relation relation = new Relation(
                new ColumnSelector(new ColumnName(null, "assistantId")),
                Operator.EQ,
                new ColumnSelector(new ColumnName(null, "clientId")));
        joinRelations.add(relation);

        List<TableName> tables = new ArrayList<>();
        tables.add(new TableName("demo", "tableClients"));
        tables.add(new TableName("myCatalog", "tableCostumers"));
        Join join = new Join(tables, joinRelations);
        selectStatement.addJoin(join);


        // WHERE CLAUSES
        List<AbstractRelation> where = new ArrayList<>();
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

        Normalizer normalizer = new Normalizer();

        SelectValidatedQuery result = null;
        try {
            result = normalizer.normalize(selectParsedQuery);
        } catch (ValidationException e) {
            fail("Test failed: " + methodName + System.lineSeparator(), e);
        }

        assertEquals(
                result.toString(),
                expectedText,
                "Test failed: ");

    }



    @SuppressWarnings("PMD.JUnitTestShouldIncludeAssert")
    @Test(expectedExceptions = BadFormatException.class)
    public void testNormalizeWrongInnerJoin() throws Exception {

        insertData();

        String inputText =
                        "SELECT colSales FROM tableClients "
                                        + "INNER JOIN tableClients ON assistantId = clientId ";

        // BASE QUERY
        BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("demo"),"sessionTest");

        // SELECTORS
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));

        SelectExpression selectExpression = new SelectExpression(selectorList);

        // SELECT STATEMENT
        SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));

        List<AbstractRelation> joinRelations = new ArrayList<>();
        Relation relation = new Relation(
                        new ColumnSelector(new ColumnName(new TableName("myCatalog","tableCostumers"), "assistantId")),
                        Operator.EQ,
                        new ColumnSelector(new ColumnName(null, "clientId")));
        joinRelations.add(relation);

        List<TableName> tables = new ArrayList<>();
        tables.add(new TableName("demo", "tableClients"));
        Join join = new Join(tables, joinRelations);
        selectStatement.addJoin(join);


        SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        //VALIDATE THE QUERY
        Normalizer normalizer = new Normalizer();
        normalizer.normalize(selectParsedQuery);


    }

    @SuppressWarnings("PMD.JUnitTestShouldIncludeAssert")
    @Test(expectedExceptions = ValidationException.class)
    public void testNormalizeWrongBasicSelect() throws Exception {

        insertData();

        String inputText =
                        "SELECT myCatalog.tableCostumers.colFee FROM demo.tableClients";

        // BASE QUERY
        BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("demo"),"sessionTest");

        // SELECTORS
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
        selectorList.add(new ColumnSelector(new ColumnName(new TableName("myCatalog","tableCostumers"), "colFee")));

        SelectExpression selectExpression = new SelectExpression(selectorList);

        // SELECT STATEMENT
        SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));

        SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        //VALIDATE THE QUERY
        Normalizer normalizer = new Normalizer();
        normalizer.normalize(selectParsedQuery);


    }



    @Test
    public void testNormalizeSubquery() throws Exception {

        insertData();

        String methodName = "testNormalizeSubquery";

        String inputText = "SELECT * FROM  "
                        + "( SELECT colsales, 1 FROM tableClients ) AS t";

        String virtualTableQN = Constants.VIRTUAL_NAME +".t";

        String expectedText = "SELECT "+virtualTableQN+".colSales AS colSales, "+virtualTableQN+".1 FROM ( SELECT "
                + "demo.tableClients.colSales AS colSales, 1 FROM demo.tableClients ) AS t";

        // BASE QUERY
        BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("Constants.VIRTUAL_NAME"),"sessionTest");

        // SELECTORS
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
        selectorList.add(new IntegerSelector(1));
        SelectExpression selectExpression = new SelectExpression(selectorList);

        // SELECT STATEMENT
        SelectStatement subqueryStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));


        //SELECT
        List<Selector> selectorList2 = new ArrayList<>();
        selectorList2.add(new AsteriskSelector(new TableName(Constants.VIRTUAL_NAME,"t")));
        SelectExpression selectExpression2 = new SelectExpression(selectorList2);
        SelectStatement selectStatement = new SelectStatement(selectExpression2,new TableName(Constants.VIRTUAL_NAME,"t"));
        selectStatement.setSubquery(subqueryStatement,"t");

        SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        Normalizer normalizer = new Normalizer();

        SelectValidatedQuery result = null;
        try {
            result = normalizer.normalize(selectParsedQuery);
        } catch (ValidationException e) {
            fail("Test failed: " + methodName + System.lineSeparator(), e);
        }

        assertEquals(
                result.toString(),
                expectedText,
                "Test failed: testNormalizeSubquery.");

    }

}
