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
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import com.stratio.crossdata.common.data.AlterOperation;
import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.executionplan.ExecutionPath;
import com.stratio.crossdata.common.executionplan.ExecutionType;
import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.executionplan.MetadataWorkflow;
import com.stratio.crossdata.common.executionplan.StorageWorkflow;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.Join;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.logicalplan.UnionStep;
import com.stratio.crossdata.common.logicalplan.Window;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.common.statements.structures.window.WindowType;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.query.MetadataValidatedQuery;
import com.stratio.crossdata.core.query.StorageParsedQuery;
import com.stratio.crossdata.core.query.StorageValidatedQuery;
import com.stratio.crossdata.core.statements.AlterCatalogStatement;
import com.stratio.crossdata.core.statements.AlterTableStatement;
import com.stratio.crossdata.core.statements.DropCatalogStatement;
import com.stratio.crossdata.core.statements.InsertIntoStatement;
import com.stratio.crossdata.core.statements.StorageStatement;

/**
 * Planner test concerning Execution workflow creation.
 */
public class PlannerExecutionWorkflowTest extends PlannerBaseTest {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(PlannerExecutionWorkflowTest.class);

    private PlannerWrapper plannerWrapper = new PlannerWrapper();

    private ConnectorMetadata connector1 = null;
    private ConnectorMetadata connector2 = null;

    private ClusterName clusterName = null;

    private TableMetadata table1 = null;
    private TableMetadata table2 = null;

    @BeforeClass(dependsOnMethods = {"setUp"})
    public void init() throws ManifestException {
        DataStoreName dataStoreName = MetadataManagerTestHelper.HELPER.createTestDatastore();

        //Connector with join.
        Set<Operations> operationsC1 = new HashSet<>();
        operationsC1.add(Operations.PROJECT);
        operationsC1.add(Operations.SELECT_OPERATOR);
        operationsC1.add(Operations.FILTER_PK_EQ);
        operationsC1.add(Operations.SELECT_INNER_JOIN);
        operationsC1.add(Operations.SELECT_INNER_JOIN_PARTIALS_RESULTS);
        operationsC1.add(Operations.ALTER_TABLE);

        //Streaming connector.
        Set<Operations> operationsC2 = new HashSet<>();
        operationsC2.add(Operations.PROJECT);
        operationsC2.add(Operations.SELECT_OPERATOR);
        operationsC2.add(Operations.SELECT_WINDOW);

        String strClusterName = "TestCluster1";


        Map<ClusterName, Integer> clusterWithDefaultPriority = new LinkedHashMap<>();
        clusterWithDefaultPriority.put(new ClusterName(strClusterName), Constants.DEFAULT_PRIORITY);

        Map<ClusterName, Integer> clusterWithTopPriority = new LinkedHashMap<>();
        clusterWithTopPriority.put(new ClusterName(strClusterName), 1);

        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector1", dataStoreName, clusterWithDefaultPriority, operationsC1,
                        "actorRef1");
        connector2 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector2", dataStoreName, clusterWithDefaultPriority, operationsC2,
                        "actorRef2");


        clusterName = MetadataManagerTestHelper.HELPER.createTestCluster(strClusterName, dataStoreName, connector1.getName());
        MetadataManagerTestHelper.HELPER.createTestCatalog("demo");
        MetadataManagerTestHelper.HELPER.createTestCatalog("demo2");
        createTestTables();
    }

    /**
     * Create a test Project operator.
     *
     * @param tableName Name of the table.
     * @param columns   List of columns.
     * @return A {@link com.stratio.crossdata.common.logicalplan.Project}.
     */
    public Project getProject(String tableName, ColumnName... columns) {
        Operations operation = Operations.PROJECT;
        Project project = new Project(operation, new TableName("demo", tableName), new ClusterName("TestCluster1"));
        for (ColumnName cn : columns) {
            project.addColumn(cn);
        }
        return project;
    }

    public Filter getFilter(Operations operation, ColumnName left, Operator operator, Selector right) {
        Relation relation = new Relation(new ColumnSelector(left), operator, right);
        Filter filter = new Filter(operation, relation);
        return filter;
    }

    public Select getSelect(ColumnName[] columns, ColumnType[] types) {
        Operations operation = Operations.SELECT_OPERATOR;
        Map<Selector, String> columnMap = new LinkedHashMap<>();
        Map<String, ColumnType> typeMap = new LinkedHashMap<>();
        Map<Selector, ColumnType> typeMapFromColumnName = new LinkedHashMap<>();

        for (int index = 0; index < columns.length; index++) {
            ColumnSelector cs = new ColumnSelector(columns[index]);
            columnMap.put(cs, columns[index].getName());
            typeMapFromColumnName.put(cs, types[index]);
            typeMap.put(columns[index].getName(), types[index]);
        }
        Select select = new Select(operation, columnMap, typeMap, typeMapFromColumnName);
        return select;
    }

    private ColumnName[] getColumnNames(TableMetadata table) {
        return table.getColumns().keySet().toArray(new ColumnName[table.getColumns().size()]);
    }

    public Join getJoin(String joinId, Relation... relations) {
        Join j = new Join(Operations.SELECT_INNER_JOIN, joinId);
        for (Relation r : relations) {
            j.addJoinRelation(r);
        }
        return j;
    }



    public void createTestTables() {
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes1 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        table1 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, "demo", "table1", columnNames1, columnTypes1,
                partitionKeys1, clusteringKeys1, null);

        String[] columnNames2 = { "id", "email" };
        ColumnType[] columnTypes2 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys2 = { "id" };
        String[] clusteringKeys2 = { };
        table2 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, "demo", "table2", columnNames2, columnTypes2,
                partitionKeys2, clusteringKeys2, null);

        String[] columnNames3 = { "id", "email" };
        ColumnType[] columnTypes3 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys3 = { "id" };
        String[] clusteringKeys3 = { };
        table2 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, "demo", "table3", columnNames3, columnTypes3,
                partitionKeys3, clusteringKeys3, null);
    }

    /**
     * Simple workflow consisting on Project -> Select
     */
    @Test
    public void projectSelect() {
        // Build Logical WORKFLOW
        // Create initial steps (Projects)
        List<LogicalStep> initialSteps = new LinkedList<>();
        Project project = getProject("table1");

        ColumnName[] columns = { new ColumnName(table1.getName(), "id"), new ColumnName(table1.getName(), "user") };
        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns, types);

        //Link the elements
        project.setNextStep(select);
        initialSteps.add(project);

        // Add initial steps
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //TEST
        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = planner.buildExecutionWorkflow("qid", workflow);
        } catch (PlanningException e) {
            LOG.error("connectorChoice test failed", e);
        }
        assertExecutionWorkflow(executionWorkflow, 1, new String[] { connector1.getActorRef().toString() });
    }

    @Test
    public void projectFilterSelect() {
        // Build Logical WORKFLOW
        // Create initial steps (Projects)
        List<LogicalStep> initialSteps = new LinkedList<>();
        Project project = getProject("table1");

        ColumnName[] columns = { new ColumnName(table1.getName(), "id"), new ColumnName(table1.getName(), "user") };
        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns[0], Operator.EQ,
                new IntegerSelector(table1.getName(), 42));

        //Link the elements
        project.setNextStep(filter);
        filter.setNextStep(select);
        initialSteps.add(project);

        // Add initial steps
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //TEST

        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = planner.buildExecutionWorkflow("qid", workflow);
        } catch (PlanningException e) {
            LOG.error("connectorChoice test failed", e);
        }
        assertExecutionWorkflow(executionWorkflow, 1, new String[] { connector1.getActorRef().toString() });

    }

    //
    // Internal methods.
    //

    @Test
    public void defineExecutionPath() {
        List<LogicalStep> initialSteps = new LinkedList<>();
        Project project = getProject("table1");

        ColumnName[] columns = { new ColumnName(table1.getName(), "id"), new ColumnName(table1.getName(), "user") };
        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns[0], Operator.EQ,
                new IntegerSelector(table1.getName(), 42));

        //Link the elements
        project.setNextStep(filter);
        filter.setNextStep(select);
        initialSteps.add(project);

        List<ConnectorMetadata> availableConnectors = new ArrayList<>();
        availableConnectors.add(connector1);
        availableConnectors.add(connector2);

        ExecutionPath path = null;
        try {
            path = plannerWrapper.defineExecutionPath(project, availableConnectors);
        } catch (PlanningException e) {
            fail("Not expecting Planning Exception", e);
        }

        assertEquals(path.getInitial(), project, "Invalid initial step");
        assertEquals(path.getLast(), select, "Invalid last step");

        assertEquals(path.getAvailableConnectors().size(), 1, "Invalid size");
        assertEquals(path.getAvailableConnectors().get(0), connector1, "Invalid connector selected");
    }

    @Test
    public void defineExecutionSelectPathNotAvailable() {
        List<LogicalStep> initialSteps = new LinkedList<>();
        Project project = getProject("table1");

        ColumnName[] columns = { new ColumnName(table1.getName(), "id"), new ColumnName(table1.getName(), "user") };
        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns[0], Operator.EQ,
                new IntegerSelector(table1.getName(), 42));

        //Link the elements
        project.setNextStep(filter);
        filter.setNextStep(select);
        initialSteps.add(project);

        List<ConnectorMetadata> availableConnectors = new ArrayList<>();
        availableConnectors.add(connector2);

        try {
            ExecutionPath path = plannerWrapper.defineExecutionPath(project, availableConnectors);
            fail("Planning exception expected");
        } catch (PlanningException e) {
            assertNotNull(e, "Expecting Planning exception");
        }

    }

    @Test
    public void mergeExecutionPathsSimpleQuery() {
        List<LogicalStep> initialSteps = new LinkedList<>();
        Project project = getProject("table1");

        ColumnName[] columns = { new ColumnName(table1.getName(), "id"), new ColumnName(table1.getName(), "user") };
        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns[0], Operator.EQ,
                new IntegerSelector(table1.getName(), 42));

        //Link the elements
        project.setNextStep(filter);
        filter.setNextStep(select);
        initialSteps.add(project);

        List<ConnectorMetadata> availableConnectors = new ArrayList<>();
        availableConnectors.add(connector1);
        ExecutionPath path = new ExecutionPath(project, select, availableConnectors);

        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = plannerWrapper.mergeExecutionPaths(
                    "qid", Arrays.asList(path),
                    new HashMap<UnionStep, Set<ExecutionPath>>());
        } catch (PlanningException e) {
            fail("Not expecting Planning Exception", e);
        }

        assertNotNull(executionWorkflow, "Null execution workflow received");
        assertExecutionWorkflow(executionWorkflow, 1,
                new String[] { connector1.getActorRef().toString() });

    }

    @Test
    public void mergeExecutionPathsJoin() {

        ColumnName[] columns1 = getColumnNames(table1);
        ColumnName[] columns2 = getColumnNames(table2);

        Project project1 = getProject("table1", columns1);
        Project project2 = getProject("table2", columns2);

        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns1, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns1[0], Operator.EQ,
                new IntegerSelector(table1.getName(), 42));

        Join join = getJoin("joinId");

        //Link the elements
        project1.setNextStep(filter);
        filter.setNextStep(join);
        project2.setNextStep(join);
        join.setNextStep(select);

        List<ConnectorMetadata> availableConnectors = new ArrayList<>();
        availableConnectors.add(connector1);
        availableConnectors.add(connector2);

        ExecutionPath path1 = new ExecutionPath(project1, filter, availableConnectors);
        ExecutionPath path2 = new ExecutionPath(project2, project2, availableConnectors);

        HashMap<UnionStep, Set<ExecutionPath>> unions = new HashMap<>();
        Set<ExecutionPath> paths = new HashSet<>();
        paths.add(path1);
        paths.add(path2);
        unions.put(join, paths);

        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = plannerWrapper.mergeExecutionPaths(
                    "qid", new ArrayList<>(paths),
                    unions);
        } catch (PlanningException e) {
            fail("Not expecting Planning Exception", e);
        }

        assertNotNull(executionWorkflow, "Null execution workflow received");
        assertExecutionWorkflow(executionWorkflow, 1,
                new String[] { connector1.getActorRef().toString() });

    }

    @Test
    public void mergeExecutionPathsJoinException() {

        ColumnName[] columns1 = getColumnNames(table1);
        ColumnName[] columns2 = getColumnNames(table2);

        Project project1 = getProject("table1", columns1);
        Project project2 = getProject("table2", columns2);

        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns1, types);

        Join join = getJoin("joinId");

        //Link the elements
        project1.setNextStep(join);
        project2.setNextStep(join);
        join.setNextStep(select);

        List<ConnectorMetadata> availableConnectors = new ArrayList<>();
        availableConnectors.add(connector2);

        ExecutionPath path1 = new ExecutionPath(project1, project1, availableConnectors);
        ExecutionPath path2 = new ExecutionPath(project2, project2, availableConnectors);

        HashMap<UnionStep, Set<ExecutionPath>> unions = new HashMap<>();
        Set<ExecutionPath> paths = new HashSet<>();
        paths.add(path1);
        paths.add(path2);
        unions.put(join, paths);

        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = plannerWrapper.mergeExecutionPaths("qid", new ArrayList<>(paths), unions);
            fail("Expecting planning exception");
        } catch (PlanningException e) {
            assertNotNull(e, "Expecting Planning exception");
        }

    }

    @Test
    public void mergeExecutionPathsPartialJoin() {

        ColumnName[] columns1 = getColumnNames(table1);
        ColumnName[] columns2 = getColumnNames(table2);

        Project project1 = getProject("table1", columns1);
        Project project2 = getProject("table2", columns2);

        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns1, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns1[0], Operator.EQ,
                new IntegerSelector(table1.getName(), 42));

        Relation r = new Relation(new ColumnSelector(columns1[0]), Operator.EQ,
                new ColumnSelector(columns2[0]));

        Join join = getJoin("joinId", r);

        //Link the elements
        project1.setNextStep(filter);
        filter.setNextStep(join);
        project2.setNextStep(join);
        join.setNextStep(select);

        List<ConnectorMetadata> availableConnectors1 = new ArrayList<>();
        availableConnectors1.add(connector1);

        List<ConnectorMetadata> availableConnectors2 = new ArrayList<>();
        availableConnectors2.add(connector2);

        ExecutionPath path1 = new ExecutionPath(project1, filter, availableConnectors1);
        ExecutionPath path2 = new ExecutionPath(project2, project2, availableConnectors2);

        HashMap<UnionStep, Set<ExecutionPath>> unions = new HashMap<>();
        Set<ExecutionPath> paths = new HashSet<>();
        paths.add(path1);
        paths.add(path2);
        unions.put(join, paths);

        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = plannerWrapper.mergeExecutionPaths(
                    "qid", new ArrayList<>(paths),
                    unions);
        } catch (PlanningException e) {
            fail("Not expecting Planning Exception", e);
        }

        assertNotNull(executionWorkflow, "Null execution workflow received");
        assertExecutionWorkflow(executionWorkflow, 2,
                new String[] { connector2.getActorRef().toString(), connector1.getActorRef().toString() });

    }

    @Test
    public void mergeExecutionPathsPartialStreamingJoin() {

        ColumnName[] columns1 = getColumnNames(table1);
        ColumnName[] columns2 = getColumnNames(table2);

        Project project1 = getProject("table1", columns1);
        Project project2 = getProject("table2", columns2);

        ColumnType[] types = { ColumnType.INT, ColumnType.TEXT };
        Select select = getSelect(columns1, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns1[0], Operator.EQ,
                new IntegerSelector(table1.getName(), 42));

        Relation r = new Relation(new ColumnSelector(columns1[0]), Operator.EQ,
                new ColumnSelector(columns2[0]));

        Window streamingWindow = new Window(Operations.SELECT_WINDOW, WindowType.NUM_ROWS);
        streamingWindow.setNumRows(10);

        Join join = getJoin("joinId", r);

        //Link the elements
        project1.setNextStep(filter);
        filter.setNextStep(join);
        project2.setNextStep(streamingWindow);
        streamingWindow.setNextStep(join);
        join.setNextStep(select);

        List<ConnectorMetadata> availableConnectors1 = new ArrayList<>();
        availableConnectors1.add(connector1);

        List<ConnectorMetadata> availableConnectors2 = new ArrayList<>();
        availableConnectors2.add(connector2);

        ExecutionPath path1 = new ExecutionPath(project1, filter, availableConnectors1);
        ExecutionPath path2 = new ExecutionPath(project2, streamingWindow, availableConnectors2);

        HashMap<UnionStep, Set<ExecutionPath>> unions = new HashMap<>();
        Set<ExecutionPath> paths = new HashSet<>();
        paths.add(path1);
        paths.add(path2);
        unions.put(join, paths);

        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = plannerWrapper.mergeExecutionPaths(
                    "qid", new ArrayList<>(paths),
                    unions);
        } catch (PlanningException e) {
            fail("Not expecting Planning Exception", e);
        }

        assertNotNull(executionWorkflow, "Null execution workflow received");
        assertExecutionWorkflow(executionWorkflow, 2,
                new String[] { connector2.getActorRef().toString(), connector1.getActorRef().toString() });

    }

    @Test
    public void storageWorkflowTest() {
        DataStoreName dataStoreName = MetadataManagerTestHelper.HELPER.createTestDatastore();
        Set<Operations> operations = new HashSet<>();
        operations.add(Operations.INSERT);
        operations.add(Operations.INSERT_IF_NOT_EXISTS);
        ConnectorMetadata connectorMetadata = null;

        String strClusterName = "cluster";
        Map<ClusterName, Integer> clusterWithDefaultPriority = new LinkedHashMap<>();
        clusterWithDefaultPriority.put(new ClusterName(strClusterName), Constants.DEFAULT_PRIORITY);

        try {
            connectorMetadata = MetadataManagerTestHelper.HELPER.createTestConnector("cassandraConnector", dataStoreName,
                            clusterWithDefaultPriority, operations, "ActorRefTest");
        } catch (ManifestException e) {
            fail();
        }
        try {
            MetadataManagerTestHelper.HELPER.createTestCluster(strClusterName, dataStoreName, connectorMetadata.getName());
        } catch (ManifestException e) {
            fail();
        }

        String[] columnNames = { "name", "gender", "age", "bool", "phrase", "email" };
        ColumnType[] columnTypes = { ColumnType.TEXT, ColumnType.TEXT, ColumnType.INT, ColumnType.BOOLEAN,
                ColumnType.TEXT,
                ColumnType.TEXT };
        String[] partitionKeys = { "name", "age" };
        String[] clusteringKeys = { "gender" };
        MetadataManagerTestHelper.HELPER.createTestTable(new ClusterName("cluster"), "demo", "users", columnNames, columnTypes,
                partitionKeys, clusteringKeys, null);
        String query = "Insert into demo.users(name,gender,age,bool,phrase,email) values ('pepe','male',23,true,'this is the phrase','mail@mail.com';";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();

        TableName tableName = new TableName("demo", "users");

        columns.add(new ColumnName(tableName, "name"));
        columns.add(new ColumnName(tableName, "gender"));
        columns.add(new ColumnName(tableName, "age"));
        columns.add(new ColumnName(tableName, "bool"));
        columns.add(new ColumnName(tableName, "phrase"));
        columns.add(new ColumnName(tableName, "email"));

        values.add(new StringSelector(tableName, "'pepe'"));
        values.add(new StringSelector(tableName, "'male'"));
        values.add(new IntegerSelector(tableName, 23));
        values.add(new BooleanSelector(tableName, true));
        values.add(new StringSelector(tableName, "'this is the phrase'"));
        values.add(new StringSelector(tableName, "'mail@mail.com'"));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("demo", "users"), columns,
                null, values, true, null, null, InsertIntoStatement.TYPE_VALUES_CLAUSE);

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"));

        StorageParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
        StorageValidatedQuery storageValidatedQuery = new StorageValidatedQuery(parsedQuery);

        Planner planner = new Planner();
        try {
            ExecutionWorkflow storageWorkflow = planner.buildExecutionWorkflow(storageValidatedQuery);
            Assert.assertEquals(((StorageWorkflow) storageWorkflow).getClusterName().getName(), "cluster");
            Assert.assertEquals(((StorageWorkflow) storageWorkflow).getTableMetadata().getName().getName(), "users");
        } catch (PlanningException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test(dependsOnMethods = { "alterCatalogWorkflowTest" })
    public void dropCatalogWorkflowTest() {

        DropCatalogStatement dropCatalogStatement = new DropCatalogStatement(new CatalogName("demo2"), true);
        String query = "Drop Catalog demo2;";
        BaseQuery baseQuery = new BaseQuery("dropId", query, new CatalogName("demo2"));
        MetadataParsedQuery metadataParsedQuery = new MetadataParsedQuery(baseQuery, dropCatalogStatement);
        MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

        Planner planner = new Planner();
        try {
            ExecutionWorkflow metadataWorkflow = planner.buildExecutionWorkflow(metadataValidatedQuery);
            Assert.assertFalse(MetadataManager.MANAGER.exists(new CatalogName("demo2")));
        } catch (PlanningException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void alterCatalogWorkflowTest() {
        String options = "{comment:'the new comment'}";
        AlterCatalogStatement alterCatalogStatement = new AlterCatalogStatement(new CatalogName("demo2"), options);
        String query = "ALTER CATALOG demo2 WITH {comment:'the new comment'};";
        BaseQuery baseQuery = new BaseQuery("alterId", query, new CatalogName("demo2"));
        MetadataParsedQuery metadataParsedQuery = new MetadataParsedQuery(baseQuery, alterCatalogStatement);
        MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

        Planner planner = new Planner();
        try {
            ExecutionWorkflow metadataWorkflow = planner.buildExecutionWorkflow(metadataValidatedQuery);
            Assert.assertTrue(MetadataManager.MANAGER.exists(new CatalogName("demo2")));
            CatalogMetadata catalogMetadata = MetadataManager.MANAGER.getCatalog(new CatalogName("demo2"));
            Assert.assertEquals(catalogMetadata.getOptions(), alterCatalogStatement.getOptions());
        } catch (PlanningException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void alterTableWorkflowTest() {
        Object[] parameters = { };
        ColumnMetadata columnMetadata = new ColumnMetadata(new ColumnName(new TableName("demo", "table3"),
                "email"), parameters, ColumnType.VARCHAR);
        AlterOptions alterOptions = new AlterOptions(AlterOperation.ALTER_COLUMN, null, columnMetadata);

        AlterTableStatement alterTableStatement = new AlterTableStatement(columnMetadata.getName().getTableName(),
                columnMetadata.getName(), ColumnType.VARCHAR, null, AlterOperation.ALTER_COLUMN);
        String query = "ALTER TABLE demo.table3 ALTER table3.email TYPE varchar;";
        BaseQuery baseQuery = new BaseQuery("alterTableId", query, new CatalogName("demo"));
        MetadataParsedQuery metadataParsedQuery = new MetadataParsedQuery(baseQuery, alterTableStatement);
        MetadataValidatedQuery metadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery);

        Planner planner = new Planner();
        try {
            ExecutionWorkflow metadataWorkflow = planner.buildExecutionWorkflow(metadataValidatedQuery);
            Assert.assertTrue(MetadataManager.MANAGER.exists(new ColumnName("demo", "table3", "email")));
            ColumnMetadata columnMetadataModified = MetadataManager.MANAGER.getColumn(new ColumnName("demo", "table3",
                    "email"));
            Assert.assertEquals(metadataWorkflow.getExecutionType(), ExecutionType.ALTER_TABLE);

            Assert.assertEquals(((MetadataWorkflow)metadataWorkflow).getAlterOptions().getOption(), AlterOperation.ALTER_COLUMN);
        } catch (PlanningException e) {
            Assert.fail(e.getMessage());
        }

    }


}
