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

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.structures.InMemoryColumnSelector;
import com.stratio.connector.inmemory.datastore.structures.InMemorySelector;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.OrderBy;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.FunctionSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.OrderDirection;
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
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
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
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
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
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 1, "Invalid number of results returned");
        checkResultMetadata(results, usersColumnNames, usersTypes);
    }

    @Test
    public void selectWithFunctionAndAlias(){
        TableMetadata usersTable = buildUsersTable();

        // Build last step
        Set<Operations> requiredOperations = new HashSet<>();
        requiredOperations.add(Operations.SELECT_FUNCTIONS);
        requiredOperations.add(Operations.SELECT_OPERATOR);
        Map<Selector, String> columnMap = new HashMap<>();
        String functionName = "concat";
        String catalog = usersTable.getName().getCatalogName().getName();
        String table = usersTable.getName().getName();
        TableName tableName = new TableName(catalog, table);
        List<Selector> functionSelectors = new ArrayList<>();
        ColumnSelector firstColInFunction = new ColumnSelector(new ColumnName(catalog, table, "id"));
        functionSelectors.add(firstColInFunction);
        ColumnSelector secondColInFunction = new ColumnSelector(new ColumnName(catalog, table, "name"));
        functionSelectors.add(secondColInFunction);
        FunctionSelector fs = new FunctionSelector(tableName, functionName, functionSelectors);
        columnMap.put(fs, functionName);
        ColumnSelector aliasedCol = new ColumnSelector(new ColumnName(catalog, table, "boss"));
        String alias = "manager";
        aliasedCol.setAlias(alias);
        columnMap.put(aliasedCol, alias);
        Map<String, ColumnType> typeMap = new HashMap<>();
        ColumnType functionColType = new ColumnType(DataType.TEXT);
        typeMap.put(functionName, functionColType);
        ColumnType aliasedColType = new ColumnType(DataType.BOOLEAN);
        typeMap.put(alias, aliasedColType);
        Map<Selector, ColumnType> typeMapFromColumnName = new HashMap<>();
        typeMapFromColumnName.put(fs, functionColType);
        typeMapFromColumnName.put(aliasedCol, aliasedColType);
        LogicalStep lastStep = new Select(requiredOperations, columnMap, typeMap, typeMapFromColumnName);

        // Build first step
        List<LogicalStep> initialSteps = new ArrayList<>();
        Set<Operations> operations = new HashSet<>();
        operations.add(Operations.PROJECT);
        ClusterName clusterName = new ClusterName("test_cluster");
        List<ColumnName> columnList = new ArrayList<>();
        columnList.add(firstColInFunction.getColumnName());
        columnList.add(secondColInFunction.getColumnName());
        columnList.add(aliasedCol.getColumnName());
        Project firstStep = new Project(operations, tableName, clusterName, columnList);
        initialSteps.add(firstStep);

        // Connect first step with last step
        firstStep.setNextStep(lastStep);
        lastStep.getPreviousSteps().add(firstStep);

        int pagination = 10;
        LogicalWorkflow lw = new LogicalWorkflow(initialSteps, lastStep, pagination);
        lw.setSqlDirectQuery("SELECT concat(test_catalog.users.id, test_catalog.users.name) AS concat, test_catalog.users.boss AS manager FROM test_catalog.users");
        QueryResult result = null;
        try {
            result = connector.getQueryEngine().execute("qId", lw);
        } catch (ConnectorException e) {
            fail("Test selectWithFunctionAndAlias failed", e);
        }

        assertNotNull(result, "Execution returned a null QueryResult");
        assertEquals(result.getResultPage(), 0, "First page should have the number 0");
        assertTrue(result.isLastResultSet(), "First page should be the last result set");
        assertNotNull(result.getResultSet(), "Result set is null");
        assertFalse(result.getResultSet().isEmpty(), "Result set shouldn't be empty");
        assertEquals(result.getResultSet().size(), pagination, pagination + " rows expected");
        assertEquals(result.getResultSet().getColumnMetadata().size(), 2, "2 columns expected");
        assertEquals(result.getResultSet().getColumnMetadata().get(0).getName().getAlias(),
                "concat",
                "Wrong alias for firstColumn");
        assertEquals(result.getResultSet().getColumnMetadata().get(1).getName().getAlias(),
                "manager",
                "Wrong alias for secondColumn");
        for(int i=0; i<pagination; i++){
            Row row = result.getResultSet().getRows().get(i);
            assertEquals(row.size(), 2, "2 columns expected");
            Iterator<Cell> iter = row.getCellList().iterator();
            int nCol = 0;
            while(iter.hasNext()){
                Object value = iter.next().getValue();
                assertNotNull(value, "Content of the cell is null");
                if(nCol == 0){
                    assertEquals(value, i + "User-" + i, "Content of the cell is wrong");
                } else {
                    assertEquals(value.getClass(), Boolean.class, "Boolean object expected");
                }
                nCol++;
            }
        }
    }

    @Test
    public void selectWithAggregationFunction(){
        TableMetadata usersTable = buildUsersTable();

        // Build last step
        Set<Operations> requiredOperations = new HashSet<>();
        requiredOperations.add(Operations.SELECT_FUNCTIONS);
        requiredOperations.add(Operations.SELECT_OPERATOR);
        Map<Selector, String> columnMap = new HashMap<>();
        String functionName = "count";
        String catalog = usersTable.getName().getCatalogName().getName();
        String table = usersTable.getName().getName();
        TableName tableName = new TableName(catalog, table);
        List<Selector> functionSelectors = new ArrayList<>();
        ColumnSelector firstColInFunction = new ColumnSelector(new ColumnName(catalog, table, "id"));
        functionSelectors.add(firstColInFunction);
        ColumnSelector secondColInFunction = new ColumnSelector(new ColumnName(catalog, table, "name"));
        functionSelectors.add(secondColInFunction);
        ColumnSelector thirdColInFunction = new ColumnSelector(new ColumnName(catalog, table, "boss"));
        functionSelectors.add(thirdColInFunction);
        FunctionSelector fs = new FunctionSelector(tableName, functionName, functionSelectors);
        columnMap.put(fs, functionName);

        Map<String, ColumnType> typeMap = new HashMap<>();
        ColumnType functionColType = new ColumnType(DataType.BIGINT);
        typeMap.put(functionName, functionColType);
        Map<Selector, ColumnType> typeMapFromColumnName = new HashMap<>();
        typeMapFromColumnName.put(fs, functionColType);
        LogicalStep lastStep = new Select(requiredOperations, columnMap, typeMap, typeMapFromColumnName);

        // Build first step
        List<LogicalStep> initialSteps = new ArrayList<>();
        Set<Operations> operations = new HashSet<>();
        operations.add(Operations.PROJECT);
        ClusterName clusterName = new ClusterName("test_cluster");
        List<ColumnName> columnList = new ArrayList<>();
        columnList.add(firstColInFunction.getColumnName());
        columnList.add(secondColInFunction.getColumnName());
        columnList.add(thirdColInFunction.getColumnName());
        Project firstStep = new Project(operations, tableName, clusterName, columnList);
        initialSteps.add(firstStep);

        // Connect first step with last step
        firstStep.setNextStep(lastStep);
        lastStep.getPreviousSteps().add(firstStep);

        int pagination = 10;
        LogicalWorkflow lw = new LogicalWorkflow(initialSteps, lastStep, pagination);
        lw.setSqlDirectQuery("SELECT count(*) AS count FROM test_catalog.users");
        QueryResult result = null;
        try {
            result = connector.getQueryEngine().execute("qId", lw);
        } catch (ConnectorException e) {
            fail("Test selectWithAggregationFunction failed", e);
        }

        assertNotNull(result, "Execution returned a null QueryResult");
        assertEquals(result.getResultPage(), 0, "First page should have the number 0");
        assertTrue(result.isLastResultSet(), "First page should be the last result set");
        assertNotNull(result.getResultSet(), "Result set is null");
        assertFalse(result.getResultSet().isEmpty(), "Result set shouldn't be empty");
        assertEquals(result.getResultSet().size(), 1, "1 row expected");
        assertEquals(result.getResultSet().getColumnMetadata().size(), 1, "1 column expected");
        assertEquals(result.getResultSet().getColumnMetadata().get(0).getName().getAlias(),
                "count",
                "Wrong alias for firstColumn");
        assertEquals(result.getResultSet().getRows().size(), 1, "Only 1 row expected");
        assertEquals(result.getResultSet().getRows().get(0).getCellList().size(), 1, "Only 1 column expected");
        assertEquals(result.getResultSet().getRows().get(0).getCellList().iterator().next().getValue(),
                10,
                "Wrong result");
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
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
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
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
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
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
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
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
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

        OrderBy orderBy = generateOrderByClause(usersTable.getName(), "boss", OrderDirection.DESC);
        Project projectUsers = generateProjectAndSelect(usersColumnNames, usersTypes, usersTable.getName(), orderBy);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) projectUsers));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
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

        OrderBy orderBy = generateOrderByClause(usersTable.getName(), "id", direction);
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

        //Expectations
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

        OrderBy orderBy = generateOrderByClause(usersTable.getName(), "boss", direction);
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
        List<SimpleValue[]> result = ((InMemoryQueryEngine) connector.getQueryEngine()).orderResult(results, workflow);

        //Expectations
        int i = 0;
        for (SimpleValue[] value: result){
            if (direction.equals(OrderDirection.ASC)){
                assertTrue(value[1].getValue().equals(i >= result.size()/2), "Bad Order");
            }else{
                assertTrue(value[1].getValue().equals(i < result.size()/2), "Bad Order");
            }
            i++;
        }
    }

    @Test
    public void testCompareCellsASC() throws UnsupportedException {
        assertEquals(compareCell(OrderDirection.ASC, true, false), -1);
    }


    @Test
    public void testCompareCellsASCCase2() throws UnsupportedException {
        assertEquals(compareCell(OrderDirection.ASC, false, true), 1);
    }

    @Test
    public void testCompareCellsDESC() throws UnsupportedException {
        assertEquals(compareCell(OrderDirection.DESC, true, false), 1);
    }

    @Test
    public void testCompareCellsDESCCase2() throws UnsupportedException {
        assertEquals(compareCell(OrderDirection.DESC, false, true), -1);
    }

    @Test
    public void testCompareCellsDESCNumber() throws UnsupportedException {
        assertEquals(compareCell(OrderDirection.DESC, 1, 2), -1);
    }

    @Test
    public void testCompareCellsASCNumber() throws UnsupportedException {
        assertEquals(compareCell(OrderDirection.ASC, 1, 2), 1);
    }

    @Test
    public void testCompareCellsASCNumberEQ() throws UnsupportedException {
        assertEquals(compareCell(OrderDirection.DESC, 1, 1), 0);
    }

    public int compareCell(OrderDirection direction, Object value1, Object value2) throws UnsupportedException {
        buildUsersTable();
        SimpleValue toBeOrdered = new SimpleValue(null, value1);
        SimpleValue alreadyOrdered =  new SimpleValue(null, value2);
        return ((InMemoryQueryEngine)connector.getQueryEngine()).compareCells(toBeOrdered, alreadyOrdered, direction);
    }

    @Test
    public void testBugCROSSDATA_516(){
        TableMetadata incomeTable = buildIncomeTable();

        Map<String, Object> values = new HashMap<>();
        values.put("id", 5);
        values.put("money",4.4);
        values.put("reten", new Double(-40.4));
        insertTestData(clusterName, incomeTable, values);

        String [] columnNames = {"money", "reten"};
        ColumnType[] usersTypes = {new ColumnType(DataType.DOUBLE), new ColumnType(DataType.DOUBLE)};

        Project project = generateProjectAndSelect(columnNames, usersTypes, incomeTable.getName());

        ColumnSelector left = new ColumnSelector(project.getColumnList().get(1));
        FloatingPointSelector right = new  FloatingPointSelector(project.getTableName(), new Double(-40.40));
        Filter filter = new Filter(singleton(Operations.FILTER_NON_INDEXED_LT), new Relation(left, Operator.LT, right));

        Select s = Select.class.cast(project.getNextStep());
        filter.setNextStep(s);
        project.setNextStep(filter);
        filter.setPrevious(project);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(singletonList((LogicalStep) project));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute("qId", workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 0, "Invalid number of results returned");


    }

}
