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

package com.stratio.crossdata.common.result;

import com.stratio.crossdata.common.data.ResultSet;

/**
 * Result of the execution of a query in CROSSDATA.
 */
public final class QueryResult extends Result {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = -4934701384106071347L;

    /**
     * Set of tuples returned by the server after successfully executing the query.
     */
    private final ResultSet resultSet;

    /**
     * Determine whether this query result contains the last resultset of the query.
     */
    private boolean lastResultSet;

    /**
     * Number of results sets that are part of the same query identifier sent to the client.
     */
    private int resultPage;

    /**
     * The current user session catalog if it has been changed.
     */
    private String currentCatalog = null;

    /**
     * Whether the session catalog has changed.
     */
    private boolean catalogChanged = false;

    /**
     * Whether the execution of the query has been ignored due to some constraints not being
     * satisfied (e.g., IF NOT EXISTS).
     */
    //TODO Return this type of query for IF NOT EXISTS
    private boolean ignoredQuery = false;

    /**
     * Private class constructor of the factory.
     *
     * @param qId The Query Identification.
     * @param resultSet The set of tuples returned.
     * @param resultPage Number of the page.
     * @param lastResultSet whether it is the last result or not.
     */
    private QueryResult(String qId, ResultSet resultSet, int resultPage, boolean lastResultSet) {
        this.queryId = qId;
        this.resultSet = resultSet;
        this.resultPage = resultPage;
        this.lastResultSet = lastResultSet;
    }

    /**
     * Create a successful query result.
     *
     * @param qId The Query Identification.
     * @param resultSet The associated {@link com.stratio.crossdata.common.data.ResultSet}
     * @param resultPage Number of the page.
     * @param lastResultSet whether it is the last result or not.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     */
    public static QueryResult createQueryResult(
            String qId,
            ResultSet resultSet,
            int resultPage,
            boolean lastResultSet) {
        return new QueryResult(qId, resultSet, resultPage, lastResultSet);
    }

    /**
     * Create a successful query result.
     *
     * @param resultSet The associated {@link com.stratio.crossdata.common.data.ResultSet}
     * @param resultPage Number of the page.
     * @param lastResultSet whether it is the last result or not.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     */
    public static QueryResult createQueryResult(
            ResultSet resultSet,
            int resultPage,
            boolean lastResultSet) {
        return new QueryResult(null, resultSet, resultPage, lastResultSet);
    }

    /**
     * Create a successful query result.
     *
     * @param qId The Query Identification.
     * @param resultSet The associated {@link com.stratio.crossdata.common.data.ResultSet}.
     * @param resultPage Number of the page.
     * @param lastResultSet whether it is the last result or not.
     * @param catalog   The new session catalog.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     */
    public static QueryResult createQueryResult(
            String qId,
            ResultSet resultSet,
            int resultPage,
            boolean lastResultSet,
            String catalog) {
        QueryResult result = new QueryResult(qId, resultSet, resultPage, lastResultSet);
        result.setCurrentCatalog(catalog);
        return result;
    }

    /**
     * Create a successful query result.
     *
     * @param resultSet The associated {@link com.stratio.crossdata.common.data.ResultSet}.
     * @param resultPage Number of the page.
     * @param lastResultSet whether it is the last result or not.
     * @param catalog   The new session catalog.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     */
    public static QueryResult createQueryResult(
            ResultSet resultSet,
            int resultPage,
            boolean lastResultSet,
            String catalog) {
        QueryResult result = new QueryResult(null, resultSet, resultPage, lastResultSet);
        result.setCurrentCatalog(catalog);
        return result;
    }

    /**
     * Get the set of results returned by the query.
     *
     * @return The set or null if an error occurred.
     */
    public ResultSet getResultSet() {
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

    /**
     * Set  the last result set of the query.
     */
    public void setLastResultSet() {
        this.lastResultSet = true;
    }

    /**
     * Set  the last result set of the query.
     * @param lastResult last result
     */
    public void setLastResultSet(boolean lastResult) {
        this.lastResultSet = lastResult;
    }

    public boolean isLastResultSet() {
        return lastResultSet;
    }

    public int getResultPage() {
        return resultPage;
    }

    public boolean isIgnoredQuery() {
        return ignoredQuery;
    }
}
