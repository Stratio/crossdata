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

package com.stratio.meta.deep.functions.aggregators;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.spark.api.java.function.Function2;

import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;

public class Sum extends Function2<Cells, Cells, Cells> implements Serializable {

  private static final long serialVersionUID = 4481367967595656047L;

  /**
   * Name of the field of the cell to compare.
   */
  private String field;

  /**
   * In apply in filter to a field in a Deep Cell.
   * 
   * @param field Name of the field to check.
   * @param inIDs List of values of the IN clause.
   */
  public Sum(String field) {
    this.field = field;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.spark.api.java.function.WrappedFunction2#call(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public Cells call(Cells row1, Cells row2) throws Exception {

    Cell cell1 = row1.getCellByName(field);
    Cell cell2 = row2.getCellByName(field);

    Cell resultCell = null;
    if (cell1.getValueType().isInstance(Integer.class)
        || cell1.getValueType().isInstance(Long.class)
        || cell1.getValueType().isInstance(BigInteger.class)) {
      BigInteger resultValue =
          ((BigInteger) cell1.getCellValue()).add((BigInteger) cell2.getCellValue());
      resultCell = Cell.create(field, resultValue);
    } else {
      BigDecimal resultValue =
          ((BigDecimal) cell1.getCellValue()).add((BigDecimal) cell2.getCellValue());
      resultCell = Cell.create(field, resultValue);
    }

    row1.add(resultCell);

    return row1;
  }
}
