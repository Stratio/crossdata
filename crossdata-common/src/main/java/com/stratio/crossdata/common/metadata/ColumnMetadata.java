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

package com.stratio.crossdata.common.metadata;

import com.stratio.crossdata.common.data.ColumnName;

/**
 * ColumnMetadata class.
 */
public class ColumnMetadata implements IMetadata {

    private static final long serialVersionUID = 4648404086101059817L;
    private final ColumnName name;
    private final Object[] parameters;
    private final ColumnType columnType;

    /**
     * Class Constructor.
     *
     * @param name The name of the column.
     * @param parameters The parameters of the column.
     * @param columnType The type of the column.
     */
    public ColumnMetadata(ColumnName name, Object[] parameters,
            ColumnType columnType) {
        this.name = name;
        if(parameters != null){
            this.parameters = parameters.clone();
        } else {
            this.parameters = null;
        }
        this.columnType = columnType;
    }

    /**
     * getter for the name.
     *
     * @return Columname
     */
    public ColumnName getName() {
        return name;
    }

    /**
     * returns copy of the internal parameters attribute.
     * @return Object[]
     */
    public Object[] getParameters() { return parameters.clone(); }

    /**
     * Get the Column Type.
     * @return column type
     */
    public ColumnType getColumnType() {
        return columnType;
    }

    /**
     * Get the index of the column.
     * @return String
     */
    public String getIndex() {
        throw new UnsupportedOperationException();
    }
}
