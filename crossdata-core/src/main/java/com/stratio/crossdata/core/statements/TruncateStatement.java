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

package com.stratio.crossdata.core.statements;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.core.validator.requirements.ValidationRequirements;
import com.stratio.crossdata.core.validator.requirements.ValidationTypes;

/**
 * TruncateStatement class.
 */
public class TruncateStatement extends StorageStatement implements ITableStatement {

    /**
     * The name of the table.
     */
    private TableName tableName;

    /**
     * Class Constructor.
     *
     * @param tableName The table name of the truncate statement.
     */
    public TruncateStatement(TableName tableName) {
        this.command = false;
        this.tableName = tableName;
    }

    @Override public TableName getTableName() {
        return tableName;
    }

    @Override public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    /**
     * Specified if the catalog is included.
     *
     * @return boolean
     */
    public boolean isCatalogInc() {
        return catalogInc;
    }

    /**
     * Set if the catalog is included.
     *
     * @param catalogInc
     */
    public void setCatalogInc(boolean catalogInc) {
        this.catalogInc = catalogInc;
    }

    /**
     * Get the catalog of the truncate statement.
     *
     * @return com.stratio.crossdata.common.data.CatalogName
     */
    public CatalogName getCatalog() {
        return catalog;
    }

    /**
     * Set the catalog name of the truncate statement.
     *
     * @param catalog The name.
     */
    public void setCatalog(CatalogName catalog) {
        this.catalog = catalog;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TRUNCATE ");
        if (catalogInc) {
            sb.append(catalog).append(".");
        }
        sb.append(tableName);
        return sb.toString();
    }

    @Override
    public ValidationRequirements getValidationRequirements() {
        return new ValidationRequirements().add(ValidationTypes.MUST_EXIST_TABLE)
                .add(ValidationTypes.MUST_EXIST_CATALOG);
    }

}
