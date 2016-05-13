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

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class InsertTableIT extends MongoWithSharedContext {

  protected override def beforeAll(): Unit = {

    // Ensure we have initialized the context before calling parent code
    super.beforeAll()
  }

  it should "insert a row using INSERT INTO table VALUES in JSON" in {
    _xdContext.sql(s"INSERT INTO $Collection VALUES (200, 25, 'proof description', true, 'pepe', false )").collect() should be (Row(1)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table VALUES in JSON" in {
    val query = s"""|INSERT INTO $Collection VALUES
        |(200, 25, 'proof description', true, 'pepe123', false ),
        |(15, 1, 'other description', false, 'pepe23', true ),
        |(6, 33, 'other fun description', false, 'pepe123', false )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be (Row(3)::Nil)
  }

}
