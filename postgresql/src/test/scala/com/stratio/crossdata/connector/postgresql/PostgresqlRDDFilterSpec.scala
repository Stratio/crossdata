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

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.execution.datasources.jdbc.PostgresqlRDD
import org.apache.spark.sql.sources._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PostgresqlRDDFilterSpec extends BaseXDTest {

  val att1 = "att1"
  val att2 = "att2"
  val att3 = "att3"

  val val1 = "val1"
  val val2 = "val2"
  val val3 = "val3"

  val equal1 = EqualTo(att1, val1)
  val equal2 = EqualTo(att2, val2)
  val lessThan = LessThan(att1, val1)
  val greaterThan = GreaterThan(att1, val1)
  val lessThanOrEqual = LessThanOrEqual(att1, val1)
  val greaterThanOrEqual = GreaterThanOrEqual(att1, val1)
  val isNull = IsNull(att1)
  val isNotNull = IsNotNull(att1)
  val startsLike = StringStartsWith(att1, val1)
  val endsLike = StringEndsWith(att1, val1)
  val containsLike = StringContains(att1, val1)
  val inValues: Array[Any] = Array(val1, val2)
  val in = In(att1, inValues)
  val notFilter = Not(equal1)
  val orFilter = Or(equal1, equal2)
  val andFilter = And(equal1, equal2)
  val orAndAndFilter = And(orFilter, andFilter)
  val orAndAndOrFilter = Or(orAndAndFilter, equal1)
  val orAndOrOrFilter = Or(And(orFilter, orFilter), equal1)

  "PostgresqlRDD " should "translate Equal filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(equal1).get
    stringOrFilter should be ("att1 = 'val1'")
  }

  "PostgresqlRDD " should "translate LessThan filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(lessThan).get
    stringOrFilter should be ("att1 < 'val1'")
  }

  "PostgresqlRDD " should "translate GreaterThan filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(greaterThan).get
    stringOrFilter should be ("att1 > 'val1'")
  }

  "PostgresqlRDD " should "translate LessThanOrEqual filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(lessThanOrEqual).get
    stringOrFilter should be ("att1 <= 'val1'")
  }

  "PostgresqlRDD " should "translate GreaterThanOrEqual filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(greaterThanOrEqual).get
    stringOrFilter should be ("att1 >= 'val1'")
  }

  "PostgresqlRDD " should "translate IsNull filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(isNull).get
    stringOrFilter should be ("att1 IS NULL")
  }

  "PostgresqlRDD " should "translate IsNotNull filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(isNotNull).get
    stringOrFilter should be ("att1 IS NOT NULL")
  }

  "PostgresqlRDD " should "translate StringStartsWith filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(startsLike).get
    stringOrFilter should be ("att1 LIKE 'val1%'")
  }

  "PostgresqlRDD " should "translate StringEndsWith filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(endsLike).get
    stringOrFilter should be ("att1 LIKE '%val1'")
  }

  "PostgresqlRDD " should "translate StringContains filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(containsLike).get
    stringOrFilter should be ("att1 LIKE '%val1%'")
  }

  "PostgresqlRDD " should "translate In filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(in).get
    stringOrFilter should be ("att1 IN ('val1', 'val2')")
  }

  "PostgresqlRDD " should "translate Not filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(notFilter).get
    stringOrFilter should be ("(NOT (att1 = 'val1'))")
  }

  "PostgresqlRDD " should "translate OR filter" in {
    val stringOrFilter: String = PostgresqlRDD.compileFilter(orFilter).get
    stringOrFilter should be ("((att1 = 'val1') OR (att2 = 'val2'))")
  }

  "PostgresqlRDD " should "translate AND filter" in {
    val stringAndFilter: String = PostgresqlRDD.compileFilter(andFilter).get
    stringAndFilter should be ("((att1 = 'val1') AND (att2 = 'val2'))")
  }

  "PostgresqlRDD " should "translate AND(OR, AND) filter" in {
    val stringOrAndAndFilter: String = PostgresqlRDD.compileFilter(orAndAndFilter).get
    stringOrAndAndFilter should be ("((((att1 = 'val1') OR (att2 = 'val2'))) AND (((att1 = 'val1') AND (att2 = 'val2'))))")
  }

  "PostgresqlRDD " should "translate OR(AND(OR, AND),equal) filter" in {
    val stringOrAndAndOrFilter: String = PostgresqlRDD.compileFilter(orAndAndOrFilter).get
    stringOrAndAndOrFilter should be ("((((((att1 = 'val1') OR (att2 = 'val2'))) AND (((att1 = 'val1') AND (att2 = 'val2'))))) OR (att1 = 'val1'))")
  }

  "PostgresqlRDD " should "translate OR(AND(OR, OR),equal) filter" in {
    val stringOrAndOrOrFilterFilter: String = PostgresqlRDD.compileFilter(orAndOrOrFilter).get
    stringOrAndOrOrFilterFilter should be ("((((((att1 = 'val1') OR (att2 = 'val2'))) AND (((att1 = 'val1') OR (att2 = 'val2'))))) OR (att1 = 'val1'))")
  }

}


