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

import java.util.List;

import org.apache.spark.api.java.function.Function2;

import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.GroupByFunction;
import com.stratio.meta.deep.transfer.ColumnInfo;
import com.stratio.meta.deep.utils.GroupByAggregators;


public class GroupByAggregation implements Function2<Cells, Cells, Cells> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -4041834957068048461L;

  private List<ColumnInfo> aggregationCols;

  public GroupByAggregation(List<ColumnInfo> aggregationCols) {
    this.aggregationCols = aggregationCols;
  }

  @Override
  public Cells call(Cells left, Cells right) throws Exception {

    for (ColumnInfo aggregation : aggregationCols) {

      if (GroupByFunction.AVG != aggregation.getAggregationFunction()) {

        Cell resultCell = null;

        Cell cellLeft = left.getCellByName(aggregation.getTable(), aggregation.getColumnName());
        Cell cellRight = right.getCellByName(aggregation.getTable(), aggregation.getColumnName());

        switch (aggregation.getAggregationFunction()) {
          case SUM:
            resultCell = GroupByAggregators.sum(cellLeft, cellRight);
            break;
          case COUNT:
            resultCell = GroupByAggregators.count(cellLeft, cellRight);
            break;
          case MAX:
            resultCell = GroupByAggregators.max(cellLeft, cellRight);
            break;
          case MIN:
            resultCell = GroupByAggregators.min(cellLeft, cellRight);
            break;
          default:
            break;
        }

        left.replaceByName(aggregation.getTable(), resultCell);
      } else {

        // Sum column
        Cell resultSumCell = null;

        Cell cellSumLeft = left.getCellByName(aggregation.getField() + "_sum");
        Cell cellSumRight = right.getCellByName(aggregation.getField() + "_sum");

        resultSumCell = GroupByAggregators.sum(cellSumLeft, cellSumRight);

        left.replaceByName(aggregation.getTable(), resultSumCell);

        // Count number of repetitions
        Cell resultCountCell = null;

        Cell cellCountLeft = left.getCellByName(aggregation.getField() + "_count");
        Cell cellCountRight = right.getCellByName(aggregation.getField() + "_count");

        resultCountCell = GroupByAggregators.count(cellCountLeft, cellCountRight);

        left.replaceByName(aggregation.getTable(), resultCountCell);
      }
    }

    return left;
  }
}
