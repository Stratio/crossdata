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

import com.stratio.connector.inmemory.datastore.datatypes.JoinValue;
import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.structures.InMemoryFunctionSelector;
import org.apache.log4j.Logger;

import com.stratio.connector.inmemory.datastore.structures.AbstractInMemoryFunction;


/**
 * This class provides a proof-of-concept implementation of an in-memory datastore for
 * demonstration purposes.
 */
public class InMemoryDatastore {

    /**
     * Maximum number of rows per table.
     */
    private final int tableRowLimit;

    /**
     * Map of catalogs in the datastore.
     */
    private final Map<String, InMemoryCatalog> catalogs = new HashMap<>();

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(InMemoryDatastore.class);

    /**
     * Class constructor.
     * @param tableRowLimit The maximum number of rows per table.
     */
    public InMemoryDatastore(int tableRowLimit){
        this.tableRowLimit = tableRowLimit;
        LOG.info("InMemoryDatastore created with row limit: " + this.tableRowLimit);
    }

    public Map<String, InMemoryCatalog> getCatalogs() {
        return catalogs;
    }

    /**
     * Create a catalog in the in memory datastore.
     * @param name The name of the catalog.
     * @return The resulting {@link com.stratio.connector.inmemory.datastore.InMemoryCatalog}.
     */
    public InMemoryCatalog createCatalog(String name){
        InMemoryCatalog catalog = new InMemoryCatalog(name);
        catalogs.put(name, catalog);
        return catalog;
    }

    /**
     * Check that the catalog exists on the system.
     * @param catalogName The name of the catalog.
     * @throws Exception If the catalog is not found.
     */
    private void catalogShouldExist(String catalogName) throws Exception{
        if(!catalogs.containsKey(catalogName)){
            throw new Exception("Catalog " + catalogName + " does not exist");
        }
    }

    /**
     * Create a new table.
     * @param catalogName The name of the catalog.
     * @param tableName The name of the table.
     * @param columnNames The name of the columns.
     * @param columnTypes The types of the table columns.
     * @param primaryKey The list of column names that belong to the primary key.
     * @throws Exception If the table cannot be created.
     */
    public void createTable(String catalogName, String tableName, String[] columnNames, Class[] columnTypes,
            List<String> primaryKey) throws Exception{
        catalogShouldExist(catalogName);
        catalogs.get(catalogName).createTable(tableName, columnNames, columnTypes, primaryKey, tableRowLimit);
    }

    /**
     * Drop an existing catalog.
     * @param catalogName The name of the catalog.
     * @throws Exception If the catalog does not exist or it still have tables in it.
     */
    public void dropCatalog(String catalogName) throws Exception{
        catalogShouldExist(catalogName);
        if(catalogs.get(catalogName).getNumberTables() > 0){
            throw new Exception("Cannot delete non-empty catalogs.");
        }
    }

    /**
     * Drop an existing table.
     * @param catalogName The name of the catalog.
     * @param tableName The name of the table.
     * @throws Exception If the table does not exist.
     */
    public void dropTable(String catalogName, String tableName) throws Exception{
        catalogShouldExist(catalogName);
        catalogs.get(catalogName).dropTable(tableName);
    }

    /**
     * Insert a new row in a table.
     * @param catalogName The name of the catalog.
     * @param tableName The name of the table.
     * @param toAdd The row to be added.
     * @throws Exception If the insertion fails.
     */
    public void insert(String catalogName, String tableName, Map<String, Object> toAdd) throws Exception{
        catalogShouldExist(catalogName);
        catalogs.get(catalogName).insert(tableName, toAdd);
    }

    /**
     * Truncate a table.
     * @param catalogName The name of the catalog.
     * @param tableName The name of the table.
     * @throws Exception If the table cannot be truncated.
     */
    public void truncateTable(String catalogName, String tableName) throws Exception{
        catalogShouldExist(catalogName);
        catalogs.get(catalogName).truncate(tableName);
    }

    /**
     * Search the elements of a table.
     * @param catalogName The name of the catalog.
     * @param inMemoryQuery The query to be executed.
     * @return A list of rows.
     * @throws Exception If search cannot be performed.
     */
    public List<SimpleValue[]> search(String catalogName, InMemoryQuery inMemoryQuery) throws Exception {
        catalogShouldExist(catalogName);
        List<SimpleValue[]> result;

        result = catalogs.get(catalogName).search(inMemoryQuery.getTableName(),inMemoryQuery.getRelations(), inMemoryQuery.getOutputColumns());

        //Execute the required aggregation functions.

        for(int index = 0; index < inMemoryQuery.getOutputColumns().size(); index++){
            if(InMemoryFunctionSelector.class.isInstance(inMemoryQuery.getOutputColumns().get(index))){
                AbstractInMemoryFunction f = InMemoryFunctionSelector.class
                        .cast(inMemoryQuery.getOutputColumns().get(index))
                        .getFunction();
                if(!f.isRowFunction()){
                    InMemoryTable table = catalogs.get(catalogName).getTable(inMemoryQuery.getTableName());
                    result = f.apply(table.getColumnIndex(), result);
                }
            }
        }

        return result;
    }


    public List<SimpleValue[]> joinResults(List<List<SimpleValue[]>> joinTables) {

        List<SimpleValue[]> finalResult = new ArrayList<SimpleValue[]>();
        if (joinTables.size() > 1) {
            for (SimpleValue[] row : joinTables.get(0)){
                SimpleValue[] joinedRow = joinRow(row, joinTables.subList(1, joinTables.size()));
                if (joinedRow != null) {
                    finalResult.add(joinedRow);
                }
            }

        }else if (joinTables.size()>0){
            for (SimpleValue[] row : joinTables.get(0)) {
                finalResult.add(getRowValues(row).toArray(new SimpleValue[]{}));
            }
        }

        return finalResult;
    }

    private List<SimpleValue> getRowValues(SimpleValue[] row) {
        List<SimpleValue> finalRow = new ArrayList();
        for(SimpleValue field:row){
            if (!(field instanceof JoinValue)){
                finalRow.add(field);
            }
        }
        return finalRow;
    }

    private SimpleValue[] joinRow(SimpleValue[] mainRow,  List<List<SimpleValue[]>> otherTables){
        List<SimpleValue> finalJoinRow = new ArrayList<>();

        //Search JoinRows
        for(SimpleValue field:mainRow){
            if(field instanceof JoinValue){
                List<SimpleValue> joinResult = calculeJoin((JoinValue) field, otherTables);
                if (joinResult != null && joinResult.size()>0){
                    finalJoinRow.addAll(joinResult);
                }else{
                    return null;
                }
            }else{
                finalJoinRow.add(field);
            }
        }

        return finalJoinRow.toArray(new SimpleValue[]{});
    }

    private List<SimpleValue> calculeJoin(JoinValue join, List<List<SimpleValue[]>> otherTables){
        List<SimpleValue> joinResult = new ArrayList<>();
        boolean matched = false;

        for(List<SimpleValue[]> table: otherTables){
            rows: for(SimpleValue[] row: table){
                for(SimpleValue field:row){
                    if (field instanceof JoinValue){
                        JoinValue tableJoin = (JoinValue) field;
                        if (join.joinMatch(tableJoin)){
                            matched = true;
                            joinResult.addAll(getRowValues(row));
                            continue rows;
                        }
                    }
                }
            }
        }

        if (matched){
            return joinResult;
        }else{
            return null;
        }
    }

    /**
     * Determine if a given catalog exists.
     * @param catalogName The name of the catalog.
     * @return Whether it exists.
     */
    public boolean existsCatalog(String catalogName) {
        return catalogs.containsKey(catalogName);
    }
}
