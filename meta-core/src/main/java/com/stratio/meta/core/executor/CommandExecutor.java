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

package com.stratio.meta.core.executor;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.statements.ExplainPlanStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.structures.DescribeType;
import org.apache.log4j.Logger;

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
                return executeDescribe((DescribeStatement) stmt, session);
            } else if(stmt instanceof ExplainPlanStatement) {
                return executeExplainPlan((ExplainPlanStatement) stmt, session);
            } else {
                return CommandResult.createFailCommandResult("Command not supported yet.");
            }
        } catch (RuntimeException rex){
            LOG.debug("Command executor failed", rex);
            return CommandResult.createFailCommandResult(rex.getMessage());
        }
    }

    private static Result executeExplainPlan(ExplainPlanStatement stmt, Session session) {
        return CommandResult.createSuccessCommandResult(
                stmt.getMetaStatement().getPlan(new MetadataManager(session), stmt.getMetaStatement().getEffectiveKeyspace()).toStringDownTop());
    }

    /**
     * Execute a {@link com.stratio.meta.core.statements.DescribeStatement}.
     *
     * @param dscrStatement Statement to execute.
     * @param session Cassandra datastax java driver {@link com.datastax.driver.core.Session}.
     * @return a {@link com.stratio.meta.common.result.Result}.
     */
    private static Result executeDescribe(DescribeStatement dscrStatement, Session session) {
        MetadataManager mm = new MetadataManager(session);
        mm.loadMetadata();
        Result result = null;
        String info = null;
        String errorMessage = null;
        if (dscrStatement.getType() == DescribeType.KEYSPACE) { // KEYSPACE
            KeyspaceMetadata ksInfo = mm.getKeyspaceMetadata(dscrStatement.getKeyspace());
            if (ksInfo == null) {
                errorMessage = "KEYSPACE " + dscrStatement.getKeyspace() + " was not found";
            } else {
                info = ksInfo.exportAsString();
            }
        } else { // TABLE
            TableMetadata tableInfo = mm.getTableMetadata(dscrStatement.getEffectiveKeyspace(), dscrStatement.getTableName());
            if (tableInfo == null) {
                errorMessage = "TABLE " + dscrStatement.getTableName() + " was not found";
            } else {
                info = tableInfo.exportAsString();
            }
        }
        if (info != null) {
            result = CommandResult.createSuccessCommandResult(info);
        } else {
            result = CommandResult.createFailCommandResult(errorMessage);
        }
        return result;
    }
}
