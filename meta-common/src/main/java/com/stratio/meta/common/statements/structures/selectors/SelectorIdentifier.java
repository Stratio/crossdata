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

package com.stratio.meta.common.statements.structures.selectors;

import java.io.Serializable;

import com.stratio.meta2.common.data.TableName;

public class SelectorIdentifier extends SelectorMeta implements Serializable {

    private static final long serialVersionUID = -8632253820536763413L;

    private TableName table;

    private String field;

    public SelectorIdentifier(String columnName) {

        this.type = TYPE_IDENT;

        if (columnName.contains(".")) {
            String[] idParts = columnName.split("\\.");
            this.table = new TableName("", idParts[0]);
            this.field = idParts[1];
        } else {
            this.field = columnName;
        }
    }

    public SelectorIdentifier(TableName tableName, String fieldName) {
        this.type = TYPE_IDENT;
        this.table = tableName;
        this.field = fieldName;
    }

    public TableName getTable() {
        return table;
    }

    public void setTable(TableName table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isColumnSelector() {
        return field.contains(".");
    }

    @Override
    public String toString() {

        return (this.table == null || "*".equals(field)) ? this.field : this.table + "." + this.field;
    }

    @Override
    public void addTablename(TableName tablename) {
        if (this.table == null) {
            this.table = tablename;
        }
    }

    /**
     * Set field and tables fields through the given identifier
     *
     * @param identifier COLUMN identifier. It must be composed by a table, a dot ('.') and a field,
     *                   or just a field.
     */
    public void setIdentifier(String identifier) {

        if (identifier.contains(".")) {
            String[] idParts = identifier.split("\\.");
            this.table = new TableName("", idParts[0]);
            this.field = idParts[1];
        } else {
            this.field = identifier;
        }
    }

}
