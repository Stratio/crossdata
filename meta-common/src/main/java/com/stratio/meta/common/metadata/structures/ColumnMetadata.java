/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.common.metadata.structures;

import java.io.Serializable;

public class ColumnMetadata implements Serializable {

  /**
   * Serial version UID in order to be Serializable.
   */
  private static final long serialVersionUID = -2151960196552242173L;

  /**
   * Parent table.
   */
  private final String tableName;

  /**
   * Name of the column.
   */
  private String columnName;

  /**
   * Alias of the column.
   */
  private String columnAlias;

  /**
   * Column type.
   */
  private ColumnType type;

  /**
   * Class constructor.
   * 
   * @param tableName Parent table name.
   * @param columnName Column name.
   */
  public ColumnMetadata(String tableName, String columnName) {
    this.tableName = tableName;
    this.columnName = columnName;
  }

  public String getTableName() {
    return tableName;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getColumnNameToShow() {
    return (this.columnAlias == null) ? this.columnName : this.columnAlias;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  /**
   * Set the column type.
   * 
   * @param type The column type.
   */
  public void setType(ColumnType type) {
    this.type = type;
  }

  /**
   * Get the column type.
   * 
   * @return A {@link com.stratio.meta.common.metadata.structures.ColumnType}.
   */
  public ColumnType getType() {
    return type;
  }

  public String getColumnAlias() {
    return columnAlias;
  }

  public void setColumnAlias(String columnAlias) {
    this.columnAlias = columnAlias;
  }

  @Override
  public String toString() {
    return this.columnName + ' ' + this.getType();
  }
}
