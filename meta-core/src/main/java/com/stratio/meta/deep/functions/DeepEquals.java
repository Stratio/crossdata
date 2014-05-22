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

import org.apache.spark.api.java.function.Function;

import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.Term;

public class DeepEquals extends Function<Cells, Boolean> implements Serializable {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -6143471452730703044L;

  /**
   * Term to compare.
   */
  private Term<?> term;

  /**
   * Name of the field of the cell to compare.
   */
  private String field;

  /**
   * DeepEquals apply = filter to a field in a Deep Cell.
   * 
   * @param field Name of the field to check.
   * @param term Term to compare to.
   */
  public DeepEquals(String field, Term<?> term) {
    this.term = term;
    this.field = field;
  }

  @Override
  public Boolean call(Cells cells) {
    Object currentValue = cells.getCellByName(field).getCellValue();
    return term.equals(currentValue);
  }
}
