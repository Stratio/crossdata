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

import org.apache.spark.api.java.function.Function;

import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.Term;

public class LessThan implements Function<Cells, Boolean> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 2675616112608139116L;

  /**
   * Term to compare.
   */
  private Term<?> term;

  /**
   * Name of the field of the cell to compare.
   */
  private String field;

  /**
   * LessEqualThan apply > filter to a field in a Deep Cell.
   * 
   * @param field Name of the field to check.
   * @param term Term to compare to.
   */
  public LessThan(String field, Term<?> term) {
    this.term = term;
    this.field = field;
  }

  @Override
  public Boolean call(Cells cells) {
    Object obj = cells.getCellByName(field).getCellValue();
    return ((Comparable) term).compareTo(obj) > 0;
  }
}
