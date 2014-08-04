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

package com.stratio.meta.deep.functions;

import org.apache.spark.api.java.function.Function;

import com.stratio.deep.entity.Cells;

public class Like implements Function<Cells, Boolean> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 5642510017426647895L;

  /**
   * Name of the field of the cell to match.
   */
  private String field;

  /**
   * Regular expression.
   */
  private String regexp;

  /**
   * Like filter.
   * 
   * @param field Name of the field to check.
   * @param regexp Regular expresion that value must match with.
   */
  public Like(String field, String regexp) {
    this.field = field;
    this.regexp = regexp;
  }

  // TODO Exception Management
  @Override
  public Boolean call(Cells cells) {
    Object currentValue = cells.getCellByName(field).getCellValue();
    return regexp.matches(String.valueOf(currentValue));
  }
}
