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
import java.util.Iterator;
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
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.FirstLevelName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.data.NameType;
import com.stratio.crossdata.common.data.NodeName;
import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.manifest.FunctionTypeHelper;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterAttachedMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.IMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.NodeMetadata;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.CaseWhenSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FunctionSelector;
import com.stratio.crossdata.common.statements.structures.SelectSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.core.query.SelectValidatedQuery;

/**
 * Singleton to manage the metadata store.
 */
public enum MetadataManager {

    /**
     * Metadata manager engine as enumeration to define a Singleton.
     */
    MANAGER;

    /**
     * Whether the manager has been initialized.
     */
    private boolean isInit = false;

    /**
     * Map associating {@link com.stratio.crossdata.common.data.FirstLevelName} with
     * {@link com.stratio.crossdata.common.metadata.IMetadata} entities.
     */
    private Map<FirstLevelName, IMetadata> metadata;

    /**
     * Lock for write access to the metadata map.
     */
    private Lock writeLock;

    /**
     * Transaction manager to access the metadata map.
     */
    private TransactionManager tm;

    /**
     * Check that the metadata manager has been previously initialized.
     */
    private void shouldBeInit() {
        if (!isInit) {
            throw new MetadataManagerException("Metadata is not initialized yet.");
        }
    }

    /**
     * Check if the object exist in the metadata store.
     *
     * @param name It is the object to check.
     * @return True if it exists.
     */
    public boolean exists(Name name) throws MetadataManagerException {
        boolean result = false;
        if (name == null) {
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

    /**
     * Check that a {@link com.stratio.crossdata.common.data.Name} is unique.
     *
     * @param name The {@link com.stratio.crossdata.common.data.Name}.
     */
    private void shouldBeUnique(Name name) {
        if (exists(name)) {
            throw new MetadataManagerException("[" + name + "] already exists");
        }
    }

    /**
     * Check that a {@link com.stratio.crossdata.common.data.Name} exists in the metadata map.
     *
     * @param name The {@link com.stratio.crossdata.common.data.Name}.
     */
    private void shouldExist(Name name) {
        if (!exists(name)) {
            throw new MetadataManagerException("[" + name + "] doesn't exist yet");
        }
    }

    /**
     * Start a transaction using the associated transaction manager.
     *
     * @throws SystemException       If the transaction manager founds an error and it becomes unable to answer future
     *                               requests.
     * @throws NotSupportedException If the operation is not supported.
     */
    private void beginTransaction() throws SystemException, NotSupportedException {
        if (tm != null) {
            tm.begin();
        }
    }

    /**
     * Commit a transaction.
     *
     * @throws HeuristicRollbackException If the transaction manager decides to rollback the transaction.
     * @throws RollbackException          If the transaction is marked as rollback only.
     * @throws HeuristicMixedException    If the transaction has been partially commited due to the use of a heuristic.
     * @throws SystemException            If the transaction manager is not available.
     */
    private void commitTransaction() throws HeuristicRollbackException, RollbackException,
            HeuristicMixedException, SystemException {
        if (tm != null) {
            tm.commit();
        }
    }

    /**
     * Check if a {@link com.stratio.crossdata.common.data.FirstLevelName} exists in the metadata map.
     *
     * @param name The {@link com.stratio.crossdata.common.data.FirstLevelName}.
     * @return Whether it exists.
     */
    private boolean exists(FirstLevelName name) {
        return metadata.containsKey(name);
    }

    /**
     * Check if the table name exists.
     *
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
     *
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
     *
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
     *
     * @param metadata  Map where MetadataManager persist the metadata objects.
     * @param writeLock Distributed lock.
     * @param tm        Distributed transaction manager.
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
     *
     * @throws SystemException            If the transaction manager is not available.
     * @throws NotSupportedException      If the operation is not supported.
     * @throws HeuristicRollbackException If the transaction manager decides to rollback the transaction.
     * @throws HeuristicMixedException    If the transaction has been partially committed due to the use of a heuristic.
     * @throws RollbackException          If the transaction is marked as rollback only.
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
     *
     * @param catalogMetadata New catalog.
     */
    public void createCatalog(CatalogMetadata catalogMetadata) {
        createCatalog(catalogMetadata, true);
    }

    /**
     * Save in the metadata store a new catalog.
     *
     * @param catalogMetadata New catalog.
     * @param unique          If it is true then check if the catalog is unique.
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
     *
     * @param catalogName Removed catalog name.
     * @param ifExist     Condition if the catalog exists
     */
    public void deleteCatalog(CatalogName catalogName, boolean ifExist) {
        shouldBeInit();
        try {
            writeLock.lock();
            if (!ifExist) {
                shouldExist(catalogName);
            }
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
     *
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
     *
     * @param tableMetadata New table.
     */
    public void createTable(TableMetadata tableMetadata) {
        createTable(tableMetadata, true);
    }

    /**
     * Save in the metadata store a new table.
     *
     * @param tableMetadata New table.
     * @param unique        If it is true then check if the table is unique.
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
     *
     * @param tableName Removed table name.
     */
    public void deleteTable(TableName tableName) {
        shouldBeInit();
        try {
            writeLock.lock();
            shouldExist(tableName);
            shouldExist(tableName.getCatalogName());
            beginTransaction();
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
     *
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
     *
     * @param clusterMetadata Metadata information that you want persist.
     * @param unique          If it's true then the cluster metadata must be unique.
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
     *
     * @param clusterMetadata Metadata information that you want persist.
     * @param unique          If it's true then the cluster metadata must be unique.
     */
    public void createCluster(ClusterMetadata clusterMetadata, boolean unique) {
        shouldBeInit();
        try {
            writeLock.lock();
            shouldExist(clusterMetadata.getDataStoreRef());
            if (unique) {
                shouldBeUnique(clusterMetadata.getName());
            }
            for (ConnectorAttachedMetadata connectorRef :
                    clusterMetadata.getConnectorAttachedRefs().values()) {
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
     *
     * @param clusterMetadata Metadata information that you want persist.
     */
    public void createCluster(ClusterMetadata clusterMetadata) {
        createCluster(clusterMetadata, true);
    }

    /**
     * Delete a cluster from the metadata store.
     *
     * @param clusterName The {@link com.stratio.crossdata.common.data.ClusterName}.
     * @param ifExist     Whether the operation should be executed only if exists.
     */
    public void deleteCluster(ClusterName clusterName, boolean ifExist) {
        shouldBeInit();
        try {
            writeLock.lock();
            if (!ifExist) {
                shouldExist(clusterName);
            }
            beginTransaction();
            metadata.remove(clusterName);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Return a selected cluster in the metadata store.
     *
     * @param name Name for the selected cluster.
     * @return Selected cluster.
     */
    public ClusterMetadata getCluster(ClusterName name) {
        shouldBeInit();
        shouldExist(name);
        return (ClusterMetadata) metadata.get(name);
    }

    /**
     * Get the list of clusters currently managed by Crossdata.
     *
     * @return A list of {@link com.stratio.crossdata.common.metadata.ClusterMetadata}.
     */
    public List<ClusterMetadata> getClusters() {
        shouldBeInit();
        List<ClusterMetadata> clusters = new ArrayList<>();
        for (Map.Entry<FirstLevelName, IMetadata> entry : metadata.entrySet()) {
            IMetadata iMetadata = entry.getValue();
            if (iMetadata instanceof ClusterMetadata) {
                clusters.add((ClusterMetadata) iMetadata);
            }
        }
        return clusters;
    }

    /**
     * Save in the metadata store a new datastore.
     *
     * @param dataStoreMetadata New datastore.
     * @param unique            If it is true then check if the datastore is unique.
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
     *
     * @param dataStoreMetadata New datastore.
     */
    public void createDataStore(DataStoreMetadata dataStoreMetadata) {
        createDataStore(dataStoreMetadata, true);
    }

    /**
     * Return a selected datastore in the metadata store.
     *
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
     *
     * @param connectorMetadata New connector.
     * @param unique            If it is true then check if the connector is unique.
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
     *
     * @param connectorMetadata New connector.
     */
    public void createConnector(ConnectorMetadata connectorMetadata) {
        createConnector(connectorMetadata, true);
    }

    /**
     * Save in the metadata store a new node.
     *
     * @param nodeMetadata New node.
     * @param unique       If it is true then check if the node is unique.
     */
    public void createNode(NodeMetadata nodeMetadata, boolean unique) {
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

    /**
     * Save in the metadata store a new node. Must be unique.
     *
     * @param nodeMetadata New node.
     */
    public void createNode(NodeMetadata nodeMetadata) {
        createNode(nodeMetadata, true);
    }

    /**
     * Return a selected connector in the metadata store.
     *
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
     *
     * @param name     Name for the selected connector.
     * @param actorRef Actor reference URI.
     */
    public void addConnectorRef(ConnectorName name, String actorRef) throws ManifestException {
        if (!exists(name)) {
            String version = null;
            Set<DataStoreName> dataStoreRefs = new HashSet<>();
            Map<ClusterName, Map<Selector, Selector>> clusterProperties = new HashMap<>();
            Map<ClusterName, Integer> clusterPriorities = new HashMap<>();
            Set<PropertyType> requiredProperties = new HashSet<>();
            Set<PropertyType> optionalProperties = new HashSet<>();
            Set<Operations> supportedOperations = new HashSet<>();
            ConnectorMetadata connectorMetadata = new ConnectorMetadata(name, version, dataStoreRefs,
                    clusterProperties, clusterPriorities, requiredProperties, optionalProperties, supportedOperations, null);
            connectorMetadata.addActorRef(actorRef);
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
            connectorMetadata.addActorRef(actorRef);
            createConnector(connectorMetadata, false);
        }
    }

    /**
     * Update connector status.
     *
     * @param name   Name for the selected connector.
     * @param status New connector status.
     */
    public void setConnectorStatus(ConnectorName name, Status status) {
        ConnectorMetadata connectorMetadata = getConnector(name);
        connectorMetadata.setStatus(status);
        createConnector(connectorMetadata, false);
    }

    /**
     * Update the status of the connector list.
     *
     * @param names  List of connectors name.
     * @param status New connector status.
     */
    public void setConnectorStatus(List<ConnectorName> names, Status status) {
        for (ConnectorName connectorName: names) {
            removeActorRefsFromConnector(connectorName);
            setConnectorStatus(connectorName, status);
        }
    }

    private void removeActorRefsFromConnector(ConnectorName name) {
        ConnectorMetadata connectorMetadata = getConnector(name);
        connectorMetadata.setActorRefs(new HashSet<String>());
        createConnector(connectorMetadata, false);
    }

    /**
     * Delete an actor reference from the connector.
     *
     * @param name      The connector name.
     * @param actorRef  The actor reference.
     */
    public void removeActorRefFromConnector(ConnectorName name, String actorRef){
        ConnectorMetadata connectorMetadata = getConnector(name);
        connectorMetadata.removeActorRef(actorRef);
        createConnector(connectorMetadata, false);
    }

    /**
     * Update the status of the node list.
     *
     * @param names  List of nodes name.
     * @param status New node status.
     */
    public void setNodeStatus(List<NodeName> names, Status status) {
        for (NodeName nodeName : names) {
            setNodeStatus(nodeName, status);
        }
    }

    /**
     * Return a connector actor ref in the metadata store.
     *
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
     *
     * @param status      Selected status.
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
     *
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
     *
     * @param connectorName Selected connector
     * @param status        Status to validate.
     * @return True if the status is equal.
     */
    public boolean checkConnectorStatus(ConnectorName connectorName, Status status) {
        shouldBeInit();
        exists(connectorName);
        return (getConnector(connectorName).getStatus() == status);
    }

    /**
     * Return all catalogs.
     *
     * @return List with all catalogs.
     */
    public List<CatalogMetadata> getCatalogs() {
        List<CatalogMetadata> catalogsMetadata = new ArrayList<>();
        for (Name name : metadata.keySet()) {
            if (name.getType() == NameType.CATALOG) {
                catalogsMetadata.add(getCatalog((CatalogName) name));
            }
        }
        return catalogsMetadata;
    }

    /**
     * Return all tables.
     *
     * @return List with all tables.
     */
    public List<TableMetadata> getTables() {
        List<TableMetadata> tablesMetadata = new ArrayList<>();
        for (CatalogMetadata catalogMetadata : getCatalogs()) {
            tablesMetadata.addAll(catalogMetadata.getTables().values());
        }
        return tablesMetadata;
    }

    /**
     * Get the list of indexes in the system.
     *
     * @return A list of {@link com.stratio.crossdata.common.metadata.IndexMetadata}.
     */
    public List<IndexMetadata> getIndexes() {
        List<IndexMetadata> indexesMetadata = new ArrayList<>();
        for (TableMetadata table: getTables()) {
            indexesMetadata.addAll(table.getIndexes().values());
        }
        return indexesMetadata;
    }

    /**
     * Return all columns.
     *
     * @return List with all columns.
     */
    public List<ColumnMetadata> getColumns() {
        List<ColumnMetadata> columnsMetadata = new ArrayList<>();
        for (TableMetadata tableMetadata : getTables()) {
            columnsMetadata.addAll(tableMetadata.getColumns().values());
        }
        return columnsMetadata;
    }

    /**
     * Return all tables in a selected catalog.
     *
     * @param catalogName Selected catalog.
     * @return List with all tables.
     */
    public List<TableMetadata> getTablesByCatalogName(String catalogName) {
        List<TableMetadata> tableList = new ArrayList<>();
        for (Name name : metadata.keySet()) {
            if (name.getType() == NameType.CATALOG) {
                CatalogName catalog = (CatalogName) name;
                if (catalog.getName().equalsIgnoreCase(catalogName)) {
                    CatalogMetadata catalogMetadata = getCatalog(catalog);
                    for (Map.Entry<TableName, TableMetadata> entry : catalogMetadata.getTables().entrySet()) {
                        tableList.add(entry.getValue());
                    }
                }
            }
        }
        return tableList;
    }

    /**
     * Return all columns in a selected table.
     *
     * @param catalog   Selected catalog.
     * @param tableName Selected table name.
     * @return List with all columns.
     */
    public List<ColumnMetadata> getColumnByTable(String catalog, String tableName) {
        List<ColumnMetadata> columnList = new ArrayList<>();

        for (Name name : metadata.keySet()) {
            if (name.getType() == NameType.CATALOG) {
                CatalogName catalogName = (CatalogName) name;
                if (catalogName.getName().equalsIgnoreCase(catalog)) {
                    CatalogMetadata catalogMetadata = getCatalog(catalogName);
                    for (Map.Entry<TableName, TableMetadata> entry : catalogMetadata.getTables().entrySet()) {
                        TableMetadata tableMetadata = entry.getValue();
                        if (tableMetadata.getName().getName().equalsIgnoreCase(tableName)) {
                            for (Map.Entry<ColumnName, ColumnMetadata> entry2 : tableMetadata.getColumns().entrySet()) {
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
     *
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
     *
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
     *
     * @param status Selected status.
     * @return List with all connectors.
     */
    public List<ConnectorMetadata> getConnectors(Status status) {
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
     *
     * @param status Selected status.
     * @return List with all connector names.
     */
    public List<ConnectorName> getConnectorNames(Status status) {
        List<ConnectorName> onlineConnectorNames = new ArrayList<>();
        for (ConnectorMetadata connectorMetadata : getConnectors(status)) {
            onlineConnectorNames.add(connectorMetadata.getName());
        }
        return onlineConnectorNames;
    }

    /**
     * Check if the metadata store is empty.
     *
     * @return True if it's empty.
     */
    public boolean isEmpty() {
        return metadata.isEmpty();
    }

    /**
     * Remove catalogs.
     *
     * @throws NotSupportedException      If the operation is not supported.
     * @throws SystemException            If the transaction manager is not available.
     * @throws HeuristicRollbackException If the transaction manager decides to rollback the transaction.
     * @throws HeuristicMixedException    If the transaction has been partially commited due to the use of a heuristic.
     * @throws RollbackException          If the transaction is marked as rollback only.
     */
    public void clearCatalogs()
            throws NotSupportedException, SystemException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException {
        shouldBeInit();
        try {
            writeLock.lock();

            Set<CatalogName> catalogs = new HashSet<>();

            for (FirstLevelName name : metadata.keySet()) {
                if (name instanceof CatalogName) {
                    catalogs.add((CatalogName) name);
                }
            }

            beginTransaction();
            for (CatalogName catalogName : catalogs) {
                metadata.remove(catalogName);
            }
            commitTransaction();

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove the connector from metadata manager.
     *
     * @param connectorName The connector name.
     * @throws NotSupportedException      If the operation is not supported.
     * @throws SystemException            If the transaction manager is not available.
     * @throws HeuristicRollbackException If the transaction manager decides to rollback the transaction.
     * @throws HeuristicMixedException    If the transaction has been partially commited due to the use of a heuristic.
     * @throws RollbackException          If the transaction is marked as rollback only.
     * @throws MetadataManagerException   If the connector does not exists.
     */
    public void deleteConnector(ConnectorName connectorName)
            throws NotSupportedException, SystemException, HeuristicRollbackException, HeuristicMixedException,
            RollbackException, MetadataManagerException {
        shouldBeInit();
        exists(connectorName);
        try {
            writeLock.lock();

            for (FirstLevelName firstLevelName : metadata.keySet()) {
                if (firstLevelName instanceof ClusterName) {
                    ClusterMetadata clusterMetadata = (ClusterMetadata) metadata.get(firstLevelName);
                    Map<ConnectorName, ConnectorAttachedMetadata> attachedConnectors =
                            clusterMetadata.getConnectorAttachedRefs();
                    if (attachedConnectors.containsKey(connectorName)) {
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
     *
     * @param dataStoreName The data store name.
     * @throws NotSupportedException      If the operation is not supported.
     * @throws SystemException            If the transaction manager is not available.
     * @throws HeuristicRollbackException If the transaction manager decides to rollback the transaction.
     * @throws HeuristicMixedException    If the transaction has been partially commited due to the use of a heuristic.
     * @throws RollbackException          If the transaction is marked as rollback only.
     * @throws MetadataManagerException   If the datastore does not exists.
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
            if ((attachedClusters != null) && (!attachedClusters.isEmpty())) {
                StringBuilder sb = new StringBuilder("Datastore ");
                sb.append(dataStoreName).append(" couldn't be deleted").append(System.lineSeparator());
                sb.append("It has attachments: ").append(System.lineSeparator());
                for (ClusterName clusterName : attachedClusters.keySet()) {
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

    /**
     * Get the node metadata associated with a node name.
     *
     * @param name The {@link com.stratio.crossdata.common.data.NodeName}.
     * @return A {@link com.stratio.crossdata.common.metadata.NodeMetadata}.
     */
    public NodeMetadata getNode(NodeName name) {
        shouldBeInit();
        shouldExist(name);
        return (NodeMetadata) metadata.get(name);
    }


    /**
     * Get the names of the supported functions for a given connector.
     *
     * @param cn A {@link com.stratio.crossdata.common.data.ConnectorName}.
     * @return A set with the function names.
     */
    public Set<String> getSupportedFunctionNames(ConnectorName cn) {
        Set<String> functions = new HashSet<>();
        ConnectorMetadata connector = getConnector(cn);

        for (FunctionType ft : connector.getConnectorFunctions()) {
            functions.add(ft.getFunctionName());
        }

        for(DataStoreName dsn: connector.getDataStoreRefs()){
            DataStoreMetadata datastore = getDataStore(dsn);
            for(FunctionType ft: datastore.getFunctions()){
                functions.add(ft.getFunctionName());
            }
        }
        functions.removeAll(connector.getExcludedFunctions());
        return functions;
    }

    /**
     * Get the names of the supported functions for a given connector.
     *
     * @param cn A {@link com.stratio.crossdata.common.data.ConnectorName}.
     * @param projects initial projects.
     * @return A set with the function names.
     */
    public Set<String> getSupportedFunctionNames(ConnectorName cn, Set<Project> projects) {
        Set<String> functions = new HashSet<>();
        ConnectorMetadata connector = getConnector(cn);

        for (FunctionType ft : connector.getConnectorFunctions()) {
            functions.add(ft.getFunctionName());
        }

        Set<DataStoreName> dataStoreNames = new HashSet<>();
        for (Project project : projects) {
            dataStoreNames.add(getCluster(project.getClusterName()).getDataStoreRef());
        }


        for(DataStoreName dsn: connector.getDataStoreRefs()){

            if (dataStoreNames.contains(dsn)){
                DataStoreMetadata datastore = getDataStore(dsn);

                for(FunctionType ft: datastore.getFunctions()){
                    functions.add(ft.getFunctionName());
                }
            }

        }
        functions.removeAll(connector.getExcludedFunctions());
        return functions;
    }


    /**
     * Get the set of supported function types for a given connector.
     *
     * @param cn The {@link com.stratio.crossdata.common.data.ConnectorName}.
     * @param projects list of clusters.
     * @return A set of {@link com.stratio.crossdata.common.manifest.FunctionType}.
     */
    public Set<FunctionType> getSupportedFunctions(ConnectorName cn, Set<Project> projects) {
        Set<FunctionType> functions = new HashSet<>();
        ConnectorMetadata connector = getConnector(cn);
        functions.addAll(connector.getConnectorFunctions());

        //datastoreNames
        Set<DataStoreName> dataStoreNames = new HashSet<>();
        for (Project project : projects) {
            dataStoreNames.add(getCluster(project.getClusterName()).getDataStoreRef());
        }

        for(DataStoreName dsn: connector.getDataStoreRefs()){

            if(dataStoreNames.contains(dsn)) {
                DataStoreMetadata datastore = getDataStore(dsn);
                for (FunctionType df : datastore.getFunctions()) {
                    if (!connector.getExcludedFunctions().contains(df.getFunctionName())) {
                        functions.add(df);
                    }
                }
            }
        }
        return functions;
    }

    /**
     * Get the set of supported function types for a given connector.
     *
     * @param cn The {@link com.stratio.crossdata.common.data.ConnectorName}.
     * @return A set of {@link com.stratio.crossdata.common.manifest.FunctionType}.
     */
    public Set<FunctionType> getSupportedFunctions(ConnectorName cn) {
        Set<FunctionType> functions = new HashSet<>();
        ConnectorMetadata connector = getConnector(cn);
        functions.addAll(connector.getConnectorFunctions());
        for(DataStoreName dsn: connector.getDataStoreRefs()){
            DataStoreMetadata datastore = getDataStore(dsn);
            for(FunctionType df: datastore.getFunctions()){
                if(!connector.getExcludedFunctions().contains(df.getFunctionName())){
                    functions.add(df);
                }
            }
        }
        return functions;
    }

    /**
     * Check if the connector has associated the input signature of the function.
     * @param fSelector     The function Selector with the signature.
     * @param connectorName The name of the connector.
     * @param subQuery      The subquery.
     * @param initialProjects The initial projects.
     * @return A boolean with the check result.
     */
    public boolean checkInputSignature(FunctionSelector fSelector, ConnectorName connectorName, SelectValidatedQuery subQuery, Set<Project> initialProjects)
            throws PlanningException {
        boolean result = false;
        FunctionType ft = getFunction(connectorName, fSelector.getFunctionName(), initialProjects);
        if(ft != null){
            String inputSignatureFromSelector = createInputSignature(fSelector, connectorName, subQuery, initialProjects);
            String storedSignature = ft.getSignature();
            String inputStoredSignature = storedSignature.substring(0,storedSignature.lastIndexOf(":Tuple["));
            result = inputStoredSignature.equals(inputSignatureFromSelector) ||
                    FunctionTypeHelper.checkInputSignatureCompatibility(inputStoredSignature, inputSignatureFromSelector);
        }
        return result;
    }

    /**
     * Check if the connector has associated the input signature of the function and its result signature.
     * @param fSelector   The function Selector with the signature.
     * @param retSelector The returned Selector.
     * @param connectorName The name of the connector.
     * @return A boolean with the check result.
     */
    public boolean checkFunctionReturnSignature(FunctionSelector fSelector, Selector retSelector, ConnectorName connectorName) throws PlanningException {
        boolean result = false;
        FunctionType ft = getFunction(connectorName, fSelector.getFunctionName());
        if(ft != null){
            String storedSignature = ft.getSignature();
            String resultStoredSignature = storedSignature.substring(storedSignature.lastIndexOf(":Tuple["),
                    storedSignature.length());
            String querySignature="";
            switch(retSelector.getType()){

            case FUNCTION:
            case RELATION:
            case SELECT:
            case GROUP:
            case CASE_WHEN:
            case NULL:
            case LIST:
            case ALIAS:
            case ASTERISK:
                return true;
            case COLUMN:
                ColumnSelector columnSelector=(ColumnSelector)retSelector;
                querySignature="Tuple[" + columnSelector.getType().name() + "]";
                break;
            case BOOLEAN:
                querySignature="Tuple[Boolean]";
                break;
            case STRING:
                querySignature="Tuple[Text]";
                break;
            case INTEGER:
                querySignature="Tuple[Int]";
                break;
            case FLOATING_POINT:
                querySignature="Tuple[Double]";
                break;
            }
            result= FunctionTypeHelper.checkInputSignatureCompatibility(resultStoredSignature,querySignature);
        }

        return result;
    }

    /**
     * Get the function type from the specified connector.
     * @param connectorName The connector name.
     * @param functionName The function name.
     * @return The {@link com.stratio.crossdata.common.manifest.FunctionType} if the function is defined; null otherwise.
     */
    public FunctionType getFunction(ConnectorName connectorName, String functionName) {
        FunctionType result = null;
        Set<FunctionType> candidateFunctions = getSupportedFunctions(connectorName);
        for(FunctionType ft: candidateFunctions){
            if(ft.getFunctionName().equals(functionName)){
                result = ft;
                break;
            }
        }
        return result;
    }

    /**
     * Get the function type from the specified connector.
     * @param connectorName The connector name.
     * @param functionName The function name.
     * @param projects Set of clusters whose datastore associated contains the functions supported.
     * @return The {@link com.stratio.crossdata.common.manifest.FunctionType} if the function is defined; null otherwise.
     */
    public FunctionType getFunction(ConnectorName connectorName, String functionName, Set<Project> projects) {
        FunctionType result = null;
        Set<FunctionType> candidateFunctions = getSupportedFunctions(connectorName, projects);
        for(FunctionType ft: candidateFunctions){
            if(ft.getFunctionName().equals(functionName)){
                result = ft;
                break;
            }
        }
        return result;
    }


    /**
     * Generates the input signature of a given function selector.
     * @param functionSelector The function selector.
     * @return A String with the input signature.
     */
    private String createInputSignature(FunctionSelector functionSelector, ConnectorName connectorName, SelectValidatedQuery subQuery, Set<Project> initialProjects)
                    throws PlanningException {

        StringBuilder sb = new StringBuilder(functionSelector.getFunctionName());
        sb.append("(Tuple[");
        Iterator<Selector> iter = functionSelector.getFunctionColumns().iterator();
        while(iter.hasNext()){
            Selector selector = iter.next();
            sb.append(inferDataType(selector, connectorName, subQuery, initialProjects));
            if(iter.hasNext()){
                sb.append(",");
            }
        }
        sb.append("])");
        return  sb.toString();
    }

    private String inferDataType(Selector selector, ConnectorName connectorName, SelectValidatedQuery subQuery, Set<Project> initialProjects) throws
            PlanningException {
        String result = null;
        switch(selector.getType()){
        case FUNCTION:
            FunctionSelector fs = (FunctionSelector) selector;
            String fSignature = getFunction(connectorName, fs.getFunctionName()).getSignature();
            String functionType = fSignature.substring(
                    fSignature.lastIndexOf(":Tuple[") + 7, fSignature.length() - 1);
            result = functionType;
            break;
        case COLUMN:
            ColumnSelector cs = (ColumnSelector) selector;
            if(cs.getName().getTableName().isVirtual()){
                if(subQuery == null){
                    throw new PlanningException("Can't infer data type for " + selector);
                }
                List<Selector> subSelectors = subQuery.getStatement().getSelectExpression().getSelectorList();
                for(Selector ss: subSelectors){
                    if(ss.getColumnName().getName().equals(cs.getColumnName().getName())
                            || ( ss.getAlias()!=null && ss.getAlias().equals(cs.getColumnName().getName()))){
                        result = inferDataType(ss, connectorName, subQuery.getSubqueryValidatedQuery(), initialProjects);
                        break;
                    }
                }
            } else {
                ColumnName columnName = cs.getName();
                ColumnMetadata column = getColumn(columnName);
                ColumnType columnType = column.getColumnType();
                result = columnType.getCrossdataType();
            }
            break;
        case BOOLEAN:
            result = new ColumnType(DataType.BOOLEAN).getCrossdataType();
            break;
        case STRING:
            result = new ColumnType(DataType.TEXT).getCrossdataType();
            break;
        case INTEGER:
            result = new ColumnType(DataType.INT).getCrossdataType();
            break;
        case FLOATING_POINT:
            result = new ColumnType(DataType.DOUBLE).getCrossdataType();
            break;
        //TODO check the real returning type
        case RELATION:
            result = new ColumnType(DataType.DOUBLE).getCrossdataType();
            break;
        case LIST:
            result = new ColumnType(DataType.LIST).getCrossdataType();
            break;
        case CASE_WHEN:
            CaseWhenSelector cws = (CaseWhenSelector) selector;
            result = inferDataType(cws.getDefaultValue(), connectorName, null, initialProjects);
            break;
        case SELECT:
            SelectSelector ss = (SelectSelector) selector;
            Select select = (Select) ss.getQueryWorkflow().getLastStep();
            Selector s = select.getColumnMap().keySet().iterator().next();
            result = inferDataType(s, connectorName, subQuery, initialProjects);
            break;
        case ALIAS:
            result = new ColumnType(DataType.TEXT).getCrossdataType();
            break;
        default:
            throw new PlanningException("The input type : "+selector.getType()+" is not supported yet");
        }
        return result;
    }

    /**
     * Get a node if exists.
     *
     * @param name The {@link com.stratio.crossdata.common.data.NodeName}.
     * @return A {@link com.stratio.crossdata.common.metadata.NodeMetadata}.
     */
    public NodeMetadata getNodeIfExists(NodeName name) {
        shouldBeInit();
        IMetadata iMetadata = metadata.get(name);
        NodeMetadata nodeMetadata = null;
        if (iMetadata != null) {
            nodeMetadata = (NodeMetadata) iMetadata;
        }
        return nodeMetadata;
    }

    /**
     * Set a node status.
     *
     * @param nodeName The {@link com.stratio.crossdata.common.data.NodeName}.
     * @param status   The {@link com.stratio.crossdata.common.data.Status}.
     */
    public void setNodeStatus(NodeName nodeName, Status status) {
        shouldBeInit();
        try {
            writeLock.lock();
            NodeMetadata nodeMetadata = new NodeMetadata(nodeName, status);
            createNode(nodeMetadata, false);
        } catch (Exception ex) {
            throw new MetadataManagerException(ex);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Set the node status if exists.
     *
     * @param nodeName The {@link com.stratio.crossdata.common.data.NodeName}.
     * @param status   The {@link com.stratio.crossdata.common.data.Status}.
     */
    public void setNodeStatusIfExists(NodeName nodeName, Status status) {
        shouldBeInit();
        try {
            writeLock.lock();
            NodeMetadata nodeMetadata = getNodeIfExists(nodeName);
            if (nodeMetadata != null) {
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

    /**
     * Get the node status.
     *
     * @param nodeName A {@link com.stratio.crossdata.common.data.NodeName}.
     * @return A {@link com.stratio.crossdata.common.data.Status}.
     */
    public Status getNodeStatus(NodeName nodeName) {
        return getNode(nodeName).getStatus();
    }

    /**
     * Get the name of the nodes with a given status.
     *
     * @param status The {@link com.stratio.crossdata.common.data.Status}.
     * @return A list of {@link com.stratio.crossdata.common.data.NodeName}.
     */
    public List<NodeName> getNodeNames(Status status) {
        List<NodeName> onlineNodeNames = new ArrayList<>();
        for (NodeMetadata nodeMetadata : getNodes(status)) {
            onlineNodeNames.add(nodeMetadata.getName());
        }
        return onlineNodeNames;
    }

    /**
     * Get the metadata of the nodes with a given status.
     *
     * @param status The {@link com.stratio.crossdata.common.data.Status}.
     * @return A list of {@link com.stratio.crossdata.common.metadata.NodeMetadata}.
     */
    public List<NodeMetadata> getNodes(Status status) {
        List<NodeMetadata> onlineNodes = new ArrayList<>();
        for (NodeMetadata node : getNodes()) {
            if (node.getStatus() == status) {
                onlineNodes.add(node);
            }
        }
        return onlineNodes;
    }

    /**
     * Get the metadata associated with all existing nodes.
     *
     * @return A list of {@link com.stratio.crossdata.common.metadata.NodeMetadata}.
     */
    public List<NodeMetadata> getNodes() {
        List<NodeMetadata> nodes = new ArrayList<>();
        for (Map.Entry<FirstLevelName, IMetadata> entry : metadata.entrySet()) {
            IMetadata iMetadata = entry.getValue();
            if (iMetadata instanceof NodeMetadata) {
                nodes.add((NodeMetadata) iMetadata);
            }
        }
        return nodes;
    }

    /**
     * Check if a connector has already been associated with a name.
     *
     * @param nodeName The {@link com.stratio.crossdata.common.data.NodeName}.
     * @return Whether the name should be asked.
     */
    public boolean checkGetConnectorName(NodeName nodeName) {
        boolean result = false;
        try {
            writeLock.lock();
            if ((!exists(nodeName)) || (getNode(nodeName).getStatus() == Status.OFFLINE)) {
                setNodeStatus(nodeName, Status.INITIALIZING);
                result = true;
            }
        } catch (Exception e) {
            result = false;
        } finally {
            writeLock.unlock();
        }
        return result;
    }

    /**
     * Associate a catalog with a cluster.
     *
     * @param catalog     The {@link com.stratio.crossdata.common.data.CatalogName}.
     * @param clusterName The {@link com.stratio.crossdata.common.data.ClusterName}.
     */
    public void addCatalogToCluster(CatalogName catalog, ClusterName clusterName) {
        ClusterMetadata clusterMetadata = getCluster(clusterName);
        clusterMetadata.addPersistedCatalog(catalog);
        createCluster(clusterMetadata, false);
    }

    /**
     * Remove a catalog from all clusters.
     *
     * @param catalog The {@link com.stratio.crossdata.common.data.CatalogName}.
     */
    public void removeCatalogFromClusters(CatalogName catalog) {
        List<ClusterMetadata> clusters = getClusters();
        for (ClusterMetadata cluster : clusters) {
            removeCatalogFromCluster(catalog, cluster);
        }
    }

    /**
     * Remove a catalog from a particular cluster.
     *
     * @param catalog A {@link com.stratio.crossdata.common.data.CatalogName}.
     * @param cluster The {@link com.stratio.crossdata.common.metadata.ClusterMetadata} to be removed.
     */
    private void removeCatalogFromCluster(CatalogName catalog, ClusterMetadata cluster) {
        cluster.removePersistedCatalog(catalog);
        createCluster(cluster, false);
    }
}
