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


public class InnerJoin {

  private String keyspace = null;

  private boolean keyspaceInc = false;

  private String tableName;

  private SelectorIdentifier leftField;

  private SelectorIdentifier rightField;

  private InnerJoin(String tableName) {

    this.tableName = tableName;

    if (this.tableName.contains(".")) {
      String[] ksAndTablename = this.tableName.split("\\.");
      keyspace = ksAndTablename[0];
      this.tableName = ksAndTablename[1];
      keyspaceInc = true;
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

  public String getKeyspace() {
    return keyspace;
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
    if (keyspaceInc) {
      sb.append(keyspace);
      sb.append(".");
    }
    sb.append(tableName);
    sb.append(" ON ").append(leftField).append("=").append(rightField);
    return sb.toString();
  }

  public boolean isKeyspaceInc() {
    return keyspaceInc;
  }
}
