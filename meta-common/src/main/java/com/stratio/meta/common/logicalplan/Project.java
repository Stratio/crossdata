/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.common.logicalplan;

import com.stratio.meta.common.metadata.structures.ColumnMetadata;

import java.util.List;

/**
 * Project operation to retrieve a list of columns from the datastore.
 */
public class Project {

  /**
   * Catalog name.
   */
  private final String catalogName;

  /**
   * Table name.
   */
  private final String tableName;

  /**
   * List of columns.
   */
  private final List<ColumnMetadata> columnList;

  /**
   * Create a projection.
   * @param catalogName Catalog name.
   * @param tableName Table name.
   * @param columnList List of columns.
   */
  public Project(String catalogName, String tableName, List<ColumnMetadata> columnList) {
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
  public String getTableName() {
    return tableName;
  }

  /**
   * Get the list of columns to be retrieved.
   * @return A list of {@link com.stratio.meta.common.metadata.structures.ColumnMetadata}.
   */
  public List<ColumnMetadata> getColumnList() {
    return columnList;
  }

}
