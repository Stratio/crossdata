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

package com.stratio.meta.common.result;

import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.TableMetadata;

import java.util.List;

public class MetadataResult extends Result {

  /**
   * List of catalogs in the database.
   */
  private List<String> catalogList = null;

  /**
   * List of tables in a catalog.
   */
  private List<TableMetadata> tableList = null;

  /**
   * Map of columns in a table.
   */
  private List<ColumnMetadata> columnList = null;

  /**
   * Private constructor of the factory.
   *
   * @param error           Whether an error occurred during the execution.
   * @param errorMessage    The error message in case of {@code error}.
   * @param ksChanged       Whether the current keyspace in the user session is modified by the
   *                        execution.
   * @param currentKeyspace The current keyspace after the execution.
   */
  private MetadataResult(boolean error, String errorMessage, boolean ksChanged,
                         String currentKeyspace) {
    super(error, errorMessage, ksChanged, currentKeyspace);
  }

  /**
   * Set the catalog list.
   *
   * @param catalogList The list.
   */
  public void setCatalogList(List<String> catalogList) {
    this.catalogList = catalogList;
  }

  /**
   * Set the table list.
   *
   * @param tableList The list.
   */
  public void setTableList(List<TableMetadata> tableList) {
    this.tableList = tableList;
  }

  /**
   * Set the column list.
   *
   * @param columnList The list.
   */
  public void setColumnList(List<ColumnMetadata> columnList) {
    this.columnList = columnList;
  }

  /**
   * Create a successful query.
   *
   * @return A {@link com.stratio.meta.common.result.MetadataResult}.
   */
  public static MetadataResult createSuccessMetadataResult() {
    return new MetadataResult(false, null, false, null);
  }

  /**
   * Create a failed query result.
   *
   * @param errorMessage The associated error message.
   * @return A {@link com.stratio.meta.common.result.QueryResult}.
   */
  public static MetadataResult createFailMetadataResult(String errorMessage) {
    return new MetadataResult(true, errorMessage, false, null);
  }

  public List<String> getCatalogList() {
    return catalogList;
  }

  public List<TableMetadata> getTableList() {
    return tableList;
  }

  public List<ColumnMetadata> getColumnList() {
    return columnList;
  }
}
