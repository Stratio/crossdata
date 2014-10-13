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

package com.stratio.meta.driver;

import org.apache.log4j.Logger;

import com.stratio.meta.common.result.IResultHandler;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta2.common.result.Result;

/**
 * IResultHandler wrapper for test purposes.
 */
public class ResultHandlerWrapper implements IResultHandler {

    private final static Logger logger = Logger.getLogger(ResultHandlerWrapper.class);
    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(ResultHandlerWrapper.class);
    private boolean ackReceived = false;
    private boolean errorReceived = false;
    private boolean resultReceived = false;
    private QueryStatus status = null;

    @Override
    public synchronized void processAck(String queryId, QueryStatus status) {
        LOG.info("Query: " + queryId + " status: " + status);
        logger.info("Query: " + queryId + " status: " + status);
        ackReceived = true;
        this.status = status;
    }

    @Override
    public synchronized void processError(Result errorResult) {
        LOG.error("Error reported: " + errorResult);
        logger.info("Error reported: " + errorResult);
        errorReceived = true;
    }

    @Override
    public synchronized void processResult(Result result) {
        LOG.info("Result: " + result);
        logger.info("Result: " + result);
        resultReceived = true;
    }

    public boolean isAckReceived() {
        return ackReceived;
    }

    public boolean isErrorReceived() {
        return errorReceived;
    }

    public boolean isResultReceived() {
        return resultReceived;
    }

    public QueryStatus getStatus() {
        return status;
    }
}
