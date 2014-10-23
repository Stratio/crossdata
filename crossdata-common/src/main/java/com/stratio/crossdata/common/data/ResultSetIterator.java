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

package com.stratio.crossdata.common.data;

import java.util.Iterator;

public class ResultSetIterator implements Iterator<com.stratio.crossdata.common.data.Row> {

    /**
     * Set representing a result.
     */
    private final ResultSet resultSet;

    /**
     * Pointer to the current element.
     */
    private int current;

    /**
     * Build a {@link ResultSetIterator} from a {@link com.stratio.crossdata.common.data.ResultSet}.
     *
     * @param resultSet Result Set.
     */
    public ResultSetIterator(ResultSet resultSet) {
        this.resultSet = resultSet;
        this.current = 0;
    }

    @Override
    public boolean hasNext() {
        return current < resultSet.getRows().size();
    }

    @Override
    public com.stratio.crossdata.common.data.Row next() {
        return resultSet.getRows().get(current++);
    }

    @Override
    public void remove() {
        resultSet.remove(current);
    }
}
