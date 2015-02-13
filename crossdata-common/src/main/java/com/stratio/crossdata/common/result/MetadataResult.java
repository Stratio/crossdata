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

import java.util.ArrayList;
import java.util.List;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;

/**
 * Class to return results from the execution of a metadata-related operation.
 */
public final class MetadataResult extends Result {

    /**
     * Unknown Operation.
     */
    public static final int OPERATION_UNKNOWN = 0;

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

    public static final int OPERATION_DESCRIBE_CATALOG = 8;

    /**
     * Operation identifier to list tables.
     */
    public static final int OPERATION_LIST_TABLES = 9;

    public static final int OPERATION_DESCRIBE_TABLE = 10;

    /**
     * Operation identifier to list columns.
     */
    public static final int OPERATION_LIST_COLUMNS = 11;

    /**
     * Operation identifier to ALTER CATALOG.
     */
    public static final int OPERATION_ALTER_CATALOG = 12;

    /**
     * Operation identifier to ALTER TABLE.
     */
    public static final int OPERATION_ALTER_TABLE = 13;

    public static final int OPERATION_DISCOVER_METADATA = 14;

    public static final int OPERATION_IMPORT_CATALOGS = 15;

    public static final int OPERATION_IMPORT_CATALOG = 16;

    public static final int OPERATION_IMPORT_TABLE = 17;

    /**
     * Operation bound to the {@link com.stratio.crossdata.common.result.MetadataResult}.
     */
    private int operation = 0;

    /**
     * Serial version UID in order to be Serializable.
     */
    private static final long serialVersionUID = 7257573696937869953L;

    private List<CatalogMetadata> catalogMetadataList = null;

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

    private boolean noOperation = false;

    /**
     * Private constructor of the factory.
     */
    private MetadataResult(final int operation) {
        this(operation, false);
    }

    private MetadataResult(final int operation, boolean noOperation) {
        this.operation = operation;
        this.noOperation = noOperation;
    }

    /**
     * Creates a successful query.
     *
     * @param operation to bind
     * @return A {@link com.stratio.crossdata.common.result.MetadataResult}.
     */
    public static MetadataResult createSuccessMetadataResult(final int operation) {
        return new MetadataResult(operation);
    }

    /**
     * Create a succeeded metadata result.
     * @param operation The operation.
     * @param noOperation The noOperation.
     * @return The result of the Metadata Operation
     */
    public static MetadataResult createSuccessMetadataResult(final int operation, boolean noOperation) {
        return new MetadataResult(operation, noOperation);
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

    public List<CatalogMetadata> getCatalogMetadataList() {
        return catalogMetadataList;
    }

    public void setCatalogMetadataList(List<CatalogMetadata> catalogMetadataList) {
        this.catalogMetadataList = catalogMetadataList;
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
        if(noOperation){
            return "None operation was necessary";
        }
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
        case MetadataResult.OPERATION_DESCRIBE_CATALOG:
            return catalogMetadataList.get(0).toString();
        case MetadataResult.OPERATION_LIST_TABLES:
            return tableList.toString();
        case MetadataResult.OPERATION_DESCRIBE_TABLE:
            return tableList.get(0).toString();
        case MetadataResult.OPERATION_LIST_COLUMNS:
            return columnList.toString();
        case MetadataResult.OPERATION_DISCOVER_METADATA:
            return showCatalogs(catalogMetadataList);
        case MetadataResult.OPERATION_IMPORT_CATALOGS:
            return "Catalogs " + showCatalogNames(catalogMetadataList) + " imported successfully";
        case MetadataResult.OPERATION_IMPORT_CATALOG:
            return "Catalog " + catalogMetadataList.get(0).getName() + " imported successfully";
        case MetadataResult.OPERATION_IMPORT_TABLE:
            return "Table " + tableList.get(0).getName() + " imported successfully";
        default:
            return "OK";
        }

    }

    private String showCatalogs(List<CatalogMetadata> catalogMetadataList) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        for(CatalogMetadata cm: catalogMetadataList){
            sb.append(" * Catalog: ").append(cm.getName()).append(System.lineSeparator());
            for(TableName tb: cm.getTables().keySet()){
                sb.append("\t").append(" - Table: ").append(tb).append(System.lineSeparator());
            }
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    private List<CatalogName> showCatalogNames(List<CatalogMetadata> catalogMetadataList) {
        List<CatalogName> catalogNames = new ArrayList<>();
        for(CatalogMetadata cm: catalogMetadataList){
            catalogNames.add(cm.getName());
        }
        return catalogNames;
    }



}
