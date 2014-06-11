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

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.function.Function2;

import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;
import com.stratio.meta.deep.utils.GroupByAggregators;


public class GroupByAggregation extends Function2<Cells, Cells, Cells> implements Serializable {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -4041834957068048461L;

  private List<String> aggregationCols;

  public GroupByAggregation(List<String> aggregationCols) {
    this.aggregationCols = aggregationCols;
  }

  @Override
  public Cells call(Cells left, Cells right) throws Exception {

    for (String aggregation : aggregationCols) {

      String[] pieces = aggregation.split("\\(");
      String function = pieces[0].toLowerCase();

      if (!"avg".equalsIgnoreCase(function)) {

        Cell resultCell = null;

        Cell cellLeft = left.getCellByName(aggregation);
        Cell cellRight = right.getCellByName(aggregation);

        switch (function) {
          case "sum":
            resultCell = GroupByAggregators.sum(cellLeft, cellRight);
            break;
          case "count":
            resultCell = GroupByAggregators.count(cellLeft, cellRight);
            break;
          case "max":
            resultCell = GroupByAggregators.max(cellLeft, cellRight);
            break;
          case "min":
            resultCell = GroupByAggregators.min(cellLeft, cellRight);
            break;
          default:
            break;
        }

        left.replaceByName(resultCell);
      } else {

        // Sum column
        Cell resultSumCell = null;

        Cell cellSumLeft = left.getCellByName(aggregation + "_sum");
        Cell cellSumRight = right.getCellByName(aggregation + "_sum");

        resultSumCell = GroupByAggregators.sum(cellSumLeft, cellSumRight);

        left.replaceByName(resultSumCell);

        // Count number of repetitions
        Cell resultCountCell = null;

        Cell cellCountLeft = left.getCellByName(aggregation + "_count");
        Cell cellCountRight = right.getCellByName(aggregation + "_count");

        resultCountCell = GroupByAggregators.count(cellCountLeft, cellCountRight);

        left.replaceByName(resultCountCell);
      }
    }

    return left;
  }
}
