package org.apache.spark.sql.crossdata

import com.stratio.crossdata.sql.sources.NativeScan
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.crossdata.test.TestXDContext
import org.apache.spark.sql.sources.{TableScan, LogicalRelation}
import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.{Inside, Matchers, FlatSpec}

import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDDataFrameSpec extends FlatSpec with Matchers with Inside {

  private lazy val xdContext = org.apache.spark.sql.crossdata.test.TestXDContext

  import xdContext.implicits._

  lazy val sparkRows = TestXDContext.sparkContext.parallelize(Seq(1)).toDF().collect()
  lazy val nativeRows = Array(Row(2l))


  "A XDDataFrame (select * from nativeRelation)" should "be executed natively" in {
    val result = XDDataFrame(xdContext, LogicalRelation(mockNativeRelation)).collect()
    result should have length 1
    result(0) should equal(nativeRows(0))
  }

  "A cached XDDataFrame (select * from nativeRelation)" should "be executed on the Spark cluster" in {
    val dataframe = XDDataFrame(xdContext, LogicalRelation(mockNativeRelation))
    val result = dataframe.cache().collect()
    dataframe.unpersist(blocking = true)
    result should have length 1
    result(0) should equal(sparkRows(0))

  }

  "A XDDataFrame with a Spark query" should "be executed on the Spark cluster" in {
    val result = XDDataFrame(xdContext, LogicalRelation(mockPureSparkNativeRelation)).collect()
    result should have length 1
    result(0) should equal(sparkRows(0))
  }

  "A XDDataFrame " should "execute collectAsList natively" in {
    val result = XDDataFrame(xdContext, LogicalRelation(mockNativeRelation)).collectAsList()
    result should have length 1
    result.get(0) should equal(nativeRows(0))
  }

  "A XDDataFrame " should "return a XDDataFrame when applying a limit" in {
    val dataframe = XDDataFrame(xdContext, LogicalRelation(mockNativeRelation)).limit(5)
    dataframe shouldBe a[XDDataFrame]
    dataframe.logicalPlan should matchPattern { case Limit(Literal(5, _), _) =>}
  }

  "A XDDataFrame " should "return a XDDataFrame when applying a count" in {
    XDDataFrame(xdContext, LogicalRelation(mockNativeRelation)).count() should be(2l)
  }


  val mockNonNativeRelation = new MockBaseRelation

  val mockNativeRelation = new MockBaseRelation with NativeScan with TableScan {
    // Native execution
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = Some(nativeRows)

    // Spark execution
    override def buildScan(): RDD[Row] = TestXDContext.sparkContext.parallelize(Seq(1)).toDF().rdd
  }

  val mockPureSparkNativeRelation = new MockBaseRelation with NativeScan with TableScan {
    // Native execution
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = None

    // Spark execution
    override def buildScan(): RDD[Row] = TestXDContext.sparkContext.parallelize(Seq(1)).toDF().rdd
  }

  val mockNativeRelationWith2Rows = new MockBaseRelation with NativeScan with TableScan {
    // Native execution
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = Some(Array(nativeRows(0), nativeRows(0)))

    // Spark execution
    override def buildScan(): RDD[Row] = TestXDContext.sparkContext.parallelize(Seq(1)).toDF().rdd
  }

}
