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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CassandraResultSet extends ResultSet implements Serializable {

    private static final long serialVersionUID = 6673808320950075999L;

    private List<Row> rows;
    private Map<String, ColumnDefinition> columnDefinitions;

    /**
     * CassandraResultSet default constructor.
     */
    public CassandraResultSet() {
        rows = new ArrayList<>();
    }

    /**
     * CassandraResultSet param constructor.
     *
     * @param rows List of {@link com.stratio.meta.common.data.Row}
     */
    public CassandraResultSet(List<Row> rows) {
        this.rows = rows;
    }

    public CassandraResultSet(Map<String, ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public CassandraResultSet(List<Row> rows, Map<String, ColumnDefinition> columnDefinitions) {
        this.rows = rows;
        this.columnDefinitions = columnDefinitions;
    }

    /**
     * Get the rows of the Result Set.
     *
     * @return A List of {@link com.stratio.meta.common.data.Row}
     */
    public List<Row> getRows() {
        return rows;
    }

    public Map<String, ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(Map<String, ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
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
