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

package com.stratio.crossdata.core.planner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.executionplan.ExecutionType;
import com.stratio.crossdata.common.executionplan.MetadataWorkflow;
import com.stratio.crossdata.common.executionplan.QueryWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.executionplan.StorageWorkflow;
import com.stratio.crossdata.common.logicalplan.Disjunction;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.IndexType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.ListSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.query.MetadataPlannedQuery;
import com.stratio.crossdata.core.query.MetadataValidatedQuery;
import com.stratio.crossdata.core.query.StorageParsedQuery;
import com.stratio.crossdata.core.query.StoragePlannedQuery;
import com.stratio.crossdata.core.query.StorageValidatedQuery;

/**
 * Planner tests considering an initial input, generating all intermediate steps,
 * and generating a ExecutionWorkflow.
 */
public class PlannerTest extends PlannerBaseTest {

    private ConnectorMetadata connector1 = null;
    private ConnectorMetadata connector2 = null;

    private ClusterName clusterName = null;

    private TableMetadata table1 = null;
    private TableMetadata table2 = null;
    private TableMetadata table3 = null;

    DataStoreName dataStoreName = null;
    Map<ClusterName, Integer> clusterWithDefaultPriority = new LinkedHashMap<>();

    @BeforeClass(dependsOnMethods = {"setUp"})
    public void init() throws ManifestException {
        MetadataManagerTestHelper.HELPER.initHelper();
        dataStoreName = MetadataManagerTestHelper.HELPER.createTestDatastore();

        //Connector with join.
        Set<Operations> operationsC1 = new HashSet<>();
        operationsC1.add(Operations.PROJECT);
        operationsC1.add(Operations.SELECT_OPERATOR);
        operationsC1.add(Operations.SELECT_FUNCTIONS);
        operationsC1.add(Operations.SELECT_WINDOW);
        operationsC1.add(Operations.SELECT_GROUP_BY);
        operationsC1.add(Operations.FILTER_NON_INDEXED_EQ);
        operationsC1.add(Operations.DELETE_PK_EQ);
        operationsC1.add(Operations.CREATE_INDEX);
        operationsC1.add(Operations.DROP_INDEX);
        operationsC1.add(Operations.UPDATE_PK_EQ);
        operationsC1.add(Operations.TRUNCATE_TABLE);
        operationsC1.add(Operations.DROP_TABLE);
        operationsC1.add(Operations.PAGINATION);
        operationsC1.add(Operations.INSERT);
        operationsC1.add(Operations.INSERT_IF_NOT_EXISTS);
        operationsC1.add(Operations.INSERT_FROM_SELECT);
        operationsC1.add(Operations.SELECT_SUBQUERY);
        operationsC1.add(Operations.FILTER_NON_INDEXED_LET);
        operationsC1.add(Operations.SELECT_ORDER_BY);

        //Streaming connector.
        Set<Operations> operationsC2 = new HashSet<>();
        operationsC2.add(Operations.PROJECT);
        operationsC2.add(Operations.SELECT_OPERATOR);
        operationsC2.add(Operations.FILTER_PK_EQ);
        operationsC2.add(Operations.SELECT_INNER_JOIN);
        operationsC2.add(Operations.SELECT_INNER_JOIN_PARTIALS_RESULTS);
        operationsC2.add(Operations.INSERT);
        operationsC2.add(Operations.FILTER_DISJUNCTION);
        operationsC1.add(Operations.FILTER_NON_INDEXED_IN);

        String strClusterName = "TestCluster1";
        clusterWithDefaultPriority.put(new ClusterName(strClusterName), Constants.DEFAULT_PRIORITY);

        List<FunctionType> functions1 = new ArrayList<>();
        // SUM function
        FunctionType sumFunction = new FunctionType();
        sumFunction.setFunctionName("sum");
        sumFunction.setSignature("sum(Tuple[Double]):Tuple[Double]");
        sumFunction.setFunctionType("aggregation");
        sumFunction.setDescription("Total sum");
        functions1.add(sumFunction);
        // AVG function
        FunctionType avgFunction = new FunctionType();
        avgFunction.setFunctionName("avg");
        avgFunction.setSignature("avg(Tuple[Double]):Tuple[Double]");
        avgFunction.setFunctionType("aggregation");
        avgFunction.setDescription("Average");
        functions1.add(avgFunction);
        // COUNT function
        FunctionType countFunction = new FunctionType();
        countFunction.setFunctionName("count");
        countFunction.setSignature("count(Tuple[Any*]):Tuple[Int]");
        countFunction.setFunctionType("aggregation");
        countFunction.setDescription("Count");
        functions1.add(countFunction);
        
        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector1", dataStoreName,
                clusterWithDefaultPriority, operationsC1, "actorRef1", functions1);
        connector2 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector2", dataStoreName,
                clusterWithDefaultPriority, operationsC2, "actorRef2", new ArrayList<FunctionType>());

        clusterName = MetadataManagerTestHelper.HELPER.createTestCluster(strClusterName, dataStoreName, connector1.getName(), connector2.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("demo").getName();
        createTestTables(catalogName);
    }

    @AfterClass
    public void tearDown(){
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    public void createTestTables(CatalogName catalogName) {
        createTestTables(catalogName, "table1", "table2", "table3");
    }

    public void createTestTables(CatalogName catalogName, String... tableNames) {
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes1 = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        table1 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[0],
                columnNames1, columnTypes1, partitionKeys1, clusteringKeys1, null);

        String[] columnNames2 = { "id", "email" };
        ColumnType[] columnTypes2 = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        String[] partitionKeys2 = { "id" };
        String[] clusteringKeys2 = { };
        table2 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[1],
                columnNames2, columnTypes2, partitionKeys2, clusteringKeys2, null);

        String[] columnNames3 = { "id_aux", "address" };
        ColumnType[] columnTypes3 = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        String[] partitionKeys3 = { "id_aux" };
        String[] clusteringKeys3 = { };
        table3 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[2],
                columnNames3, columnTypes3, partitionKeys3, clusteringKeys3, null);
    }

    @Test
    public void selectSingleColumn() throws ManifestException {

        init();

        String inputText = "SELECT demo.table1.id FROM demo.table1 WHERE demo.table1.user = 'test';";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "selectSingleColumn", false, table1);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector1.getActorRef(), "Wrong target actor");
    }

    @Test
    public void selectWithFunction() throws ManifestException {

        init();

        String inputText = "SELECT getYear(demo.table1.id) AS getYear FROM demo.table1;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "selectWithFunction", false, table1);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector1.getActorRef(), "Wrong target actor");
    }

    @Test
    public void selectJoinMultipleColumns() throws ManifestException {

        init();

        String inputText = "SELECT demo.table1.id, demo.table1.user, demo.table2.id, demo.table2.email"
                + " FROM demo.table1"
                + " INNER JOIN demo.table2 ON demo.table1.id = demo.table2.id;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "selectJoinMultipleColumns", false, table1, table2);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector2.getActorRef(), "Wrong target actor");
    }

    @Test
    public void selectJoinMultipleColumnsDiffOnNames() throws ManifestException {

        init();

        String inputText = "SELECT demo.table1.id, demo.table1.user, demo.table3.id_aux, demo.table3.address"
                + " FROM demo.table1"
                + " INNER JOIN demo.table3 ON demo.table1.id = demo.table3.id_aux;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "selectJoinMultipleColumnsDiffOnNames", false, table1, table3);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector2.getActorRef(), "Wrong target actor");
    }

    @Test
    public void complexSubqueries() throws ManifestException {

        init();

        String inputText = "SELECT id FROM " +
                "( SELECT * FROM demo.table1 " +
                    "WHERE demo.table1.user = 18 + (SELECT demo.table2.email FROM demo.table2) * 'my comment' )"
                + " AS myTable;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "complexSubqueries", false, false, table1, table2);

        assertNotNull(queryWorkflow, "Null workflow received.");

    }

    @Test
    public void dropTable() throws ManifestException {

        init();

        String inputText = "DROP TABLE demo.table1;";

        IParsedQuery stmt = helperPT.testRegularStatement(inputText, "dropTable");

        MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery((MetadataParsedQuery) stmt);

        MetadataPlannedQuery plan = null;
        try {
            plan = planner.planQuery(metadataValidatedQuery);
        } catch (PlanningException e) {
            fail("dropTable test failed");
        }

        MetadataWorkflow metadataWorkflow = (MetadataWorkflow) plan.getExecutionWorkflow();

        assertNotNull(metadataWorkflow, "Null workflow received.");
        assertEquals(metadataWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(metadataWorkflow.getExecutionType(), ExecutionType.DROP_TABLE, "Invalid execution type");
        assertTrue("actorRef1".equalsIgnoreCase(metadataWorkflow.getActorRef()), "Actor reference is not correct");
        assertEquals(metadataWorkflow.getTableName(), new TableName("demo", "table1"), "Table name is not correct");
    }

    @Test
    public void deleteRows() throws ManifestException {

        init();

        String inputText = "DELETE FROM demo.table1 WHERE id = 3;";

        String expectedText = "DELETE FROM demo.table1 WHERE demo.table1.id = 3;";

        IParsedQuery stmt = helperPT.testRegularStatement(inputText, expectedText, "deleteRows");

        StorageValidatedQuery storageValidatedQuery = new StorageValidatedQuery((StorageParsedQuery) stmt);

        StoragePlannedQuery plan = null;
        try {
            plan = planner.planQuery(storageValidatedQuery);
        } catch (PlanningException e) {
            fail("deleteRows test failed");
        }

        StorageWorkflow storageWorkflow = (StorageWorkflow) plan.getExecutionWorkflow();

        assertNotNull(storageWorkflow, "Null workflow received.");
        assertEquals(storageWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(storageWorkflow.getExecutionType(), ExecutionType.DELETE_ROWS, "Invalid execution type");
        assertTrue("actorRef1".equalsIgnoreCase(storageWorkflow.getActorRef()), "Actor reference is not correct");
        assertEquals(storageWorkflow.getTableName(), new TableName("demo", "table1"), "Table name is not correct");

        Collection<Filter> whereClauses = new ArrayList<>();
        whereClauses.add(
                new Filter(Collections.singleton(Operations.DELETE_PK_EQ),
                        new Relation(
                                new ColumnSelector(new ColumnName("demo", "table1", "id")),
                                Operator.EQ,
                                new IntegerSelector(new TableName("demo", "table1"), 3))));

        assertEquals(storageWorkflow.getWhereClauses().size(), whereClauses.size(), "Where clauses size differs");

        assertTrue(storageWorkflow.getWhereClauses().iterator().next().toString().equalsIgnoreCase(
                        whereClauses.iterator().next().toString()),
                "Where clauses are not equal");
    }

    @Test
    public void createIndex() throws ManifestException {

        init();

        String inputText = "CREATE INDEX indexTest ON demo.table1(user);";

        String expectedText = "CREATE DEFAULT INDEX indexTest ON demo.table1(demo.table1.user);";

        IParsedQuery stmt = helperPT.testRegularStatement(inputText, expectedText, "createIndex");

        MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery((MetadataParsedQuery) stmt);

        MetadataPlannedQuery plan = null;
        try {
            plan = planner.planQuery(metadataValidatedQuery);
        } catch (PlanningException e) {
            fail("createIndex test failed");
        }

        MetadataWorkflow metadataWorkflow = (MetadataWorkflow) plan.getExecutionWorkflow();

        assertNotNull(metadataWorkflow, "Null workflow received.");
        assertEquals(metadataWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(metadataWorkflow.getExecutionType(), ExecutionType.CREATE_INDEX, "Invalid execution type");
        assertTrue("actorRef1".equalsIgnoreCase(metadataWorkflow.getActorRef()), "Actor reference is not correct");

        IndexMetadata indexMetadata = metadataWorkflow.getIndexMetadata();

        assertNotNull(metadataWorkflow, "Null workflow received.");
        assertTrue("actorRef1".equalsIgnoreCase(metadataWorkflow.getActorRef()), "Actor reference is not correct");
        assertEquals(indexMetadata.getType(), IndexType.DEFAULT, "Index types differ");
        assertEquals(indexMetadata.getName(), new IndexName("demo", "table1", "indexTest"), "Index names differ");

        Map<ColumnName, ColumnMetadata> columns = new HashMap<>();
        ColumnName columnName = new ColumnName("demo", "table1", "user");
        columns.put(columnName, new ColumnMetadata(
                columnName,
                null,
                new ColumnType(DataType.TEXT)));
        assertEquals(indexMetadata.getColumns().size(), columns.size(), "Column sizes differ");
        assertEquals(indexMetadata.getColumns().values().iterator().next().getColumnType(),
                columns.values().iterator().next().getColumnType(),
                "Column types differs");
    }

    @Test
    public void dropIndex() throws ManifestException {

        init();

        String inputText = "DROP INDEX demo.table1.indexTest;";

        String expectedText = "DROP INDEX demo.table1.index[indexTest];";

        IParsedQuery stmt = helperPT.testRegularStatement(inputText, expectedText, "dropIndex");

        MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery((MetadataParsedQuery) stmt);

        IndexName indexName = new IndexName("demo", "table1", "indexTest");
        Map<ColumnName, ColumnMetadata> cols = new HashMap<>();
        ColumnMetadata colMetadata = new ColumnMetadata(
                new ColumnName("demo", "table1", "user"),
                null,
                new ColumnType(DataType.TEXT));
        cols.put(colMetadata.getName(), colMetadata);
        IndexMetadata indexMetadata = new IndexMetadata(
                indexName,
                cols,
                IndexType.CUSTOM,
                new HashMap<Selector, Selector>());
        table1.addIndex(indexMetadata.getName(), indexMetadata);

        MetadataPlannedQuery plan = null;
        try {
            plan = planner.planQuery(metadataValidatedQuery);
        } catch (PlanningException e) {
            fail("dropIndex test failed");
        }

        MetadataWorkflow metadataWorkflow = (MetadataWorkflow) plan.getExecutionWorkflow();

        assertNotNull(metadataWorkflow, "Null workflow received.");
        assertTrue("actorRef1".equalsIgnoreCase(metadataWorkflow.getActorRef()), "Actor reference is not correct");
        assertNotNull(metadataWorkflow, "Null workflow received.");
        assertEquals(metadataWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(metadataWorkflow.getExecutionType(), ExecutionType.DROP_INDEX, "Invalid execution type");
        assertTrue("actorRef1".equalsIgnoreCase(metadataWorkflow.getActorRef()), "Actor reference is not correct");

        indexMetadata = metadataWorkflow.getIndexMetadata();

        Map<ColumnName, ColumnMetadata> columns = new HashMap<>();
        ColumnName columnName = new ColumnName("demo", "table1", "user");
        columns.put(columnName, new ColumnMetadata(
                columnName,
                null,
                new ColumnType(DataType.TEXT)));
        assertEquals(indexMetadata.getColumns().size(), columns.size(), "Column sizes differ");

        ColumnMetadata columnMetadata = indexMetadata.getColumns().values().iterator().next();
        ColumnMetadata staticColumnMetadata = columns.values().iterator().next();

        assertEquals(columnMetadata.getColumnType(), staticColumnMetadata.getColumnType(), "Column types differs");
        assertEquals(columnMetadata.getName(), staticColumnMetadata.getName(), "Column names differ");
    }

    @Test
    public void updateTable() throws ManifestException {

        init();

        String inputText = "UPDATE demo.table1 SET user = 'DataHub' WHERE id = 1;";

        String expectedText = "UPDATE demo.table1 SET demo.table1.user = 'DataHub' WHERE demo.table1.id = 1;";

        IParsedQuery stmt = helperPT.testRegularStatement(inputText, expectedText, "updateTable");

        StorageValidatedQuery storageValidatedQuery = new StorageValidatedQuery((StorageParsedQuery) stmt);

        StoragePlannedQuery plan = null;
        try {
            plan = planner.planQuery(storageValidatedQuery);
        } catch (PlanningException e) {
            fail("updateTable test failed");
        }

        StorageWorkflow storageWorkflow = (StorageWorkflow) plan.getExecutionWorkflow();

        assertNotNull(storageWorkflow, "Null workflow received.");
        assertTrue("actorRef1".equalsIgnoreCase(storageWorkflow.getActorRef()), "Actor reference is not correct");

        assertEquals(storageWorkflow.getTableName(), new TableName("demo", "table1"), "Table names differ");

        List<Relation> relations = new ArrayList<>();
        Selector leftSelector = new ColumnSelector(new ColumnName("demo", "table1", "user"));
        Selector rightTerm = new StringSelector(new TableName("demo", "table1"), "DataHub");
        relations.add(new Relation(leftSelector, Operator.ASSIGN, rightTerm));
        assertEquals(storageWorkflow.getAssignments().size(), 1, "Wrong assignments size");
        assertEquals(storageWorkflow.getAssignments().size(), relations.size(), "Assignments sizes differ");
        assertTrue(storageWorkflow.getAssignments().iterator().next().toString().equalsIgnoreCase(
                        relations.iterator().next().toString()),
                "Assignments differ");

        List<Filter> filters = new ArrayList<>();
        ColumnSelector firstSelector = new ColumnSelector(new ColumnName("demo", "table1", "id"));
        IntegerSelector secondSelector = new IntegerSelector(new TableName("demo", "table1"), 1);
        Relation relation = new Relation(firstSelector, Operator.EQ, secondSelector);
        filters.add(
                new Filter(
                        Collections.singleton(Operations.UPDATE_PK_EQ),
                        relation));
        assertEquals(storageWorkflow.getWhereClauses().size(), 1, "Wrong where clauses size");
        assertEquals(storageWorkflow.getWhereClauses().size(), filters.size(), "Where clauses sizes differ");
        assertTrue(storageWorkflow.getWhereClauses().iterator().next().toString().equalsIgnoreCase(
                        filters.iterator().next().toString()),
                "Where clauses differ");
    }

    @Test
    public void truncateTable() throws ManifestException {

        init();

        String inputText = "TRUNCATE demo.table1;";

        IParsedQuery stmt = helperPT.testRegularStatement(inputText, "truncateTable");

        StorageValidatedQuery storageValidatedQuery = new StorageValidatedQuery((StorageParsedQuery) stmt);

        StoragePlannedQuery plan = null;
        try {
            plan = planner.planQuery(storageValidatedQuery);
        } catch (PlanningException e) {
            fail("truncateTable test failed");
        }

        StorageWorkflow storageWorkflow = (StorageWorkflow) plan.getExecutionWorkflow();

        assertNotNull(storageWorkflow, "Null workflow received.");
        assertTrue("actorRef1".equalsIgnoreCase(storageWorkflow.getActorRef()), "Actor reference is not correct");

        assertEquals(storageWorkflow.getTableName(), new TableName("demo", "table1"), "Table names differ");
        assertEquals(storageWorkflow.getExecutionType(), ExecutionType.TRUNCATE_TABLE, "Execution types differ");

    }

    @Test
    public void selectGroupBy() throws ManifestException {

        init();

        String inputText =
                "SELECT demo.table1.id, shorten(demo.table1.user) AS shorten FROM demo.table1 GROUP BY demo.table1.id;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "selectGroupBy", false, table1);

        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector1.getActorRef(), "Wrong target actor");
    }

    @Test
    public void pagination() throws ManifestException {

        init();

        String inputText = "SELECT * FROM demo.table1 WHERE demo.table1.user = 'test';";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "pagination", false, table1);
        int expectedPagedSize = 5;
        assertEquals(queryWorkflow.getWorkflow().getPagination(), expectedPagedSize, "Pagination plan failed.");
    }

    @Test
    public void testJoinWithStreaming() throws ManifestException {

        init();

        String inputText = "SELECT * FROM demo.table1 WITH WINDOW 5 MINUTES " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Planner failed.");
        assertNotNull(queryWorkflow.getTriggerStep(), "Planner failed.");
        assertNotNull(queryWorkflow.getNextExecutionWorkflow(), "Planner failed.");
        assertNotNull(queryWorkflow, "Planner failed");
    }

/*
    @Test
    public void testMultipleJoin() throws ManifestException {

        init();

        String inputText = "SELECT * FROM demo.table1 " +
                "INNER JOIN demo.table2 ON demo.table1.id = demo.table2.id " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testMultipleJoin", false, table1, table2, table3);
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Planner failed.");
        assertNotNull(queryWorkflow.getTriggerStep(), "Planner failed.");
        assertNotNull(queryWorkflow.getNextExecutionWorkflow(), "Planner failed.");
        assertNotNull(queryWorkflow, "Planner failed");
    }
*/
    @Test
    public void testInsertIntoFromSelectDirect() throws ManifestException {

        init();

        String inputText = "INSERT INTO demo.table1 (demo.table1.id, demo.table1.user) SELECT * FROM demo.table2;";
        StorageWorkflow storageWorkflow = null;
        try {
            storageWorkflow = (StorageWorkflow) getPlannedStorageQuery(
                    inputText, "testInsertIntoFromSelectDirect", false);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
        assertNotNull(storageWorkflow, "Planner failed");
        assertEquals(storageWorkflow.getExecutionType(), ExecutionType.INSERT_FROM_SELECT, "Planner failed.");
        assertNotNull(storageWorkflow.getPreviousExecutionWorkflow(), "Planner failed.");
        assertNotNull(storageWorkflow.getPreviousExecutionWorkflow().getTriggerStep(), "Planner failed.");
    }

    @Test
    public void testInsertIntoFromSelectWithTwoPhases() throws ManifestException {
        MetadataManagerTestHelper.HELPER.insertDataStore("greatDatastore", "greatCluster");

        //Create Connector
        Set<Operations> greatOperations = new HashSet<>();
        greatOperations.add(Operations.PROJECT);
        greatOperations.add(Operations.SELECT_OPERATOR);
        greatOperations.add(Operations.SELECT_FUNCTIONS);
        greatOperations.add(Operations.SELECT_WINDOW);
        greatOperations.add(Operations.SELECT_GROUP_BY);
        greatOperations.add(Operations.DELETE_PK_EQ);
        greatOperations.add(Operations.CREATE_INDEX);
        greatOperations.add(Operations.DROP_INDEX);
        greatOperations.add(Operations.UPDATE_PK_EQ);
        greatOperations.add(Operations.TRUNCATE_TABLE);
        greatOperations.add(Operations.DROP_TABLE);
        greatOperations.add(Operations.PAGINATION);
        greatOperations.add(Operations.INSERT);
        greatOperations.add(Operations.INSERT_IF_NOT_EXISTS);

        String strClusterName = "greatCluster";
        clusterWithDefaultPriority.put(new ClusterName(strClusterName), Constants.DEFAULT_PRIORITY);

        ConnectorMetadata connector3 = MetadataManagerTestHelper.HELPER.createTestConnector(
                "greatConnector",
                new DataStoreName("greatDatastore"),
                clusterWithDefaultPriority,
                greatOperations,
                "greatActorRef",
                new ArrayList<FunctionType>());

        clusterName = MetadataManagerTestHelper.HELPER.createTestCluster(
                strClusterName, new DataStoreName("greatDatastore"),
                connector3.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("greatCatalog").getName();
        createTestTables(catalogName, "table4", "table5", "table6");

        // Generate query
        String inputText = "INSERT INTO greatCatalog.table4 (greatCatalog.table4.id, greatCatalog.table4.user)"
                + " SELECT * FROM greatCatalog.table5;";
        StorageWorkflow storageWorkflow = null;
        try {
            storageWorkflow = (StorageWorkflow) getPlannedStorageQuery(
                    inputText, "testInsertIntoFromSelectWithTwoPhases", false);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }

        try {
            // DETACH CLUSTER
            ClusterMetadata clusterMetadata =
                    MetadataManager.MANAGER.getCluster(clusterName);

            Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs =
                    clusterMetadata.getConnectorAttachedRefs();

            connectorAttachedRefs.remove(connector3.getName());
            clusterMetadata.setConnectorAttachedRefs(connectorAttachedRefs);

            MetadataManager.MANAGER.createCluster(clusterMetadata, false);


            ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connector3.getName());
            connectorMetadata.getClusterRefs().remove(clusterName);
            connectorMetadata.getClusterProperties().remove(clusterName);
            connectorMetadata.getClusterPriorities().remove(clusterName);

            MetadataManager.MANAGER.createConnector(connectorMetadata, false);

            // DELETE OTHER STRUCTURES
            MetadataManager.MANAGER.deleteCluster(clusterName, false);
            MetadataManager.MANAGER.deleteConnector(connector3.getName());
            MetadataManager.MANAGER.deleteCatalog(new CatalogName("greatCatalog"), false);
        } catch (Exception e) {
            fail("Test failed: " + System.lineSeparator() + e.getMessage());
        }

        assertNotNull(storageWorkflow, "Planner failed");
        assertEquals(storageWorkflow.getExecutionType(), ExecutionType.INSERT_BATCH, "Planner failed.");
        assertNotNull(storageWorkflow.getPreviousExecutionWorkflow(), "Planner failed.");
        assertNotNull(storageWorkflow.getPreviousExecutionWorkflow().getTriggerStep(), "Planner failed.");
    }

    @Test
    public void testSelectWithDisjunction() throws ManifestException {

        init();

        String[] columnNames1 = { "id", "code" };
        ColumnType[] columnTypes1 = { new ColumnType(DataType.INT), new ColumnType(DataType.INT) };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        TableMetadata table4 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, "demo", "table4",
                columnNames1, columnTypes1, partitionKeys1, clusteringKeys1, null);

        String inputText = "SELECT * FROM demo.table4 WHERE"
                + " id = code"
                + " AND id = 25"
                + " AND code = 25 OR id = 25;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testSelectWithDisjunction", false, false, table4);

        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getWorkflow().getInitialSteps().size(), 1,
                "Only one initial step was expected");
        assertEquals(queryWorkflow.getWorkflow().getInitialSteps().get(0).getClass(), Project.class,
                "First step of the workflow should be a Project");
        assertEquals(
                queryWorkflow.getWorkflow().getInitialSteps().get(0).getNextStep().getNextStep().getNextStep().getClass(),
                Disjunction.class,
                "Fourth step should be a Disjunction");
    }

    @Test
    public void testSelectWithDisjunctionAndParenthesis() throws ManifestException {

        init();

        String[] columnNames1 = { "id", "code" };
        ColumnType[] columnTypes1 = { new ColumnType(DataType.INT), new ColumnType(DataType.INT) };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        TableMetadata table4 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, "demo", "table4",
                columnNames1, columnTypes1, partitionKeys1, clusteringKeys1, null);

        String inputText = "SELECT * FROM demo.table4 WHERE"
                + " id = code"
                + " AND ((id = 25 AND code = 25) OR (code = 14 AND id = 14))"
                + " AND id = 25;";

        /*
        String inputText = "SELECT * FROM demo.table4 WHERE"
                + " id = code"
                + " AND (id = 25 OR code = 25)"
                + " AND id = 25;";
        */

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testSelectWithDisjunctionAndParenthesis", false, false, table4);

        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getWorkflow().getInitialSteps().size(), 1,
                "Only one initial step was expected");
        assertEquals(queryWorkflow.getWorkflow().getInitialSteps().get(0).getClass(), Project.class,
                "First step of the workflow should be a Project");
        assertEquals(
                queryWorkflow.getWorkflow().getInitialSteps().get(0).getNextStep().getNextStep().getClass(),
                Disjunction.class,
                "Third step should be a Disjunction");
        assertEquals(
                ((Disjunction) queryWorkflow.getWorkflow().getInitialSteps().get(0).getNextStep().getNextStep())
                        .getTerms().size(),
                2,
                "Disjunction should have 2 terms");
        assertEquals(
                ((Disjunction) queryWorkflow.getWorkflow().getInitialSteps().get(0).getNextStep().getNextStep())
                        .getTerms().get(0).size(),
                2,
                "First term of the disjunction should have 2 relations");
        assertEquals(
                ((Disjunction) queryWorkflow.getWorkflow().getInitialSteps().get(0).getNextStep().getNextStep())
                        .getTerms().get(1).size(),
                2,
                "Second term of the disjunction should have 2 relations");
    }

    @Test
    public void testSelectWithOperatorsPreference() throws ManifestException {

        init();

        String[] columnNames1 = { "id", "name", "size", "retailprice", "date" };
        ColumnType[] columnTypes1 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.NATIVE)};
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        TableMetadata table5 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, "demo", "part",
                columnNames1, columnTypes1, partitionKeys1, clusteringKeys1, null);

        String inputText = "SELECT "
                + "name, "
                + "size*retailprice, "
                + "(2*retailprice), "
                + "sum(size*(1-size)*(1+retailprice)) as sum_charge, "
                + "avg(size) as avg_size, "
                + "count(*) as count_order "
                + "FROM demo.part "
                + "WHERE "
                + "date <= '1998-12-01' - interval('1998-12-01', 3) "
                + "GROUP BY name "
                + "ORDER BY name;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testSelectWithDisjunctionAndParenthesis", false, false, table5);

        assertNotNull(queryWorkflow, "Workflow is null for testSelectWithOperatorsPreference");
        assertEquals(queryWorkflow.getWorkflow().getLastStep().getClass(), Select.class, "Last step must be a Select");
        Select finalSelect = (Select) queryWorkflow.getWorkflow().getLastStep();
        assertEquals(
                finalSelect.getTypeMap().get("name"),
                ColumnType.valueOf("TEXT"),
                "Column name must be of type text");
        assertEquals(
                finalSelect.getTypeMap().get("Column2"),
                ColumnType.valueOf("DOUBLE"),
                "Column Column2 must be of type double");
        assertEquals(
                finalSelect.getTypeMap().get("Column3"),
                ColumnType.valueOf("DOUBLE"),
                "Column Column3 must be of type double");
        assertEquals(
                finalSelect.getTypeMap().get("sum_charge"),
                ColumnType.valueOf("DOUBLE"),
                "Column sum_charge must be of type double");
        assertEquals(
                finalSelect.getTypeMap().get("avg_size"),
                ColumnType.valueOf("DOUBLE"),
                "Column avg_size must be of type double");
        assertEquals(
                finalSelect.getTypeMap().get("count_order"),
                ColumnType.valueOf("INT"),
                "Column count_order must be of type int");
    }

    @Test
    public void testSelectWithCollectionList() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                + "id, "
                + "user "
                + "FROM table1 "
                + "WHERE user IN ['admin', 'dev'];";

        QueryWorkflow queryWorkflow;
        queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testSelectWithCollectionList", false, false, table3);
        assertNotNull(queryWorkflow, "Planner failed");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Planner failed.");
        assertEquals(queryWorkflow.getWorkflow().getInitialSteps().size(), 1, "Only one initial step was expected.");
        assertEquals(queryWorkflow.getWorkflow().getInitialSteps().get(0).getNextStep().getClass(),
                Filter.class,
                "Second step should be a Filter.");
        Filter filter = (Filter) queryWorkflow.getWorkflow().getInitialSteps().get(0).getNextStep();
        assertEquals(filter.getRelation().getRightTerm().getClass(),
                ListSelector.class,
                "Right term of the relation should be a list.");
    }

}
