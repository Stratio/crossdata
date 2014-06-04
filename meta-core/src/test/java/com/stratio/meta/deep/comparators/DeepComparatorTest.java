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

  @Test
  public void testSimpleCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "10");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "b");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testSimpleAscCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.ASC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "10");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "b");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testSimpleDescCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.DESC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "10");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "b");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testSimpleEqualsCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "10");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "aaa");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) == 0);
  }

  @Test
  public void testMultipleCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));
    fieldsOrdering.add(new Ordering("age"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "30");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "b");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testMultipleAscCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.ASC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.ASC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "30");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "b");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testMultipleDescCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.DESC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.DESC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "30");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "b");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testMultipleCompareEqualsFirstFieldOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));
    fieldsOrdering.add(new Ordering("age"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "30");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "aaa");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testMultipleAscCompareEqualsFirstFieldOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.ASC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.ASC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "30");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "aaa");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) > 0);
  }

  @Test
  public void testMultipleDescCompareEqualsFirstFieldOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name", true, OrderDirection.DESC));
    fieldsOrdering.add(new Ordering("age", true, OrderDirection.DESC));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "30");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "aaa");
    Cell cellAge2 = Cell.create("age", "20");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) < 0);
  }

  @Test
  public void testMultipleEqualsCompareOk() {

    List<Ordering> fieldsOrdering = new ArrayList<>();
    fieldsOrdering.add(new Ordering("name"));
    fieldsOrdering.add(new Ordering("age"));

    DeepComparator comparator = new DeepComparator(fieldsOrdering);

    // Cells1
    Cell cellName1 = Cell.create("name", "aaa");
    Cell cellAge1 = Cell.create("age", "10");

    Cells cells1 = new Cells(cellName1, cellAge1);

    // Cells2
    Cell cellName2 = Cell.create("name", "aaa");
    Cell cellAge2 = Cell.create("age", "10");

    Cells cells2 = new Cells(cellName2, cellAge2);

    assertTrue("Error comparing cells", comparator.compare(cells1, cells2) == 0);
  }
}
