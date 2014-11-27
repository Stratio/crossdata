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

package com.stratio.crossdata.sh;

import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.sh.utils.ConsoleUtils;

/**
 * Results handler for the Crossdata shell when the asynchronous interface is used.
 */
public class ShellDriverResultHandler implements IDriverResultHandler {

    /**
     * Parent Crossdata shell.
     */
    private final Shell parent;

    /**
     * Class constructor.
     *
     * @param parent Parent Crossdata Shell that will be informed when results are available.
     */
    public ShellDriverResultHandler(Shell parent) {
        this.parent = parent;
    }

    @Override
    public void processAck(String queryId, QueryStatus status) {
    }

    @Override
    public void processError(Result errorResult) {
        parent.println("");
        parent.println("Result: " + ConsoleUtils.stringResult(errorResult));
        parent.flush();
    }

    @Override
    public void processResult(Result result) {
        parent.updatePrompt(result);
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append("Result: QID: ").append(result.getQueryId()).append(System.lineSeparator())
                .append(ConsoleUtils.stringResult(result));
        if (QueryResult.class.isInstance(result)) {
            QueryResult r = QueryResult.class.cast(result);
            sb.append(System.lineSeparator()).append("Result page: ").append(r.getResultPage());
            if (r.isLastResultSet()) {
                sb.append(System.lineSeparator()).append("Removing results handler for: ").append(result.getQueryId());
                parent.removeResultsHandler(result.getQueryId());
            }
        }
        parent.println(sb.toString());
        parent.flush();
    }
}
