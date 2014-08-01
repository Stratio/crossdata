package com.stratio.meta.rest.utils;

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


import java.util.Iterator;
import java.util.NoSuchElementException;

import com.stratio.meta.rest.models.JsonMetaResultSet;
import com.stratio.meta.rest.models.JsonRow;

public class CResultSetIteratorJson implements Iterator<JsonRow> {

    /**
     * Set representing a result from Cassandra.
     */
    private final JsonMetaResultSet cResultSet;

    /**
     * Pointer to the current element.
     */
    private int current;

    /**
     * Build a {@link com.stratio.meta.common.data.CResultSetIterator} from a {@link com.stratio.meta.common.data.MetaResultSet}.
     * @param cResultSet Cassandra Result Set.
     */
    public CResultSetIteratorJson(JsonMetaResultSet cResultSet) {
        this.cResultSet = cResultSet;
        this.current = 0;
    }

    @Override
    public boolean hasNext() {
        return current < cResultSet.getRows().size();
    }

    @Override
    public JsonRow next() throws NoSuchElementException{
        return cResultSet.getRows().get(current++);
    }

    @Override
    public void remove() throws UnsupportedOperationException, IllegalStateException{
        cResultSet.remove(current);
    }
}
