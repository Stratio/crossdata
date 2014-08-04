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
    Double sumValue = new Double(sumCell.getCellValue().toString());
    Double countValue = ((BigInteger) countCell.getCellValue()).doubleValue();
    average = sumValue / countValue;

    resultCell = CassandraCell.create(averageColumnInfo.getColumnName(), average);

    // Removing temporary data
    row.remove(averageColumnInfo.getTable(), averageSumField);
    row.remove(averageColumnInfo.getTable(), averageCountField);

    // Adding average result
    row.add(averageColumnInfo.getTable(), resultCell);

    return row;
  }
}
