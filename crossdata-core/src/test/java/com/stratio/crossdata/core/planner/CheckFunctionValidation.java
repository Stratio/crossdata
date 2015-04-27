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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.executionplan.ExecutionType;
import com.stratio.crossdata.common.executionplan.QueryWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.metadata.MetadataManager;

public class CheckFunctionValidation extends PlannerBaseTest {

    private static final Logger LOG = Logger.getLogger(CheckFunctionValidation.class);
    private ConnectorMetadata connector1 = null;

    private ClusterName clusterName = null;

    private TableMetadata item = null;
    private TableMetadata stock = null;

    DataStoreName dataStoreName = null;
    Map<ClusterName, Integer> clusterWithDefaultPriority = new LinkedHashMap<>();

    @BeforeClass(dependsOnMethods = { "setUp" })
    public void init() throws ManifestException {
        MetadataManagerTestHelper.HELPER.initHelper();
        dataStoreName = MetadataManagerTestHelper.HELPER.createTestDatastore();

        //Connector with JOIN.
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
        operationsC1.add(Operations.FILTER_NON_INDEXED_BETWEEN);
        operationsC1.add(Operations.FILTER_NON_INDEXED_NOT_LIKE);
        operationsC1.add(Operations.FILTER_NON_INDEXED_LIKE);
        operationsC1.add(Operations.FILTER_NON_INDEXED_GET);
        operationsC1.add(Operations.FILTER_NON_INDEXED_LT);
        operationsC1.add(Operations.FILTER_PK_EQ);
        operationsC1.add(Operations.SELECT_INNER_JOIN);
        operationsC1.add(Operations.SELECT_LIMIT);
        operationsC1.add(Operations.FILTER_NON_INDEXED_GT);
        operationsC1.add(Operations.FILTER_FUNCTION_IN);
        operationsC1.add(Operations.FILTER_FUNCTION_GT);
        operationsC1.add(Operations.FILTER_FUNCTION_EQ);
        operationsC1.add(Operations.FILTER_NON_INDEXED_IN);
        operationsC1.add(Operations.FILTER_DISJUNCTION);
        operationsC1.add(Operations.FILTER_NON_INDEXED_GET);
        operationsC1.add(Operations.FILTER_PK_IN);
        operationsC1.add(Operations.SELECT_LEFT_OUTER_JOIN);
        operationsC1.add(Operations.SELECT_CROSS_JOIN);

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
        // SUBSTRING function
        FunctionType substringFunction = new FunctionType();
        substringFunction.setFunctionName("substr");
        substringFunction.setSignature("substr(Tuple[Text,Int,Int]):Tuple[Text]");
        substringFunction.setFunctionType("simple");
        substringFunction.setDescription("substring");
        functions1.add(substringFunction);
        // MAX function
        FunctionType maxFunction = new FunctionType();
        maxFunction.setFunctionName("max");
        maxFunction.setSignature("max(Tuple[Any]):Tuple[Any]");
        maxFunction.setFunctionType("aggregation");
        maxFunction.setDescription("maximum");
        functions1.add(maxFunction);
        // extract_day function
        FunctionType extractDayFunction = new FunctionType();
        extractDayFunction.setFunctionName("day");
        extractDayFunction.setSignature("day(Tuple[Native]):Tuple[Int]");
        extractDayFunction.setFunctionType("simple");
        extractDayFunction.setDescription("extract day from date");
        functions1.add(extractDayFunction);

        // DAYS_BETWEEN function
        FunctionType days_between = new FunctionType();
        days_between.setFunctionName("datediff");
        days_between.setSignature("datediff(Tuple[Any*]):Tuple[Any]");
        days_between.setFunctionType("simple");
        days_between.setDescription("datediff");
        functions1.add(days_between);

        // to_date function
        FunctionType toDateFunction = new FunctionType();
        toDateFunction.setFunctionName("to_date");
        toDateFunction.setSignature("to_date(Tuple[Any*]):Tuple[Any]");
        toDateFunction.setFunctionType("simple");
        toDateFunction.setDescription("to_date");
        functions1.add(toDateFunction);

        // to_date function
        FunctionType toAddFunction = new FunctionType();
        toAddFunction.setFunctionName("add_days");
        toAddFunction.setSignature("add_days(Tuple[Any*]):Tuple[Any]");
        toAddFunction.setFunctionType("simple");
        toAddFunction.setDescription("add_days");
        functions1.add(toAddFunction);

        // date_sub function
        FunctionType date_sub = new FunctionType();
        date_sub.setFunctionName("date_sub");
        date_sub.setSignature("date_sub(Tuple[Any*]):Tuple[Any]");
        date_sub.setFunctionType("simple");
        date_sub.setDescription("date_sub");
        functions1.add(date_sub);

        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector1", dataStoreName,
                clusterWithDefaultPriority, operationsC1, "actorRef1", functions1);

        clusterName = MetadataManagerTestHelper.HELPER
                .createTestCluster(strClusterName, dataStoreName, connector1.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("all").getName();
        createTestTables(catalogName);

        //Remove all other possible connectors except TestConnector1
        List<ConnectorMetadata> connectorsMetadata = MetadataManager.MANAGER.getConnectors();

        for (ConnectorMetadata cm : connectorsMetadata) {
            if (!cm.getName().getName().equals("TestConnector1")) {
                try {
                    MetadataManager.MANAGER.deleteConnector(cm.getName());
                } catch (NotSupportedException | SystemException | HeuristicRollbackException |
                        HeuristicMixedException | RollbackException e) {
                    LOG.error("Error when try to delete de connectors of the test:" + e.getMessage());
                }
            }
        }

    }

    @AfterClass
    public void tearDown() {
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    public void createTestTables(CatalogName catalogName) {
        createTestTables(catalogName, "item", "stock");
    }

    public void createTestTables(CatalogName catalogName, String... tableNames) {
        int i = 0;

        //item
        String[] columnNames1 = { "id", "name", "price", "data" };
        ColumnType[] columnTypes12 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        item = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames1, columnTypes12, partitionKeys1, clusteringKeys1, null);

        //stock
        String[] columnNames13 = { "id", "quantity", "order_cnt", "remote_cnt", "data" };
        ColumnType[] columnTypes13 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys13 = { "s_w_id" };
        String[] clusteringKeys13 = { };
        stock = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames13, columnTypes13, partitionKeys13, clusteringKeys13, null);

    }

    @Test
    public void testQ00Alias() throws ManifestException {

        init();

        String inputText =
                "[all], SELECT * FROM all.item alias WHERE  alias.price > 9.2;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ00Alias", false, false,
                item);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());

    }

    @Test
    public void testQ01() throws ManifestException {

        init();

        String inputText =
                "[all], SELECT sum(name) FROM all.item;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ01", false, true,
                item);
        Assert.assertTrue(true);

    }

    @Test
    public void testQ02() throws ManifestException {

        init();

        String inputText =
                "[all], SELECT * FROM all.item where substr(data,1,5)='hola';";

        try {
            QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ02", false, false,
                    item);
            Assert.assertTrue(true);
        } catch (Exception e) {

        }

    }

    @Test
    public void testQ03() throws ManifestException {

        init();

        String inputText =
                "[all], SELECT * FROM all.item where substr(data,1,5)='hola';";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ03", false, false,
                item);

        Assert.assertTrue(true);

    }

    @Test
    public void testQ04() throws ManifestException {

        init();

        String inputText =
                "[all], SELECT * FROM all.item where substr(id,1,5)='hola';";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ04", false, true, item);
        Assert.assertTrue(true);

    }

    @Test
    public void testQ05() throws ManifestException {

        init();

        String inputText =
                "[all], SELECT * FROM all.item where substr(data,1,5)=5.9;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ05", false, false, item);
        Assert.assertTrue(true);

    }

}

