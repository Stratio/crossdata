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
 * Exception thrown when the operation is not currently supported
 * by the com.stratio.crossdata server.
 */
public class UnsupportedException extends ConnectorException {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = 5194419221361563613L;

    /**
     * Constructor class based in a Exception.
     * @param e The exception.
     */
    public UnsupportedException(Exception e){
        super(e);
    }

    /**
     * Constructor class based in a message.
     * @param message The message.
     */
    public UnsupportedException(String message) {
        super(message);
    }

    /**
     * Constructor class based in a message and a throwable exception.
     * @param msg The message.
     * @param cause The throwable exception.
     */
    public UnsupportedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
