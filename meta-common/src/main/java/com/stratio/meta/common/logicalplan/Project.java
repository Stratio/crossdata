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

import java.util.List;

import com.stratio.meta.common.statements.structures.ColumnName;
import com.stratio.meta.common.statements.structures.TableName;

/**
 * Project operation to retrieve a list of columns from the datastore.
 */
public class Project extends TransformationStep{

  /**
   * Catalog name.
   */
  private final String catalogName;

  /**
   * Table name.
   */
  private final TableName tableName;

  /**
   * List of columns.
   */
  private final List<ColumnName> columnList;

  /**
   * Create a projection.
   * @param catalogName Catalog name.
   * @param tableName Table name.
   * @param columnList List of columns.
   */
  public Project(String catalogName, TableName tableName, List<ColumnName> columnList) {
    this.catalogName = catalogName;
    this.tableName = tableName;
    this.columnList = columnList;
  }

  /**
   * Get the name of the target catalog.
   * @return The name.
   */
  public String getCatalogName() {
    return catalogName;
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
   * @return A list of {@link com.stratio.meta.common.statements.structures.ColumnName}.
   */
  public List<ColumnName> getColumnList() {
    return columnList;
  }

}
