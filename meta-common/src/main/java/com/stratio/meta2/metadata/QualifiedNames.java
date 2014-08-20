/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.metadata;

public class QualifiedNames {
  public static String getCatalogQualifiedName(String name) {
    return normalize("catalog." + name);
  }

  public static String getTableQualifiedName(String catalog, String name) {
    return normalize(getCatalogQualifiedName(catalog) + "." + name);
  }

  public static String getCatalogFromTableQualifiedName(String tableName){
    return tableName.substring(0,tableName.lastIndexOf('.'));
  }

  public static String getColumnQualifiedName(String catalog, String table, String name) {
    return normalize(getTableQualifiedName(catalog, table) + "." + name);
  }

  public static String getClusterQualifiedName(String name) {
    return normalize("cluster." + name);
  }

  public static String getConnectorQualifiedName(String name) {
    return normalize("connector." + name);
  }

  public static String getDataStoreQualifiedName(String name){
    return normalize("datastore." + name);
  }

  private static String normalize(String qName) {
    return qName.toLowerCase();
  }
}
