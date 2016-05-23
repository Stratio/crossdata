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
package com.stratio.crossdata.connector.mongodb

import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoInsertTableIT extends MongoInsertCollection {

  it should "insert a row using INSERT INTO table VALUES in MongoDb" in {
    _xdContext.sql(s"INSERT INTO $Collection VALUES (20, 25, 'proof description', true, 'Eve', false,['proof'])").collect() should be (Row(1)::Nil)
  }

  it should "insert a row using INSERT INTO table(schema) VALUES in MongoDb" in {
    _xdContext.sql(s"INSERT INTO $Collection(age,name, enrolled) VALUES ( 25, 'Peter', true)").collect() should be (Row(1)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table VALUES in MongoDb" in {
    val query = s"""|INSERT INTO $Collection VALUES
        |(21, 25, 'proof description', true, 'John', false, [4,5] ),
        |(22, 1, 'other description', false, 'James', true, [1,2,3] ),
        |(23, 33, 'other fun description', false, 'July', false, [true,true] )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be (Row(3)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in MongoDb" in {
    _xdContext.sql(s"INSERT INTO $Collection(age,name, enrolled) VALUES ( 50, 'Samantha', true),( 1, 'Charlie', false)").collect() should be (Row(2)::Nil)
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Arrays in MongoDb" in {
    val query = s"""|INSERT INTO $Collection (age,name, enrolled, array_test) VALUES
                    |( 55, 'Jules', true, [true, false]),
                    |( 12, 'Martha', false, ['test1,t', 'test2'])
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

}
