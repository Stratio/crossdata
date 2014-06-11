/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.structures;

import java.io.Serializable;

public class SelectorIdentifier extends SelectorMeta implements Serializable {

  private static final long serialVersionUID = -8632253820536763413L;

  private String table;

  private String field;

  public SelectorIdentifier(String identifier) {

    this.type = TYPE_IDENT;

    if (identifier.contains(".")) {
      String[] idParts = identifier.split("\\.");
      this.table = idParts[0];
      this.field = idParts[1];
    } else {
      this.field = identifier;
    }
  }

  public SelectorIdentifier(String tableName, String fieldName) {

    this.type = TYPE_IDENT;
    this.table = tableName;
    this.field = fieldName;
  }

  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public boolean isColumnSelector() {
    return field.contains(".");
  }

  @Override
  public String toString() {

    return (this.table == null || field.equals("*")) ? this.field : this.table + "." + this.field;
  }

  @Override
  public void addTablename(String tablename) {

    if (this.table == null)
      this.table = tablename;
  }

}
