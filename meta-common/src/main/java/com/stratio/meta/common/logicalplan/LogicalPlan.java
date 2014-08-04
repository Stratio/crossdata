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

package com.stratio.meta.common.logicalplan;

import java.util.List;

/**
 * Logical Plan that contains the decomposition of the task that need to be executed
 * at connector level to retrieve the required data. The basic operations contained in
 * a logical plan are:
 * <ul>
 *   <li>Project: Retrieve a set of columns from a table.</li>
 *   <li>Filter: Apply a clause to filter data.</li>
 *   <li>Window: The window to be applied on streaming sources.</li>
 * </ul>
 */
public class LogicalPlan {

  /**
   * List with the steps in order.
   */
  private final List<LogicalStep> stepList;

  /**
   * Logical plan constructor.
   * @param stepList
   */
  public LogicalPlan(List<LogicalStep> stepList) {
    this.stepList = stepList;
  }

  /**
   * Get the list of steps.
   * @return A list of {@link com.stratio.meta.common.logicalplan.LogicalStep}.
   */
  public List<LogicalStep> getStepList(){
    return this.stepList;
  }
}
