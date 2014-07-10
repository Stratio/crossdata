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

package com.stratio.meta.deep.transfer;

import com.stratio.meta.core.structures.GroupByFunction;

/**
 * Transfer object to store the column information for a specific field
 * 
 * @author Óscar Puertas
 * 
 */
public class ColumnInfo {

  private final String table;

  private final String field;

  private final GroupByFunction aggregationFunction;

  public ColumnInfo(String table, String field, GroupByFunction aggregationFunction) {
    this.table = table;
    this.field = field;
    this.aggregationFunction = aggregationFunction;
  }

  public ColumnInfo(String table, String field) {
    this(table, field, null);
  }

  public String getTable() {
    return table;
  }

  public String getField() {
    return field;
  }

  public GroupByFunction getAggregationFunction() {
    return aggregationFunction;
  }

  /**
   * Builds and returns the column name from the column information.
   * 
   * @return Column name
   */
  public String getColumnName() {

    String columnName = null;

    if (this.aggregationFunction != null) {
      if (this.aggregationFunction == GroupByFunction.COUNT) {
        columnName = "COUNT(*)";
      } else {
        columnName = this.aggregationFunction.name() + "(" + this.field + ")";
      }
    } else {
      columnName = this.field;
    }

    return columnName;
  }
}
