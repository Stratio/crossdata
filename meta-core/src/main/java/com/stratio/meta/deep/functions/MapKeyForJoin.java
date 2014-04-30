/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.Serializable;


public class MapKeyForJoin<T> extends PairFunction<Cells, T, Cells> implements Serializable{

    private static final long serialVersionUID = -6677647619149716567L;

    private String key;

    /**
     * MapKeyForJoin maps a field in a Cell
     * @param key Field to map
     */
    public MapKeyForJoin(String key){
        this.key=key;
    }

    @Override
    public Tuple2<T, Cells> call(Cells cells){
        return new Tuple2<T, Cells>((T)cells.getCellByName(key).getCellValue(),cells);
    }
}
