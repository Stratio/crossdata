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

package com.stratio.meta.deep.transfer;

import java.io.Serializable;

import com.stratio.meta.core.structures.GroupByFunction;

/**
 * Transfer object to store the column information for a specific field
 * 
 * @author Óscar Puertas
 * 
 */
public class ColumnInfo implements Serializable {

  private static final long serialVersionUID = -7511331222469777299L;

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
