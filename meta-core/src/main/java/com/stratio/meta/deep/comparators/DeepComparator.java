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

package com.stratio.meta.deep.comparators;

import com.stratio.deep.entity.Cells;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.core.structures.OrderDirection;
import com.stratio.meta2.core.structures.Ordering;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DeepComparator implements Comparator<Cells>, Serializable {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 3173462422717736001L;

  /**
   * ORDER BY Clause.
   */
  private List<Ordering> orderings;

  /**
   * DeepComparator constructor.
   * 
   * @param orderings List of {@link com.stratio.meta2.core.structures.Ordering} which represents
   *        ORDER BY clause.
   */
  public DeepComparator(List<Ordering> orderings) {
    this.orderings = orderings;
  }

  @Override
  public int compare(Cells o1, Cells o2) {
    boolean resolution = false;
    int result = 0;
    Iterator<Ordering> it = orderings.iterator();
    while (!resolution && it.hasNext()) {
      Ordering ordering = it.next();
      String currentField = ((ColumnSelector) ordering.getSelector()).getName().getName();
      result =
          ((Comparable) o1.getCellByName(currentField).getCellValue()).compareTo(o2.getCellByName(
              currentField).getCellValue());
      if (result != 0) {
        resolution = true;
        result = checkOrderDirection(result, ordering);
      }
    }

    return result;
  }

  /**
   * Change result depending on ORDER BY direction.
   * 
   * @param input Result of comparison.
   * @param ordering current {@link com.stratio.meta2.core.structures.Ordering}.
   * @return same result or contrary depending on direction.
   */
  private int checkOrderDirection(int input, Ordering ordering) {

    int result = input;
    if (ordering.isDirInc() && ordering.getOrderDir() == OrderDirection.DESC) {
      result = input * -1;
    }

    return result;
  }
}
