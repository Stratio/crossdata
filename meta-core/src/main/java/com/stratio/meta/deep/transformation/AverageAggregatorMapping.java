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
import com.stratio.meta.deep.transfer.ColumnInfo;


public class AverageAggregatorMapping implements Function<Cells, Cells> {

  private static final long serialVersionUID = -1708746004449933719L;

  private String averageSumField;

  private String averageCountField;

  private ColumnInfo averageColumnInfo;

  public AverageAggregatorMapping(ColumnInfo averageColumnInfo) {
    this.averageColumnInfo = averageColumnInfo;
    this.averageSumField = averageColumnInfo.getField() + "_sum";
    this.averageCountField = averageColumnInfo.getField() + "_count";
  }

  @Override
  public Cells call(Cells row) throws Exception {

    CassandraCell sumCell =
        (CassandraCell) row.getCellByName(averageColumnInfo.getTable(), averageSumField);
    CassandraCell countCell =
        (CassandraCell) row.getCellByName(averageColumnInfo.getTable(), averageCountField);

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

    resultCell = CassandraCell.create(averageColumnInfo.getColumnName(), average);

    // Removing temporary data
    row.remove(averageColumnInfo.getTable(), averageSumField);
    row.remove(averageColumnInfo.getTable(), averageCountField);

    // Adding average result
    row.add(averageColumnInfo.getTable(), resultCell);

    return row;
  }
}
