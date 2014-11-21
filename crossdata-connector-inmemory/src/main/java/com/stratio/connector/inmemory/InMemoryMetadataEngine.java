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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.stratio.connector.inmemory.datastore.InMemoryDatastore;
import com.stratio.crossdata.common.connector.IMetadataEngine;
import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;

/**
 * Class that implements the {@link com.stratio.crossdata.common.connector.IMetadataEngine}.
 */
public class InMemoryMetadataEngine implements IMetadataEngine{

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
            for(Map.Entry<ColumnName, ColumnMetadata> column : tableMetadata.getColumns().entrySet()){
                columnNames[index] = column.getKey().getName();
                columnTypes[index] = column.getValue().getColumnType().getDbClass();
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

        }else{
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
}
