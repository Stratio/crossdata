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

import java.io.Serializable;

public abstract class Term<T extends Comparable<T>> extends ValueCell<T>
        implements Comparable<T>, Serializable {

    private static final long serialVersionUID = -4258938152892510227L;

    protected Class<T> clazz;
    protected T value;

    /**
     * Get the Term Java Class.
     * 
     * @return A {@link java.lang.Class}.
     */
    public Class<?> getTermClass() {
        return clazz;
    }

    public void setTermClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Get the term value.
     * 
     * @return A {@link java.lang.Object} with the value.
     */
    public abstract T getTermValue();

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(T o) {
        return this.value.compareTo(o);
    }

    /**
     * Returns a hash code value for the object. This method is supported for
     * the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * 
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return clazz.hashCode() * this.getTermValue().hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p/>
     * The {@code equals} method implements an equivalence relation on non-null
     * object references:
     * 
     * @param obj
     *            the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
     * @see #hashCode()
     * @see java.util.HashMap
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Term)) {
            return false;
        }

        Term other = (Term) obj;
        if (other.getClass() != this.getClass()) {
            return false;
        }

        if (other.clazz != this.clazz) {
            return false;
        }

        return other.getTermValue().equals(this.getTermValue());
    }
}
