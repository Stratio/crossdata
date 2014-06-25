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

package com.stratio.meta.common.metadata.structures;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TableMetadata implements Serializable{

  /**
   * Serial version UID in order to be Serializable.
   */
  private static final long serialVersionUID = 8495217651370053187L;

  /**
   * Parent catalog.
   */
  private final String parentCatalog;

  /**
   * Name of the table.
   */
  private final String tableName;

  /**
   * Set of columns.
   */
  private Set<ColumnMetadata> columns = new HashSet<>();

  /**
   * Type of table.
   */
  private final TableType type;

  /**
   * Class constructor.
   * @param tableName Name of the table.
   * @param parentCatalog Parent catalog.
   * @param type Type of table.
   */
  public TableMetadata(String tableName, String parentCatalog, TableType type){
    this.parentCatalog = parentCatalog;
    this.tableName = tableName;
    this.type = type;
  }

  /**
   * Class constructor.
   * @param tableName Name of the table.
   * @param parentCatalog Parent catalog.
   * @param columns Set of columns.
   */
  public TableMetadata(String tableName,
                       String parentCatalog,
                       TableType type,
                       Set<ColumnMetadata> columns){
    this(tableName, parentCatalog, type);
    this.columns.addAll(columns);
  }

  /**
   * Get the name of the table.
   * @return The name.
   */
  public String getTableName(){
    return tableName;
  }

  /**
   * Get the parent catalog.
   * @return The name or null if not set.
   */
  public String getParentCatalog(){
    return parentCatalog;
  }

  /**
   * Get the set of columns.
   * @return The set of columns.
   */
  public Set<ColumnMetadata> getColumns(){
    return columns;
  }

  public ColumnMetadata getColumn(String colName) {
    ColumnMetadata foundCM = null;
    for(ColumnMetadata cm: columns){
      if(cm.getColumnName().equalsIgnoreCase(colName)){
        foundCM = cm;
        break;
      }
    }
    return foundCM;
  }
}

