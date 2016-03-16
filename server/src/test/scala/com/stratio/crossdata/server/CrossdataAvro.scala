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
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CrossdataAvro extends SharedXDContextTest with ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[CrossdataAvro])

  "Crossdata" should "execute csv queries" in {

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
