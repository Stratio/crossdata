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

package com.stratio.meta.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stratio.meta.common.metadata.structures.ColumnMetadata;

public class ResultSet implements Iterable<Row>, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -1239158068912896730L;
    /**
     * List of {@link com.stratio.meta.common.data.Row}.
     */
    private List<Row> rows;
    /**
     * List of {@link com.stratio.meta.common.metadata.structures.ColumnMetadata}.
     */
    private List<ColumnMetadata> columnMetadata;

    /**
     * CassandraResultSet default constructor.
     */
    public ResultSet() {
        rows = new ArrayList<>();
        columnMetadata = new ArrayList<>();
    }

    /**
     * Check if ResultSet is empty.
     *
     * @return {@code true} if is empty
     */
    public boolean isEmpty() {
        return size() < 1;
    }

    /**
     * Get the rows of the Result Set.
     *
     * @return A List of {@link com.stratio.meta.common.data.Row}
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * Set the list of rows.
     *
     * @param rows The list.
     */
    public void setRows(List<Row> rows) {
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
     * @param columnMetadata A list of {@link com.stratio.meta.common.metadata.structures.ColumnMetadata}
     *                       in order.
     */
    public void setColumnMetadata(List<ColumnMetadata> columnMetadata) {
        this.columnMetadata = columnMetadata;
    }

    /**
     * Add a row to the Result Set.
     *
     * @param row {@link com.stratio.meta.common.data.Row} to add
     */
    public void add(Row row) {
        rows.add(row);
    }

    /**
     * Remove a row.
     *
     * @param index INDEX of the row to remove
     */
    public void remove(int index) {
        rows.remove(index);
    }

    /**
     * Get the size of ResultSet.
     *
     * @return the size of ResultSet
     */
    public int size() {
        return rows.size();
    }

    /**
     * Get a Iterator over the ResultSet.
     *
     * @return Iterator
     */
    public Iterator<Row> iterator() {
        return new ResultSetIterator(this);
    }

}
