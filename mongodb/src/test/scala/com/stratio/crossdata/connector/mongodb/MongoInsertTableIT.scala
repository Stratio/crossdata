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

import com.mongodb.{BasicDBList, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class MongoInsertTableIT extends MongoInsertCollection {

  it should "insert a row using INSERT INTO table VALUES in MongoDb" in {

    val query =
      s"""|INSERT INTO $Collection VALUES (20, 25, 'proof description', true, 'Eve', false,
          |['proof'], (a->2), [ (x -> 1, y-> 1), (z -> 1) ], ( x->[1,2], y-> [3,4] ) )""".stripMargin

    _xdContext.sql(query).collect() should be (Row(1)::Nil)

    //EXPECTATION
    val results = sql(s"select * from $Collection where id=20").collect()

    results map { row =>
      row.getAs[Int]("id") shouldBe 20
      row.getAs[Int]("age") shouldBe 25
      row.getAs[String]("description") shouldBe "proof description"
      row.getAs[Boolean]("enrolled") shouldBe true
      row.getAs[String]("name") shouldBe "Eve"
      row.getAs[Boolean]("optionalField") shouldBe false
      row.getAs[Seq[String]]("array_test") shouldBe Seq("proof")
      row.getAs[Map[String,String]]("map_test") shouldBe Map("a" -> "2")
      row.getAs[Seq[Map[String,String]]]("array_map") shouldBe List( Map("x" -> "1", "y" -> "1"), Map("z" -> "1"))
      row.getAs[Map[String,Seq[String]]]("map_array") shouldBe Map("x" -> List("1" , "2"), "y" -> List("3", "4"))
    }


  }

  it should "insert a row using INSERT INTO table(schema) VALUES in MongoDb" in {
    _xdContext.sql(s"INSERT INTO $Collection(age,name, enrolled) VALUES ( 25, 'Peter', true)").collect() should be (Row(1)::Nil)

    //EXPECTATION
    val results = sql(s"select * from $Collection where age=25 and name='Peter'").collect()

    results map { row =>
      row.getAs[Int]("age") shouldBe 25
      row.getAs[Boolean]("enrolled") shouldBe true
      row.getAs[String]("name") shouldBe "Peter"
    }
  }

  it should "insert multiple rows using INSERT INTO table VALUES in MongoDb" in {
    val query = s"""|INSERT INTO $Collection VALUES
        |(21, 25, 'proof description', true, 'John', false, [4,5], (x -> 1), [ (z -> 1) ], ( x->[1,2] ) ),
        |(22, 1, 'other description', false, 'James', true, [1,2,3], (key -> value), [ (a -> 1) ], ( x->[1,a] )),
        |(23, 33, 'other fun description', false, 'July', false, [true,true], (z->1, a-> 2), [ (za -> 12) ], ( x->[1,2] ) )
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

  it should "insert rows using INSERT INTO table(schema) VALUES with Map in MongoDb" in {
    val query = s"""|INSERT INTO $Collection (age,name, enrolled, map_test) VALUES
                    |( 12, 'Albert', true, (x->1, y->2, z->3) ),
                    |( 20, 'Alfred', false, (xa->1, ya->2, za->3,d -> 5) )
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Array of Maps in MongoDb" in {
    val query = s"""|INSERT INTO $Collection (age,name, enrolled, array_map) VALUES
                    |( 1, 'Nikolai', true, [(x -> 3), (z -> 1)] ),
                    |( 14, 'Ludwig', false, [(x -> 1, y-> 1), (z -> 1)] )
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Map of Array in MongoDb" in {
    val query = s"""|INSERT INTO $Collection (age,name, enrolled, map_array) VALUES
                    |( 13, 'Svletiana', true, ( x->[1], y-> [3,4] ) ),
                    |( 17, 'Wolfang', false, ( x->[1,2], y-> [3] ) )
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

}
