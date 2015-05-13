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
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.executionplan.ExecutionPath;
import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;

/**
 * Planner test concerning priority.
 */
public class PlannerPriorityTest extends PlannerBaseTest {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(PlannerExecutionWorkflowTest.class);

    private PlannerWrapper plannerWrapper = new PlannerWrapper("127.0.0.1");

    private ConnectorMetadata connector1 = null;
    private ConnectorMetadata connector2 = null;
    private ConnectorMetadata connector3 = null;

    private ClusterName clusterName = null;

    private TableMetadata table1 = null;
    private TableMetadata table2 = null;

    @BeforeClass(dependsOnMethods = { "setUp" })
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

        connector1 = MetadataManagerTestHelper.HELPER
                        .createTestConnector("TestConnector1", dataStoreName, clusterWithDefaultPriority, operationsC1,
                                        "actorRef1", new ArrayList<FunctionType>());
        connector2 = MetadataManagerTestHelper.HELPER
                        .createTestConnector("TestConnector2", dataStoreName, clusterWithDefaultPriority, operationsC2,
                                        "actorRef2", new ArrayList<FunctionType>());
        connector3 = MetadataManagerTestHelper.HELPER
                        .createTestConnector("TestConnector3", dataStoreName, clusterWithTopPriority, operationsC1,
                                        "actorRef3", new ArrayList<FunctionType>());

        clusterName = MetadataManagerTestHelper.HELPER
                        .createTestCluster(strClusterName, dataStoreName, connector1.getName(), connector3.getName());
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
        Project project = new Project(
                Collections.singleton(operation),
                new TableName("demo", tableName),
                new ClusterName("TestCluster1"));
        for (ColumnName cn : columns) {
            project.addColumn(cn);
        }
        return project;
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
        Select select = new Select(
                Collections.singleton(operation),
                columnMap,
                typeMap,
                typeMapFromColumnName);
        return select;
    }

    public void createTestTables() {
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes1 = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        table1 = MetadataManagerTestHelper.HELPER
                        .createTestTable(clusterName, "demo", "table1", columnNames1, columnTypes1, partitionKeys1,
                                        clusteringKeys1, null);

        String[] columnNames2 = { "id", "email" };
        ColumnType[] columnTypes2 = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        String[] partitionKeys2 = { "id" };
        String[] clusteringKeys2 = { };
        table2 = MetadataManagerTestHelper.HELPER
                        .createTestTable(clusterName, "demo", "table2", columnNames2, columnTypes2, partitionKeys2,
                                        clusteringKeys2, null);

        String[] columnNames3 = { "id", "email" };
        ColumnType[] columnTypes3 = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        String[] partitionKeys3 = { "id" };
        String[] clusteringKeys3 = { };
        table2 = MetadataManagerTestHelper.HELPER
                        .createTestTable(clusterName, "demo", "table3", columnNames3, columnTypes3, partitionKeys3,
                                        clusteringKeys3, null);
    }
    //
    // Internal methods.
    //

    @Test
    public void testBestConnectorBasicSelect() {
        List<LogicalStep> initialSteps = new LinkedList<>();
        Project project = getProject("table1");

        ColumnName[] columns = { new ColumnName(table1.getName(), "id"), new ColumnName(table1.getName(), "user") };
        ColumnType[] types = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        Select select = getSelect(columns, types);

        //Link the elements
        project.setNextStep(select);
        initialSteps.add(project);

        List<ConnectorMetadata> availableConnectors = new ArrayList<>();
        availableConnectors.add(connector1);
        availableConnectors.add(connector3);

        ExecutionPath path = null;
        ExecutionWorkflow executionWorkflow = null;
        try {

            IParsedQuery stmt = helperPT.testRegularStatement("select catalog.table.a from catalog.table;",
                    "mergeExecutionPathsJoinException");
            SelectParsedQuery spq = SelectParsedQuery.class.cast(stmt);
            SelectStatement ss = spq.getStatement();

            SelectValidatedQueryWrapper svqw = new SelectValidatedQueryWrapper(ss, spq);
            path = plannerWrapper.defineExecutionPath(project, availableConnectors, svqw);
            executionWorkflow = plannerWrapper.buildExecutionWorkflow(svqw, new LogicalWorkflow(initialSteps));
        } catch (PlanningException e) {
            fail("Not expecting Planning Exception", e);
        }

        assertEquals(path.getAvailableConnectors().size(), 2, "Invalid size");
        assertEquals(executionWorkflow.getActorRef(), connector3.getActorRef("127.0.0.1"), "Invalid connector selected");
    }

}
