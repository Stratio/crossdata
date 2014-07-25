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

package com.stratio.meta2.metadata;

import java.util.Map;

public class TableMetadata {
  private String name;

  private Map<String, Object> options;

  private Map<String, ColumnMetadata> columns;

  private CatalogMetadata catalog;

  private StorageMetadata storage;

  public TableMetadata() {
  }



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, Object> getOptions() {
    return options;
  }

  public void setOptions(Map<String, Object> options) {
    this.options = options;
  }

  public Map<String, ColumnMetadata> getColumns() {
    return columns;
  }

  public void setColumns(Map<String, ColumnMetadata> columns) {
    this.columns = columns;
  }

  public CatalogMetadata getCatalog() {
    return catalog;
  }

  public void setCatalog(CatalogMetadata catalog) {
    this.catalog = catalog;
  }

  public StorageMetadata getStorage() {
    return storage;
  }

  public void setStorage(StorageMetadata storage) {
    this.storage = storage;
  }
}
