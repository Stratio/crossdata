package com.stratio.crossdata.server

import java.nio.file.Paths

import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CrossdataCSVIT extends SharedXDContextTest {

  "Crossdata" should "execute csv queries" in {

    try{
      sql(s"CREATE TABLE cars USING com.databricks.spark.csv OPTIONS (path '${Paths.get(getClass.getResource("/cars.csv").toURI()).toString}', header 'true')")

      val result = sql("SELECT * FROM cars").collect()
      result should have length 8
      result.head should have length 9

    } finally {
      sql("DROP TABLE cars")
    }
  }



}
