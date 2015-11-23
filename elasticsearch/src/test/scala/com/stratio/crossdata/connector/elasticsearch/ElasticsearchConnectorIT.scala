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
package com.stratio.crossdata.connector.elasticsearch

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchConnectorIT extends ElasticWithSharedContext {

  "The ElasticSearch Connector execute natively a" should " select *" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type ")

    //Expectations
    val schema = dataframe.schema
    val result = dataframe.collect(Native)

    result should have length 10
    schema.fieldNames should equal (Seq("id", "age", "description", "enrolled", "name", "optionalField", "birthday"))
    result.head.toSeq(4).toString should fullyMatch regex "Name [0-9]+"
  }


  it should "select with projections" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type ")

    //Expectations
    val schema = dataframe.schema
    val result = dataframe.collect(Native)

    result should have length 10
    schema.fieldNames should equal (Seq("name", "age"))
    result.head.toSeq(0).toString should fullyMatch regex "Name [0-9]+"
  }

  it should "select with simple filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type where id = 1")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be (1)
  }

  it should "select with simple filter and projection" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type where id = 2")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be ("Name 2")
    result(0).get(1) should be (12)
  }


  it should "select with LT filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type where id < 5")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 4
    result(0).get(0).toString.toInt should be <5
  }

  it should "select with LTE filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type where id <= 5")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 5
    result(0).get(0).toString.toInt should be <= 5
  }

  it should "select with GTE filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type where id >= 5")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 6
    result(0).get(0).toString.toInt should be >= 5
  }

  it should "select with GT filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type where id > 5")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 5
    result(0).get(0).toString.toInt  should be > 5
  }


  it should "select with IN filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT id, name FROM $Type where id IN (3,4)")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 2
    result(0).get(0).toString.toInt should (be (3) or be (4))
    result.head.toSeq(1).toString should fullyMatch regex "Name [3,4]+"
  }


  it should "select with Null filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT id, name FROM $Type where enrolled Is Null")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 5
    result(0).get(0).toString.toInt % 2 should not be  0
    result.head.toSeq(1).toString should fullyMatch regex "Name [1,3,5,7,9]+"
  }

  it should "select with Not Null filter" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT id, name FROM $Type where enrolled Is Not Null")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 5
    result(0).get(0).toString.toInt % 2 should be (0)
    result.head.toSeq(1).toString should fullyMatch regex "Name [2,4,6,8,10]+"
  }

  it should "select with AND" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type where id > 2 AND age = 13")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be ("Name 3")
    result(0).get(1) should be (13)
  }

  it should "select with Equals String" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type where name ='Name 3'")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be ("Name 3")
    result(0).get(1) should be (13)
  }

  it should "select with Like String" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type where name like 'Name 3'")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be ("Name 3")
    result(0).get(1) should be (13)
  }

  it should "select with Like %String%" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type where description like '%name3%'")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be ("Name 3")
    result(0).get(1) should be (13)
  }

  it should "select with Like String%" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type where description like '4des%'")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be ("Name 4")
  }

  //TODO add support for dates in query?
  ignore should "select with Date Range" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT name, age FROM $Type where birthday = '1984-01-01T10:00:00-00:00'")

    //Expectations
    val result = dataframe.collect(Native)
    result should have length 1
    result(0).get(0) should be ("Name 4")
    result(0).get(1) should be (14)
  }
}

