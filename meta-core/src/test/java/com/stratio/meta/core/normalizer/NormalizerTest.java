/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.core.normalizer;

import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.IMetadata;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.SelectExpression;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.common.statements.structures.selectors.StringSelector;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.NormalizedQuery;
import com.stratio.meta2.core.query.SelectParsedQuery;
import com.stratio.meta2.core.statements.SelectStatement;
import com.stratio.meta2.core.structures.OrderBy;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class NormalizerTest {

  // TODO: SetUpBeforeClass for mocking MetaDataManager

  @BeforeMethod
  public void setUp() throws Exception {

    // METADATAMANAGER
    Map<FirstLevelName, IMetadata> md = new HashMap<>();
    DataStoreMetadata dsmd = new DataStoreMetadata(
        new DataStoreName("Cassandra"), //name
        "1.0.0", //version
        new HashSet<String>(), //requiredProperties
        new HashSet<String>() //othersProperties
        );
    md.put(new DataStoreName("Cassandra"), dsmd);
    MetadataManager.MANAGER.init(md, new ReentrantLock());

    // CLUSTER
    ClusterName clusterName = new ClusterName("testing");
    DataStoreName dataStoreRef = new DataStoreName("Cassandra");
    Map<String, Object> clusterOptions = new HashMap<>();
    Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = new HashMap<>();

    ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, dataStoreRef, clusterOptions, connectorAttachedRefs);

    MetadataManager.MANAGER.createCluster(clusterMetadata);

    // CATALOG 1
    HashMap<TableName, TableMetadata> tables = new HashMap<>();

    TableName tableName = new TableName("demo", "tableClients");
    Map<String, Object> options = new HashMap<>();

    Map<ColumnName, ColumnMetadata> columns = new HashMap<>();

    ColumnMetadata columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "clientId"),
        new Object[]{},
        ColumnType.TEXT);
    columns.put(new ColumnName(tableName, "clientId"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "colSales"),
        new Object[]{},
        ColumnType.INT);
    columns.put(new ColumnName(tableName, "colSales"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "gender"),
        new Object[]{},
        ColumnType.TEXT);
    columns.put(new ColumnName(tableName, "gender"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "colExpenses"),
        new Object[]{},
        ColumnType.INT);
    columns.put(new ColumnName(tableName, "colExpenses"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "year"),
        new Object[]{},
        ColumnType.INT);
    columns.put(new ColumnName(tableName, "year"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "colPlace"),
        new Object[]{},
        ColumnType.TEXT);
    columns.put(new ColumnName(tableName, "colPlace"), columnMetadata);

    Map<IndexName, IndexMetadata > indexes = new HashMap<>();
    ClusterName clusterRef = new ClusterName("testing");
    List<ColumnName> partitionKey = Collections.singletonList(new ColumnName("demo", "tableClients", "clientId"));
    List<ColumnName> clusterKey = new ArrayList<>();

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
        new HashMap<String, Object>(), // options
        tables // tables
    );

    MetadataManager.MANAGER.createCatalog(catalogMetadata);

    // CATALOG 2
    tables = new HashMap<>();

    tableName = new TableName("myCatalog", "tableCostumers");
    options = new HashMap<>();

    columns = new HashMap<>();

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "assistantId"),
        new Object[]{},
        ColumnType.TEXT);
    columns.put(new ColumnName(tableName, "assistantId"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "age"),
        new Object[]{},
        ColumnType.INT);
    columns.put(new ColumnName(tableName, "age"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "colFee"),
        new Object[]{},
        ColumnType.INT);
    columns.put(new ColumnName(tableName, "colFee"), columnMetadata);

    columnMetadata = new ColumnMetadata(
        new ColumnName(tableName, "colCity"),
        new Object[]{},
        ColumnType.TEXT);
    columns.put(new ColumnName(tableName, "colCity"), columnMetadata);

    indexes = new HashMap<>();
    clusterRef = new ClusterName("myCluster");
    partitionKey = Collections.singletonList(new ColumnName("myCatalog", "tableCostumers", "assistantId"));
    clusterKey = new ArrayList<>();

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
        new HashMap<String, Object>(), // options
        tables // tables
    );

    MetadataManager.MANAGER.createCatalog(catalogMetadata);

  }

  @AfterMethod
  public void tearDown() throws Exception {
    // TODO: Drop DataStore
    // TODO: Drop Cluster
    // TODO: Drop Catalog
  }

  public void testSelectedParserQuery(SelectParsedQuery selectParsedQuery, String expectedText, String methodName){
    Normalizer normalizer = new Normalizer();

    NormalizedQuery result = null;
    try {
      result = normalizer.normalize(selectParsedQuery);
    } catch (ValidationException e) {
      fail("Test failed: " + methodName + System.lineSeparator(), e);
    }

    assertTrue(result.toString().equalsIgnoreCase(expectedText),
               "Test failed: "+ methodName + System.lineSeparator() +
               "Result:   " + result.toString() + System.lineSeparator() +
               "Expected: " + expectedText);
  }

  @Test
  public void testNormalizeWhereOrderGroup() throws Exception {

    String methodName = "testNormalizeWhereOrderGroup";

    String inputText = "SELECT colSales, colExpenses FROM tableClients "
                       + "WHERE colCity = 'Madrid' "
                       + "ORDER BY age "
                       + "GROUP BY gender;";

    String expectedText = "SELECT myCatalog.tableClients.colSales, myCatalog.tableClients.colExpenses FROM myCatalog.tableClients "
                          + "WHERE myCatalog.tableClients.colPlace = 'Madrid' "
                          + "ORDER BY myCatalog.tableClients.year "
                          + "GROUP BY myCatalog.tableClients.gender;";

    // BASE QUERY
    BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("myCatalog"));

    // SELECTORS
    List<Selector> selectorList = new ArrayList<>();
    selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
    selectorList.add(new ColumnSelector(new ColumnName(null, "colRevenues")));

    SelectExpression selectExpression = new SelectExpression(selectorList);

    // SELECT STATEMENT
    SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("myCatalog", "tableClients"));

    // WHERE CLAUSES
    List<Relation> where = new ArrayList<>();
    where.add(new Relation(new ColumnSelector(new ColumnName(null, "colCity")), Operator.ASSIGN, new StringSelector("Madrid")));
    selectStatement.setWhere(where);

    // ORDER BY
    List<Selector> selectorListOrder = new ArrayList<>();
    selectorListOrder.add(new ColumnSelector(new ColumnName(null, "age")));
    OrderBy orderBy = new OrderBy(selectorListOrder);
    selectStatement.setOrderBy(orderBy);

    // GROUP BY
    List<Selector> groupBy = new ArrayList<>();
    groupBy.add(new ColumnSelector(new ColumnName(null, "gender")));
    selectStatement.setGroupBy(new GroupBy(groupBy));

    SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

    testSelectedParserQuery(selectParsedQuery, expectedText, methodName);
  }

  @Test
  public void testNormalizeInnerJoin() throws Exception {

    String methodName = "testNormalizeInnerJoin";

    String inputText =
        "SELECT colSales, colRevenues FROM tableClients "
        + "INNER JOIN tableCostumers ON assistantId = clientId "
        + "WHERE colCity = 'Madrid' "
        + "ORDER BY age "
        + "GROUP BY gender;";

    String expectedText = "SELECT demo.tableClients.colSales, myCatalog.tableCostumers.january.colRevenues FROM demo.tableClients "
                          + "INNER JOIN myCatalog.tableCostumers ON myCatalog.tableCostumers.assistantId = demo.tableClients.clientId "
                          + "WHERE myCatalog.tableCostumers.colCity = 'Madrid' "
                          + "ORDER BY myCatalog.tableCostumers.age "
                          + "GROUP BY demo.tableClients.gender;";


    // BASE QUERY
    BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName("demo"));

    // SELECTORS
    List<Selector> selectorList = new ArrayList<>();
    selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
    selectorList.add(new ColumnSelector(new ColumnName(null, "colRevenues")));

    SelectExpression selectExpression = new SelectExpression(selectorList);

    // SELECT STATEMENT
    SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName("demo", "tableClients"));

    List<Relation> joinRelations = new ArrayList<>();
    Relation relation = new Relation(
        new ColumnSelector(new ColumnName(null, "assistandId")),
        Operator.ASSIGN,
        new ColumnSelector(new ColumnName(null, "clientId")));
    joinRelations.add(relation);
    InnerJoin innerJoin = new InnerJoin(new TableName(null, "tableCostumers"), joinRelations);
    selectStatement.setJoin(innerJoin);

    // WHERE CLAUSES
    List<Relation> where = new ArrayList<>();
    where.add(new Relation(new ColumnSelector(new ColumnName(null, "colCity")), Operator.ASSIGN, new StringSelector("Madrid")));
    selectStatement.setWhere(where);

    // ORDER BY
    List<Selector> selectorListOrder = new ArrayList<>();
    selectorListOrder.add(new ColumnSelector(new ColumnName(null, "age")));
    OrderBy orderBy = new OrderBy(selectorListOrder);
    selectStatement.setOrderBy(orderBy);

    // GROUP BY
    List<Selector> groupBy = new ArrayList<>();
    groupBy.add(new ColumnSelector(new ColumnName(null, "gender")));
    selectStatement.setGroupBy(new GroupBy(groupBy));

    SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

    testSelectedParserQuery(selectParsedQuery, expectedText, methodName);

  }

}
