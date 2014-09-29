/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.metadata;

import java.io.Serializable;
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

import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.Name;
import com.stratio.meta2.common.data.Status;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;

public enum MetadataManager {
    MANAGER;

    private boolean isInit = false;

    private Map<FirstLevelName, Serializable> metadata;
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
        case Catalog:
            result = exists((CatalogName) name);
            break;
        case Cluster:
            result = exists((ClusterName) name);
            break;
        case Column:
            result = exists((ColumnName) name);
            break;
        case Connector:
            result = exists((ConnectorName) name);
            break;
        case DataStore:
            result = exists((DataStoreName) name);
            break;
        case Table:
            result = exists((TableName) name);
            break;
        case Index:
            result = exists((IndexName) name);
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

    public synchronized void init(Map<FirstLevelName, Serializable> metadata, Lock writeLock, TransactionManager tm) {
        if (metadata != null && writeLock != null) {
            this.metadata = metadata;
            this.writeLock = writeLock;
            this.tm = tm;
            this.isInit = true;
        } else {
            throw new NullPointerException("Any parameter can't be NULL");
        }
    }

    public void createCatalog(CatalogMetadata catalogMetadata) {
        shouldBeInit();
        try {
            writeLock.lock();
            shouldBeUnique(catalogMetadata.getName());
            beginTransaction();
            metadata.put(catalogMetadata.getName(), catalogMetadata);
            commitTransaction();
        } catch (MetadataManagerException mex) {
            throw mex;
        } catch (Exception ex) {
            throw new MetadataManagerException(ex.getMessage(), ex.getCause());
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
                throw new MetadataManagerException("Table [" + tableMetadata.getName() + "] already exists");
            }

            catalogMetadata.getTables().put(tableMetadata.getName(), tableMetadata);
            beginTransaction();
            metadata.put(tableMetadata.getName().getCatalogName(), catalogMetadata);
            commitTransaction();
        } catch (Exception ex) {
            throw new MetadataManagerException(ex.getMessage(), ex.getCause());
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
        } catch (MetadataManagerException mex) {
            throw mex;
        } catch (Exception ex) {
            throw new MetadataManagerException(ex.getMessage(), ex.getCause());
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
        } catch (MetadataManagerException mex) {
            throw mex;
        } catch (Exception ex) {
            throw new MetadataManagerException(ex.getMessage(), ex.getCause());
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
        try {
            writeLock.lock();
            if (unique) {
                shouldBeUnique(connectorMetadata.getName());
            }
            beginTransaction();
            metadata.put(connectorMetadata.getName(), connectorMetadata);
            commitTransaction();
        } catch (MetadataManagerException mex) {
            throw mex;
        } catch (Exception ex) {
            throw new MetadataManagerException(ex.getMessage(), ex.getCause());
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

    public void addConnectorRef(ConnectorName name, Serializable actorRef) {
        ConnectorMetadata connectorMetadata = getConnector(name);
        connectorMetadata.setActorRef(actorRef);
        createConnector(connectorMetadata, false);
    }

    public Serializable getConnectorRef(ConnectorName name) {
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

    public ColumnMetadata getColumn(ColumnName name) {
        shouldBeInit();
        shouldExist(name);
        TableMetadata tableMetadata = this.getTable(name.getTableName());
        return tableMetadata.getColumns().get(name);
    }

}
