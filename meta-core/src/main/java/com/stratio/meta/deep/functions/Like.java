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

public class Like extends Function<Cells, Boolean> implements Serializable{
    private static final long serialVersionUID = 5642510017426647895L;

    private String field;
    private String regexp;

    /**
     * Like filter
     * @param field Name of the field to check.
     * @param regexp Regular expresion that value must match with.
     */
    public Like(String field, String regexp){
        this.field=field;
        this.regexp=regexp;
    }

    //TODO Exception Management
    @Override
    public Boolean call(Cells cells){
        Object currentValue = cells.getCellByName(field).getCellValue();
        if (currentValue == null){
            return regexp == null;
        }
        return String.valueOf(currentValue).matches(regexp);
    }
}
