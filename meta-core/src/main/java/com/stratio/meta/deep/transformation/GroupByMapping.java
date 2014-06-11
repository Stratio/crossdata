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
import java.math.BigInteger;
import java.util.List;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.GroupBy;


public class GroupByMapping extends PairFunction<Cells, Cells, Cells> implements Serializable {

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
      if (aggCol.toLowerCase().equals("count(*)")) {
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

    for (GroupBy groupByCol : groupByClause) {

      String[] fieldParts = groupByCol.toString().split("\\.");
      grouppingKeys.add(cells.getCellByName(fieldParts[fieldParts.length - 1]));
    }

    return new Tuple2<>(grouppingKeys, cellsExtended);
  }
}
