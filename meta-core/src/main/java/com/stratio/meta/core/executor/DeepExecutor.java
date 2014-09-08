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

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.deep.Bridge;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta2.core.statements.SelectStatement;

import org.apache.log4j.Logger;

import java.util.List;

public class DeepExecutor {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(DeepExecutor.class);

  /**
   * Private class constructor as all methods are static.
   */
  private DeepExecutor(){

  }

  /**
   * Executes a statement in Spark using Deep.
   * @param stmt {@link com.stratio.meta2.core.statements.MetaStatement}
   * @param resultsFromChildren List of {@link com.stratio.meta.common.result.Result} of children.
   * @param isRoot Indicates if these node is root.
   * @param deepSparkContext Spark context from Deep
   * @param engineConfig The {@link com.stratio.meta.core.engine.EngineConfig}.
   * @return a {@link com.stratio.meta.common.result.Result} of execution in Spark.
   */
  public static Result execute(MetaStatement stmt,
                               List<Result> resultsFromChildren,
                               boolean isRoot,
                               DeepSparkContext deepSparkContext,
                               EngineConfig engineConfig) {

    if (stmt instanceof SelectStatement) {
      SelectStatement ss = (SelectStatement) stmt;
      Bridge bridge = new Bridge(deepSparkContext, engineConfig);
      ResultSet resultSet;
      try {
        resultSet = bridge.execute(ss, resultsFromChildren, isRoot);
      } catch(Exception ex){
        LOG.error("Spark exception", ex);
        return Result.createExecutionErrorResult("Spark exception: " +
                                                 System.lineSeparator()+ex.getMessage());
      }
      return QueryResult.createQueryResult(resultSet);
    } else {
      return Result.createExecutionErrorResult("Statement " + stmt + " not supported by deep");
    }
  }
}
