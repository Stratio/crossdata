/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.common.data;

import com.stratio.meta.common.metadata.structures.ColumnMetadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CassandraResultSet extends ResultSet implements Serializable {

    /**
     * Serial version UID in order to be Serializable.
     */
    private static final long serialVersionUID = -5989403621101496698L;

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
    public CassandraResultSet() {
        rows = new ArrayList<>();
    }

    /**
     * Set the list of rows.
     * @param rows The list.
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
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
     * Set the list of column metadata.
     * @param columnMetadata A list of {@link com.stratio.meta.common.metadata.structures.ColumnMetadata} in order.
     */
    public void setColumnMetadata(List<ColumnMetadata> columnMetadata) {
        this.columnMetadata = columnMetadata;
    }

    /**
     * Get the column metadata in order.
     * @return A list of {@link com.stratio.meta.common.metadata.structures.ColumnMetadata}.
     */
    public List<ColumnMetadata> getColumnMetadata(){
        return columnMetadata;
    }

    /**
     * Add a row to the Result Set.
     *
     * @param row {@link com.stratio.meta.common.data.Row} to add
     */
    public void add(Row row){
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
    public int size(){
        return rows.size();
    }

    @Override
    public Iterator<Row> iterator() {
        return new CResultSetIterator(this);
    }

}
