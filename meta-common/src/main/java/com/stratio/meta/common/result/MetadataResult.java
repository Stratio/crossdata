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

package com.stratio.meta.common.result;

import java.util.List;

import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.result.Result;

public class MetadataResult extends Result {

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
    private MetadataResult() {
    }

    /**
     * Create a successful query.
     *
     * @return A {@link com.stratio.meta.common.result.MetadataResult}.
     */
    public static MetadataResult createSuccessMetadataResult() {
        return new MetadataResult();
    }

    public List<String> getCatalogList() {

        return catalogList;
    }

    /**
     * Set the catalog list.
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
}
