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
package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.And
import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QueryBuilderSpec extends BaseXDTest {

  def formatOutput(query: String): String = query.stripMargin.replace(System.lineSeparator(), " ").replaceAll ("""\s\s+""", " ").trim

  import com.stratio.crossdata.driver.querybuilder._

  "The Query Builder" should " be able to build a completed query with distinct" in {
    //import Literal

    val query = select("count(*)", 'col).from('table join 'table2 on "5 + 4" = 'columna6).where("a = 5")
/*    val query = QueryBuilder
      .select()
      .distinct(List("*"))
      .from(List("test"))
      .where("quantity > 10")
      .groupBy(List("age"))
      .having("age > 25")
      .orderBy("age")
      .limit("10")
      .build()
      .toString*/

    val expected = formatOutput("""SELECT DISTINCT *
                     | FROM test
                     | WHERE quantity > 10
                     | GROUP BY age
                     | HAVING age > 25
                     | ORDER BY age
                     | LIMIT 10""")

    query should be (expected)
  }

/*  it should " be able to build a completed query without distinct" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where("quantity > 10")
      .groupBy(List("age"))
      .having("age > 25")
      .orderBy("age")
      .limit("10")
      .build()
      .toString

    val expected = formatOutput("""SELECT name, age, quantity
                    | FROM test
                    | WHERE quantity > 10
                    | GROUP BY age
                    | HAVING age > 25
                    | ORDER BY age
                    | LIMIT 10""")

    query should be (expected)
  }

  it should " be able to build a query with an empty having" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where("quantity > 10")
      .groupBy(List("age"))
      .having()
      .orderBy("age")
      .limit("10")
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                     | FROM test
                     | WHERE quantity > 10
                     | GROUP BY age
                     | ORDER BY age
                     | LIMIT 10""")
    query should be (expected)
  }

  it should " be able to build a query with an empty group by" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where("quantity > 10")
      .groupBy()
      .having()
      .orderBy("age")
      .limit("10")
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                     | FROM test
                     | WHERE quantity > 10
                     | ORDER BY age
                     | LIMIT 10""")
    query should be (expected)
  }

  it should " be able to build a query with an empty limit" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where("quantity > 10")
      .groupBy()
      .having()
      .orderBy("age")
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                     | FROM test
                     | WHERE quantity > 10
                     | ORDER BY age""")
    query should be (expected)
  }

  it should " be able to build a query with an empty where clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where()
      .groupBy()
      .having()
      .orderBy("age")
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test
                                  | ORDER BY age""")
    query should be (expected)
  }

  it should " be able to build a query with an empty order by" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where()
      .groupBy()
      .having()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with a subquery" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List(QueryBuilder.select(true)
            .projections(List("*"))
            .from(List("animals"))
            .where()
            .groupBy()
            .having()
            .orderBy()
            .limit()
            .build()
            .toString))
      .where()
      .groupBy()
      .having()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM (SELECT * FROM animals)""")
    query should be (expected)
  }

  it should " be able to build a query with a join clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test INNER JOIN animals ON test.id = animals.id"))
      .where()
      .groupBy()
      .having()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test INNER JOIN animals ON test.id = animals.id""")
    query should be (expected)
  }

  it should " be able to build a query skipping the limit clause" in {

    val query = QueryBuilder
      .select()
      .distinct(List("*"))
      .from(List("test"))
      .where()
      .groupBy()
      .having()
      .orderBy()
      .build()
      .toString
    val expected = formatOutput("""SELECT DISTINCT *
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query skipping the order by clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("id, name, age"))
      .from(List("test"))
      .where()
      .groupBy()
      .having()
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query skipping the having clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("id, name, age"))
      .from(List("test"))
      .where()
      .groupBy()
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query skipping the group by clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("id, name, age"))
      .from(List("test"))
      .where()
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query skipping the where clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("id, name, age"))
      .from(List("test"))
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the where clause missing from a from clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .groupBy()
      .having()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the group by clause missing from a from clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .having()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the having clause missing from a from clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the order by clause missing from a from clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the group by clause missing from a where clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where()
      .having()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the having clause missing from a where clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the order by clause missing from a where clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .where()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the having clause missing from a group by clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .groupBy()
      .orderBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the order by clause missing from a group by clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .groupBy()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  it should " be able to build a query with the order by clause missing from a having clause" in {

    val query = QueryBuilder
      .select()
      .projections(List("name, age, quantity"))
      .from(List("test"))
      .groupBy()
      .having()
      .limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }*/

}
