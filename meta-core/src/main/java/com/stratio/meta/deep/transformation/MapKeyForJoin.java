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

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.stratio.deep.entity.Cells;

public class MapKeyForJoin<T> implements PairFunction<Cells, T, Cells> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -6677647619149716567L;

  /**
   * Map key.
   */
  private String key;

  /**
   * MapKeyForJoin maps a field in a Cell.
   * 
   * @param key Field to map
   */
  public MapKeyForJoin(String key) {
    this.key = key;
  }

  @Override
  public Tuple2<T, Cells> call(Cells cells) {
    return new Tuple2<>((T) cells.getCellByName(key).getCellValue(), cells);
  }
}
