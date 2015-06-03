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

/**
 * Result of the connection from a crossdata connector to a cluster.
 */
public final class ConnectToConnectorResult extends Result {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = 5731347024563712574L;

    /**
     * Exception associated with the result.
     */
    private Exception exception;

    /**
     * Private class constructor of the factory.
     *
     * @param isConnected Whether the connector is succesfully connected or not.
     * @param queryId Query identifier associated with the result.
     */
    private ConnectToConnectorResult(boolean isConnected, String queryId) {
        this.queryId = queryId;
        this.error = !isConnected;
    }

    /**
     * Create a successful connection result.
     *
     * @param queryId Query identifier associated with the result.
     * @return A {@link com.stratio.crossdata.common.result.ConnectToConnectorResult}.
     */
    public static ConnectToConnectorResult createSuccessConnectResult(String queryId) {
        return new ConnectToConnectorResult(true, queryId);
    }

    /**
     * Create a successful connection result.
     *
     * @param queryId   Query identifier associated with the result.
     * @param exception Exception associated with the result.
     * @return A {@link com.stratio.crossdata.common.result.ConnectToConnectorResult}.
     */
    public static ConnectToConnectorResult createFailureConnectResult(String queryId, Exception exception) {
        ConnectToConnectorResult result = new ConnectToConnectorResult(false, queryId);
        result.exception = exception;
        return result;
    }

    /**
     * Returns the exception associated with the result.
     *
     * @return The exception thrown by the connector.
     */
    public Exception getException() {
        return exception;
    }
}
