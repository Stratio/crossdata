/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.execution.udaf

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner




@RunWith(classOf[JUnitRunner])
class UdafsIT extends SharedXDContextTest {

  private val TableName = "tableId"
  private val DatabaseName = "dbId"
  private val DatasourceName = "json"
  private val Schema = StructType(Seq(StructField("col", StringType)))

  "XDContext" should "resolve a query with the UDAF group_concat" in {
    val tempContext = _xdContext

    val schema = StructType(Seq(StructField("name", StringType), StructField("age", IntegerType)))

    val df = _xdContext.createDataFrame(_xdContext.sc.parallelize(Seq(Row("Torcuato", 27), Row("Rosalinda", 34), Row("Arthur", 41))), schema)

    df.registerTempTable("udafs_test_gc")

    val result = sql(s"SELECT group_concat(name) FROM udafs_test_gc")

    result.first().getAs[String](0) shouldBe "Torcuato, Rosalinda, Arthur"

  }

}
