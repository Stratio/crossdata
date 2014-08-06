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

import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.GroupBy;

import org.apache.spark.api.java.function.PairFunction;

import java.math.BigInteger;
import java.util.List;

import scala.Tuple2;


public class GroupByMapping implements PairFunction<Cells, Cells, Cells> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -2763959543919248527L;

  private List<String> aggregationCols;

  private List<GroupBy> groupByClause;

  public GroupByMapping(List<String> aggregationCols, List<GroupBy> groupByClause) {
    this.aggregationCols = aggregationCols;
    this.groupByClause = groupByClause;
  }

  @Override
  public Tuple2<Cells, Cells> call(Cells cells) throws Exception {

    Cells grouppingKeys = new Cells();
    Cells cellsExtended = cells;
    // Copying aggregation columns to not apply the function over the original data
    for (String aggCol : aggregationCols) {
      if ("count(*)".equalsIgnoreCase(aggCol)) {
        cellsExtended.add(com.stratio.deep.entity.Cell.create(aggCol, new BigInteger("1")));
      } else if (aggCol.toLowerCase().startsWith("avg(")) {
        String fieldName = aggCol.substring(aggCol.indexOf("(") + 1, aggCol.indexOf(")"));
        com.stratio.deep.entity.Cell cellToCopy = cells.getCellByName(fieldName);
        cellsExtended.add(com.stratio.deep.entity.Cell.create(aggCol + "_count",
            new BigInteger("1")));
        cellsExtended.add(com.stratio.deep.entity.Cell.create(aggCol + "_sum",
            cellToCopy.getCellValue()));
      } else {
        String fieldName = aggCol.substring(aggCol.indexOf("(") + 1, aggCol.indexOf(")"));
        com.stratio.deep.entity.Cell cellToCopy = cells.getCellByName(fieldName);
        cellsExtended.add(com.stratio.deep.entity.Cell.create(aggCol, cellToCopy.getCellValue()));
      }
    }

    if (groupByClause != null) {
      for (GroupBy groupByCol : groupByClause) {

        String[] fieldParts = groupByCol.toString().split("\\.");
        grouppingKeys.add(cells.getCellByName(fieldParts[fieldParts.length - 1]));
      }
    } else {
      grouppingKeys.add(Cell.create("_", "_"));
    }

    return new Tuple2<>(grouppingKeys, cellsExtended);
  }
}
