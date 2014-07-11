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

package com.stratio.meta.common.result;

import com.stratio.meta.common.data.ResultSet;

/**
 * Result of the execution of a query in META.
 */
public class QueryResult extends Result {

  /**
   * Serial version UID in order to be {@link java.io.Serializable}.
   */

  /**
   * Set of tuples returned by the server after successfully executing the query.
   */
  private final ResultSet resultSet;

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

  /**
   * Whether the execution of the query has been ignored due to some constraints not being
   * satisfied (e.g., IF NOT EXISTS).
   */
  //TODO Return this type of query for IF NOT EXISTS
  private boolean ignoredQuery = false;

  /**
   * Private class constructor of the factory.
   *
   * @param resultSet The set of tuples returned.
   */
  private QueryResult(ResultSet resultSet) {
    this.resultSet = resultSet;
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
   * Set the new user session catalog.
   *
   * @param currentCatalog The new catalog.
   */
  public void setCurrentCatalog(String currentCatalog) {
    this.currentCatalog = currentCatalog;
    this.catalogChanged = true;
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

  public void setLastResultSet() {
    this.lastResultSet = true;
  }

  public boolean isLastResultSet() {
    return lastResultSet;
  }

  public void setResultPage(int resultPage) {
    this.resultPage = resultPage;
  }

  public int getResultPage() {
    return resultPage;
  }

  public static QueryResult createSuccessQueryResult(){
    return new QueryResult(null);
  }

  /**
   * Create a successful query result.
   *
   * @param resultSet The associated {@link com.stratio.meta.common.data.ResultSet}
   * @return A {@link com.stratio.meta.common.result.QueryResult}.
   */
  public static QueryResult createQueryResult(ResultSet resultSet) {
    return new QueryResult(resultSet);
  }


  /**
   * Create a successful query result.
   *
   * @param resultSet The associated {@link com.stratio.meta.common.data.ResultSet}.
   * @param catalog   The new session catalog.
   * @return A {@link com.stratio.meta.common.result.QueryResult}.
   */
  public static QueryResult createSuccessQueryResult(ResultSet resultSet, String catalog) {
    QueryResult result = new QueryResult(resultSet);
    result.setCurrentCatalog(catalog);
    return result;
  }


}
