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

package com.stratio.crossdata.common.result;

import java.util.List;

import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;

/**
 * Class to return results from the execution of a metadata-related operation.
 */
public final class MetadataResult extends Result {

    /**
     * Operation identifier to create catalog.
     */
    public static final int OPERATION_CREATE_CATALOG = 1;

    /**
     * Operation identifier to create table.
     */
    public static final int OPERATION_CREATE_TABLE = 2;

    /**
     * Operation identifier to create index.
     */
    public static final int OPERATION_CREATE_INDEX = 3;

    /**
     * Operation identifier to drop catalog.
     */
    public static final int OPERATION_DROP_CATALOG = 4;

    /**
     * Operation identifier to drop table.
     */
    public static final int OPERATION_DROP_TABLE = 5;

    /**
     * Operation identifier to drop index.
     */
    public static final int OPERATION_DROP_INDEX= 6;

    /**
     * Operation identifier to list catalogs.
     */
    public static final int OPERATION_LIST_CATALOGS = 7;

    /**
     * Operation identifier to list tables.
     */
    public static final int OPERATION_LIST_TABLES = 8;

    /**
     * Operation identifier to list columns.
     */
    public static final int OPERATION_LIST_COLUMNS = 9;

    /**
     * Operation identifier to ALTER CATALOG.
     */
    public static final int OPERATION_ALTER_CATALOG = 10;

    /**
     * Operation identifier to ALTER TABLE.
     */
    public static final int OPERATION_ALTER_TABLE = 11;

    /**
     * Operation identifier to PROVIDE METADATA.
     */
    public static final int OPERATION_PROVIDE_METADATA = 12;

    /**
     * Operation identifier to PROVIDE TABLE METADATA.
     */
    public static final int OPERATION_PROVIDE_TABLE_METADATA = 13;

    /**
     * Operation identifier to PROVIDE CATALOG METADATA.
     */
    public static final int OPERATION_PROVIDE_CATALOG_METADATA = 14;

    /**
     * Unknown Operation.
     */
    public static final int OPERATION_UNKNOWN = 15;


    /**
     * Operation bound to the {@link com.stratio.crossdata.common.result.MetadataResult}.
     */
    private int operation = 0;

    /**
     * Serial version UID in order to be Serializable.
     */
    private static final long serialVersionUID = 7257573696937869953L;

    /**
     * List of catalogs in the database.
     */
    private List<String> catalogList = null;

    /**
     * List of tables in a catalog.
     */
    private List<TableMetadata> tableList = null;

    /**
     * Map of columns in a table.
     */
    private List<ColumnMetadata> columnList = null;

    /**
     * Private constructor of the factory.
     */
    private MetadataResult(final int operation) {
        this.operation = operation;
    }

    /**
     * Creates a successful query.
     *
     * @param operation to bind
     * @return A {@link com.stratio.crossdata.common.result.MetadataResult}.
     */
    public static MetadataResult createSuccessMetadataResult(final int operation) {
        return new MetadataResult
                (operation);
    }

    public List<String> getCatalogList() {
        return catalogList;
    }

    /**
     * Gets the operation bound to the {@link com.stratio.crossdata.common.result.MetadataResult}.
     *
     * @return int with the operation type
     */

    public int getOperation() {
        return operation;
    }

    /**
     *java.lang.Object Set the catalog list.
     *
     * @param catalogList The list.
     */
    public void setCatalogList(List<String> catalogList) {
        this.catalogList = catalogList;
    }

    public List<TableMetadata> getTableList() {
        return tableList;
    }

    /**
     * Set the table list.
     *
     * @param tableList The list.
     */
    public void setTableList(List<TableMetadata> tableList) {
        this.tableList = tableList;
    }

    public List<ColumnMetadata> getColumnList() {
        return columnList;
    }

    /**
     * Set the column list.
     *
     * @param columnList The list.
     */
    public void setColumnList(List<ColumnMetadata> columnList) {
        this.columnList = columnList;
    }

    /**
     * Analyzes the operation bound to the MetadataResult and generates an String.
     *
     * @return The string representation of the operation to be carried out.
     */
    @Override
    public String toString() {
        switch (this.operation) {

        case MetadataResult.OPERATION_CREATE_CATALOG:
            return "CATALOG created successfully";
        case MetadataResult.OPERATION_CREATE_TABLE:
            return "TABLE created successfully";
        case MetadataResult.OPERATION_CREATE_INDEX:
            return "INDEX created successfully";
        case MetadataResult.OPERATION_DROP_CATALOG:
            return "CATALOG dropped successfully";
        case MetadataResult.OPERATION_DROP_TABLE:
            return "TABLE dropped successfully";
        case MetadataResult.OPERATION_DROP_INDEX:
            return "INDEX dropped successfully";
        case MetadataResult.OPERATION_LIST_CATALOGS:
            return catalogList.toString();
        case MetadataResult.OPERATION_LIST_TABLES:
            return tableList.toString();
        case MetadataResult.OPERATION_LIST_COLUMNS:
            return columnList.toString();

        default:
            return "OK";

        }

    }



}
