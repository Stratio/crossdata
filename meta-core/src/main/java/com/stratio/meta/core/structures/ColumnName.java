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

package com.stratio.meta.core.structures;



/**
 * Column name recognized by the parser. The name may contain:
 * <ul>
 *   <li>catalog.table.column</li>
 *   <li>table.column</li>
 *   <li>column</li>
 * </ul>
 */
public class ColumnName {

  /**
   * Name of the catalog.
   */
  private String catalog = null;

  /**
   * Name of the table.
   */
  private String table = null;

  /**
   * Name of the column.
   */
  private final String columnName;

  /**
   * Default constructor.
   * @param columnName Name of the column.
   */
  public ColumnName(String columnName){
    this.columnName = columnName;
  }

  public void setCatalog(String catalog){
    this.catalog = catalog;
  }

  public void setTable(String table){
    this.table = table;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getTable() {
    return table;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if(catalog != null){
      sb.append(catalog).append(".");
    }
    if(table != null){
      sb.append(table).append(".");
    }
    sb.append(columnName);
    return sb.toString();
  }
}
