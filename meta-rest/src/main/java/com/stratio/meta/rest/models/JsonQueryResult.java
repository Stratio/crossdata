package com.stratio.meta.rest.models;

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
  private final JsonResultSet resultSet;

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
  public JsonResultSet getResultSet() {
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
}
