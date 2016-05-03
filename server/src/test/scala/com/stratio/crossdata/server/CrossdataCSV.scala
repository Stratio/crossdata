/**
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
package com.stratio.crossdata.server

import java.nio.file.Paths

import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata._
import org.apache.spark.sql.crossdata.test.{CoreWithSharedContext, SharedXDContextTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CrossdataCSV extends CoreWithSharedContext with ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[CrossdataCSV])

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
