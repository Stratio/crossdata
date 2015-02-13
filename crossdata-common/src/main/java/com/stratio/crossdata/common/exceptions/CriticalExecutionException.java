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
 * Critical execution exceptions.
 */
public class CriticalExecutionException extends ExecutionException {

    private static final long serialVersionUID = -2333941596529953292L;

    /**
     * Constructor class.
     * @param e A Exception
     */
    public CriticalExecutionException(Exception e){
        super(e);
    }

    /**
     * Constructor class.
     * @param message The message.
     */
    public CriticalExecutionException(String message) {
        super(message);
    }

    /**
     * Constructor class.
     * @param message The message.
     * @param exception The throwable exception.
     */
    public CriticalExecutionException(String message, Exception exception){
        super(message, exception);
    }
}
