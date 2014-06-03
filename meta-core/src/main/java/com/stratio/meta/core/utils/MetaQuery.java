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

package com.stratio.meta.core.utils;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.MetaStatement;

/**
 * Class that contains all the information required to execute a statement. The different
 * {@link com.stratio.meta.core.engine.Engine} steps will populate the query information.
 */
public class MetaQuery {

    /**
     * The query introduced by the user.
     */
    private String query;

  /**
   * Unique query identifier.
   */
  private String queryId;

    /**
     * The query status.
     */
    private QueryStatus status;

    /**
     * Whether an error has been detected at some point during the processing or execution.
     */
    private boolean hasError = false;

    /**
     * The {@link com.stratio.meta.core.statements.MetaStatement} build by the {@link com.stratio.meta.core.parser.Parser}.
     */
    private MetaStatement statement;

    /**
     * The {@link com.stratio.meta.core.utils.Tree} with the execution plan.
     */
    private Tree plan;

    /**
     * A {@link com.stratio.meta.common.result.Result} with the execution result or error message.
     */
    private Result result;

    /**
     * The keyspace on the user session launching the query.
     */
    private String sessionKeyspace;

    /**
     * Class constructor.
     */
    public MetaQuery() {
        status = QueryStatus.NONE;
        result = QueryResult.createSuccessQueryResult();
    }

    /**
     * Class constructor.
     * @param query The user query.
     */
    public MetaQuery(String query) {
        this();
        this.query = query;
    }

    /**
     * Get the user query.
     * @return The query or null if not set.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Set the user query.
     * @param query The user query.
     */
    public void setQuery(String query) {
        this.query = query;
    }

  /**
   * Get the query identifier.
   * @return The identifier.
   */
  public String getQueryId() {
    return queryId;
  }

  /**
   * Set the query identifier.
   * @param queryId The identifier.
   */
  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  /**
     * Set the execution status.
     * @param status A {@link com.stratio.meta.core.utils.QueryStatus}.
     */
    public void setStatus(QueryStatus status) {
        this.status = status;
    }

    /**
     * Whether the query processing or execution is erroneous.
     * @return Whether there is an error or not.
     */
    public boolean hasError() {
        return hasError;
    }

    /**
     * Set the {@code hasError} to true.
     */
    public void setError(String errorStr){
        result=QueryResult.createFailQueryResult("Unknown parser error: "+errorStr);
        hasError = true;

    }

    /**
     * Set the error message.
     * @param errorMsg The error message.
     */
    public void setErrorMessage(String errorMsg) {
        hasError = true;
        result=QueryResult.createFailQueryResult(errorMsg);
    }

    /**
     * Set the parsed {@link com.stratio.meta.core.statements.MetaStatement}.
     * @param statement The statement.
     */
    public void setStatement(MetaStatement statement) {
        this.statement = statement;
    }

    /**
     * Get the parsed statement.
     * @return The {@link com.stratio.meta.core.statements.MetaStatement}.
     */
    public MetaStatement getStatement() {
        return statement;
    }

    /**
     * Get the execution plan.
     * @return A {@link com.stratio.meta.core.utils.Tree} with the plan.
     */
    public Tree getPlan() {
        return plan;
    }

    /**
     * Set the execution plan.
     * @param plan A {@link com.stratio.meta.core.utils.Tree}.
     */
    public void setPlan(Tree plan) {
        this.plan = plan;
    }

    /**
     * Set the execution result.
     * @param result A {@link com.stratio.meta.common.result.Result}.
     */
    public void setResult(Result result) {
        this.result = result;
        this.hasError = result.hasError();
    }

    /**
     * Get the execution result.
     * @return A {@link com.stratio.meta.common.result.Result}.
     */
    public Result getResult() {
      result.setQueryId(getQueryId());
        return result;
    }

    /**
     * Get the user session keyspace.
     * @return The keyspace.
     */
    public String getSessionKeyspace() {
        return sessionKeyspace;
    }

    /**
     * Set the user session keyspace.
     * @param sessionKeyspace The keyspace.
     */
    public void setSessionKeyspace(String sessionKeyspace) {
        this.sessionKeyspace = sessionKeyspace;
        statement.setSessionKeyspace(sessionKeyspace);
    }
}
