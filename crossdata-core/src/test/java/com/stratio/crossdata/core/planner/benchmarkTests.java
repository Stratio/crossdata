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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;

public class benchmarkTests extends PlannerBaseTest {

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
        String[] columnNames1 = { "id", "user", "rating", "comment" };
        ColumnType[] columnTypes1 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.TEXT) };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        table1 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[0],
                columnNames1, columnTypes1, partitionKeys1, clusteringKeys1, null);

        String[] columnNames2 = { "id", "email", "value", "year" };
        ColumnType[] columnTypes2 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.INT) };
        String[] partitionKeys2 = { "id" };
        String[] clusteringKeys2 = { };
        table2 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[1],
                columnNames2, columnTypes2, partitionKeys2, clusteringKeys2, null);

        String[] columnNames3 = { "id_aux", "address", "city", "code" };
        ColumnType[] columnTypes3 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.BIGINT) };
        String[] partitionKeys3 = { "id_aux" };
        String[] clusteringKeys3 = { };
        table3 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[2],
                columnNames3, columnTypes3, partitionKeys3, clusteringKeys3, null);
    }

    /*
    @Test
    public void testQ1() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                + "id, "
                + "sum(id*(rating)*(1+rating)) AS sum_charge, "
                + "avg(rating) AS avg_size, "
                + "count(*) AS count_order "
                + "FROM table1 "
                + "WHERE "
                + "rating <= date('1998-12-01') - interval(70, 3) "
                + "GROUP BY rating "
                + "ORDER BY user;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ1", false, false, table1);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ2() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                + "user "
                + "FROM table1, table3, table2 "
                + "WHERE "
                + "id_aux = table1.id "
                + "AND code = 100 "
                + "AND comment LIKE '%s' "
                + "AND rating = ("
                    + "SELECT "
                    + "min(code) "
                    + "FROM table3, table1, table2 "
                    + "WHERE city = 'NYC' AND value = 999"
                + ") "
                + "ORDER BY year DESC;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ2", false, true, table1);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }
    */

}
