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
        operationsC1.add(Operations.FILTER_NON_INDEXED_BETWEEN);
        operationsC1.add(Operations.FILTER_NON_INDEXED_NOT_LIKE);
        operationsC1.add(Operations.FILTER_NON_INDEXED_LIKE);
        operationsC1.add(Operations.FILTER_NON_INDEXED_GET);
        operationsC1.add(Operations.FILTER_NON_INDEXED_LT);
        operationsC1.add(Operations.FILTER_PK_EQ);
        operationsC1.add(Operations.SELECT_INNER_JOIN);
        operationsC1.add(Operations.SELECT_LIMIT);
        operationsC1.add(Operations.FILTER_NON_INDEXED_GT);

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
        substringFunction.setFunctionName("substring");
        substringFunction.setSignature("substring(Tuple[String,Int,Int]):Tuple[String]");
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

        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector("TestConnector1", dataStoreName,
                clusterWithDefaultPriority, operationsC1, "actorRef1", functions1);

        clusterName = MetadataManagerTestHelper.HELPER.createTestCluster(strClusterName, dataStoreName, connector1.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("tpcc").getName();
        createTestTables(catalogName);
    }

    @AfterClass
    public void tearDown(){
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    public void createTestTables(CatalogName catalogName) {
        createTestTables(catalogName, "warehouse", "district", "customer", "history", "new_order", "order", "order_line", "item", "stock");
    }


    public void createTestTables(CatalogName catalogName, String... tableNames) {
        int i = 0;

        //warehouse
        String[] columnNames4 = { "w_id", "w_name", "w_street_1", "w_street_2", "w_city", "w_state", "w_zip", "w_tax", "w_ytd" };
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
        warehouse = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames4, columnTypes4, partitionKeys4, clusteringKeys4, null);

        //district
        String[] columnNames5 = { "d_id", "d_w_id", "d_name", "d_street_1", "d_street_2", "d_city", "d_state", "d_zip", "d_tax", "d_ytd", "d_next_o_id" };
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
        String[] clusteringKeys5 = { "d_d_id"};
        district = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames5, columnTypes5, partitionKeys5, clusteringKeys5, null);

        //customer
        String[] columnNames6 = { "c_id", "c_d_id", "c_w_id", "c_first", "c_middle", "c_last", "c_street_1", "c_street_2", "c_city", "c_state", "c_zip", "c_phone", "c_since",  "c_credit", "c_credit_lim", "c_discount", "c_balance", "c_ytd_payment", "c_payment_cnt", "c_delivery_cnt", "c_data"   };
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
        String[] columnNames8 = { "h_c_id", "h_c_d_id", "h_c_w_id", "h_d_id", "h_w_id", "h_date", "h_amount", "h_data" };
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
        String[] columnNames9 = { "no_o_id", "no_d_id", "no_w_id"};
        ColumnType[] columnTypes9 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT)
        };
        String[] partitionKeys9 = { "no_o_id" };
        String[] clusteringKeys9 = { };
        new_order = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames9, columnTypes9, partitionKeys9, clusteringKeys9, null);

        //order
        String[] columnNames10 = { "o_id", "o_d_id", "o_w_id", "o_c_id", "o_entry_d", "o_carrier_id", "o_ol_cnt", "o_all_local" };
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
        String[] columnNames11 = { "ol_o_id", "ol_d_id", "ol_w_id", "ol_number", "ol_i_id", "ol_supply_w_id", "ol_delivery_d", "ol_quantity", "ol_amount", "ol_dist_info" };
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
        order_line = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
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
        String[] columnNames13 = { "s_i_id", "s_w_id", "s_quantity", "s_dist_01", "s_dist_02", "s_dist_03", "s_dist_04", "s_dist_05", "s_dist_06", "s_dist_07", "s_dist_08", "s_dist_09", "s_dist_10", "s_ytd", "s_order_cnt", "s_remote_cnt", "s_data" };
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
    public void testQ00Hive() throws ManifestException {

        init();

        String inputText = "[tpcc],    select substring(c_state,1,1) as country," + "    count(*) as numcust,"
                        + "    sum(c_balance) as totacctbal from" + "    tpcc.customer" + "    inner join"
                        + "    (select avg(sub.c_balance) as balance from  tpcc.customer sub where  sub.c_balance > 0.00 and substring(sub.c_phone,1,1) in ['1','2','3','4','5','6','7']) y"
                        + "    where  substring(c_phone,1,1) in ['1','2','3','4','5','6','7']"
                        + "    and c_balance > y.balance"
                        + "    group by substring(c_state,1,1)"
                        + "    order by country;";


        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ00", false, false, customer);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    //ADD FUNCTION TO METADATA MANAGER
    @Test
    public void testQ00CrossdataVeryEasy() throws ManifestException {

        init();

        String inputText = "[tpcc],    select substring(c_state,1,1) as country,"
                        + "    count(*) as numcust,"
                        + "    sum(c_balance) as totacctbal from"
                        + "    (select avg(sub.c_balance) as balance from  tpcc.customer sub where  sub.c_balance > 0.00 ) y"
                        + "    inner join tpcc.customer on c_balance = y.balance "
                        + "    group by c_state"
                        + "    order by country;";


        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ0", false, false, customer);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ00CrossdataEasy() throws ManifestException {

        init();

        String inputText = "[tpcc],    select substring(c_state,1,1) as country,"
                        + "    count(*) as numcust,"
                        + "    sum(c_balance) as totacctbal from"
                        + "    (select avg(sub.c_balance) as balance from  tpcc.customer sub where  sub.c_balance > 0.00 and substring(sub.c_phone,1,1) in ['1','2','3','4','5','6','7']) y"
                        + "    inner join tpcc.customer on c_balance = y.balance "
                        + "    where  c_phone in ['1','2','3','4','5','6','7']"
                        + "    group by c_state"
                        + "    order by country;";


        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ0", false, false, customer);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

   @Test
    public void testQ01Original() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                + "select ol_o_id, ol_d_id,ol_w_id,sum(ol_quantity),avg(ol_quantity),sum(ol_amount) as suma,avg(ol_amount),count(*) "
                        + "    from tpcc.order_line where ol_d_id=4 and ol_w_id=175 "
                        + "    group by ol_o_id, ol_d_id,ol_w_id order by  sum(ol_amount) desc limit 10;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ01Hive", false, false, order_line);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ02Original() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                        + "select OL.OL_w_id,OL.OL_D_id,OL.OL_O_id,AVG_Amoun,avg(OL.ol_amount) "
                        + "from "
                        + "(select d_id,d_w_id, avg(ol_amount) as AVG_Amoun "
                        + "    from tpcc.district D, tpcc.order_line OL_A where D.d_id=OL_A.ol_d_id and D.d_w_id=OL_A.ol_w_id and"
                        + "    d_id=3 and d_w_id=241 group by d_id,d_w_id"
                        + ") A, tpcc.order_line OL "
                        + "   where A.d_id=OL.ol_d_id and A.d_w_id=OL.ol_w_id and OL.ol_d_id=${random(district)} and OL.ol_w_id=${random(0,tpcc.number.warehouses)} "
                        + "   group by OL.OL_w_id,OL.OL_D_id,OL.OL_O_id,AVG_Amoun having avg(OL.ol_amount) > AVG_Amoun order by avg(OL.ol_amount) desc;";

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
                        + "select OL.ol_w_id,OL.ol_d_id,OL.ol_o_id,AVG_Amoun,avg(OL.ol_amount) as average from "
                        + "(select d_id,d_w_id, avg(ol_amount) as AVG_Amoun"
                            + " from tpcc.district D, tpcc.order_line OL_A where D.d_id=OL_A.ol_d_id and D.d_w_id=OL_A.ol_w_id and "
                            + "d_id=3 and d_w_id=241 group by d_id,d_w_id"
                        + ") A, "
                        + "tpcc.order_line OL "
                        + "where A.d_id=OL.ol_d_id and A.d_w_id=OL.ol_w_id and OL.ol_d_id=3 and OL.ol_w_id=241 group by OL.ol_w_id,OL.ol_d_id,OL.ol_o_id,AVG_Amoun having avg(OL.ol_amount) > AVG_Amoun order by average desc;";

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
                        + "select OL.ol_w_id,OL.ol_d_id,OL.ol_o_id,avg(OL.ol_amount) as average from "
                        + "(select d_id,d_w_id"
                        + " from tpcc.district D inner join tpcc.order_line OL_A on D.d_id=OL_A.ol_d_id and D.d_w_id=OL_A.ol_w_id "
                        + "where d_id=3 and d_w_id=241 group by d_id,d_w_id"
                        + ") A inner join  "
                        + "tpcc.order_line as OL ON A.d_id=OL.ol_d_id and A.d_w_id=OL.ol_w_id "
                        + "where OL.ol_w_id=241 and OL.ol_d_id=3 "
                        + "group by OL.ol_w_id,OL.ol_d_id,OL.ol_o_id order by average desc;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                        inputText, "testQ02", false, false, order_line);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }




    @Test
    public void testQ03() throws ManifestException {

        init();

        String inputText = "[tpcc], select c_d_id,c_credit,count(o_id) from tpcc.customer"
                        + " inner join tpcc.order on c_d_id=o_d_id and c_w_id=o_w_id and o_c_id=c_id"
                        + "    where c_w_id=168  group by c_credit,c_d_id "
                        + "    order by c_d_id, c_credit;";
        
        
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ3", false, false, customer, order);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }




    @Test
    public void testQ04SomeRewrite() throws ManifestException {

        init();

        String inputText = "[tpcc],  select "
                        + "    c.c_state,days_between(o.o_entry_d,ol.ol_delivery_d), sum(ol.ol_amount),avg(ol.ol_amount) "
                        + "    from tpcc.order_line ol,"
                        + "    tpcc.order o,"
                        + "    tpcc.customer c "
                        + "    where o.o_id=ol.ol_o_id  "
                        + "    and o.o_d_id=ol.ol_d_id  "
                        + "    and o.o_w_id=ol.ol_w_id and o.o_c_id=c.c_id "
                        + "    and o.o_d_id=c.c_d_id "
                        + "    and o.o_w_id=c.c_w_id "
                        + "    and c.c_w_id=100 "
                        + "    and c_since>= "
                        + "     (select add_days(max(c_since),-7) from tpcc.customer c )"
                        + "    and days_between(o.o_entry_d,ol.ol_delivery_d)>30 "
                        + "    group by c.c_state,days_between(o.o_entry_d,ol.ol_delivery_d)"
                        + "    order by count(*) desc LIMIT 10;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ4", false, false, order, customer);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }



    @Test
    public void testQ05() throws ManifestException {

        init();

        String inputText = "[tpcc],select s_i_id,i_price, s_quantity, i_name,count(*) as numero_pedidos, sum(i_price*s_quantity) as venta_total "
                        + "from tpcc.stock, tpcc.item where i_id=s_i_id and i_name like 'af%' "
                        + "group by  s_i_id,i_price, s_quantity, i_name order by venta_total desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ5", false, false, customer, stock, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ05Crossdata() throws ManifestException {

        init();

        String inputText = "[tpcc],select s_i_id,i_price, s_quantity, i_name,count(*) as numero_pedidos, sum(i_price*s_quantity) as venta_total "
                        + "from tpcc.stock inner join tpcc.item on i_id=s_i_id where i_name like 'af%' "
                        + "group by  s_i_id,i_price, s_quantity, i_name order by venta_total desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ5", false, false, customer, stock, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ06() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                        + "   SELECT s_w_id,count(*) FROM tpcc.stock"
                        + " where s_quantity>85 and "
                        + " s_quantity <= 115 group by s_w_id;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ6", false, false, stock);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ07() throws ManifestException {

        init();

        String inputText = "[tpcc],"
                        + "  select max(ol_amount) from tpcc.order_line;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ7", false, false, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ08() throws ManifestException {

        init();

        String inputText = "[tpcc],      select max(ol_amount),max(ol_quantity) from tpcc.order_line;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ8", false, false, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ09() throws ManifestException {

        init();

        String inputText = "[tpcc],  "
                        + " select extract_day(h_date), avg(h_amount) "
                        + "from tpcc.history where h_c_w_id=245 group by extract_day(h_date) order by extract_day(h_date);";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ9", false, false, history);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ09Crossdata() throws ManifestException {

        init();

        String inputText = "[tpcc],  "
                        + " select extract_day(h_date) as alias_h_day, avg(h_amount) "
                        + "from tpcc.history where h_c_w_id=245 group by alias_h_day order by alias_h_day;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ9", false, false, history);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }



    @Test
    public void testQ10() throws ManifestException {

        init();

        String inputText = "[tpcc], select sum(ol_amount) as revenue  "
                        + "    from tpcc.order_line, tpcc.item  "
                        + "    where (  "
                        + "                    ol_i_id = i_id  "
                        + "                    and i_data like '%a'  "
                        + "                    and ol_quantity >= ${random(1,5)}  "
                        + "    and ol_quantity <= ${random(1,5)+5}  "
                        + "    and i_price between 1 and 400000  "
                        + "    and ol_w_id in (1,2,3)  "
                        + "                    ) or (  "
                        + "                    ol_i_id = i_id  "
                        + "                    and i_data like '%b'  "
                        + "                    and ol_quantity >= ${random(1,5)}  "
                        + "    and ol_quantity <= ${random(1,5)+5}  "
                        + "    and i_price between 1 and 400000  "
                        + "    and ol_w_id in (1,2,4)  "
                        + "                    ) or (  "
                        + "                    ol_i_id = i_id  "
                        + "                    and i_data like '%c'  "
                        + "                    and ol_quantity >= ${random(1,5)}  "
                        + "    and ol_quantity <= ${random(1,5)+5}  "
                        + "    and i_price between 1 and 400000  "
                        + "    and ol_w_id in (1,5,3)  "
                        + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer, order_line, item);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }



    @Test
    public void testQ11() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                        + "select ol_number,  "
                        + "    sum(ol_quantity) as sum_qty,  "
                        + "    sum(ol_amount) as sum_amount,  "
                        + "    avg(ol_quantity) as avg_qty,  "
                        + "    avg(ol_amount) as avg_amount,  "
                        + "    count(*) as count_order  "
                        + "    from tpcc.order_line  "
                        + "    where ol_delivery_d > to_date('2013-10-05','YYYY-MM-DD')  "
                        + "    group by ol_number  order by sum(ol_quantity) desc LIMIT 100;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ11", false, false, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ12() throws ManifestException {

        init();

        String inputText = "[tpcc], "
                        + " select  ol_o_id, ol_w_id, ol_d_id, "
                        + "    sum(ol_amount) as revenue, o_entry_d  "
                        + "    from tpcc.customer, tpcc.new_order, tpcc.order, tpcc.order_line  "
                        + "    where   c_state like 'A%'  "
                        + "    and c_id = o_c_id  "
                        + "    and c_w_id = o_w_id  "
                        + "    and c_d_id = o_d_id  "
                        + "    and no_w_id = o_w_id  "
                        + "    and no_d_id = o_d_id  "
                        + "    and no_o_id = o_id  "
                        + "    and ol_w_id = o_w_id  "
                        + "    and ol_d_id = o_d_id  "
                        + "    and ol_o_id = o_id  "
                        + "    and o_entry_d >  to_date('2013-07-24','YYYY-MM-DD')  "
                        + "    group by ol_o_id, ol_w_id, ol_d_id, o_entry_d  "
                        + "    order by revenue desc, o_entry_d limit 100";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ12", false, false, customer, new_order, order, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    
    @Test
    public void testQ13WithoutRewrite() throws ManifestException {

        init();

        String inputText = "[tpcc], select top 10 o_ol_cnt,   "
                        + "    sum(case when o_carrier_id = 1 or o_carrier_id = 2 then 1 else 0 end) as high_line_count,   "
                        + "    sum(case when o_carrier_id <> 1 and o_carrier_id <> 2 then 1 else 0 end) as low_line_count   "
                        + "    from  tpcc.order, tpcc.order_line   "
                        + "    where  ol_w_id = o_w_id   "
                        + "    and ol_d_id = o_d_id   "
                        + "    and ol_o_id = o_id   "
                        + "    and o_entry_d <= ol_delivery_d   "
                        + "    and ol_delivery_d <  to_date('2013-07-09','YYYY-MM-DD')   "
                        + "    group by o_ol_cnt   "
                        + "    order by sum(case when o_carrier_id = 1 or o_carrier_id = 2 then 1 else 0 end) desc, sum(case when o_carrier_id <> 1 and o_carrier_id <> 2 then 1 else 0 end);";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ13", false, false, customer, order, order_line);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }
    /*

    @Test
    public void testQ13Easy() throws ManifestException {

        init();

        String inputText = "[demo], "
                        + "SELECT  "
                        + "count(o_orderkey)  "
                        + "FROM  "
                        + "orders "
                        + "WHERE o_comment NOT LIKE \"%special%requests%\";";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ13Easy", false, false, customer, orders);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ14() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                    + "100.00 * sum(CASE  "
                    + "WHEN p_type LIKE 'PROMO%'  "
                    + "THEN l_extendedprice*(1-l_discount)  "
                    + "ELSE 0  "
                    + "end) / sum(l_extendedprice * (1 - l_discount)) AS promo_revenue  "
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
    public void testQ14Easy() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "100.00 * sum (CASE WHEN p_type LIKE 'PROMO%' THEN l_extendedprice*(1-l_discount ELSE 0 end) "
                        + "FROM  "
                        + "lineitem;  ";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14Easy", false, false, lineitem, part);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ14Easy2() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "sum (CASE WHEN p_type LIKE 'PROMO%' THEN l_extendedprice*(1-l_discount ELSE 0 end) "
                        + "FROM  "
                        + "lineitem;  ";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14Easy2", false, false, lineitem, part);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ14Easy3() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "sum (CASE WHEN p_type = 'PR' THEN l_extendedprice ELSE 0 end) "
                        + "FROM  "
                        + "lineitem;  ";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14Easy23", false, false, lineitem, part);
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
                        + "supplier_cnt desc,  "
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

