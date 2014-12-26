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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stratio.crossdata.common.statements.structures.FunctionSelector;

/**
 * Catalog abstraction containing a set of tables.
 */
public class InMemoryCatalog {

    /**
     * Catalog name.
     */
    private final String name;

    /**
     * Map of tables contained in a catalog.
     */
    private final Map<String, InMemoryTable> tables = new HashMap<>();

    /**
     * Class constructor.
     * @param name Catalog name.
     */
    public InMemoryCatalog(String name){
        this.name = name;
    }

    /**
     * Create a new table.
     * @param tableName The name of the table.
     * @param columnNames The column names.
     * @param columnTypes The column types.
     * @param primaryKey The list of columns in the primary key.
     * @param maxRows The maximum number of rows per table.
     */
    public void createTable(String tableName, String[] columnNames, Class[] columnTypes, List<String> primaryKey,
            int maxRows) {
        InMemoryTable table = new InMemoryTable(tableName, columnNames, columnTypes, primaryKey, maxRows);
        tables.put(tableName, table);
    }

    /**
     * Get the number of tables in the catalog.
     * @return The number of tables.
     */
    public int getNumberTables(){
        return tables.size();
    }

    /**
     * Check that a table exists.
     * @param tableName The name of the table.
     * @throws Exception If the table does not exist.
     */
    private void tableShouldExists(String tableName) throws Exception{
        if(!tables.containsKey(tableName)){
            throw new Exception("Table " + tableName + " does not exist in the selected catalog");
        }
    }

    /**
     * Drop a table from the current catalog.
     * @param tableName The name of the table.
     * @throws Exception If the table does not exist.
     */
    public void dropTable(String tableName) throws Exception {
        tableShouldExists(tableName);
        tables.remove(tableName);
    }

    /**
     * Insert a new row in a table.
     * @param tableName The name of the table.
     * @param row The row to be inserted.
     * @throws Exception If the insertion fails.
     */
    public void insert(String tableName, Map<String, Object> row) throws Exception {
        tableShouldExists(tableName);
        tables.get(tableName).insert(row);
    }

    /**
     * Truncate a table.
     * @param tableName The name of the table.
     * @throws Exception If the table cannot be truncated.
     */
    public void truncate(String tableName) throws Exception{
        tableShouldExists(tableName);
        tables.get(tableName).truncate();
    }

    /**
     * Search the elements of a table.
     * @param tableName The name of the table.
     * @param relations A list of {@link InMemoryRelation} to be satisfied.
     * @param columnOrder The column order.
     * @return A list of rows.
     * @throws Exception If the search cannot be executed.
     */
    public List<Object[]> search(String tableName, List<InMemoryRelation> relations,
            List<FunctionSelector> functions, List<String> columnOrder) throws Exception {
        tableShouldExists(tableName);
        return tables.get(tableName).fullScanSearch(relations, functions, columnOrder);
    }
}
