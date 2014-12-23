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

package com.stratio.connector.inmemory.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stratio.crossdata.common.statements.structures.FunctionSelector;

/**
 * This class provides a basic abstraction of a database-like table stored in memory.
 */
public class InMemoryTable {

    /**
     * Table name.
     */
    private final String tableName;

    /**
     * Name of the columns.
     */
    private final String [] columnNames;

    /**
     * Index in where the different columns have been stored in the object array.
     */
    private final Map<String, Integer> columnIndex = new HashMap<>();

    /**
     * Java column types.
     */
    private final Class [] columnTypes;

    /**
     * List of columns in the primary key.
     */
    private final List<String> primaryKey = new ArrayList<>();

    /**
     * Map of rows indexed by primary key.
     */
    private final Map<String, Object[]> rows = new HashMap<>();

    /**
     * Maximum number of rows in the table.
     */
    private final int maxRows;

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param columnNames The name of the columns.
     * @param columnTypes The types of the columns.
     * @param primaryKey The list of columns in the primary key.
     * @param maxRows The maximum number of rows per table.
     */
    public InMemoryTable(String tableName, String[] columnNames, Class[] columnTypes, List<String> primaryKey,
            int maxRows) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.primaryKey.addAll(primaryKey);
        int index = 0;
        for(String col: columnNames){
            columnIndex.put(col, index);
            index++;
        }
        this.maxRows = maxRows;
    }

    /**
     * Get the types of the columns.
     * @return An array of Java classes.
     */
    public Class[] getColumnTypes() {
        return columnTypes;
    }

    /**
     * Get the column names.
     * @return An array of column names.
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
     * Insert a new row in the table.
     * @param row The map associating column name with cell value.
     */
    public void insert(Map<String, Object> row) throws Exception {
        checkTableSpace();
        Object [] rowObjects = new Object[columnNames.length];
        for(Map.Entry<String, Object> cols : row.entrySet()){
            rowObjects[columnIndex.get(cols.getKey())] = cols.getValue();
        }
        String key = generatePrimaryKey(row);
        rows.put(key, rowObjects);
    }

    /**
     * Check that the table size have not reached the maximum.
     * @throws Exception If the maximum capacity have been reached.
     */
    private void checkTableSpace() throws Exception {
        if(rows.size() >= maxRows){
            throw new Exception("Table maximum capacity reached: " + maxRows);
        }
    }

    /**
     * Generate the primary key for a given row.
     * @param row The map associating column name with cell value.
     * @return A String representation of the key.
     * @throws Exception If the row does not contains all required values.
     */
    private String generatePrimaryKey(Map<String, Object> row) throws Exception {
        StringBuffer sb = new StringBuffer();
        for(String keyColumn: primaryKey){
            if(!row.containsKey(keyColumn)){
                throw new Exception("Key column " + keyColumn + " not found in the row to be inserted.");
            }else{
                sb.append(row.get(keyColumn)).append("$");
            }
        }
        return sb.toString();
    }

    /**
     * Truncate the contents of a table.
     */
    public void truncate() {
        rows.clear();
    }

    /**
     * Perform a full scan search.
     * @param relations The list of relationships.
     * @param outputColumns The output columns in order.
     * @return A list with the matching columns.
     */
    public List<Object[]> fullScanSearch(
            List<InMemoryRelation> relations,
            List<FunctionSelector> functions,
            List<String> outputColumns){
        List<Object[]> results = new ArrayList<>();
        boolean toAdd = true;
        for(Object [] row : rows.values()){
            toAdd = true;
            for(InMemoryRelation relation : relations){
                Object o = row[columnIndex.get(relation.getColumnName())];
                toAdd &= relation.getRelation().compare(o, relation.getRightPart());
            }
            if((functions!= null) && (!functions.isEmpty())){
                row = checkFunction(row, functions, outputColumns);
            }

            if(toAdd){
                results.add(projectColumns(row, outputColumns));
            }
        }
        return results;
    }

    private Object[] checkFunction(Object[] row, List<FunctionSelector> functions, List<String> outputColumns) {
        return row;
    }

    /**
     * Project a set of columns given a complete row.
     * @param row The source row.
     * @param outputColumns The set of output columns.
     * @return A row with the projected columns.
     */
    private Object[] projectColumns(final Object [] row, final List<String> outputColumns){
        Object [] result = new Object[outputColumns.size()];
        int index = 0;
        for(String output : outputColumns){
            result[index] = row[columnIndex.get(output)];
            index++;
        }
        return result;
    }

    /**
     * Get the size of the table in number of rows.
     * @return The number of rows.
     */
    public int size(){
        return rows.size();
    }
}
