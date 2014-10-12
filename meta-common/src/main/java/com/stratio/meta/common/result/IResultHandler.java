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

import com.stratio.meta2.common.result.Result;

/**
 * Callback interface for classes receiving asynchronous results from meta servers.
 */
public interface IResultHandler {

    /**
     * Process an acknowledgement message for a specific query.
     *
     * @param queryId The query identifier.
     * @param status  The query status.
     */
    public void processAck(String queryId, QueryStatus status);

    /**
     * Process an error result.
     *
     * @param errorResult The error.
     */
    public void processError(Result errorResult);

    /**
     * Process a successful result.
     *
     * @param result The result.
     */
    public void processResult(Result result);
}
