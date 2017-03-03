/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata

import com.stratio.crossdata.connector.NativeScan

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.crossdata.test.SharedXDSession
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources.TableScan
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.Inside
import org.scalatest.junit.JUnitRunner
import org.apache.spark.sql.crossdata.XDDatasetFunctions._

@RunWith(classOf[JUnitRunner])
class DataFrameIT extends SharedXDSession with Inside {

  lazy val sparkRows: Array[Row] = spark.createDataFrame(spark.sparkContext.parallelize(Seq(Row(1))), StructType(Array(StructField("id", IntegerType)))).collect()
  lazy val nativeRows = Array(InternalRow(2))

   /* SPARK
  val sparkSession = SparkSession.builder().appName("mySession").enableHiveSupport().getOrCreate()
  sparkSession.newSession()
  */

  /*val xdSessionProvider: BasicSessionProvider = new SessionProvider(config)
  val xdSession = XDSessionProvider = xdSesssionProvider.newSession("unai")
  xdSessionProvider.newSession("david")
*/

  // TODO enable XDPlan => https://stratio.atlassian.net/browse/DCS-2055
  "A XDDataFrame (select * from nativeRelation)" should "be executed natively" ignore {
    val result = Dataset.ofRows(spark, LogicalRelation(mockNativeRelation, None, None)).collect()
    result should have length 1
    result(0)(0) should equal(nativeRows(0).getInt(0))
  }

  "A cached XDDataFrame (select * from nativeRelation)" should "be executed on the Spark cluster" in {
    val dataframe = spark.baseRelationToDataFrame(mockNativeRelation) // TODO Dataset.ofRows(spark, LogicalRelation(mockNativeRelation))
    val result = dataframe.cache().collect()
    dataframe.unpersist(blocking = true)
    result should have length 1
    result(0) should equal(sparkRows(0))
  }


  "A XDDataFrame resulting in an error when executing natively" should "be executed on the Spark cluster" in {
    val result = Dataset.ofRows(spark, LogicalRelation(mockPureSparkNativeRelation)).collect()
    result should have length 1
    result(0) should equal(sparkRows(0))
  }

  "A XDDataFrame with a logical plan which is not supported natively" should "be executed on the Spark cluster" in {
    val result = Dataset.ofRows(spark, LogicalRelation(mockNativeRelationUnsupportedPlan)).collect()
    result should have length 1
    result(0) should equal(sparkRows(0))
  }

  // TODO enable XDPlan => https://stratio.atlassian.net/browse/DCS-2055
  "A XDDataFrame " should "execute collectAsList natively" ignore {
    val result = Dataset.ofRows(spark, LogicalRelation(mockNativeRelation)).collectAsList()
    result should have length 1
    result.get(0)(0) should equal(nativeRows(0).getInt(0))
  }

  "A XDDataFrame" should "keep the posibility of execute native queries when applying Dataset operations" in {
    val dataframe = Dataset.ofRows(spark, LogicalRelation(mockNativeRelation)).limit(5)
    dataframe.logicalPlan should matchPattern { case Limit(Literal(5, _), _) => }
  }

  // TODO enable XDPlan => https://stratio.atlassian.net/browse/DCS-2055
  "A XDDataFrame " should "return a XDDataFrame when applying a count" ignore {
    println("init")
    val result = Dataset.ofRows(spark, LogicalRelation(mockNativeRelation)).filter("id < 5").collect()
    result should have length 1
    result(0)(0) should equal(nativeRows(0).getInt(0))
  }


  "A Dataset" should "allow to configure the execution type" in {
    val result = Dataset.ofRows(spark, LogicalRelation(mockNativeRelation, None, None)).collect(ExecutionType.Native)
    result should have length 1
    result(0)(0) should equal(nativeRows(0).getInt(0))
  }

  "A Dataset" should "allow to execute a flattenedColect" in {
    val result = Dataset.ofRows(spark, LogicalRelation(mockNativeRelation, None, None)).flattenedCollect()
    /* TODO test    result should have length 1
    result(0)(0) should equal(nativeRows(0).getInt(0))*/
  }


  "A DataSet from a class that implicitly is XDDataset " should "be fail natively" in {

    val person=new Person("Paco",7)
    val personDS=Seq(person)
    val sparkFake = spark
    import sparkFake.implicits._
    val myDs=spark.createDataset(personDS)

    a[RuntimeException] should be thrownBy myDs.collect(ExecutionType.Native)

  }

  //TODO When Postgres or Cassandra datasource be available uncomment this test
//  "A XDDataSet from a cassandra/postgres " should "be execute natively" in {
//    val cassandraDs = spark.read.format("cassandra").load()
//    val sparkFake = spark
//    import sparkFake.implicits._
//    val result = cassandraDs.as[Person].collect(ExecutionType.Native)
//
//    result should have length 1
//    result(0).age should be (7)
//  }




  val mockNonNativeRelation = new MockBaseRelation

  val mockNativeRelation = new MockBaseRelation with NativeScan with TableScan {
    override def isSupported(logicalStep: LogicalPlan, fullyLogicalPlan: LogicalPlan) = true

    // Native execution
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[InternalRow]] = Some(nativeRows)

    // Spark execution
    override def buildScan(): RDD[Row] = spark.createDataFrame(spark.sparkContext.parallelize(Seq(Row(1))), StructType(Array(StructField("id", IntegerType)))).rdd
  }


  val mockPureSparkNativeRelation = new MockBaseRelation with NativeScan with TableScan {
    override def isSupported(logicalStep: LogicalPlan, fullyLogicalPlan: LogicalPlan) = true

    // Native execution
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[InternalRow]] = None

    // Spark execution
    override def buildScan(): RDD[Row] = spark.createDataFrame(spark.sparkContext.parallelize(Seq(Row(1))), StructType(Array(StructField("id", IntegerType)))).rdd
  }

  val mockNativeRelationWith2Rows = new MockBaseRelation with NativeScan with TableScan {
    override def isSupported(logicalStep: LogicalPlan, fullyLogicalPlan: LogicalPlan) = true

    // Native execution
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[InternalRow]] = Some(Array(nativeRows(0), nativeRows(0)))

    // Spark execution
    override def buildScan(): RDD[Row] = spark.createDataFrame(spark.sparkContext.parallelize(Seq(Row(1))), StructType(Array(StructField("id", IntegerType)))).rdd
  }

  val mockNativeRelationUnsupportedPlan = new MockBaseRelation with NativeScan with TableScan {
    override def isSupported(logicalStep: LogicalPlan, fullyLogicalPlan: LogicalPlan) = false

    // Native execution
    override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[InternalRow]] = Some(nativeRows)

    // Spark execution
    override def buildScan(): RDD[Row] = spark.createDataFrame(spark.sparkContext.parallelize(Seq(Row(1))), StructType(Array(StructField("id", IntegerType)))).rdd
  }

}
