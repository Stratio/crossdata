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

package com.stratio.crossdata.core.query;

import java.io.Serializable;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.result.QueryStatus;

/**
 * A base query class that contains the status of the query, the id, the query and the catalog.
 */
public class BaseQuery implements Serializable {
    /**
     * The query introduced by the user.
     */
    private final String query;

    /**
     * Unique query identifier.
     */
    private final String queryId;

    private final String sessionId;

    private final CatalogName defaultCatalog;

    private QueryStatus queryStatus;



    /**
     * Constructor class.
     * @param queryId The query id.
     * @param query The string with the query.
     * @param defaultCatalog The catalog of the query.
     * @param sessionId The session associated to the query.
     */
    public BaseQuery(String queryId, String query, CatalogName defaultCatalog, String sessionId) {
        this.queryId = queryId;
        this.query = query;
        this.defaultCatalog = defaultCatalog;
        this.queryStatus = QueryStatus.NONE;
        this.sessionId=sessionId;
    }

    /**
     * Constructor class.
     * @param baseQuery The base query.
     */
    BaseQuery(BaseQuery baseQuery) {
        this(baseQuery.getQueryId(), baseQuery.getQuery(), baseQuery.getDefaultCatalog(), baseQuery.getSessionId());
    }

    public String getQuery() {
        return query;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public QueryStatus getStatus() {
        return queryStatus;
    }

    public CatalogName getDefaultCatalog() {
        return defaultCatalog;
    }

    public void setQueryStatus(QueryStatus queryStatus) {
        this.queryStatus = queryStatus;
    }
}
