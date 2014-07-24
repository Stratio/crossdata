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

package com.stratio.meta2.metadata;

/**
 * Types of columns supported by META with their equivalence in ODBC data types. Notice that a
 * NATIVE type has been added to map those types that are not generic and database dependant.
 */
public enum ColumnType {
  BIGINT("SQL_BIGINT"), BOOLEAN("BOOLEAN"), DOUBLE("SQL_DOUBLE"), FLOAT("SQL_FLOAT"), INT(
      "SQL_INTEGER"), TEXT("SQL_VARCHAR"), VARCHAR("SQL_VARCHAR"), NATIVE(null);

  private final String standardType;


  ColumnType(String standardType) {
    this.standardType = standardType;
  }

  public String getStandardType() {
    return standardType;
  }
}
