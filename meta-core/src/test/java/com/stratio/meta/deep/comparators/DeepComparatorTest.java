package com.stratio.meta.deep.comparators;

import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stratio.deep.entity.CassandraCell;
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

    Cell cellName = CassandraCell.create(CONSTANT_NAME, valueName);
    Cell cellAge = CassandraCell.create(CONSTANT_AGE, valueAge);

    Cells cells = new Cells();
    cells.add(cellName);
    cells.add(cellAge);

    return cells;
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
