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

import com.stratio.meta2.metadata.CatalogMetadata;
import com.stratio.meta2.metadata.ClusterMetadata;
import com.stratio.meta2.metadata.TableMetadata;




public enum MetadataManager {
  MANAGER;
  private boolean isInit = false;

  private Map<String, CatalogMetadata> catalogs;
  private Map<String, ClusterMetadata> clusters;
  private Lock writeLock;


  private void shouldBeInit() {
    if (!isInit) {
      throw new MetadataManagerException("Metadata is not initialized yet.");
    }
  }

  private void shouldBeUnique(CatalogMetadata metadata) {
    if (catalogs.containsKey(metadata.getName())) {
      throw new MetadataManagerException("Catalog [" + metadata.getName()
          + "] already exists");
    }
  }

  private void shouldExist(String catalogQualifiedName) {
    if (!catalogs.containsKey(catalogQualifiedName)) {
      throw new MetadataManagerException("Catalog [" + catalogQualifiedName + "] doesn't exist yet");
    }
  }


  public synchronized void init(Map<String, CatalogMetadata> catalogs, Lock writeLock) {
    if (catalogs != null && writeLock != null ) {
      this.catalogs = catalogs;
      this.writeLock = writeLock;
      this.isInit = true;
    } else {
      throw new NullPointerException("Any parameter can't be NULL");
    }
  }

  public void createCatalog(CatalogMetadata catalogMetadata) {
    shouldBeInit();
    shouldBeUnique(catalogMetadata);
    try {
      writeLock.lock();
      catalogs.put(catalogMetadata.getName(), catalogMetadata);
    } catch (Exception ex) {
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    }finally {
      writeLock.unlock();
    }
  }


  public void createTable(String catalogQualifiedName, TableMetadata tableMetadata) {
    shouldBeInit();
    shouldExist(catalogQualifiedName);

    try {
      writeLock.lock();
      CatalogMetadata catalogMetadata = catalogs.get(catalogQualifiedName);

      if (catalogMetadata.getTables().containsKey(tableMetadata.getName())) {
        throw new MetadataManagerException("Table [" + tableMetadata.getName()
            + "] already exists");
      }

      catalogMetadata.getTables().put(tableMetadata.getName(), tableMetadata);
      catalogs.put(catalogQualifiedName, catalogMetadata);
    } catch (Exception ex) {
      throw new MetadataManagerException(ex.getMessage(), ex.getCause());
    }finally {
      writeLock.unlock();
    }

  }



}
