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

package com.stratio.crossdata.driver;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.result.Result;

public class TestResultHandler implements IDriverResultHandler {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(TestResultHandler.class);

    private final int expectedPages;
    private int count = 0;
    private boolean resultAvailable = false;
    private boolean success = false;

    public TestResultHandler(int expectedPages) {
        this.expectedPages = expectedPages-1;
    }

    /**
     * Process an acknowledgement message for a specific query.
     *
     * @param queryId The query identifier.
     * @param status  The query status.
     */
    @Override
    public void processAck(String queryId, QueryStatus status) {
        LOG.info(queryId + " " + status);
    }

    /**
     * Process a successful result.
     *
     * @param result The result.
     */
    @Override
    public void processResult(Result result) {
        try {
            QueryResult queryResult = (QueryResult) result;
            LOG.info(queryResult.getResultSet());
            if(queryResult.isLastResultSet()){
                resultAvailable = true;
                if((count == queryResult.getResultPage()) && (count == expectedPages)){
                    success = true;
                }
            }
            count++;
        } catch (Exception ex){
            resultAvailable = true;
            success = false;
        }
    }

    /**
     * Process an error result.
     *
     * @param result The error.
     */
    @Override
    public void processError(Result result) {
        resultAvailable = true;
        ErrorResult errorResult = (ErrorResult) result;
        LOG.error(errorResult);
    }

    public boolean isResultAvailable() {
        return resultAvailable;
    }

    public boolean wasSuccessfully() {
        return success;
    }
}
