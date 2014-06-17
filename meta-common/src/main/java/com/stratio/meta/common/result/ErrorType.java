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

package com.stratio.meta.common.result;

/**
 * Types of errors reported by META when a query is processed.
 */
public enum ErrorType {
  /**
   * Parsing error.
   */
  PARSING,

  /**
   * Validation error.
   */
  VALIDATION,

  /**
   * Execution error.
   */
  EXECUTION,

  /**
   * Connection error.
   */
  CONNECTION,

  /**
   * Operation not supported error.
   */
  NOT_SUPPORTED
}
