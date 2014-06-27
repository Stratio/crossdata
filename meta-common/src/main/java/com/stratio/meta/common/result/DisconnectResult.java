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

public class DisconnectResult extends Result {

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
     * @param sessionId       The associated session identifier.
     */
    private DisconnectResult(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Get the user session identifier.
     *
     * @return The identifier or -1 if an error occurred.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Create a successful connection result.
     *
     * @param sessionId The user session identifier.
     * @return A {@link com.stratio.meta.common.result.ConnectResult}.
     */
    public static DisconnectResult createDisconnectResult(String sessionId) {
        return new DisconnectResult(sessionId);
    }

}
