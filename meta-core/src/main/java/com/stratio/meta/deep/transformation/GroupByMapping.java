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

import java.math.BigInteger;
import java.util.List;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.stratio.deep.entity.CassandraCell;
import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.core.structures.GroupByFunction;
import com.stratio.meta.deep.transfer.ColumnInfo;

public class GroupByMapping implements PairFunction<Cells, Cells, Cells> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -2763959543919248527L;

  private List<ColumnInfo> aggregationCols;

  private List<GroupBy> groupByClause;

  public GroupByMapping(List<ColumnInfo> aggregationCols, List<GroupBy> groupByClause) {
    this.aggregationCols = aggregationCols;
    this.groupByClause = groupByClause;
  }

  @Override
  public Tuple2<Cells, Cells> call(Cells cells) throws Exception {

    Cells grouppingKeys = new Cells();
    Cells cellsExtended = cells;
    // Copying aggregation columns to not apply the function over the original data
    for (ColumnInfo aggCol : aggregationCols) {
      if (GroupByFunction.COUNT == aggCol.getAggregationFunction()) {
        cellsExtended.add(CassandraCell.create(aggCol.getColumnName(), new BigInteger("1")));
      } else if (GroupByFunction.AVG == aggCol.getAggregationFunction()) {
        com.stratio.deep.entity.Cell cellToCopy =
            cells.getCellByName(aggCol.getTable(), aggCol.getField());
        cellsExtended.add(aggCol.getTable(),
            CassandraCell.create(aggCol.getField() + "_count", new BigInteger("1")));
        cellsExtended.add(aggCol.getTable(),
            CassandraCell.create(aggCol.getField() + "_sum", cellToCopy.getCellValue()));
      } else {
        com.stratio.deep.entity.Cell cellToCopy =
            cells.getCellByName(aggCol.getTable(), aggCol.getField());
        cellsExtended.add(aggCol.getTable(),
            CassandraCell.create(aggCol.getColumnName(), cellToCopy.getCellValue()));
      }
    }

    if (groupByClause != null) {
      for (GroupBy groupByCol : groupByClause) {

        Cell cell =
            cells.getCellByName(groupByCol.getSelectorIdentifier().getTable(), groupByCol
                .getSelectorIdentifier().getField());
        grouppingKeys.add(cell);
      }
    } else {
      grouppingKeys.add(CassandraCell.create("_", "_"));
    }

    return new Tuple2<>(grouppingKeys, cellsExtended);
  }
}
