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

import java.sql.{Date, Timestamp}
import java.util.GregorianCalendar

import com.stratio.crossdata.driver.querybuilder
import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QueryBuilderSpec extends BaseXDTest {


  "The Query Builder" should "be able to build a completed query using strings" in {

    val query = select("col, '1', max(col)") from "table inner join table2 on a = b" where "a = b" groupBy "col" having "a = b" orderBy "col ASC" limit 5

    val expected = """
                     | SELECT col, '1', max(col)
                     | FROM table inner join table2 on a = b
                     | WHERE a = b
                     | GROUP BY col
                     | HAVING a = b
                     | ORDER BY col ASC
                     | LIMIT 5
                   """

    compareAfterFormatting(query, expected)
  }


  it should "be able to add a where clause on a limited query" in {

    val query = selectAll from 'table limit 1 where 'a < 5

    val expected = """
                     | SELECT * FROM table
                     | WHERE a < 5
                     | LIMIT 1
                   """

    compareAfterFormatting(query, expected)
  }

  it should "be able to add a where clause on a limited query which contains filters" in {

    val query = selectAll from 'table where 'a > 10 limit 1 where 'a < 5

    val expected = """
                     | SELECT * FROM table
                     | WHERE (a > 10) AND (a < 5)
                     | LIMIT 1
                   """

    compareAfterFormatting(query, expected)
  }


  it should "be able to join several queries" in {

    val query = (selectAll from 'table) unionAll (selectAll from 'table2) unionAll (selectAll from 'table3)

    val expected = """
                     | SELECT * FROM table
                     | UNION ALL
                     | SELECT * FROM table2
                     | UNION ALL
                     | SELECT * FROM table3
                   """

    compareAfterFormatting(query, expected)
  }

  it should "support union distinct to join runnable queries" in {

    val query = (selectAll from 'table) unionDistinct (selectAll from 'table2)

    val expected = """
                     | SELECT * FROM table
                     | UNION DISTINCT
                     | SELECT * FROM table2
                   """

    compareAfterFormatting(query, expected)
  }

  it should "support intersect to join runnable queries" in {

    val query = (selectAll from 'table) intersect (selectAll from 'table2)

    val expected = """
                     | SELECT * FROM table
                     | INTERSECT
                     | SELECT * FROM table2
                   """

    compareAfterFormatting(query, expected)
  }

  it should "support except to join runnable queries" in {

    val query = (selectAll from 'table) except (selectAll from 'table2)

    val expected = """
                     | SELECT * FROM table
                     | EXCEPT
                     | SELECT * FROM table2
                   """

    compareAfterFormatting(query, expected)
  }

  it should "not allow to add a filter on a combined query" in {

    the[Error] thrownBy {
      (selectAll from 'table) unionAll (selectAll from 'table2) where "a = b"
    } should have message "Predicates cannot by applied to combined queries"

  }

  it should "be able to build a query containing predicates with objects" in {

    val query = selectAll from 'table where ('a < new Date(0) or 'a > 5)

    val expected = """
                     | SELECT * FROM table
                     | WHERE
                     | (a < '1970-01-01')
                     | OR
                     | (a > 5)
                   """

    compareAfterFormatting(query, expected)
  }

  it should "be able to build a query containing a subquery as a predicate" in {

    val query = select('c + 4).from('table).where('col === (select('c) from 't))

    val expected = """
                     | SELECT c + 4 FROM table
                     | WHERE col = ( SELECT c FROM t )
                   """
    compareAfterFormatting(query, expected)

  }

  it should "be able to build a completed query without distinct" in {

    val query = select(distinct('col)) from 'test where ('quantity > 10) groupBy 'age having ('age > 25) orderBy 'age limit 10

    val expected = """
                     | SELECT DISTINCT col
                     | FROM test
                     | WHERE quantity > 10
                     | GROUP BY age
                     | HAVING age > 25
                     | ORDER BY age
                     | LIMIT 10
                   """

    compareAfterFormatting(query, expected)

  }

  it should "be able to build a query with a subquery" in {

    val query = select("alias.name") from ((selectAll from 'table) as 'alias)

    val expected = """
                     | SELECT alias.name
                     | FROM (
                     | SELECT * FROM table
                     | ) AS alias
                   """

    compareAfterFormatting(query, expected)

  }

  it should "be able to build a query with an inner join clause" in {

    val query = select('name, 'age, 'quantity) from ('test innerJoin 'animals on "test.id = animals.id")

    val expected = """
                     | SELECT name, age, quantity
                     | FROM test
                     | JOIN animals
                     | ON test.id = animals.id
                   """

    compareAfterFormatting(query, expected)
  }

  it should "be able to build a query with a left semi join clause" in {

    val query = selectAll from ('t1 leftSemiJoin 't2)

    val expected = """
                     | SELECT * FROM t1
                     | LEFT SEMI JOIN
                     | t2
                   """

    compareAfterFormatting(query, expected)
  }

  it should "be able to build a query with a left outer join clause" in {

    val query = selectAll from ('t1 leftOuterJoin 't2)

    val expected = """
                     | SELECT * FROM t1
                     | LEFT OUTER JOIN
                     | t2
                   """

    compareAfterFormatting(query, expected)
  }

  it should "be able to build a query with a right outer join clause" in {

    val query = selectAll from ('t1 rightOuterJoin 't2)

    val expected = """
                     | SELECT * FROM t1
                     | RIGHT OUTER JOIN
                     | t2
                   """

    compareAfterFormatting(query, expected)
  }

  it should "be able to build a query with a full outer join clause" in {

    val query = selectAll from ('t1 fullOuterJoin 't2)

    val expected = """
                     | SELECT * FROM t1
                     | FULL OUTER JOIN
                     | t2
                   """

    compareAfterFormatting(query, expected)
  }

  it should "be able to maintain user associations" in {

    val query = select (('a + 13) * ('hola + 2) + 5) from 'test

    val expected = """
                     | SELECT ((a +  13) * (hola + 2)) + 5
                     | FROM test
                   """

    compareAfterFormatting(query, expected)
  }


  it should "be able to support aliases" in {

    val query = select ('a as 'alias) from ('test as 'talias, (selectAll from 'table) as 'qalias)

    val expected = """
                     | SELECT a AS alias
                     | FROM test AS talias JOIN ( SELECT * FROM table ) AS qalias
                   """

    compareAfterFormatting(query, expected)
  }


  /*
  This test is here as documentation. Actually, its testing Scala since
  a mathematical precedence order is guaranteed by Scala's method names precedence table.

  Check "Programming in Scala: A comprehensive step-by-step guide", M.Ordersky,
  Section "5.8 - Operator precedence and associativity".
  */
  it should "make use of Scala's method names precedence rules" in {
    val query = select ('a, 'c - 'd * 'a) from 'test

    val expected = "SELECT a, c - (d * a) FROM test"

    compareAfterFormatting(query, expected)
  }

  it should "keep operator precedence provided by the user through the use of parenthesis" in {
    val query = select ('a, 'b * ( 'c - 'd )) from 'test

    val expected = "SELECT a, b * (c - d) FROM test"

    compareAfterFormatting(query, expected)
  }

  it should "generate correct queries using arithmetic operators" in {

    val arithmeticExpressions = ('a + 'b)::('c - 'd)::('e * 'f)::('g / 'h)::('i % 'j)::Nil
    val baseQuery = select (arithmeticExpressions:_*) from 'test

    val query = (baseQuery /: arithmeticExpressions) {
      (q, op) => q.where(op === 'ref)
    }

    val expectedExpressions = "a + b"::"c - d"::"e * f"::"g / h"::"i % j"::Nil
    val expected = s"""
                   |SELECT ${expectedExpressions mkString ", "}
                   |FROM test
                   |WHERE ${expectedExpressions.map(exp => s"($exp = ref)") mkString " AND "}
                   |""".stripMargin

    compareAfterFormatting(query.build, expected)

  }

  it should "generate insert-select queries" in {

    val selQueryStr = "SELECT a FROM sourceTable"

    Seq(
      (insert into 'test select 'a from 'sourceTable, s"INSERT INTO test $selQueryStr"),
      (insert overwrite 'test select 'a from 'sourceTable, s"INSERT OVERWRITE test $selQueryStr")
    ) foreach { case (query, expected) =>
      compareAfterFormatting(query.build, expected)
    }

  }

  it should "be able to support common functions in the select expression" in {
    val query = select(
      distinct('col), countDistinct('col), sumDistinct('col),
      count(querybuilder.all), approxCountDistinct('col, 0.95),
      avg('col), min('col), max('col), sum('col), abs('col)
    ) from 'table

    val expected = """
                     | SELECT DISTINCT col, count( DISTINCT col), sum( DISTINCT col),
                     | count(*), APPROXIMATE (0.95) count ( DISTINCT col),
                     | avg(col), min(col), max(col), sum(col), abs(col)
                     | FROM table
                   """

    compareAfterFormatting(query, expected)
  }


  it should "be able to allow different order selections" in {

    val queryAsc = selectAll from 'table orderBy('col asc)
    val queryDesc = selectAll from 'table sortBy('col desc)

    val expectedAsc =
      """
        | SELECT *
        | FROM table
        | ORDER BY col ASC
      """

    val expectedDesc =
      """
        | SELECT *
        | FROM table
        | SORT BY col DESC
      """

    compareAfterFormatting(queryAsc, expectedAsc)
    compareAfterFormatting(queryDesc, expectedDesc)

  }


  it should "be able to support comparison predicates" in {

    val query = selectAll from 'table where( !('a < 5 && 'a <= 5 && 'a > 5 && 'a >=5 && 'a === 5 && 'a <> 5 || false))

    val expected =
      """
        | SELECT *
        | FROM table
        | WHERE !(((a < 5) AND (a <= 5) AND (a > 5) AND (a >= 5) AND (a = 5) AND (a <> 5)) OR false)
      """

    compareAfterFormatting(query, expected)

  }

  it should "be able to support common predicates" in {

    val query = selectAll from 'table where ( ('a in (2,3,4)) && ('b like "%R") && ('b isNull) && ('b isNotNull))

    val expected =
      """
        | SELECT *
        | FROM table
        | WHERE ( a IN (2,3,4)) AND (b LIKE '%R') AND ( b IS NULL) AND ( b IS NOT NULL)
      """

    compareAfterFormatting(query, expected)

  }


  it should "be able to support SparkSQL types" in {

    val timestampVal = new Timestamp(new GregorianCalendar(1970,0,1,0,0,0).getTimeInMillis)

    val query = selectAll from 'table where ( ('a <> "string") &&  ('a <> 5f) && ('a <> true) && ('a <> timestampVal) && ('a <> new java.math.BigDecimal(1)))

    val expected =
      """
        | SELECT * FROM table
        | WHERE (a <> 'string') AND (a <> 5.0) AND (a <> true) AND (a <> '1970-01-01 00:00:00.0') AND (a <> 1)
        |
      """

    compareAfterFormatting(query, expected)

  }

  def compareAfterFormatting(query: RunnableQuery, expected: String) = {
    formatOutput(query.build) should be(formatOutput(expected))
  }


  def formatOutput(query: String): String =
    query.stripMargin.replaceAll(System.lineSeparator(), " ").trim.replaceAll(" +", " ")






}
