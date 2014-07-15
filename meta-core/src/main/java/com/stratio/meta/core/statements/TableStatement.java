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

import scala.tools.cmd.Meta;

/**
 * Meta statement that are executed over a table.
 */
public abstract class TableStatement extends MetaStatement{

  /**
   * The target table.
   */
  protected String tableName;

  /**
   * Get the table to be described.
   *
   * @return The name or null if not set.
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Set the name of the table to be described. If the table is fully qualified,
   * the target catalog is updated accordingly.
   *
   * @param tableName The table name.
   */
  public void setTableName(String tableName) {
    if (tableName.contains(".")) {
      String[] ksAndTableName = tableName.split("\\.");
      catalogInc = true;
      catalog = ksAndTableName[0];
      this.tableName = ksAndTableName[1];
    } else {
      this.tableName = tableName;
    }
  }
}
