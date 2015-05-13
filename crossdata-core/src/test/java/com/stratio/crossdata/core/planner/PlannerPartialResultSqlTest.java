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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.executionplan.*;
import com.stratio.crossdata.common.logicalplan.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.JoinType;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.exceptions.ValidationException;
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
public class PlannerPartialResultSqlTest extends PlannerBaseTest {

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
        operationsC1.add(Operations.SELECT_GROUP_BY);
        operationsC1.add(Operations.FILTER_NON_INDEXED_EQ);
        operationsC1.add(Operations.SELECT_SUBQUERY);
        operationsC1.add(Operations.FILTER_NON_INDEXED_LET);
        operationsC1.add(Operations.SELECT_ORDER_BY);
        operationsC1.add(Operations.FILTER_PK_EQ);
        operationsC1.add(Operations.SELECT_INNER_JOIN);
        operationsC1.add(Operations.SELECT_INNER_JOIN_PARTIALS_RESULTS);
        operationsC1.add(Operations.SELECT_CROSS_JOIN);


        //Streaming connector.
        Set<Operations> operationsC2 = new HashSet<>();
        operationsC2.add(Operations.PROJECT);
        operationsC2.add(Operations.SELECT_OPERATOR);
        operationsC2.add(Operations.FILTER_DISJUNCTION);
        operationsC2.add(Operations.FILTER_NON_INDEXED_IN);
        operationsC2.add(Operations.FILTER_PK_GT);
        operationsC2.add(Operations.SELECT_WINDOW);
        operationsC2.add(Operations.FILTER_NON_INDEXED_EQ);
        operationsC2.add(Operations.FILTER_PK_EQ);
        operationsC2.add(Operations.FILTER_NON_INDEXED_EQ);
        operationsC2.add(Operations.FILTER_NON_INDEXED_LIKE);
        operationsC2.add(Operations.FILTER_PK_LET);

        String strClusterName = "TestCluster1";
        clusterWithDefaultPriority.put(new ClusterName(strClusterName), Constants.DEFAULT_PRIORITY);

        List<FunctionType> functions1 = new ArrayList<>();
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
    public void joinWindowBasicWhere() throws ManifestException {

        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 MINUTES " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id > 5;";

        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id";

        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");

    }

    @Test
    public void joinWindowMultipleRelation() throws ManifestException {

        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 ROWS " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.user LIKE 'David' AND demo.table1.id <= 6;";

        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id";

        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");

    }



    @Test
    public void joinWindowAndRelation() throws ManifestException {

        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 ROWS " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.user LIKE 'David' AND demo.table3.address = 'via';";

        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table3.address = 'via'";

        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");

    }

    @Test
    public void joinWindowInnerRelation() throws ManifestException {

        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 ROWS " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id = demo.table1.id + 1;";

        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id";

        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");

    }

    @Test
    public void joinWindowInnerComposeRelation() throws ManifestException {

        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 ROWS " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id = demo.table3.id_aux + 1;";

        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id = demo.table3.id_aux + 1";

        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");

    }

    @Test
    public void joinWindowPrecedenceRelation() throws ManifestException {

        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 ROWS " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id = 5 AND demo.table1.id > 5 AND demo.table1.id < 5 OR demo.table3.id_aux > 5;";

        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id < 5 OR demo.table3.id_aux > 5";

        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");

    }




    @Test
    public void testSelectWithDisjunction() throws ManifestException {


        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 ROWS " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id = 25 AND ((demo.table1.id = 25 AND demo.table1.id = 25) OR (demo.table1.id = 14 AND demo.table1.id = 14) OR (demo.table1.id = 25 AND demo.table1.id = 14));";


        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id";


        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");


    }

    @Test
    public void testSelectWithDisjunctionMultipleTables() throws ManifestException {


        init();

        String inputText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 WITH WINDOW 5 ROWS " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE demo.table1.id = 25 AND ((demo.table1.id = 25 AND demo.table1.id = 25) OR (demo.table3.id_aux = 14 AND demo.table1.id = 14) OR (demo.table1.id = 25 AND demo.table1.id = 14));";


        String expectedText = "SELECT demo.table3.address, demo.table1.id FROM demo.table1 " +
                "INNER JOIN demo.table3 ON demo.table3.id_aux = demo.table1.id WHERE ((demo.table1.id = 25 AND demo.table1.id = 25) OR (demo.table3.id_aux = 14 AND demo.table1.id = 14) OR (demo.table1.id = 25 AND demo.table1.id = 14))";


        ExecutionWorkflow lastExecutionWorkflow=  getPlannedQuery(
                inputText, "testJoinWithStreaming", false, table1, table3);

        while(lastExecutionWorkflow.getNextExecutionWorkflow() != null){
            lastExecutionWorkflow = lastExecutionWorkflow.getNextExecutionWorkflow();
        }

        LogicalWorkflow logicalWorkflow = ((QueryWorkflow) lastExecutionWorkflow).getWorkflow();
        assertEquals(logicalWorkflow.getSqlDirectQuery(), expectedText, "Sql malformed");


    }


}
