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

package com.stratio.meta.core.structures;

/**
 * Table name recognized by the parser. The name may contain:
 * <ul>
 *   <li>catalog.table</li>
 *   <li>table</li>
 * </ul>
 */
public class TableName {

  private String catalog = null;

  private final String tableName;

  public TableName(String tableName){
    this.tableName = tableName;
  }

  public void setCatalog(String catalog){
    this.catalog = catalog;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getTableName() {
    return tableName;
  }

  public boolean containsCatalog(){
    return catalog != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if(catalog != null){
      sb.append(catalog).append(".");
    }
    sb.append(tableName);
    return sb.toString();
  }
}
