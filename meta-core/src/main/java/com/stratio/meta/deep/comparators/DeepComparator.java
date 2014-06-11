package com.stratio.meta.deep.comparators;


import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.OrderDirection;
import com.stratio.meta.core.structures.Ordering;

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
   * @param orderings List of {@link com.stratio.meta.core.structures.Ordering} which represents
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
      String currentField = ordering.getSelectorIdentifier().getField();
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
   * @param result Result of comparison.
   * @param ordering current {@link com.stratio.meta.core.structures.Ordering}.
   * @return same result or contrary depending on direction.
   */
  private int checkOrderDirection(int result, Ordering ordering) {
    if (ordering.isDirInc() && ordering.getOrderDir() == OrderDirection.DESC) {
      result = result * -1;
    }
    return result;
  }
}
