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
    private final boolean error;

    /**
     * The associated error message in case of {@code error}.
     */
    private final String errorMessage;

    /**
     * Whether the user session keyspace is changed by the execution of the action.
     */
    private final boolean ksChanged;

    /**
     * The current user session keyspace in case of {@code ksChanged}.
     */
    private final String currentKeyspace;

    /**
     * Build a generic result of execution an action in a META server.
     * @param error Whether an error occurred during the execution.
     * @param errorMessage The error message in case of {@code error}.
     * @param ksChanged Whether the current keyspace in the user session is modified by the execution.
     * @param currentKeyspace The current keyspace after the execution.
     */
    Result(boolean error,
           String errorMessage,
           boolean ksChanged,
           String currentKeyspace) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.ksChanged = ksChanged;
        this.currentKeyspace = currentKeyspace;
    }

    /**
     * Whether the result contains an error.
     * @return True if the result contains errors.
     */
    public boolean hasError() {
        return error;
    }

    /**
     * Get the error message.
     * @return The message or null if no error occurred.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Whether the keyspace is changed by the execution.
     * @return True if the user session keyspace is changed.
     */
    public boolean isKsChanged() {
        return ksChanged;
    }

    /**
     * Get the current user session keyspace.
     * @return The keyspace or null if the keyspace is not changed.
     */
    public String getCurrentKeyspace() {
        return currentKeyspace;
    }

}
