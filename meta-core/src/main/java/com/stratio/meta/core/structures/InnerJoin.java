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

package com.stratio.meta.core.structures;


import com.stratio.meta.common.statements.structures.selectors.SelectorIdentifier;

public class InnerJoin {

  private String catalog = null;

  private boolean catalogInc = false;

  private String tableName;

  private SelectorIdentifier leftField;

  private SelectorIdentifier rightField;

  private InnerJoin(String tableName) {

    this.tableName = tableName;

    if (this.tableName.contains(".")) {
      String[] ksAndTablename = this.tableName.split("\\.");
      catalog = ksAndTablename[0];
      this.tableName = ksAndTablename[1];
      catalogInc = true;
    }
  }

  public InnerJoin(String tableName, String leftField, String rightField) {

    this(tableName);

    this.leftField = new SelectorIdentifier(leftField);
    this.rightField = new SelectorIdentifier(rightField);
  }

  public InnerJoin(String tableName, SelectorIdentifier leftField, SelectorIdentifier rightField) {

    this(tableName);

    this.leftField = leftField;
    this.rightField = rightField;
  }

  public String getTablename() {
    return tableName;
  }

  public String getCatalog() {
    return catalog;
  }

  public SelectorIdentifier getLeftField() {
    return leftField;
  }

  public SelectorIdentifier getRightField() {
    return rightField;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (catalogInc) {
      sb.append(catalog);
      sb.append(".");
    }
    sb.append(tableName);
    sb.append(" ON ").append(leftField).append("=").append(rightField);
    return sb.toString();
  }

  public boolean isCatalogInc() {
    return catalogInc;
  }
}
