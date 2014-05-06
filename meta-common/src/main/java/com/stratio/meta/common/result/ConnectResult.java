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

/**
 * Result of the connection with a remote META server.
 */
public class ConnectResult extends Result {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = -2632581413648530295L;

    /**
     * Session identifier.
     */
    private long sessionId = -1;

    /**
     * Private class constructor of the factory.
     * @param sessionId The associated session identifier.
     * @param error Whether an error occurred during the connection process.
     * @param errorMessage The error message in case of {@code error}.
     * @param ksChanged Whether the current keyspace in the user session is modified by the connection.
     * @param currentKeyspace The current keyspace after the connection.
     */
    private ConnectResult(long sessionId,
                          boolean error,
                          String errorMessage,
                          boolean ksChanged,
                          String currentKeyspace){
        super(error,errorMessage,ksChanged,currentKeyspace);
        this.sessionId=sessionId;
    }

    /**
     * Get the user session identifier.
     * @return The identifier or -1 if an error occurred.
     */
    public long getSessionId() {
        return sessionId;
    }

    /**
     * Create a successful connection result.
     * @param sessionId The user session identifier.
     * @return A {@link com.stratio.meta.common.result.ConnectResult}.
     */
    public static ConnectResult createSuccessConnectResult(long sessionId){
        return new ConnectResult(sessionId, false, null, false, null);
    }

    /**
     * Create a failed connection result.
     * @param errorMessage The associated error message.
     * @return A {@link com.stratio.meta.common.result.ConnectResult}.
     */
    public static ConnectResult createFailConnectResult(String errorMessage){
        return new ConnectResult(-1, true, errorMessage, false, null);
    }

    
}
