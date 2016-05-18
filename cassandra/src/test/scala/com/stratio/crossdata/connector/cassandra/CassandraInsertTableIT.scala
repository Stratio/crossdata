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
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraInsertTableIT extends CassandraWithSharedContext {

  it should "insert a row using INSERT INTO table VALUES in JSON" in {
    _xdContext.sql(s"INSERT INTO $Table VALUES (200, 25, 'proof description', true, 'pepe')").collect() should be (Row(1)::Nil)
  }

  it should "insert a row using INSERT INTO table(schema) VALUES in JSON" in {
    _xdContext.sql(s"INSERT INTO $Table(id,age,comment) VALUES ( 201, 25, 'pepe')").collect() should be (Row(1)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table VALUES in JSON" in {
    val query = s"""|INSERT INTO $Table VALUES
        |(202, 25, 'proof description', true, 'pepe123'),
        |(203, 1, 'other description', false, 'pepe23'),
        |(204, 33, 'other fun description', false, 'pepe123')
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be (Row(3)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in JSON" in {
    _xdContext.sql(s"INSERT INTO $Table(id,age,comment, enrolled) VALUES ( 205,252, 'pepe2', true),( 206,1, 'asd', false)").collect() should be (Row(2)::Nil)
  }

}
