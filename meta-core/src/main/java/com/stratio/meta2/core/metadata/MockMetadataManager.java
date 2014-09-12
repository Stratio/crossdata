/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.core.metadata;

import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.FirstLevelName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.IMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public enum MockMetadataManager {
  MANAGER;

  private List<ClusterMetadata> clusterMetadataList = new ArrayList<>();
  private List<CatalogMetadata> catalogMetadataList = new ArrayList<>();

  public void init(Map<FirstLevelName, IMetadata> md, ReentrantLock reentrantLock) {

  }

  public void createCluster(ClusterMetadata clusterMetadata) {
    clusterMetadataList.add(clusterMetadata);
  }

  public void createCatalog(CatalogMetadata catalogMetadata) {
    catalogMetadataList.add(catalogMetadata);
  }

  public boolean exists(TableName tableName) {
    CatalogName catalogName = tableName.getCatalogName();
    for(CatalogMetadata catalogMetadata: catalogMetadataList){
      if(catalogMetadata.getName().equals(catalogName)){
        return true;
      }
    }
    return false;
  }

  public boolean exists(ColumnName columnName) {
    for(CatalogMetadata catalogMetadata: catalogMetadataList){
      if(catalogMetadata.getName().equals(columnName.getTableName().getCatalogName())){
        for(TableName tableName: catalogMetadata.getTables().keySet()){
          if(tableName.equals(columnName.getTableName())){
            TableMetadata tableMetadata = catalogMetadata.getTables().get(tableName);
            for(ColumnName colName: tableMetadata.getColumns().keySet()){
              if(colName.equals(columnName)){
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  public TableMetadata getTable(TableName table) {
    for(CatalogMetadata catalogMetadata: catalogMetadataList){
      if(catalogMetadata.getName().equals(table.getCatalogName())){
        for(TableName tableName: catalogMetadata.getTables().keySet()){
          if(tableName.equals(table)){
            return catalogMetadata.getTables().get(tableName);
          }
        }
      }
    }
    return null;
  }

}
