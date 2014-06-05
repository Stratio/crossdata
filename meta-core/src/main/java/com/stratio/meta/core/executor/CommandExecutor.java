/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.executor;

import org.apache.log4j.Logger;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.statements.ExplainPlanStatement;
import com.stratio.meta.core.statements.MetaStatement;

public class CommandExecutor {

  private static final Logger LOG = Logger.getLogger(CommandExecutor.class);

  /**
   * Private class constructor as all methods are static.
   */
  private CommandExecutor() {

  }

  /**
   * Execute a {@link com.stratio.meta.core.statements.MetaStatement} command.
   * 
   * @param stmt Statement to execute.
   * @param session Cassandra datastax java driver {@link com.datastax.driver.core.Session}.
   * @return a {@link com.stratio.meta.common.result.Result}.
   */
  public static Result execute(MetaStatement stmt, Session session) {
    try {
      if (stmt instanceof DescribeStatement) {
        DescribeStatement descStmt = (DescribeStatement) stmt;
        return descStmt.execute(session);
      } else if (stmt instanceof ExplainPlanStatement) {
        return executeExplainPlan((ExplainPlanStatement) stmt, session);
      } else {
        return CommandResult.createFailCommandResult("Command not supported yet.");
      }
    } catch (RuntimeException rex) {
      LOG.debug("Command executor failed", rex);
      return CommandResult.createFailCommandResult(rex.getMessage());
    }
  }

  private static Result executeExplainPlan(ExplainPlanStatement stmt, Session session) {
    return CommandResult.createSuccessCommandResult(stmt.getMetaStatement()
        .getPlan(new MetadataManager(session), stmt.getMetaStatement().getEffectiveKeyspace())
        .toStringDownTop());
  }
}
