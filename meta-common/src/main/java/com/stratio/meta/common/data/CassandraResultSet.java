package com.stratio.meta.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CassandraResultSet extends ResultSet implements Serializable {

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
