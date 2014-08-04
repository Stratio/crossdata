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

package com.stratio.meta.deep.transformation;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.spark.api.java.function.Function;

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

    resultCell = Cell.create(averageField, average);

    // Removing temporary data
    row.remove(averageSumField);
    row.remove(averageCountField);

    // Adding average result
    row.add(resultCell);

    return row;
  }
}
