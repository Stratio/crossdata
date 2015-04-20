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
import com.stratio.crossdata.common.data.Status;
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

public class TPCCBenchmarkTests extends PlannerBaseTest {

    private static final Logger LOG = Logger.getLogger(TPCCBenchmarkTests.class);
    private ConnectorMetadata connector1 = null;

    private ClusterName clusterName = null;

    private TableMetadata warehouse = null;
    private TableMetadata district = null;
    private TableMetadata customer = null;
    private TableMetadata history = null;
    private TableMetadata new_order = null;
    private TableMetadata order = null;
    private TableMetadata order_line = null;
    private TableMetadata item = null;
    private TableMetadata stock = null;
    private TableMetadata partsupp = null;
    private TableMetadata part = null;
    private TableMetadata supplier = null;

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
        operationsC1.add(Operations.FILTER_NON_INDEXED_IN);
        operationsC1.add(Operations.FILTER_DISJUNCTION);
        operationsC1.add(Operations.FILTER_NON_INDEXED_GET);
        operationsC1.add(Operations.FILTER_PK_IN);
        operationsC1.add(Operations.SELECT_LEFT_OUTER_JOIN);

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
        days_between.setFunctionName("days_between");
        days_between.setSignature("days_between(Tuple[Any*]):Tuple[Any]");
        days_between.setFunctionType("simple");
        days_between.setDescription("days_between");
        functions1.add(days_between);

        // to_date function
        FunctionType toDateFunction = new FunctionType();
        toDateFunction.setFunctionName("to_date");
        toDateFunction.setSignature("to_date(Tuple[Any*]):Tuple[Any]");
        toDateFunction.setFunctionType("simple");
        toDateFunction.setDescription("to_date");
        functions1.add(toDateFunction);

        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector1", dataStoreName,
                clusterWithDefaultPriority, operationsC1, "actorRef1", functions1);

        clusterName = MetadataManagerTestHelper.HELPER
                .createTestCluster(strClusterName, dataStoreName, connector1.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("tpcc").getName();
        createTestTables(catalogName);

        //Remove all other possible connectors except TestConnector1
        List<ConnectorMetadata> connectorsMetadata=MetadataManager.MANAGER.getConnectors();

        for (ConnectorMetadata cm:connectorsMetadata){
            if (!cm.getName().getName().equals("TestConnector1")){
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
        createTestTables(catalogName, "warehouse", "district", "customer", "history", "new_order", "order",
                "order_line", "item", "stock");
    }

    public void createTestTables(CatalogName catalogName, String... tableNames) {
        int i = 0;

        //warehouse
        String[] columnNames4 = { "w_id", "w_name", "w_street_1", "w_street_2", "w_city", "w_state", "w_zip", "w_tax",
                "w_ytd" };
        ColumnType[] columnTypes4 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT)
        };
        String[] partitionKeys4 = { "w_id" };
        String[] clusteringKeys4 = { };
        warehouse = MetadataManagerTestHelper.HELPER
                .createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames4, columnTypes4, partitionKeys4, clusteringKeys4, null);

        //district
        String[] columnNames5 = { "d_id", "d_w_id", "d_name", "d_street_1", "d_street_2", "d_city", "d_state", "d_zip",
                "d_tax", "d_ytd", "d_next_o_id" };
        ColumnType[] columnTypes5 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT)
        };
        String[] partitionKeys5 = { "d_w_id" };
        String[] clusteringKeys5 = { "d_d_id" };
        district = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames5, columnTypes5, partitionKeys5, clusteringKeys5, null);

        //customer
        String[] columnNames6 = { "c_id", "c_d_id", "c_w_id", "c_first", "c_middle", "c_last", "c_street_1",
                "c_street_2", "c_city", "c_state", "c_zip", "c_phone", "c_since", "c_credit", "c_credit_lim",
                "c_discount", "c_balance", "c_ytd_payment", "c_payment_cnt", "c_delivery_cnt", "c_data" };
        ColumnType[] columnTypes6 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.NATIVE),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT)

        };
        String[] partitionKeys6 = { "c_w_id", "c_d_id,", "c_id" };
        String[] clusteringKeys6 = { };
        customer = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames6, columnTypes6, partitionKeys6, clusteringKeys6, null);

        //history
        String[] columnNames8 = { "h_c_id", "h_c_d_id", "h_c_w_id", "h_d_id", "h_w_id", "h_date", "h_amount",
                "h_data" };
        ColumnType[] columnTypes8 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.NATIVE),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys8 = { "h_c_w_id" };
        String[] clusteringKeys8 = { };
        history = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames8, columnTypes8, partitionKeys8, clusteringKeys8, null);

        //new_order
        String[] columnNames9 = { "no_o_id", "no_d_id", "no_w_id" };
        ColumnType[] columnTypes9 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT)
        };
        String[] partitionKeys9 = { "no_o_id" };
        String[] clusteringKeys9 = { };
        new_order = MetadataManagerTestHelper.HELPER
                .createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames9, columnTypes9, partitionKeys9, clusteringKeys9, null);

        //order
        String[] columnNames10 = { "o_id", "o_d_id", "o_w_id", "o_c_id", "o_entry_d", "o_carrier_id", "o_ol_cnt",
                "o_all_local" };
        ColumnType[] columnTypes10 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.NATIVE),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT)
        };
        String[] partitionKeys10 = { "o_w_id" };
        String[] clusteringKeys10 = { };
        order = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames10, columnTypes10, partitionKeys10, clusteringKeys10, null);

        //order_line
        String[] columnNames11 = { "ol_o_id", "ol_d_id", "ol_w_id", "ol_number", "ol_i_id", "ol_supply_w_id",
                "ol_delivery_d", "ol_quantity", "ol_amount", "ol_dist_info" };
        ColumnType[] columnTypes11 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.NATIVE),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys11 = { "ol_w_id" };
        String[] clusteringKeys11 = { };
        order_line = MetadataManagerTestHelper.HELPER
                .createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames11, columnTypes11, partitionKeys11, clusteringKeys11, null);

        //item
        String[] columnNames12 = { "i_id", "i_im_id", "i_name", "i_price", "i_data" };
        ColumnType[] columnTypes12 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys12 = { "i_id" };
        String[] clusteringKeys12 = { };
        item = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames12, columnTypes12, partitionKeys12, clusteringKeys12, null);

        //stock
        String[] columnNames13 = { "s_i_id", "s_w_id", "s_quantity", "s_dist_01", "s_dist_02", "s_dist_03", "s_dist_04",
                "s_dist_05", "s_dist_06", "s_dist_07", "s_dist_08", "s_dist_09", "s_dist_10", "s_ytd", "s_order_cnt",
                "s_remote_cnt", "s_data" };
        ColumnType[] columnTypes13 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys13 = { "s_w_id" };
        String[] clusteringKeys13 = { };
        stock = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames13, columnTypes13, partitionKeys13, clusteringKeys13, null);
/*
        //partsupp
        String[] columnNames14 = { "ps_partkey" };
        ColumnType[] columnTypes14 = {
                new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys14 = { "ps_partkey" };
        String[] clusteringKeys14 = { };
        partsupp = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames14, columnTypes14, partitionKeys14, clusteringKeys14, null);

        //part
        String[] columnNames15 = { "p_partkey", "p_type", "p_brand", "p_size" };
        ColumnType[] columnTypes15 = {
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT)
        };
        String[] partitionKeys15 = { "p_partkey" };
        String[] clusteringKeys15 = { };
        part = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames15, columnTypes15, partitionKeys15, clusteringKeys15, null);

        //supplier
        String[] columnNames16 = { "s_comment", "supplier_cnt" };
        ColumnType[] columnTypes16 = {
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),

        };
        String[] partitionKeys16 = { "supplier_cnt" };
        String[] clusteringKeys16 = { };
        part = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames16, columnTypes16, partitionKeys16, clusteringKeys16, null);
*/
    }

    //Subquery is not supported within an innerJoin clause by the grammar
    @Test
    public void testQ00Hive() throws ManifestException {

        init();

        String inputText = "[tpcc], SELECT substr(c_state,1,1) AS country, count(*) AS numcust, sum(c_balance) AS totacctbal "
                        + "FROM tpcc.customer "
                        + " INNER JOIN"
                        + " ( SELECT avg(sub.c_balance) AS balance "
                                + "FROM tpcc.customer sub WHERE  sub.c_balance > 0.00 AND substr(sub.c_phone,1,1) IN ['1','2','3','4','5','6','7']"
                        + " ) y"
                        + "    WHERE  substr(c_phone,1,1) IN ['1','2','3','4','5','6','7']"
                        + "    AND c_balance > y.balance"
                        + "    GROUP BY substring(c_state,1,1)"
                        + "    ORDER BY country;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ00", false, false, customer);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ00Crossdata() throws ManifestException {

        init();

        String inputText = "[tpcc], SELECT substr(c_state,1,1) AS country,"
                        + "    count(*) AS numcust,"
                        + "    sum(c_balance) AS totacctbal FROM"
                        + "    ( SELECT avg(sub.c_balance) AS balance FROM  tpcc.customer sub WHERE  sub.c_balance > 0.00 AND substr(sub.c_phone,1,1) IN ['1','2','3','4','5','6','7']) y"
                        + "    INNER JOIN tpcc.customer ON c_balance > y.balance "
                        + "    WHERE substr(c_phone,1,1) IN ['1','2','3','4','5','6','7']"
                        + "    GROUP BY substring(c_state,1,1)"
                        + "    ORDER BY country;";


        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ0", false, false, customer);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT REVIEWED --> OK
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());

    }

    @Test
    public void testQ00CrossdataImplicit() throws ManifestException {

        init();

        String inputText = "[tpcc], SELECT substr(c_state,1,1) AS country,"
                + "    count(*) AS numcust,"
                + "    sum(c_balance) AS totacctbal FROM"
                + "    ( SELECT avg(sub.c_balance) AS balance FROM  tpcc.customer sub WHERE  sub.c_balance > 0.00 AND substr(sub.c_phone,1,1) IN ['1','2','3','4','5','6','7']) y"
                + "    , tpcc.customer "
                +"     WHERE c_balance > y.balance "
                + "    AND substr(c_phone,1,1) IN ['1','2','3','4','5','6','7']"
                + "    GROUP BY substring(c_state,1,1)"
                + "    ORDER BY country;";


        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ0", false, false, customer);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ01Original() throws ManifestException {

        init();

        String inputText = "[tpcc], SELECT "
                + "ol_o_id, ol_d_id, ol_w_id, sum(ol_quantity), avg(ol_quantity), sum(ol_amount) AS suma, avg(ol_amount),count(*) "
                + "FROM tpcc.order_line WHERE ol_d_id=4 AND ol_w_id=175 "
                + "GROUP BY ol_o_id, ol_d_id,ol_w_id ORDER BY sum(ol_amount) desc limit 10;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ01Hive", false, false, order_line);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }


    @Test
    public void testQ02Original() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + " SELECT OL.OL_w_id,OL.OL_D_id,OL.OL_O_id,AVG_Amoun,avg(OL.ol_amount) "
                + "FROM "
                + "( SELECT d_id,d_w_id, avg(ol_amount) AS AVG_Amoun "
                + "    FROM tpcc.district D, tpcc.order_line OL_A WHERE D.d_id=OL_A.ol_d_id AND D.d_w_id=OL_A.ol_w_id and"
                + "    d_id=3 AND d_w_id=241 GROUP BY d_id,d_w_id"
                + ") A, tpcc.order_line OL "
                + "   WHERE A.d_id=OL.ol_d_id AND A.d_w_id=OL.ol_w_id AND OL.ol_d_id=${random(district)} AND OL.ol_w_id=${random(0,tpcc.number.warehouses)} "
                + "   GROUP BY OL.OL_w_id,OL.OL_D_id,OL.OL_O_id,AVG_Amoun having avg(OL.ol_amount) > AVG_Amoun ORDER BY avg(OL.ol_amount) desc;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ02", false, false, order_line);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());

    }

    @Test
    public void testQ02HiveWithoutTypo() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + "SELECT OL.ol_w_id, OL.ol_d_id, OL.ol_o_id, AVG_Amoun, avg(OL.ol_amount) AS average FROM "
                + "(SELECT d_id, d_w_id, avg(ol_amount) AS AVG_Amoun "
                    + "FROM tpcc.district D, tpcc.order_line OL_A "
                    + "WHERE D.d_id=OL_A.ol_d_id AND D.d_w_id=OL_A.ol_w_id AND d_id=3 AND d_w_id=241 GROUP BY d_id,d_w_id"
                + ") A, "
                + "tpcc.order_line OL "
                + "WHERE A.d_id=OL.ol_d_id AND A.d_w_id=OL.ol_w_id AND OL.ol_d_id=3 AND OL.ol_w_id=241 "
                + "GROUP BY OL.ol_w_id, OL.ol_d_id, OL.ol_o_id, AVG_Amoun having avg(OL.ol_amount) > AVG_Amoun "
                + "ORDER BY average desc;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ02", false, false, order_line);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ02Crossdata() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + " SELECT OL.ol_w_id,OL.ol_d_id,OL.ol_o_id,avg(OL.ol_amount) AS average FROM "
                + "( SELECT d_id,d_w_id"
                + " FROM tpcc.district D INNER JOIN tpcc.order_line OL_A on D.d_id=OL_A.ol_d_id AND D.d_w_id=OL_A.ol_w_id "
                + "WHERE d_id=3 AND d_w_id=241 GROUP BY d_id,d_w_id"
                + ") A INNER JOIN  "
                + "tpcc.order_line AS OL ON A.d_id=OL.ol_d_id AND A.d_w_id=OL.ol_w_id "
                + "WHERE OL.ol_w_id=241 AND OL.ol_d_id=3 "
                + "GROUP BY OL.ol_w_id,OL.ol_d_id,OL.ol_o_id ORDER BY average desc;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ02", false, false, order_line);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ03() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT c_d_id,c_credit,count(o_id) FROM tpcc.customer"
                + " INNER JOIN tpcc.order on c_d_id=o_d_id AND c_w_id=o_w_id AND o_c_id=c_id"
                + "    WHERE c_w_id=168  GROUP BY c_credit,c_d_id "
                + "    ORDER BY c_d_id, c_credit;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ3", false, false, customer,
                order);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());

    }

    @Test
    public void testQ04SomeRewrite() throws ManifestException {

        init();

        String inputText = "[tpcc],   SELECT "
                + "    c.c_state,days_between(o.o_entry_d,ol.ol_delivery_d), sum(ol.ol_amount),avg(ol.ol_amount) "
                + "    FROM tpcc.order_line ol,"
                + "    tpcc.order o,"
                + "    tpcc.customer c "
                + "    WHERE o.o_id=ol.ol_o_id  "
                + "    AND o.o_d_id=ol.ol_d_id  "
                + "    AND o.o_w_id=ol.ol_w_id AND o.o_c_id=c.c_id "
                + "    AND o.o_d_id=c.c_d_id "
                + "    AND o.o_w_id=c.c_w_id "
                + "    AND c.c_w_id=100 "
                + "    AND c_since>= "
                + "     ( SELECT add_days(max(c_since),-7) FROM tpcc.customer c )"
                + "    AND days_between(o.o_entry_d,ol.ol_delivery_d)>30 "
                + "    GROUP BY c.c_state,days_between(o.o_entry_d,ol.ol_delivery_d)"
                + "    ORDER BY count(*) desc LIMIT 10;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ4", false, false, order,
                customer);

        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ04Previous() throws ManifestException {

        init();

        String inputText = "[tpcc], SELECT c.c_state, o.o_entry_d " +
                "FROM tpcc.order_line ol INNER JOIN tpcc.order o " +
                "ON o.o_id = ol.ol_o_id AND o.o_d_id = ol.ol_d_id AND o.o_w_id = ol.ol_w_id " +
                "INNER JOIN tpcc.customer c ON o.o_c_id = c.c_id AND o.o_d_id = c.c_d_id AND o.o_w_id = c.c_w_id;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ04Previous", false, false,
                order, customer);
        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());

    }

        @Test
    public void testQ04RewriteWithoutImplicits() throws ManifestException {

        init();

        String inputText = "[tpcc],     SELECT c.c_state," +
                "days_between(o.o_entry_d,ol.ol_delivery_d), " +
                "sum(ol.ol_amount)," +
                "avg(ol.ol_amount) " +
                "FROM tpcc.order_line ol INNER JOIN tpcc.order o " +
                "ON o.o_id = ol.ol_o_id AND o.o_d_id = ol.ol_d_id AND o.o_w_id = ol.ol_w_id " +
                "INNER JOIN tpcc.customer c ON o.o_c_id = c.c_id AND o.o_d_id = c.c_d_id AND o.o_w_id = c.c_w_id " +
                "WHERE c_since >= (SELECT add_days(max(c_since), -7) FROM tpcc.customer c) " +
                "AND days_between(o.o_entry_d, ol.ol_delivery_d)>30 " +
                "GROUP BY c.c_state, days_between(o.o_entry_d, ol.ol_delivery_d) " +
                "ORDER BY count(*) desc " +
                "LIMIT 10;";

        String inputText2 = "[tpcc],     SELECT " +
                "days_between(o.o_entry_d,ol.ol_delivery_d), " +
                "sum(ol.ol_amount)," +
                "avg(ol.ol_amount) " +
                "FROM tpcc.order_line ol INNER JOIN tpcc.order o " +
                "ON o.o_id = ol.ol_o_id AND o.o_d_id = ol.ol_d_id AND o.o_w_id = ol.ol_w_id " +
                "WHERE days_between (o.o_entry_d, ol.ol_delivery_d)>30 " +
                "ORDER BY count(*) desc " +
                "LIMIT 10;";

        String inputText3 = "[tpcc],     SELECT c.c_state," +
                "days_between(o.o_entry_d,ol.ol_delivery_d), " +
                "sum(ol.ol_amount)," +
                "avg(ol.ol_amount) " +
                "FROM tpcc.order_line ol INNER JOIN tpcc.order o " +
                "ON o.o_id = ol.ol_o_id " +
                "INNER JOIN tpcc.customer c ON o.o_c_id = c.c_id " +
                "WHERE c_since >= (SELECT add_days(max(c_since), -7) FROM tpcc.customer c) " +
                "AND days_between (o.o_entry_d, ol.ol_delivery_d)>30 " +
                "GROUP BY c.c_state, days_between(o.o_entry_d, ol.ol_delivery_d) " +
                "ORDER BY count( *)desc " +
                "LIMIT 10;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ4RWI", false, false, order,
                customer);
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ05() throws ManifestException {

        init();

        String inputText =
                "[tpcc], SELECT s_i_id,i_price, s_quantity, i_name,count(*) AS numero_pedidos, sum(i_price*s_quantity) AS venta_total "
                        + "FROM tpcc.stock, tpcc.item WHERE i_id=s_i_id AND i_name LIKE 'af%' "
                        + "GROUP BY  s_i_id,i_price, s_quantity, i_name ORDER BY venta_total desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ5", false, false, customer,
                stock, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ05Crossdata() throws ManifestException {

        init();

        String inputText =
                "[tpcc], SELECT s_i_id,i_price, s_quantity, i_name,count(*) AS numero_pedidos, sum(i_price*s_quantity) AS venta_total "
                        + "FROM tpcc.stock INNER JOIN tpcc.item on i_id=s_i_id WHERE i_name LIKE 'af%' "
                        + "GROUP BY  s_i_id,i_price, s_quantity, i_name ORDER BY venta_total desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ5", false, false, customer,
                stock, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ05Compatibility() throws ManifestException {

        init();


        String implicit = "[tpcc], SELECT s_i_id,i_price, s_quantity, i_name,count(*) AS numero_pedidos, sum(i_price*s_quantity) AS venta_total "
                + "FROM tpcc.stock, tpcc.item WHERE i_id=s_i_id AND i_name LIKE 'af%' "
                + "GROUP BY  s_i_id,i_price, s_quantity, i_name ORDER BY venta_total desc LIMIT 100;";

        String explicit = "[tpcc], SELECT s_i_id,i_price, s_quantity, i_name,count(*) AS numero_pedidos, sum(i_price*s_quantity) AS venta_total "
                + "FROM tpcc.stock INNER JOIN tpcc.item on i_id=s_i_id WHERE i_name LIKE 'af%' "
                + "GROUP BY  s_i_id,i_price, s_quantity, i_name ORDER BY venta_total desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(implicit, "testQ5", false, false, customer, stock, item);
        QueryWorkflow queryWorkflowExplicit = (QueryWorkflow) getPlannedQuery(explicit, "testQ5", false, false, customer, stock, item);

        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), queryWorkflowExplicit.getWorkflow().getSqlDirectQuery());

        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ06() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + "   SELECT s_w_id,count(*) FROM tpcc.stock"
                + " WHERE s_quantity>85 AND "
                + " s_quantity <= 115 GROUP BY s_w_id;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ6", false, false, stock);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ07() throws ManifestException {

        init();

        String inputText = "[tpcc],"
                + "   SELECT max(ol_amount) FROM tpcc.order_line;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ7", false, false, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ08() throws ManifestException {

        init();

        String inputText = "[tpcc],       SELECT max(ol_amount),max(ol_quantity) FROM tpcc.order_line;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ8", false, false, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ09() throws ManifestException {

        init();

        String inputText = "[tpcc],  "
                        + "  SELECT day(h_date), avg(h_amount) "
                        + "FROM tpcc.history WHERE h_c_w_id=245 GROUP BY day(h_date) ORDER BY day(h_date);";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ9", false, false, history);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());

    }

    @Test
    public void testQ09Crossdata() throws ManifestException {

        init();

        String inputText = "[tpcc],  "
                + "  SELECT extract_day(h_date) AS alias_h_day, avg(h_amount) "
                + "FROM tpcc.history WHERE h_c_w_id=245 GROUP BY alias_h_day ORDER BY alias_h_day;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ9", false, false, history);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ10() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT sum(ol_amount) AS revenue  "
                        + "    FROM tpcc.order_line, tpcc.item  "
                        + "    WHERE (  "
                        + "                    ol_i_id = i_id  "
                        + "                    AND i_data LIKE '%a'  "
                        + "                    AND ol_quantity >= 4  "
                        + "    AND ol_quantity <= 9  "
                        + "    AND i_price between 1 AND 400000  "
                        + "    AND ol_w_id in [1,2,3]  "
                        + "                    ) or (  "
                        + "                    ol_i_id = i_id  "
                        + "                    AND i_data LIKE '%b'  "
                        + "                    AND ol_quantity >= 4  "
                        + "    AND ol_quantity <= 9  "
                        + "    AND i_price between 1 AND 400000  "
                        + "    AND ol_w_id in [1,2,4]  "
                        + "                    ) or (  "
                        + "                    ol_i_id = i_id  "
                        + "                    AND i_data LIKE '%c'  "
                        + "                    AND ol_quantity >= 4  "
                        + "    AND ol_quantity <= 9  "
                        + "    AND i_price between 1 AND 400000  "
                        + "    AND ol_w_id in [1,5,3]  "
                        + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer, order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }


    @Test
    public void testQ10BasicOr() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT sum(ol_amount) AS revenue  "
                + "    FROM tpcc.order_line INNER JOIN  tpcc.item ON ol_i_id = i_id  "
                + "    WHERE i_data LIKE '%a' or (i_data LIKE '%b' or i_data LIKE '%c');";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer,
                order_line, item);
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }



    @Test
    public void testQ10Rewrite() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT sum(ol_amount) AS revenue  "
                + "    FROM tpcc.order_line INNER JOIN  tpcc.item ON ol_i_id = i_id  "
                + "    WHERE ( i_data LIKE '%a' AND ol_quantity >= 4  AND ol_quantity <= 9  AND i_price between 1 AND 400000 AND ol_w_id in [1,2,3])" +
                  "       or ( i_data LIKE '%b' AND ol_quantity >= 4  AND ol_quantity <= 9  AND i_price between 1 AND 400000 AND ol_w_id in [1,2,4])" +
                  "       or ( i_data LIKE '%c' AND ol_quantity >= 4  AND ol_quantity <= 9  AND i_price between 1 AND 400000 AND ol_w_id in [1,5,3]);";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer,
                order_line, item);
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ10WithoutOr() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT sum(ol_amount) AS revenue  "
                + "    FROM tpcc.order_line, tpcc.item  "
                + "    WHERE (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%a'  "
                + "                    AND ol_quantity >= 4  "
                + "    AND ol_quantity <= 9  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in [1,2,3]  "
                + "                    ) or (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%b'  "
                + "                    AND ol_quantity >= 4  "
                + "    AND ol_quantity <= 9  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in [1,2,4]  "
                + "                    );";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer, order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }







    @Test
    public void testQ10AndInsteadOfOr() throws ManifestException {

        init();

        String inputText =  "[tpcc],  SELECT sum(ol_amount) AS revenue  "
                + "    FROM tpcc.order_line, tpcc.item  "
                + "    WHERE (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%a'  "
                + "                    AND ol_quantity >= 4  "
                + "    AND ol_quantity <= 9  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in [1,2,3]  "
                + "                    ) and (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%b'  "
                + "                    AND ol_quantity >= 4  "
                + "    AND ol_quantity <= 9  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in [1,2,4]  "
                + "                    ) and (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%c'  "
                + "                    AND ol_quantity >= 4  "
                + "    AND ol_quantity <= 9  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in [1,5,3]  "
                + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer, order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }


    @Test
    public void testQ10Easy() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT sum(ol_amount) AS revenue  "
                + "    FROM tpcc.order_line  "
                + "    WHERE (  "
                + "                    ol_quantity >= 4  "
                + "                    ) or (  "
                + "                    ol_quantity >= 4  "
                + "                    ) or (  "
                + "                    ol_quantity >= 4  "
                + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer, order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }


    @Test
    public void testQ11() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + " SELECT ol_number,  "
                + "    sum(ol_quantity) AS sum_qty,  "
                + "    sum(ol_amount) AS sum_amount,  "
                + "    avg(ol_quantity) AS avg_qty,  "
                + "    avg(ol_amount) AS avg_amount,  "
                + "    count(*) AS count_order  "
                + "    FROM tpcc.order_line  "
                + "    WHERE ol_delivery_d > to_date('2013-10-05','YYYY-MM-DD')  "
                + "    GROUP BY ol_number  ORDER BY sum(ol_quantity) desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ11", false, false, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ12() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + "  SELECT  ol_o_id, ol_w_id, ol_d_id, "
                + "    sum(ol_amount) AS revenue, o_entry_d  "
                + "    FROM tpcc.customer INNER JOIN  tpcc.new_order ON c_id = o_c_id AND c_w_id = o_w_id AND c_d_id = o_d_id"
                + "    INNER JOIN tpcc.order ON no_w_id = o_w_id AND no_d_id = o_d_id AND no_o_id = o_id "
                + "    INNER JOIN tpcc.order_line ON ol_w_id = o_w_id AND ol_d_id = o_d_id AND ol_o_id = o_id"
                + "    WHERE c_state LIKE 'A%'  "
                + "    AND o_entry_d >  to_date('2013-07-24','YYYY-MM-DD')  "
                + "    GROUP BY ol_o_id, ol_w_id, ol_d_id, o_entry_d  "
                + "    ORDER BY revenue DESC, o_entry_d limit 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ12", false, false, customer,
                new_order, order, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ13() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT o_ol_cnt,   "
                + "    sum(CASE WHEN o_carrier_id = 1 or o_carrier_id = 2 THEN 1 ELSE 0 END) AS high_line_count,   "
                + "    sum(CASE WHEN o_carrier_id <> 1 AND o_carrier_id <> 2 THEN 1 ELSE 0 END) AS low_line_count   "
                + "    FROM  tpcc.order INNER JOIN tpcc.order_line ON   "
                + "    ol_w_id = o_w_id   "
                + "    AND ol_d_id = o_d_id   "
                + "    AND ol_o_id = o_id   "
                + "    AND o_entry_d <= ol_delivery_d "
                + "    WHERE ol_delivery_d <  to_date('2013-07-09','YYYY-MM-DD')   "
                + "    GROUP BY o_ol_cnt   "
                + "    ORDER BY sum(CASE WHEN o_carrier_id = 1 or o_carrier_id = 2 THEN 1 ELSE 0 END) DESC, sum(CASE WHEN o_carrier_id <> 1 AND o_carrier_id <> 2 THEN 1 ELSE 0 END)"
                + "    LIMIT 10;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ13", false, false, customer,
                order, order_line);

        LOG.info("SQL Direct: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }


    @Test
    public void testQ14() throws ManifestException {

        init();

        String inputText = "[tpcc], select c_count, count(*) as custdist " +
                "    from " +
                "(" +
                "           select c_id, count(o_id) AS c_count " +
                "           from tpcc.customer left outer join tpcc.order on ( " +
                "                    c_w_id = o_w_id " +
                "                    and c_d_id = o_d_id " +
                "                    and c_id = o_c_id " +
                "                    and o_carrier_id > 6 )" +
                "    group by c_id" +
                ") as c_orders " +
                "    group by c_count " +
                "    order by custdist desc, c_count desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14", false, false, customer, order);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }


    @Test
    public void testQ14WithoutParenthesisWhereAdded() throws ManifestException {

        init();

        String inputText = "[tpcc], select c_count, count(*) as custdist " +
                "    from " +
                "(" +
                "           select c_id, count(o_id) AS c_count " +
                "           from tpcc.customer left outer join tpcc.order on  " +
                "                    c_w_id = o_w_id " +
                "                    and c_d_id = o_d_id " +
                "                    and c_id = o_c_id " +
                "                    where o_carrier_id > 6 " +
                "    group by c_id" +
                ") as c_orders " +
                "    group by c_count " +
                "    order by custdist desc, c_count desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14", false, false, customer, order);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }

    @Test
    public void testQ14Easy() throws ManifestException {

        init();

        String inputText = "[tpcc], select c_count, count(*) as custdist " +
                "    from " +
                "(" +
                "           select c_id, count(o_id) AS c_count " +
                "           from tpcc.customer " +
                "    group by c_id" +
                ") as c_orders " +
                "    group by c_count " +
                "    order by custdist desc, c_count desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14", false, false, customer, order);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }



    @Test
    public void testQ15() throws ManifestException {

        init();

        String inputText = "[tpcc], select 100.00 * sum(case when i_data like 'a%' then ol_amount else 0 end) / (1+sum(ol_amount)) as promo_revenue  " +
                "    from tpcc.order_line, tpcc.item  " +
                "    where ol_i_id = i_id   " +
                "    and ol_delivery_d >= to_date('2013-02-19','YYYY-MM-DD')  " +
                "    and ol_delivery_d < to_date('2013-04-19','YYYY-MM-DD'); ";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14Easy", false, false, order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT NOT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
    }




    @Test
    public void testQ16() throws ManifestException {

        init();

        String inputText = "[tpcc], Select	sum(ol_amount) / 2.0 as avg_yearly	" +
                    "from (select i_id, " +
                    "avg(ol_quantity) as a " +
                    "from item, order_line " +
                    "where i_data like '%b' " +
                    "and ol_i_id = i_id " +
                    "group by i_id) t " +
                    "INNER JOIN order_line ON ol_i_id = t.i_id AND ol_quantity < t.a;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ16", false, false, order,
                order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());

        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), "SELECT sum(tpcc.order_line.ol_amount) / 2.0 AS avg_yearly FROM ( SELECT tpcc.item.i_id, avg(tpcc.order_line.ol_quantity) AS a FROM tpcc.item INNER JOIN tpcc.order_line ON tpcc.order_line.ol_i_id = tpcc.item.i_id WHERE tpcc.item.i_data LIKE '%b' GROUP BY tpcc.item.i_id ) AS t INNER JOIN tpcc.order_line ON tpcc.order_line.ol_i_id = t.i_id AND tpcc.order_line.ol_quantity < t.a");
    }

    @Test
    public void testQ17() throws ManifestException {

        init();

        String inputText = "[tpcc], select substr(i_name,1,3) ," +
                "i_price, " +
                "s_quantity, " +
                "count(*) as numero_pedidos, " +
                "sum(i_price*s_quantity) as venta_total " +
                "from stock inner join item on i_id=s_i_id " +
                "where i_id= 5 " +
                "group by substr(i_name,1,3), s_i_id, i_price, s_quantity " +
                "order by sum(i_price*s_quantity) desc " +
                "limit 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ17", false, false, stock , item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");

        // SQL DIRECT REVIEWED
        LOG.info("SQL DIRECT: " + queryWorkflow.getWorkflow().getSqlDirectQuery());
        Assert.assertEquals( queryWorkflow.getWorkflow().getSqlDirectQuery(), "SELECT substr(tpcc.item.i_name, 1, 3) AS substr, tpcc.item.i_price, tpcc.stock.s_quantity, count(*) AS numero_pedidos, sum(tpcc.item.i_price * tpcc.stock.s_quantity) AS venta_total FROM tpcc.stock INNER JOIN tpcc.item ON tpcc.item.i_id = tpcc.stock.s_i_id WHERE tpcc.item.i_id = 5 GROUP BY substr(tpcc.item.i_name, 1, 3), tpcc.stock.s_i_id, tpcc.item.i_price, tpcc.stock.s_quantity ORDER BY sum(tpcc.item.i_price * tpcc.stock.s_quantity) DESC LIMIT 100");
    }



}

