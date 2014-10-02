/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.common.logicalplan;

import java.util.Iterator;
import java.util.Map;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.metadata.ColumnType;

/**
 * Select operator that specifies the list of columns that should be
 * returned with their expected name. Notice that this operator is applied
 * to limit the number of columns returned and to provide alias support.
 */
public class Select extends TransformationStep {

    /**
     * Map of columns associating the name given in the Project logical steps
     * with the name expected in the results.
     */
    private final Map<ColumnName, String> columnMap;

    /**
     * Map of {@link com.stratio.meta2.common.metadata.ColumnType} associated with each column.
     */
    private final Map<String, ColumnType> typeMap;

    /**
     * Class constructor.
     *
     * @param operation The operation to be applied.
     * @param columnMap Map of columns associating the name given in the Project
     *                  logical steps with the name expected in the result.
     * @param typeMap   The mapping of column types.
     */
    public Select(Operations operation, Map<ColumnName, String> columnMap, Map<String, ColumnType> typeMap) {
        super(operation);
        this.columnMap = columnMap;
        this.typeMap = typeMap;
    }

    public Map<ColumnName, String> getColumnMap() {
        return columnMap;
    }

    public Map<String, ColumnType> getTypeMap() {
        return typeMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SELECT (");
        Iterator<Map.Entry<ColumnName, String>> it = columnMap.entrySet().iterator();
        Map.Entry<ColumnName, String> entry = null;
        while (it.hasNext()) {
            entry = it.next();
            sb.append(entry.getKey()).append(" AS ").append(entry.getValue());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
