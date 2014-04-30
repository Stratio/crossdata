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

import java.util.Iterator;

public abstract class ResultSet implements Iterable<Row> {

    /**
     * Get a Iterator over the ResultSet.
     *
     * @return Iterator
     */
    public abstract Iterator<Row> iterator();

    /**
     * Get the size of ResultSet.
     *
     * @return the size of ResultSet
     */
    public abstract int size();

    /**
     * Check if ResultSet is empty.
     *
     * @return {@code true} if is empty
     */
    public boolean isEmpty(){
        return size() < 1;
    }
}
