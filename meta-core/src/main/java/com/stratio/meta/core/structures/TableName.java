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

package com.stratio.meta.core.structures;

/**
 * Table name recognized by the parser. The name may contain:
 * <ul>
 *   <li>catalog.table</li>
 *   <li>table</li>
 * </ul>
 */
public class TableName {

  private String catalog = null;

  private final String tableName;

  public TableName(String tableName){
    this.tableName = tableName;
  }

  public void setCatalog(String catalog){
    this.catalog = catalog;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getTableName() {
    return tableName;
  }

  public boolean containsCatalog(){
    return catalog != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if(catalog != null){
      sb.append(catalog).append(".");
    }
    sb.append(tableName);
    return sb.toString();
  }
}
