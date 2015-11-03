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

package com.stratio.crossdata.testsAT.specs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;

import cucumber.api.DataTable;

public class ContainsRowsMatcher extends BaseMatcher<ResultSet> {

    private final DataTable table;
    private ResultSet result_obtained;
    private ResultSet expected_res;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainsRowsMatcher.class);

    public ContainsRowsMatcher(DataTable table) {
        this.table = table;
    }

    @Factory
    public static ContainsRowsMatcher ContainsRows(DataTable table) {
        return new ContainsRowsMatcher(table);
    }

    @Override
    public void describeTo(Description description) {
        // description.appendText("The data of the table is not equals to the ResultSet.\nThe result expected is:\n")
        // .appendText(stringResult(expected_res)).appendText("\nAnd the result obtained is:\n").appendText(stringResult(result_obtained));
        description.appendText(stringResult(expected_res));
    }

    public void describeMismatch(final Object item, final Description description) {
        // description.appendText("was").appendValue(((Foo) item).getNumber());
        description.appendText("The result obtained is:").appendText(stringResult((ResultSet) item));
    }

    @Override
    public boolean matches(Object item) {
        result_obtained = (ResultSet) item;
        LOGGER.info("RESULT OBTAINED : " + stringResult(result_obtained));
        expected_res = dataTableToResultSet(table);
        LOGGER.info("RESULT EXPECTED " + stringResult(expected_res));
        return assertEqualElementsIgnoringOrder(expected_res, result_obtained);
    }

    // @Override
    // public boolean matches(ResultSet item) {
    // result_obtained = item;
    // LOGGER.info("RESULT OBTAINED : " + stringResult(item));
    // expected_res = dataTableToResultSet(table);
    // LOGGER.info("RESULT EXPECTED " + stringResult(expected_res));
    // return assertEqualElementsIgnoringOrder(expected_res, item);
    // }

    private com.stratio.crossdata.common.data.ResultSet dataTableToResultSet(DataTable tab) {
        // Primero construimos las columas
        Map<String[], ColumnDefinition> columnsMap = getColumnDef(tab.raw().get(0));
        ArrayList<com.stratio.crossdata.common.data.Row> rows = getRowsFromDataTable(tab);
        com.stratio.crossdata.common.data.ResultSet expected_res = toMetaResultSet(rows, columnsMap);
        return expected_res;
    }

    private static ArrayList<com.stratio.crossdata.common.data.Row> getRowsFromDataTable(DataTable tab) {
        ArrayList<com.stratio.crossdata.common.data.Row> rows = new ArrayList<com.stratio.crossdata.common.data.Row>();
        List<String> firstRow = tab.raw().get(0);
        com.stratio.crossdata.common.data.Row row;
        for (int i = 1; i < tab.raw().size(); i++) {
            row = createRow(tab.raw().get(i), firstRow);
            // row.setCells(map_cells);
            rows.add(row);
        }
        return rows;
    }

    private static com.stratio.crossdata.common.data.Row createRow(List<String> row_as_list, List<String> firstRow) {
        com.stratio.crossdata.common.data.Row row = new com.stratio.crossdata.common.data.Row();
        String aux = "";
        for (int x = 0; x < row_as_list.size(); x++) {
            String[] col_info = firstRow.get(x).split("-");
            String column_name = col_info[1];
            String column_type = col_info[2];
            if (aux.equals(column_name)) {
                System.out.println("ERROR");
            }
            if (column_type.equals("String")) {
                String cell_string = new String(row_as_list.get(x).toString());
                Cell cell = new Cell(cell_string);
                aux = column_name;
                row.addCell(col_info[1].toString(), cell);
            }
            if (column_type.equals("Integer")) {
                Cell cell_1 = new Cell(Integer.parseInt(row_as_list.get(x)));
                row.addCell(col_info[1], cell_1);
            }
            if (column_type.equals("Boolean")) {
                Cell cell_2 = new Cell((Boolean.valueOf(row_as_list.get(x))));
                row.addCell(col_info[1], cell_2);
            }
            if (column_type.equals("Float")) {
                Cell cell_3 = new Cell(Float.parseFloat(row_as_list.get(x)));
                row.addCell(col_info[1], cell_3);
            }
            if (column_type.equals("Double")) {
                Cell cell_4 = new Cell(Double.parseDouble(row_as_list.get(x)));
                row.addCell(col_info[1], cell_4);
            }
            if (column_type.equals("BigInteger")) {
                Cell cell_5 = new Cell(Long.parseLong(row_as_list.get(x)));
                row.addCell(col_info[1], cell_5);
            }
            if (column_type.equals("Long")) {
                Cell cell_6 = new Cell(Long.parseLong(row_as_list.get(x)));
                row.addCell(col_info[1], cell_6);
            }
            // map_cells.put(column_name, cell);
        }
        return row;

    }

    private Map<String[], ColumnDefinition> getColumnDef(List<String> firstRow) {
        LinkedHashMap<String[], ColumnDefinition> columnsMap = new LinkedHashMap<String[], ColumnDefinition>();
        for (String s : firstRow) {
            String[] s_array = s.split("-");
            switch (s_array[2]) {
            case "String":
                columnsMap.put(s_array, new ColumnDefinition(String.class));
                break;
            case "Integer":
                columnsMap.put(s_array, new ColumnDefinition(Integer.class));
                break;
            case "Boolean":
                columnsMap.put(s_array, new ColumnDefinition(Boolean.class));
                break;
            case "Float":
                columnsMap.put(s_array, new ColumnDefinition(Float.class));
                break;
            case "Double":
                columnsMap.put(s_array, new ColumnDefinition(Double.class));
                break;
            case "BigInteger":
                columnsMap.put(s_array, new ColumnDefinition(Long.class));
                break;
            case "Long":
                columnsMap.put(s_array, new ColumnDefinition(Long.class));
                break;
            default:
                break;
            }
        }
        return columnsMap;

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static com.stratio.crossdata.common.data.ResultSet toMetaResultSet(
            ArrayList<com.stratio.crossdata.common.data.Row> rows, Map<String[], ColumnDefinition> map) {
        ResultSet crs = new ResultSet();
        ArrayList<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        crs.setRows((List) rows);
        for (Entry<String[], ColumnDefinition> data : map.entrySet()) {
            String[] key = data.getKey();
            Class<?> col = data.getValue().getDatatype();
            String[] aux_key_1 = key[0].split("\\.");
            ColumnName columna = new ColumnName(aux_key_1[0], aux_key_1[1], aux_key_1[2]);
            columna.setAlias(key[1]);
            ColumnMetadata aux = new ColumnMetadata(columna, null, getColumnType(col));

            columns.add(aux);
        }
        crs.setColumnMetadata(columns);
        return crs;
    }

    private static ColumnType getColumnType(Class<?> type) {
        ColumnType res = null;
        type.toString();
        switch (type.getSimpleName()) {
        case "String":
            res = new ColumnType(DataType.VARCHAR);
            break;
        case "Integer":
            res = new ColumnType(DataType.INT);
            break;
        case "Boolean":
            res = new ColumnType(DataType.BOOLEAN);
            break;
        case "Float":
            res = new ColumnType(DataType.FLOAT);
            break;
        case "Double":
            res = new ColumnType(DataType.DOUBLE);
            break;
        case "BigInteger":
            res = new ColumnType(DataType.BIGINT);
            break;
        case "Long":
            res = new ColumnType(DataType.FLOAT);
            break;
        }
        return res;

    }


    @SuppressWarnings("unused")
    private static boolean assertEqualsResultSets(com.stratio.crossdata.common.data.ResultSet connector,
            com.stratio.crossdata.common.data.ResultSet meta) {

        ArrayList<com.stratio.crossdata.common.data.Row> connectorRowsList = new ArrayList<com.stratio.crossdata.common.data.Row>();
        ArrayList<com.stratio.crossdata.common.data.Row> metaRowsList = new ArrayList<com.stratio.crossdata.common.data.Row>();

        for (com.stratio.crossdata.common.data.Row r : connector) {
            connectorRowsList.add(r);
        }
        for (com.stratio.crossdata.common.data.Row r : meta) {
            metaRowsList.add(r);
        }

        if (connectorRowsList.size() != metaRowsList.size()) {
            return false;
        }

        for (int i = 0; i < connectorRowsList.size(); i++) {
            if (!assertEqualsRows(connectorRowsList.get(i), metaRowsList.get(i)))
                return false;
        }

        return true;
    }

    private static boolean assertEqualsRows(com.stratio.crossdata.common.data.Row r1,
            com.stratio.crossdata.common.data.Row r2) {

        Collection<Cell> cells_row1 = r1.getCellList();
        Collection<Cell> cells_row2 = r2.getCellList();
        List<Cell> listCells1 = new ArrayList<>(cells_row1);
        List<Cell> listCells2 = new ArrayList<>(cells_row2);
        if (listCells1.size() != listCells2.size()) {
            return false;
        }
        for (int i = 0; i < listCells1.size(); i++) {
            if (!assertEqualsCells(listCells1.get(i), listCells2.get(i))) {
                return false;
            }
        }
        // for (Entry<String, Cell> e : r1.getCells().entrySet()) {
        // Cell c = r2.getCell(e.getKey());
        // if (c == null) {
        // return false;
        // } else {
        // if (!assertEqualsCells(c, e.getValue()))
        // return false;
        // }
        // }
        return true;
    }

    private static boolean assertEqualsRows1(com.stratio.crossdata.common.data.Row r1,
            com.stratio.crossdata.common.data.Row r2) {
        Map<String, Cell> row_1 = r1.getCells();
        Map<String, Cell> row_2 = r2.getCells();
        if (row_1.size() != row_2.size())
            return false;
        for (String key : row_1.keySet()) {
            if (!row_1.get(key).getValue().equals(row_2.get(key).getValue())) {
                return false;
            }
        }
        return true;
    }

    private static boolean assertEqualsCells(Cell c1, Cell c2) {
        // TODO Implementar casteo de celdas

        /*
         * String cell_type = c1.getValue().getClass().getSimpleName(); switch(cell_type){ case "BigInteger": BigInteger
         * bi1 = (BigInteger) c1.getValue(); Long lo1 = bi1.longValue(); Cell c_aux = new Cell(new Long(lo1)); return
         * (c_aux.getValue().equals(c2.getValue())); case }
         */

        return (c1.getValue().equals(c2.getValue()));
    }

    private static boolean assertEqualElementsIgnoringOrder(com.stratio.crossdata.common.data.ResultSet r1,
            com.stratio.crossdata.common.data.ResultSet r2) {
        // recorrer rows r1
        // comprobar por cada row de r1 que hay una igual en el otro result set
        List<ColumnMetadata> columns_r2 = r2.getColumnMetadata();
        List<ColumnMetadata> columns_r1 = r1.getColumnMetadata();
        for (int i = 0; i < columns_r2.size(); i++) {
            try {
                columns_r2.get(i).getColumnType().getODBCType();
            } catch (NullPointerException e) {
                System.out.println("The column " + columns_r2.get(i).getName().getName() + " has no ODBCtype defined.");
                return false;
            }
        }
        if (columns_r1.size() != columns_r2.size()) {
            return false;
        }
        for (int i = 0; i < columns_r1.size(); i++) {
            // Comparar tipos y nombres
            ColumnName col1 = columns_r1.get(i).getName();
            ColumnName col2 = columns_r2.get(i).getName();
            if (!col1.getAlias().equals(col2.getAlias())) {
                return false;
            }
            if (!col1.getTableName().equals(col2.getTableName())) {
                return false;
            }
            if (!col1.getName().equals(col2.getName())) {
                return false;
            }
            if (!col1.getQualifiedName().equals(col2.getQualifiedName())) {
                return false;
            }
            if (!col1.getType().equals(col2.getType())) {
                return false;
            }
        }
//        if (r1.size() != r2.size())
//            return false;

        for (com.stratio.crossdata.common.data.Row r : r2) {
            if (!isRowContained(r, r1))
                return false;
        }

        return true;
    }

    private static boolean isRowContained(com.stratio.crossdata.common.data.Row r,
            com.stratio.crossdata.common.data.ResultSet resultSet) {
        for (com.stratio.crossdata.common.data.Row rSet : resultSet) {
            if (assertEqualsRows1(rSet, r))
                return true;
        }
        return false;
    }

    public static String stringResult(com.stratio.crossdata.common.data.ResultSet metaResultSet) {
        if (metaResultSet.isEmpty() || metaResultSet.getRows().size() == 0) {
            return "OK";
        }

        com.stratio.crossdata.common.data.ResultSet resultSet = metaResultSet;

        Map<String, Integer> colWidths = calculateColWidths(resultSet);

        String bar = StringUtils.repeat('-', getTotalWidth(colWidths) + (colWidths.values().size() * 3) + 1);

        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append(bar).append(System.lineSeparator());
        boolean firstRow = true;
        for (com.stratio.crossdata.common.data.Row row : resultSet) {
            sb.append("| ");

            if (firstRow) {
                // for (String key : row.getCells().keySet()) {
                // sb.append(StringUtils.rightPad(key, colWidths.get(key))).append("| ");
                // }
                for (ColumnMetadata columnMetadata : resultSet.getColumnMetadata()) {
                    sb.append(
                            StringUtils.rightPad(columnMetadata.getName().getColumnNameToShow(),
                                    colWidths.get(columnMetadata.getName().getColumnNameToShow()) + 1)).append("| ");
                }
                sb.append(System.lineSeparator());
                sb.append(bar);
                sb.append(System.lineSeparator());
                sb.append("| ");
                firstRow = false;
            }
            for (Map.Entry<String, Cell> entry : row.getCells().entrySet()) {
                String str = String.valueOf(entry.getValue().getValue());
                sb.append(StringUtils.rightPad(str, colWidths.get(entry.getKey())));
                sb.append(" | ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append(bar).append(System.lineSeparator());
        return sb.toString();
    }

    private static Map<String, Integer> calculateColWidths(com.stratio.crossdata.common.data.ResultSet resultSet) {
        Map<String, Integer> colWidths = new HashMap<>();
        // Get column names
        com.stratio.crossdata.common.data.Row firstRow = resultSet.iterator().next();
        for (String key : firstRow.getCells().keySet()) {
            colWidths.put(key, key.length());
        }
        // Find widest cell content of every column
        for (com.stratio.crossdata.common.data.Row row : resultSet) {
            for (String key : row.getCells().keySet()) {
                String cellContent = String.valueOf(row.getCell(key).getValue());
                int currentWidth = colWidths.get(key);
                if (cellContent.length() > currentWidth) {
                    colWidths.put(key, cellContent.length());
                }
            }
        }
        return colWidths;
    }

    private static int getTotalWidth(Map<String, Integer> colWidths) {
        int totalWidth = 0;
        for (int width : colWidths.values()) {
            totalWidth += width;
        }
        return totalWidth;
    }

}
