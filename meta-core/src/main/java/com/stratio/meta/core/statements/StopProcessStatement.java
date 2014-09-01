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

package com.stratio.meta.core.statements;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta.streaming.MetaStream;
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
  public String translateToCQL(MetadataManager metadataManager) {
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
