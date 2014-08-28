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

package com.stratio.meta.common.connector;

import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta2.common.data.ClusterName;

/**
 * Interface provided by a connector to access query related operations such as retrieving as set of
 * results.
 */
public interface IQueryEngine {

  /**
   * Execute a workflow to retrieve a subset of data.
   *
   * @param targetCluster Target cluster.
   * @param workflow      The {@link com.stratio.meta.common.logicalplan.LogicalWorkflow} that
   *                      contains the {@link com.stratio.meta.common.logicalplan.LogicalStep} to be
   *                      executed.
   * @return A {@link com.stratio.meta.common.result.QueryResult}.
   * @throws UnsupportedException If the required set of operations are not supported by the
   *                              connector.
   * @throws ExecutionException   If the execution of the required steps fails.
   */
  public QueryResult execute(ClusterName targetCluster, LogicalWorkflow workflow)
      throws UnsupportedException,
             ExecutionException;

}
