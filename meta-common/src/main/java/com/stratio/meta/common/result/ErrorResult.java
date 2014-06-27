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
 * Error result of a given type.
 * <ul>
 *   <li>Parsing</li>
 *   <li>Validation</li>
 *   <li>Execution</li>
 *   <li>Not supported</li>
 * </ul>
 */
public class ErrorResult extends Result{

  /**
   * Type of error.
   */
  private final ErrorType type;

  /**
   * The associated error message in case of {@code error}.
   */
  private final String errorMessage;

  public ErrorResult(ErrorType type, String errorMessage){
    this.type = type;
    this.errorMessage = errorMessage;
    this.error = true;
  }

  /**
   * Get the error message.
   *
   * @return The message or null if no error occurred.
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  public ErrorType getType() {
    return type;
  }
}
