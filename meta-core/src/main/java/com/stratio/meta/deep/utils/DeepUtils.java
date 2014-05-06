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

package com.stratio.meta.deep.utils;

import com.stratio.deep.entity.Cells;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.*;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;

import java.util.List;
import java.util.Map;

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
        for (Cells deepRow : cells) {
            Row metaRow = new Row();
            for (com.stratio.deep.entity.Cell deepCell : deepRow.getCells()) {
                if (deepCell.getCellName().toLowerCase().startsWith("stratio")) {
                    continue;
                }
                if (selectedCols.isEmpty()) {
                    Cell metaCell = new Cell(deepCell.getValueType(), deepCell.getCellValue());
                    metaRow.addCell(deepCell.getCellName(), metaCell);
                } else if (selectedCols.contains(deepCell.getCellName())) {
                    Cell metaCell = new Cell(deepCell.getValueType(), deepCell.getCellValue());
                    metaRow.addCell(deepCell.getCellName(), metaCell);
                }
            }
            rs.add(metaRow);
        }

        StringBuilder logResult = new StringBuilder("Deep Result: ").append(rs.size());
        if (!rs.isEmpty()) {
            logResult.append(" rows & ").append(rs.iterator().next().size()).append(" columns");
        }
        LOG.info(logResult);

        if(LOG.isDebugEnabled()){
            printDeepResult(rs.getRows());
        }

        return rs;
    }

    /**
     * Create a result with a count.
     *
     * @param rdd rdd to be counted
     * @return ResultSet Result set with only a cell containing the a number of rows
     */
    public static ResultSet buildCountResult(JavaRDD rdd) {
        CassandraResultSet rs = new CassandraResultSet();

        int numberOfRows = (int) rdd.count();

        Row metaRow = new Row();

        Cell metaCell = new Cell(Integer.class, numberOfRows);
        metaRow.addCell("COUNT", metaCell);
        rs.add(metaRow);

        return rs;
    }

    /**
     * Print a List of {@link com.stratio.meta.common.data.Row}.
     *
     * @param rows List of Rows
     */
    protected static void printDeepResult(List<Row> rows){
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
            for(Map.Entry<String, Cell> entry : row.getCells().entrySet()){
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
    public static String[] retrieveSelectorFields(SelectStatement ss){
        //Retrieve selected column names
        SelectionList sList = (SelectionList) ss.getSelectionClause();
        Selection selection = sList.getSelection();
        String [] columnsSet = {};
        if(selection instanceof SelectionSelectors){
            SelectionSelectors sSelectors = (SelectionSelectors) selection;
            columnsSet = new String[sSelectors.getSelectors().size()];
            for(int i=0;i<sSelectors.getSelectors().size();++i){
                SelectionSelector sSel = sSelectors.getSelectors().get(i);
                SelectorIdentifier selId = (SelectorIdentifier) sSel.getSelector();
                columnsSet[i] = selId.getColumnName();
            }
        }
        return columnsSet;
    }

}
