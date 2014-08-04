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

import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;
import com.stratio.meta.core.structures.OrderDirection;
import com.stratio.meta.core.structures.Ordering;

public class DeepComparatorTest {

  private static final String CONSTANT_NAME = "name";

  private static final String CONSTANT_AGE = "age";

  @Test
  public void testSimpleCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering(CONSTANT_NAME));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "10");

    // Cells2
    Cells cells2 = createNameAndAgeCells("b", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  private Cells createNameAndAgeCells(String valueName, String valueAge) {

    Cell cellName = Cell.create(CONSTANT_NAME, valueName);
    Cell cellAge = Cell.create(CONSTANT_AGE, valueAge);

    return new Cells(cellName, cellAge);
  }

  @Test
  public void testSimpleAscCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.ASC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "10");

    // Cells2
    Cells cells2 = createNameAndAgeCells("b", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testSimpleDescCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.DESC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "10");

    // Cells2
    Cells cells2 = createNameAndAgeCells("b", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testSimpleEqualsCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "10");

    // Cells2
    Cells cells2 = createNameAndAgeCells("aaa", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) == 0);
  }

  @Test
  public void testMultipleCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));
    fieldsOrdering.add(new Ordering("age"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "30");

    // Cells2
    Cells cells2 = createNameAndAgeCells("b", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testMultipleAscCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.ASC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.ASC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "30");

    // Cells2
    Cells cells2 = createNameAndAgeCells("b", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testMultipleDescCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.DESC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.DESC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "30");

    // Cells2
    Cells cells2 = createNameAndAgeCells("b", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testMultipleCompareEqualsFirstFieldOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));
    fieldsOrdering.add(new Ordering("age"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "30");

    // Cells2
    Cells cells2 = createNameAndAgeCells("aaa", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testMultipleAscCompareEqualsFirstFieldOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.ASC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.ASC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "30");

    // Cells2
    Cells cells2 = createNameAndAgeCells("aaa", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testMultipleDescCompareEqualsFirstFieldOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.DESC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.DESC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "30");

    // Cells2
    Cells cells2 = createNameAndAgeCells("aaa", "20");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testMultipleEqualsCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));
    fieldsOrdering.add(new Ordering("age"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cells cells1 = createNameAndAgeCells("aaa", "10");

    // Cells2
    Cells cells2 = createNameAndAgeCells("aaa", "10");

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) == 0);
  }
}
