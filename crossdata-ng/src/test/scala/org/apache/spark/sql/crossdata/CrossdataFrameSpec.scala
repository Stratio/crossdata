package org.apache.spark.sql.crossdata

import com.stratio.crossdata.sql.sources.NativeScan
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.Inner
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.sources.{BaseRelation, LogicalRelation}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkContext, SparkConf}
import org.scalatest.FlatSpec


class CrossdataFrameSpec extends FlatSpec{

  val sparkConf = new SparkConf().
    setAppName("CassandraExample").
    setMaster("local[4]")

 val sc = new SparkContext(sparkConf)
 val xdContext = new XDContext(sc)


 class MockBaseRelation extends BaseRelation {
   override def sqlContext: SQLContext = xdContext
   override def schema: StructType = StructType(List(StructField("id", StringType)))
 }

 val mockNativeRelation = new MockBaseRelation with NativeScan{
   override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = ???
 }

 val mockNonNativeRelation = new MockBaseRelation


  "A logical plan (select *) with native relation" should  "return a native relation" in {
     val nativeRelation = CrossdataFrame.findNativeRelation(LogicalRelation(mockNativeRelation))
     assert(nativeRelation.nonEmpty)
     assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select *) with non-native relation" should  "return None" in {
    val nativeRelation = CrossdataFrame.findNativeRelation(LogicalRelation(mockNonNativeRelation))
    assert(nativeRelation.isEmpty)
  }

  "A logical plan (Select count(*),comment, count(comment) from students group by comment ) with native relation" should  "return a native relation" in {
    val attributeReference = AttributeReference("comment", StringType)(ExprId(1), Seq("students"))
    val projectPlan = Project(Seq(attributeReference), LogicalRelation(mockNativeRelation))
    val count = Count(Literal(1))
    val optimizedLogicalPlan = Aggregate(Seq(attributeReference),
      Seq(Alias(count, "c0")(ExprId(2)), attributeReference, Alias(count, "c2")(ExprId(3))), projectPlan)

    val nativeRelation = CrossdataFrame.findNativeRelation(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }

  "A logical plan (select id as alias, id from students) with native relation" should  "return a native relation" in {
    val aliasRef = AttributeReference("id", StringType)(ExprId(1), Seq("students"))
    val idRef = AttributeReference("id", StringType)(ExprId(2), Seq("students"))

    val projectList:Seq[NamedExpression] = Seq(Alias(aliasRef,"alias")(ExprId(3)),idRef)
    val child = LogicalRelation(mockNativeRelation)
    val optimizedLogicalPlan = Project(projectList, child)

    val nativeRelation = CrossdataFrame.findNativeRelation(optimizedLogicalPlan)

    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }



  "A logical plan (select * from (SELECT id from students) as stu , students where stu.id = 5" should  "return a native relation" in {
    val idRef = AttributeReference("id", IntegerType)(ExprId(0), Seq("id"))
    val idRef2 = AttributeReference("id", IntegerType)(ExprId(11), Seq("id"))
    val ageRef = AttributeReference("age", IntegerType)(ExprId(12), Seq("id"))
    val commentRef = AttributeReference("comment", StringType)(ExprId(13), Seq("id"))
    val enrolledRef = AttributeReference("enrolled", BooleanType)(ExprId(14), Seq("id"))
    val nameRef = AttributeReference("name", StringType)(ExprId(15), Seq("id"))

    val projectList:Seq[NamedExpression] = Seq(idRef, idRef2,ageRef, commentRef, enrolledRef, nameRef)
    val right: LogicalPlan = LogicalRelation(mockNativeRelation)

    val left: LogicalPlan = Project(Seq(idRef), Filter(EqualTo(idRef, Literal("5")), right) )
    val child = Join(left, right, Inner, None)

    val optimizedLogicalPlan = Project(projectList, child)

    val nativeRelation = CrossdataFrame.findNativeRelation(optimizedLogicalPlan)
    assert(nativeRelation.nonEmpty)
    assert(nativeRelation.get === mockNativeRelation)
  }


}
