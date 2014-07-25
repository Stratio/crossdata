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
