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

package com.stratio.meta2.common.data;

public class ColumnName extends Name {

  /**
   * Name of the column.
   */
  private final String name;

  private TableName tableName;

  /**
   * Default constructor.
   *
   * @param catalogName Name of the catalog.
   * @param tableName Name of the table.
   * @param columnName Name of the column.
   */
  public ColumnName(String catalogName, String tableName, String columnName) {
    if(tableName != null && !tableName.isEmpty()){
      this.tableName = new TableName(catalogName, tableName);
    }else{
      this.tableName = null;
    }
    this.name = columnName;
  }

  /**
   * Constructor using existing TableName.
   *
   * @param tableName TableName.
   * @param columnName Name of the column.
   */
  public ColumnName(TableName tableName, String columnName) {
    this.tableName = tableName;
    this.name = columnName;
  }

  public TableName getTableName() {
    return tableName;
  }

  public void setTableName(TableName tableName) {
    this.tableName = tableName;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean isCompletedName() {
    return tableName != null && tableName.isCompletedName();
  }

  public String getQualifiedName() {
    String result;
    if (isCompletedName()) {
      result= QualifiedNames.getColumnQualifiedName(this.getTableName().getCatalogName().getName(),
          getTableName().getName(), getName());
    }else{
      String catalogName = UNKNOWN_NAME;
      String tableName = UNKNOWN_NAME;
      if(this.getTableName() != null){
        tableName = this.getTableName().getName();
        if(this.getTableName().getCatalogName() != null){
          catalogName = this.getTableName().getCatalogName().getName();
        }
      }

      result=QualifiedNames.getColumnQualifiedName(catalogName, tableName, getName());
    }
    return result;
  }

  @Override public NameType getType() {
    return null;
  }

}
