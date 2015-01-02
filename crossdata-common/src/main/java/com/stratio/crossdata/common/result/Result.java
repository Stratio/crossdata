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

import java.io.Serializable;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.CriticalExecutionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;

/**
 * Class that models a generic result of an action executed in CROSSDATA.
 */
public abstract class Result implements Serializable {


    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = 8596330240278204430L;

    /**
     * Whether an error occurred during the execution of an action.
     */
    protected boolean error = false;

    /**
     * Query identifier associated with the result.
     */
    protected String queryId;

    /**
     * Create an error result. This is the preferred way to notify that any exception
     * has been triggered by an operation.
     *
     * @param e The associated exception.
     * @return An {@link com.stratio.crossdata.common.result.ErrorResult}.
     */
    public static ErrorResult createErrorResult(Exception e) {
        return new ErrorResult(e);
    }

    /**
     * Create a connection error result passing a message.
     *
     * @param message The message.
     * @return An {@link com.stratio.crossdata.common.result.ErrorResult}.
     */
    public static ErrorResult createConnectionErrorResult(String message) {
        return new ErrorResult(new ConnectionException(message));
    }

    /**
     * Create a parsing error result passing a message.
     *
     * @param message The message.
     * @return An {@link com.stratio.crossdata.common.result.ErrorResult}.
     */
    public static ErrorResult createParsingErrorResult(String message) {
        return new ErrorResult(new ParsingException(message));
    }

    /**
     * Create a validation error result passing a {@link com.stratio.crossdata.common.exceptions.ValidationException}.
     *
     * @param e Any ValidationException.
     * @return An {@link com.stratio.crossdata.common.result.ErrorResult}.
     */
    public static ErrorResult createValidationErrorResult(ValidationException e) {
        return new ErrorResult(e);
    }

    /**
     * Create an execution error result passing a message.
     *
     * @param message The message.
     * @return An {@link com.stratio.crossdata.common.result.ErrorResult}.
     */
    public static ErrorResult createExecutionErrorResult(String message) {
        return new ErrorResult(new ExecutionException(message));
    }

    /**
     * Create an unsupported operation error result passing a message.
     *
     * @param message The message.
     * @return An {@link com.stratio.crossdata.common.result.ErrorResult}.
     */
    public static ErrorResult createUnsupportedOperationErrorResult(String message) {
        return new ErrorResult(new UnsupportedException(message));
    }

    /**
     * Create a critical operation error result passing a message.
     *
     * @param message The message.
     * @return An {@link com.stratio.crossdata.common.result.ErrorResult}.
     */
    public static ErrorResult createCriticalOperationErrorResult(String message) {
        return new ErrorResult(new CriticalExecutionException(message));
    }

    /**
     * Get the query identifier.
     *
     * @return The query identifier.
     */
    public String getQueryId() {
        return queryId;
    }

    /**
     * Set the query identifier.
     *
     * @param queryId The query identifier.
     */
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    /**
     * Whether the result contains an error.
     *
     * @return True if the result contains errors.
     */
    public boolean hasError() {
        return error;
    }

}
