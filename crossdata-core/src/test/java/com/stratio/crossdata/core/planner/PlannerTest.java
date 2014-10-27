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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.executionplan.ExecutionType;
import com.stratio.crossdata.common.executionplan.QueryWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;

/**
 * Planner tests considering an initial input, generating all intermediate steps,
 * and generating a ExecutionWorkflow.
 */
public class PlannerTest extends PlannerBaseTest{

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(PlannerTest.class);

    private ConnectorMetadata connector1 = null;
    private ConnectorMetadata connector2 = null;

    private ClusterName clusterName = null;

    private TableMetadata table1 = null;
    private TableMetadata table2 = null;
    private TableMetadata table3 = null;

    @BeforeClass
    public void setUp() {
        super.setUp();
        DataStoreName dataStoreName = createTestDatastore();

        //Connector with join.
        Set<Operations> operationsC1 = new HashSet<>();
        operationsC1.add(Operations.PROJECT);
        operationsC1.add(Operations.SELECT_OPERATOR);
        operationsC1.add(Operations.SELECT_WINDOW);

        //Streaming connector.
        Set<Operations> operationsC2 = new HashSet<>();
        operationsC2.add(Operations.PROJECT);
        operationsC2.add(Operations.SELECT_OPERATOR);
        operationsC2.add(Operations.FILTER_PK_EQ);
        operationsC2.add(Operations.SELECT_INNER_JOIN);
        operationsC2.add(Operations.SELECT_INNER_JOIN_PARTIALS_RESULTS);

        connector1 = createTestConnector("TestConnector1", dataStoreName, new HashSet<ClusterName>(),operationsC1, "actorRef1");
        connector2 = createTestConnector("TestConnector2", dataStoreName, new HashSet<ClusterName>(),operationsC2, "actorRef2");

        clusterName = createTestCluster("TestCluster1", dataStoreName, connector1.getName(), connector2.getName());
        CatalogName catalogName = createTestCatalog("demo");
        createTestTables();
    }

    public void createTestTables() {
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes1 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        table1 = createTestTable(clusterName, "demo", "table1",
                columnNames1, columnTypes1, partitionKeys1, clusteringKeys1);

        String[] columnNames2 = { "id", "email" };
        ColumnType[] columnTypes2 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys2 = { "id" };
        String[] clusteringKeys2 = { };
        table2 = createTestTable(clusterName, "demo", "table2",
                columnNames2, columnTypes2, partitionKeys2, clusteringKeys2);

        String[] columnNames3 = { "id_aux", "address" };
        ColumnType[] columnTypes3 = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys3 = { "id_aux" };
        String[] clusteringKeys3 = { };
        table3 = createTestTable(clusterName, "demo", "table3",
                columnNames3, columnTypes3, partitionKeys3, clusteringKeys3);
    }


    @Test
    public void selectSingleColumn(){
        String inputText = "SELECT demo.table1.id FROM demo.table1;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "selectSingleColumn", false, table1);
        assertNotNull(queryWorkflow, "Null worfklow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector1.getActorRef(), "Wrong target actor");
    }

    @Test
    public void selectJoinMultipleColumns(){
        String inputText = "SELECT demo.table1.id, demo.table1.user, demo.table2.id, demo.table2.email"
                + " FROM demo.table1"
                + " INNER JOIN demo.table2 ON demo.table1.id = demo.table2.id;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "selectJoinMultipleColumns", false, table1, table2);
        assertNotNull(queryWorkflow, "Null worfklow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector2.getActorRef(), "Wrong target actor");
    }

    @Test
    public void selectJoinMultipleColumnsDiffOnNames(){
        String inputText = "SELECT demo.table1.id, demo.table1.user, demo.table3.id_aux, demo.table3.address"
                + " FROM demo.table1"
                + " INNER JOIN demo.table3 ON demo.table1.id = demo.table3.id_aux;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "selectJoinMultipleColumnsDiffOnNames", false, table1, table3);
        assertNotNull(queryWorkflow, "Null worfklow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        assertEquals(queryWorkflow.getActorRef(), connector2.getActorRef(), "Wrong target actor");
    }

}
