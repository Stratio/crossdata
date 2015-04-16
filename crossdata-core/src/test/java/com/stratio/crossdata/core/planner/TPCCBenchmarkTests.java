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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class TPCCBenchmarkTests extends PlannerBaseTest {

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

        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector1", dataStoreName,
                clusterWithDefaultPriority, operationsC1, "actorRef1", functions1);

        clusterName = MetadataManagerTestHelper.HELPER
                .createTestCluster(strClusterName, dataStoreName, connector1.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("tpcc").getName();
        createTestTables(catalogName);
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
    }

    @Test

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
                "AND days_between (o.o_entry_d, ol.ol_delivery_d)>30 " +
                "GROUP BY c.c_state, days_between(o.o_entry_d, ol.ol_delivery_d) " +
                "ORDER BY count( *)desc " +
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

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText3, "testQ4RWI", false, false, order,
                customer);
        assertTrue(true);

        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
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
    }

    @Test
    public void testQ08() throws ManifestException {

        init();

        String inputText = "[tpcc],       SELECT max(ol_amount),max(ol_quantity) FROM tpcc.order_line;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ8", false, false, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
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
    }

    @Test
    public void testQ10() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT sum(ol_amount) AS revenue  "
                + "    FROM tpcc.order_line, tpcc.item  "
                + "    WHERE (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%a'  "
                + "                    AND ol_quantity >= ${random(1,5)}  "
                + "    AND ol_quantity <= ${random(1,5)+5}  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in (1,2,3)  "
                + "                    ) or (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%b'  "
                + "                    AND ol_quantity >= ${random(1,5)}  "
                + "    AND ol_quantity <= ${random(1,5)+5}  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in (1,2,4)  "
                + "                    ) or (  "
                + "                    ol_i_id = i_id  "
                + "                    AND i_data LIKE '%c'  "
                + "                    AND ol_quantity >= ${random(1,5)}  "
                + "    AND ol_quantity <= ${random(1,5)+5}  "
                + "    AND i_price between 1 AND 400000  "
                + "    AND ol_w_id in (1,5,3)  "
                + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer,
                order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
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
    }

    @Test
    public void testQ12() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + "  SELECT  ol_o_id, ol_w_id, ol_d_id, "
                + "    sum(ol_amount) AS revenue, o_entry_d  "
                + "    FROM tpcc.customer, tpcc.new_order, tpcc.order, tpcc.order_line  "
                + "    WHERE   c_state LIKE 'A%'  "
                + "    AND c_id = o_c_id  "
                + "    AND c_w_id = o_w_id  "
                + "    AND c_d_id = o_d_id  "
                + "    AND no_w_id = o_w_id  "
                + "    AND no_d_id = o_d_id  "
                + "    AND no_o_id = o_id  "
                + "    AND ol_w_id = o_w_id  "
                + "    AND ol_d_id = o_d_id  "
                + "    AND ol_o_id = o_id  "
                + "    AND o_entry_d >  to_date('2013-07-24','YYYY-MM-DD')  "
                + "    GROUP BY ol_o_id, ol_w_id, ol_d_id, o_entry_d  "
                + "    ORDER BY revenue DESC, o_entry_d limit 100";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ12", false, false, customer,
                new_order, order, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ13WithoutRewrite() throws ManifestException {

        init();

        String inputText = "[tpcc],  SELECT top 10 o_ol_cnt,   "
                + "    sum(CASE WHEN o_carrier_id = 1 or o_carrier_id = 2 THEN 1 ELSE 0 END) AS high_line_count,   "
                + "    sum(CASE WHEN o_carrier_id <> 1 AND o_carrier_id <> 2 THEN 1 ELSE 0 END) AS low_line_count   "
                + "    FROM  tpcc.order, tpcc.order_line   "
                + "    WHERE  ol_w_id = o_w_id   "
                + "    AND ol_d_id = o_d_id   "
                + "    AND ol_o_id = o_id   "
                + "    AND o_entry_d <= ol_delivery_d   "
                + "    AND ol_delivery_d <  to_date('2013-07-09','YYYY-MM-DD')   "
                + "    GROUP BY o_ol_cnt   "
                + "    ORDER BY sum(CASE WHEN o_carrier_id = 1 or o_carrier_id = 2 THEN 1 ELSE 0 END) DESC, sum(CASE WHEN o_carrier_id <> 1 AND o_carrier_id <> 2 THEN 1 ELSE 0 END);";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ13", false, false, customer,
                order, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    /*
    @Test
    public void testQ14() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                    + "100.00 * sum(CASE  "
                    + "WHEN p_type LIKE 'PROMO%'  "
                    + "THEN l_extendedprice*(1-l_discount)  "
                    + "ELSE 0  "
                    + "END) / sum(l_extendedprice * (1 - l_discount)) AS promo_revenue  "
                    + "FROM  "
                    + "lineitem,  "
                    + "part  "
                    + "WHERE "
                    + "l_partkey = p_partkey  "
                    + "AND l_shipdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "AND l_shipdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"month\") ;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14", false, false, lineitem, part);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ15() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "100.00 * sum (CASE WHEN p_type LIKE 'PROMO%' THEN l_extendedprice*(1-l_discount ELSE 0 END) "
                        + "FROM  "
                        + "lineitem;  ";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14Easy", false, false, lineitem, part);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ16() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "p_brand,  "
                        + "p_type,  "
                        + "p_size,  "
                        + "count(DISTINCT ps_suppkey) AS supplier_cnt  "
                        + "FROM  "
                        + "partsupp,  "
                        + "part  "
                        + "WHERE "
                        + "p_partkey = ps_partkey  "
                        + "AND p_brand <> 'Brand#45'  "
                        + "AND p_type NOT LIKE \"MEDIUM POLISHED%\"  "
                        + "AND p_size IN (49, 14, 23, 45, 19, 3, 36, 9)  "
                        + "AND ps_suppkey NOT IN (  "
                        + "SELECT  "
                        + "s_suppkey  "
                        + "FROM  "
                        + "supplier  "
                        + "WHERE "
                        + "s_comment LIKE \"%Customer%Complaints%\"  "
                        + ")  "
                        + "GROUP BY  "
                        + "p_brand,  "
                        + "p_type,  "
                        + "p_size  "
                        + "ORDER BY  "
                        + "supplier_cnt DESC,  "
                        + "p_brand,  "
                        + "p_type,  "
                        + "p_size;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ16", false, false, partsupp, part, supplier);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ17() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "sum(l_extendedprice) / 7.0 AS avg_yearly  "
                        + "FROM  "
                        + "lineitem,  "
                        + "part  "
                        + "WHERE "
                        + "p_partkey = l_partkey  "
                        + "AND p_brand = 'Brand#23'  "
                        + "AND p_container = 'MED BOX'  "
                        + "AND l_quantity < (  "
                        + "SELECT  "
                        + "0.2 * avg(l_quantity)  "
                        + "FROM  "
                        + "lineitem  "
                        + "WHERE "
                        + "l_partkey = p_partkey  "
                        + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ17", false, false, lineitem , part);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

   */

}

