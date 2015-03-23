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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.stratio.connector.inmemory.datastore.InMemoryCatalog;
import com.stratio.connector.inmemory.datastore.InMemoryDatastore;
import com.stratio.connector.inmemory.datastore.InMemoryTable;
import com.stratio.connector.inmemory.datastore.datatypes.AbstractInMemoryDataType;
import com.stratio.crossdata.common.connector.IMetadataEngine;
import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Class that implements the {@link com.stratio.crossdata.common.connector.IMetadataEngine}.
 */
public class InMemoryMetadataEngine implements IMetadataEngine {

    /**
     * Link to the in memory connector.
     */
    private final InMemoryConnector connector;

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(InMemoryMetadataEngine.class);

    /**
     * Class constructor.
     * @param connector The linked {@link com.stratio.connector.inmemory.InMemoryConnector}.
     */
    public InMemoryMetadataEngine(InMemoryConnector connector){
        this.connector = connector;
    }

    @Override
    public void createCatalog(ClusterName targetCluster, CatalogMetadata catalogMetadata)
            throws ConnectorException {
        LOG.info("Creating catalog " + catalogMetadata.getName().getQualifiedName() + " on " + targetCluster);
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            datastore.createCatalog(catalogMetadata.getName().getQualifiedName());
        }else{
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }

    @Override public void alterCatalog(ClusterName targetCluster, CatalogName catalogName,
            Map<Selector, Selector> options) throws ConnectorException {
        throw new UnsupportedException("Alter Catalog not implemented yet");
    }

    @Override
    public void createTable(ClusterName targetCluster, TableMetadata tableMetadata)
            throws ConnectorException {
        LOG.info("Creating table " + tableMetadata.getName().getQualifiedName() + " on " + targetCluster);
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            String catalogName = tableMetadata.getName().getCatalogName().getQualifiedName();
            String tableName = tableMetadata.getName().getName();

            String [] columnNames = new String[tableMetadata.getColumns().size()];
            Class [] columnTypes = new Class[tableMetadata.getColumns().size()];

            int index = 0;
            for(Map.Entry<ColumnName, ColumnMetadata> column: tableMetadata.getColumns().entrySet()){
                columnNames[index] = column.getKey().getName();
                columnTypes[index] = column.getValue().getColumnType().getDbClass();
                // Check if it's a native data type
                AbstractInMemoryDataType inMemoryDataType = AbstractInMemoryDataType.castToNativeDataType(
                        column.getValue().getColumnType().getDbType());
                if(inMemoryDataType != null){
                    columnTypes[index] = inMemoryDataType.getClazz();
                }
                index++;
            }

            List<String> primaryKey = new ArrayList<>();
            for(ColumnName column : tableMetadata.getPrimaryKey()){
                primaryKey.add(column.getName());
            }

            try {
                //Create catalog if not exists
                if(!datastore.existsCatalog(catalogName)){
                    datastore.createCatalog(catalogName);
                }
                datastore.createTable(catalogName, tableName, columnNames, columnTypes, primaryKey);
            } catch (Exception e) {
                throw new ExecutionException(e);
            }

        } else {
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }

    @Override
    public void dropCatalog(ClusterName targetCluster, CatalogName name) throws ConnectorException {
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            try {
                datastore.dropCatalog(name.getQualifiedName());
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }else{
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }

    @Override
    public void dropTable(ClusterName targetCluster, TableName name) throws ConnectorException {
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            try {
                datastore.dropTable(name.getCatalogName().getQualifiedName(), name.getName());
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }else{
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }

    @Override
    public void alterTable(ClusterName targetCluster, TableName name, AlterOptions alterOptions)
            throws ConnectorException {
        throw new UnsupportedException("Altering table definition is not supported.");
    }

    @Override
    public void createIndex(ClusterName targetCluster, IndexMetadata indexMetadata)
            throws ConnectorException {
        throw new UnsupportedException("Creating indexes is not supported.");
    }

    @Override
    public void dropIndex(ClusterName targetCluster, IndexMetadata indexMetadata) throws ConnectorException {
        throw new UnsupportedException("Deleting indexes is not supported.");
    }

    @Override
    public List<CatalogMetadata> provideMetadata(ClusterName clusterName) throws ConnectorException {
        InMemoryDatastore datastore = connector.getDatastore(clusterName);
        List<CatalogMetadata> result = new ArrayList<>();
        if(datastore != null){
            Map<String, InMemoryCatalog> catalogs = datastore.getCatalogs();
            for(InMemoryCatalog inMemoryCatalog: catalogs.values()){
                CatalogMetadata catalogMetadata = convertToXdCatalog(clusterName, inMemoryCatalog);
                result.add(catalogMetadata);
            }
        }
        return result;
        /*
        CatalogName name = new CatalogName("InMemoryCatalog");
        Map<Selector, Selector> options = new HashMap<>();
        Map<TableName, TableMetadata> tables = new HashMap<>();
        TableName tableName = new TableName("InMemoryCatalog", "InMemoryTable");
        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();

        // First column
        ColumnName columnName = new ColumnName(tableName, "FirstCol");
        Object[] parameters = new Object[0];
        ColumnType columnType = ColumnType.TEXT;
        ColumnMetadata col = new ColumnMetadata(columnName, parameters, columnType);
        columns.put(col.getName(), col);
        // Second column
        columnName = new ColumnName(tableName, "SecondCol");
        columnType = ColumnType.INT;
        col = new ColumnMetadata(columnName, parameters, columnType);
        columns.put(col.getName(), col);
        // Third column
        columnName = new ColumnName(tableName, "ThirdCol");
        columnType = ColumnType.BOOLEAN;
        col = new ColumnMetadata(columnName, parameters, columnType);
        columns.put(col.getName(), col);

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        ClusterName clusterRef = null;
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        partitionKey.add(columns.keySet().iterator().next());
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        TableMetadata tableMetadata = new TableMetadata(tableName, options, columns, indexes, clusterRef,
                partitionKey, clusterKey);
        tables.put(tableName, tableMetadata);
        CatalogMetadata catalogMetadata = new CatalogMetadata(name, options, tables);
        return Arrays.asList(catalogMetadata);
        */
    }

    private CatalogMetadata convertToXdCatalog(ClusterName clusterName, InMemoryCatalog inMemoryCatalog) {
        CatalogName catalogName = new CatalogName(inMemoryCatalog.getName());
        Map<Selector, Selector> options = new HashMap<>();
        Map<TableName, TableMetadata> tables = new HashMap<>();
        for(InMemoryTable inMemoryTable: inMemoryCatalog.getTables().values()){
            TableName tableName = new TableName(inMemoryCatalog.getName(), inMemoryTable.getTableName());
            TableMetadata tableMetadata = convertToXdTable(clusterName, inMemoryCatalog.getName(), inMemoryTable);
            tables.put(tableName, tableMetadata);
        }
        CatalogMetadata catalogMetadata = new CatalogMetadata(catalogName, options, tables);
        return catalogMetadata;
    }

    private TableMetadata convertToXdTable(ClusterName clusterName, String catalogName, InMemoryTable inMemoryTable) {
        TableName name = new TableName(catalogName, inMemoryTable.getTableName());

        LinkedHashMap<ColumnName, ColumnMetadata> columns = new LinkedHashMap<>();

        String[] columnNames = inMemoryTable.getColumnNames();
        Class[] columnTypes = inMemoryTable.getColumnTypes();
        for(int i = 0; i < columnNames.length; i++){
            ColumnMetadata columnMetadata = convertToXdColumn(name, columnNames[i], columnTypes[i]);
            columns.put(columnMetadata.getName(), columnMetadata);
        }

        Map<IndexName, IndexMetadata> indexes = new HashMap<>();

        Map<Selector, Selector> options = new HashMap<>();

        List<ColumnName> partitionKey = new ArrayList<>();
        partitionKey.add(new ColumnName(name, columnNames[0]));

        List<ColumnName> clusterKey = new ArrayList<>();

        TableMetadata tableMetadata = new TableMetadata(name, options, columns, indexes, clusterName, partitionKey, clusterKey);
        return tableMetadata;
    }

    private ColumnMetadata convertToXdColumn(TableName tableName, String columnName, Class columnType) {
        ColumnName name = new ColumnName(tableName, columnName);
        Object[] parameters = new Object[]{};
        ColumnType columnXdType = convertToXdColumnType(columnType);
        ColumnMetadata columnMetadata = new ColumnMetadata(name, parameters, columnXdType);
        return columnMetadata;
    }

    private ColumnType convertToXdColumnType(Class columnType) {
        ColumnType ct = new ColumnType(DataType.NATIVE);
        if(columnType == Long.class){
            ct = new ColumnType(DataType.BIGINT);
        } else if (columnType == Boolean.class) {
            ct = new ColumnType(DataType.BOOLEAN);
        } else if (columnType == Double.class) {
            ct = new ColumnType(DataType.DOUBLE);
        } else if (columnType == Float.class) {
            ct = new ColumnType(DataType.FLOAT);
        } else if (columnType == Integer.class) {
            ct = new ColumnType(DataType.INT);
        } else if (columnType == String.class) {
            ct = new ColumnType(DataType.TEXT);
        } else {
            ct.setDBMapping(columnType.getSimpleName(), Object.class);
            ct.setODBCType(columnType.getSimpleName());
        }
        return ct;
    }

    @Override
    public CatalogMetadata provideCatalogMetadata(ClusterName clusterName, CatalogName catalogName)
            throws ConnectorException {
        List<CatalogMetadata> catalogs = provideMetadata(clusterName);
        CatalogMetadata result = null;
        for(CatalogMetadata catalog: catalogs){
            if(catalog.getName().equals(catalogName)){
                result = catalog;
                break;
            }
        }
        return  result;
        /*
        CatalogMetadata foundCatalog = null;
        List<CatalogMetadata> catalogs = provideMetadata(clusterName);
        for(CatalogMetadata catalog: catalogs){
            if(catalog.getName().equals(catalogName)){
                foundCatalog = catalog;
                break;
            }
        }
        if(foundCatalog == null){
            throw new ExecutionException("Catalog " + catalogName + " not found.");
        }
        return foundCatalog;
        */
    }

    @Override
    public TableMetadata provideTableMetadata(ClusterName clusterName, TableName tableName)
            throws ConnectorException {
        CatalogMetadata catalogMetadata = provideCatalogMetadata(clusterName, tableName.getCatalogName());
        TableMetadata result = null;
        for(Map.Entry<TableName, TableMetadata> table: catalogMetadata.getTables().entrySet()){
            if(table.getKey().equals(tableName)){
                result = table.getValue();
                break;
            }
        }
        return result;
        /*
        return provideCatalogMetadata(clusterName, tableName.getCatalogName()).getTables().get(tableName);
        */
    }
}
