package com.stratio.meta.common.data;

import java.util.ArrayList;
import java.util.List;

public class CassandraResultSet extends ResultSet {

    private List<Row> rows = new ArrayList<>();
    private int pointer = 0;

    public void addRow(Row row){
        rows.add(row);
    }

    public int size(){
        return rows.size();
    }

    public void reset(){
        pointer = 0;
    }

    @Override
    public Row next() {
        return rows.get(pointer++);
    }

    @Override
    public boolean hasNext() {
        return pointer < rows.size();
    }

    @Override
    public void close() {
        return;
    }
}
