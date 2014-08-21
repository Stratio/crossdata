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

package com.stratio.meta.core.executor;

import org.apache.log4j.Logger;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.statements.ExplainPlanStatement;
import com.stratio.meta.core.statements.ListStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.StopProcessStatement;
import com.stratio.streaming.api.IStratioStreamingAPI;

public class CommandExecutor {

  private static final Logger LOG = Logger.getLogger(CommandExecutor.class);

  /**
   * Private class constructor as all methods are static.
   */
  private CommandExecutor() {

  }

  /**
   * Execute a {@link com.stratio.meta2.core.statements.MetaStatement} command.
   * 
   * @param stmt Statement to execute.
   * @param session Cassandra datastax java driver {@link com.datastax.driver.core.Session}.
   * @return a {@link com.stratio.meta.common.result.Result}.
   */
  public static Result execute(String queryId, MetaStatement stmt, Session session,
      IStratioStreamingAPI stratioStreamingAPI) {
    try {
      if (stmt instanceof DescribeStatement) {
        DescribeStatement descStmt = (DescribeStatement) stmt;
        return descStmt.execute(session, stratioStreamingAPI);
      } else if (stmt instanceof ExplainPlanStatement) {
        ExplainPlanStatement explainStmt = (ExplainPlanStatement) stmt;
        return explainStmt.execute(session, stratioStreamingAPI);
      } else if (stmt instanceof ListStatement) {
        ListStatement listStmt = (ListStatement) stmt;
        return listStmt.execute(queryId, stratioStreamingAPI);
      } else if (stmt instanceof StopProcessStatement) {
        StopProcessStatement stopStmt = (StopProcessStatement) stmt;
        return stopStmt.execute(stratioStreamingAPI);
      } else {
        return Result.createExecutionErrorResult("Command not supported yet.");
      }
    } catch (RuntimeException rex) {
      LOG.debug("Command executor failed", rex);
      return Result.createExecutionErrorResult(rex.getMessage());
    }
  }

}
