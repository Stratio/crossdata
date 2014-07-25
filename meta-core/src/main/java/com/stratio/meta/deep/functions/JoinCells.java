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

package com.stratio.meta.deep.functions;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

import com.stratio.deep.entity.Cells;

public class JoinCells<T> implements Function<Tuple2<T, Tuple2<Cells, Cells>>, Cells> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 4534397129761833793L;

  @Override
  public Cells call(Tuple2<T, Tuple2<Cells, Cells>> result) {
    Cells left = result._2()._1();
    Cells right = result._2()._2();
    Cells joinedCells = new Cells();

    joinedCells.addAll(left.getInternalCells());
    joinedCells.addAll(right.getInternalCells());

    return joinedCells;
  }
}
