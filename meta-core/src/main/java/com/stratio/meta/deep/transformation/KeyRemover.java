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

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

import com.stratio.deep.entity.Cells;


public class KeyRemover implements Function<Tuple2<Cells, Cells>, Cells> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 5540221408306143803L;

  @Override
  public Cells call(Tuple2<Cells, Cells> tuple) throws Exception {

    return tuple._2;
  }
}
