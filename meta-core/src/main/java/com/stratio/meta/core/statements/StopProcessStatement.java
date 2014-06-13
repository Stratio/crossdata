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
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.messages.StreamQuery;
import com.stratio.streaming.commons.streams.StratioStream;

import org.apache.log4j.Logger;

import java.util.List;

public class StopProcessStatement extends MetaStatement {

  private String ident;
  private static final Logger LOG = Logger.getLogger(StopProcessStatement.class);

  public StopProcessStatement(String ident) {
    this.command = true;
    this.ident = ident;
  }

  public String getIdent() {
    return ident;
  }
  public String getStream() {
    String StreamName="";
    try {
      IStratioStreamingAPI stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();
      List<StratioStream> streamsList = stratioStreamingAPI.listStreams();
      for (StratioStream stream : streamsList) {
        if (stream.getQueries().size() > 0) {
          for (StreamQuery query : stream.getQueries()) {
            if (ident.contentEquals(query.getQueryId())){
              StreamName=stream.getStreamName();
            }
          }
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return StreamName;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Stop process ");
    sb.append(ident);
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
  public Result validate(MetadataManager metadata) {
    Result result= Result.createValidationErrorResult("UDF and TRIGGER not supported yet");

    try {
      IStratioStreamingAPI stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();
      List<StratioStream> streamsList = stratioStreamingAPI.listStreams();
      for (StratioStream stream : streamsList) {
        if (stream.getQueries().size() > 0) {
          for (StreamQuery query : stream.getQueries()) {
            if (ident.contentEquals(query.getQueryId())){
              result = QueryResult.createSuccessQueryResult();
            }
          }
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return result;
  }
}
