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
   * The engine supports retrieving a set of columns from a specific table.
   */
  PROJECT,

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
   * The engine supports aggregator operations (e.g., sum, avg, etc.) on a Select statement.
   */
  SELECT_FUNCTIONS,

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
   * The engine supports {@link com.stratio.meta.common.logicalplan.Filter} operations on columns
   * that are part of the primary key.
   */
  FILTER_PK,

  /**
   * The engine supports {@link com.stratio.meta.common.logicalplan.Filter} operations on columns
   * that are not indexed by the underlying datastore.
   */
  FILTER_NON_INDEXED,

  /**
   * The engine supports {@link com.stratio.meta.common.logicalplan.Filter} operations on columns
   * that have an associated index in the underlying datastore.
   */
  FILTER_INDEXED,

  /**
   * The engine supports full text search syntax in {@link com.stratio.meta.common.logicalplan.Filter}
   * operations.
   */
  FILTER_FULLTEXT
  ;

}
