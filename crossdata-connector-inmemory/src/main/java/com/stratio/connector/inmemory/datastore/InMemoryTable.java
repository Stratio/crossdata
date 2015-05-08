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

import java.util.*;

import com.stratio.connector.inmemory.datastore.datatypes.AbstractInMemoryDataType;
import com.stratio.connector.inmemory.datastore.datatypes.JoinValue;
import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.structures.*;

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
    private final Map<String, Object[]> rows = new LinkedHashMap<>();

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
        this.columnNames = columnNames.clone();
        this.columnTypes = columnTypes.clone();
        this.primaryKey.addAll(primaryKey);
        int index = 0;
        for(String col: columnNames){
            columnIndex.put(col, index);
            index++;
        }
        this.maxRows = maxRows;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * Get the types of the columns.
     * @return An array of Java classes.
     */
    public Class[] getColumnTypes() {
        return columnTypes.clone();
    }

    /**
     * Get the column names.
     * @return An array of column names.
     */
    public String[] getColumnNames() {
        return columnNames.clone();
    }

    /**
     * Get the column mapping indexes.
     * @return A map associating column names with columns indexes.
     */
    public Map<String, Integer> getColumnIndex() {
        return columnIndex;
    }

    /**
     * Insert a new row in the table.
     * @param row The map associating column name with cell value.
     */
    public void insert(Map<String, Object> row) throws Exception {
        checkTableSpace();
        Object [] rowObjects = new Object[columnNames.length];
        for(Map.Entry<String, Object> cols: row.entrySet()){
            rowObjects[columnIndex.get(cols.getKey())] = cols.getValue();
            // Check if it's a native data type
            Class clazz = getColumnTypes()[columnIndex.get(cols.getKey())];
            AbstractInMemoryDataType inMemoryDataType =
                    AbstractInMemoryDataType.castToNativeDataType(clazz.getSimpleName());
            if(inMemoryDataType != null){
                rowObjects[columnIndex.get(cols.getKey())] =
                        inMemoryDataType.convertStringToInMemoryDataType(String.valueOf(cols.getValue()));
            }
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
        StringBuilder sb = new StringBuilder();
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
    public List<SimpleValue[]> fullScanSearch(
            List<InMemoryRelation> relations,
            List<InMemorySelector> outputColumns)
    throws Exception{

        List<SimpleValue[]> results = new ArrayList<>();
        boolean toAdd;
        for(Object [] row : rows.values()){
            toAdd = true;
            for(InMemoryRelation relation : relations){
                Object o = row[columnIndex.get(relation.getColumnName())];
                toAdd &= relation.getRelation().compare(o, relation.getRightPart());
            }

            if(toAdd){
                results.add(projectColumns(row, outputColumns));
            }
        }
        return results;
    }



    /**
     * Project a set of columns given a complete row.
     * @param row The source row.
     * @param outputColumns The set of output columns.
     * @return A row with the projected columns.
     */
    private SimpleValue[] projectColumns(final Object[] row, final List<InMemorySelector> outputColumns) throws Exception{
        SimpleValue [] result = new SimpleValue[outputColumns.size()];
        int index = 0;
        for(InMemorySelector selector : outputColumns){
            if(InMemoryFunctionSelector.class.isInstance(selector)){
               //Process function.
                AbstractInMemoryFunction f = InMemoryFunctionSelector.class.cast(selector).getFunction();
                if(f.isRowFunction()) {
                    result[index] = new SimpleValue(selector, f.apply(columnIndex, convertToSimpleValueRow(selector, row)));
                }
            }else if(InMemoryColumnSelector.class.isInstance(selector)){
                Integer pos = columnIndex.get(selector.getName());
                result[index] = new SimpleValue(selector,row[pos]);
            }else if(InMemoryLiteralSelector.class.isInstance(selector)){
                result[index] = new SimpleValue(selector,selector.getName());
            }else if (InMemoryJoinSelector.class.isInstance(selector)){
                InMemoryJoinSelector join = InMemoryJoinSelector.class.cast(selector);
                String column = join.getMyTerm().getColumnName().getName();
                Integer pos = columnIndex.get(column);
                result[index] = new JoinValue(join, row[pos]);
            } else{
                throw new Exception("Cannot recognize selector class " + selector.getClass());
            }
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


    private SimpleValue[] convertToSimpleValueRow(InMemorySelector selector, Object[] row){
        SimpleValue[] result = new SimpleValue[row.length];

        int i = 0;
        for (Object field:row){
            result[i]=new SimpleValue(selector, field);
        }
        return result;
    }
}
