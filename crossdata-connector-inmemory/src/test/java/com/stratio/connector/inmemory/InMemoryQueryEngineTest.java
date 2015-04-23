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

import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.selector.InMemoryColumnSelector;
import com.stratio.connector.inmemory.datastore.selector.InMemorySelector;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.*;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.statements.structures.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.testng.Assert.*;

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
    public void simpleSelectFilterTextEQNoResult() {
        TableMetadata usersTable = buildUsersTable();

        String [] usersColumnNames = {"id", "name"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT), new ColumnType(DataType.TEXT)};

        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName());

        ColumnSelector left = new ColumnSelector(projectUsers.getColumnList().get(1));
        StringSelector right = new StringSelector(projectUsers.getTableName(), "User-50");
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

        assertEquals(results.size(), 0, "Invalid number of results returned");
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


    @Test
    public void simpleSelectBoolOrder() {
        TableMetadata usersTable = buildUsersTable();

        String [] usersColumnNames = {"boss"};
        ColumnType[] usersTypes = {new ColumnType(DataType.BOOLEAN)};

        OrderBy orderBy = generateOrderByClausule(usersTable.getName(), "boss", OrderDirection.DESC);
        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName(), orderBy);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), NUM_ROWS, "Invalid number of results returned");
        int i = 0;
        for (Row row: results.getRows()){
            if (i<5){
                assertTrue((Boolean) row.getCell("boss").getValue(),"Invalid Result OrderBy");
            }else{
                assertFalse((Boolean) row.getCell("boss").getValue(), "Invalid Result OrderBy");
            }
            i++;
        }
    }

    @Test
    public void sortNumberASC() throws UnsupportedException, ExecutionException {
        sortNumber(OrderDirection.ASC);
    }

    @Test
    public void sortNumberDES() throws UnsupportedException, ExecutionException {
        sortNumber(OrderDirection.DESC);
    }

    public void sortNumber(OrderDirection direction) throws UnsupportedException, ExecutionException {
        TableMetadata usersTable = buildUsersTable();
        String [] usersColumnNames = {"id"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT)};

        OrderBy orderBy = generateOrderByClausule(usersTable.getName(), "id", direction);
        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName(), orderBy);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        InMemorySelector column = new InMemoryColumnSelector("id");
        List<SimpleValue[]> results = new ArrayList<>();
        results.add(new SimpleValue[]{new SimpleValue(column, 2)});
        results.add(new SimpleValue[]{new SimpleValue(column, 1)});
        results.add(new SimpleValue[]{new SimpleValue(column, 3)});
        results.add(new SimpleValue[]{new SimpleValue(column, 4)});

        //Experimentation
        List<SimpleValue[]> result = ((InMemoryQueryEngine)connector.getQueryEngine()).orderResult(results, workflow);

        //Espectations
        int i =direction.equals(OrderDirection.DESC) ? result.size():1;
        for (SimpleValue[] value: result){
            assertEquals(value[0].getValue(), direction.equals(OrderDirection.DESC) ? i-- : i++, "Invalid Order");
        }
    }

    @Test
    public void sortBoolDES() throws UnsupportedException, ExecutionException {
        sortBool(OrderDirection.DESC);
    }

    @Test
    public void sortBoolASC() throws UnsupportedException, ExecutionException {
        sortBool(OrderDirection.ASC);
    }

    public void sortBool(OrderDirection direction) throws UnsupportedException, ExecutionException {
        TableMetadata usersTable = buildUsersTable();
        String [] usersColumnNames = {"id","boss"};
        ColumnType[] usersTypes = {new ColumnType(DataType.INT), new ColumnType(DataType.BOOLEAN)};

        OrderBy orderBy = generateOrderByClausule(usersTable.getName(), "boss", direction);
        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName(), orderBy);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        InMemorySelector columnBoss = new InMemoryColumnSelector("boss");
        InMemorySelector columnId = new InMemoryColumnSelector("id");
        List<SimpleValue[]> results = new ArrayList<>();
        results.add(new SimpleValue[]{new SimpleValue(columnId, 1),new SimpleValue(columnBoss, false)});
        results.add(new SimpleValue[]{new SimpleValue(columnId, 2),new SimpleValue(columnBoss, true)});
        results.add(new SimpleValue[]{new SimpleValue(columnId, 3),new SimpleValue(columnBoss, false)});
        results.add(new SimpleValue[]{new SimpleValue(columnId, 4),new SimpleValue(columnBoss, true)});


        //Experimentation
        List<SimpleValue[]> result = ((InMemoryQueryEngine)connector.getQueryEngine()).orderResult(results, workflow);

        //Espectations
        int i = 0;
        for (SimpleValue[] value: result){
            if (direction.equals(OrderDirection.ASC)){
                assertTrue((Boolean) value[1].getValue().equals(i >= result.size()/2) , "Bad Order");
            }else{
                assertTrue((Boolean) value[1].getValue().equals(i < result.size()/2) , "Bad Order");
            }
            i++;
        }
    }


    @Test
    public void testCompareCellsASC() throws UnsupportedException {
        testCompareCell(OrderDirection.ASC, true, false, -1);
    }


    @Test
    public void testCompareCellsASCCase2() throws UnsupportedException {
        testCompareCell(OrderDirection.ASC, false, true, 1);
    }

    @Test
    public void testCompareCellsDESC() throws UnsupportedException {
        testCompareCell(OrderDirection.DESC, true, false, 1);
    }

    @Test
    public void testCompareCellsDESCCase2() throws UnsupportedException {
        testCompareCell(OrderDirection.DESC, false, true, -1);
    }

    @Test
    public void testCompareCellsDESCNumber() throws UnsupportedException {
        testCompareCell(OrderDirection.DESC, 1, 2, -1);
    }

    @Test
    public void testCompareCellsASCNumber() throws UnsupportedException {
        testCompareCell(OrderDirection.ASC, 1, 2, 1);
    }

    @Test
    public void testCompareCellsASCNumberEQ() throws UnsupportedException {
        testCompareCell(OrderDirection.DESC, 1, 1, 0);
    }

    public void testCompareCell(OrderDirection direction, Object value1, Object value2, int expected) throws UnsupportedException {

        buildUsersTable();

        SimpleValue toBeOrdered = new SimpleValue(null, value1);
        SimpleValue alreadyOrdered =  new SimpleValue(null, value2);

        int result = ((InMemoryQueryEngine)connector.getQueryEngine()).compareCells(toBeOrdered, alreadyOrdered, direction);

        assertEquals(result, expected);

    }

}
