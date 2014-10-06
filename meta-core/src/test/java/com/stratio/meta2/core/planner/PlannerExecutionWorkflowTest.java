/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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

        connectorName = createTestConnector("TestConnector1", dataStoreName, operationsC1);
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
    public void simpleWorkflow(){

    }


    @Test
    public void connectorChoice(){
        // Build Logical WORKFLOW

        // Create initial steps (Projects)
        List<LogicalStep> initialSteps = new LinkedList<>();
        Operations operation = Operations.PROJECT;
        Project project = new Project(operation, new TableName("demo", "table1"), new ClusterName("TestCluster1"));

        // Next step (Select)
        operation = Operations.SELECT_OPERATOR;
        Map<ColumnName, String> columnMap = new LinkedHashMap<>();
        columnMap.put(new ColumnName(table1.getName(), "id"), "id");
        columnMap.put(new ColumnName(table1.getName(), "user"), "user");
        Map<String, ColumnType> typeMap = new LinkedHashMap<>();
        typeMap.put("id", ColumnType.INT);
        typeMap.put("user", ColumnType.VARCHAR);
        Select select = new Select(operation, columnMap, typeMap);

        // Next step (Filter)
        operation = Operations.FILTER_PK_EQ;
        Selector selector = new ColumnSelector(new ColumnName(table1.getName(), "id"));
        Operator operator = Operator.EQ;
        Selector rightTerm = new IntegerSelector(25);
        Relation relation = new Relation(selector, operator, rightTerm);
        Filter filter = new Filter(operation, relation);

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

        try {
            planner.buildExecutionWorkflow(workflow);
        } catch (PlanningException e) {
            LOG.error("connectorChoice test failed", e);
        }
    }


}
