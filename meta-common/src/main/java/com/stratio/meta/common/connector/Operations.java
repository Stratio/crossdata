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

package com.stratio.meta.common.connector;

/**
 * Operations supported by an {@link com.stratio.meta.common.connector.IConnector}.
 */
public enum Operations {

  /**
   * The engine supports creating new catalogs.
   */
  CREATE_CATALOG,

  /**
   * The engine supports creating deleting existing catalogs.
   */
  DROP_CATALOG,

  /**
   * The engine supports creating new tables given an existing catalog.
   */
  CREATE_TABLE,

  /**
   * The engine supports deleting new tables given an existing catalog.
   */
  DROP_TABLE,

  /**
   * The engine supports inserting data in existing tables.
   */
  INSERT,

  /**
   * The engine supports deleting elements from existing tables.
   */
  DELETE,

  /**
   * The engine supports {@link com.stratio.meta.common.statements.structures.window.Window} logical
   * plans for streaming-like datastores.
   */
  SELECT_WINDOW,

  /**
   * The engine supports limiting the number of results returned in a query.
   */
  SELECT_LIMIT,

  /**
   * The engine supports inner joins.
   */
  SELECT_INNER_JOIN,

  /**
   * The engine supports order by clauses.
   */
  SELECT_ORDER_BY,

  /**
   * The engine supports group by clauses.
   */
  SELECT_GROUP_BY,

  /**
   * The engine supports aggregator operations (e.g., sum, avg, etc.) on the columns of a
   * {@link com.stratio.meta.common.logicalplan.Project}.
   */
  SELECT_AGGREGATION_SELECTORS,

  /**
   * The engine supports in relationships in {@link com.stratio.meta.common.logicalplan.Filter}
   * operations.
   */
  SELECT_WHERE_IN,

  /**
   * The engine supports between relationships in {@link com.stratio.meta.common.logicalplan.Filter}
   * operations.
   */
  SELECT_WHERE_BETWEEN,

  /**
   * The engine supports match relationships in {@link com.stratio.meta.common.logicalplan.Filter}
   * The engine supports match relationships in {@link com.stratio.meta.common.logicalplan.Filter}
   * operations.
   */
  SELECT_WHERE_MATCH;

}
