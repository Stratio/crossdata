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

public class BenchmarkTests extends PlannerBaseTest {

    private ConnectorMetadata connector1 = null;

    private ClusterName clusterName = null;

    private TableMetadata table1 = null;
    private TableMetadata table2 = null;
    private TableMetadata table3 = null;
    private TableMetadata part = null;
    private TableMetadata supplier = null;
    private TableMetadata partsupp = null;
    private TableMetadata customer = null;
    private TableMetadata orders = null;
    private TableMetadata lineitem = null;
    private TableMetadata nation = null;
    private TableMetadata region = null;


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
        operationsC1.add(Operations.FILTER_NON_INDEXED_LIKE);
        operationsC1.add(Operations.SELECT_INNER_JOIN);

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

        clusterName = MetadataManagerTestHelper.HELPER.createTestCluster(strClusterName, dataStoreName, connector1.getName());
        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("demo").getName();
        createTestTables(catalogName);
    }

    @AfterClass
    public void tearDown(){
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    public void createTestTables(CatalogName catalogName) {
        createTestTables(catalogName, "table1", "table2", "table3", "part", "supplier", "partsupp", "customer", "orders", "lineitem", "nation", "region");
    }

    public void createTestTables(CatalogName catalogName, String... tableNames) {
        int i = 0;
        //table1
        String[] columnNames1 = { "id", "user", "rating", "comment" };
        ColumnType[] columnTypes1 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.TEXT) };
        String[] partitionKeys1 = { "id" };
        String[] clusteringKeys1 = { };
        table1 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames1, columnTypes1, partitionKeys1, clusteringKeys1, null);

        //table2
        String[] columnNames2 = { "id", "email", "value", "year" };
        ColumnType[] columnTypes2 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.FLOAT),
                new ColumnType(DataType.INT) };
        String[] partitionKeys2 = { "id" };
        String[] clusteringKeys2 = { };
        table2 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames2, columnTypes2, partitionKeys2, clusteringKeys2, null);

        //table3
        String[] columnNames3 = { "id_aux", "address", "city", "code" };
        ColumnType[] columnTypes3 = {
                new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.BIGINT) };
        String[] partitionKeys3 = { "id_aux" };
        String[] clusteringKeys3 = { };
        table3 = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                columnNames3, columnTypes3, partitionKeys3, clusteringKeys3, null);

        //part
        String[] columnNames4 = { "p_partkey", "p_name", "p_mfgr", "p_brand", "p_type", "p_size", "p_container", "p_retailprice", "p_comment" };
        ColumnType[] columnTypes4 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT)
                            };
        String[] partitionKeys4 = { "p_partkey" };
        String[] clusteringKeys4 = { };
        part = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames4, columnTypes4, partitionKeys4, clusteringKeys4, null);

        //supplier
        String[] columnNames5 = { "s_suppkey", "s_name", "s_address", "s_nationkey", "s_phone", "s_acctbal", "s_comment" };
        ColumnType[] columnTypes5 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys5 = { "s_suppkey" };
        String[] clusteringKeys5 = { };
        supplier = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames5, columnTypes5, partitionKeys5, clusteringKeys5, null);

        //partsupp
        String[] columnNames6 = { "ps_partkey", "ps_suppkey", "ps_availqty", "ps_supplycost", "ps_comment" };
        ColumnType[] columnTypes6 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys6 = { "ps_partkey", "ps_suppkey" };
        String[] clusteringKeys6 = { };
        partsupp = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames6, columnTypes6, partitionKeys6, clusteringKeys6, null);

        //customer
        String[] columnNames7 = { "c_custkey", "c_name", "c_address", "c_nationkey", "c_phone", "c_acctbal", "c_mktsegment", "c_comment" };
        ColumnType[] columnTypes7 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys7 = { "c_custkey" };
        String[] clusteringKeys7 = { };
        customer = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames7, columnTypes7, partitionKeys7, clusteringKeys7, null);


        //orders
        String[] columnNames8 = { "o_orderkey", "o_custkey", "o_orderstatus", "o_totalprice", "o_orderdate", "o_orderpriority", "o_clerk", "o_shippriority", "o_comment" };
        ColumnType[] columnTypes8 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.NATIVE),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys8 = { "o_orderkey" };
        String[] clusteringKeys8 = { };
        orders = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames8, columnTypes8, partitionKeys8, clusteringKeys8, null);

        //lineitem
        String[] columnNames9 = { "l_orderkey", "l_partkey", "l_suppkey", "l_linenumber", "l_quantity", "l_extendedprice", "l_discount", "l_tax", "l_returnflag", "l_linestatus", "l_shipdate", "l_commitdate", "l_receiptdate", "l_shipinstruct", "l_shipmode", "l_comment" };
        ColumnType[] columnTypes9 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.NATIVE),
                        new ColumnType(DataType.NATIVE),
                        new ColumnType(DataType.NATIVE),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys9 = { "l_orderkey" };
        String[] clusteringKeys9 = { "l_linenumber"};
        lineitem = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames9, columnTypes9, partitionKeys9, clusteringKeys9, null);

        //nation
        String[] columnNames10 = { "n_nationkey", "n_name", "n_regionkey", "n_comment" };
        ColumnType[] columnTypes10 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys10 = { "n_nationkey" };
        String[] clusteringKeys10 = { };
        nation = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames10, columnTypes10, partitionKeys10, clusteringKeys10, null);

        //region
        String[] columnNames11 = { "r_regionkey", "r_name", "r_comment" };
        ColumnType[] columnTypes11 = {
                        new ColumnType(DataType.INT),
                        new ColumnType(DataType.TEXT),
                        new ColumnType(DataType.TEXT)
        };
        String[] partitionKeys11 = { "r_regionkey" };
        String[] clusteringKeys11 = { };
        region = MetadataManagerTestHelper.HELPER.createTestTable(clusterName, catalogName.getName(), tableNames[i++],
                        columnNames11, columnTypes11, partitionKeys11, clusteringKeys11, null);
    }

    @Test
    public void testQ01() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                        + "l_returnflag, "
                        + "l_linestatus, "
                        + "sum(l_quantity) AS sum_qty, "
                        + "sum(l_extendedprice) AS sum_base_price, "
                        + "sum(l_extendedprice*(1-l_discount)) AS sum_disc_price, "
                        + "sum(l_extendedprice*(1-l_discount)*(1+l_tax)) AS sum_charge, "
                        + "avg(l_quantity) AS avg_qty, "
                        + "avg(l_extendedprice) AS avg_price, "
                        + "avg(l_discount) AS avg_disc, "
                        + "count(*) AS count_order "
                        + "FROM "
                        + "lineitem "
                        + "WHERE "
                        + "l_shipdate <= date(\"1998-12-01\", \"yyyy-mm-dd\") - interval(70, \"day\") "
                        + "GROUP BY "
                        + "l_returnflag,"
                        + "l_linestatus "
                        + "ORDER BY "
                        + "l_returnflag,"
                        + "l_linestatus;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ1", false, false, lineitem);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ02VerySimple() throws ManifestException {

        init();

        String inputText = "[demo], "
                + "SELECT s_acctbal, s_name, p_partkey, p_mfgr, s_address, s_phone, s_comment "
                + "FROM part, supplier, partsupp "
                + "WHERE  p_partkey = ps_partkey "
                + "AND s_suppkey = ps_suppkey "
                + "AND p_size = 15 "
                + "AND p_type LIKE '%BRASS' "
                + "AND ps_supplycost = 25 "
                + "ORDER BY s_acctbal desc, s_name, p_partkey;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ02VerySimple", false, false, part, supplier,partsupp);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ02Previous() throws ManifestException {

        init();

        String inputText = "[demo], "
                + "SELECT s_acctbal, s_name, n_name, p_partkey, p_mfgr, s_address, s_phone, s_comment "
                + "FROM part, supplier, partsupp, nation, region "
                + "WHERE  p_partkey = ps_partkey "
                    + "AND s_suppkey = ps_suppkey "
                    + "AND p_size = 15 "
                    + "AND p_type LIKE '%BRASS' "
                    + "AND s_nationkey = n_nationkey "
                    + "AND n_regionkey = r_regionkey "
                    + "AND r_name = 'EUROPE' "
                    + "AND ps_supplycost = 25 "
                + "ORDER BY s_acctbal desc, n_name, s_name, p_partkey;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(
                inputText, "testQ02Previous", false, false, part, supplier,partsupp, region, nation);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    /*
    @Test
    public void testQ02() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                        + "s_acctbal,"
                        + "s_name,"
                        + "n_name,"
                        + "p_partkey,"
                        + "p_mfgr,"
                        + "s_address,"
                        + "s_phone,"
                        + "s_comment "
                        + "FROM "
                        + "part,"
                        + "supplier,"
                        + "partsupp,"
                        + "nation,"
                        + "region "
                        + "WHERE "
                        + "p_partkey = ps_partkey "
                        + "AND s_suppkey = ps_suppkey "
                        + "AND p_size = 15 "
                        + "AND p_type LIKE '%BRASS' "
                        + "AND s_nationkey = n_nationkey "
                        + "AND n_regionkey = r_regionkey "
                        + "AND r_name = 'EUROPE' "
                        + "AND ps_supplycost = ( "
                        + "SELECT "
                        + "min(ps_supplycost)"
                        + "FROM "
                        + "partsupp, supplier,"
                        + "nation, region "
                        + "WHERE "
                        + "p_partkey = ps_partkey "
                        + "AND s_suppkey = ps_suppkey "
                        + "AND s_nationkey = n_nationkey "
                        + "AND n_regionkey = r_regionkey "
                        + "AND r_name = 'MOZAMBIQUE' "
                        + ") "
                        + "ORDER BY "
                        + "s_acctbal desc, "
                        + "n_name, "
                        + "s_name, "
                        + "p_partkey;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ2", false, true, part, supplier,partsupp, region, nation);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ03() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                        + "l_orderkey, "
                        + "sum(l_extendedprice*(1-l_discount)) AS revenue, "
                        + "o_orderdate, "
                        + "o_shippriority "
                        + "FROM "
                        + "customer, "
                        + "orders, "
                        + "lineitem "
                        + "WHERE "
                        + "c_mktsegment = 'BUILDING' "
                        + "AND c_custkey = o_custkey "
                        + "AND l_orderkey = o_orderkey "
                        + "AND o_orderdate < date(\"1995-03-15\", \"yyyy-mm-dd\") "
                        + "AND l_shipdate > date(\"1995-03-15\", \"yyyy-mm-dd\") "
                        + "GROUP BY "
                        + "l_orderkey, "
                        + "o_orderdate, "
                        + "o_shippriority "
                        + "ORDER BY "
                        + "revenue desc, "
                        + "o_orderdate;";
        
        
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ3", false, true, customer, orders,lineitem);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ04() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                        + "o_orderpriority, "
                        + "count(*) AS order_count "
                        + "FROM "
                        + "orders "
                        + "WHERE "
                        + "o_orderdate >=  date(\"1995-03-15\", \"yyyy-mm-dd\") "
                        + "AND o_orderdate < date(\"1995-03-15\", \"yyyy-mm-dd\") + interval(3, \"month\") "
                        + "AND exists ( "
                        + "SELECT "
                        + "* "
                        + "FROM "
                        + "lineitem "
                        + "WHERE "
                        + "l_orderkey = o_orderkey "
                        + "AND l_commitdate < l_receiptdate "
                        + ") "
                        + "GROUP BY "
                        + "o_orderpriority "
                        + "ORDER BY "
                        + "o_orderpriority;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ4", false, false, orders, lineitem);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ05() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                    + "n_name, "
                    + "sum(l_extendedprice * (1 - l_discount)) AS revenue "
                    + "FROM "
                    + "customer, "
                    + "orders, "
                    + "lineitem, "
                    + "supplier, "
                    + "nation, "
                    + "region "
                    + "WHERE "
                    + "c_custkey = o_custkey "
                    + "AND l_orderkey = o_orderkey "
                    + "AND l_suppkey = s_suppkey "
                    + "AND c_nationkey = s_nationkey "
                    + "AND s_nationkey = n_nationkey "
                    + "AND n_regionkey = r_regionkey "
                    + "AND r_name = 'ASIA' "
                    + "AND o_orderdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "AND o_orderdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\") "
                    + "GROUP BY "
                    + "n_name "
                    + "ORDER BY "
                    + "revenue desc;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ5", false, false, customer, orders, lineitem, supplier, nation, region);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ06() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
            + "sum(l_extendedprice*l_discount) AS revenue "
            + "FROM "
            + "lineitem "
            + "WHERE "
            + "l_shipdate >= date(\"1994-01-01\", \"yyyy-mm-dd\")"
            + "AND l_shipdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\") "
            + "AND l_discount BETWEEN 0.06 - 0.01 AND 0.06 +0.01 "
            + "AND l_quantity < 24;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ6", false, false, lineitem);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ07() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                    + "supp_nation, "
                    + "cust_nation, "
                    + "l_year, sum(volume) AS revenue "
                    + "FROM ( "
                    + "SELECT "
                    + "n1.n_name AS supp_nation, "
                    + "n2.n_name AS cust_nation, "
                    + "extract(l_shipdate, \"year\" ) AS l_year, "
                    + "l_extendedprice * (1 - l_discount) AS volume "
                    + "FROM "
                    + "supplier, "
                    + "lineitem, "
                    + "orders, "
                    + "customer, "
                    + "nation n1, "
                    + "nation n2 "
                    + "WHERE "
                    + "s_suppkey = l_suppkey "
                    + "AND o_orderkey = l_orderkey "
                    + "AND c_custkey = o_custkey "
                    + "AND s_nationkey = n1.n_nationkey "
                    + "AND c_nationkey = n2.n_nationkey "
                    + "AND ( "
                    + "(n1.n_name = 'FRANCE' AND n2.n_name = 'GERMANY') "
                    + "OR (n1.n_name = 'GERMANY' AND n2.n_name = 'FRANCE') "
                    + ") "
                    + "AND l_shipdate BETWEEN date(\"1995-01-01\", \"yyyy-mm-dd\") AND date(\"1996-12-31\", \"yyyy-mm-dd\") "
                    + ") AS shipping "
                    + "GROUP BY "
                    + "supp_nation, "
                    + "cust_nation, "
                    + "l_year "
                    + "ORDER BY "
                    + "supp_nation, "
                    + "cust_nation, "
                    + "l_year;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ7", false, false, supplier, lineitem, orders, customer, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ08() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                    + "o_year, "
                    + "sum(CASE "
                    + "WHEN nation = 'BRAZIL' "
                    + "THEN volume "
                    + "ELSE 0 "
                    + "end) / sum(volume) AS mkt_share "
                    + "FROM ( "
                    + "SELECT "
                    + "extract(o_orderdate,\"year\") AS o_year, "
                    + "l_extendedprice * (1-l_discount) AS volume, "
                    + "n2.n_name AS nation "
                    + "FROM "
                    + "part, "
                    + "supplier, "
                    + "lineitem, "
                    + "orders, "
                    + "customer, "
                    + "nation n1, "
                    + "nation n2, "
                    + "region "
                    + "WHERE "
                    + "p_partkey = l_partkey "
                    + "AND s_suppkey = l_suppkey "
                    + "AND l_orderkey = o_orderkey "
                    + "AND o_custkey = c_custkey "
                    + "AND c_nationkey = n1.n_nationkey "
                    + "AND n1.n_regionkey = r_regionkey "
                    + "AND r_name = 'AMERICA' "
                    + "AND s_nationkey = n2.n_nationkey "
                    + "AND o_orderdate BETWEEN date(\"1995-01-01\", \"yyyy-mm-dd\") AND date(\"1996-12-31\", \"yyyy-mm-dd\") "
                    + "AND p_type = 'ECONOMY ANODIZED STEEL' "
                    + ") AS all_nations "
                    + "GROUP BY "
                    + "o_year "
                    + "ORDER BY "
                    + "o_year;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ8", false, false, part, supplier, lineitem, orders, customer, nation, region);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ09() throws ManifestException {

        init();

        String inputText = "[demo],SELECT "
                    + "nation, "
                    + "o_year, "
                    + "sum(amount) AS sum_profit "
                    + "FROM ( "
                    + "SELECT "
                    + "n_name AS nation, "
                    + "extract(year FROM o_orderdate) AS o_year, "
                    + "l_extendedprice * (1 - l_discount) - ps_supplycost * l_quantity AS amount "
                    + "FROM "
                    + "part, "
                    + "supplier, "
                    + "lineitem, "
                    + "partsupp, "
                    + "orders, "
                    + "nation "
                    + "WHERE "
                    + "s_suppkey = l_suppkey "
                    + "AND ps_suppkey = l_suppkey "
                    + "AND ps_partkey = l_partkey "
                    + "AND p_partkey = l_partkey "
                    + "AND o_orderkey = l_orderkey "
                    + "AND s_nationkey = n_nationkey "
                    + "AND p_name LIKE '%green%' "
                    + ") AS profit "
                    + "GROUP BY "
                    + "nation, "
                    + "o_year "
                    + "ORDER BY "
                    + "nation, "
                    + "o_year desc;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ9", false, false, part, orders , lineitem, partsupp, supplier, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ10() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                    + "c_custkey, "
                    + "c_name, "
                    + "sum(l_extendedprice * (1 - l_discount)) AS revenue, "
                    + "c_acctbal, "
                    + "n_name, "
                    + "c_address, "
                    + "c_phone, "
                    + "c_comment "
                    + "FROM "
                    + "customer, "
                    + "orders, "
                    + "lineitem, "
                    + "nation "
                    + "WHERE "
                    + "c_custkey = o_custkey "
                    + "AND l_orderkey = o_orderkey "
                    + "AND o_orderdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "AND o_orderdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(3, \"month\") "
                    + "AND l_returnflag = 'R' "
                    + "AND c_nationkey = n_nationkey "
                    + "GROUP BY "
                    + "c_custkey, "
                    + "c_name, "
                    + "c_acctbal, "
                    + "c_phone, "
                    + "n_name, "
                    + "c_address, "
                    + "c_comment "
                    + "ORDER BY "
                    + "revenue desc;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ10", false, false, customer, orders, lineitem, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }



    @Test
    public void testQ11() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                        + "ps_partkey, "
                        + "sum(ps_supplycost*ps_availqty) AS value "
                        + "FROM partsupp, supplier, nation "
                        + "WHERE "
                        + "ps_suppkey = s_suppkey "
                        + "AND s_nationkey = n_nationkey "
                        + "AND n_name = 'MOZAMBIQUE' "
                        + "GROUP BY "
                        + "ps_partkey "
                        + "HAVING "
                        + "sum(ps_supplycost*ps_availqty) > "
                        + "("
                        + "SELECT "
                        + " sum(ps_supplycost*ps_availqty) * 0.0001 "
                        + "FROM partsupp, supplier, nation "
                        + "WHERE "
                        + "ps_suppkey = s_suppkey "
                        + "AND s_nationkey = n_nationkey "
                        + "AND n_name = 'MOZAMBIQUE'"
                        + ")"
                        + "ORDER BY "
                        + "value desc;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ11", false, false, partsupp, supplier, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ12() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                    + "l_shipmode,  "
                    + "sum(CASE  "
                    + "WHEN o_orderpriority ='1-URGENT'  "
                    + "OR o_orderpriority ='2-HIGH'  "
                    + "THEN 1  "
                    + "ELSE 0  "
                    + "end) AS high_line_count,  "
                    + "sum(CASE  "
                    + "WHEN o_orderpriority <> '1-URGENT'  "
                    + "AND o_orderpriority <> '2-HIGH'  "
                    + "THEN 1  "
                    + "ELSE 0  "
                    + "end) AS low_line_count  "
                    + "FROM  "
                    + "orders,  "
                    + "lineitem  "
                    + "WHERE "
                    + "o_orderkey = l_orderkey  "
                    + "AND l_shipmode IN ('MAIL', 'SHIP')  "
                    + "AND l_commitdate < l_receiptdate  "
                    + "AND l_shipdate < l_commitdate  "
                    + "AND l_receiptdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "AND l_receiptdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\") "
                    + "GROUP BY  "
                    + "l_shipmode  "
                    + "ORDER BY  "
                    + "l_shipmode;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ12", false, false, orders, lineitem);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ13() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "c_count, count(*) AS custdist  "
                        + "FROM (  "
                        + "SELECT  "
                        + "c_custkey,  "
                        + "count(o_orderkey)  "
                        + "FROM  "
                        + "customer left outer join orders on  "
                        + "c_custkey = o_custkey  "
                        + "AND o_comment NOT LIKE ‘%special%requests%’  "
                        + "GROUP BY  "
                        + "c_custkey  "
                        + ")as c_orders (c_custkey, c_count)  "
                        + "GROUP BY  "
                        + "c_count  "
                        + "ORDER BY  "
                        + "custdist desc,  "
                        + "c_count desc;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ13", false, false, customer, orders);
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
                        + "AND p_type NOT LIKE 'MEDIUM POLISHED%'  "
                        + "AND p_size IN (49, 14, 23, 45, 19, 3, 36, 9)  "
                        + "AND ps_suppkey NOT IN (  "
                        + "SELECT  "
                        + "s_suppkey  "
                        + "FROM  "
                        + "supplier  "
                        + "WHERE "
                        + "s_comment LIKE '%Customer%Complaints%'  "
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

    @Test
    public void testQ18() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "c_name,  "
                        + "c_custkey,  "
                        + "o_orderkey,  "
                        + "o_orderdate,  "
                        + "o_totalprice,  "
                        + "sum(l_quantity)  "
                        + "FROM  "
                        + "customer,  "
                        + "orders,  "
                        + "lineitem  "
                        + "WHERE "
                        + "o_orderkey IN (  "
                        + "SELECT  "
                        + "l_orderkey  "
                        + "FROM  "
                        + "lineitem  "
                        + "GROUP BY  "
                        + "l_orderkey HAVING  "
                        + "sum(l_quantity) > 300  "
                        + ")  "
                        + "AND c_custkey = o_custkey  "
                        + "AND o_orderkey = l_orderkey  "
                        + "GROUP BY  "
                        + "c_name,  "
                        + "c_custkey,  "
                        + "o_orderkey,  "
                        + "o_orderdate,  "
                        + "o_totalprice  "
                        + "ORDER BY  "
                        + "o_totalprice desc,  "
                        + "o_orderdate;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ18", false, false, customer, orders, lineitem);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ19() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "sum(l_extendedprice * (1 - l_discount) ) AS revenue  "
                        + "FROM  "
                        + "lineitem,  "
                        + "part  "
                        + "WHERE "
                        + "(  "
                        + "p_partkey = l_partkey  "
                        + "AND p_brand = ‘Brand#12’  "
                        + "AND p_container IN ( ‘SM CASE’, ‘SM BOX’, ‘SM PACK’, ‘SM PKG’)  "
                        + "AND l_quantity >= 1 AND l_quantity <= 1 + 10"
                        + "AND p_size BETWEEN 1 AND 5  "
                        + "AND l_shipmode IN (‘AIR’, ‘AIR REG’)  "
                        + "AND l_shipinstruct = ‘DELIVER IN PERSON’  "
                        + ")  "
                        + "OR  "
                        + "(  "
                        + "p_partkey = l_partkey  "
                        + "AND p_brand = ‘Brand#23’  "
                        + "AND p_container IN (‘MED BAG’, ‘MED BOX’, ‘MED PKG’, ‘MED PACK’)  "
                        + "AND l_quantity >= 10 AND l_quantity <= 10 + 10 "
                        + "AND p_size BETWEEN 1 AND 10  "
                        + "AND l_shipmode IN (‘AIR’, ‘AIR REG’)  "
                        + "AND l_shipinstruct = ‘DELIVER IN PERSON’  "
                        + ")  "
                        + "OR  "
                        + "(  "
                        + "p_partkey = l_partkey  "
                        + "AND p_brand = ‘Brand#34’  "
                        + "AND p_container IN ( ‘LG CASE’, ‘LG BOX’, ‘LG PACK’, ‘LG PKG’)  "
                        + "AND l_quantity >= 20 AND l_quantity <= 20 + 10 "
                        + "AND p_size BETWEEN 1 AND 15  "
                        + "AND l_shipmode IN (‘AIR’, ‘AIR REG’)  "
                        + "AND l_shipinstruct = ‘DELIVER IN PERSON’  "
                        + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ19", false, false, lineitem, part, supplier, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ20() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                                + "s_name,  "
                                + "s_address  "
                                + "FROM  "
                                + "supplier, nation  "
                                + "WHERE "
                                + "s_suppkey IN (  "
                                + "SELECT  "
                                + "ps_suppkey  "
                                + "FROM  "
                                + "partsupp  "
                                + "WHERE "
                                + "ps_partkey IN (  "
                                + "SELECT  "
                                + "p_partkey  "
                                + "FROM  "
                                + "part  "
                                + "WHERE "
                                + "p_name LIKE 'forest%'  "
                                + ")  "
                                + "AND ps_availqty > (  "
                                + "SELECT  "
                                + "0.5 * sum(l_quantity)  "
                                + "FROM  "
                                + "lineitem  "
                                + "WHERE "
                                + "l_partkey = ps_partkey  "
                                + "AND l_suppkey = ps_suppkey  "
                                + "AND l_shipdate >= date(\"1994-01-01\", \"yyyy-mm-dd\")  "
                                + "AND l_shipdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\")"
                                + ")  "
                                + ")  "
                                + "AND s_nationkey = n_nationkey  "
                                + "AND n_name = 'CANADA' "
                                + "ORDER BY  "
                                + "s_name;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ20", false, false, lineitem, supplier, part, partsupp);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ21() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "s_name,  "
                        + "count(*) AS numwait  "
                        + "FROM  "
                        + "supplier,  "
                        + "lineitem l1,  "
                        + "orders,  "
                        + "nation  "
                        + "WHERE "
                        + "s_suppkey = l1.l_suppkey  "
                        + "AND o_orderkey = l1.l_orderkey  "
                        + "AND o_orderstatus = 'F'  "
                        + "AND l1.l_receiptdate > l1.l_commitdate  "
                        + "AND exists (  "
                        + "SELECT  "
                        + "* "
                        + "FROM  "
                        + "lineitem l2  "
                        + "WHERE "
                        + "l2.l_orderkey = l1.l_orderkey  "
                        + "AND l2.l_suppkey <> l1.l_suppkey  "
                        + ")  "
                        + "AND NOT exists (  "
                        + "SELECT  "
                        + "*  "
                        + "FROM  "
                        + "lineitem l3  "
                        + "WHERE "
                        + "l3.l_orderkey = l1.l_orderkey  "
                        + "AND l3.l_suppkey <> l1.l_suppkey  "
                        + "AND l3.l_receiptdate > l3.l_commitdate  "
                        + ")  "
                        + "AND s_nationkey = n_nationkey  "
                        + "AND n_name = 'SAUDI ARABIA'  "
                        + "GROUP BY  "
                        + "s_name  "
                        + "ORDER BY  "
                        + "numwait desc,  "
                        + "s_name;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ21", false, false, orders, lineitem,  partsupp, supplier, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ22() throws ManifestException {

        init();

        String inputText = "[demo], SELECT  "
                        + "cntrycode,  "
                        + "count(*) AS numcust,  "
                        + "sum(c_acctbal) AS totacctbal  "
                        + "FROM (  "
                        + "SELECT  "
                        + "substring(c_phone,1 , 2) AS cntrycode,  "
                        + "c_acctbal  "
                        + "FROM  "
                        + "customer  "
                        + "WHERE "
                        + "substring(c_phone, 1 , 2) IN  "
                        + "('13','31’,'23','29','30','18','17')  "
                        + "AND c_acctbal > (  "
                        + "SELECT  "
                        + "avg(c_acctbal)  "
                        + "FROM  "
                        + "customer  "
                        + "WHERE "
                        + "c_acctbal > 0.00  "
                        + "AND substring (c_phone, 1, 2) IN  "
                        + "('13','31’,'23','29','30','18','17')  "
                        + ")  "
                        + "AND NOT exists (  "
                        + "SELECT  "
                        + "*  "
                        + "FROM  "
                        + "orders  "
                        + "WHERE "
                        + "o_custkey = c_custkey  "
                        + ") "
                        + ") AS custsale  "
                        + "GROUP BY  "
                        + "cntrycode  "
                        + "ORDER BY  "
                        + "cntrycode;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ22", false, false, customer, orders);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }
*/

}
