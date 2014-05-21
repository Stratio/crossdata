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

package com.stratio.meta.core.structures;

public class IntegerTerm<T extends Number & Comparable<T>> extends Term<T> {

    private static final long serialVersionUID = 7097178218828822792L;

    private final T number;

    public IntegerTerm(String term) {
        super.clazz = (Class<T>) Long.class;
        number = (T) Long.valueOf(term);
    }

    /** {@inheritDoc} */
    @Override
    public T getTermValue() {
        return number;
    }

    @Override
    public String getStringValue() {
        return toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
