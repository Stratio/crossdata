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

package com.stratio.meta.common.statements.structures;



/**
 * Column name recognized by the parser. The name may contain:
 * <ul>
 *   <li>catalog.table.column</li>
 *   <li>table.column</li>
 *   <li>column</li>
 *   <li>catalog.table.column[1]</li>
 *   <li>catalog.table.column['name']</li>
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

  //TODO: Add indexTerm field, isIndexed(), setIndex(Term t)

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
