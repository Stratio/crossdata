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

package com.stratio.connector.inmemory;

import static java.util.Collections.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.connector.ConnectorClusterConfig;
import com.stratio.crossdata.common.connector.IConfiguration;
import com.stratio.crossdata.common.connector.IConnector;
import com.stratio.crossdata.common.connector.IConnectorApp;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.InitializationException;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.security.ICredentials;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;

/**
 * Query engine test.
 */
public class InMemoryQueryEngineTest extends InMemoryQueryEngineTestParent {

    @Test
    public void simpleSelect() {

        TableMetadata usersTable = buildUsersTable();

        String [] usersColumnNames = {"name"};
        ColumnType[] usersTypes = {new ColumnType(DataType.TEXT)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), NUM_ROWS, "Invalid number of results returned");
        checkResultMetadata(results, usersColumnNames, usersTypes);
    }

    @Test
    public void simpleSelectAllColumns() {
        TableMetadata usersTable = buildUsersTable();

        String [] usersColumnNames = {"id", "name", "boss"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT),
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.BOOLEAN)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), NUM_ROWS, "Invalid number of results returned");
        checkResultMetadata(results, usersColumnNames, usersTypes);
    }

    @Test
    public void simpleSelectFilterTextEQ() {
        TableMetadata usersTable = buildUsersTable();

        String [] usersColumnNames = {"id", "name"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT), new ColumnType(DataType.TEXT)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());

        ColumnSelector left = new ColumnSelector(projectUsers.getColumnList().get(1));
        StringSelector right = new StringSelector(projectUsers.getTableName(), "User-9");
        Filter filter = new Filter(
                singleton(Operations.FILTER_NON_INDEXED_EQ),
                new Relation(left, Operator.EQ, right));

        Select s = Select.class.cast(projectUsers.getNextStep());
        filter.setNextStep(s);
        projectUsers.setNextStep(filter);
        filter.setPrevious(projectUsers);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 1, "Invalid number of results returned");
        checkResultMetadata(results, usersColumnNames, usersTypes);
    }

    @Test
    public void simpleSelectFilterBoolEQ() {
        TableMetadata usersTable = buildUsersTable();

        String [] usersColumnNames = {"boss"};
        ColumnType[] usersTypes = {new ColumnType(DataType.BOOLEAN)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());

        ColumnSelector left = new ColumnSelector(projectUsers.getColumnList().get(0));
        BooleanSelector right = new BooleanSelector(projectUsers.getTableName(), true);
        Filter filter = new Filter(
                singleton(Operations.FILTER_NON_INDEXED_EQ),
                new Relation(left, Operator.EQ, right));

        Select s = Select.class.cast(projectUsers.getNextStep());
        filter.setNextStep(s);
        projectUsers.setNextStep(filter);
        filter.setPrevious(projectUsers);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), NUM_ROWS / 2, "Invalid number of results returned");
        checkResultMetadata(results, usersColumnNames, usersTypes);
    }

    @Test
    public void simpleSelectFilterIntEQ() {

        TableMetadata usersTable = buildUsersTable();
        String [] usersColumnNames = {"id"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());

        ColumnSelector left = new ColumnSelector(projectUsers.getColumnList().get(0));
        IntegerSelector right = new IntegerSelector(projectUsers.getTableName(), 5);
        Filter filter = new Filter(
                singleton(Operations.FILTER_NON_INDEXED_EQ),
                new Relation(left, Operator.EQ, right));

        Select s = Select.class.cast(projectUsers.getNextStep());
        filter.setNextStep(s);
        projectUsers.setNextStep(filter);
        filter.setPrevious(projectUsers);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 1, "Invalid number of results returned");
        checkResultMetadata(results, usersColumnNames, usersTypes);
    }

    @Test
    public void simpleSelectFilterNonIndexedIntGT() {

        TableMetadata usersTable = buildUsersTable();

        String [] usersColumnNames = {"id"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());

        ColumnSelector left = new ColumnSelector(projectUsers.getColumnList().get(0));
        IntegerSelector right = new IntegerSelector(projectUsers.getTableName(), NUM_ROWS/2);
        Filter filter = new Filter(singleton(Operations.FILTER_NON_INDEXED_GT),
                new Relation(left, Operator.GT, right));

        Select s = Select.class.cast(projectUsers.getNextStep());
        filter.setNextStep(s);
        projectUsers.setNextStep(filter);
        filter.setPrevious(projectUsers);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), (NUM_ROWS/2)-1, "Invalid number of results returned");
        checkResultMetadata(results, usersColumnNames, usersTypes);
    }

}
