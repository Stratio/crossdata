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

package com.stratio.crossdata.core.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import com.stratio.crossdata.common.connector.Operations;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.api.PropertyType;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.FirstLevelName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.data.NameType;
import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterAttachedMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.IMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.selectors.Selector;

public enum MetadataManager {
    MANAGER;

    private boolean isInit = false;

    private Map<FirstLevelName, IMetadata> metadata;
    private Lock writeLock;
    private TransactionManager tm;

    private void shouldBeInit() {
        if (!isInit) {
            throw new MetadataManagerException("Metadata is not initialized yet.");
        }
    }

    public boolean exists(Name name) {
        boolean result = false;
        switch (name.getType()) {
        case CATALOG:
            result = exists((CatalogName) name);
            break;
        case CLUSTER:
            result = exists((ClusterName) name);
            break;
        case COLUMN:
            result = exists((ColumnName) name);
            break;
        case CONNECTOR:
            result = exists((ConnectorName) name);
            break;
        case DATASTORE:
            result = exists((DataStoreName) name);
            break;
        case TABLE:
            result = exists((TableName) name);
            break;
        case INDEX:
            result = exists((IndexName) name);
            break;
        default:
            break;
        }
        return result;
    }

    private void shouldBeUnique(Name name) {
        if (exists(name)) {
            throw new MetadataManagerException("[" + name + "] already exists");
        }
    }

    private void shouldExist(Name name) {
        if (!exists(name)) {
            throw new MetadataManagerException("[" + name + "] doesn't exist yet");
        }
    }

    private void beginTransaction() throws SystemException, NotSupportedException {
        if (tm != null) {
            tm.begin();
        }
    }

    private void commitTransaction() throws HeuristicRollbackException, RollbackException,
            HeuristicMixedException, SystemException {
        if (tm != null) {
            tm.commit();
        }
    }

    private boolean exists(FirstLevelName name) {
        return metadata.containsKey(name);
    }

    public boolean exists(TableName name) {
        boolean result = false;
        if (exists(name.getCatalogName())) {
            CatalogMetadata catalogMetadata = this.getCatalog(name.getCatalogName());
            result = catalogMetadata.getTables().containsKey(name);
        }
        return result;
    }

    public boolean exists(ColumnName name) {
        boolean result = false;
        if (exists(name.getTableName())) {
            TableMetadata catalogMetadata = this.getTable(name.getTableName());
            result = catalogMetadata.getColumns().containsKey(name);
        }
        return result;
    }

    public boolean exists(IndexName name) {
        boolean result = false;
        if (exists(name.getTableName())) {
            TableMetadata tableMetadata = this.getTable(name.getTableName());
            result = tableMetadata.getIndexes().containsKey(name);
        }
        return result;
    }

    public synchronized void init(Map<FirstLevelName, IMetadata> metadata, Lock writeLock, TransactionManager tm) {
        if (metadata != null && writeLock != null) {
            this.metadata = metadata;
            this.writeLock = writeLock;
            this.tm = tm;
            this.isInit = true;
        } else {
            throw new IllegalArgumentException("Any parameter can't be NULL");
        }
    }

    public synchronized void clear()
            throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException {
        beginTransaction();
        metadata.clear();
        commitTransaction();
    }

    public void createCatalog(CatalogMetadata catalogMetadata) {
        createCatalog(catalogMetadata, true);
    }

    public void createCatalog(CatalogMetadata catalogMetadata, boolean unique) {
        shouldBeInit();
        try {
            writeLock.lock();
            if (unique) {
                shouldBeUnique(catalogMetadata.getName());
            }
            beginTransaction();
            metadata.put(catalogMetadata.getName(), catalogMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void deleteCatalog(CatalogName catalogName) {

    }

    public CatalogMetadata getCatalog(CatalogName name) {
        shouldBeInit();
        shouldExist(name);
        return (CatalogMetadata) metadata.get(name);
    }

    public void createTable(TableMetadata tableMetadata, boolean unique) {
        shouldBeInit();
        try {
            writeLock.lock();
            shouldExist(tableMetadata.getName().getCatalogName());
            shouldExist(tableMetadata.getClusterRef());
            if (unique) {
                shouldBeUnique(tableMetadata.getName());
            }
            CatalogMetadata catalogMetadata =
                    ((CatalogMetadata) metadata.get(tableMetadata.getName().getCatalogName()));

            if (catalogMetadata.getTables().containsKey(tableMetadata.getName()) && unique) {
                throw new MetadataManagerException("TABLE [" + tableMetadata.getName() + "] already exists");
            }

            catalogMetadata.getTables().put(tableMetadata.getName(), tableMetadata);
            beginTransaction();
            metadata.put(tableMetadata.getName().getCatalogName(), catalogMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void createTable(TableMetadata tableMetadata) {
        createTable(tableMetadata, true);
    }

    public void deleteTable(TableName tableName) {

    }

    public TableMetadata getTable(TableName name) {
        shouldBeInit();
        shouldExist(name);
        CatalogMetadata catalogMetadata = this.getCatalog(name.getCatalogName());
        return catalogMetadata.getTables().get(name);
    }

    public void createCluster(ClusterMetadata clusterMetadata, boolean unique, boolean attachToDatastore) {
        shouldBeInit();
        try {
            writeLock.lock();
            shouldExist(clusterMetadata.getDataStoreRef());
            if (unique) {
                shouldBeUnique(clusterMetadata.getName());
            }
            for (ConnectorAttachedMetadata connectorRef : clusterMetadata.getConnectorAttachedRefs()
                    .values()) {
                shouldExist(connectorRef.getConnectorRef());
            }
            ClusterName clusterName = clusterMetadata.getName();
            DataStoreName dataStoreName = clusterMetadata.getDataStoreRef();

            beginTransaction();

            metadata.put(clusterName, clusterMetadata);
            // recover DataStore info
            IMetadata iMetadata = metadata.get(dataStoreName);
            DataStoreMetadata dataStoreMetadata = (DataStoreMetadata) iMetadata;
            Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs = dataStoreMetadata.getClusterAttachedRefs();
            clusterAttachedRefs.put(clusterName, new ClusterAttachedMetadata(clusterName,
                    dataStoreName, clusterMetadata.getOptions()));
            // attach Cluster to DataStore
            dataStoreMetadata.setClusterAttachedRefs(clusterAttachedRefs);
            metadata.put(dataStoreName, dataStoreMetadata);

            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void createCluster(ClusterMetadata clusterMetadata, boolean unique) {
        shouldBeInit();
        try {
            writeLock.lock();
            shouldExist(clusterMetadata.getDataStoreRef());
            if (unique) {
                shouldBeUnique(clusterMetadata.getName());
            }
            for (ConnectorAttachedMetadata connectorRef : clusterMetadata.getConnectorAttachedRefs()
                    .values()) {
                shouldExist(connectorRef.getConnectorRef());
            }
            beginTransaction();
            metadata.put(clusterMetadata.getName(), clusterMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void createCluster(ClusterMetadata clusterMetadata) {
        createCluster(clusterMetadata, true);
    }

    public ClusterMetadata getCluster(ClusterName name) {
        shouldBeInit();
        shouldExist(name);
        return (ClusterMetadata) metadata.get(name);
    }

    public void createDataStore(DataStoreMetadata dataStoreMetadata, boolean unique) {
        shouldBeInit();
        try {
            writeLock.lock();
            if (unique) {
                shouldBeUnique(dataStoreMetadata.getName());
            }
            beginTransaction();
            metadata.put(dataStoreMetadata.getName(), dataStoreMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void createDataStore(DataStoreMetadata dataStoreMetadata) {
        createDataStore(dataStoreMetadata, true);
    }

    public DataStoreMetadata getDataStore(DataStoreName name) {
        shouldBeInit();
        shouldExist(name);
        return (DataStoreMetadata) metadata.get(name);
    }

    public void createConnector(ConnectorMetadata connectorMetadata, boolean unique) {
        shouldBeInit();
        //TODO Check whether datastores can be added after adding connectors
        try {
            writeLock.lock();
            if (unique) {
                shouldBeUnique(connectorMetadata.getName());
            }
            beginTransaction();
            metadata.put(connectorMetadata.getName(), connectorMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void createConnector(ConnectorMetadata connectorMetadata, boolean unique, boolean attachToCluster) {
        shouldBeInit();
        for (DataStoreName dataStore : connectorMetadata.getDataStoreRefs()) {
            shouldExist(dataStore);
            // Check clusters
            for (ClusterName c : connectorMetadata.getClusterRefs()) {
                shouldExist(c);
            }
        }
        try {
            writeLock.lock();
            if (unique) {
                shouldBeUnique(connectorMetadata.getName());
            }
            beginTransaction();
            metadata.put(connectorMetadata.getName(), connectorMetadata);
            for (ClusterName c : connectorMetadata.getClusterRefs()) {
                IMetadata iMetadata = metadata.get(c);
                ClusterMetadata clusterMetadata = (ClusterMetadata) iMetadata;
                Map<ConnectorName, ConnectorAttachedMetadata> connectorList = clusterMetadata
                        .getConnectorAttachedRefs();
                connectorList.put(connectorMetadata.getName(), new ConnectorAttachedMetadata(connectorMetadata.getName
                        (), c, connectorMetadata.getClusterProperties().get(c)));
                clusterMetadata.setConnectorAttachedRefs(connectorList);
            }
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void createConnector(ConnectorMetadata connectorMetadata) {
        createConnector(connectorMetadata, true);
    }

    public ConnectorMetadata getConnector(ConnectorName name) {
        shouldBeInit();
        shouldExist(name);
        return (ConnectorMetadata) metadata.get(name);
    }

    public void addConnectorRef(ConnectorName name, String actorRef) {
        if (!exists(name)) {
            String version = null;
            Set<DataStoreName> dataStoreRefs = null;
            Set<ClusterName> clusterRefs = null;
            Map<ClusterName, Map<Selector, Selector>> clusterProperties = null;
            Set<PropertyType> requiredProperties = null;
            Set<PropertyType> optionalProperties = null;
            Set<Operations> supportedOperations = null;
            ConnectorMetadata connectorMetadata = new ConnectorMetadata(name, version, dataStoreRefs, clusterRefs,
                    clusterProperties, requiredProperties, optionalProperties, supportedOperations);
            connectorMetadata.setActorRef(actorRef);
            try {
                writeLock.lock();
                beginTransaction();
                metadata.put(connectorMetadata.getName(), connectorMetadata);
                commitTransaction();
            } catch (Exception ex) {
                throw new MetadataManagerException(ex);
            } finally {
                writeLock.unlock();
            }
        } else {
            ConnectorMetadata connectorMetadata = getConnector(name);
            connectorMetadata.setActorRef(actorRef);
            createConnector(connectorMetadata, false);
        }
    }

    public void setConnectorStatus(ConnectorName name, Status status) {
        ConnectorMetadata connectorMetadata = getConnector(name);
        connectorMetadata.setStatus(status);
        createConnector(connectorMetadata, false);
    }

    public void setConnectorStatus(List<ConnectorName> names, Status status) {
        for(ConnectorName connectorName: names){
            setConnectorStatus(connectorName, status);
        }
    }

    public String getConnectorRef(ConnectorName name) {
        return getConnector(name).getActorRef();
    }

    /**
     * Get the connectors that are attached to the clusters that store the requested tables.
     *
     * @param connectorStatus The status of the connector.
     * @param tables          The list of table names.
     * @return A map associating table names with a list of the available connectors.
     */
    public Map<TableName, List<ConnectorMetadata>> getAttachedConnectors(Status connectorStatus,
            List<TableName> tables) {
        Map<TableName, List<ConnectorMetadata>> result = new HashMap<>();
        List<ConnectorMetadata> connectors;
        for (TableName table : tables) {

            ClusterName clusterName = getTable(table).getClusterRef();

            Set<ConnectorName> connectorNames = getCluster(clusterName)
                    .getConnectorAttachedRefs().keySet();

            connectors = new ArrayList<>();
            for (ConnectorName connectorName : connectorNames) {
                ConnectorMetadata connectorMetadata = getConnector(connectorName);
                if (connectorMetadata.getStatus() == connectorStatus) {
                    connectors.add(connectorMetadata);
                }
            }

            result.put(table, connectors);
        }
        return result;
    }

    public List<ConnectorMetadata> getAttachedConnectors(Status status, ClusterName clusterName) {
        List<ConnectorMetadata> connectors = new ArrayList<>();
        Set<ConnectorName> connectorNames = getCluster(clusterName)
                .getConnectorAttachedRefs().keySet();
        for (ConnectorName connectorName : connectorNames) {
            ConnectorMetadata connectorMetadata = getConnector(connectorName);
            if (connectorMetadata.getStatus() == status) {
                connectors.add(connectorMetadata);
            }
        }
        return connectors;
    }

    public ColumnMetadata getColumn(ColumnName name) {
        shouldBeInit();
        shouldExist(name);
        TableMetadata tableMetadata = this.getTable(name.getTableName());
        return tableMetadata.getColumns().get(name);
    }

    public void setQueryStatus(CatalogName catalogName, QueryStatus queryStatus, String queryId) {
        CatalogMetadata catalogMetadata = getCatalog(catalogName);
        catalogMetadata.setQueryStatus(queryStatus);
        catalogMetadata.setQueryId(queryId);
        createCatalog(catalogMetadata, false);
    }

    public boolean checkConnectorStatus(ConnectorName connectorName, Status status) {
        shouldBeInit();
        exists(connectorName);
        return (getConnector(connectorName).getStatus() == status);
    }

    public List<String> getCatalogs() {
        List<String> catalogsMetadata = new ArrayList<>();
        for (Name name: metadata.keySet()) {
            if (name.getType() == NameType.CATALOG) {
                catalogsMetadata.add(getCatalog((CatalogName) name).getName().getName().toLowerCase());
            }
        }
        return catalogsMetadata;
    }

    public List<TableMetadata> getTables() {
        List<TableMetadata> tablesMetadatas = new ArrayList<>();
        for (Name name : metadata.keySet()) {
            if (name.getType() == NameType.TABLE) {
                tablesMetadatas.add(getTable((TableName) name));
            }
        }
        return tablesMetadatas;
    }

    public List<ColumnMetadata> getColumns() {
        List<ColumnMetadata> columnsMetadatas = new ArrayList<>();
        for (Name name : metadata.keySet()) {
            if (name.getType() == NameType.COLUMN) {
                columnsMetadatas.add(getColumn((ColumnName) name));
            }
        }
        return columnsMetadatas;
    }

    public List<TableMetadata> getTablesByCatalogName(String catalogName) {
        List<TableMetadata> tableList=new ArrayList<>();
        for(Name name:metadata.keySet()) {
            if (name.getType()== NameType.CATALOG) {
                CatalogName catalog=(CatalogName)name;
                if (catalog.getName().equals(catalogName)){
                    CatalogMetadata catalogMetadata=getCatalog(catalog);
                    for (Map.Entry<TableName, TableMetadata> entry : catalogMetadata.getTables().entrySet())
                    {
                        tableList.add(entry.getValue());
                    }
                }
            }
        }
        return tableList;
    }

    public List<ColumnMetadata> getColumnByTable(String catalog,String tableName) {
        List<ColumnMetadata> columnList=new ArrayList<>();

        for(Name name:metadata.keySet()) {
            if (name.getType()== NameType.CATALOG) {
                CatalogName catalogName=(CatalogName)name;
                if (catalogName.getName().equals(catalog)){
                    CatalogMetadata catalogMetadata=getCatalog(catalogName);
                    for (Map.Entry<TableName, TableMetadata> entry : catalogMetadata.getTables().entrySet())
                    {
                        TableMetadata tableMetadata=entry.getValue();
                        if (tableMetadata.getName().getName().equals(tableName)){
                            for (Map.Entry<ColumnName, ColumnMetadata> entry2 : tableMetadata.getColumns().entrySet()){
                                columnList.add(entry2.getValue());
                            }
                        }
                    }
                }
            }
        }
        return columnList;
    }

    public List<IMetadata> getMetadatas(String clazz){
        List<IMetadata> connectorsMetadata = new ArrayList<>();
        for(Map.Entry<FirstLevelName, IMetadata> entry: metadata.entrySet()){
            IMetadata iMetadata = entry.getValue();
            if (iMetadata.getClass().toString().equalsIgnoreCase(clazz)){
                connectorsMetadata.add(iMetadata);
            }
        }
        return connectorsMetadata;
    }

    public List<ConnectorMetadata> getConnectors() {
        List<ConnectorMetadata> connectors = new ArrayList<>();
        for (Map.Entry<FirstLevelName, IMetadata> entry : metadata.entrySet()) {
            IMetadata iMetadata = entry.getValue();
            if (iMetadata instanceof ConnectorMetadata) {
                connectors.add((ConnectorMetadata) iMetadata);
            }
        }
        return connectors;
    }

    public List<ConnectorMetadata> getConnectors(Status status){
        List<ConnectorMetadata> onlineConnectors = new ArrayList<>();
        for (ConnectorMetadata connector : getConnectors()) {
            if (connector.getStatus() == status) {
                onlineConnectors.add(connector);
            }
        }
        return onlineConnectors;
    }

    public List<ConnectorName> getConnectorNames(Status status){
        List<ConnectorName> onlineConnectorNames = new ArrayList<>();
        for(ConnectorMetadata connectorMetadata: getConnectors(status)){
            onlineConnectorNames.add(connectorMetadata.getName());
        }
        return onlineConnectorNames;
    }

}
