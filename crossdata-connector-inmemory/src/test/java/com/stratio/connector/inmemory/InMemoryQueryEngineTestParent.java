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
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.metadata.*;
import com.stratio.crossdata.common.security.ICredentials;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import org.testng.annotations.BeforeTest;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by lcisneros on 14/04/15.
 */
public abstract class InMemoryQueryEngineTestParent {

    protected static final int NUM_ROWS = 10;
    protected ClusterName clusterName = null;
    protected IConnectorApp connectorApp = new MockConnectorApp();
    protected IConnector connector = new InMemoryConnector(connectorApp);
    protected CatalogMetadata catalogMetadata;

    @BeforeTest
    public void prepateTest() throws Exception {
        startConnector();
        catalogMetadata = new CatalogMetadata(new CatalogName("test_catalog"), null, null);
        connector.getMetadataEngine().createCatalog(clusterName, catalogMetadata);
    }

    protected IConnector startConnector() {
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
                @Override
                public int hashCode() {
                    return super.hashCode();
                }
            }, config);
        } catch (ConnectionException e) {
            fail("Cannot connect to inmemory cluster", e);
        }
        return connector;
    }

    protected TableMetadata createTestTable(String tableName, Map<String,DataType> columnsDef) {
        TableMetadata tableMetadata = null;

        try {
            tableMetadata = buildTableMetadata(clusterName, tableName, columnsDef);
            connector.getMetadataEngine().createTable(clusterName, tableMetadata);
        } catch (ConnectorException e) {
            fail("Cannot create test environment", e);
        }

        return tableMetadata;
    }

    protected TableMetadata buildTableMetadata(ClusterName targetCluster, String name, Map<String, DataType> columnsDef) {
        TableName tableName = new TableName("test_catalog", name);
        Map<Selector, Selector> options = new HashMap<>();

        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();

        LinkedHashMap<ColumnName, ColumnMetadata> columns = buildColumns(tableName, partitionKey, columnsDef);

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        TableMetadata table =
                new TableMetadata(tableName, options, columns, indexes, targetCluster, partitionKey,
                        clusterKey);
        return table;
    }

    protected LinkedHashMap<ColumnName, ColumnMetadata> buildColumns(TableName targetTable, LinkedList<ColumnName> partitionKey, Map<String, DataType> columnsDef){
        LinkedHashMap<ColumnName, ColumnMetadata> result = new LinkedHashMap<>();
        Object[] parameters = { };

        for(String columname:columnsDef.keySet()){
            result.put(new ColumnName(targetTable, columname),
                    new ColumnMetadata(new ColumnName(targetTable, columname), parameters,
                            new ColumnType(columnsDef.get(columname)))
            );
        }

        partitionKey.add(result.keySet().iterator().next());

        return result;
    }

    protected TableMetadata buildUsersTable(){

        Map<String, DataType> columns = new LinkedHashMap<>();
        columns.put("id", DataType.INT);
        columns.put("name", DataType.TEXT);
        columns.put("boss", DataType.BOOLEAN);

        TableMetadata table = createTestTable("users", columns);

        for(int i = 0; i < NUM_ROWS; i++){
            Map<String, Object> values = new HashMap<>();
            values.put("id", i);
            values.put("name","User-"+i);
            values.put("boss",i%2==0);
            insertTestData(clusterName, table, values);
        }

        return table;
    }

    protected TableMetadata buildPhonesTable(){

        Map<String, DataType> columns = new HashMap<>();
        columns.put("id", DataType.INT);
        columns.put("user_id", DataType.INT);
        columns.put("phone", DataType.TEXT);

        TableMetadata table = createTestTable("phones", columns);

        for(int i = 0; i <= 9; i++){
            Map<String, Object> values = new HashMap<>();
            values.put("id", i);
            values.put("user_id", i);
            values.put("phone","555-555-"+i);
            insertTestData(clusterName, table, values);
        }

        return table;
    }

    protected void insertTestData(ClusterName targetCluster, TableMetadata targetTable, Map<String, Object> values) {
        try {
            Row row = new Row();
            for (String columnName: values.keySet()) {
                row.addCell(columnName, new Cell(values.get(columnName)));
            }
            connector.getStorageEngine().insert(targetCluster, targetTable, row, false);
        } catch (ConnectorException e) {
            fail("Insertion failed", e);
        }
    }

    protected Project generateProjectAndSelect(String [] columnNames, ColumnType [] types, TableName table){

        Project project = new Project(
                Collections.singleton(Operations.PROJECT),
                table,
                clusterName);

        for(String columnName: columnNames) {
            project.addColumn(new ColumnName(table, columnName));
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

    protected void checkResultMetadata(ResultSet results, String [] columnNames, ColumnType [] types){
        assertEquals(results.getColumnMetadata().size(), columnNames.length, "Expecting one column");
        for(int index = 0; index < columnNames.length; index++) {
            assertEquals(results.getColumnMetadata().get(index).getName().getAlias(), columnNames[index],
                    "Expecting matching column name");
            assertEquals(results.getColumnMetadata().get(index).getColumnType(), types[index],
                    "Expecting matching column type");
        }
    }
}
