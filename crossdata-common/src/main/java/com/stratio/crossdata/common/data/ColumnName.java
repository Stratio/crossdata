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

package com.stratio.crossdata.common.data;

public class ColumnName extends Name {

    /**
     * Serial version UID in order to be Serializable.
     */
    private static final long serialVersionUID = -659653868246944302L;

    /**
     * Name of the column.
     */
    private final String name;

    private TableName tableName;
    private String alias;

    /**
     * Default constructor.
     *
     * @param catalogName Name of the catalog.
     * @param tableName   Name of the table.
     * @param columnName  Name of the column.
     */
    public ColumnName(String catalogName, String tableName, String columnName) {
        if (tableName != null && !tableName.isEmpty()) {
            this.tableName = new TableName(catalogName, tableName);
        } else {
            this.tableName = null;
        }
        if(columnName != null && !columnName.isEmpty()){
            this.name = columnName.toLowerCase();
        } else {
            this.name = null;
        }
    }

    /**
     * Constructor using existing TableName.
     *
     * @param tableName  TableName.
     * @param columnName Name of the column.
     */
    public ColumnName(TableName tableName, String columnName) {
        if (tableName != null) {
            this.tableName = tableName;
        } else {
            this.tableName = null;
        }
        if(columnName != null && !columnName.isEmpty()){
            this.name = columnName.toLowerCase();
        } else {
            this.name = null;
        }
    }

    public TableName getTableName() {
        return tableName;
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    public String getName() {
        return name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean isCompletedName() {
        return tableName != null && tableName.isCompletedName();
    }

    public String getQualifiedName() {
        String result;
        if (isCompletedName()) {
            result = QualifiedNames.getColumnQualifiedName(this.getTableName().getCatalogName().getName(),
                    getTableName().getName(), getName());
        } else {
            String catalogName = UNKNOWN_NAME;
            String newTableName = UNKNOWN_NAME;
            if (this.getTableName() != null) {
                newTableName = this.getTableName().getName();
                if (this.getTableName().getCatalogName() != null) {
                    catalogName = this.getTableName().getCatalogName().getName();
                }
            }

            result = QualifiedNames.getColumnQualifiedName(catalogName, newTableName, getName());
        }
        return result;
    }

    @Override public NameType getType() {
        return NameType.COLUMN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ColumnName that = (ColumnName) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        return result;
    }

    public String getColumnNameToShow() {
        return (alias==null)? name: alias;
    }

    public String getAlias() {
        return alias;
    }
}
