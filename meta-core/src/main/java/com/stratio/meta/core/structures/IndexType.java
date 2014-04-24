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

/**
 * Types of indexes supported by META.
 * <ul>
 *  <li> DEFAULT: Default index created by Cassandra.
 *  <li> LUCENE: Index supported by Lucene.
 * </ul>
 */
public enum IndexType {
    /**
     * Default index created by Cassandra.
     */
    DEFAULT,
    /**
     * Lucene backed index.
     */
    LUCENE
}
