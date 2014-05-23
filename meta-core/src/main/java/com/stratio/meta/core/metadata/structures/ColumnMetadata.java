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

package com.stratio.meta.core.metadata.structures;

public class ColumnMetadata {

    /**
     * Catalog associated with the parent table.
     */
    private final String catalogName;

    /**
     * Parent table.
     */
    private final String tableName;

    /**
     * Name of the column.
     */
    private final String columnName;

    /**
     * Class constructor.
     * @param catalogName Name of the catalog associated with the parent table.
     * @param tableName Parent table name.
     * @param columnName Column name.
     */
    public ColumnMetadata(String catalogName, String tableName, String columnName){
        this.catalogName = catalogName;
        this.tableName = tableName;
        this.columnName = columnName;
    }


}
