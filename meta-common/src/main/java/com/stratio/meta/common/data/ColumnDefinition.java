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

package com.stratio.meta.common.data;

import java.io.Serializable;

public class ColumnDefinition implements Serializable {

    private static final long serialVersionUID = 4958734787690152927L;

    private Class<?> datatype;

    public ColumnDefinition() {
        this.datatype = null;
    }

    /**
     * Constructor
     *
     * @param datatype Class of the value
     */
    public ColumnDefinition(Class<?> datatype) {
        this.datatype = datatype;
    }

    /**
     * Get the datatype of the cell.
     *
     * @return Datatype of the cell.
     */
    public Class<?> getDatatype() {
        return datatype;
    }

    public void setDatatype(Class<?> datatype) {
        this.datatype = datatype;
    }
}
