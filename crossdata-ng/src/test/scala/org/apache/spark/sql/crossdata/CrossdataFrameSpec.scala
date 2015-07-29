package org.apache.spark.sql.crossdata

import com.stratio.crossdata.sql.sources.NativeScan
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.catalyst.plans.logical.{Project, Aggregate, LogicalPlan}
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


}
