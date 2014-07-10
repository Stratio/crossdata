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
 * Window
 */
public class Window {

  /**
   * Type of window.
   */
  private final WindowType type;

  /**
   * Number of rows for WindowType.NUM_ROWS.
   */
  private int numRows = -1;

  /**
   * Number of time units for WindowType.TEMPORAL.
   */
  private int numTimeUnits = -1;

  /**
   * Time unit for WindowType.TEMPORAL.
   */
  private TimeUnit timeUnit = null;


  public Window(WindowType type) {
    this.type = type;
  }

  /**
   * Set the number of rows for WindowType.NUM_ROWS.
   *
   * @param numRows Number of rows.
   */
  public void setNumRows(int numRows) {
    this.numRows = numRows;
  }

  /**
   * Set the number of time units and unit for WindowType.TEMPORAL
   *
   * @param numTimeUnits Number of time units.
   * @param unit         Time unit.
   */
  public void setTimeWindow(int numTimeUnits, TimeUnit unit) {
    this.numTimeUnits = numTimeUnits;
    this.timeUnit = unit;
  }

  /**
   * Get the type of window.
   *
   * @return A {@link com.stratio.meta.common.statements.structures.window.WindowType}.
   */
  public WindowType getType() {
    return type;
  }

  /**
   * Get the time window duration in milliseconds.
   *
   * @return The duration if WindowType.TEMPORAL, or 0 otherwise.
   */
  public long getDurationInMilliseconds() {
    long millis = 0;
    if (WindowType.TEMPORAL.equals(this.type)) {
      int factor = 1;
      if (timeUnit.equals(TimeUnit.SECONDS)) {
        factor = factor * 1000;
      } else if (timeUnit.equals(TimeUnit.MINUTES)) {
        factor = factor * 1000 * 60;
      } else if (timeUnit.equals(TimeUnit.HOURS)) {
        factor = factor * 1000 * 60 * 60;
      } else if (timeUnit.equals(TimeUnit.DAYS)) {
        factor = factor * 1000 * 60 * 60 * 24;
      }
      millis = numTimeUnits * factor;
    }
    return millis;
  }

  @Override
  public String toString() {
    String result = "";
    if (WindowType.LAST.equals(type)) {
      result = "LAST";
    } else if (WindowType.NUM_ROWS.equals(type)) {
      result = numRows + " ROWS";
    } else if (WindowType.TEMPORAL.equals(type)) {
      result = numTimeUnits + " " + timeUnit;
    }
    return result;
  }

}
