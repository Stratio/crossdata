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

package com.stratio.meta.core.utils;

import com.stratio.meta.common.result.ErrorType;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.result.Result;
import com.stratio.meta2.core.statements.MetaStatement;

/**
 * Class that contains all the information required to execute a statement. The different {@link
 * com.stratio.meta2.core.engine.Engine} steps will populate the query information.
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
   * The {@link com.stratio.meta2.core.statements.MetaStatement} build by the {@link
   * com.stratio.meta2.core.parser.Parser}.
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
   * The catalog on the user session launching the query.
   */
  private String sessionCatalog;

  /**
   * Class constructor.
   */
  public MetaQuery() {
    status = QueryStatus.NONE;
    result = QueryResult.createSuccessQueryResult();
  }

  /**
   * Class constructor.
   *
   * @param query The user query.
   */
  public MetaQuery(String query) {
    this();
    this.query = query;
  }

  /**
   * Get the user query.
   *
   * @return The query or null if not set.
   */
  public String getQuery() {
    return query;
  }

  /**
   * Set the user query.
   *
   * @param query The user query.
   */
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Get the query identifier.
   *
   * @return The identifier.
   */
  public String getQueryId() {
    return queryId;
  }

  /**
   * Set the query identifier.
   *
   * @param queryId The identifier.
   */
  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  /**
   * Set the execution status.
   *
   * @param status A {@link com.stratio.meta.common.result.QueryStatus}.
   */
  public void setStatus(QueryStatus status) {
    this.status = status;
  }

  /**
   * Whether the query processing or execution is erroneous.
   *
   * @return Whether there is an error or not.
   */
  public boolean hasError() {
    return hasError;
  }

  /**
   * Set the error message and {@code hasError} to true.
   *
   * @param type     Error type.
   * @param errorMsg The error message.
   */
  public void setErrorMessage(ErrorType type, String errorMsg) {
    hasError = true;
    if (ErrorType.PARSING.equals(type)) {
      result = Result.createParsingErrorResult(errorMsg);
    } else if (ErrorType.VALIDATION.equals(type)) {
      result = Result.createValidationErrorResult(errorMsg);
    } else if (ErrorType.EXECUTION.equals(type)) {
      result = Result.createExecutionErrorResult(errorMsg);
    } else {
      result = Result.createUnsupportedOperationErrorResult(errorMsg);
    }
  }

  /**
   * Set the parsed {@link com.stratio.meta2.core.statements.MetaStatement}.
   *
   * @param statement The statement.
   */
  public void setStatement(MetaStatement statement) {
    this.statement = statement;
  }

  /**
   * Get the parsed statement.
   *
   * @return The {@link com.stratio.meta2.core.statements.MetaStatement}.
   */
  public MetaStatement getStatement() {
    return statement;
  }

  /**
   * Get the execution plan.
   *
   * @return A {@link com.stratio.meta.core.utils.Tree} with the plan.
   */
  public Tree getPlan() {
    return plan;
  }

  /**
   * Set the execution plan.
   *
   * @param plan A {@link com.stratio.meta.core.utils.Tree}.
   */
  public void setPlan(Tree plan) {
    this.plan = plan;
  }

  /**
   * Set the execution result.
   *
   * @param result A {@link com.stratio.meta.common.result.Result}.
   */
  public void setResult(Result result) {
    this.result = result;
    this.hasError = result.hasError();
  }

  /**
   * Get the execution result.
   *
   * @return A {@link com.stratio.meta.common.result.Result}.
   */
  public Result getResult() {
    result.setQueryId(getQueryId());
    return result;
  }

  /**
   * Get the user session catalog.
   *
   * @return The catalog.
   */
  public String getSessionCatalog() {
    return sessionCatalog;
  }

  /**
   * Set the user session catalog.
   *
   * @param sessionCatalog The catalog.
   */
  public void setSessionCatalog(String sessionCatalog) {
    this.sessionCatalog = sessionCatalog;
    statement.setSessionCatalog(sessionCatalog);
  }
}
