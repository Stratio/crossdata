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

package com.stratio.crossdata.core.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import com.stratio.crossdata.common.ask.APICommand;
import com.stratio.crossdata.common.ask.Command;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.MetadataManagerTestHelper;

/**
 * Explain plan tests using the API apiManager
 */
public class ExplainPlanAPIManagerTest {

    private ConnectorMetadata connector1 = null;

    private ClusterName clusterName = null;

    private TableMetadata table1 = null;
    private TableMetadata table2 = null;

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(ExplainPlanAPIManagerTest.class);

    @BeforeClass
    public void setUp() throws ManifestException {

        MetadataManagerTestHelper.HELPER.initHelper();
        MetadataManagerTestHelper.HELPER.createTestEnvironment();

        DataStoreName dataStoreName = MetadataManagerTestHelper.HELPER.createTestDatastore();

        Set<Operations> operationsC1 = new HashSet<>();
        operationsC1.add(Operations.CREATE_TABLE);
        operationsC1.add(Operations.INSERT);
        operationsC1.add(Operations.PROJECT);
        operationsC1.add(Operations.SELECT_OPERATOR);
        operationsC1.add(Operations.FILTER_PK_EQ);
        operationsC1.add(Operations.SELECT_INNER_JOIN);

        String strClusterName = "TestCluster1";
        Map<ClusterName, Integer> clusterWithDefaultPriority = new LinkedHashMap<>();
        clusterWithDefaultPriority.put(new ClusterName(strClusterName), Constants.DEFAULT_PRIORITY);

        connector1 = MetadataManagerTestHelper.HELPER.createTestConnector(
                "TestConnector1", dataStoreName, clusterWithDefaultPriority, operationsC1, "actorRef1", new ArrayList<FunctionType>());
        clusterName = MetadataManagerTestHelper.HELPER.
                createTestCluster(strClusterName, dataStoreName, connector1.getName());

        CatalogName catalogName = MetadataManagerTestHelper.HELPER.createTestCatalog("demo").getName();
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes = { new ColumnType(DataType.INT), new ColumnType(DataType.TEXT) };
        String[] partitionKeys = { "id" };
        String[] clusteringKeys = { };
        table1 = MetadataManagerTestHelper.HELPER.createTestTable(
                clusterName, "demo", "table1", columnNames1, columnTypes,
                partitionKeys, clusteringKeys, null);

        String[] columnNames2 = { "id", "email" };
        table2 = MetadataManagerTestHelper.HELPER.createTestTable(
                clusterName, "demo", "table2", columnNames2, columnTypes,
                partitionKeys, clusteringKeys, null);
    }


    @AfterClass
    public void tearDown() throws Exception {
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    private Command getCommand(String statement) {
        List<Object> params = new ArrayList<>();
        params.add(statement);
        params.add("demo");
        return new Command("QID", APICommand.EXPLAIN_PLAN(), params,"sessionTest");
    }

    @Test
    public void invalidExplainRequest() {
        List<Object> params = new ArrayList<>();
        Command cmd = new Command("QID", APICommand.EXPLAIN_PLAN(), params,"sessionTest");
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting error result");
        assertEquals(ErrorResult.class.cast(r).getException().getClass(), UnsupportedException.class,
                "Expecting unsupported exception");
    }

    @Test
    public void explainQualifiedSelect() {
        String inputText = "EXPLAIN PLAN FOR SELECT demo.table1.id FROM demo.table1;";
        Command cmd = getCommand(inputText);
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainNonQualifiedSelect() {
        String inputText = "EXPLAIN PLAN FOR SELECT id FROM table1;";
        Command cmd = getCommand(inputText);
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainInsert() {
        String inputText = "EXPLAIN PLAN FOR INSERT INTO table1(id, user) VALUES (1, 'user1');";
        Command cmd = getCommand(inputText);
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainCreateTable() {
        String inputText = "EXPLAIN PLAN FOR CREATE TABLE new_table ON CLUSTER TestCluster1" +
                " (id int PRIMARY KEY, name text);";
        Command cmd = getCommand(inputText);
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainNotSupportedSelect() {
        String inputText = "EXPLAIN PLAN FOR SELECT id FROM table1 WHERE id > 5;";
        Command cmd = getCommand(inputText);
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting command result");
        ErrorResult result = (ErrorResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        LOG.info(result.getErrorMessage());
        LOG.info(result.getException());
    }

    @Test
    public void explainInvalidSelect() {
        String inputText = "EXPLAIN PLAN FOR SELECT id FROM table1 WHERE unknown > 5;";
        Command cmd = getCommand(inputText);
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting command result");
        ErrorResult result = (ErrorResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        LOG.info(result.getErrorMessage());
        LOG.info(result.getException());
    }

    @Test
    public void explainUnrecognized() {
        String inputText = "EXPLAIN PLAN FOR SELL id FROM table1 WHERE unknown > 5;";
        Command cmd = getCommand(inputText);
        Result r = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting command result");
        ErrorResult result = (ErrorResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        LOG.info(result.getErrorMessage());
        LOG.info(result.getException());
    }

}
