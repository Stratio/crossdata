/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.common.metadata.structures;

import java.util.HashSet;
import java.util.Set;

public class CatalogMetadata {

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
     * @param catalogName Name of the catalog.
     */
    public CatalogMetadata(String catalogName){
        this.catalogName = catalogName;
    }

    /**
     * Class constructor.
     * @param catalogName Name of the catalog.
     * @param tables Set of tables that belong to the catalog.
     */
    public CatalogMetadata(String catalogName, Set<TableMetadata> tables){
        this(catalogName);
        this.tables.addAll(tables);
    }

    /**
     * Get the name of the catalog.
     * @return The name.
     */
    public String getCatalogName(){
        return catalogName;
    }

    /**
     * Get the set of tables.
     * @return A {@link java.util.Set}.
     */
    public Set<TableMetadata> getTables(){
        return tables;
    }
}
