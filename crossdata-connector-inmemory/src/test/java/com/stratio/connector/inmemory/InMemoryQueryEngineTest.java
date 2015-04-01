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
public class InMemoryQueryEngineTest {

    private ClusterName clusterName = null;

    private TableMetadata tableMetadata = null;

    private IConnectorApp connectorApp = new MockConnectorApp();

    private IConnector connector = new InMemoryConnector(connectorApp);

    private static final int NUM_ROWS = 100;

    public IConnector startConnector() {
        try {
            connector.init(new IConfiguration() {
                @Override public int hashCode() {
                    return super.hashCode();
                }
            });
        } catch (InitializationException e) {
            fail("Failed to init the connector", e);
        }

        Map<String,String> clusterOptions = new HashMap<>();
        Map<String,String> connectorOptions = new HashMap<>();
        clusterOptions.put("TableRowLimit", "1000");
        clusterName = new ClusterName("test_cluster");
        ConnectorClusterConfig config = new ConnectorClusterConfig(clusterName, connectorOptions, clusterOptions);

        try {
            connector.connect(new ICredentials() {
                @Override public int hashCode() {
                    return super.hashCode();
                }
            }, config);
        } catch (ConnectionException e) {
            fail("Cannot connect to inmemory cluster", e);
        }
        return connector;
    }

    private TableMetadata getTestTableMetadata(ClusterName targetCluster) {
        TableName targetTable = new TableName("test_catalog", "types");
        Map<Selector, Selector> options = new HashMap<>();
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        Object[] parameters = { };
        columns.put(new ColumnName(targetTable, "text_column"),
                new ColumnMetadata(new ColumnName(targetTable, "text_column"), parameters,
                        new ColumnType(DataType.TEXT))
        );
        partitionKey.add(columns.keySet().iterator().next());

        columns.put(new ColumnName(targetTable, "int_column"),
                new ColumnMetadata(new ColumnName(targetTable, "int_column"), parameters,
                        new ColumnType(DataType.INT))
        );
        columns.put(new ColumnName(targetTable, "bool_column"),
                new ColumnMetadata(new ColumnName(targetTable, "bool_column"), parameters,
                        new ColumnType(DataType.BOOLEAN))
        );

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        TableMetadata table =
                new TableMetadata(targetTable, options, columns, indexes, targetCluster, partitionKey,
                        clusterKey);
        return table;
    }

    public void createTestTable() {

        CatalogMetadata catalogMetadata = new CatalogMetadata(new CatalogName("test_catalog"), null, null);
        try {
            connector.getMetadataEngine().createCatalog(clusterName, catalogMetadata);
            tableMetadata = getTestTableMetadata(clusterName);
            connector.getMetadataEngine().createTable(clusterName, tableMetadata);
        } catch (ConnectorException e) {
            fail("Cannot create test environment", e);
        }
    }

    public void insertTestData(ClusterName targetCluster, TableMetadata targetTable, int numberRows) {
        try {
            for (int index = 0; index < numberRows; index++) {
                Row row = new Row();
                row.addCell("text_column", new Cell("text_" + index));
                row.addCell("int_column", new Cell(index));
                row.addCell("bool_column", new Cell(index % 2 == 0));
                connector.getStorageEngine().insert(targetCluster, targetTable, row, false);
            }
        } catch (ConnectorException e) {
            fail("Insertion failed", e);
        }
    }

    @BeforeClass
    public void beforeClass() {
        startConnector();
        createTestTable();
        insertTestData(clusterName, tableMetadata, NUM_ROWS);
    }

    public Project generateProjectAndSelect(String [] columnNames, ColumnType [] types){
        Project project = new Project(
                Collections.singleton(Operations.PROJECT),
                tableMetadata.getName(),
                clusterName);
        for(String columnName: columnNames) {
            project.addColumn(new ColumnName(tableMetadata.getName(), columnName));
        }

        Map<Selector, String> columnMap = new LinkedHashMap<>();
        Map<String, ColumnType> typeMap = new LinkedHashMap<>();
        Map<Selector, ColumnType> typeMapFromColumnName = new LinkedHashMap<>();

        int index = 0;
        for(ColumnName column : project.getColumnList()){
            ColumnSelector cs = new ColumnSelector(column);
            columnMap.put(cs, column.getName());
            typeMap.put(column.getName(), types[index]);
            typeMapFromColumnName.put(cs, types[index]);
            index++;
        }
        Select select = new Select(
                Collections.singleton(Operations.SELECT_OPERATOR),
                columnMap,
                typeMap,
                typeMapFromColumnName);

        //Link the elements
        project.setNextStep(select);
        return project;
    }

    public void checkResultMetadata(ResultSet results, String [] columnNames, ColumnType [] types){
        assertEquals(results.getColumnMetadata().size(), columnNames.length, "Expecting one column");
        for(int index = 0; index < columnNames.length; index++) {
            assertEquals(results.getColumnMetadata().get(index).getName().getAlias(), columnNames[index],
                    "Expecting matching column name");
            assertEquals(results.getColumnMetadata().get(index).getColumnType(), types[index],
                    "Expecting matching column type");
        }
    }

    @Test
    public void simpleSelect() {
        String [] columnNames = {"text_column"};
        ColumnType[] types = { new ColumnType(DataType.TEXT) };

        Project project = generateProjectAndSelect(columnNames, types);
        LogicalWorkflow workflow = new LogicalWorkflow(Arrays.asList((LogicalStep)project));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), NUM_ROWS, "Invalid number of results returned");
        checkResultMetadata(results, columnNames, types);
    }

    @Test
    public void simpleSelectAllColumns() {
        String [] columnNames = {"text_column", "int_column", "bool_column"};
        ColumnType[] types = {
                new ColumnType(DataType.TEXT),
                new ColumnType(DataType.INT),
                new ColumnType(DataType.BOOLEAN) };

        Project project = generateProjectAndSelect(columnNames, types);
        LogicalWorkflow workflow = new LogicalWorkflow(Arrays.asList((LogicalStep)project));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), NUM_ROWS, "Invalid number of results returned");
        checkResultMetadata(results, columnNames, types);
    }

    @Test
    public void simpleSelectFilterTextEQ() {
        String [] columnNames = {"text_column"};
        ColumnType[] types = { new ColumnType(DataType.TEXT) };

        Project project = generateProjectAndSelect(columnNames, types);

        ColumnSelector left = new ColumnSelector(project.getColumnList().get(0));
        StringSelector right = new StringSelector(project.getTableName(), "text_42");
        Filter filter = new Filter(
                Collections.singleton(Operations.FILTER_NON_INDEXED_EQ),
                new Relation(left, Operator.EQ, right));

        Select s = Select.class.cast(project.getNextStep());
        filter.setNextStep(s);
        project.setNextStep(filter);
        filter.setPrevious(project);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(Arrays.asList((LogicalStep)project));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 1, "Invalid number of results returned");
        checkResultMetadata(results, columnNames, types);
    }

    @Test
    public void simpleSelectFilterBoolEQ() {
        String [] columnNames = {"bool_column"};
        ColumnType[] types = { new ColumnType(DataType.BOOLEAN) };

        Project project = generateProjectAndSelect(columnNames, types);

        ColumnSelector left = new ColumnSelector(project.getColumnList().get(0));
        BooleanSelector right = new BooleanSelector(project.getTableName(), true);
        Filter filter = new Filter(
                Collections.singleton(Operations.FILTER_NON_INDEXED_EQ),
                new Relation(left, Operator.EQ, right));

        Select s = Select.class.cast(project.getNextStep());
        filter.setNextStep(s);
        project.setNextStep(filter);
        filter.setPrevious(project);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(Arrays.asList((LogicalStep)project));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), NUM_ROWS / 2, "Invalid number of results returned");
        checkResultMetadata(results, columnNames, types);
    }

    @Test
    public void simpleSelectFilterIntEQ() {
        String [] columnNames = {"int_column"};
        ColumnType[] types = { new ColumnType(DataType.INT) };

        Project project = generateProjectAndSelect(columnNames, types);

        ColumnSelector left = new ColumnSelector(project.getColumnList().get(0));
        IntegerSelector right = new IntegerSelector(project.getTableName(), 42);
        Filter filter = new Filter(
                Collections.singleton(Operations.FILTER_NON_INDEXED_EQ),
                new Relation(left, Operator.EQ, right));

        Select s = Select.class.cast(project.getNextStep());
        filter.setNextStep(s);
        project.setNextStep(filter);
        filter.setPrevious(project);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(Arrays.asList((LogicalStep)project));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), 1, "Invalid number of results returned");
        checkResultMetadata(results, columnNames, types);
    }

    @Test
    public void simpleSelectFilterNonIndexedIntGT() {
        String [] columnNames = {"int_column"};
        ColumnType[] types = { new ColumnType(DataType.INT) };

        Project project = generateProjectAndSelect(columnNames, types);

        ColumnSelector left = new ColumnSelector(project.getColumnList().get(0));
        IntegerSelector right = new IntegerSelector(project.getTableName(), NUM_ROWS/2);
        Filter filter = new Filter(
                Collections.singleton(Operations.FILTER_NON_INDEXED_GT),
                new Relation(left, Operator.GT, right));

        Select s = Select.class.cast(project.getNextStep());
        filter.setNextStep(s);
        project.setNextStep(filter);
        filter.setPrevious(project);
        s.setPrevious(filter);
        LogicalWorkflow workflow = new LogicalWorkflow(Arrays.asList((LogicalStep)project));

        ResultSet results = null;
        try {
            QueryResult result = connector.getQueryEngine().execute(workflow);
            results = result.getResultSet();
        } catch (ConnectorException e) {
            fail("Cannot retrieve data", e);
        }

        assertEquals(results.size(), (NUM_ROWS/2)-1, "Invalid number of results returned");
        checkResultMetadata(results, columnNames, types);
    }

}
