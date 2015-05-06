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

import com.stratio.connector.inmemory.datastore.selector.InMemorySelector;

import java.util.ArrayList;
import java.util.List;

public class InMemoryQuery {

    private String tableName;
    private List<InMemoryRelation> relations = new ArrayList<>();
    private List<InMemorySelector> outputColumns = new ArrayList<>();
    private String catalogName;

    public InMemoryQuery(String tableName, List<InMemoryRelation> relations, List<InMemorySelector> outputColumns) {
        this.tableName = tableName;
        this.relations = relations;
        this.outputColumns = outputColumns;
    }

    public InMemoryQuery() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<InMemoryRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<InMemoryRelation> relations) {
        this.relations = relations;
    }

    public List<InMemorySelector> getOutputColumns() {
        return outputColumns;
    }

    public void setOutputColumns(List<InMemorySelector> outputColumns) {
        this.outputColumns = outputColumns;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

}
