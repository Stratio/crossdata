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
