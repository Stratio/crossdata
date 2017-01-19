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
package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlInsertTableIT extends PostgresqlInsertCollection {

  it should "insert a row using INSERT INTO table VALUES in Postgresql" in {
    _xdContext.sql(s"INSERT INTO $postgresqlSchema.$Table VALUES (20, 25, 'proof description', true, 'Eve' )").collect() should be(Row(1) :: Nil)

    //EXPECTATION
    val results = sql(s"select * from $postgresqlSchema.$Table where id=20").collect()

    results should have length 1
    results should contain
    Row(20, 25, "proof description", true, "Eve")
  }

  it should "insert a row using INSERT INTO table(schema) VALUES in Postgresql" in {
    _xdContext.sql(s"INSERT INTO $postgresqlSchema.$Table(id, age, name) VALUES (21, 25, 'Peter')").collect() should be(Row(1) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age, enrolled from $postgresqlSchema.$Table where id=21").collect()

    results should have length 1
    results should contain
    Row(21, 25, "Peter")
  }

  it should "insert a row using INSERT INTO table(schema) VALUES without not nullable column in Postgresql" in {

    an [Exception] should be thrownBy (_xdContext.sql(s"INSERT INTO $postgresqlSchema.$Table(age, name) VALUES (25, 'Peter')").collect() should be(Row(1) :: Nil))

  }

  it should "insert multiple rows using INSERT INTO table VALUES in Postgresql" in {
    val query =
      s"""|INSERT INTO $postgresqlSchema.$Table VALUES
          |(22, 25, 'proof description', true, 'John' ),
          |(23, 1, 'other description', false, 'James' ),
          |(24, 33, 'other fun description', false, 'July' )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be(Row(3) :: Nil)

    //EXPECTATION
    val results = sql(s"select id,age,comment,enrolled,name from $postgresqlSchema.$Table where id=22 or id=23 or id=24").collect()

    results should have length 3
    results should contain allOf(

      Row(22, 25, "proof description", true, "John"),

      Row(23, 1, "other description", false, "James"),

      Row(24, 33, "other fun description", false, "July")
    )
  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in Postgresql" in {
    _xdContext.sql(s"INSERT INTO $postgresqlSchema.$Table(id, age, name, enrolled) VALUES ( 25, 50, 'Samantha', true),( 26, 1, 'Charlie', false)").collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age, name, enrolled from $postgresqlSchema.$Table where id=25 or id=26").collect()

    results should have length 2
    results should contain allOf(
      Row(25, 50, "Samantha", true),
      Row(26, 1, "Charlie", false)
    )
  }

}

