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
 * Disconnect Result class implements the result of a detach statement.
 */
public final class DisconnectResult extends Result {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */

    /**
     * Session identifier.
     */
    private String sessionId;

    /**
     * Private class constructor of the factory.
     *
     * @param sessionId The associated session identifier.
     */
    private DisconnectResult(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Create a successful connection result.
     *
     * @param sessionId The user session identifier.
     * @return A {@link com.stratio.crossdata.common.result.ConnectResult}.
     */
    public static DisconnectResult createDisconnectResult(String sessionId) {
        return new DisconnectResult(sessionId);
    }

    /**
     * Get the user session identifier.
     *
     * @return The identifier or -1 if an error occurred.
     */
    public String getSessionId() {
        return sessionId;
    }

}
