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

public class CassandraResultSet extends ResultSet implements Serializable {

    private static final long serialVersionUID = 6673808320950075999L;

    private List<Row> rows;

    public CassandraResultSet() {
        rows = new ArrayList<>();
    }

    public CassandraResultSet(List<Row> rows) {
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void add(Row row){
        rows.add(row);
    }

    public void remove(int current) {
        rows.remove(current);
    }

    public int size(){
        return rows.size();
    }

    @Override
    public Iterator<Row> iterator() {
        return new CResultSetIterator(this);
    }

}
