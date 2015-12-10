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

import java.sql.Date

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

    compareAfterFormatting(query.build, expected)
  }


  it should "be able to add a where clause on a limited query" in {

    val query = selectAll from 'table limit 1 where 'a < 5

    val expected = """
                     | SELECT * FROM table
                     | WHERE a < 5
                     | LIMIT 1
                   """

    compareAfterFormatting(query.build, expected)
  }

  it should "be able to add a where clause on a limited query which contains filters" in {

    val query = selectAll from 'table where 'a > 10 limit 1 where 'a < 5

    val expected = """
                     | SELECT * FROM table
                     | WHERE (a > 10) AND (a < 5)
                     | LIMIT 1
                   """

    compareAfterFormatting(query.build, expected)
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

    compareAfterFormatting(query.build, expected)
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

    compareAfterFormatting(query.build, expected)
  }

  it should "be able to build a query containing a subquery as a predicate" in {

    val query = select('c + 4).from('table).where('col === (select('c) from 't))

    val expected = """
                     | SELECT c + 4 FROM table
                     | WHERE col = ( SELECT c FROM t )
                   """
    compareAfterFormatting(query.build, expected)

  }

  it should " be able to build a completed query without distinct" in {

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

    compareAfterFormatting(query.build, expected)

  }

  it should " be able to build a query with a subquery" in {

    val query = select("alias.name") from ((selectAll from 'table) as 'alias)

    val expected = """
                     | SELECT alias.name
                     | FROM (
                     | SELECT * FROM table
                     | ) AS alias
                   """

    compareAfterFormatting(query.build, expected)

  }

  it should " be able to build a query with a join clause" in {

    val query = select('name, 'age, 'quantity) from ('test innerJoin 'animals on "test.id = animals.id")

    val expected = """
                     | SELECT name, age, quantity
                     | FROM test
                     | JOIN animals
                     | ON test.id = animals.id
                   """

    compareAfterFormatting(query.build, expected)
  }

  // TODO types tests

  // TODO precedence tests

  // TODO test by class

  // TODO tests below


  def compareAfterFormatting(query: String, expected: String) = {
    formatOutput(query) should be(formatOutput(expected))
  }


  def formatOutput(query: String): String =
    query.stripMargin.replaceAll(System.lineSeparator(), " ").trim.replaceAll(" +", " ")

}
