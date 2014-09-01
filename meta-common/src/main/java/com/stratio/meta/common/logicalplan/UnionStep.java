/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.common.logicalplan;

import com.stratio.meta.common.connector.Operations;

import java.util.List;

/**
 * Class definition for {@link com.stratio.meta.common.logicalplan.LogicalStep} that need
 * to work with the results of several previous logical steps. Examples of this type of
 * operators are Join and Union.
 */
public class UnionStep extends LogicalStep{

  private List<LogicalStep> previousSteps;

  /**
   * Class constructor.
   * @param operation The operation to be applied.
   */
  public UnionStep(Operations operation) {
    super(operation);
  }

  public void setPreviousSteps(List<LogicalStep> previousSteps) {
    this.previousSteps = previousSteps;
  }

  @Override
  public List<LogicalStep> getPreviousSteps() {
    return previousSteps;
  }

  @Override
  public LogicalStep getFirstPrevious() {
    LogicalStep result = null;
    if(previousSteps!=null){
      result = previousSteps.get(0);
    }
    return result;
  }
}
