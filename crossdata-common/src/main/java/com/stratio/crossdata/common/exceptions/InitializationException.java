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
 * Exception thrown by the connectors to signal a problem during the initialization
 * phase. The error may be related to the configuration, the connectivity or any other
 * problem the connector may encounter during the initialization.
 */
public class InitializationException extends ConnectorException {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -3453090024561154440L;

    /**
     * Constructor class based in an Exception.
     * @param e The Exception.
     */
    public InitializationException(Exception e){
        super(e);
    }

    /**
     * Constructor class based in a string message.
     * @param msg The message.
     */
    public InitializationException(String msg) {
        super(msg);
    }

    /**
     * Constructor class based in a string message and a throwable exception.
     * @param msg The message.
     * @param cause The throwable exception.
     */
    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
