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

    _xdContext.sql(query).collect() should be(Row(1) :: Nil)

    //EXPECTATION
    val results = sql(s"select * from $Collection where id=20").collect()

    results should have length 1
    results should contain
    Row(20,
        25,
        "proof description",
        true,
        "Eve",
        false,
        Seq("proof"),
        Map("a" -> "2"),
        List(Map("x" -> "1", "y" -> "1"), Map("z" -> "1")),
        Map("x" -> List("1", "2"), "y" -> List("3", "4")))

  }

  it should "insert a row using INSERT INTO table(schema) VALUES in MongoDb" in {
    _xdContext
      .sql(s"INSERT INTO $Collection(age,name, enrolled) VALUES ( 25, 'Peter', true)")
      .collect() should be(Row(1) :: Nil)

    //EXPECTATION
    val results = sql(
        s"select age, name, enrolled from $Collection where age=25 and name='Peter'")
      .collect()

    results should have length 1
    results should contain
    Row(25, true, "Peter")
  }

  it should "insert multiple rows using INSERT INTO table VALUES in MongoDb" in {

    val query = s"""|INSERT INTO $Collection VALUES
        |(21, 25, 'proof description', true, 'John', false, [4,5], (x -> 1), [ (z -> 1) ], ( x->[1,2] ) ),
        |(22, 1, 'other description', false, 'James', true, [1,2,3], (key -> value), [ (a -> 1) ], ( x->[1,a] )),
        |(23, 33, 'other fun description', false, 'July', false, [true,true], (z->1, a-> 2), [ (za -> 12) ], ( x->[1,2] ) )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be(Row(3) :: Nil)

    //EXPECTATION
    val results =
      sql(s"select * from $Collection where id=21 or id=22 or id=23").collect()

    results should have length 3
    results should contain allOf (
        Row(21,
            25,
            "proof description",
            true,
            "John",
            false,
            Seq("4", "5"),
            Map("x" -> "1"),
            Seq(Map("z" -> "1")),
            Map("x" -> Seq("1", "2"))),
        Row(22,
            1,
            "other description",
            false,
            "James",
            true,
            Seq("1", "2", "3"),
            Map("key" -> "value"),
            Seq(Map("a" -> "1")),
            Map("x" -> Seq("1", "a"))),
        Row(23,
            33,
            "other fun description",
            false,
            "July",
            false,
            Seq("true", "true"),
            Map("z" -> "1", "a" -> "2"),
            Seq(Map("za" -> "12")),
            Map("x" -> Seq("1", "2")))
    )

  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in MongoDb" in {
    _xdContext
      .sql(s"INSERT INTO $Collection(age,name, enrolled) VALUES ( 50, 'Samantha', true),( 1, 'Charlie', false)")
      .collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(
        s"select age, enrolled, name from $Collection where (age=50 and name='Samantha') or (age=1 and name='Charlie')")
      .collect()

    results should have length 2
    results should contain allOf (
        Row(50, true, "Samantha"),
        Row(1, false, "Charlie")
    )
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Arrays in MongoDb" in {
    val query =
      s"""|INSERT INTO $Collection (age,name, enrolled, array_test) VALUES
                    |( 55, 'Jules', true, [true, false]),
                    |( 12, 'Martha', false, ['test1,t', 'test2'])
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(
        s"select age, name, enrolled, array_test from $Collection where (age=55 and name='Jules') or (age=12 and name='Martha')")
      .collect()

    results should have length 2
    results should contain allOf (
        Row(55, "Jules", true, Seq("true", "false")),
        Row(12, "Martha", false, Seq("test1,t", "test2"))
    )
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Map in MongoDb" in {
    val query =
      s"""|INSERT INTO $Collection (age,name, enrolled, map_test) VALUES
                    |( 12, 'Albert', true, (x->1, y->2, z->3) ),
                    |( 20, 'Alfred', false, (xa->1, ya->2, za->3,d -> 5) )
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(
        s"select age, name, enrolled, map_test from $Collection where (age=12 and name='Albert') or (age=20 and name='Alfred')")
      .collect()

    results should have length 2
    results should contain allOf (
        Row(12, "Albert", true, Map("x" -> "1", "y" -> "2", "z" -> "3")),
        Row(20,
            "Alfred",
            false,
            Map("xa" -> "1", "ya" -> "2", "za" -> "3", "d" -> "5"))
    )
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Array of Maps in MongoDb" in {
    val query =
      s"""|INSERT INTO $Collection (age,name, enrolled, array_map) VALUES
                    |( 1, 'Nikolai', true, [(x -> 3), (z -> 1)] ),
                    |( 14, 'Ludwig', false, [(x -> 1, y-> 1), (z -> 1)] )
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(
        s"select age, name, enrolled, array_map from $Collection where (age=1 and name='Nikolai') or (age=14 and name='Ludwig')")
      .collect()

    results should have length 2
    results should contain allOf (
        Row(1, "Nikolai", true, Seq(Map("x" -> "3"), Map("z" -> "1"))),
        Row(14,
            "Ludwig",
            false,
            Seq(Map("x" -> "1", "y" -> "1"), Map("z" -> "1")))
    )
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Map of Array in MongoDb" in {
    val query =
      s"""|INSERT INTO $Collection (age,name, enrolled, map_array) VALUES
                    |( 13, 'Svletiana', true, ( x->[1], y-> [3,4] ) ),
                    |( 17, 'Wolfang', false, ( x->[1,2], y-> [3] ) )
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(
        s"select age, name, enrolled, map_array from $Collection where (age=13 and name='Svletiana') or (age=17 and name='Wolfang')")
      .collect()

    results should have length 2
    results should contain allOf (
        Row(13, "Svletiana", true, Map("x" -> Seq("1"), "y" -> Seq("3", "4"))),
        Row(17, "Wolfang", false, Map("x" -> Seq("1", "2"), "y" -> Seq("3")))
    )
  }

}
