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

package com.stratio.meta2.core.statements;

//public abstract class TableStatement extends MetaStatement;

import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.TableName;

/**
 * Meta statement that are executed over a table.
 */
public interface ITableStatement {

    /**
     * The target table.
     */
    //protected TableName tableName;

    /**
     * Get the table to be described.
     *
     * @return The name or null if not set.
     */
    public TableName getTableName();

    public void setTableName(TableName tableName);

    public CatalogName getEffectiveCatalog();

  /*
  public TableName getTableName() {
    return tableName;
  }

  public void setTableName(TableName tableName) {
    this.tableName = tableName;
  }

  @Override
  public String getEffectiveCatalog() {
    String effective;
    if(tableName != null){
      effective = tableName.getCatalogName().getName();
    }else{
      effective = catalog;
    }
    if(sessionCatalog != null){
      effective = sessionCatalog;
    }
    return effective;
  }
  */

}
