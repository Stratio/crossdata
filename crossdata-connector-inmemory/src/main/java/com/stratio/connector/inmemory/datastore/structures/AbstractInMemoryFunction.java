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

package com.stratio.connector.inmemory.datastore.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.structures.InMemorySelector;

/**
 * Definition of an abstract function for the in-memory datastore.
 */
public class AbstractInMemoryFunction {

    protected boolean rowFunction = true;

    protected final List<InMemorySelector> arguments = new ArrayList<>();

    public void setArguments(List<InMemorySelector> arguments) {
        this.arguments.addAll(arguments);
    }

    /**
     *
     * Applies the function to this row.
     *
     * @param columnIndex a Map with the columns involved in the functions
     * @param row the values.
     * @return the result of the function
     * @throws Exception Is the function does not apply to a single row
     */
    public Object apply(Map<String, Integer> columnIndex, SimpleValue [] row) throws Exception{
        throw new Exception("Function cannot be applied to a row");
    }

    /**
     * * Applies the function to this list of rows.
     *
     * @param columnIndex a Map with the columns involved in the functions
     * @param rows tha list of row with the values.
     * @return the  results of apply the function
     * @throws Exception Is the function does not apply to a list of row
     */
    public List<SimpleValue []> apply(Map<String, Integer> columnIndex, List<SimpleValue []> rows) throws Exception{
        throw new Exception("Function cannot be applied to a list of rows");
    }

    /**
     *
     * @return true is this function applies to a Row, false if applies to many rows.
     */
    public boolean isRowFunction() {
        return rowFunction;
    }
}
