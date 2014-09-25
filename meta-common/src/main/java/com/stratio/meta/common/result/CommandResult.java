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

package com.stratio.meta.common.result;

/**
 * Result of the execution of a command in META.
 */
public class CommandResult extends Result {

    /**
     * Serial version UID in order to be {@link java.io.Serializable}.
     */

    /**
     * Execution result.
     */
    private final String result;

    /**
     * Private class constructor of the factory.
     *
     * @param result The execution result.
     */
    private CommandResult(String result) {
        this.result = result;
    }

    /**
     * Create a successful command result.
     *
     * @param result The execution result.
     * @return A {@link com.stratio.meta.common.result.CommandResult}.
     */
    public static CommandResult createCommandResult(String result) {
        return new CommandResult(result);
    }

    /**
     * Get the execution result.
     *
     * @return The result or null if an error occurred.
     */
    public Object getResult() {
        return result;
    }

}
