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

package com.stratio.meta.common.result;

import java.util.List;
import java.util.Map;

public class MetadataResult extends Result{

    /**
     * List of catalogs in the database.
     */
    private List<String> catalogList = null;

    /**
     * List of tables in a catalog.
     */
    private List<String> tableList = null;

    /**
     * Map of columns in a table.
     */
    private Map<String, String> columnMap = null;

    /**
     * Private constructor of the factory.
     * @param error           Whether an error occurred during the execution.
     * @param errorMessage    The error message in case of {@code error}.
     * @param ksChanged       Whether the current keyspace in the user session is modified by the execution.
     * @param currentKeyspace The current keyspace after the execution.
     */
    private MetadataResult(boolean error, String errorMessage, boolean ksChanged, String currentKeyspace) {
        super(error, errorMessage, ksChanged, currentKeyspace);
    }

    /**
     * Private constructor of the factory.
     * @param error           Whether an error occurred during the execution.
     * @param errorMessage    The error message in case of {@code error}.
     * @param ksChanged       Whether the current keyspace in the user session is modified by the execution.
     * @param currentKeyspace The current keyspace after the execution.
     * @param content         List of catalogs or tables.
     * @param catalogs     Whether the list contains catalogs or tables.
     */
    private MetadataResult(boolean error, String errorMessage,
                           boolean ksChanged, String currentKeyspace,
                           List<String> content, boolean catalogs) {
        super(error, errorMessage, ksChanged, currentKeyspace);
        if(catalogs){
            catalogList = content;
        }else{
            tableList = content;
        }
    }

    /**
     * Private constructor of the factory.
     * @param error           Whether an error occurred during the execution.
     * @param errorMessage    The error message in case of {@code error}.
     * @param ksChanged       Whether the current keyspace in the user session is modified by the execution.
     * @param currentKeyspace The current keyspace after the execution.
     * @param columnMap       Map with the columns in a table.
     */
    private MetadataResult(boolean error, String errorMessage,
                           boolean ksChanged, String currentKeyspace,
                           Map<String, String> columnMap) {
        super(error, errorMessage, ksChanged, currentKeyspace);
        this.columnMap = columnMap;
    }

    /**
     * Create a successful query.
     * @param content List of catalogs or tables.
     * @param catalogs Whether the list contains catalogs or tables.
     * @return A {@link com.stratio.meta.common.result.MetadataResult}.
     */
    public static MetadataResult createSuccessMetadataResult(List<String> content, boolean catalogs){
        return new MetadataResult(false, null, false, null, content, catalogs);
    }

    /**
     * Create a successful query.
     * @param columnMap Map with the columns in a table.
     * @return A {@link com.stratio.meta.common.result.MetadataResult}.
     */
    public static MetadataResult createSuccessMetadataResult(Map<String, String> columnMap){
        return new MetadataResult(false, null, false, null, columnMap);
    }

    /**
     * Create a failed query result.
     * @param errorMessage The associated error message.
     * @return A {@link com.stratio.meta.common.result.QueryResult}.
     */
    public static MetadataResult createFailMetadataResult(String errorMessage){
        return new MetadataResult(true,errorMessage,false,null);
    }

    public List<String> getCatalogList(){
        return catalogList;
    }

    public List<String> getTableList(){
        return tableList;
    }

    public Map<String, String> getColumnMap(){
        return columnMap;
    }
}
