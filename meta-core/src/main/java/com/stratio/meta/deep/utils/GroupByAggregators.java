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
