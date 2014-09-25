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

import java.util.ArrayList;

import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;

public class ResultSet {

    private ArrayList<Row> rows;

    private ArrayList<ColumnMetadata> columnMetadata;

    private boolean empty;

    private int size;

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public ArrayList<ColumnMetadata> getColumnMetadata() {
        return columnMetadata;
    }

    public void setColumnMetadata(ArrayList<ColumnMetadata> columnMetadata) {
        this.columnMetadata = columnMetadata;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toStringCustom() {
        return "ResultSet [Rows=" + rows + ", columnMetadata=" + columnMetadata + ", empty=" + empty
                + ", size=" + size + "]";
    }

}
