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

package com.stratio.meta.core.statements;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta.streaming.MetaStream;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.messages.StreamQuery;
import com.stratio.streaming.commons.streams.StratioStream;

import org.apache.log4j.Logger;

import java.util.List;

public class StopProcessStatement extends MetaStatement {

  private String queryId;
  private static final Logger LOG = Logger.getLogger(StopProcessStatement.class);

  public StopProcessStatement(String queryId) {
    this.command = true;
    this.queryId = queryId;
  }

  public String getQueryId() {
    return queryId;
  }

  public String getStream() {
    String StreamName="";
    try {
      IStratioStreamingAPI stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();
      List<StratioStream> streamsList = stratioStreamingAPI.listStreams();
      for (StratioStream stream : streamsList) {
        if (stream.getQueries().size() > 0) {
          for (StreamQuery query : stream.getQueries()) {
            if (queryId.contentEquals(query.getQueryId())){
              StreamName=stream.getStreamName();
            }
          }
        }
      }
    } catch (Exception e) {
      LOG.error(e);
    }
    return StreamName;
  }

  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Stop process ");
    sb.append(queryId);
    return sb.toString();
  }

  @Override
  public String translateToCQL() {
    return this.toString();
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    return new Tree(new MetaStep(MetaPath.COMMAND,this));
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    //TODO: Check user query identifier.
    //Result result= Result.createValidationErrorResult("UDF and TRIGGER not supported yet");

    //StratioStream stream = metadata.checkQuery(queryId);
    //if ( stream != null)   result = QueryResult.createSuccessQueryResult();

    return QueryResult.createSuccessQueryResult();
  }

  public Result execute(IStratioStreamingAPI stratioStreamingAPI) {
    return MetaStream.removeStreamingQuery(this.getQueryId(), stratioStreamingAPI);
  }
}
