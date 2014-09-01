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

package com.stratio.meta.common.logicalplan;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;

import java.util.ArrayList;
import java.util.List;

/**
 * Project operation to retrieve a list of columns from the datastore.
 */
public class Project extends TransformationStep{

  /**
   * Table name.
   */
  private final TableName tableName;

  /**
   * List of columns.
   */
  private final List<ColumnName> columnList = new ArrayList<>();

  /**
   * Create a projection.
   * @param operation The operation to be applied.
   * @param tableName The table name.
   */
  public Project(Operations operation, TableName tableName){
    super(operation);
    this.tableName = tableName;
  }

  /**
   * Create a projection.
   * @param operation The operation to be applied.
   * @param tableName Table name.
   * @param columnList List of columns.
   */
  public Project(Operations operation, TableName tableName, List<ColumnName> columnList) {
    super(operation);
    this.tableName = tableName;
    this.columnList.addAll(columnList);
  }

  /**
   * Add a column to the list of columns to be projected.
   * @param column The column.
   */
  public void addColumn(ColumnName column){
    this.columnList.add(column);
  }

  /**
   * Get the name of the target catalog.
   * @return The name.
   */
  public String getCatalogName() {
    return this.tableName.getCatalogName().getName();
  }

  /**
   * Get the name of the target table.
   * @return The name.
   */
  public TableName getTableName() {
    return tableName;
  }

  /**
   * Get the list of columns to be retrieved.
   * @return A list of {@link com.stratio.meta2.common.data.ColumnName}.
   */
  public List<ColumnName> getColumnList() {
    return columnList;
  }

}
