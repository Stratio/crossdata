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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

/**
 * Table tests.
 */
public class InMemoryTableTest {

    /**
     * Number of rows to be inserted in the tests.
     */
    public final static int INSERT_TEST_SIZE = 10;

    /**
     * Maximum number of rows in a table.
     */
    public final static int TABLE_MAX_ROWS = 100;

    /**
     * Create a test table.
     *
     * @param tableName   The table name.
     * @param columnNames The column names.
     * @param columnTypes The types of columns.
     * @param primaryKey  The list of columns in the primary key.
     * @return An {@link com.stratio.connector.inmemory.datastore.InMemoryTable}.
     */
    public InMemoryTable createTestTable(
            String tableName,
            String[] columnNames,
            Class[] columnTypes,
            List<String> primaryKey) {
        InMemoryTable table = new InMemoryTable(tableName, columnNames, columnTypes, primaryKey, TABLE_MAX_ROWS);
        assertNotNull(table, "Cannot create table");
        assertEquals(table.getColumnNames(), columnNames, "Invalid column names");
        assertEquals(table.getColumnTypes(), columnTypes, "Invalid column types");
        assertEquals(table.size(), 0, "Invalid size");
        return table;
    }

    @Test
    public void createTable() {
        String tableName = "testTable";
        String[] columnNames = new String[] { "col1", "col2" };
        Class[] columnTypes = new Class[] { String.class, Integer.class };
        List<String> primaryKey = Arrays.asList("col1");
        InMemoryTable table = new InMemoryTable(tableName, columnNames, columnTypes, primaryKey, TABLE_MAX_ROWS);
        assertNotNull(table, "Cannot create table");
        assertEquals(table.getColumnNames(), columnNames, "Invalid column names");
        assertEquals(table.getColumnTypes(), columnTypes, "Invalid column types");
        assertEquals(table.size(), 0, "Invalid size");
    }

    @Test
    public void insert() {
        String tableName = "testTable";
        String[] columnNames = new String[] { "col1", "col2" };
        Class[] columnTypes = new Class[] { String.class, Integer.class };
        List<String> primaryKey = Arrays.asList("col1");
        InMemoryTable table = createTestTable(tableName, columnNames, columnTypes, primaryKey);
        try {
            Map<String, Object> row = new HashMap<>();
            for (int index = 0; index < INSERT_TEST_SIZE; index++) {
                row = new HashMap<>();
                row.put(columnNames[0], columnNames[0] + index);
                row.put(columnNames[1], index);
                table.insert(row);
            }
        } catch (Exception e) {
            fail("Insert should work", e);
        }
        assertEquals(table.size(), INSERT_TEST_SIZE, "Invalid size");
    }

    @Test
    public void basicSelect(){
        String tableName = "testTable";
        String[] columnNames = new String[] { "string_col", "integer_col", "boolean_col" };
        Class[] columnTypes = new Class[] { String.class, Integer.class, Boolean.class };
        List<String> primaryKey = Arrays.asList("string_col");
        InMemoryTable table = createTestTable(tableName, columnNames, columnTypes, primaryKey);
        try {
            Map<String, Object> row = new HashMap<>();
            for (int index = 0; index < INSERT_TEST_SIZE; index++) {
                row = new HashMap<>();
                row.put(columnNames[0], columnNames[0] + index);
                row.put(columnNames[1], index);
                row.put(columnNames[2], index % 2 == 0);
                table.insert(row);
            }
        } catch (Exception e) {
            fail("Insert should work", e);
        }
        assertEquals(table.size(), INSERT_TEST_SIZE, "Invalid size");

        int indexToFind = (INSERT_TEST_SIZE-1);
        InMemoryRelation relation = new InMemoryRelation(columnNames[0], InMemoryOperations.EQ,
                columnNames[0]+indexToFind);
        List<Object[]> result = table.fullScanSearch(Arrays.asList(relation), primaryKey);
        assertEquals(result.size(), 1, "Invalid size");
        Object[] row = result.get(0);
        assertEquals(row.length, 1, "Invalid number of columns returned.");
        assertEquals(row[0], columnNames[0]+indexToFind, "Invalid row retrieved.");

    }

}
