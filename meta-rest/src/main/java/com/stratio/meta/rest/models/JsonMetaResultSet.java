package com.stratio.meta.rest.models;

/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;

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
   * List of {@link com.stratio.meta.common.metadata.structures.ColumnMetadata}.
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
    columnMetadata.add(new ColumnMetadata("Result", "Result", ColumnType.VARCHAR));
  }

  /**
   * Set the list of rows.
   * 
   * @param rows The list.
   */
  public void setRows(List<JsonRow> rows) {
    this.rows = rows;
  }

  public String getRows() {
    String result = "[";
    for (JsonRow r : rows) {
      String rowResult = "{";
      for (Entry<String, Cell> c : r.getCells().entrySet()) {
        String value = "";
        value += c.getValue().getValue().toString();
        rowResult += c.getKey() + ": '" + value + "', ";
      }
      rowResult = rowResult.substring(0, rowResult.length() - 2); // removes last comma
      rowResult += "}";

      result += rowResult+" , ";
    }
    result = result.substring(0, result.length() - 2); // removes last comma
    result += "]";
    return result.trim();
  }


  /**
   * Set the list of column metadata.
   * 
   * @param columnMetadata A list of
   *        {@link com.stratio.meta.common.metadata.structures.ColumnMetadata} in order.
   */
  public void setColumnMetadata(List<ColumnMetadata> columnMetadata) {
    this.columnMetadata = columnMetadata;
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
