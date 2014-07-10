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

package com.stratio.meta.common.statements.structures.window;

/**
 * Types of windows supported in SELECT statements.
 */
public enum WindowType {

  /**
   * Window based on a number of tuples to be received.
   */
  NUM_ROWS,

  /**
   * Window to process results as soon as the become available.
   */
  LAST,

  /**
   * Gather results in a time window.
   */
  TEMPORAL
}
