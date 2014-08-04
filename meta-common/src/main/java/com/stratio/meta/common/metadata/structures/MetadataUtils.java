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
