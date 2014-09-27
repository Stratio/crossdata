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

package com.stratio.meta.common.result;

import java.io.Serializable;

/**
 * Class that models a generic result of an action executed in META.
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
    private String queryId;

    public static ErrorResult createErrorResult(ErrorType type, String errorMessage) {
        return new ErrorResult(type, errorMessage);
    }

    public static ErrorResult createConnectionErrorResult(String errorMessage) {
        return new ErrorResult(ErrorType.CONNECTION, errorMessage);
    }

    public static ErrorResult createParsingErrorResult(String errorMessage) {
        return new ErrorResult(ErrorType.PARSING, errorMessage);
    }

    public static ErrorResult createValidationErrorResult(String errorMessage) {
        return new ErrorResult(ErrorType.VALIDATION, errorMessage);
    }

    public static ErrorResult createExecutionErrorResult(String errorMessage) {
        return new ErrorResult(ErrorType.EXECUTION, errorMessage);
    }

    public static ErrorResult createUnsupportedOperationErrorResult(String errorMessage) {
        return new ErrorResult(ErrorType.NOT_SUPPORTED, errorMessage);
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
