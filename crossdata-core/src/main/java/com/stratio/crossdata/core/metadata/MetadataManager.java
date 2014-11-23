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
import java.util.HashSet;
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

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.NodeName;
import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.FirstLevelName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.data.NameType;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterAttachedMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.IMetadata;
import com.stratio.crossdata.common.metadata.NodeMetadata;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Singleton to manage the metadata store.
 */
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

    /**
     * Check if the object exist in the metadata store.
     * @param name It is the object to check.
     * @return True if it exists.
     */
    public boolean exists(Name name) throws MetadataManagerException {
        boolean result = false;
        if(name == null){
            throw new MetadataManagerException("Name is null");
        }
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
        case NODE:
            result = exists((NodeName) name);
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

    /**
     * Check if the table name exists.
     * @param name It's the table name to check.
     * @return True if it exists.
     */
    public boolean exists(TableName name) {
        boolean result = false;
        if (exists(name.getCatalogName())) {
            CatalogMetadata catalogMetadata = this.getCatalog(name.getCatalogName());
            result = catalogMetadata.getTables().containsKey(name);
        }
        return result;
    }
    /**
     * Check if the column name exists.
     * @param name It's the column name to check.
     * @return True if it exists.
     */
    public boolean exists(ColumnName name) {
        boolean result = false;
        if (exists(name.getTableName())) {
            TableMetadata tableMetadata = this.getTable(name.getTableName());
            result = tableMetadata.getColumns().containsKey(name);
        }
        return result;
    }

    /**
     * Check if the index name exists.
     * @param name It's the index name to check.
     * @return True if it exists
     */
    public boolean exists(IndexName name) {
        boolean result = false;
        if (exists(name.getTableName())) {
            TableMetadata tableMetadata = this.getTable(name.getTableName());
            result = tableMetadata.getIndexes().containsKey(name);
        }
        return result;
    }

    /**
     * Initialize the MetadataManager. This method is mandatory to use the MetadataManager.
     * @param metadata Map where MetadataManager persist the metadata objects.
     * @param writeLock Distributed lock.
     * @param tm Distributed transaction manager.
     * @see com.stratio.crossdata.core.grid.Grid
     */
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

    /**
     * Clear all metadata information.
     * @throws SystemException
     * @throws NotSupportedException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     */
    public synchronized void clear()
            throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException {
        beginTransaction();
        metadata.clear();
        commitTransaction();
    }

    /**
     * Save in the metadata store a new catalog. Must be unique.
     * @param catalogMetadata New catalog.
     */
    public void createCatalog(CatalogMetadata catalogMetadata) {
        createCatalog(catalogMetadata, true);
    }

    /**
     * Save in the metadata store a new catalog.
     * @param catalogMetadata New catalog.
     * @param unique If it is true then check if the catalog is unique.
     */
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

    /**
     * Remove the selected catalog. Not implemented yet.
     * @param catalogName Removed catalog name.
     * @param ifExist Conditon if the catalog exists
     */
    public void deleteCatalog(CatalogName catalogName, boolean ifExist) {
        shouldBeInit();
        writeLock.lock();
        if (!ifExist) {
            shouldExist(catalogName);
        }
        try {
            beginTransaction();
            metadata.remove(catalogName);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }

    }

    /**
     * Return a selected catalog in the metadata store.
     * @param name Name for the selected catalog.
     * @return Selected catalog.
     */
    public CatalogMetadata getCatalog(CatalogName name) {
        shouldBeInit();
        shouldExist(name);
        return (CatalogMetadata) metadata.get(name);
    }

    /**
     * Save in the metadata store a new table. Must be unique.
     * @param tableMetadata New table.
     */
    public void createTable(TableMetadata tableMetadata) {
        createTable(tableMetadata, true);
    }

    /**
     * Save in the metadata store a new table.
     * @param tableMetadata New table.
     * @param unique If it is true then check if the table is unique.
     */
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

    /**
     * Remove the selected table.
     * @param tableName Removed table name.
     */
    public void deleteTable(TableName tableName) {
        shouldBeInit();
        writeLock.lock();
        shouldExist(tableName);
        shouldExist(tableName.getCatalogName());
        try {
            beginTransaction();
            metadata.remove(tableName);
            CatalogMetadata catalogMetadata = getCatalog(tableName.getCatalogName());
            catalogMetadata.getTables().remove(tableName);
            metadata.put(catalogMetadata.getName(), catalogMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Return a selected table in the metadata store.
     * @param name Name for the selected table.
     * @return Selected table.
     */
    public TableMetadata getTable(TableName name) {
        shouldBeInit();
        shouldExist(name);
        CatalogMetadata catalogMetadata = this.getCatalog(name.getCatalogName());
        return catalogMetadata.getTables().get(name);
    }

    /**
     * Persist this cluster in MetadataStore and attach with the datastore.
     * @param clusterMetadata Metadata information that you want persist.
     * @param unique If it's true then the cluster metadata must be unique.
     */
    public void createClusterAndAttach(ClusterMetadata clusterMetadata, boolean unique) {
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

    /**
     * Persist this cluster in MetadataStore .
     * @param clusterMetadata Metadata information that you want persist.
     * @param unique If it's true then the cluster metadata must be unique.
     */
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
    /**
     * Persist this cluster in MetadataStore and attach with the datastore. Must be unique.
     * @param clusterMetadata Metadata information that you want persist.
     */
    public void createCluster(ClusterMetadata clusterMetadata) {
        createCluster(clusterMetadata, true);
    }

    /**
     * Return a selected cluster in the metadata store.
     * @param name Name for the selected cluster.
     * @return Selected cluster.
     */
    public ClusterMetadata getCluster(ClusterName name) {
        shouldBeInit();
        shouldExist(name);
        return (ClusterMetadata) metadata.get(name);
    }

    /**
     * Save in the metadata store a new datastore.
     * @param dataStoreMetadata New datastore.
     * @param unique If it is true then check if the datastore is unique.
     */
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

    /**
     * Save in the metadata store a new datastore. Must be unique.
     * @param dataStoreMetadata New datastore.
     */
    public void createDataStore(DataStoreMetadata dataStoreMetadata) {
        createDataStore(dataStoreMetadata, true);
    }

    /**
     * Return a selected datastore in the metadata store.
     * @param name Name for the selected datastore.
     * @return Selected datastore.
     */
    public DataStoreMetadata getDataStore(DataStoreName name) {
        shouldBeInit();
        shouldExist(name);
        return (DataStoreMetadata) metadata.get(name);
    }

    /**
     * Save in the metadata store a new connector.
     * @param connectorMetadata New connector.
     * @param unique If it is true then check if the connector is unique.
     */
    public void createConnector(ConnectorMetadata connectorMetadata, boolean unique) {
        shouldBeInit();
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

    /**
     * Save in the metadata store a new connector. Must be unique.
     * @param connectorMetadata New connector.
     */
    public void createConnector(ConnectorMetadata connectorMetadata) {
        createConnector(connectorMetadata, true);
    }

    /**
     * Return a selected connector in the metadata store.
     * @param name Name for the selected connector.
     * @return Selected connector.
     */
    public ConnectorMetadata getConnector(ConnectorName name) {
        shouldBeInit();
        shouldExist(name);
        return (ConnectorMetadata) metadata.get(name);
    }

    /**
     * Add a new actor reference.
     * @param name Name for the selected connector.
     * @param actorRef Actor reference URI.
     */
    public void addConnectorRef(ConnectorName name, String actorRef) {
        if (!exists(name)) {
            String version = null;
            Set<DataStoreName> dataStoreRefs = null;
            Map<ClusterName, Map<Selector, Selector>> clusterProperties = null;
            Set<PropertyType> requiredProperties = null;
            Set<PropertyType> optionalProperties = null;
            Set<Operations> supportedOperations = null;
            ConnectorMetadata connectorMetadata = new ConnectorMetadata(name, version, dataStoreRefs,
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

    /**
     * Update connector status.
     * @param name Name for the selected connector.
     * @param status New connector status.
     */
    public void setConnectorStatus(ConnectorName name, Status status) {
        ConnectorMetadata connectorMetadata = getConnector(name);
        connectorMetadata.setStatus(status);
        createConnector(connectorMetadata, false);
    }

    /**
     * Update the status of the connector list.
     * @param names List of connectors name.
     * @param status New connector status.
     */
    public void setConnectorStatus(List<ConnectorName> names, Status status) {
        for(ConnectorName connectorName: names){
            setConnectorStatus(connectorName, status);
        }
    }

    /**
     * Return a connector actor ref in the metadata store.
     * @param name Name for the selected connector.
     * @return Connector actor ref.
     */
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

    /**
     * Select a list available connector for a selected cluster.
     * @param status Selected status.
     * @param clusterName Selected cluster.
     * @return List of connector that it validate the restrictions.
     */
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

    /**
     * Return a selected column in the metadata store.
     * @param name Name for the selected column.
     * @return Selected column.
     */
    public ColumnMetadata getColumn(ColumnName name) {
        shouldBeInit();
        shouldExist(name);
        TableMetadata tableMetadata = this.getTable(name.getTableName());
        return tableMetadata.getColumns().get(name);
    }

    /**
     * Validate connector status.
     * @param connectorName Selected connector
     * @param status Status to validate.
     * @return True if the status is equal.
     */
    public boolean checkConnectorStatus(ConnectorName connectorName, Status status) {
        shouldBeInit();
        exists(connectorName);
        return (getConnector(connectorName).getStatus() == status);
    }

    /**
     * Return all catalogs.
     * @return List with all catalogs.
     */
    public List<CatalogMetadata> getCatalogs() {
        List<CatalogMetadata> catalogsMetadata = new ArrayList<>();
        for (Name name: metadata.keySet()) {
            if (name.getType() == NameType.CATALOG) {
                catalogsMetadata.add(getCatalog((CatalogName) name));
            }
        }
        return catalogsMetadata;
    }

    /**
     * Return all tables.
     * @return List with all tables.
     */
    public List<TableMetadata> getTables() {
        List<TableMetadata> tablesMetadata = new ArrayList<>();
        for(CatalogMetadata catalogMetadata: getCatalogs()){
            tablesMetadata.addAll(catalogMetadata.getTables().values());
        }
        return tablesMetadata;
    }

    /**
     * Return all columns.
     * @return List with all columns.
     */
    public List<ColumnMetadata> getColumns() {
        List<ColumnMetadata> columnsMetadata = new ArrayList<>();
        for(TableMetadata tableMetadata: getTables()){
            columnsMetadata.addAll(tableMetadata.getColumns().values());
        }
        return columnsMetadata;
    }

    /**
     * Return all tables in a selected catalog.
     * @param catalogName Selected catalog.
     * @return List with all tables.
     */
    public List<TableMetadata> getTablesByCatalogName(String catalogName) {
        List<TableMetadata> tableList=new ArrayList<>();
        for(Name name:metadata.keySet()) {
            if (name.getType()== NameType.CATALOG) {
                CatalogName catalog=(CatalogName)name;
                if (catalog.getName().equalsIgnoreCase(catalogName)){
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

    /**
     * Return all columns in a selected table.
     * @param catalog Selected catalog.
     * @param tableName Selected table name.
     * @return List with all columns.
     */
    public List<ColumnMetadata> getColumnByTable(String catalog,String tableName) {
        List<ColumnMetadata> columnList=new ArrayList<>();

        for(Name name:metadata.keySet()) {
            if (name.getType()== NameType.CATALOG) {
                CatalogName catalogName=(CatalogName)name;
                if (catalogName.getName().equalsIgnoreCase(catalog)){
                    CatalogMetadata catalogMetadata=getCatalog(catalogName);
                    for (Map.Entry<TableName, TableMetadata> entry : catalogMetadata.getTables().entrySet())
                    {
                        TableMetadata tableMetadata=entry.getValue();
                        if (tableMetadata.getName().getName().equalsIgnoreCase(tableName)){
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

        /**
     * Return all connectors.
     * @return List with all connectors.
     */
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

    /**
     * Return all datastores.
     * @return List with all connectors.
     */
    public List<DataStoreMetadata> getDatastores() {
        List<DataStoreMetadata> datastores = new ArrayList<>();
        for (Map.Entry<FirstLevelName, IMetadata> entry : metadata.entrySet()) {
            IMetadata iMetadata = entry.getValue();
            if (iMetadata instanceof DataStoreMetadata) {
                datastores.add((DataStoreMetadata) iMetadata);
            }
        }
        return datastores;
    }

    /**
     * Return all connector with a specific status.
     * @param status Selected status.
     * @return List with all connectors.
     */
    public List<ConnectorMetadata> getConnectors(Status status){
        List<ConnectorMetadata> onlineConnectors = new ArrayList<>();
        for (ConnectorMetadata connector : getConnectors()) {
            if (connector.getStatus() == status) {
                onlineConnectors.add(connector);
            }
        }
        return onlineConnectors;
    }

    /**
     * Return all connectors name with a selected status.
     * @param status Selected status.
     * @return List with all connector names.
     */
    public List<ConnectorName> getConnectorNames(Status status){
        List<ConnectorName> onlineConnectorNames = new ArrayList<>();
        for(ConnectorMetadata connectorMetadata: getConnectors(status)){
            onlineConnectorNames.add(connectorMetadata.getName());
        }
        return onlineConnectorNames;
    }

    /**
     * Check if the metadata store is empty.
     * @return True if it's empty.
     */
    public boolean isEmpty() {
        return metadata.isEmpty();
    }

    /**
     * Remove catalogs.
     * @throws NotSupportedException
     * @throws SystemException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     */
    public void clearCatalogs()
            throws NotSupportedException, SystemException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException {
        shouldBeInit();
        try {
            writeLock.lock();

            Set<CatalogName> catalogs = new HashSet<>();

            for(FirstLevelName name: metadata.keySet()){
                if(name instanceof CatalogName){
                    catalogs.add((CatalogName) name);
                }
            }

            beginTransaction();
            for(CatalogName catalogName: catalogs){
                metadata.remove(catalogName);
            }
            commitTransaction();

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove the connector from metadata manager.
     * @param connectorName The connector name
     * @throws NotSupportedException
     * @throws SystemException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     * @throws MetadataManagerException
     */
    public void deleteConnector(ConnectorName connectorName)
            throws NotSupportedException, SystemException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException, MetadataManagerException {
        shouldBeInit();
        exists(connectorName);
        try {
            writeLock.lock();

            for(FirstLevelName firstLevelName: metadata.keySet()){
                if(firstLevelName instanceof ClusterName) {
                    ClusterMetadata clusterMetadata = (ClusterMetadata) metadata.get(firstLevelName);
                    Map<ConnectorName, ConnectorAttachedMetadata> attachedConnectors =
                            clusterMetadata.getConnectorAttachedRefs();
                    if(attachedConnectors.containsKey(connectorName)){
                        StringBuilder sb = new StringBuilder("Connector ");
                        sb.append(connectorName).append(" couldn't be deleted").append(System.lineSeparator());
                        sb.append("It's attached to cluster ").append(clusterMetadata.getName());
                        throw new MetadataManagerException(sb.toString());
                    }
                }
            }

            beginTransaction();
            metadata.remove(connectorName);
            commitTransaction();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove the data store from the metadata manager.
     * @param dataStoreName The data store name
     * @throws NotSupportedException
     * @throws SystemException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     * @throws MetadataManagerException
     */
    public void deleteDatastore(DataStoreName dataStoreName)
            throws NotSupportedException, SystemException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException, MetadataManagerException {
        shouldBeInit();
        exists(dataStoreName);
        try {
            writeLock.lock();

            DataStoreMetadata dataStoreMetadata = getDataStore(dataStoreName);
            Map<ClusterName, ClusterAttachedMetadata> attachedClusters = dataStoreMetadata.getClusterAttachedRefs();
            if((attachedClusters != null) && (!attachedClusters.isEmpty())){
                StringBuilder sb = new StringBuilder("Datastore ");
                sb.append(dataStoreName).append(" couldn't be deleted").append(System.lineSeparator());
                sb.append("It has attachments: ").append(System.lineSeparator());
                for(ClusterName clusterName: attachedClusters.keySet()){
                    sb.append(" - ").append(clusterName).append(System.lineSeparator());
                }
                throw new MetadataManagerException(sb.toString());
            }

            beginTransaction();
            metadata.remove(dataStoreName);
            commitTransaction();
        } finally {
            writeLock.unlock();
        }
    }

    public NodeMetadata getNode(NodeName name) {
        shouldBeInit();
        shouldExist(name);
        return (NodeMetadata) metadata.get(name);
    }

    public NodeMetadata getNodeIfExists(NodeName name) {
        shouldBeInit();
        IMetadata iMetadata = metadata.get(name);
        NodeMetadata nodeMetadata = null;
        if(iMetadata != null){
            nodeMetadata = (NodeMetadata) iMetadata;
        }
        return nodeMetadata;
    }

    public void createNode(NodeMetadata nodeMetadata){
        createNode(nodeMetadata, true);
    }

    public void createNode(NodeMetadata nodeMetadata, boolean unique){
        shouldBeInit();
        try {
            writeLock.lock();
            if (unique) {
                shouldBeUnique(nodeMetadata.getName());
            }
            beginTransaction();
            metadata.put(nodeMetadata.getName(), nodeMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void setNodeStatus(NodeName nodeName, Status status){
        shouldBeInit();
        try {
            writeLock.lock();
            beginTransaction();
            NodeMetadata nodeMetadata = new NodeMetadata(nodeName, status);
            createNode(nodeMetadata, false);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public void setNodeStatusIfExists(NodeName nodeName, Status status){
        shouldBeInit();
        try {
            writeLock.lock();
            NodeMetadata nodeMetadata = getNodeIfExists(nodeName);
            if(nodeMetadata != null){
                nodeMetadata.setStatus(status);
                beginTransaction();
                createNode(nodeMetadata, false);
                commitTransaction();
            }
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    public Status getNodeStatus(NodeName nodeName){
        return getNode(nodeName).getStatus();
    }

    public List<NodeName> getNodeNames(Status status){
        List<NodeName> onlineNodeNames = new ArrayList<>();
        for(NodeMetadata nodeMetadata: getNodes(status)){
            onlineNodeNames.add(nodeMetadata.getName());
        }
        return onlineNodeNames;
    }

    public List<NodeMetadata> getNodes(Status status){
        List<NodeMetadata> onlineNodes = new ArrayList<>();
        for (NodeMetadata node: getNodes()) {
            if (node.getStatus() == status) {
                onlineNodes.add(node);
            }
        }
        return onlineNodes;
    }

    public List<NodeMetadata> getNodes() {
        List<NodeMetadata> nodes = new ArrayList<>();
        for (Map.Entry<FirstLevelName, IMetadata> entry: metadata.entrySet()) {
            IMetadata iMetadata = entry.getValue();
            if (iMetadata instanceof ConnectorMetadata) {
                nodes.add((NodeMetadata) iMetadata);
            }
        }
        return nodes;
    }

    public void setNodeStatus(List<NodeName> names, Status status) {
        for(NodeName nodeName: names){
            setNodeStatus(nodeName, status);
        }
    }

    public boolean checkGetConnectorName(NodeName nodeName) {
        boolean result = false;
        try {
            writeLock.lock();
            beginTransaction();
            if ((!exists(nodeName)) || (getNode(nodeName).getStatus() == Status.OFFLINE)) {
                setNodeStatus(nodeName, Status.INITIALIZING);
                result = true;
            }
            commitTransaction();
        } catch (SystemException | HeuristicRollbackException | RollbackException | NotSupportedException | HeuristicMixedException e) {
            result = false;
        } finally {
            writeLock.unlock();
        }
        return result;
    }

}
