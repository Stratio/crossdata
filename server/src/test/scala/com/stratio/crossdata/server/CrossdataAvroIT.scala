/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.server

import java.nio.file.Paths

import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CrossdataAvroIT extends SharedXDContextTest {

  "Crossdata" should "execute avro queries" in {

    try{
      sql(s"CREATE TABLE test USING com.databricks.spark.avro OPTIONS (path '${Paths.get(getClass.getResource("/test.avro").toURI()).toString}')")
      val result = sql("SELECT * FROM test").collect()
      result should have length 3
      result.head should have length 12

    } finally {
      sql("DROP TABLE test")
    }
  }

}