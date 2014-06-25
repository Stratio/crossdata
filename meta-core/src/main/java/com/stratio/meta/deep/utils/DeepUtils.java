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

package com.stratio.meta.deep.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;

import com.stratio.deep.entity.Cells;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.core.metadata.AbstractMetadataHelper;
import com.stratio.meta.core.metadata.CassandraMetadataHelper;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.GroupByFunction;
import com.stratio.meta.core.structures.Selection;
import com.stratio.meta.core.structures.SelectionList;
import com.stratio.meta.core.structures.SelectionSelectors;
import com.stratio.meta.core.structures.SelectorGroupBy;
import com.stratio.meta.core.structures.SelectorIdentifier;
import com.stratio.meta.core.structures.SelectorMeta;

public final class DeepUtils {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(DeepUtils.class);

  /**
   * Private class constructor as all methods are static.
   */
  private DeepUtils() {

  }

  /**
   * Build ResultSet from list of Cells.
   * 
   * @param cells list of Cells
   * @param selectedCols List of fields selected in the SelectStatement.
   * @return ResultSet
   */
  public static ResultSet buildResultSet(List<Cells> cells, List<String> selectedCols) {
    CassandraResultSet rs = new CassandraResultSet();
    // CellValidator
    AbstractMetadataHelper helper = new CassandraMetadataHelper();

    if (cells.size() > 0) {
      List<com.stratio.meta.common.metadata.structures.ColumnMetadata> columnList =
          new ArrayList<>();
      com.stratio.meta.common.metadata.structures.ColumnMetadata columnMetadata = null;
      // Obtain the metadata associated with the columns.
      for (com.stratio.deep.entity.Cell def : cells.get(0).getCells()) {
        columnMetadata =
            new com.stratio.meta.common.metadata.structures.ColumnMetadata("deep",
                def.getCellName());
        ColumnType type = helper.toColumnType(def);
        columnMetadata.setType(type);
        columnList.add(columnMetadata);
      }
      rs.setColumnMetadata(columnList);
    }

    if (!cells.isEmpty()) {
      if (selectedCols.isEmpty()) {
        for (Cells deepRow : cells) {
          Row metaRow = new Row();
          for (com.stratio.deep.entity.Cell deepCell : deepRow.getCells()) {

            if (deepCell.getCellName().toLowerCase().startsWith("stratio")) {
              continue;
            }

            Cell metaCell = new Cell(deepCell.getCellValue());
            metaRow.addCell(deepCell.getCellName(), metaCell);
          }

          rs.add(metaRow);
        }
      } else {
        List<Integer> fieldPositions = retrieveFieldsPositionsList(cells.get(0), selectedCols);

        for (Cells deepRow : cells) {
          Row metaRow = new Row();
          for (int fieldPosition : fieldPositions) {
            com.stratio.deep.entity.Cell deepCell = deepRow.getCellByIdx(fieldPosition);

            Cell metaCell = new Cell(deepCell.getCellValue());
            metaRow.addCell(deepCell.getCellName(), metaCell);
          }
          rs.add(metaRow);
        }
      }
    }

    StringBuilder logResult = new StringBuilder("Deep Result: ").append(rs.size());
    if (!rs.isEmpty()) {
      logResult.append(" rows & ").append(rs.iterator().next().size()).append(" columns");
    }
    LOG.info(logResult);

    if (LOG.isDebugEnabled()) {
      printDeepResult(rs.getRows());
    }

    return rs;
  }

  private static List<Integer> retrieveFieldsPositionsList(Cells firstRow, List<String> selectedCols) {

    List<Integer> fieldPositions = new ArrayList<>();
    for (String selectCol : selectedCols) {
      Integer position = 0;
      boolean fieldFound = false;

      Iterator<com.stratio.deep.entity.Cell> cellsIt = firstRow.getCells().iterator();
      while (!fieldFound && cellsIt.hasNext()) {
        com.stratio.deep.entity.Cell cell = cellsIt.next();

        if (cell.getCellName().equalsIgnoreCase(selectCol)) {
          fieldPositions.add(position);
          fieldFound = true;
        }

        position++;
      }
    }

    return fieldPositions;
  }

  /**
   * Create a result with a count.
   * 
   * @param rdd rdd to be counted
   * @return ResultSet Result set with only a cell containing the a number of rows
   */
  public static ResultSet buildCountResult(JavaRDD<?> rdd) {
    CassandraResultSet rs = new CassandraResultSet();

    int numberOfRows = (int) rdd.count();

    Row metaRow = new Row();

    Cell metaCell = new Cell(numberOfRows);

    List<ColumnMetadata> columns = new ArrayList<>();
    ColumnMetadata metadata = new ColumnMetadata("count", "COUNT");
    ColumnType type = ColumnType.INT;
    type.setDBMapping("int", Integer.class);
    metadata.setType(type);
    rs.setColumnMetadata(columns);

    metaRow.addCell("COUNT", metaCell);
    rs.add(metaRow);

    return rs;
  }

  /**
   * Print a List of {@link com.stratio.meta.common.data.Row}.
   * 
   * @param rows List of Rows
   */
  protected static void printDeepResult(List<Row> rows) {
    StringBuilder sb = new StringBuilder(System.lineSeparator());
    boolean firstRow = true;
    for (Row row : rows) {
      if (firstRow) {
        for (String colName : row.getCells().keySet()) {
          sb.append(colName).append(" | ");
        }
        sb.append(System.lineSeparator());
        sb.append("---------------------------------------------------------------------");
        sb.append(System.lineSeparator());
      }
      firstRow = false;
      for (Map.Entry<String, Cell> entry : row.getCells().entrySet()) {
        sb.append(String.valueOf(entry.getValue())).append(" - ");
      }
      sb.append(System.lineSeparator());
    }
    sb.append(System.lineSeparator());
    LOG.debug(sb.toString());
  }

  /**
   * Retrieve fields in selection clause.
   * 
   * @param ss SelectStatement of the query
   * @return Array of fields in selection clause or null if all fields has been selected
   */
  public static String[] retrieveSelectorFields(SelectStatement ss) {
    // Retrieve selected column names
    SelectionList sList = (SelectionList) ss.getSelectionClause();
    Selection selection = sList.getSelection();
    List<String> columnsSet = new ArrayList<>();
    if (selection instanceof SelectionSelectors) {
      SelectionSelectors sSelectors = (SelectionSelectors) selection;
      for (int i = 0; i < sSelectors.getSelectors().size(); ++i) {
        SelectorMeta selectorMeta = sSelectors.getSelectors().get(i).getSelector();
        if (selectorMeta instanceof SelectorIdentifier) {
          SelectorIdentifier selId = (SelectorIdentifier) selectorMeta;
          columnsSet.add(selId.getField());
        } else if (selectorMeta instanceof SelectorGroupBy) {
          SelectorGroupBy selectorGroupBy = (SelectorGroupBy) selectorMeta;
          if (selectorGroupBy.getGbFunction() != GroupByFunction.COUNT) {
            SelectorIdentifier selId = (SelectorIdentifier) selectorGroupBy.getParam();
            columnsSet.add(selId.getField());
          }
        }
      }
    }
    return columnsSet.toArray(new String[columnsSet.size()]);
  }

  /**
   * Retrieve fields in selection clause.
   * 
   * @param ss SelectStatement of the query
   * @return Array of fields in selection clause or null if all fields has been selected
   */
  public static List<String> retrieveSelectors(Selection selection) {

    // Retrieve aggretation function column names
    List<String> columnsSet = new ArrayList<>();
    if (selection instanceof SelectionSelectors) {
      SelectionSelectors sSelectors = (SelectionSelectors) selection;
      for (int i = 0; i < sSelectors.getSelectors().size(); ++i) {
        SelectorMeta selectorMeta = sSelectors.getSelectors().get(i).getSelector();
        if (selectorMeta instanceof SelectorIdentifier) {
          SelectorIdentifier selId = (SelectorIdentifier) selectorMeta;
          columnsSet.add(selId.getField());
        } else if (selectorMeta instanceof SelectorGroupBy) {
          SelectorGroupBy selGroup = (SelectorGroupBy) selectorMeta;
          columnsSet.add(selGroup.getGbFunction().name() + "("
              + ((SelectorIdentifier) selGroup.getParam()).getField() + ")");
        }
      }
    }
    return columnsSet;
  }

  /**
   * Retrieve fields in selection clause.
   * 
   * @param ss SelectStatement of the query
   * @return Array of fields in selection clause or null if all fields has been selected
   */
  public static List<String> retrieveSelectorAggegationFunctions(Selection selection) {

    // Retrieve aggretation function column names
    List<String> columnsSet = new ArrayList<>();
    if (selection instanceof SelectionSelectors) {
      SelectionSelectors sSelectors = (SelectionSelectors) selection;
      for (int i = 0; i < sSelectors.getSelectors().size(); ++i) {
        SelectorMeta selectorMeta = sSelectors.getSelectors().get(i).getSelector();
        if (selectorMeta instanceof SelectorGroupBy) {
          SelectorGroupBy selGroup = (SelectorGroupBy) selectorMeta;
          columnsSet.add(selGroup.getGbFunction().name() + "("
              + ((SelectorIdentifier) selGroup.getParam()).getField() + ")");
        }
      }
    }
    return columnsSet;
  }
}
