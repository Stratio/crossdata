package com.stratio.crossdata.sql.sources.mongodb

import java.util
import java.util.regex.Pattern

import com.mongodb.{DBObject, QueryOperators}
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.UTF8String
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoQueryProcessorSpec extends BaseXDTest {

  val Keyspace = "keyspace"
  val Table = "table"
  val TableQN = s"$Keyspace.$Table"
  val ColumnAge = "age"
  val ColumnId = "id"
  val ObjectId = "_id"
  val Limit = 12000
  val ValueAge = 25
  val ValueAge2 = 30
  val ValueId = UTF8String("00123")

  "A MongoQueryProcessor" should "build a query requiring some columns" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId, ColumnAge), Array())
    val columnsSet = requiredColumns.keySet

    filters.keySet should have size 0

    columnsSet should have size 3
    columnsSet should contain allOf (ColumnId, ColumnAge)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ColumnAge) should be (1)
    requiredColumns.get(ObjectId) should be (0)
  }

  it should "build a query with two equal filters" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(EqualTo(ColumnAge, ValueAge), EqualTo(ColumnId, ValueId)))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain(ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 2
    filters.get(ColumnId) should be (ValueId.toString)
    filters.get(ColumnAge) should be (ValueAge)

  }

  it should "build a query with an IN clause" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(In(ColumnAge, Array(ValueAge, ValueAge2))))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain (ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 1
    filters.get(ColumnAge) shouldBe a [DBObject]

    val inListValues = filters.get(ColumnAge).asInstanceOf[DBObject].get(QueryOperators.IN)
    inListValues shouldBe a [Array[Object]]
    inListValues.asInstanceOf[Array[Object]] should have size 2
    inListValues.asInstanceOf[Array[Object]] should contain allOf (ValueAge, ValueAge2)

  }

  it should "build a query with a LT clause" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(LessThan(ColumnAge, ValueAge)))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain(ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 1
    filters.get(ColumnAge) shouldBe a [DBObject]

    filters.get(ColumnAge).asInstanceOf[DBObject].get(QueryOperators.LT) shouldBe (ValueAge)

  }

  it should "build a query with a LTE clause" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(LessThanOrEqual(ColumnAge, ValueAge)))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain(ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 1
    filters.get(ColumnAge) shouldBe a [DBObject]
    filters.get(ColumnAge).asInstanceOf[DBObject].get(QueryOperators.LTE) shouldBe (ValueAge)

  }


  it should "build a query with a GTE clause" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(GreaterThanOrEqual(ColumnAge, ValueAge)))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain(ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 1
    filters.get(ColumnAge) shouldBe a [DBObject]

    filters.get(ColumnAge).asInstanceOf[DBObject].get(QueryOperators.GTE) shouldBe (ValueAge)

  }

  it should "build a query with an IS NOT NULL clause" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(IsNotNull(ColumnAge)))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain(ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 1
    filters.get(ColumnAge) shouldBe a [DBObject]

    filters.get(ColumnAge).asInstanceOf[DBObject].get(QueryOperators.NE) shouldBe (null)

  }

  it should "build a query with an AND(v1 > x <= v2)" in {
    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(And(GreaterThan(ColumnAge, ValueAge), LessThanOrEqual(ColumnAge, ValueAge2))))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain (ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 1
    filters.get(QueryOperators.AND) shouldBe a [util.ArrayList[DBObject]]
    val subfilters = filters.get(QueryOperators.AND).asInstanceOf[util.ArrayList[DBObject]]

    //filter GT
    subfilters.get(0).get(ColumnAge) shouldBe a [DBObject]
    subfilters.get(0).get(ColumnAge).asInstanceOf[DBObject].get(QueryOperators.GT) shouldBe (ValueAge)

    //filter LTE
    subfilters.get(1).get(ColumnAge) shouldBe a [DBObject]
    subfilters.get(1).get(ColumnAge).asInstanceOf[DBObject].get(QueryOperators.LTE) shouldBe (ValueAge2)
  }

  it should "build a query with a REGEX clause " in {

    val (filters, requiredColumns) = MongoQueryProcessor.buildNativeQuery(Array(ColumnId), Array(StringContains(ColumnId, ValueId.toString)))
    val filterSet = filters.keySet

    requiredColumns.keySet should contain (ColumnId)
    requiredColumns.get(ColumnId) should be (1)
    requiredColumns.get(ObjectId) should be (0)

    filterSet should have size 1
    filters.get(ColumnId) shouldBe a [Pattern]

    filters.get(ColumnId).asInstanceOf[Pattern].pattern should be (Pattern.compile(s".*${ValueId.toString}.*").pattern)
  }

}
