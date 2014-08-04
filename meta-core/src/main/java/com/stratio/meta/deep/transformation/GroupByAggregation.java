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

        Cell cellSumLeft =
            left.getCellByName(aggregation.getTable(), aggregation.getField() + "_sum");
        Cell cellSumRight =
            right.getCellByName(aggregation.getTable(), aggregation.getField() + "_sum");

        resultSumCell = GroupByAggregators.sum(cellSumLeft, cellSumRight);

        left.replaceByName(aggregation.getTable(), resultSumCell);

        // Count number of repetitions
        Cell resultCountCell = null;

        Cell cellCountLeft =
            left.getCellByName(aggregation.getTable(), aggregation.getField() + "_count");
        Cell cellCountRight =
            right.getCellByName(aggregation.getTable(), aggregation.getField() + "_count");

        resultCountCell = GroupByAggregators.count(cellCountLeft, cellCountRight);

        left.replaceByName(aggregation.getTable(), resultCountCell);
      }
    }

    return left;
  }
}
