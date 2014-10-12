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

package com.stratio.meta2.common.result;

/**
 * Error result of a given type.
 * <ul>
 * <li>Parsing</li>
 * <li>Validation</li>
 * <li>Execution</li>
 * <li>Not supported</li>
 * </ul>
 */
public class ErrorResult extends Result {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -3692402254351549778L;

    /**
     * Type of error.
     */
    private final ErrorType type;

    /**
     * The associated error message in case of {@code error}.
     */
    private final String errorMessage;

    private Exception exception;

    public ErrorResult(ErrorType type, String errorMessage) {
        this.type = type;
        this.errorMessage = errorMessage;
        this.error = true;
    }

    public ErrorResult(ErrorType type, String errorMessage, Exception e) {
        this.type = type;
        this.errorMessage = errorMessage;
        this.error = true;
        this.exception = e;
    }

    /**
     * Get the error message.
     *
     * @return The message or null if no error occurred.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorType getType() {
        return type;
    }

    public Exception getException() {
        return exception;
    }
}
