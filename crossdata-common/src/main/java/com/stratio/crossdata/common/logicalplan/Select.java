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

package com.stratio.crossdata.common.logicalplan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Select operator that specifies the list of columns that should be returned with their expected name. Notice that this
 * operator is applied to limit the number of columns returned and to provide alias support.
 */
public class Select extends TransformationStep {

    /**
     * Map of columns associating the name given in the Project logical steps with the name expected in the results.
     */
    private final Map<Selector, String> columnMap;

    /**
     * Map of {@link com.stratio.crossdata.common.metadata.ColumnType} associated with each column.
     */
    private Map<String, ColumnType> typeMap;

    /**
     * Map with the types of the selectors.
     */
    private Map<Selector, ColumnType> typeMapFromColumnName;

    /**
     * Class constructor.
     * 
     * @param operation
     *            The operation to be applied.
     * @param columnMap
     *            Map of columns associating the name given in the Project logical steps with the name expected in the
     *            result.
     * @param typeMap
     *            The mapping of column types.
     * @param typeMapFromColumnName  The types of selectors.
     */
    public Select(Operations operation, Map<Selector, String> columnMap, Map<String, ColumnType> typeMap,
            Map<Selector, ColumnType> typeMapFromColumnName) {
        super(operation);
        this.columnMap = columnMap;
        this.typeMap = typeMap;
        this.typeMapFromColumnName = typeMapFromColumnName;
    }

    public Map<Selector, String> getColumnMap() {
        return columnMap;
    }

    public Map<String, ColumnType> getTypeMap() {
        return typeMap;
    }

    public Map<Selector, ColumnType> getTypeMapFromColumnName() {
        return typeMapFromColumnName;
    }

    public void setTypeMap(Map<String, ColumnType> typeMap) {
        this.typeMap = typeMap;
    }

    public void setTypeMapFromColumnName(Map<Selector, ColumnType> typeMapFromColumnName) {
        this.typeMapFromColumnName = typeMapFromColumnName;
    }

    /**
     * Get the selectors that must be returned as a result of executing the query
     * in the expected order.
     * @return A list of {@link com.stratio.crossdata.common.statements.structures.Selector}.
     */
    public List<Selector> getOutputSelectorOrder(){
        List<Selector> result = new ArrayList<>();
        for(Selector selector : columnMap.keySet()){
            result.add(selector);
        }
        return result;
    }

    /**
     * @deprecated
     * Get the columns in the expected order. With the introduction of function support
     * this method is marked as Deprecated in favour of #getOutputSelectorOrder().
     * @return A list of {@link com.stratio.crossdata.common.data.ColumnName}.
     */
    @Deprecated
    public List<ColumnName> getColumnOrder(){
        List<ColumnName> results = new ArrayList<>();
        for(Selector selector: columnMap.keySet()){
            results.add(selector.getColumnName());
        }
        return results;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SELECT (");
        Iterator<Map.Entry<Selector, String>> it = columnMap.entrySet().iterator();
        Map.Entry<Selector, String> entry;
        while (it.hasNext()) {
            entry = it.next();
            sb.append(entry.getKey().getColumnName().getQualifiedName()).append(" AS ").append(entry.getValue());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
