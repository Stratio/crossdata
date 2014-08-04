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

package com.stratio.meta.common.metadata.structures;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TableMetadata implements Serializable {

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
   * 
   * @param tableName Name of the table.
   * @param parentCatalog Parent catalog.
   * @param type Type of table.
   */
  public TableMetadata(String tableName, String parentCatalog, TableType type) {
    this.parentCatalog = parentCatalog;
    this.tableName = tableName;
    this.type = type;
  }

  /**
   * Class constructor.
   * 
   * @param tableName Name of the table.
   * @param parentCatalog Parent catalog.
   * @param columns Set of columns.
   */
  public TableMetadata(String tableName, String parentCatalog, TableType type,
      Set<ColumnMetadata> columns) {
    this(tableName, parentCatalog, type);
    this.columns.addAll(columns);
  }

  /**
   * Get the name of the table.
   * 
   * @return The name.
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Get the parent catalog.
   * 
   * @return The name or null if not set.
   */
  public String getParentCatalog() {
    return parentCatalog;
  }

  /**
   * Get the set of columns.
   * 
   * @return The set of columns.
   */
  public Set<ColumnMetadata> getColumns() {
    return columns;
  }

  public ColumnMetadata getColumn(String colName) {
    ColumnMetadata foundCM = null;
    for (ColumnMetadata cm : columns) {
      if (cm.getColumnName().equalsIgnoreCase(colName)) {
        foundCM = cm;
        break;
      }
    }
    return foundCM;
  }

  public String exportAsString() {

    StringBuilder sb = new StringBuilder();

    sb.append("CREATE TABLE ").append(this.parentCatalog).append(".").append(this.tableName)
        .append(" (").append(System.lineSeparator());
    for (ColumnMetadata columnMetadata : this.columns) {
      sb.append("    ").append(columnMetadata.toString()).append(",")
          .append(System.lineSeparator());
    }
    sb.deleteCharAt(sb.length() - 2);
    if (type == TableType.EPHEMERAL) {
      sb.append(") WITH EPHEMERAL = true;");
    } else {
      sb.append(");");
    }

    return sb.toString();
  }
}
