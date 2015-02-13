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

import java.util.HashMap;
import java.util.Map;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Catalog metadatada class that implements the characteristics of a catalog.
 */
public class CatalogMetadata implements IMetadata {

    private static final long serialVersionUID = 1366045716958459009L;
    /**
     * The catalog name.
     */
    private final CatalogName name;

    /**
     * The creation options of the catalog.
     */
    private Map<Selector, Selector> options;

    /**
     * The tables contained by the catalog.
     */
    private final Map<TableName, TableMetadata> tables;

    /**
     * Constructor class.
     * @param name The name of the catalog.
     * @param options The creation options.
     * @param tables The tables of the catalog.
     */
    public CatalogMetadata(CatalogName name, Map<Selector, Selector> options,
            Map<TableName, TableMetadata> tables) {
        this.name = name;
        this.options = options;
        if(tables == null){
            this.tables = new HashMap<>();
        } else {
            this.tables = tables;
        }

    }

    public final CatalogName getName() {
        return name;
    }

    public Map<Selector, Selector> getOptions() {
        return options;
    }

    public Map<TableName, TableMetadata> getTables() {
        return tables;
    }

    public void setOptions(Map<Selector, Selector> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(System.getProperty("line.separator"));

        sb.append("Catalog: ").append(getName()).append(System.lineSeparator());

        sb.append("Options: ").append(System.lineSeparator());
        for (Map.Entry<Selector, Selector> entry : getOptions().entrySet()) {
            sb.append("\t").append(entry.getKey()).append(": ").append(entry.getValue())
                    .append(System.lineSeparator());
        }

        sb.append("Tables: ").append(getTables().keySet()).append(System.lineSeparator());

        return sb.toString();
    }
}
