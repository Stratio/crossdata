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
 * Exception thrown by a connector operation.
 */
public class ConnectorException extends Exception {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -847420817619537147L;

    /**
     * Constructor class based in a Exception.
     * @param e The exception.
     */
    public ConnectorException(Exception e){
        super(e);
    }

    /**
     * Constructor class based in a message.
     * @param msg The message.
     */
    public ConnectorException(String msg) {
        super(msg);
    }

    /**
     * Constructor class based in a message and a throwable exception.
     * @param msg The message.
     * @param cause The throwable exception.
     */
    public ConnectorException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
