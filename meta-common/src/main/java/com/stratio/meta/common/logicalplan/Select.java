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

import java.util.Map;

/**
 * Select operator that specifies the list of columns that should be
 * returned with their expected name. Notice that this operator is applied
 * to limit the number of columns returned and to provide alias support.
 */
public class Select extends TransformationStep{

  /**
   * Map of columns associating the name given in the Project logical steps
   * with the name expected in the results.
   */
  private final Map<String, String> columnMap;

  /**
   * Class constructor.
   *
   * @param operation The operation to be applied.
   * @param columnMap Map of columns associating the name given in the Project
   *                  logical steps with the name expected in the result.
   */
  public Select(Operations operation, Map<String, String> columnMap) {
    super(operation);
    this.columnMap = columnMap;
  }

  public Map<String, String> getColumnMap() {
    return columnMap;
  }
}
