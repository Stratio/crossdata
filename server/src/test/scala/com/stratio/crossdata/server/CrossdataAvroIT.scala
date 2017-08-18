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