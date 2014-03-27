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

public class CustomIndexMetadata {

    /**
     * Type of index associated with the column.
     */
    private final IndexType _type;

    /**
     * Index target column.
     */
    private final ColumnMetadata _column;

    /**
     * The name of the index.
     */
    private final String _indexName;

    /**
     * Index options.
     */
    private String _options = null;



    /**
     * Class constructor for default Cassandra indexes.
     * @param columnMetadata The column metadata associated with the index.
     * @param type Type of index.
     */
    public CustomIndexMetadata(ColumnMetadata columnMetadata, String indexName, IndexType type){
        _type = type;
        _column = columnMetadata;
        _indexName = indexName;
    }

    /**
     * Get the type of the index associated with the column.
     * @return The type of index.
     */
    public IndexType getIndexType(){
        return _type;
    }

    /**
     * Get the index name.
     * @return The name.
     */
    public String getIndexName(){
        return _indexName;
    }

    /**
     * Set the custom index options.
     * @param options The options.
     */
    public void setIndexOptions(String options){
        _options = options;
    }

    /**
     * Get the options associated with the index.
     * @return The options or null if not set.
     */
    public String getIndexOptions(){
        return _options;
    }
}
