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

package com.stratio.meta2.core.query;

import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta2.common.data.CatalogName;

public class BaseQuery {
  /**
   * The query introduced by the user.
   */
  private final String query;

  /**
   * Unique query identifier.
   */
  private final String queryId;


  private final CatalogName defaultCatalog;

  public BaseQuery(String queryId, String query, CatalogName defaultCatalog) {
    this.queryId = queryId;
    this.query = query;
    this.defaultCatalog = defaultCatalog;
  }

  BaseQuery(BaseQuery baseQuery) {
    this(baseQuery.getQueryId(), baseQuery.getQuery(), baseQuery.getDefaultCatalog());
  }

  public String getQuery() {
    return query;
  }

  public String getQueryId() {
    return queryId;
  }

  public QueryStatus getStatus() {
    return QueryStatus.NONE;
  }

  public CatalogName getDefaultCatalog() {
    return defaultCatalog;
  }
}
