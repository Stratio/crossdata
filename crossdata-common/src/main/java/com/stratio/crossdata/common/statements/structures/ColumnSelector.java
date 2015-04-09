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

package com.stratio.crossdata.common.statements.structures;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;

/**
 * Single column selector.
 */
public class ColumnSelector extends Selector {

    private static final long serialVersionUID = 1912743509341730253L;
    /**
     * Name of the selected column.
     */
    private ColumnName name;

    /**
     * Class constructor.
     *
     * @param name The column name.
     */
    public ColumnSelector(ColumnName name) {
        super(name.getTableName());
        this.name = name;
    }

    /**
     * Get the column name associated with this selector.
     *
     * @return A {@link com.stratio.crossdata.common.data.ColumnName}.
     */
    public ColumnName getName() {
        return name;
    }

    /**
     * Set the column name.
     * @param name A {@link com.stratio.crossdata.common.data.ColumnName}.
     */
    public void setName(ColumnName name) {
        this.name = name;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.COLUMN;
    }

    @Override
    public ColumnName getColumnName() {
        return name;
    }

    @Override
    public Set<TableName> getSelectorTables() {
        return new HashSet(Arrays.asList(this.name.getTableName()));
    }

    /**
     * Get the associated table name.
     *
     * @return A {@link com.stratio.crossdata.common.data.TableName}.
     */
    @Override
    public TableName getTableName() {
        if((tableName == null) && (name != null)){
            return name.getTableName();
        }
        return new TableName("<UNKNOWN_NAME>", "<UNKNOWN_NAME>");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name.toString());
        if (this.alias != null) {
            sb.append(" AS ").append(alias);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ColumnSelector that = (ColumnSelector) o;

        if (!name.equals(that.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (alias != null) {
            result = alias.hashCode();
        }
        result = 31 * result + name.hashCode();
        return result;
    }
}
