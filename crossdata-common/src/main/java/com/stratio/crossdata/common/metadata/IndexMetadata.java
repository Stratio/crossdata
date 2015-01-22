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

import java.io.Serializable;
import java.util.Map;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Index Metadata class.
 */
public class IndexMetadata implements Serializable {

    private static final long serialVersionUID = -3324957892171791688L;
    private final IndexName name;
    private final Map<ColumnName, ColumnMetadata> columns;
    private final IndexType type;
    private final Map<Selector, Selector> options;

    /**
     * Constructor class.
     * @param name The name of the index.
     * @param columns The column to be indexed.
     * @param type The type of the index.
     * @param options The options of the creation of the index.
     */
    public IndexMetadata(IndexName name, Map<ColumnName, ColumnMetadata> columns, IndexType type,
            Map<Selector, Selector> options) {
        this.name = name;
        this.columns = columns;
        this.type = type;
        this.options = options;
    }

    public IndexName getName() {
        return name;
    }

    public Map<ColumnName, ColumnMetadata> getColumns() {
        return columns;
    }

    public IndexType getType() {
        return type;
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }

}
