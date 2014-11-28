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

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.ask.APICommand;
import com.stratio.crossdata.common.ask.Command;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.core.metadata.MetadataManagerTestHelper;
import com.stratio.crossdata.core.parser.Parser;
import com.stratio.crossdata.core.planner.Planner;
import com.stratio.crossdata.core.validator.Validator;

/**
 * Explain plan tests using the API manager
 */
public class ExplainPlanAPIManagerTest extends MetadataManagerTestHelper{

    private final Parser parser = new Parser();
    private final Validator validator = new Validator();
    private final Planner planner = new Planner();

    private final APIManager manager = new APIManager(parser, validator, planner);

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
        super.setUp();
        DataStoreName dataStoreName = createTestDatastore();

        Set<Operations> operationsC1 = new HashSet<>();
        operationsC1.add(Operations.CREATE_TABLE);
        operationsC1.add(Operations.INSERT);
        operationsC1.add(Operations.PROJECT);
        operationsC1.add(Operations.SELECT_OPERATOR);
        operationsC1.add(Operations.FILTER_PK_EQ);
        operationsC1.add(Operations.SELECT_INNER_JOIN);

        connector1 = createTestConnector("TestConnector1", dataStoreName, new HashSet<ClusterName>(),operationsC1, "actorRef1");
        clusterName = createTestCluster("TestCluster1", dataStoreName, connector1.getName());

        CatalogName catalogName = createTestCatalog("demo");
        String[] columnNames1 = { "id", "user" };
        ColumnType[] columnTypes = { ColumnType.INT, ColumnType.TEXT };
        String[] partitionKeys = { "id" };
        String[] clusteringKeys = { };
        table1 = createTestTable(clusterName, "demo", "table1", columnNames1, columnTypes, partitionKeys,
                clusteringKeys);

        String[] columnNames2 = { "id", "email" };
        table2 = createTestTable(clusterName, "demo", "table2", columnNames2, columnTypes, partitionKeys,
                clusteringKeys);


    }


    private Command getCommand(String statement){
        List<Object> params = new ArrayList<>();
        params.add(statement);
        params.add("demo");
        return new Command("QID", APICommand.EXPLAIN_PLAN(), params);
    }

    @Test
    public void invalidExplainRequest(){
        List<Object> params = new ArrayList<>();
        Command cmd = new Command("QID", APICommand.EXPLAIN_PLAN(), params);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting error result");
        assertEquals(ErrorResult.class.cast(r).getException().getClass(), UnsupportedException.class,
                "Expecting unsupported exception");
    }

    @Test
    public void explainQualifiedSelect(){
        String inputText = "SELECT demo.table1.id FROM demo.table1;";
        Command cmd = getCommand(inputText);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainNonQualifiedSelect(){
        String inputText = "SELECT id FROM table1;";
        Command cmd = getCommand(inputText);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainInsert(){
        String inputText = "INSERT INTO table1(id, user) VALUES (1, 'user1');";
        Command cmd = getCommand(inputText);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainCreateTable(){
        String inputText = "CREATE TABLE new_table ON CLUSTER TestCluster1" +
                " (id int PRIMARY KEY, name text);";
        Command cmd = getCommand(inputText);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), CommandResult.class, "Expecting command result");
        CommandResult result = (CommandResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        assertNotNull(result.getResult(), "Expecting string results");
        LOG.info(result.getResult());
    }

    @Test
    public void explainNotSupportedSelect(){
        String inputText = "SELECT id FROM table1 WHERE id > 5;";
        Command cmd = getCommand(inputText);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting command result");
        ErrorResult result = (ErrorResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        LOG.info(result.getErrorMessage());
        LOG.info(result.getException());
    }

    @Test
    public void explainInvalidSelect(){
        String inputText = "SELECT id FROM table1 WHERE unknown > 5;";
        Command cmd = getCommand(inputText);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting command result");
        ErrorResult result = (ErrorResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        LOG.info(result.getErrorMessage());
        LOG.info(result.getException());
    }

    @Test
    public void explainUnrecognized(){
        String inputText = "SELL id FROM table1 WHERE unknown > 5;";
        Command cmd = getCommand(inputText);
        Result r = manager.processRequest(cmd);
        assertNotNull(r, "Expecting result");
        assertEquals(r.getClass(), ErrorResult.class, "Expecting command result");
        ErrorResult result = (ErrorResult) r;
        assertNotNull(result.getQueryId(), "Expecting query id on results");
        LOG.info(result.getErrorMessage());
        LOG.info(result.getException());
    }

}
