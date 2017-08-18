/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.mongodb

import org.apache.spark.sql.crossdata.ExecutionType
import org.apache.spark.sql.crossdata.exceptions.CrossdataException
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest
import org.apache.spark.sql.types.StructField
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoDotsNotationIT extends MongoDataTypesCollection {


  it should "supports Projection with DOT notation using Spark" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT bigint, structofstruct.field1 FROM ${SharedXDContextTypesTest.dataTypesTableName}").collect(ExecutionType.Spark)

    sparkRow.head.schema.size should be (2)
    sparkRow.head.schema.head.isInstanceOf[StructField] should be (true)
  }

  it should "supports Projection with DOT notation with no ExecutionType defined" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT bigint, structofstruct.field1 FROM ${SharedXDContextTypesTest.dataTypesTableName}").collect()

    sparkRow.head.schema.size should be (2)
    sparkRow.head.schema.head.isInstanceOf[StructField] should be (true)
  }

  it should "Does not supports Projection with DOT notation in Native" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT bigint, structofstruct.field1 FROM ${SharedXDContextTypesTest.dataTypesTableName}")

    an [CrossdataException] should be thrownBy df.collect(ExecutionType.Native)
  }

  it should "supports Filters with DOT notation with no ExecutionType defined" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT int FROM ${SharedXDContextTypesTest.dataTypesTableName} WHERE struct.field2=3").collect()

    sparkRow.length should be (10)
  }

  it should "Does not supports Filters with DOT notation in Native" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT int FROM ${SharedXDContextTypesTest.dataTypesTableName} WHERE struct.field2=3")

    an [CrossdataException] should be thrownBy df.collect(ExecutionType.Native)
  }
}
