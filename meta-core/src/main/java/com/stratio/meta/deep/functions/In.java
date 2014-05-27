/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.deep.functions;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.function.Function;

import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.Term;

public class In extends Function<Cells, Boolean> implements Serializable {

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
  private List<Term<?>> terms;

  /**
   * In apply in filter to a field in a Deep Cell.
   * 
   * @param field Name of the field to check.
   * @param terms List of terms of the IN clause.
   */
  public In(String field, List<Term<?>> terms) {
    this.field = field;
    this.terms = terms;
  }

  @Override
  public Boolean call(Cells cells) {

    Boolean isValid = false;
    Object cellValue = cells.getCellByName(field).getCellValue();

    Class<?> dataType = (Class<?>) terms.get(0).getTermClass();
    Object currentValue = dataType.cast(cellValue);
    if (currentValue != null) {
      isValid = isIncludedInList(terms, currentValue);
    }

    return isValid;
  }

  private Boolean isIncludedInList(List<Term<?>> list, Object value) {

    for (Term<?> term : list) {
      if (term.getTermValue().equals(value))
        return true;
    }

    return false;
  }
}
