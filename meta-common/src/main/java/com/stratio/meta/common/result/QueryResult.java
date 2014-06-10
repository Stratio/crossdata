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
    private static final long serialVersionUID = -5367895767326893839L;

    /**
     * Set of tuples returned by the server after successfully executing the query.
     */
    private final ResultSet resultSet;

  /**
   * Determine whether this query result contains the last resultset of the query.
   */
  private boolean lastResultSet = false;

    /**
     * Private class constructor of the factory.
     * @param resultSet The set of tuples returned.
     * @param error Whether an error occurred during the query execution.
     * @param errorMessage The error message in case of {@code error}.
     * @param ksChanged Whether the current keyspace in the user session is modified by the query execution.
     * @param currentKeyspace The current keyspace in case of {@code ksChanged}.
     */
    private QueryResult(ResultSet resultSet,
                        boolean error,
                        String errorMessage,
                        boolean ksChanged,
                        String currentKeyspace){
        super(error,errorMessage,ksChanged,currentKeyspace);
        this.resultSet=resultSet;
    }

    /**
     * Get the set of results returned by the query.
     * @return The set or null if an error occurred.
     */
    public ResultSet getResultSet() {
        return resultSet;
    }

    /**
     * Create a successful query result.
     * @return A {@link com.stratio.meta.common.result.QueryResult} without a {@link com.stratio.meta.common.data.ResultSet}.
     */
    public static QueryResult createSuccessQueryResult(){
        return  createSuccessQueryResult(null);
    }

    /**
     * Create a successful query result.
     * @param resultSet The associated {@link com.stratio.meta.common.data.ResultSet}
     * @return A {@link com.stratio.meta.common.result.QueryResult}.
     */
    public static QueryResult createSuccessQueryResult(ResultSet resultSet){
        return new QueryResult(resultSet,false,null,false,null);
    }

    /**
     * Create a successful query result.
     * @param resultSet The associated {@link com.stratio.meta.common.data.ResultSet}.
     * @param keySpace The new session keyspace.
     * @return A {@link com.stratio.meta.common.result.QueryResult}.
     */
    public static QueryResult createSuccessQueryResult(ResultSet resultSet, String keySpace){
        return new QueryResult(resultSet,false,null,true,keySpace);
    }

    /**
     * Create a failed query result.
     * @param errorMessage The associated error message.
     * @return A {@link com.stratio.meta.common.result.QueryResult}.
     */
    public static QueryResult createFailQueryResult(String errorMessage){
        return new QueryResult(null,true,errorMessage,false,null);
    }

  public void setLastResultSet() {
    this.lastResultSet = true;
  }

  public boolean isLastResultSet() {
    return lastResultSet;
  }
}
