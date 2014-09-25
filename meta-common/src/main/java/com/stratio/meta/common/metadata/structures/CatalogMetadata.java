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

package com.stratio.meta.common.metadata.structures;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CatalogMetadata implements Serializable {

    /**
     * Serial version UID in order to be Serializable.
     */
    private static final long serialVersionUID = -5970771778306568496L;

    /**
     * Name of the catalog.
     */
    private final String catalogName;

    /**
     * Set of tables that belong to the catalog.
     */
    private Set<TableMetadata> tables = new HashSet<>();

    /**
     * Class constructor.
     *
     * @param catalogName Name of the catalog.
     */
    public CatalogMetadata(String catalogName) {
        this.catalogName = catalogName;
    }

    /**
     * Class constructor.
     *
     * @param catalogName Name of the catalog.
     * @param tables      Set of tables that belong to the catalog.
     */
    public CatalogMetadata(String catalogName, Set<TableMetadata> tables) {
        this(catalogName);
        this.tables.addAll(tables);
    }

    /**
     * Get the name of the catalog.
     *
     * @return The name.
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * Get the set of tables.
     *
     * @return A {@link java.util.Set}.
     */
    public Set<TableMetadata> getTables() {
        return tables;
    }
}
