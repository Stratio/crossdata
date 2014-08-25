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


import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.stratio.meta2.metadata.*;



public enum MetadataManager {
  MANAGER;

  private boolean isInit = false;

  private Map<String, IMetadata> metadata;
  private Lock writeLock;


  private void shouldBeInit() {
    if (!isInit) {
      throw new MetadataManagerException("Metadata is not initialized yet.");
    }
  }


  private boolean existsMetadata(String name) {
    return metadata.containsKey(name);
  }

  private void shouldBeUniqueCatalog(String name) {
    if (existsCatalog(name)) {
      throw new MetadataManagerException("Catalog [" + name + "] already exists");
    }
  }

  private void shouldExistCatalog(String name) {
    if (!existsCatalog(name)) {
      throw new MetadataManagerException("Catalog [" + name + "] doesn't exist yet");
    }
  }

  public boolean existsCatalog(String name) {
    return existsMetadata(name);
  }

  private void shouldBeUniqueCluster(String name) {
    if (existsCluster(name)) {
      throw new MetadataManagerException("Cluster [" + name + "] already exists");
    }
  }

  private void shouldExistCluster(String name) {
    if (!existsCluster(name)) {
      throw new MetadataManagerException("Cluster [" + name + "] doesn't exist yet");
    }
  }

  public boolean existsCluster(String name) {
    return existsMetadata(name);
  }

  private void shouldBeUniqueConnector(String name) {
    if (existsConnector(name)) {
      throw new MetadataManagerException("Connector [" + name + "] already exists");
    }
  }

  private void shouldExistConnector(String name) {
    if (!existsConnector(name)) {
      throw new MetadataManagerException("Connector [" + name + "] doesn't exist yet");
    }
  }

  public boolean existsConnector(String name) {
    return existsMetadata(name);
  }



  private void shouldExistTable(String name) {
    if (!existsTable(name)) {
      throw new MetadataManagerException("Table [" + name + "] doesn't exist yet");
    }
  }

  private void shouldBeUniqueTable(String name) {
    if (existsTable(name)) {
      throw new MetadataManagerException("Table [" + name + "] exists already");
    }
  }

  private void shouldBeUniqueDataStore(String name) {
    if (existsDataStore(name)) {
      throw new MetadataManagerException("DataStore [" + name + "] already exists");
    }
  }

  private void shouldExistDataStore(String name) {
    if (!existsDataStore(name)) {
      throw new MetadataManagerException("DataStore [" + name + "] doesn't exist yet");
    }
  }

  public boolean existsDataStore(String name) {
    return existsMetadata(name);
  }


  public boolean existsTable(String name) {
    boolean result = false;
    String catalog = QualifiedNames.getCatalogFromTableQualifiedName(name);
    if (existsCatalog(catalog)) {
      CatalogMetadata catalogMetadata = this.getCatalog(catalog);
      result = catalogMetadata.getTables().containsKey(name);
    }
    return result;
  }



  public synchronized void init(Map<String, IMetadata> metadata, Lock writeLock) {
    if (metadata != null && writeLock != null) {
      this.metadata = metadata;
      this.writeLock = writeLock;
      this.isInit = true;
    } else {
      throw new NullPointerException("Any parameter can't be NULL");
    }
  }

  public void createCatalog(CatalogMetadata catalogMetadata) {
    shouldBeInit();
    try {
      writeLock.lock();
      shouldBeUniqueCatalog(catalogMetadata.getName());
      metadata.put(catalogMetadata.getName(), catalogMetadata);
    } catch (MetadataManagerException mex) {
      throw mex;
    } catch (Exception ex) {
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    } finally {
      writeLock.unlock();
    }
  }

  public CatalogMetadata getCatalog(String name) {
    shouldBeInit();
    shouldExistCatalog(name);
    return (CatalogMetadata) metadata.get(name);
  }


  public void createTable(TableMetadata tableMetadata) {
    shouldBeInit();
    try {
      writeLock.lock();
      shouldExistCatalog(tableMetadata.getCatalogRef());
      shouldExistCluster(tableMetadata.getClusterRef());
      shouldBeUniqueTable(tableMetadata.getName());
      CatalogMetadata catalogMetadata =
          ((CatalogMetadata) metadata.get(tableMetadata.getCatalogRef()));

      if (catalogMetadata.getTables().containsKey(tableMetadata.getName())) {
        throw new MetadataManagerException("Table [" + tableMetadata.getName()
            + "] already exists");
      }

      catalogMetadata.getTables().put(tableMetadata.getName(), tableMetadata);
      metadata.put(tableMetadata.getCatalogRef(), catalogMetadata);
    } catch (Exception ex) {
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    } finally {
      writeLock.unlock();
    }
  }

  public TableMetadata getTable(String name) {
    shouldBeInit();
    shouldExistTable(name);
    String catalog = QualifiedNames.getCatalogFromTableQualifiedName(name);
    CatalogMetadata catalogMetadata = this.getCatalog(catalog);
    return catalogMetadata.getTables().get(name);
  }

  public void createCluster(ClusterMetadata clusterMetadata) {
    shouldBeInit();
    try {
      writeLock.lock();
      shouldExistDataStore(clusterMetadata.getDataStoreRef());
      shouldBeUniqueCluster(clusterMetadata.getName());
      for (ConnectorAttachedMetadata connectorRef : clusterMetadata.getConnectorAttachedRefs()
          .values()) {
        shouldExistConnector(connectorRef.getConnectorRef());
      }
      metadata.put(clusterMetadata.getName(),clusterMetadata);
    } catch (MetadataManagerException mex) {
      throw mex;
    } catch (Exception ex) {
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    } finally {
      writeLock.unlock();
    }
  }

  public ClusterMetadata getCluster(String name) {
    shouldBeInit();
    shouldExistCluster(name);
    return (ClusterMetadata) metadata.get(name);
  }

  public void createDataStore(DataStoreMetadata dataStoreMetadata) {
    shouldBeInit();
    try {
      writeLock.lock();
      shouldBeUniqueDataStore(dataStoreMetadata.getName());
      metadata.put(dataStoreMetadata.getName(), dataStoreMetadata);
    } catch (MetadataManagerException mex) {
      throw mex;
    } catch (Exception ex) {
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    } finally {
      writeLock.unlock();
    }
  }

  public DataStoreMetadata getDataStore(String name) {
    shouldBeInit();
    shouldExistDataStore(name);
    return (DataStoreMetadata) metadata.get(name);
  }

  public void createConnector(ConnectorMetadata connectorMetadata) {
    shouldBeInit();
    try {
      writeLock.lock();
      shouldBeUniqueConnector(connectorMetadata.getName());
      metadata.put(connectorMetadata.getName(), connectorMetadata);
    } catch (MetadataManagerException mex) {
      throw mex;
    } catch (Exception ex) {
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    } finally {
      writeLock.unlock();
    }
  }

  public ConnectorMetadata getConnector(String name) {
    shouldBeInit();
    shouldExistDataStore(name);
    return (ConnectorMetadata) metadata.get(name);
  }
  
  

}
