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
package org.apache.spark.sql.querybuilder

import com.stratio.crossdata.sql.querybuilder.QueryBuilder
import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QueryBuilderTest extends BaseXDTest {

  def formatOutput(query: String): String = query.stripMargin.replace(System.lineSeparator(), " ").replaceAll ("""\s\s+""", " ").trim

  "The Query Builder" should " be able to build a completed query with distinct" in {

    val query = QueryBuilder
      .Select()
      .Distinct(List("*"))
      .From(List("test"))
      .Where("quantity > 10")
      .GroupBy(List("age"))
      .Having("age > 25")
      .OrderBy("age")
      .Limit("10")
      .build()
      .toString

    val expected = formatOutput("""SELECT DISTINCT *
                     | FROM test
                     | WHERE quantity > 10
                     | GROUP BY age
                     | HAVING age > 25
                     | ORDER BY age
                     | LIMIT 10""")

    query should be (expected)
  }

  "The Query Builder" should " be able to build a completed query without distinct" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where("quantity > 10")
      .GroupBy(List("age"))
      .Having("age > 25")
      .OrderBy("age")
      .Limit("10")
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

  "The Query Builder" should " be able to build a query with an empty having" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where("quantity > 10")
      .GroupBy(List("age"))
      .Having()
      .OrderBy("age")
      .Limit("10")
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

  "The Query Builder" should " be able to build a query with an empty group by" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where("quantity > 10")
      .GroupBy()
      .Having()
      .OrderBy("age")
      .Limit("10")
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                     | FROM test
                     | WHERE quantity > 10
                     | ORDER BY age
                     | LIMIT 10""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with an empty limit" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where("quantity > 10")
      .GroupBy()
      .Having()
      .OrderBy("age")
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                     | FROM test
                     | WHERE quantity > 10
                     | ORDER BY age""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with an empty where clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where()
      .GroupBy()
      .Having()
      .OrderBy("age")
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test
                                  | ORDER BY age""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with an empty order by" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where()
      .GroupBy()
      .Having()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with a subquery" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List(QueryBuilder.Select(true)
            .Projections(List("*"))
            .From(List("animals"))
            .Where()
            .GroupBy()
            .Having()
            .OrderBy()
            .Limit()
            .build()
            .toString))
      .Where()
      .GroupBy()
      .Having()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM (SELECT * FROM animals)""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with a join clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test INNER JOIN animals ON test.id = animals.id"))
      .Where()
      .GroupBy()
      .Having()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test INNER JOIN animals ON test.id = animals.id""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query skipping the limit clause" in {

    val query = QueryBuilder
      .Select()
      .Distinct(List("*"))
      .From(List("test"))
      .Where()
      .GroupBy()
      .Having()
      .OrderBy()
      .build()
      .toString
    val expected = formatOutput("""SELECT DISTINCT *
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query skipping the order by clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("id, name, age"))
      .From(List("test"))
      .Where()
      .GroupBy()
      .Having()
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query skipping the having clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("id, name, age"))
      .From(List("test"))
      .Where()
      .GroupBy()
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query skipping the group by clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("id, name, age"))
      .From(List("test"))
      .Where()
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query skipping the where clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("id, name, age"))
      .From(List("test"))
      .build()
      .toString
    val expected = formatOutput("""SELECT id, name, age
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the where clause missing from a from clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .GroupBy()
      .Having()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the group by clause missing from a from clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Having()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the having clause missing from a from clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the order by clause missing from a from clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the group by clause missing from a where clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where()
      .Having()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the having clause missing from a where clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the order by clause missing from a where clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .Where()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the having clause missing from a group by clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .GroupBy()
      .OrderBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the order by clause missing from a group by clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .GroupBy()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

  "The Query Builder" should " be able to build a query with the order by clause missing from a having clause" in {

    val query = QueryBuilder
      .Select()
      .Projections(List("name, age, quantity"))
      .From(List("test"))
      .GroupBy()
      .Having()
      .Limit()
      .build()
      .toString
    val expected = formatOutput("""SELECT name, age, quantity
                                  | FROM test""")
    query should be (expected)
  }

}
