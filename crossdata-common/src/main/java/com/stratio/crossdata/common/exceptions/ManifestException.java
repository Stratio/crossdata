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

package com.stratio.crossdata.common.exceptions;

/**
 * Exception throw if the provided manifest is not valid.
 */
public class ManifestException extends ApiException {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = 6466318923082574130L;

    /**
     * Constructs a new Exception with a given message.
     *
     * @param message The message.
     */
    public ManifestException(String message) {
        super(message);
    }

    /**
     * Constructs a new Exception with a specific cause.
     *
     * @param cause The associated cause.
     */
    public ManifestException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new Exception with a specific message and cause.
     *
     * @param msg   The associated message.
     * @param cause The associated cause.
     */
    public ManifestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public String toString() {
        return "Error parsing manifest: " + System.lineSeparator() + super.toString();
    }

}
