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
import com.stratio.crossdata.common.executionplan.QueryWorkflow;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.metadata.MetadataManager;

public class TPCCBenchmarkConnectorsTests extends PlannerBaseTest {

    private static final Logger LOG = Logger.getLogger(TPCCBenchmarkConnectorsTests.class);
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
        sumFunction.setSignature("sum(Tuple[Any*]):Tuple[Any]");
        sumFunction.setFunctionType("aggregation");
        sumFunction.setDescription("Total sum");
        functions1.add(sumFunction);
        // AVG function
        FunctionType avgFunction = new FunctionType();
        avgFunction.setFunctionName("avg");
        avgFunction.setSignature("avg(Tuple[Any*]):Tuple[Any]");
        avgFunction.setFunctionType("aggregation");
        avgFunction.setDescription("Average");
        functions1.add(avgFunction);
        // COUNT function
        FunctionType countFunction = new FunctionType();
        countFunction.setFunctionName("count");
        countFunction.setSignature("count(Tuple[Any*]):Tuple[Any]");
        countFunction.setFunctionType("aggregation");
        countFunction.setDescription("Count");
        functions1.add(countFunction);
        // SUBSTR function
        FunctionType substringFunction = new FunctionType();
        substringFunction.setFunctionName("substr");
        substringFunction.setSignature("substr(Tuple[Text,Int,Int]):Tuple[Text]");
        substringFunction.setFunctionType("simple");
        substringFunction.setDescription("substring");
        functions1.add(substringFunction);
        // SUBSTRING function
        FunctionType substring2Function = new FunctionType();
        substring2Function.setFunctionName("substr");
        substring2Function.setSignature("substring(Tuple[Text,Int,Int]):Tuple[Text]");
        substring2Function.setFunctionType("simple");
        substring2Function.setDescription("substring");
        functions1.add(substring2Function);
        // MAX function
        FunctionType maxFunction = new FunctionType();
        maxFunction.setFunctionName("max");
        maxFunction.setSignature("max(Tuple[Any*]):Tuple[Any]");
        maxFunction.setFunctionType("aggregation");
        maxFunction.setDescription("maximum");
        functions1.add(maxFunction);
        // extract_day function
        FunctionType extractDayFunction = new FunctionType();
        extractDayFunction.setFunctionName("day");
        extractDayFunction.setSignature("day(Tuple[Any]):Tuple[Any]");
        extractDayFunction.setFunctionType("simple");
        extractDayFunction.setDescription("extract day from date");
        functions1.add(extractDayFunction);

        // extract_day year
        FunctionType yearFunction = new FunctionType();
        yearFunction.setFunctionName("year");
        yearFunction.setSignature("year(Tuple[Any]):Tuple[Any]");
        yearFunction.setFunctionType("simple");
        yearFunction.setDescription("extract year from date");
        functions1.add(yearFunction);

        // extract_day month
        FunctionType monthFunction = new FunctionType();
        monthFunction.setFunctionName("month");
        monthFunction.setSignature("month(Tuple[Any]):Tuple[Any]");
        monthFunction.setFunctionType("simple");
        monthFunction.setDescription("extract month from date");
        functions1.add(monthFunction);

        // DAYS_BETWEEN function
        FunctionType days_between = new FunctionType();
        days_between.setFunctionName("datediff");
        days_between.setSignature("datediff(Tuple[Any,Any]):Tuple[Int]");
        days_between.setFunctionType("simple");
        days_between.setDescription("datediff");
        functions1.add(days_between);

        // to_date function
        FunctionType toDateFunction = new FunctionType();
        toDateFunction.setFunctionName("to_date");
        toDateFunction.setSignature("to_date(Tuple[Any]):Tuple[Any]");
        toDateFunction.setFunctionType("simple");
        toDateFunction.setDescription("to_date");
        functions1.add(toDateFunction);

        // to_date function
        FunctionType toAddFunction = new FunctionType();
        toAddFunction.setFunctionName("date_add");
        toAddFunction.setSignature("date_add(Tuple[Any,Int]):Tuple[Any]");
        toAddFunction.setFunctionType("simple");
        toAddFunction.setDescription("Returns the date after the addiction");
        functions1.add(toAddFunction);

        // date_sub function
        FunctionType date_sub = new FunctionType();
        date_sub.setFunctionName("date_sub");
        date_sub.setSignature("date_sub(Tuple[Any,Int]):Tuple[Any]");
        date_sub.setFunctionType("simple");
        date_sub.setDescription("date_sub");
        functions1.add(date_sub);

        // min
        FunctionType min = new FunctionType();
        min.setFunctionName("min");
        min.setSignature("min(Tuple[Any*]):Tuple[Any]");
        min.setFunctionType("aggregation");
        min.setDescription("Minimum");
        functions1.add(min);

        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector1", dataStoreName,
                clusterWithDefaultPriority, operationsC1, "actorRef1", functions1);

        clusterName = MetadataManagerTestHelper.HELPER
                .createTestCluster(strClusterName, dataStoreName, connector1.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("testmetastore").getName();
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
        createTestTables(catalogName, "warehouse", "district", "customer", "history", "new_order", "orders",
                "order_line", "item", "stock");
    }

    public void createTestTables(CatalogName catalogName, String... tableNames) {
        int i = 0;

        //warehouse
        String[] columnNames4 = { "w_id", "w_name", "w_street_1", "w_street_2", "w_city", "w_state", "w_zip",
                "w_tax", "w_ytd" };
        ColumnType[] columnTypes4 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.FLOAT)
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
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.FLOAT)
        };
        String[] partitionKeys5 = { "d_w_id" };
        String[] clusteringKeys5 = { };
        district = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames5, columnTypes5, partitionKeys5, clusteringKeys5, null);

        //customer
        String[] columnNames6 = { "C_D", "C_D_ID", "C_W_ID", "C_FIRST", "C_MIDDLE", "C_LAST", "C_STREET_1",
                "C_STREET_2", "C_CITY", "C_STATE", "C_ZIP", "C_PHONE", "C_SINCE", "C_CREDIT", "C_CREDIT_LIM",
                "C_DISCOUNT", "C_BALANCE", "C_YTD_PAYMENT", "C_PAYMENT_CNT", "C_DELYVERY_CNT", "C_DATA" };
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
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT)

        };
        String[] partitionKeys6 = { "C_D" };
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
                new ColumnType(DataType.FLOAT),
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

        //orders
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
        String[] columnNames11 = { "ol_o_id", "ol_d_id", "ol_w_id", "ol_number", "ol_id", "ol_supply_w_id",
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
        String[] partitionKeys11 = { "ol_o_id" };
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
                new ColumnType(DataType.FLOAT),
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

    @Test
    public void testQ00() throws ManifestException {

        init();
        String inputText =
                "[testmetastore], SELECT substr(C_STATE,1,1) AS country, count(*) AS numcust,  " +
                        "sum(C_BALANCE) AS totacctbal FROM (SELECT avg(sub.C_BALANCE) AS balance  FROM testmetastore" +
                        ".customer sub WHERE  sub.C_BALANCE > 0.00 AND substr(sub.C_PHONE,1,1)  IN ['1','2','3','4'," +
                        "'5','6','7']) y INNER JOIN testmetastore.customer ON C_BALANCE > y.balance  WHERE substr" +
                        "(C_PHONE,1,1) IN ['1','2','3','4','5','6','7'] GROUP BY substring(C_STATE,1," +
                        "1)  ORDER BY country;";

        String expectedValue = "SELECT substr(testmetastore.customer.C_STATE, 1, 1) AS country, count(*) AS numcust," +
                " sum(testmetastore.customer.C_BALANCE) AS totacctbal FROM ( SELECT avg(sub.C_BALANCE) AS balance " +
                "FROM" +
                " testmetastore.customer AS sub WHERE sub.C_BALANCE > 0.0 AND substr(sub.C_PHONE, 1, 1) IN ('1', '2'," +
                " '3', '4', '5', '6', '7') ) AS y INNER JOIN testmetastore.customer ON testmetastore.customer" +
                ".C_BALANCE > y.balance WHERE substr(testmetastore.customer.C_PHONE, 1, 1) IN ('1', '2', '3', '4', " +
                "'5', '6', '7') GROUP BY substring(testmetastore.customer.C_STATE, 1, 1) ORDER BY country";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ00Hive", false, false,
                customer);

        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue);

    }

    @Test
    public void testQ01() throws ManifestException {

        init();

        String inputText = "[testmetastore], select ol_o_id, ol_d_id,ol_w_id,sum(ol_quantity)," +
                "avg(ol_quantity) as avq_quantity,sum(ol_amount) as suma,avg(ol_amount) as avg_amount," +
                "count(*)  from testmetastore.order_line where ol_d_id=4 and ol_w_id=175 group by ol_o_id, ol_d_id," +
                "ol_w_id order by suma desc;";
        String expectedValue="SELECT testmetastore.order_line.ol_o_id, testmetastore.order_line.ol_d_id, " +
                "testmetastore.order_line.ol_w_id, sum(testmetastore.order_line.ol_quantity) AS sum, " +
                "avg(testmetastore.order_line.ol_quantity) AS avq_quantity, sum(testmetastore.order_line.ol_amount) " +
                "AS suma, avg(testmetastore.order_line.ol_amount) AS avg_amount, count(*) AS count FROM testmetastore" +
                ".order_line WHERE testmetastore.order_line.ol_d_id = 4 AND testmetastore.order_line.ol_w_id = 175 " +
                "GROUP BY testmetastore.order_line.ol_o_id, testmetastore.order_line.ol_d_id, " +
                "testmetastore.order_line.ol_w_id ORDER BY suma DESC";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ01Hive", false, false,
                order_line);

        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ02() throws ManifestException {

        init();

        String inputText = "[testmetastore], SELECT OL.ol_w_id, OL.ol_d_id, OL.ol_o_id, AVG_Amoun, " +
                "avg(OL.ol_amount) AS average FROM (SELECT d_id, d_w_id, avg(ol_amount) AS AVG_Amoun FROM " +
                "testmetastore.district D INNER JOIN  testmetastore.order_line OL_A ON D.d_id=OL_A.ol_d_id AND D" +
                ".d_w_id=OL_A.ol_w_id WHERE d_id=3 AND d_w_id=241 GROUP BY d_id,d_w_id ) A INNER JOIN  testmetastore" +
                ".order_line OL ON A.d_id=OL.ol_d_id AND A.d_w_id=OL.ol_w_id WHERE OL.ol_d_id=3 AND OL.ol_w_id=241  " +
                "GROUP BY OL.ol_w_id, OL.ol_d_id, OL.ol_o_id, AVG_Amoun having avg(OL.ol_amount) > AVG_Amoun  ORDER " +
                "BY average desc;";
        String expectedValue="SELECT OL.ol_w_id, OL.ol_d_id, OL.ol_o_id, A.AVG_Amoun, " +
                "avg(OL.ol_amount) AS average FROM ( SELECT D.d_id, D.d_w_id, avg(OL_A.ol_amount) AS AVG_Amoun FROM " +
                "testmetastore.district AS D INNER JOIN testmetastore.order_line AS OL_A ON D.d_id = OL_A.ol_d_id AND" +
                " D.d_w_id = OL_A.ol_w_id WHERE D.d_id = 3 AND D.d_w_id = 241 GROUP BY D.d_id, " +
                "D.d_w_id ) AS A INNER JOIN testmetastore.order_line AS OL ON A.d_id = OL.ol_d_id AND A.d_w_id = OL" +
                ".ol_w_id WHERE OL.ol_d_id = 3 AND OL.ol_w_id = 241 GROUP BY OL.ol_w_id, OL.ol_d_id, OL.ol_o_id, " +
                "A.AVG_Amoun HAVING avg(OL.ol_amount) > A.AVG_Amoun ORDER BY average DESC";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ02", false, false, order_line);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");

    }

    @Test
    public void testQ03() throws ManifestException {

        init();

        String inputText = "[testmetastore], select C_D_ID,C_CREDIT,count(o_id) from testmetastore.customer  inner " +
                "join testmetastore.orders on C_D_ID=o_d_id and C_W_ID=o_w_id and o_c_id=C_D where C_W_ID=168  group" +
                " by C_CREDIT,C_D_ID order by C_D_ID, C_CREDIT;";
        String expectedValue="SELECT testmetastore.customer.C_D_ID, testmetastore.customer.C_CREDIT, " +
                "count(testmetastore.orders.o_id) AS count FROM testmetastore.customer INNER JOIN testmetastore" +
                ".orders ON testmetastore.customer.C_D_ID = testmetastore.orders.o_d_id AND testmetastore.customer" +
                ".C_W_ID = testmetastore.orders.o_w_id AND testmetastore.orders.o_c_id = testmetastore.customer.C_D " +
                "WHERE testmetastore.customer.C_W_ID = 168 GROUP BY testmetastore.customer.C_CREDIT, " +
                "testmetastore.customer.C_D_ID ORDER BY testmetastore.customer.C_D_ID, testmetastore.customer.C_CREDIT";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ3", false, false,
                customer, order);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");

    }

    @Test
    public void testQ04() throws ManifestException {

        init();

        String inputText =
                "[testmetastore], SELECT c.C_STATE, datediff(to_date(o.o_entry_d), to_date(ol.ol_delivery_d)),  " +
                        "sum(ol.ol_amount) , avg(ol.ol_amount), count(*) AS cantidad FROM ( SELECT date_sub(to_date" +
                        "(max(C_SINCE)), 7) AS fecha FROM testmetastore.customer ) y INNER JOIN testmetastore" +
                        ".customer AS c ON C_SINCE >= y.fecha INNER JOIN testmetastore.orders AS o ON  o.o_c_id = c" +
                        ".C_D AND o.o_d_id = c.C_D_ID AND o.o_w_id = c.C_W_ID INNER JOIN testmetastore.order_line AS " +
                        " ol ON  o.o_id = ol.ol_o_id AND o.o_d_id = ol.ol_d_id AND o.o_w_id = ol.ol_w_id WHERE  c" +
                        ".C_W_ID = 100 AND datediff(to_date(o.o_entry_d), to_date(ol.ol_delivery_d)) > 30 GROUP BY  c" +
                        ".C_STATE, datediff(to_date(o.o_entry_d), to_date(ol.ol_delivery_d)) ORDER BY cantidad  DESC " +
                        "LIMIT 10;";

        String expectedValue="SELECT c.C_STATE, datediff(to_date(o.o_entry_d), " +
                "to_date(ol.ol_delivery_d)) AS datediff, sum(ol.ol_amount) AS sum, avg(ol.ol_amount) AS avg, " +
                "count(*) AS cantidad FROM ( SELECT date_sub(to_date(max(testmetastore.customer.C_SINCE)), " +
                "7) AS fecha FROM testmetastore.customer ) AS y INNER JOIN testmetastore.customer AS c ON c.C_SINCE " +
                ">= y.fecha INNER JOIN testmetastore.orders AS o ON o.o_c_id = c.C_D AND o.o_d_id = c.C_D_ID AND o" +
                ".o_w_id = c.C_W_ID INNER JOIN testmetastore.order_line AS ol ON o.o_id = ol.ol_o_id AND o.o_d_id = " +
                "ol.ol_d_id AND o.o_w_id = ol.ol_w_id WHERE c.C_W_ID = 100 AND datediff(to_date(o.o_entry_d), " +
                "to_date(ol.ol_delivery_d)) > 30 GROUP BY c.C_STATE, datediff(to_date(o.o_entry_d), " +
                "to_date(ol.ol_delivery_d)) ORDER BY cantidad DESC LIMIT 10";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ4Crossdata", false, false,
                customer, order);
        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");

    }

    @Test
    public void testQ05() throws ManifestException {

        init();

        String inputText =
                "[testmetastore],select s_i_id,i_price, s_quantity, i_name,count(*) as numero_pedidos, " +
                        "sum(i_price*s_quantity) as venta_total from testmetastore.stock inner join testmetastore" +
                        ".item on i_id=s_i_id where i_name like 'af%' group by s_i_id,i_price, s_quantity, " +
                        "i_name order by venta_total desc LIMIT 100;";

        String expectedValue="SELECT testmetastore.stock.s_i_id, testmetastore.item.i_price, " +
                "testmetastore.stock.s_quantity, testmetastore.item.i_name, count(*) AS numero_pedidos, " +
                "sum(testmetastore.item.i_price * testmetastore.stock.s_quantity) AS venta_total FROM testmetastore" +
                ".stock INNER JOIN testmetastore.item ON testmetastore.item.i_id = testmetastore.stock.s_i_id WHERE " +
                "testmetastore.item.i_name LIKE 'af%' GROUP BY testmetastore.stock.s_i_id, " +
                "testmetastore.item.i_price, testmetastore.stock.s_quantity, testmetastore.item.i_name ORDER BY " +
                "venta_total DESC LIMIT 100";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ5", false, false, customer,
                stock, item);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ06() throws ManifestException {

        init();

        String inputText = "[testmetastore], SELECT s_w_id,count(*) FROM testmetastore.stock where s_quantity>85 and " +
                "s_quantity <= 115 group by s_w_id;";

        String expectedValue="SELECT testmetastore.stock.s_w_id, count(*) AS count FROM testmetastore.stock WHERE " +
                "testmetastore.stock.s_quantity > 85 AND testmetastore.stock.s_quantity <= 115 GROUP BY testmetastore" +
                ".stock.s_w_id";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ6", false, false, stock);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ07() throws ManifestException {

        init();

        String inputText = "[testmetastore],select max(ol_amount) from testmetastore.order_line;";
        String expectedValue="SELECT max(testmetastore.order_line.ol_amount) AS max FROM testmetastore.order_line";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ7", false, false, order_line);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ08() throws ManifestException {

        init();

        String inputText = "[testmetastore], select max(ol_amount) as max_amount, max(ol_quantity) as max_quantity " +
                "from testmetastore.order_line;";
        String expectedValue="SELECT max(testmetastore.order_line.ol_amount) AS max_amount, " +
                "max(testmetastore.order_line.ol_quantity) AS max_quantity FROM testmetastore.order_line";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ8", false, false, order_line);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ09() throws ManifestException {

        init();

        String inputText = "[testmetastore], SELECT day(h_date) as date, avg(h_amount) FROM testmetastore.history " +
                "WHERE h_c_w_id=245 GROUP BY day(h_date) ORDER BY date;";

        String expectedValue="SELECT day(testmetastore.history.h_date) AS date, avg(testmetastore.history.h_amount) " +
                "AS avg FROM testmetastore.history WHERE testmetastore.history.h_c_w_id = 245 GROUP BY day" +
                "(testmetastore.history.h_date) ORDER BY date";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ9", false, false, history);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");

    }

    @Test
    public void testQ10() throws ManifestException {

        init();

        String inputText = "[testmetastore], SELECT sum(ol_amount) AS revenue FROM testmetastore.order_line INNER " +
                "JOIN testmetastore.item ON ol_id = i_id WHERE (i_data LIKE '%a' AND ol_quantity >= 4 AND ol_quantity" +
                " <= 9 AND i_price between 1 AND 400000 AND ol_w_id in [1,2,3]) OR (i_data LIKE '%b'AND ol_quantity " +
                ">= 4 AND ol_quantity <= 9 AND i_price between 1 AND 400000 AND ol_w_id in [1,2," +
                "4]) OR (i_data LIKE '%c' AND ol_quantity >= 4 AND ol_quantity <= 9 AND i_price between 1 AND 400000 " +
                "AND ol_w_id in [1,5,3]);";

        String expectedValue="SELECT sum(testmetastore.order_line.ol_amount) AS revenue FROM testmetastore.order_line" +
                " INNER JOIN testmetastore.item ON testmetastore.order_line.ol_id = testmetastore.item.i_id WHERE " +
                "(testmetastore.item.i_data LIKE '%a' AND testmetastore.order_line.ol_quantity >= 4 AND testmetastore" +
                ".order_line.ol_quantity <= 9 AND testmetastore.item.i_price BETWEEN 1 AND 400000 AND testmetastore" +
                ".order_line.ol_w_id IN (1, 2, 3)) OR (testmetastore.item.i_data LIKE '%b' AND testmetastore" +
                ".order_line.ol_quantity >= 4 AND testmetastore.order_line.ol_quantity <= 9 AND testmetastore.item" +
                ".i_price BETWEEN 1 AND 400000 AND testmetastore.order_line.ol_w_id IN (1, 2, " +
                "4)) OR (testmetastore.item.i_data LIKE '%c' AND testmetastore.order_line.ol_quantity >= 4 AND " +
                "testmetastore.order_line.ol_quantity <= 9 AND testmetastore.item.i_price BETWEEN 1 AND 400000 AND " +
                "testmetastore.order_line.ol_w_id IN (1, 5, 3))";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer,
                order_line, item);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ11() throws ManifestException {

        init();

        String inputText = "[testmetastore], SELECT ol_number, sum(ol_quantity) AS sum_qty, " +
                "sum(ol_amount) AS sum_amount, avg(ol_quantity) AS avg_qty, avg(ol_amount) AS avg_amount, " +
                "count(*) AS count_order FROM testmetastore.order_line WHERE ol_delivery_d > to_date('2013-10-05') " +
                "GROUP BY ol_number ORDER BY sum_qty desc LIMIT 100;";

        String expectedValue="SELECT testmetastore.order_line.ol_number, sum(testmetastore.order_line.ol_quantity) AS" +
                " sum_qty, sum(testmetastore.order_line.ol_amount) AS sum_amount, " +
                "avg(testmetastore.order_line.ol_quantity) AS avg_qty, avg(testmetastore.order_line.ol_amount) AS " +
                "avg_amount, count(*) AS count_order FROM testmetastore.order_line WHERE testmetastore.order_line" +
                ".ol_delivery_d > to_date('2013-10-05') GROUP BY testmetastore.order_line.ol_number ORDER BY sum_qty " +
                "DESC LIMIT 100";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ11", false, false, order_line);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ12() throws ManifestException {

        init();

        String inputText = "[testmetastore], SELECT o_ol_cnt, sum(CASE WHEN o_carrier_id = 1 or o_carrier_id = 2 THEN" +
                " 1 ELSE 0 END) AS high_line_count, sum(CASE WHEN o_carrier_id <> 1 AND o_carrier_id <> 2 THEN 1 ELSE" +
                " 0 END) AS low_line_count FROM testmetastore.orders INNER JOIN testmetastore.order_line ON ol_w_id =" +
                " o_w_id AND ol_d_id = o_d_id AND ol_o_id = o_id AND o_entry_d <= ol_delivery_d WHERE ol_delivery_d <" +
                " to_date('2013-07-09') GROUP BY o_ol_cnt ORDER BY high_line_count DESC, low_line_count LIMIT 10;";

        String expectedValue="SELECT testmetastore.orders.o_ol_cnt, sum(CASE WHEN testmetastore.orders.o_carrier_id =" +
                " 1 OR testmetastore.orders.o_carrier_id = 2 THEN 1 ELSE 0 END) AS high_line_count, " +
                "sum(CASE WHEN testmetastore.orders.o_carrier_id <> 1 AND testmetastore.orders.o_carrier_id <> 2 THEN" +
                " 1 ELSE 0 END) AS low_line_count FROM testmetastore.orders INNER JOIN testmetastore.order_line ON " +
                "testmetastore.order_line.ol_w_id = testmetastore.orders.o_w_id AND testmetastore.order_line.ol_d_id " +
                "= testmetastore.orders.o_d_id AND testmetastore.order_line.ol_o_id = testmetastore.orders.o_id AND " +
                "testmetastore.orders.o_entry_d <= testmetastore.order_line.ol_delivery_d WHERE testmetastore" +
                ".order_line.ol_delivery_d < to_date('2013-07-09') GROUP BY testmetastore.orders.o_ol_cnt ORDER BY " +
                "high_line_count DESC, low_line_count LIMIT 10";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ12", false, false, customer,
                new_order, order, order_line);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ13() throws ManifestException {

        init();

        String inputText = "[testmetastore],  SELECT o_ol_cnt, sum(CASE WHEN o_carrier_id = 1 or o_carrier_id = 2 " +
                "THEN 1 ELSE 0 END) AS high_line_count, sum(CASE WHEN o_carrier_id <> 1 AND o_carrier_id <> 2 THEN 1 " +
                "ELSE 0 END) AS low_line_count FROM testmetastore.orders INNER JOIN testmetastore.order_line ON " +
                "ol_w_id = o_w_id AND ol_d_id = o_d_id AND ol_o_id = o_id AND o_entry_d <= ol_delivery_d WHERE " +
                "ol_delivery_d < to_date('2013-07-09') GROUP BY o_ol_cnt ORDER BY high_line_count DESC, " +
                "low_line_count LIMIT 10;";

        String expectedValue="SELECT testmetastore.orders.o_ol_cnt, sum(CASE WHEN testmetastore.orders.o_carrier_id =" +
                " 1 OR testmetastore.orders.o_carrier_id = 2 THEN 1 ELSE 0 END) AS high_line_count, " +
                "sum(CASE WHEN testmetastore.orders.o_carrier_id <> 1 AND testmetastore.orders.o_carrier_id <> 2 THEN" +
                " 1 ELSE 0 END) AS low_line_count FROM testmetastore.orders INNER JOIN testmetastore.order_line ON " +
                "testmetastore.order_line.ol_w_id = testmetastore.orders.o_w_id AND testmetastore.order_line.ol_d_id " +
                "= testmetastore.orders.o_d_id AND testmetastore.order_line.ol_o_id = testmetastore.orders.o_id AND " +
                "testmetastore.orders.o_entry_d <= testmetastore.order_line.ol_delivery_d WHERE testmetastore" +
                ".order_line.ol_delivery_d < to_date('2013-07-09') GROUP BY testmetastore.orders.o_ol_cnt ORDER BY " +
                "high_line_count DESC, low_line_count LIMIT 10";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ13", false, false, customer,
                order, order_line);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ14() throws ManifestException {

        init();

        String inputText = "[testmetastore], select c_count, count(*) as custdist from (select C_D, " +
                "count(o_id) AS c_count from testmetastore.customer left outer join testmetastore.orders on (C_W_ID =" +
                " o_w_id and C_D_ID = o_d_id and C_D = o_c_id) where o_carrier_id > 6 group by C_D) as c_orders group" +
                " by c_count order by custdist desc, c_count desc LIMIT 100;";

        String expectedValue ="SELECT c_orders.c_count, count(*) AS custdist FROM ( SELECT testmetastore.customer" +
                ".C_D, count(testmetastore.orders.o_id) AS c_count FROM testmetastore.customer LEFT OUTER JOIN " +
                "testmetastore.orders ON testmetastore.customer.C_W_ID = testmetastore.orders.o_w_id AND " +
                "testmetastore.customer.C_D_ID = testmetastore.orders.o_d_id AND testmetastore.customer.C_D = " +
                "testmetastore.orders.o_c_id WHERE testmetastore.orders.o_carrier_id > 6 GROUP BY testmetastore" +
                ".customer.C_D ) AS c_orders GROUP BY c_orders.c_count ORDER BY custdist DESC, " +
                "c_orders.c_count DESC LIMIT 100";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14", false, false, customer,
                order);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ15() throws ManifestException {

        init();

        String inputText =
                "[testmetastore], select 100.00 * sum(case when i_data like 'a%' then ol_amount else 0 end) / (1+sum" +
                        "(ol_amount)) as promo_revenue from testmetastore.order_line, " +
                        "testmetastore.item where ol_id = i_id  and ol_delivery_d >= to_date('2013-02-19') and " +
                        "ol_delivery_d < to_date('2013-04-19');";

        String expectedValue="SELECT 100.0 * sum(CASE WHEN testmetastore.item.i_data LIKE 'a%' THEN testmetastore" +
                ".order_line.ol_amount ELSE 0 END) / (1 + sum(testmetastore.order_line.ol_amount)) AS promo_revenue " +
                "FROM testmetastore.order_line INNER JOIN testmetastore.item ON testmetastore.order_line.ol_id = " +
                "testmetastore.item.i_id WHERE testmetastore.order_line.ol_delivery_d >= to_date('2013-02-19') AND " +
                "testmetastore.order_line.ol_delivery_d < to_date('2013-04-19')";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14Easy", false, false,
                order_line, item);

        // SQL DIRECT NOT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");
    }

    @Test
    public void testQ16() throws ManifestException {

        init();

        String inputText = "[testmetastore], Select sum(ol_amount) / 2.0 as avg_yearly from (select i_id, " +
                "avg(ol_quantity) as a from testmetastore.item, testmetastore.order_line where i_data like '%b' and " +
                "ol_id = i_id group by i_id) t INNER JOIN testmetastore.order_line ON ol_id = t.i_id AND ol_quantity " +
                "<= t.a;";

        String expectedValue="SELECT sum(testmetastore.order_line.ol_amount) / 2.0 AS avg_yearly FROM ( SELECT " +
                "testmetastore.item.i_id, avg(testmetastore.order_line.ol_quantity) AS a FROM testmetastore.item " +
                "INNER JOIN testmetastore.order_line ON testmetastore.order_line.ol_id = testmetastore.item.i_id " +
                "WHERE testmetastore.item.i_data LIKE '%b' GROUP BY testmetastore.item.i_id ) AS t INNER JOIN " +
                "testmetastore.order_line ON testmetastore.order_line.ol_id = t.i_id AND testmetastore.order_line" +
                ".ol_quantity <= t.a";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ16", false, false, order,
                order_line, item);

        // SQL DIRECT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");

    }

    @Test
    public void testQ17() throws ManifestException {

        init();

        String inputText = "[testmetastore], select substr(i_name,1,3), i_price, s_quantity, " +
                "count(*) as numero_pedidos, sum(i_price*s_quantity) as venta_total from testmetastore.stock inner " +
                "join testmetastore.item on i_id=s_i_id where i_id= 5 group by substr(i_name,1,3), s_i_id, i_price, " +
                "s_quantity order by venta_total desc limit 100;";

        String expectedValue="SELECT substr(testmetastore.item.i_name, 1, 3) AS substr, testmetastore.item.i_price, " +
                "testmetastore.stock.s_quantity, count(*) AS numero_pedidos, sum(testmetastore.item.i_price * " +
                "testmetastore.stock.s_quantity) AS venta_total FROM testmetastore.stock INNER JOIN testmetastore" +
                ".item ON testmetastore.item.i_id = testmetastore.stock.s_i_id WHERE testmetastore.item.i_id = 5 " +
                "GROUP BY substr(testmetastore.item.i_name, 1, 3), testmetastore.stock.s_i_id, " +
                "testmetastore.item.i_price, testmetastore.stock.s_quantity ORDER BY venta_total DESC LIMIT 100";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ17", false, false, stock, item);

        // SQL DIRECT REVIEWED
        Assert.assertEquals(queryWorkflow.getWorkflow().getSqlDirectQuery(), expectedValue, "Sql direct Expected is not the same as sql direct obtained");

    }

}

