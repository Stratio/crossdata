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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.statements.structures.FunctionSelector;

/**
 * This class provides a proof-of-concept implementation of an in-memory datastore for
 * demonstration purposes.
 */
public class InMemoryDatastore {

    /**
     * Maximum number of rows per table.
     */
    private final int TABLE_ROW_LIMIT;

    /**
     * Map of catalogs in the datastore.
     */
    private final Map<String, InMemoryCatalog> catalogs = new HashMap<>();

    private final Set<String> simpleFunctions = new HashSet<>();

    private final Set<String> aggregationFunctions = new HashSet<>();

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(InMemoryDatastore.class);

    /**
     * Class constructor.
     * @param tableRowLimit The maximum number of rows per table.
     */
    public InMemoryDatastore(int tableRowLimit){
        simpleFunctions.add("concat");
        aggregationFunctions.add("count");
        TABLE_ROW_LIMIT = tableRowLimit;
        LOG.info("InMemoryDatastore created with row limit: " + TABLE_ROW_LIMIT);
    }

    public Set<String> getSimpleFunctions() {
        return simpleFunctions;
    }

    public void addSimpleFunction(String function){
        simpleFunctions.add(function);
    }

    public Set<String> getAggregationFunctions() {
        return aggregationFunctions;
    }

    public void addAggregationFunction(String function){
        aggregationFunctions.add(function);
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
        catalogs.get(catalogName).createTable(tableName, columnNames, columnTypes, primaryKey, TABLE_ROW_LIMIT);
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
     * @param tableName The name of the table.
     * @param relations The list of {@link InMemoryRelation} to be satisfied.
     * @param functions Functions included in the selectors.
     * @param columnOrder The column order.
     * @return A list of rows.
     * @throws Exception If search cannot be performed.
     */
    public List<Object[]> search(String catalogName, String tableName, List<InMemoryRelation> relations,
            List<FunctionSelector> functions, List<String> columnOrder) throws Exception {
        catalogShouldExist(catalogName);
        List<Object[]> result;

        boolean includesAggr = false;
        FunctionSelector aggrFunction = null;
        for(FunctionSelector fn: functions){
            if(aggregationFunctions.contains(fn.getFunctionName().toLowerCase())){
                includesAggr = true;
                aggrFunction = fn;
                break;
            }
        }

        result = catalogs.get(catalogName).search(tableName, relations, functions, columnOrder);

        if(includesAggr){
            result = executeAggregationFunction(result, aggrFunction);
        }

        return result;
    }

    private List<Object[]> executeAggregationFunction(List<Object[]> rows, FunctionSelector aggrFunction) {
        List<Object[]> result = rows;
        if(aggrFunction.getFunctionName().toLowerCase().equalsIgnoreCase("count")){
            result = new ArrayList<>();
            result.add(new Object[]{rows.size()});
        }
        return result;
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
