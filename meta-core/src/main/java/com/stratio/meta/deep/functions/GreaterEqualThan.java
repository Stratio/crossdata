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
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

public class GreaterEqualThan extends Function<Cells, Boolean> implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2927596112428729111L;

    /**
     * Value to compare.
     */
    private Object value;

    /**
     * Name of the field of the cell to compare.
     */
    private String field;

    /**
     * GreaterEqualThan apply >= filter to a field in a Deep Cell.
     * @param field Name of the field to check.
     * @param value Value to compare to.
     */
    public GreaterEqualThan(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells){
        Object currentValue = cells.getCellByName(field).getCellValue();
        return ((Comparable) value).compareTo(currentValue) >= 0;
    }
}
