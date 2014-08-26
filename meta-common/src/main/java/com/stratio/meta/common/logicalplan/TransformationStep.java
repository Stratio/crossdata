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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class definition for {@link com.stratio.meta.common.logicalplan.LogicalStep} that are
 * preceeded by a single logical step. Examples of this type of operators are PROJECT and FILTER.
 */
public class TransformationStep extends LogicalStep{

  /**
   * Single previous step.
   */
  private LogicalStep previous;

  @Override
  public List<LogicalStep> getPreviousSteps() {
    return Arrays.asList(previous);
  }

  @Override
  public LogicalStep getFirstPrevious() {
    return null;
  }
}
