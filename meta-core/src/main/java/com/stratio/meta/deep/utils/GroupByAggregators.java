/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.deep.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.stratio.deep.entity.CassandraCell;
import com.stratio.deep.entity.Cell;

/**
 * @author Óscar Puertas
 * 
 */
public final class GroupByAggregators {

  public static Cell sum(Cell cell1, Cell cell2) {

    Cell resultCell = null;
    Class<?> valueType = ((CassandraCell) cell1).getValueType();

    if (valueType.equals(Integer.class) || valueType.equals(Long.class)
        || valueType.equals(BigInteger.class)) {

      BigInteger bigValue1 = new BigInteger(cell1.getCellValue().toString());
      BigInteger bigValue2 = new BigInteger(cell2.getCellValue().toString());
      BigInteger resultValue = bigValue1.add(bigValue2);

      resultCell = CassandraCell.create(cell1.getCellName(), resultValue);
    } else {

      BigDecimal bigValue1 = new BigDecimal(cell1.getCellValue().toString());
      BigDecimal bigValue2 = new BigDecimal(cell2.getCellValue().toString());
      BigDecimal resultValue = bigValue1.add(bigValue2);

      resultCell = CassandraCell.create(cell1.getCellName(), resultValue);
    }

    return resultCell;
  }

  public static Cell count(Cell cell1, Cell cell2) {

    Cell resultCell = null;

    BigInteger resultValue =
        ((BigInteger) cell1.getCellValue()).add((BigInteger) cell2.getCellValue());
    resultCell = CassandraCell.create(cell1.getCellName(), resultValue);

    return resultCell;
  }

  public static Cell max(Cell cell1, Cell cell2) {

    Cell resultCell = null;

    Class<?> valueType = ((CassandraCell) cell1).getValueType();
    if (valueType.equals(Integer.class) || valueType.equals(Long.class)
        || valueType.equals(BigInteger.class)) {
      BigInteger bigValue1 = new BigInteger(cell1.getCellValue().toString());
      BigInteger bigValue2 = new BigInteger(cell2.getCellValue().toString());
      resultCell = (bigValue1.compareTo(bigValue2) > 0) ? cell1 : cell2;
    } else {
      BigDecimal bigValue1 = new BigDecimal(cell1.getCellValue().toString());
      BigDecimal bigValue2 = new BigDecimal(cell2.getCellValue().toString());
      resultCell = (bigValue1.compareTo(bigValue2) > 0) ? cell1 : cell2;
    }


    return resultCell;
  }

  public static Cell min(Cell cell1, Cell cell2) {

    Cell resultCell = null;

    Class<?> valueType = ((CassandraCell) cell1).getValueType();
    if (valueType.equals(Integer.class) || valueType.equals(Long.class)
        || valueType.equals(BigInteger.class)) {
      BigInteger bigValue1 = new BigInteger(cell1.getCellValue().toString());
      BigInteger bigValue2 = new BigInteger(cell2.getCellValue().toString());
      resultCell = (bigValue1.compareTo(bigValue2) < 0) ? cell1 : cell2;
    } else {
      BigDecimal bigValue1 = new BigDecimal(cell1.getCellValue().toString());
      BigDecimal bigValue2 = new BigDecimal(cell2.getCellValue().toString());
      resultCell = (bigValue1.compareTo(bigValue2) < 0) ? cell1 : cell2;
    }

    return resultCell;
  }
}
