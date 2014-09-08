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

package com.stratio.meta2.core.statements;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.ListType;
import com.stratio.meta.streaming.MetaStream;
import com.stratio.meta2.core.validator.ValidationRequirements;

import org.apache.log4j.Logger;

/**
 * Class that models a {@code LIST} statement from the META language.
 */
public class ListStatement extends MetaDataStatement {

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
  public Result validate(MetadataManager metadata, EngineConfig config) {

    Result result = QueryResult.createSuccessQueryResult();
    if (type.equals(ListType.TRIGGER)||type.equals(ListType.UDF)){
      result= Result.createValidationErrorResult("UDF and TRIGGER not supported yet");
    }

    return result;
  }

  public Result execute(String queryId) {
    return MetaStream.listStreamingQueries(queryId, null);
  }
  @Override
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements();
  }
}
