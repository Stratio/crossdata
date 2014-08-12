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
import com.stratio.meta2.metadata.IMetadata;
import com.stratio.meta2.metadata.StorageMetadata;
import com.stratio.meta2.transaction.ITransactionContext;
import com.stratio.meta2.transaction.ITransactionContextFactory;



public enum  MetadataManager {
  MANAGER;
  private boolean isInit = false;

  private Map<String, IMetadata> metadata;
  private ITransactionContextFactory factory;

  private void shouldBeInit(){
    if (!isInit) {
      throw new MetadataManagerException("Metadata is not initialized yet.");
    }
  }

  private void shouldBeUnique(IMetadata iMetadata){
    if (metadata.containsKey(iMetadata.getQualifiedName())) {
      throw new MetadataManagerException("Entity["+ iMetadata.getQualifiedName()+"] already exists");
    }
  }

  public synchronized void init(Map<String,IMetadata> metadata, ITransactionContextFactory factory){
    if (metadata != null && factory != null) {
      this.metadata = metadata;
      this.factory = factory;
      this.isInit = true;
    } else {
      throw new MetadataManagerException("[metadata] and [lock] must be NOT NULL");
    }
  }

  public void CreateCatalog(CatalogMetadata catalogMetadata) {
    shouldBeInit();
    shouldBeUnique(catalogMetadata);
    ITransactionContext context= factory.newTransactionContext();
    context.beginTransaction();
    try {
      metadata.put(catalogMetadata.getQualifiedName(), catalogMetadata);
      context.commitTransaction();
    }catch (Exception ex){
      context.rollbackTransaction();
      throw ex;
    }
  }

  public void CreateStorage(StorageMetadata storageMetadata){
    shouldBeInit();
    shouldBeUnique(storageMetadata);

  }





}
