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

import com.datastax.driver.core.ColumnMetadata;
import com.stratio.meta.core.structures.IndexType;

import java.util.ArrayList;
import java.util.List;

public class CustomIndexMetadata {

    /**
     * Type of index associated with the column.
     */
    private final IndexType type;

    /**
     * Cassandra column linked with the index.
     */
    private final ColumnMetadata column;

    /**
     * List of columns indexed.
     */
    private final List<String> indexedColumns;

    /**
     * The name of the index.
     */
    private final String indexName;

    /**
     * Index options.
     */
    private String options = null;

    /**
     * Class constructor for default Cassandra indexes.
     * @param columnMetadata The column metadata associated with the index.
     * @param indexName The name of the index.
     * @param type Type of index.
     * @param indexedColumn The name of the column indexed by the current index.
     */
    public CustomIndexMetadata(ColumnMetadata columnMetadata, String indexName, IndexType type, String indexedColumn){
        this.type = type;
        column = columnMetadata;
        this.indexName = indexName;
        indexedColumns = new ArrayList<>();
        indexedColumns.add(indexedColumn);
    }

    /**
     * Class constructor for default Cassandra indexes.
     * @param columnMetadata The column metadata associated with the index.
     * @param indexName The name of the index.
     * @param type Type of index.
     * @param indexedColumns The names of the columns indexed by the current index.
     */
    public CustomIndexMetadata(ColumnMetadata columnMetadata, String indexName, IndexType type, List<String> indexedColumns){
        this.type = type;
        column = columnMetadata;
        this.indexName = indexName;
        this.indexedColumns = new ArrayList<>();
        this.indexedColumns.addAll(indexedColumns);
    }

    /**
     * Get the type of the index associated with the column.
     * @return The type of index.
     */
    public IndexType getIndexType(){
        return type;
    }

    /**
     * Get the index name.
     * @return The name.
     */
    public String getIndexName(){
        return indexName;
    }

    /**
     * Get the list of indexed columns.
     * @return The list of indexed columns.
     */
    public List<String> getIndexedColumns(){
        return indexedColumns;
    }


}
