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

package com.stratio.meta.common.logicalplan;

import java.util.List;

/**
 * Logical Plan that contains the decomposition of the task that need to be executed
 * at connector level to retrieve the required data. The basic operations contained in
 * a logical plan are:
 * <ul>
 *   <li>Project: Retrieve a set of columns from a table.</li>
 *   <li>Filter: Apply a clause to filter data.</li>
 *   <li>Window: The window to be applied on streaming sources.</li>
 * </ul>
 */
public class LogicalPlan {

  /**
   * List with the steps in order.
   */
  private final List<LogicalStep> stepList;

  /**
   * Logical plan constructor.
   * @param stepList
   */
  public LogicalPlan(List<LogicalStep> stepList) {
    this.stepList = stepList;
  }

  /**
   * Get the list of steps.
   * @return A list of {@link com.stratio.meta.common.logicalplan.LogicalStep}.
   */
  public List<LogicalStep> getStepList(){
    return this.stepList;
  }
}
