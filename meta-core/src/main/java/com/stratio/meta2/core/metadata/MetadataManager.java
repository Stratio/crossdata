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

package com.stratio.meta2.core.metadata;


import java.util.Map;

import com.stratio.meta2.metadata.CatalogMetadata;
import com.stratio.meta2.metadata.ClusterMetadata;
import com.stratio.meta2.metadata.TableMetadata;
import com.stratio.meta2.transaction.ITransactionContext;
import com.stratio.meta2.transaction.ITransactionContextFactory;



public enum  MetadataManager {
  MANAGER;
  private boolean isInit = false;

  private Map<String, CatalogMetadata> catalogs;
  private Map<String, ClusterMetadata> clusters;
  private ITransactionContextFactory factory;

  private void shouldBeInit(){
    if (!isInit) {
      throw new MetadataManagerException("Metadata is not initialized yet.");
    }
  }

  private void shouldBeUnique(CatalogMetadata metadata) {
    if (catalogs.containsKey(metadata.getQualifiedName())) {
      throw new MetadataManagerException("Catalog [" + metadata.getQualifiedName()
          + "] already exists");
    }
  }

  private void shouldBeUnique(ClusterMetadata metadata) {
    if (clusters.containsKey(metadata.getQualifiedName())) {
      throw new MetadataManagerException("Storage [" + metadata.getQualifiedName()
          + "] already exists");
    }
  }

  private void shouldExistCatalog(String catalogQualifiedName) {
    if (!catalogs.containsKey(catalogQualifiedName)) {
      throw new MetadataManagerException("Catalog [" + catalogQualifiedName + "] doesn't exist yet");
    }
  }

  private void shouldExistCluster(String catalogQualifiedName) {
    if (!clusters.containsKey(catalogQualifiedName)) {
      throw new MetadataManagerException("Cluster [" + catalogQualifiedName + "] doesn't exist yet");
    }
  }

  public synchronized void init(Map<String, CatalogMetadata> catalogs,
      Map<String, ClusterMetadata> clusters, ITransactionContextFactory factory) {
    if (catalogs != null && clusters != null && factory != null) {
      this.catalogs = catalogs;
      this.clusters = clusters;
      this.factory = factory;
      this.isInit = true;
    } else {
      throw new NullPointerException("Any parameter can't be NULL");
    }
  }

  public void createCatalog(CatalogMetadata catalogMetadata) {
    shouldBeInit();
    shouldBeUnique(catalogMetadata);
    ITransactionContext context= factory.newTransactionContext();
    context.beginTransaction();
    try {
      catalogs.put(catalogMetadata.getQualifiedName(), catalogMetadata);
      context.commitTransaction();
    }catch (Exception ex){
      context.rollbackTransaction();
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    }
  }

  public void createCluster(ClusterMetadata clusterMetadata) {
    shouldBeInit();
    shouldBeUnique(clusterMetadata);
    ITransactionContext context = factory.newTransactionContext();
    context.beginTransaction();
    try {
      clusters.put(clusterMetadata.getQualifiedName(), clusterMetadata);
      context.commitTransaction();
    } catch (Exception ex) {
      context.rollbackTransaction();
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    }


  }

  public void createTable(String clusterQualifiedName, String catalogQualifiedName,
      TableMetadata tableMetadata) {
    shouldBeInit();
    shouldExistCatalog(catalogQualifiedName);
    shouldExistCluster(clusterQualifiedName);
    ITransactionContext context = factory.newTransactionContext();
    context.beginTransaction();
    try {
      ClusterMetadata clusterMetadata = clusters.get(clusterQualifiedName);
      CatalogMetadata catalogMetadata = catalogs.get(catalogQualifiedName);

      if (clusterMetadata.getTables().containsKey(tableMetadata.getQualifiedName())
          || catalogMetadata.getTables().containsKey(tableMetadata.getQualifiedName())) {
        throw new MetadataManagerException("Table [" + tableMetadata.getQualifiedName()
            + "] already exists");
      }
      clusterMetadata.getTables().put(tableMetadata.getQualifiedName(),tableMetadata);
      catalogMetadata.getTables().put(tableMetadata.getQualifiedName(),tableMetadata);
      clusters.remove(clusterQualifiedName);
      clusters.put(clusterQualifiedName,clusterMetadata);
      catalogs.remove(catalogQualifiedName);
      catalogs.put(catalogQualifiedName,catalogMetadata);
      context.commitTransaction();
    } catch (Exception ex) {
      context.rollbackTransaction();
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    }



  }



}
