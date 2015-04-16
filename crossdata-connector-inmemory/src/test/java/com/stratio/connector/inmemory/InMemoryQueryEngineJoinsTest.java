/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.connector.inmemory;

import com.stratio.crossdata.common.connector.ConnectorClusterConfig;
import com.stratio.crossdata.common.connector.IConfiguration;
import com.stratio.crossdata.common.connector.IConnector;
import com.stratio.crossdata.common.connector.IConnectorApp;
import com.stratio.crossdata.common.data.*;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.InitializationException;
import com.stratio.crossdata.common.logicalplan.*;
import com.stratio.crossdata.common.metadata.*;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.security.ICredentials;
import com.stratio.crossdata.common.statements.structures.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

import static java.util.Collections.singleton;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Query engine test.
 */
public class InMemoryQueryEngineJoinsTest extends InMemoryQueryEngineTestParent {

    @Test
    public void simpleJoin() {

        TableMetadata usersTable = buildUsersTable();
        TableMetadata phonesTable = buildPhonesTable();

        String [] usersColumnNames = {"id", "name"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT), new ColumnType(DataType.TEXT)};

        String [] phonesColumnNames = {"id", "user_id", "phone"};
        ColumnType[] phonesTypes = {new ColumnType(DataType.INT), new ColumnType(DataType.INT), new ColumnType(DataType.TEXT)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());
        Project projectPhones = generateProjectAndSelect(phonesColumnNames, phonesTypes, phonesTable.getName());

        Join join = getJoin(usersTable, phonesTable, projectUsers, projectPhones);

        Select select = getSelect(usersTable, phonesTable);

        join.setNextStep(select);
        select.setPrevious(join);

        List<LogicalStep> initialSteps = new ArrayList<>();
        initialSteps.add(projectUsers);
        initialSteps.add(projectPhones);

        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);
        workflow.setLastStep(select);

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 10, "Invalid number of results returned");
        checkResultMetadata(results, new String[]{"name", "phone"}, new ColumnType[]{new ColumnType(DataType.TEXT), new ColumnType(DataType.TEXT)});
    }


    @Test
    public void simpleJoinWithFilters() {

        TableMetadata usersTable = buildUsersTable();
        TableMetadata phonesTable = buildPhonesTable();

        String [] usersColumnNames = {"id", "name"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT), new ColumnType(DataType.TEXT)};

        String [] phonesColumnNames = {"id", "user_id", "phone"};
        ColumnType[] phonesTypes = {new ColumnType(DataType.INT), new ColumnType(DataType.INT), new ColumnType(DataType.TEXT)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());
        Project projectPhones = generateProjectAndSelect(phonesColumnNames, phonesTypes, phonesTable.getName());

        Join join = getJoin(usersTable, phonesTable, projectUsers, projectPhones);

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


        Select select = getSelect(usersTable, phonesTable);

        join.setNextStep(select);
        select.setPrevious(join);

        List<LogicalStep> initialSteps = new ArrayList<>();
        initialSteps.add(projectUsers);
        initialSteps.add(projectPhones);

        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);
        workflow.setLastStep(select);

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 1, "Invalid number of results returned");
        checkResultMetadata(results, new String[]{"name", "phone"}, new ColumnType[]{new ColumnType(DataType.TEXT), new ColumnType(DataType.TEXT)});
    }

    private Select getSelect(TableMetadata usersTable, TableMetadata phonesTable) {
        Map<Selector, String> columnMap = new LinkedHashMap<>();
        Map<String, ColumnType> typeMap = new LinkedHashMap<>();
        Map<Selector, ColumnType> typeMapFromColumnName = new LinkedHashMap<>();

        columnMap.put(new ColumnSelector(new ColumnName(usersTable.getName(), "name")), "name");
        columnMap.put(new ColumnSelector(new ColumnName(phonesTable.getName(), "phone")), "phone");

        typeMap.put("name", new ColumnType(DataType.TEXT));
        typeMap.put("phone", new ColumnType(DataType.TEXT));

        typeMapFromColumnName.put(new ColumnSelector(new ColumnName(usersTable.getName(), "name")), new ColumnType(DataType.TEXT));
        typeMapFromColumnName.put(new ColumnSelector(new ColumnName(phonesTable.getName(), "phone")), new ColumnType(DataType.TEXT));

        return new Select(
                Collections.singleton(Operations.SELECT_OPERATOR),
                columnMap,
                typeMap,
                typeMapFromColumnName);
    }

    private Join getJoin(TableMetadata usersTable, TableMetadata phonesTable, Project projectUsers, Project projectPhones) {
        Set<Operations> joinOperation = new HashSet<>();
        joinOperation.add(Operations.SELECT_INNER_JOIN);

        Join join = new Join(joinOperation, "innerJoin");
        join.getSourceIdentifiers().add(usersTable.getName().getQualifiedName());
        join.getSourceIdentifiers().add(phonesTable.getName().getQualifiedName());

        Selector right = new ColumnSelector(new ColumnName(phonesTable.getName(), "user_id"));
        Selector left = new ColumnSelector(new ColumnName(usersTable.getName(), "id"));

        join.addJoinRelation(new Relation(left, Operator.EQ, right));
        projectUsers.getNextStep().setNextStep(join);
        projectPhones.getNextStep().setNextStep(join);
        join.setPreviousSteps(Arrays.asList(projectUsers.getNextStep()));
        return join;
    }

}
