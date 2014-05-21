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

public class FloatingTerm<T extends Number & Comparable<T>> extends Term<T> {

    private static final long serialVersionUID = -578510540271635667L;

    private final T number;

    public FloatingTerm(String term) {
        super.clazz = (Class<T>) Double.class;
        this.number = (T) Double.valueOf(term);
    }

    /**
     * Get the term value.
     * 
     * @return A {@link Object} with the value.
     */
    @Override
    public T getTermValue() {
        return number;
    }

    /**
     * Get the String value representation.
     * 
     * @return The String value.
     */
    @Override
    public String getStringValue() {
        return toString();
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
