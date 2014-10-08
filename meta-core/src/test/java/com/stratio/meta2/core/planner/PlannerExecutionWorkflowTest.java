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

package com.stratio.meta2.core.planner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.exceptions.PlanningException;
import com.stratio.meta.common.executionplan.ExecutionWorkflow;
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.logicalplan.Select;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.IntegerSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;

/**
 * Planner test concerning Execution workflow creation.
 */
public class PlannerExecutionWorkflowTest extends PlannerBaseTest {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(PlannerExecutionWorkflowTest.class);

    private ConnectorName connectorName = null;

    private ClusterName clusterName = null;

    private TableMetadata table1 = null;
    private TableMetadata table2 = null;

    /**
     * Create a test Project operator.
     * @param tableName Name of the table.
     * @param columns List of columns.
     * @return A {@link com.stratio.meta.common.logicalplan.Project}.
     */
    public Project getProject(String tableName, ColumnName ... columns){
        Operations operation = Operations.PROJECT;
        Project project = new Project(operation, new TableName("demo", tableName), new ClusterName("TestCluster1"));
        for(ColumnName cn: columns) {
            project.addColumn(cn);
        }
        return project;
    }

    public Filter getFilter(Operations operation, ColumnName left, Operator operator, Selector right){
        Relation relation = new Relation(new ColumnSelector(left), operator, right);
        Filter filter = new Filter(operation, relation);
        return filter;
    }

    public Select getSelect(ColumnName [] columns, ColumnType [] types){
        Operations operation = Operations.SELECT_OPERATOR;
        Map<ColumnName, String> columnMap = new LinkedHashMap<>();
        Map<String, ColumnType> typeMap = new LinkedHashMap<>();

        for(int index = 0; index < columns.length; index++){
            columnMap.put(columns[index], columns[index].getName());
            typeMap.put(columns[index].getName(), types[index]);
        }
        Select select = new Select(operation, columnMap, typeMap);
        return select;
    }


    @BeforeClass
    public void setUp(){
        super.setUp();
        DataStoreName dataStoreName = createTestDatastore();

        //Connector with join.
        Set<Operations> operationsC1 = new HashSet<>();
        operationsC1.add(Operations.PROJECT);
        operationsC1.add(Operations.SELECT_OPERATOR);
        operationsC1.add(Operations.FILTER_PK_EQ);
        operationsC1.add(Operations.SELECT_INNER_JOIN);
        operationsC1.add(Operations.SELECT_INNER_JOIN_PARTIALS_RESULTS);

        //Streaming connector.
        Set<Operations> operationsC2 = new HashSet<>();
        operationsC2.add(Operations.PROJECT);
        operationsC2.add(Operations.SELECT_OPERATOR);
        operationsC2.add(Operations.SELECT_WINDOW);

        connectorName = createTestConnector("TestConnector1", dataStoreName, operationsC1, "actorRef1");
        clusterName = createTestCluster("TestCluster1", dataStoreName, connectorName);
        CatalogName catalogName = createTestCatalog("demo");
        createTestTables();
    }


    public void createTestTables(){
        String [] columnNames1 = {"id", "user"};
        ColumnType [] columnTypes1 = {ColumnType.INT, ColumnType.TEXT};
        String [] partitionKeys1 = {"id"};
        String [] clusteringKeys1 = {};
        table1 = createTestTable(clusterName, "demo", "table1", columnNames1,columnTypes1, partitionKeys1,
                clusteringKeys1);

        String [] columnNames2 = {"id", "email"};
        ColumnType [] columnTypes2 = {ColumnType.INT, ColumnType.TEXT};
        String [] partitionKeys2 = {"id"};
        String [] clusteringKeys2 = {};
        table2 = createTestTable(clusterName, "demo", "table2", columnNames2,columnTypes2, partitionKeys2,
                clusteringKeys2);
    }

    /**
     * Simple workflow consisting on Project -> Select
     */
    @Test
    public void projectSelect(){

    }


    @Test
    public void projectFilterSelect(){
        // Build Logical WORKFLOW

        // Create initial steps (Projects)
        List<LogicalStep> initialSteps = new LinkedList<>();

        Project project = getProject("table1");

        ColumnName [] columns = {new ColumnName(table1.getName(), "id"), new ColumnName(table1.getName(), "user")};
        ColumnType [] types = {ColumnType.INT, ColumnType.TEXT};
        Select select = getSelect(columns, types);

        Filter filter = getFilter(Operations.FILTER_PK_EQ, columns[0], Operator.EQ, new IntegerSelector(42));

        //Link the elements
        project.setNextStep(filter);
        filter.setNextStep(select);
        initialSteps.add(project);

        // Add initial steps
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //TEST

        // Get initial steps
        List<TableName> tables = planner.getInitialSteps(workflow.getInitialSteps());

        // Get connectors meeting the required capabilities
        Map<TableName, List<ConnectorMetadata>> candidatesConnectors = planner.findCapableConnectors(tables,
                workflow.getInitialSteps());

        assertEquals(candidatesConnectors.values().iterator().next().iterator().next().getName(), connectorName,
                "Candidate Connectors wrong");

        // Get more suitable connector
        try {
            ConnectorMetadata chosenConnector = planner.findMoreSuitableConnector(candidatesConnectors);
            assertEquals(chosenConnector.getName(), connectorName, "Chosen connector wrong");
        } catch (PlanningException e) {
            fail(e.getMessage());
        }

        ExecutionWorkflow executionWorkflow = null;
        try {
            executionWorkflow = planner.buildExecutionWorkflow(workflow);
        } catch (PlanningException e) {
            LOG.error("connectorChoice test failed", e);
        }
        assertExecutionWorkflow(executionWorkflow, 1, new String [] {"actorRef1"});


    }


}
