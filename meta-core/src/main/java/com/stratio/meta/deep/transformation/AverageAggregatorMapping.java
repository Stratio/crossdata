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

package com.stratio.meta.deep.transformation;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.spark.api.java.function.Function;

import com.stratio.deep.entity.CassandraCell;
import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;


public class AverageAggregatorMapping implements Function<Cells, Cells> {

  private static final long serialVersionUID = -1708746004449933719L;

  private String averageSumField;

  private String averageCountField;

  private String averageField;

  public AverageAggregatorMapping(String averageSelector) {
    this.averageField = averageSelector;
    this.averageSumField = averageSelector + "_sum";
    this.averageCountField = averageSelector + "_count";
  }

  @Override
  public Cells call(Cells row) throws Exception {

    Cell sumCell = row.getCellByName(averageSumField);
    Cell countCell = row.getCellByName(averageCountField);

    Cell resultCell = null;
    double average;

    // Calculating the row average for the requested field
    Class<?> valueType = sumCell.getValueType();
    if (valueType.equals(Integer.class) || valueType.equals(Long.class)
        || valueType.equals(BigInteger.class)) {
      average =
          ((BigInteger) sumCell.getCellValue()).doubleValue()
              / ((BigInteger) countCell.getCellValue()).doubleValue();
    } else {
      average =
          ((BigDecimal) sumCell.getCellValue()).doubleValue()
              / ((BigDecimal) countCell.getCellValue()).doubleValue();
    }

    resultCell = CassandraCell.create(averageField, average);

    // Removing temporary data
    row.remove(averageSumField);
    row.remove(averageCountField);

    // Adding average result
    row.add(resultCell);

    return row;
  }
}
