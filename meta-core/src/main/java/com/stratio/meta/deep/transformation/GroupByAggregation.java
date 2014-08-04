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
import com.stratio.meta.deep.utils.GroupByAggregators;


public class GroupByAggregation implements Function2<Cells, Cells, Cells> {

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
