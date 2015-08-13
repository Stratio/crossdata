package org.apache.spark.sql.crossdata

import com.stratio.crossdata.sql.sources.NativeScan
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.sources.LogicalRelation
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.FlatSpec

import XDDataFrame._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDDataFrameUtilsSpec extends FlatSpec {

  val mockNativeRelation = new MockBaseRelation with NativeScan {
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = ???
  }

  val mockNonNativeRelation = new MockBaseRelation


  "A logical plan (select *) with native relation" should "return a native relation" in {
    val nativeRelation = findNativeQueryExecutor(LogicalRelation(mockNativeRelation))
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select *) with non-native relation" should "return None" in {
    val nativeRelation = findNativeQueryExecutor(LogicalRelation(mockNonNativeRelation))
    assert(nativeRelation.isEmpty)
  }

  "A logical plan (Select count(*),comment, count(comment) from students group by comment ) with native relation" should "return a native relation" in {
    val attributeReference = AttributeReference("comment", StringType)(ExprId(1), Seq("students"))
    val projectPlan = Project(Seq(attributeReference), LogicalRelation(mockNativeRelation))
    val count = Count(Literal(1))
    val optimizedLogicalPlan = Aggregate(Seq(attributeReference),
      Seq(Alias(count, "c0")(ExprId(2)), attributeReference, Alias(count, "c2")(ExprId(3))), projectPlan)

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select count(a.comment) from (select comment from students) as a ) with native relation" should "return a native relation" in {
    val commentAttributeReference = AttributeReference("comment", StringType)(ExprId(1), Seq("students"))
    val projectPlan = Project(Seq(commentAttributeReference), LogicalRelation(mockNativeRelation))
    val count = Count(commentAttributeReference)
    val optimizedLogicalPlan = Aggregate(
      Nil,
      Seq(Alias(count, "c0")(ExprId(4))),
      projectPlan)

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select * from students where id = 1) with native relation" should "return a native relation" in {
    val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))
    val condition = EqualTo(idAttributeReference, Literal(1))
    val optimizedLogicalPlan = Filter(condition, LogicalRelation(mockNativeRelation))

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select * from students where id > 2 or id = 1) with native relation" should "return a native relation" in {
    val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))
    val orCondition = Or(GreaterThan(idAttributeReference, Literal(2)), EqualTo(idAttributeReference, Literal(1)))
    val optimizedLogicalPlan = Filter(orCondition, LogicalRelation(mockNativeRelation))

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select * from students order by id) with native relation" should "return a native relation" in {
    val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))
    val sortOrder = SortOrder(idAttributeReference, Ascending)
    val optimizedLogicalPlan = Sort(Seq(sortOrder), global = true, LogicalRelation(mockNativeRelation))

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select * from students limit 5) with native relation" should "return a native relation" in {

    val optimizedLogicalPlan = Limit(Literal(5), LogicalRelation(mockNativeRelation))

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select case id > 5 when true then 1 else 0 end from students) with native relation" should "return a native relation" in {

    val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))
    val caseKeyWhen = CaseKeyWhen(GreaterThan(idAttributeReference, Literal(5)), Seq(Literal(true), Literal(1), Literal(0)))
    val caseWhenAlias = Alias(caseKeyWhen, "c0")(ExprId(5))
    val optimizedLogicalPlan = Project(Seq(caseWhenAlias), LogicalRelation(mockNativeRelation))

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select * from students where id between 0 and 6 AND id in (3,5,10)) with native relation" should "return a native relation" in {

    val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))
    val betweenFilter = And(GreaterThanOrEqual(idAttributeReference, Literal(0)), LessThanOrEqual(idAttributeReference, Literal(6)))
    val condition = And(betweenFilter, InSet(idAttributeReference, Set(5, 10, 3)))
    val optimizedLogicalPlan = Filter(condition, LogicalRelation(mockNativeRelation))

    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select count(*) from students where id >5 group by comment HAVING count(*)>1 order by count(*) limit 5) with native relation" should "return a native relation" in {

    val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))

    val commentAttributeReference = AttributeReference("comment", StringType)(ExprId(1), Seq("students"))
    val havingAttributeReference = AttributeReference("havingCondition", BooleanType)(ExprId(7))
    val countAttributeReference = AttributeReference("c0", LongType)(ExprId(6))

    val filterPlan = Filter(GreaterThan(idAttributeReference, Literal(5)), LogicalRelation(mockNativeRelation))

    val firstProjectPlan = Project(Seq(commentAttributeReference), filterPlan)

    val aggregateLogicalPlan = Aggregate(
      Seq(commentAttributeReference),
      Seq(
        Alias(GreaterThanOrEqual(Count(Literal(1)), Literal(1)), "havingCondition")(ExprId(7)),
        Alias(Count(Literal(1)), "c0")(ExprId(6))
      ),
      firstProjectPlan)

    val filterLogicalPlan = Filter(havingAttributeReference, aggregateLogicalPlan)

    val projectLogicalPlan = Project(Seq(countAttributeReference), filterLogicalPlan)

    val sortOrder = SortOrder(Count(Literal(1)), Ascending)
    val sortLogicalPlan = Sort(Seq(sortOrder), global = true, projectLogicalPlan)


    val optimizedLogicalPlan = Limit(Literal(5), sortLogicalPlan)
    val nativeRelation = findNativeQueryExecutor(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }


}
