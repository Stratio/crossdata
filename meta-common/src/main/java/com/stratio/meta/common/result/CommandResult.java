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
 * Result of the execution of a command in META.
 */
public class CommandResult extends Result {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = -6987780936081295180L;

    /**
     * Execution result.
     */
    private final String result;

    /**
     * Private class constructor of the factory.
     * @param result The execution result.
     * @param error Whether an error occurred during the query execution.
     * @param errorMessage The error message in case of {@code error}.
     * @param currentKeyspace The current keyspace after the execution of the command.
     */
    private CommandResult(String result,
                          boolean error,
                          String errorMessage,
                          String currentKeyspace){
        super(error, errorMessage, false, currentKeyspace);
        this.result=result;
    }

    /**
     * Get the execution result.
     * @return The result or null if an error occurred.
     */
    public Object getResult() {
        return result;
    }

    /**
     * Create a successful command result.
     * @param result The execution result.
     * @return A {@link com.stratio.meta.common.result.CommandResult}.
     */
    public static CommandResult createSuccessCommandResult(String result){
        return new CommandResult(result, false, null, null);
    }

    /**
     * Create a failed command result.
     * @param errorMessage The associated error message.
     * @return A {@link com.stratio.meta.common.result.CommandResult}.
     */
    public static CommandResult createFailCommandResult(String errorMessage){
        return new CommandResult(null, true, errorMessage, null);
    }

}
