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

package com.stratio.meta.core.metadata;

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.meta.common.metadata.structures.CatalogMetadata;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.common.metadata.structures.TableMetadata;

import java.util.*;

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
     * Transform a Cassandra {@link com.datastax.driver.core.KeyspaceMetadata} into
     * a META CatalogMetadata.
     * @param keyspaceMetadata The keyspace metadata.
     * @return A {@link com.stratio.meta.common.metadata.structures.CatalogMetadata}.
     */
    public CatalogMetadata toCatalogMetadata(KeyspaceMetadata keyspaceMetadata){
        Set<TableMetadata> tables = new HashSet<>(keyspaceMetadata.getTables().size());
        CatalogMetadata result = new CatalogMetadata(
                keyspaceMetadata.getName(),
                tables);
        for(com.datastax.driver.core.TableMetadata table: keyspaceMetadata.getTables()){
            tables.add(toTableMetadata(keyspaceMetadata.getName(), table));
        }
        return result;
    }

    public TableMetadata toTableMetadata(String parentCatalog,
                                         com.datastax.driver.core.TableMetadata tableMetadata){
        Set<ColumnMetadata> columns = new HashSet<>(tableMetadata.getColumns().size());
        for(com.datastax.driver.core.ColumnMetadata column: tableMetadata.getColumns()){
            columns.add(toColumnMetadata(tableMetadata.getName(), column));
        }
        TableMetadata result = new TableMetadata(
                tableMetadata.getName(),
                parentCatalog,
                columns);
        return result;
    }

    public ColumnMetadata toColumnMetadata(String parentTable,
                                           com.datastax.driver.core.ColumnMetadata columnMetadata){
        ColumnMetadata result = new ColumnMetadata(
                parentTable,
                columnMetadata.getName());
        ColumnType type = toColumnType(columnMetadata.getType().getName().toString());
        result.setType(type);
        return result;
    }

    /**
     * Obtain the ColumnType associated with a database type.
     * @param dbTypeName The name of the database type.
     * @return A {@link com.stratio.meta.common.metadata.structures.ColumnType} or null
     * if the type should be considered native.
     */
    public abstract ColumnType toColumnType(String dbTypeName);
}
