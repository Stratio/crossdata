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
    private ConnectorMetadata connector2 = null;

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
    private void simpleQ1() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                + "id, "
                + "sum(id*(rating)*(1+rating)) AS sum_charge, "
                + "avg(rating) AS avg_size, "
                + "count(*) AS count_order "
                + "FROM table1 "
                + "WHERE "
                + "rating <= date(\"1998-12-01\", \"yyyy-mm-dd\") - interval(70, \"day\") "
                + "GROUP BY rating "
                + "ORDER BY user;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "simpleQ1", false, false, table1);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void simpleQ2() throws ManifestException {

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
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "simpleQ2", false, true, table1);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    private void testQ1() throws ManifestException {

        init();

        String inputText = "[demo], SELECT "
                        + "l_returnflag, "
                        + "l_linestatus, "
                        + "sum(l_quantity) as sum_qty, "
                        + "sum(l_extendedprice) as sum_base_price, "
                        + "sum(l_extendedprice*(1-l_discount)) as sum_disc_price, "
                        + "sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge, "
                        + "avg(l_quantity) as avg_qty, "
                        + "avg(l_extendedprice) as avg_price, "
                        + "avg(l_discount) as avg_disc, "
                        + "count(*) as count_order "
                        + "from "
                        + "lineitem "
                        + "where "
                        + "l_shipdate <= date(\"1998-12-01\", \"yyyy-mm-dd\") - interval(70, \"day\") "
                        + "group by "
                        + "l_returnflag,"
                        + "l_linestatus "
                        + "order by "
                        + "l_returnflag,"
                        + "l_linestatus;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ1", false, false, lineitem);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ2() throws ManifestException {

        init();

        String inputText = "[demo], "
                        + "select "
                        + "s_acctbal,"
                        + "s_name,"
                        + "n_name,"
                        + "p_partkey,"
                        + "p_mfgr,"
                        + "s_address,"
                        + "s_phone,"
                        + "s_comment "
                        + "from "
                        + "part,"
                        + "supplier,"
                        + "partsupp,"
                        + "nation,"
                        + "region "
                        + "where "
                        + "p_partkey = ps_partkey "
                        + "and s_suppkey = ps_suppkey "
                        + "and p_size = 15 "
                        + "and p_type like '%BRASS' "
                        + "and s_nationkey = n_nationkey "
                        + "and n_regionkey = r_regionkey "
                        + "and r_name = 'EUROPE' "
                        + "and ps_supplycost = ( "
                        + "select "
                        + "min(ps_supplycost)"
                        + "from" + "partsupp, supplier,"
                        + "nation, region "
                        + "where "
                        + "p_partkey = ps_partkey "
                        + "and s_suppkey = ps_suppkey "
                        + "and s_nationkey = n_nationkey "
                        + "and n_regionkey = r_regionkey "
                        + "and r_name = 'MOZAMBIQUE' "
                        + ") " + "order by "
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
    public void testQ3() throws ManifestException {

        init();

        String inputText = "[demo], select "
                        + "l_orderkey, "
                        + "sum(l_extendedprice*(1-l_discount)) as revenue, "
                        + "o_orderdate, "
                        + "o_shippriority "
                        + "from "
                        + "customer, "
                        + "orders, "
                        + "lineitem "
                        + "where "
                        + "c_mktsegment = 'BUILDING' "
                        + "and c_custkey = o_custkey "
                        + "and l_orderkey = o_orderkey "
                        + "and o_orderdate < date(\"1995-03-15\", \"yyyy-mm-dd\") "
                        + "and l_shipdate > date(\"1995-03-15\", \"yyyy-mm-dd\") "
                        + "group by "
                        + "l_orderkey, "
                        + "o_orderdate, "
                        + "o_shippriority "
                        + "order by "
                        + "revenue desc, "
                        + "o_orderdate;";
        
        
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ3", false, true, customer, orders,lineitem);
        assertNotNull(queryWorkflow, "Null workflow received.");
        assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


    @Test
    public void testQ4() throws ManifestException {

        init();

        String inputText = "[demo], select "  
                        + "o_orderpriority, "
                        + "count(*) as order_count "
                        + "from "
                        + "orders "
                        + "where "
                        + "o_orderdate >=  date(\"1995-03-15\", \"yyyy-mm-dd\") "
                        + "and o_orderdate < date(\"1995-03-15\", \"yyyy-mm-dd\") + interval(3, \"month\") "
                        + "and exists ( "
                        + "select "
                        + "* "
                        + "from "
                        + "lineitem "
                        + "where "
                        + "l_orderkey = o_orderkey "
                        + "and l_commitdate < l_receiptdate "
                        + ") "
                        + "group by "
                        + "o_orderpriority "
                        + "order by "
                        + "o_orderpriority;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ4", false, false, orders, lineitem);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ5() throws ManifestException {

        init();

        String inputText = "[demo], select "  
                    + "n_name, "
                    + "sum(l_extendedprice * (1 - l_discount)) as revenue "
                    + "from "
                    + "customer, "
                    + "orders, "
                    + "lineitem, "
                    + "supplier, "
                    + "nation, "
                    + "region "
                    + "where "
                    + "c_custkey = o_custkey "
                    + "and l_orderkey = o_orderkey "
                    + "and l_suppkey = s_suppkey "
                    + "and c_nationkey = s_nationkey "
                    + "and s_nationkey = n_nationkey "
                    + "and n_regionkey = r_regionkey "
                    + "and r_name = 'ASIA' "
                    + "and o_orderdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "and o_orderdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\") "
                    + "group by "
                    + "n_name "
                    + "order by "
                    + "revenue desc;";
        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ5", false, false, customer, orders, lineitem, supplier, nation, region);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ6() throws ManifestException {

        init();

        String inputText = "[demo], select "  
            + "sum(l_extendedprice*l_discount) as revenue "
            + "from "
            + "lineitem "
            + "where "
            + "l_shipdate >= date(\"1994-01-01\", \"yyyy-mm-dd\")"
            + "and l_shipdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\") "
            + "and l_discount between 0.06 - 0.01 and 0.06 +0.01 "
            + "and l_quantity < 24;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ6", false, false, lineitem);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ7() throws ManifestException {

        init();

        String inputText = "[demo], select "  
                    + "supp_nation, "
                    + "cust_nation, "
                    + "l_year, sum(volume) as revenue "
                    + "from ( "
                    + "select "
                    + "n1.n_name as supp_nation, "
                    + "n2.n_name as cust_nation, "
                    + "extract(l_shipdate, \"year\" ) as l_year, "
                    + "l_extendedprice * (1 - l_discount) as volume "
                    + "from "
                    + "supplier, "
                    + "lineitem, "
                    + "orders, "
                    + "customer, "
                    + "nation n1, "
                    + "nation n2 "
                    + "where "
                    + "s_suppkey = l_suppkey "
                    + "and o_orderkey = l_orderkey "
                    + "and c_custkey = o_custkey "
                    + "and s_nationkey = n1.n_nationkey "
                    + "and c_nationkey = n2.n_nationkey "
                    + "and ( "
                    + "(n1.n_name = 'FRANCE' and n2.n_name = 'GERMANY') "
                    + "or (n1.n_name = 'GERMANY' and n2.n_name = 'FRANCE') "
                    + ") "
                    + "and l_shipdate between date(\"1995-01-01\", \"yyyy-mm-dd\") and date(\"1996-12-31\", \"yyyy-mm-dd\") "
                    + ") as shipping "
                    + "group by "
                    + "supp_nation, "
                    + "cust_nation, "
                    + "l_year "
                    + "order by "
                    + "supp_nation, "
                    + "cust_nation, "
                    + "l_year;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ7", false, false, supplier, lineitem, orders, customer, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ8() throws ManifestException {

        init();

        String inputText = "[demo], select "  
                    + "o_year, "
                    + "sum(case "
                    + "when nation = 'BRAZIL' "
                    + "then volume "
                    + "else 0 "
                    + "end) / sum(volume) as mkt_share "
                    + "from ( "
                    + "select "
                    + "extract(o_orderdate,\"year\") as o_year, "
                    + "l_extendedprice * (1-l_discount) as volume, "
                    + "n2.n_name as nation "
                    + "from "
                    + "part, "
                    + "supplier, "
                    + "lineitem, "
                    + "orders, "
                    + "customer, "
                    + "nation n1, "
                    + "nation n2, "
                    + "region "
                    + "where "
                    + "p_partkey = l_partkey "
                    + "and s_suppkey = l_suppkey "
                    + "and l_orderkey = o_orderkey "
                    + "and o_custkey = c_custkey "
                    + "and c_nationkey = n1.n_nationkey "
                    + "and n1.n_regionkey = r_regionkey "
                    + "and r_name = 'AMERICA' "
                    + "and s_nationkey = n2.n_nationkey "
                    + "and o_orderdate between date(\"1995-01-01\", \"yyyy-mm-dd\") and date(\"1996-12-31\", \"yyyy-mm-dd\") "
                    + "and p_type = 'ECONOMY ANODIZED STEEL' "
                    + ") as all_nations "
                    + "group by "
                    + "o_year "
                    + "order by "
                    + "o_year;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ8", false, false, part, supplier, lineitem, orders, customer, nation, region);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }



    @Test
    public void testQ9() throws ManifestException {

        init();

        String inputText = "[demo],select "  
                    + "nation, "
                    + "o_year, "
                    + "sum(amount) as sum_profit "
                    + "from ( "
                    + "select "
                    + "n_name as nation, "
                    + "extract(year from o_orderdate) as o_year, "
                    + "l_extendedprice * (1 - l_discount) - ps_supplycost * l_quantity as amount "
                    + "from "
                    + "part, "
                    + "supplier, "
                    + "lineitem, "
                    + "partsupp, "
                    + "orders, "
                    + "nation "
                    + "where "
                    + "s_suppkey = l_suppkey "
                    + "and ps_suppkey = l_suppkey "
                    + "and ps_partkey = l_partkey "
                    + "and p_partkey = l_partkey "
                    + "and o_orderkey = l_orderkey "
                    + "and s_nationkey = n_nationkey "
                    + "and p_name like '%green%' "
                    + ") as profit "
                    + "group by "
                    + "nation, "
                    + "o_year "
                    + "order by "
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

        String inputText = "[demo], select "  
                    + "c_custkey, "
                    + "c_name, "
                    + "sum(l_extendedprice * (1 - l_discount)) as revenue, "
                    + "c_acctbal, "
                    + "n_name, "
                    + "c_address, "
                    + "c_phone, "
                    + "c_comment "
                    + "from "
                    + "customer, "
                    + "orders, "
                    + "lineitem, "
                    + "nation "
                    + "where "
                    + "c_custkey = o_custkey "
                    + "and l_orderkey = o_orderkey "
                    + "and o_orderdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "and o_orderdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(3, \"month\") "
                    + "and l_returnflag = 'R' "
                    + "and c_nationkey = n_nationkey "
                    + "group by "
                    + "c_custkey, "
                    + "c_name, "
                    + "c_acctbal, "
                    + "c_phone, "
                    + "n_name, "
                    + "c_address, "
                    + "c_comment "
                    + "order by "
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

        String inputText = "[demo], select  " 
                    + "l_shipmode,  "
                    + "sum(case  "
                    + "when o_orderpriority ='1-URGENT'  "
                    + "or o_orderpriority ='2-HIGH'  "
                    + "then 1  "
                    + "else 0  "
                    + "end) as high_line_count,  "
                    + "sum(case  "
                    + "when o_orderpriority <> '1-URGENT'  "
                    + "and o_orderpriority <> '2-HIGH'  "
                    + "then 1  "
                    + "else 0  "
                    + "end) as low_line_count  "
                    + "from  "
                    + "orders,  "
                    + "lineitem  "
                    + "where  "
                    + "o_orderkey = l_orderkey  "
                    + "and l_shipmode in ('MAIL', 'SHIP')  "
                    + "and l_commitdate < l_receiptdate  "
                    + "and l_shipdate < l_commitdate  "
                    + "and l_receiptdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "and l_receiptdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\") "
                    + "group by  "
                    + "l_shipmode  "
                    + "order by  "
                    + "l_shipmode;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ12", false, false, orders, lineitem);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ13() throws ManifestException {

        init();

        String inputText = "[demo], select  " 
                        + "c_count, count(*) as custdist  "
                        + "from (  "
                        + "select  "
                        + "c_custkey,  "
                        + "count(o_orderkey)  "
                        + "from  "
                        + "customer left outer join orders on  "
                        + "c_custkey = o_custkey  "
                        + "and o_comment not like ‘%special%requests%’  "
                        + "group by  "
                        + "c_custkey  "
                        + ")as c_orders (c_custkey, c_count)  "
                        + "group by  "
                        + "c_count  "
                        + "order by  "
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

        String inputText = "[demo], select  " 
                    + "100.00 * sum(case  "
                    + "when p_type like 'PROMO%'  "
                    + "then l_extendedprice*(1-l_discount)  "
                    + "else 0  "
                    + "end) / sum(l_extendedprice * (1 - l_discount)) as promo_revenue  "
                    + "from  "
                    + "lineitem,  "
                    + "part  "
                    + "where  "
                    + "l_partkey = p_partkey  "
                    + "and l_shipdate >= date(\"1994-01-01\", \"yyyy-mm-dd\") "
                    + "and l_shipdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"month\") ;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ14", false, false, lineitem, part);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ16() throws ManifestException {

        init();

        String inputText = "[demo], select  " 
                        + "p_brand,  "
                        + "p_type,  "
                        + "p_size,  "
                        + "count(distinct ps_suppkey) as supplier_cnt  "
                        + "from  "
                        + "partsupp,  "
                        + "part  "
                        + "where  "
                        + "p_partkey = ps_partkey  "
                        + "and p_brand <> 'Brand#45'  "
                        + "and p_type not like 'MEDIUM POLISHED%'  "
                        + "and p_size in (49, 14, 23, 45, 19, 3, 36, 9)  "
                        + "and ps_suppkey not in (  "
                        + "select  "
                        + "s_suppkey  "
                        + "from  "
                        + "supplier  "
                        + "where  "
                        + "s_comment like '%Customer%Complaints%'  "
                        + ")  "
                        + "group by  "
                        + "p_brand,  "
                        + "p_type,  "
                        + "p_size  "
                        + "order by  "
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

        String inputText = "[demo], select  " 
                        + "sum(l_extendedprice) / 7.0 as avg_yearly  "
                        + "from  "
                        + "lineitem,  "
                        + "part  "
                        + "where  "
                        + "p_partkey = l_partkey  "
                        + "and p_brand = 'Brand#23'  "
                        + "and p_container = 'MED BOX'  "
                        + "and l_quantity < (  "
                        + "select  "
                        + "0.2 * avg(l_quantity)  "
                        + "from  "
                        + "lineitem  "
                        + "where  "
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

        String inputText = "[demo], select  " 
                        + "c_name,  "
                        + "c_custkey,  "
                        + "o_orderkey,  "
                        + "o_orderdate,  "
                        + "o_totalprice,  "
                        + "sum(l_quantity)  "
                        + "from  "
                        + "customer,  "
                        + "orders,  "
                        + "lineitem  "
                        + "where  "
                        + "o_orderkey in (  "
                        + "select  "
                        + "l_orderkey  "
                        + "from  "
                        + "lineitem  "
                        + "group by  "
                        + "l_orderkey having  "
                        + "sum(l_quantity) > 300  "
                        + ")  "
                        + "and c_custkey = o_custkey  "
                        + "and o_orderkey = l_orderkey  "
                        + "group by  "
                        + "c_name,  "
                        + "c_custkey,  "
                        + "o_orderkey,  "
                        + "o_orderdate,  "
                        + "o_totalprice  "
                        + "order by  "
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

        String inputText = "[demo], select  " 
                        + "sum(l_extendedprice * (1 - l_discount) ) as revenue  "
                        + "from  "
                        + "lineitem,  "
                        + "part  "
                        + "where  "
                        + "(  "
                        + "p_partkey = l_partkey  "
                        + "and p_brand = ‘Brand#12’  "
                        + "and p_container in ( ‘SM CASE’, ‘SM BOX’, ‘SM PACK’, ‘SM PKG’)  "
                        + "and l_quantity >= 1 and l_quantity <= 1 + 10"
                        + "and p_size between 1 and 5  "
                        + "and l_shipmode in (‘AIR’, ‘AIR REG’)  "
                        + "and l_shipinstruct = ‘DELIVER IN PERSON’  "
                        + ")  "
                        + "or  "
                        + "(  "
                        + "p_partkey = l_partkey  "
                        + "and p_brand = ‘Brand#23’  "
                        + "and p_container in (‘MED BAG’, ‘MED BOX’, ‘MED PKG’, ‘MED PACK’)  "
                        + "and l_quantity >= 10 and l_quantity <= 10 + 10 "
                        + "and p_size between 1 and 10  "
                        + "and l_shipmode in (‘AIR’, ‘AIR REG’)  "
                        + "and l_shipinstruct = ‘DELIVER IN PERSON’  "
                        + ")  "
                        + "or  "
                        + "(  "
                        + "p_partkey = l_partkey  "
                        + "and p_brand = ‘Brand#34’  "
                        + "and p_container in ( ‘LG CASE’, ‘LG BOX’, ‘LG PACK’, ‘LG PKG’)  "
                        + "and l_quantity >= 20 and l_quantity <= 20 + 10 "
                        + "and p_size between 1 and 15  "
                        + "and l_shipmode in (‘AIR’, ‘AIR REG’)  "
                        + "and l_shipinstruct = ‘DELIVER IN PERSON’  "
                        + ");";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ19", false, false, lineitem, part, supplier, nation);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ20() throws ManifestException {

        init();

        String inputText = "[demo], select  " 
                                + "s_name,  "
                                + "s_address  "
                                + "from  "
                                + "supplier, nation  "
                                + "where  "
                                + "s_suppkey in (  "
                                + "select  "
                                + "ps_suppkey  "
                                + "from  "
                                + "partsupp  "
                                + "where  "
                                + "ps_partkey in (  "
                                + "select  "
                                + "p_partkey  "
                                + "from  "
                                + "part  "
                                + "where  "
                                + "p_name like 'forest%'  "
                                + ")  "
                                + "and ps_availqty > (  "
                                + "select  "
                                + "0.5 * sum(l_quantity)  "
                                + "from  "
                                + "lineitem  "
                                + "where  "
                                + "l_partkey = ps_partkey  "
                                + "and l_suppkey = ps_suppkey  "
                                + "and l_shipdate >= date(\"1994-01-01\", \"yyyy-mm-dd\")  "
                                + "and l_shipdate < date(\"1994-01-01\", \"yyyy-mm-dd\") + interval(1, \"year\")"
                                + ")  "
                                + ")  "
                                + "and s_nationkey = n_nationkey  "
                                + "and n_name = 'CANADA' "
                                + "order by  "
                                + "s_name;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ20", false, false, lineitem, supplier, part, partsupp);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }

    @Test
    public void testQ21() throws ManifestException {

        init();

        String inputText = "[demo], select  " 
                        + "s_name,  "
                        + "count(*) as numwait  "
                        + "from  "
                        + "supplier,  "
                        + "lineitem l1,  "
                        + "orders,  "
                        + "nation  "
                        + "where  "
                        + "s_suppkey = l1.l_suppkey  "
                        + "and o_orderkey = l1.l_orderkey  "
                        + "and o_orderstatus = 'F'  "
                        + "and l1.l_receiptdate > l1.l_commitdate  "
                        + "and exists (  "
                        + "select  "
                        + "*  "
                        + "from  "
                        + "lineitem l2  "
                        + "where  "
                        + "l2.l_orderkey = l1.l_orderkey  "
                        + "and l2.l_suppkey <> l1.l_suppkey  "
                        + ")  "
                        + "and not exists (  "
                        + "select  "
                        + "*  "
                        + "from  "
                        + "lineitem l3  "
                        + "where  "
                        + "l3.l_orderkey = l1.l_orderkey  "
                        + "and l3.l_suppkey <> l1.l_suppkey  "
                        + "and l3.l_receiptdate > l3.l_commitdate  "
                        + ")  "
                        + "and s_nationkey = n_nationkey  "
                        + "and n_name = 'SAUDI ARABIA'  "
                        + "group by  "
                        + "s_name  "
                        + "order by  "
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

        String inputText = "[demo], select  " 
                        + "cntrycode,  "
                        + "count(*) as numcust,  "
                        + "sum(c_acctbal) as totacctbal  "
                        + "from (  "
                        + "select  "
                        + "substring(c_phone,1 , 2) as cntrycode,  "
                        + "c_acctbal  "
                        + "from  "
                        + "customer  "
                        + "where  "
                        + "substring(c_phone, 1 , 2) in  "
                        + "('13','31’,'23','29','30','18','17')  "
                        + "and c_acctbal > (  "
                        + "select  "
                        + "avg(c_acctbal)  "
                        + "from  "
                        + "customer  "
                        + "where  "
                        + "c_acctbal > 0.00  "
                        + "and substring (c_phone, 1, 2) in  "
                        + "('13','31’,'23','29','30','18','17')  "
                        + ")  "
                        + "and not exists (  "
                        + "select  "
                        + "*  "
                        + "from  "
                        + "orders  "
                        + "where  "
                        + "o_custkey = c_custkey  "
                        + ") "
                        + ") as custsale  "
                        + "group by  "
                        + "cntrycode  "
                        + "order by  "
                        + "cntrycode;";

        QueryWorkflow queryWorkflow = (QueryWorkflow) getPlannedQuery(inputText, "testQ22", false, false, customer, orders);
        //assertNotNull(queryWorkflow, "Null workflow received.");
        //assertEquals(queryWorkflow.getResultType(), ResultType.RESULTS, "Invalid result type");
        //assertEquals(queryWorkflow.getExecutionType(), ExecutionType.SELECT, "Invalid execution type");
    }


}
