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
 * Types of list processes supported by META.
 * <ul>
 *  <li> PROCESS: List the existing running queries.
 *  <li> UDF: List the existing UDF in the system.
 *  <li> TRIGGER: List the existing triggers in the system.
 * </ul>
 */
public enum ListType {
    /**
     * List all running processes.
     */
    PROCESS,
    /**
     * List all known UDF.
     */
    UDF,
    /**
     * List all known triggers.
     */
    TRIGGER
}
