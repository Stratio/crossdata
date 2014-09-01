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

package com.stratio.meta.common.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Row implements Serializable {

    private static final long serialVersionUID = 2053597366590152927L;
    private Map<String, Cell> cells;

    /**
     * Row default constructor.
     */
    public Row() {
        cells = new LinkedHashMap<>();
    }

    /**
     * Row params constructor with a cell.
     *
     * @param key Key of the cell
     * @param cell A {@link com.stratio.meta.common.data.Cell}
     */
    public Row(String key, Cell cell) {
        this();
        addCell(key, cell);
    }

    /**
     * Get the size of row.
     *
     * @return the size requested
     */
    public int size(){
        return cells.size();
    }

    /**
     * Get the cells of current row.
     *
     * @return A map which contains the cells
     */
    public Map<String, Cell> getCells() {
        return cells;
    }

    /**
     * Get the collection of cells.
     * @return A collection of Cells.
     */
    public Collection<Cell> getCellList(){
        return cells.values();
    }

    /**
     * Set the cells of the row.
     *
     * @param cells A map of cells
     */
    public void setCells(Map<String, Cell> cells) {
        this.cells = cells;
    }

    /**
     * Add a Cell to the row.
     *
     * @param key Key
     * @param cell Cell
     */
    public void addCell(String key, Cell cell){
        cells.put(key, cell);
        cells.values();
    }

    /**
     * Get a cell by key.
     *
     * @param key Key of the cell
     * @return Cell requested.
     */
    public Cell getCell(String key){
        return cells.get(key);
    }

}
