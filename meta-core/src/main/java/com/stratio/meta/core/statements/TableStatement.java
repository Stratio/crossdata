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

package com.stratio.meta.core.statements;

import com.stratio.meta.core.structures.TableName;

import scala.tools.cmd.Meta;

/**
 * Meta statement that are executed over a table.
 */
public abstract class TableStatement extends MetaStatement{

  /**
   * The target table.
   */
  protected TableName tableName;

  /**
   * Get the table to be described.
   *
   * @return The name or null if not set.
   */
  public TableName getTableName() {
    return tableName;
  }

  public void setTableName(TableName tableName) {
    this.tableName = tableName;
  }

  @Override
  public String getEffectiveCatalog() {
    String effective = null;
    if(tableName != null){
      effective = tableName.getCatalog();
    }else{
      effective = catalog;
    }
    if(sessionCatalog != null){
      effective = sessionCatalog;
    }
    return effective;
  }
}
