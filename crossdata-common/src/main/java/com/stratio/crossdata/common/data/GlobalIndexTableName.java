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

/**
 * Created by lcisneros on 14/07/15.
 */
public class GlobalIndexTableName extends TableName {

    private TableName mainTable;
    /**
     * Constructor class.
     *
     * @param catalogName The catalog name.
     * @param tableName   The table name.
     */
    public GlobalIndexTableName(TableName mainTable, String indexTableName) {
        super(mainTable.getCatalogName().getName(), indexTableName);
        this.mainTable = mainTable;
    }

    public TableName getMainTable() {
        return mainTable;
    }

    public void setMainTable(TableName mainTable) {
        this.mainTable = mainTable;
    }
}
