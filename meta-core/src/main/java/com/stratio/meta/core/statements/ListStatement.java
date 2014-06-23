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
import com.stratio.meta.core.structures.ListType;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta.streaming.MetaStream;
import com.stratio.streaming.api.IStratioStreamingAPI;

import org.apache.log4j.Logger;

/**
 * Class that models a {@code LIST} statement from the META language.
 */
public class ListStatement extends MetaStatement {

  private static final Logger LOG = Logger.getLogger(ListStatement.class);
  /**
     * The {@link com.stratio.meta.core.structures.ListType} to be executed.
     */
    private ListType type = null;

    /**
     * Class constructor.
     * @param type The {@link com.stratio.meta.core.structures.ListType} to be executed.
     */
    public ListStatement(String type){
        this.command = true;
        this.type = ListType.valueOf(type.toUpperCase());
    }

    @Override
    public String toString() {
            return "LIST " + type;
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

    Result result = QueryResult.createSuccessQueryResult();
    if (type.equals(ListType.TRIGGER)||type.equals(ListType.UDF)){
      result= Result.createValidationErrorResult("UDF and TRIGGER not supported yet");
    }

    return result;
  }

  public Result execute(String queryId, IStratioStreamingAPI stratioStreamingAPI) {
    /*List<StratioStream> streamsList = null;
    try {
      streamsList = stratioStreamingAPI.listStreams();
    } catch (Exception e) {
      e.printStackTrace();
    }
    LOG.debug("Number of streams: " + streamsList.size());
    StringBuilder sb = new StringBuilder();
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
    return CommandResult.createCommandResult(sb.toString());
    */
    return MetaStream.listStreamingQueries(queryId, stratioStreamingAPI);
  }
}
