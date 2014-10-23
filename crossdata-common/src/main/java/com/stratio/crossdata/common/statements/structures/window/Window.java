/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.statements.structures.window;

import java.io.Serializable;

/**
 * Window
 */
public class Window implements Serializable {

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

    /**
     * Number of milliseconds in a second.
     */
    private static final int TO_MILLISECONDS = 1000;

    /**
     * Number of minutes in an hour.
     */
    private static final int TO_MINUTES = 60;

    /**
     * Number of hours in a day.
     */
    private static final int TO_HOURS = 24;

    /**
     * Class constructor.
     *
     * @param type The {@link com.stratio.crossdata.common.statements.structures.window.WindowType}.
     */
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
     * @return A {@link com.stratio.crossdata.common.statements.structures.window.WindowType}.
     */
    public WindowType getType() {
        return type;
    }

    /**
     * Get the number of rows.
     *
     * @return The number of rows or -1 if a time window is set.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Get the number of time units.
     *
     * @return The number of time units or -1 if a row-based window is set.
     */
    public int getNumTimeUnits() {
        return numTimeUnits;
    }

    /**
     * Get the time unit associated with the window.
     * @return A {@link com.stratio.crossdata.common.statements.structures.window.TimeUnit}.
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
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
                factor = factor * TO_MILLISECONDS;
            } else if (timeUnit.equals(TimeUnit.MINUTES)) {
                factor = factor * TO_MILLISECONDS * TO_MINUTES;
            } else if (timeUnit.equals(TimeUnit.HOURS)) {
                factor = factor * TO_MILLISECONDS * TO_MINUTES * TO_MINUTES;
            } else if (timeUnit.equals(TimeUnit.DAYS)) {
                factor = factor * TO_MILLISECONDS * TO_MINUTES * TO_MINUTES * TO_HOURS;
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
