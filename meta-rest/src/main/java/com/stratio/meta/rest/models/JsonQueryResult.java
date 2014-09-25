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

package com.stratio.meta.rest.models;

/**
 * Result of the execution of a query in META.
 */
public class JsonQueryResult extends JsonResult {

    /**
     *
     */
    private static final long serialVersionUID = 2615773587916703533L;

    /**
     * Set of tuples returned by the server after successfully executing the query.
     */
    private final JsonMetaResultSet resultSet;

    /**
     * Determine whether this query result contains the last resultset of the query.
     */
    private boolean lastResultSet = false;

    /**
     * Number of results sets that are part of the same query identifier sent to the client.
     */
    private int resultPage = 0;

    /**
     * The current user session catalog if it has been changed.
     */
    private String currentCatalog = null;

    /**
     * Whether the session catalog has changed.
     */
    private boolean catalogChanged = false;

    public JsonQueryResult(JsonMetaResultSet resultSet, int resultPage, boolean catalogChanged,
            String currentCatalog, boolean lastResultSet) {
        this.resultSet = resultSet;
        this.resultPage = resultPage;
        this.catalogChanged = catalogChanged;
        this.currentCatalog = currentCatalog;
        this.lastResultSet = lastResultSet;
    }

    /**
     * Get the set of results returned by the query.
     *
     * @return The set or null if an error occurred.
     */
    public JsonMetaResultSet getResultSet() {
        return resultSet;
    }

    /**
     * Whether the session catalog has changed as an effect of executing the requested query.
     *
     * @return Whether it has changed or not.
     */
    public boolean isCatalogChanged() {
        return catalogChanged;
    }

    public String getCurrentCatalog() {
        return currentCatalog;
    }

    /**
     * Set the new user session catalog.
     *
     * @param currentCatalog The new catalog.
     */
    public void setCurrentCatalog(String currentCatalog) {
        this.currentCatalog = currentCatalog;
        this.catalogChanged = true;
    }

    public void setLastResultSet() {
        this.lastResultSet = true;
    }

    public boolean isLastResultSet() {
        return lastResultSet;
    }

    public int getResultPage() {
        return resultPage;
    }

    public void setResultPage(int resultPage) {
        this.resultPage = resultPage;
    }
}
