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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class DeepUtils {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(DeepUtils.class);

    private DeepUtils() {}

    /**
     * Build ResultSet from list of Cells.
     * @param cells list of Cells
     * @return ResultSet
     */
    public static ResultSet buildResultSet(List<Cells> cells, List<String> selectedCols) {
        CassandraResultSet rs = new CassandraResultSet();
        for(Cells deepRow: cells){
            Row metaRow = new Row();
            for(com.stratio.deep.entity.Cell deepCell: deepRow.getCells()){
                if(deepCell.getCellName().toLowerCase().startsWith("stratio")){
                    continue;
                }
                if(selectedCols.isEmpty()){
                    Cell metaCell = new Cell(deepCell.getValueType(), deepCell.getCellValue());
                    metaRow.addCell(deepCell.getCellName(), metaCell);
                } else if(selectedCols.contains(deepCell.getCellName())){
                    Cell metaCell = new Cell(deepCell.getValueType(), deepCell.getCellValue());
                    metaRow.addCell(deepCell.getCellName(), metaCell);
                }
            }

            rs.add(metaRow);
        }

        boolean firstLine = true;
        for(Row metaRows: rs.getRows()){
            Map<String, Cell> mapOfCells = metaRows.getCells();
            if(firstLine){
                System.out.println("Header: "+Arrays.toString(mapOfCells.keySet().toArray()));
                firstLine = false;
            }
            for(String key: mapOfCells.keySet()){
                System.out.print(mapOfCells.get(key).getValue().toString()+" - ");
            }
            System.out.println();
        }

        StringBuilder logResult = new StringBuilder().append("Deep Result: " + rs.size());
        if(rs.size()>0){
            logResult.append(" rows & " + rs.iterator().next().size() + " columns");
        }
        LOG.info(logResult);
        return rs;
    }

    /**
     * Retrieve fields in selection clause.
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
