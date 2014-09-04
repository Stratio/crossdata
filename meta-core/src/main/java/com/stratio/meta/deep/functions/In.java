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

import com.stratio.deep.entity.Cells;
import com.stratio.meta2.common.statements.structures.selectors.Selector;

import org.apache.spark.api.java.function.Function;

import java.util.List;

public class In implements Function<Cells, Boolean> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -6637139616271541577L;

  /**
   * Name of the field of the cell to compare.
   */
  private String field;

  /**
   * IDs in the IN clause.
   */
  private List<Selector> terms;

  /**
   * In apply in filter to a field in a Deep Cell.
   * 
   * @param field Name of the field to check.
   * @param terms List of terms of the IN clause.
   */
  public In(String field, List<Selector> terms) {
    this.field = field;
    this.terms = terms;
  }

  @Override
  public Boolean call(Cells cells) {

    Boolean isValid = false;
    Object cellValue = cells.getCellByName(field).getCellValue();

    /*Class<?> dataType = (Class<?>) terms.get(0).getTermClass();
    Object currentValue = dataType.cast(cellValue);
    if (currentValue != null) {
      isValid = isIncludedInList(terms, currentValue);
    }*/

    return isValid;
  }

  private Boolean isIncludedInList(List<Selector> list, Object value) {
    for (Selector term : list) {
      /*if (term.getTermValue().equals(value))
        return true;*/
    }

    return false;
  }
}
