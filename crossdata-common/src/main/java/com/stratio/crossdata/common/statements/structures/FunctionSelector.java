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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.stratio.crossdata.common.data.TableName;

/**
 * Selector composed by a includes and the list of columns required by the includes.
 */
public class FunctionSelector extends Selector {

    private static final long serialVersionUID = -713392181217425684L;
    /**
     * Name of the includes.
     */
    private final String functionName;

    /**
     * List of columns.
     */
    private final List<Selector> functionColumns;

    /**
     * Class constructor.
     *
     * @param functionName Name of the includes.
     * @param functionColumns A list of selectors with the columns affected.
     */
    public FunctionSelector(String functionName, List<Selector> functionColumns) {
        this(null, functionName, functionColumns);
    }

    /**
     * Class constructor.
     *
     * @param tableName The table name.
     * @param functionName Name of the includes.
     * @param functionColumns A list of selectors with the columns affected.
     */
    public FunctionSelector(TableName tableName, String functionName, List<Selector> functionColumns) {
        super(tableName);
        this.functionName = functionName;
        this.alias = functionName;
        this.functionColumns = functionColumns;
    }

    /**
     * Get the function name.
     * @return A string.
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Get the list of columns required by the includes.
     *
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.Selector}.
     */
    public List<Selector> getFunctionColumns() {
        return functionColumns;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.FUNCTION;
    }

    @Override
    public LinkedHashSet<TableName> getSelectorTables() {
        LinkedHashSet<TableName> result = new LinkedHashSet<>();
        for (Selector s: this.functionColumns) {
            result.addAll(s.getSelectorTables());
        }
        return result;
    }

    /**
     * Get the table name.
     * @return A {@link com.stratio.crossdata.common.data.TableName} .
     */
    public TableName getTableName() {
        return tableName;
    }

    /**
     * toString without alias for Insert statements.
     * @return A String.
     */
    public String toStringWithoutAlias() {
        StringBuilder sb = new StringBuilder(functionName);
        sb.append("(");

        boolean first=true;
        for (Selector selector:functionColumns) {
            if(!first){
                sb.append(", ");
            }
            if(FunctionSelector.class.isInstance(selector)){
                sb.append(((FunctionSelector)selector).toStringWithoutAlias());
            }else {
                sb.append(selector.toString());
            }
            first=false;

        }
        sb.append(")");

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(functionName);
        sb.append("(");
        Iterator<Selector> selectors = functionColumns.iterator();
        while (selectors.hasNext()) {
            sb.append(selectors.next().toString());
            if (selectors.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
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

        FunctionSelector that = (FunctionSelector) o;

        if (functionColumns != null ? !functionColumns.equals(that.functionColumns) : that.functionColumns != null) {
            return false;
        }
        if (functionName != null ? !functionName.equals(that.functionName) : that.functionName != null) {
            return false;
        }
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = functionName != null ? functionName.hashCode() : 0;
        result = 31 * result + (functionColumns != null ? functionColumns.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        return result;
    }

}
