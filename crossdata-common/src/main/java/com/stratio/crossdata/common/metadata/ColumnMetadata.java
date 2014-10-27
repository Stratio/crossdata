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

public class ColumnMetadata implements IMetadata {
    private final ColumnName name;
    private final Object[] parameters;
    private final ColumnType columnType;

//TODO:javadoc
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

//TODO:javadoc
    public ColumnName getName() {
        return name;
    }

//TODO:javadoc
    public Object[] getParameters() { return parameters.clone(); }

//TODO:javadoc
    public ColumnType getColumnType() {
        return columnType;
    }

//TODO:javadoc
    public String getIndex() {
        throw new UnsupportedOperationException();
    }
}
