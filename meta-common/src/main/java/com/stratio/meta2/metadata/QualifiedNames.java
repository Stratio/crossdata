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
  static String getCatalogQualifiedName(String name) {
    return normalize("catalog." + name);
  }

  public static CatalogName getCatalogNameFromQualifiedName(String qualifiedName){
    CatalogName catalogName =null;
    String [] arrNames= qualifiedName.split("\\.");
    if(arrNames.length == 2){
      catalogName=new CatalogName(arrNames[1]);
    }
    return catalogName;
  }


  static String getTableQualifiedName(String catalog, String name) {
    return normalize(getCatalogQualifiedName(catalog) + "." + name);
  }

  public static TableName getTableNameFromQualifiedName(String qualifiedName){
    TableName tableName =null;
    String [] arrNames= qualifiedName.split("\\.");
    if(arrNames.length == 3){
      tableName=new TableName(arrNames[1],arrNames[2]);
    }
    return tableName;
  }


  static String getColumnQualifiedName(String catalog, String table, String name) {
    return normalize(getTableQualifiedName(catalog, table) + "." + name);
  }

  public static ColumnName getColumnNameFromQualifiedName(String qualifiedName){
    ColumnName columnName =null;
    String [] arrNames= qualifiedName.split("\\.");
    if(arrNames.length == 4){
      columnName=new ColumnName(arrNames[1],arrNames[2],arrNames[3]);
    }
    return columnName;
  }

  static String getClusterQualifiedName(String name) {
    return normalize("cluster." + name);
  }

  public static ClusterName getClusterNameFromQualifiedName(String qualifiedName){
    ClusterName clusterName =null;
    String [] arrNames= qualifiedName.split("\\.");
    if(arrNames.length == 2){
      clusterName=new ClusterName(arrNames[1]);
    }
    return clusterName;
  }

  static String getConnectorQualifiedName(String name) {
    return normalize("connector." + name);
  }

  public static ConnectorName getConnectorNameFromQualifiedName(String qualifiedName){
    ConnectorName connectorName =null;
    String [] arrNames= qualifiedName.split("\\.");
    if(arrNames.length == 2){
      connectorName=new ConnectorName(arrNames[1]);
    }
    return connectorName;
  }

  public static String getDataStoreQualifiedName(String name){
    return normalize("datastore." + name);
  }

  public static DataStoreName getDataStoreNameFromQualifiedName(String qualifiedName){
    DataStoreName dataStoreName =null;
    String [] arrNames= qualifiedName.split("\\.");
    if(arrNames.length == 2){
      dataStoreName=new DataStoreName(arrNames[1]);
    }
    return dataStoreName;
  }

  private static String normalize(String qName) {
    return qName.toLowerCase();
  }
}
