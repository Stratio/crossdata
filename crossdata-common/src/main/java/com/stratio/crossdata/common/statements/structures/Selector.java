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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;

/**
 * This class represents one of the elements requested in a SELECT statement.
 */
public abstract class Selector implements Serializable, ISqlExpression {

    /**
     * Serial version UID in order to be serializable.
     */
    private static final long serialVersionUID = -2200687442507159727L;

    /**
     * The alias to be applied to the current Selector.
     */
    protected String alias = null;

    /**
     * The associated {@link com.stratio.crossdata.common.data.TableName}.
     */
    protected TableName tableName;

    protected boolean parenthesis = false;

    /**
     * Class constructor.
     *
     * @param tableName The associated {@link com.stratio.crossdata.common.data.TableName}.
     */
    protected Selector(TableName tableName) {
        this.tableName = tableName;
    }

    /**
     * Get the column alias.
     *
     * @return The alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Get the associated table name.
     *
     * @return A {@link com.stratio.crossdata.common.data.TableName}.
     */
    public TableName getTableName() {
        return tableName;
    }

    /**
     * Set the associated table name.
     *
     * @param tableName A {@link com.stratio.crossdata.common.data.TableName}.
     */
    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    /**
     * Set the alias for this selector.
     *
     * @param alias The alias.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Get the selector type.
     *
     * @return A {@link SelectorType}.
     */
    public abstract SelectorType getType();

    /**
     * Get the tables associated with the current selector.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.TableName}.
     */
    public Set<TableName> getSelectorTables() {
        return new HashSet<>();
    }

    /**
     * Get a string representation of the tables associated with the selector.
     *
     * @return A string with the table qualified names separated by -.
     */
    public String getSelectorTablesAsString() {
        StringBuilder sb = new StringBuilder();
        Iterator<TableName> it = getSelectorTables().iterator();
        while (it.hasNext()) {
            TableName t=it.next();
            if (t==null){
                continue;
            }
            sb.append(t.getQualifiedName());
            if (it.hasNext()) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    /**
     * Get the string value of the current selector.
     *
     * @return A string.
     */
    public String getStringValue() {
        return toString();
    }

    /**
     * Get the column name associated with the selector.
     *
     * @return A {@link com.stratio.crossdata.common.data.ColumnName}.
     */
    public ColumnName getColumnName() {
        return new ColumnName(tableName, alias);
    }

    public boolean isParenthesis() {
        return parenthesis;
    }

    public void setParenthesis(boolean parenthesis) {
        this.parenthesis = parenthesis;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();


}
