/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.mongodb

import org.apache.spark.sql.crossdata.ExecutionType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongodbDataTypesIT extends MongoDataTypesCollection{

  override val emptyTypesSetError: String = "Type test entries should have been already inserted"

  doTypesTest("The MongoDB connector")

  it should "be able to natively select array elements using their index" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT arraystring, arraystring[2], arraystring[-1], arrayint[0] FROM typesCheckTable")
    val firstRow = df.collect(ExecutionType.Native).head

    firstRow(0) shouldBe a[Seq[_]]    // Whole `arraystring` column
    firstRow(1) shouldBe a[String]    // Access to a single element within a string array
    Option(firstRow(2)) shouldBe None // Access to an out-of-bounds index
    firstRow(3) shouldBe a[Integer]   // Access to a single element within an int array

  }

  it should "to natively filter by array column indexed elements" in {
    assumeEnvironmentIsUpAndRunning

    val query =
      """|SELECT arraystring, arraystring[2], arraystring[-1], arrayint[0]
         | FROM typesCheckTable
         | WHERE (arrayint[0] = 1 OR arrayint[1] = 1) AND arrayint[2] = 3
      """.stripMargin.replace("\n", "")

    val df = sql(query)
    val res = df.collect(ExecutionType.Native)

    res.length should equal(10)

  }


}
