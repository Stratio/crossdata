/*
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
class CassandraInsertTableIT extends CassandraInsertCollection {

  it should "insert a row using INSERT INTO table VALUES in Cassandra" in {
    _xdContext.sql(s"INSERT INTO $Table VALUES (20, 25, ['proof'], 'proof description',  true, (a->2), 'Eve')").collect() should be (Row(1)::Nil)
  }

  it should "insert a row using INSERT INTO table(schema) VALUES in Cassandra" in {
    _xdContext.sql(s"INSERT INTO $Table(id, age, name) VALUES (21, 25, 'Peter')").collect() should be (Row(1)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table VALUES in Cassandra" in {
    val query = s"""|INSERT INTO $Table VALUES
                    |(22, 25, [4,5], 'proof description', true, (x -> 1), 'John'  ),
                    |(23, 1, [1,2,3], 'other description', false, (key -> value), 'James'),
                    |(24, 33, [true,true], 'other fun description', false, (z->1, a-> 2), 'July' )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be (Row(3)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in Cassandra" in {
    _xdContext.sql(s"INSERT INTO $Table(id, age, name, enrolled) VALUES ( 25, 50, 'Samantha', true),( 26, 1, 'Charlie', false)").collect() should be (Row(2)::Nil)
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Arrays in Cassandra" in {
    val query = s"""|INSERT INTO $Table (id, age, name, enrolled, array_test) VALUES
                    |(27, 55, 'Jules', true, [true, false]),
                    |(28, 12, 'Martha', false, ['test1,t', 'test2'])
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Map in Cassandra" in {
    val query = s"""|INSERT INTO $Table (id, age, name, enrolled, map_test) VALUES
                    |( 29, 12, 'Albert', true, (x->1, y->2, z->3) ),
                    |( 30, 20, 'Alfred', false, (xa->1, ya->2, za->3,d -> 5) )
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

  /*it should "insert rows using INSERT INTO table(schema) VALUES with Map and Arrays in MongoDb" in {
    val query = s"""|INSERT INTO $Collection (age,name, enrolled, map_test) VALUES
                    |( 1, 'Nikolai', true, (x->[1,2], y->2, z->3) ),
                    |( 14, 'Ludwig', false, (xa->1, ya->[5, 7, 8], za->3,d -> 5) )
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }*/

}
