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

package com.stratio.meta.core.metadata;


import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.deep.entity.Cell;
import com.stratio.meta.common.metadata.structures.CatalogMetadata;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.common.metadata.structures.TableMetadata;
import com.stratio.meta.common.metadata.structures.TableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMetadataHelper {

  /**
   * Database type mappings.
   */
  protected static Map<ColumnType, String> dbType = new HashMap<>();

  /**
   * Database Java class mappings.
   */
  protected static Map<ColumnType, Class<?>> dbClass = new HashMap<>();

  /**
   * Transform a Cassandra {@link com.datastax.driver.core.KeyspaceMetadata} into a META
   * CatalogMetadata.
   * 
   * @param keyspaceMetadata The keyspace metadata.
   * @return A {@link com.stratio.meta.common.metadata.structures.CatalogMetadata}.
   */
  public CatalogMetadata toCatalogMetadata(KeyspaceMetadata keyspaceMetadata) {
    Set<TableMetadata> tables = new HashSet<>(keyspaceMetadata.getTables().size());
    for (com.datastax.driver.core.TableMetadata table : keyspaceMetadata.getTables()) {
      tables.add(toTableMetadata(keyspaceMetadata.getName(), table));
    }
    CatalogMetadata result = new CatalogMetadata(keyspaceMetadata.getName(), tables);
    return result;
  }

  public TableMetadata toTableMetadata(String parentCatalog,
      com.datastax.driver.core.TableMetadata tableMetadata) {
    Set<ColumnMetadata> columns = new LinkedHashSet<>(tableMetadata.getColumns().size());
    for (com.datastax.driver.core.ColumnMetadata column: tableMetadata.getColumns()) {
      columns.add(toColumnMetadata(tableMetadata.getName(), column));
    }
    List<String> partitionKey = fromColumnsMetadataToColumnsString(tableMetadata.getPartitionKey());
    List<String> clusteringKey =
        fromColumnsMetadataToColumnsString(tableMetadata.getClusteringColumns());
    TableMetadata result =
        new TableMetadata(tableMetadata.getName(), parentCatalog, TableType.DATABASE, columns,
            partitionKey, clusteringKey);
    return result;
  }

  private List<String> fromColumnsMetadataToColumnsString(
      List<com.datastax.driver.core.ColumnMetadata> columnsMetadata) {

    List<String> columnsString = new ArrayList<>();
    Iterator<com.datastax.driver.core.ColumnMetadata> columnsIt = columnsMetadata.iterator();
    while (columnsIt.hasNext()) {
      com.datastax.driver.core.ColumnMetadata column = columnsIt.next();
      columnsString.add(column.getName());
    }

    return columnsString;
  }

  public ColumnMetadata toColumnMetadata(String parentTable,
      com.datastax.driver.core.ColumnMetadata columnMetadata) {
    ColumnMetadata result = new ColumnMetadata(parentTable, columnMetadata.getName());
    ColumnType type = toColumnType(columnMetadata.getType().getName().toString());
    result.setType(type);
    return result;
  }

  /**
   * Obtain the ColumnType associated with a database type.
   * 
   * @param dbTypeName The name of the database type.
   * @return A {@link com.stratio.meta.common.metadata.structures.ColumnType}.
   */
  public abstract ColumnType toColumnType(String dbTypeName);

  /**
   * Obtain the ColumnType associated with a Deep cell.
   * 
   * @param deepCell The deep cell.
   * @return A {@link com.stratio.meta.common.metadata.structures.ColumnType}.
   */
  public abstract ColumnType toColumnType(Cell deepCell);
}
