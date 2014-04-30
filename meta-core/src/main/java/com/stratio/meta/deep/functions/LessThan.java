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

public class LessThan extends Function<Cells, Boolean> implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2675616112608139116L;

    /**
     * Value to compare.
     */
    private Object value;

    /**
     * Name of the field of the cell to compare.
     */
    private String field;

    /**
     * LessEqualThan apply > filter to a field in a Deep Cell.
     * @param field Name of the field to check.
     * @param value Value to compare to.
     */
    public LessThan(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells){
        Object obj = cells.getCellByName(field).getCellValue();
        return ((Comparable)obj).compareTo(value) < 0;
    }
}
