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

package com.stratio.meta.rest.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.stratio.meta.common.data.Cell;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;

public class JsonMetaResultSet implements Serializable {

    /**
     * Serial version UID in order to be Serializable.
     */
    private static final long serialVersionUID = -5989403621101496698L;

    /**
     * List of {@link com.stratio.meta.common.data.Row}.
     */
    private List<JsonRow> rows;

    /**
     * List of {@link ColumnMetadata}.
     */
    private List<ColumnMetadata> columnMetadata;

    /**
     * MetaResultSet default constructor.
     */
    public JsonMetaResultSet() {
        rows = new ArrayList<>();
        columnMetadata = new ArrayList<>();
    }

    public JsonMetaResultSet(String uniqueCell) {
        rows = new ArrayList<>();
        rows.add(new JsonRow("Result", new Cell(uniqueCell)));
        columnMetadata = new ArrayList<>();
        columnMetadata.add(new ColumnMetadata(new ColumnName("Result", "Result", "Result"),
                new String[] { "Result" }, ColumnType.VARCHAR));
    }

    public String getRows() {
        String result = "[";
        for (JsonRow r : rows) {
            String rowResult = "{";
            for (Entry<String, Cell> c : r.getCells().entrySet()) {
                String value = "";
                value += c.getValue().getValue().toString();
                rowResult += "'" + c.getKey() + "'" + ": '" + value + "', ";
            }
            rowResult = rowResult.substring(0, rowResult.length() - 2); // removes last comma
            rowResult += "}";

            result += rowResult + " , ";
        }
        result = result.substring(0, result.length() - 2); // removes last comma
        result += "]";
        return result.trim();
    }

    /**
     * Set the list of rows.
     *
     * @param rows The list.
     */
    public void setRows(List<JsonRow> rows) {
        this.rows = rows;
    }

    /**
     * Get the column metadata in order.
     *
     * @return A list of {@link com.stratio.meta.common.metadata.structures.ColumnMetadata}.
     */
    public List<ColumnMetadata> getColumnMetadata() {
        return columnMetadata;
    }

    /**
     * Set the list of column metadata.
     *
     * @param columnMetadata A list of
     *                       {@link com.stratio.meta.common.metadata.structures.ColumnMetadata} in order.
     */
    public void setColumnMetadata(List<ColumnMetadata> columnMetadata) {
        this.columnMetadata = columnMetadata;
    }

    /**
     * Add a row to the Result Set.
     *
     * @param row {@link com.stratio.meta.common.data.Row} to add
     */
    public void add(JsonRow row) {
        rows.add(row);
    }

    /**
     * Remove a row.
     *
     * @param index Index of the row to remove
     */
    public void remove(int index) {
        rows.remove(index);
    }

    /**
     * Get the size of the Result Set.
     *
     * @return Size.
     */
    public int size() {
        return rows.size();
    }

}
