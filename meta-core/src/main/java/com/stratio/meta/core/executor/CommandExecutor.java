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

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.statements.ExplainPlanStatement;
import com.stratio.meta.core.statements.ListStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.StopProcessStatement;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioStreamingException;
import com.stratio.streaming.commons.messages.StreamQuery;
import com.stratio.streaming.commons.streams.StratioStream;

import org.apache.log4j.Logger;

import java.util.List;

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

      }else if (stmt instanceof ListStatement) {

        StringBuilder sb= new StringBuilder();

        try {
          IStratioStreamingAPI stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();
          List<StratioStream> streamsList = stratioStreamingAPI.listStreams();
          LOG.debug("Number of streams: " + streamsList.size());
          for (StratioStream stream : streamsList) {
            LOG.debug("--> Stream Name: " + stream.getStreamName());
            if (stream.getQueries().size() > 0) {
              for (StreamQuery query : stream.getQueries()) {
                sb.append(query.getQueryId());
                sb.append(", ").append(stream.getStreamName());
                sb.append(", ").append(query.getQuery()).append(System.lineSeparator());
                LOG.debug("Query: " + query.getQuery());
              }
            }
          }

        } catch (Throwable t) {

          t.printStackTrace();
        }

        return Result.createExecutionErrorResult(sb.toString());

      }else if (stmt instanceof StopProcessStatement){
        try{
          IStratioStreamingAPI stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();
          try {
            stratioStreamingAPI.removeQuery(((StopProcessStatement) stmt).getStream(), ((StopProcessStatement) stmt).getIdent());
          } catch(StratioStreamingException ssEx) {
            ssEx.printStackTrace();
          }
        }
        catch (Throwable t){
          t.printStackTrace();
        }
        return Result.createExecutionErrorResult(
            "delete " + ((StopProcessStatement) stmt).getIdent());



      }else {
        return Result.createExecutionErrorResult("Command not supported yet.");
      }
    } catch (RuntimeException rex){
      LOG.debug("Command executor failed", rex);
      return Result.createExecutionErrorResult(rex.getMessage());
    }

  }


  private static Result executeExplainPlan(ExplainPlanStatement stmt, Session session) {
    return CommandResult.createCommandResult(
        stmt.getMetaStatement()
            .getPlan(new MetadataManager(session, null), stmt.getMetaStatement().getEffectiveKeyspace())
            .toStringDownTop());
  }

  /**
   * Execute a {@link com.stratio.meta.core.statements.DescribeStatement}.
   *
   * @param dscrStatement Statement to execute.
   * @param session Cassandra datastax java driver {@link com.datastax.driver.core.Session}.
   * @return a {@link com.stratio.meta.common.result.Result}.
   */
  private static Result executeDescribe(DescribeStatement dscrStatement, Session session) {
    MetadataManager mm = new MetadataManager(session, null);
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
      TableMetadata
          tableInfo = mm.getTableMetadata(dscrStatement.getEffectiveKeyspace(), dscrStatement.getTableName());
      if (tableInfo == null) {
        errorMessage = "TABLE " + dscrStatement.getTableName() + " was not found";
      } else {
        info = tableInfo.exportAsString();
      }
    }
    if (info != null) {
      result = CommandResult.createCommandResult(info);
    } else {
      result = Result.createExecutionErrorResult(errorMessage);
    }
    return result;
  }

}
