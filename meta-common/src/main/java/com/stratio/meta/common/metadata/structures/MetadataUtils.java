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

public class MetadataUtils {

  public static Class<?> getJavaClass(String storage, ColumnType columnType){
    Class<?> clazz = null;
    if(columnType == ColumnType.BIGINT){
      return Long.class;
    } else if (columnType == ColumnType.BOOLEAN) {
      return Boolean.class;
    } else if (columnType == ColumnType.DOUBLE) {
      return Double.class;
    } else if (columnType == ColumnType.FLOAT) {
      return Float.class;
    } else if (columnType == ColumnType.INT) {
      return Integer.class;
    } else if (columnType == ColumnType.TEXT) {
      return String.class;
    } else if (columnType == ColumnType.VARCHAR) {
      return String.class;
    }
    return clazz;
  }

  public static void updateType(ColumnType type) {
    type.setDBMapping(type.getDbType(), getJavaClass("", type));
  }

  public static void updateType(ColumnMetadata cm) {
    updateType(cm.getType());
  }
}
