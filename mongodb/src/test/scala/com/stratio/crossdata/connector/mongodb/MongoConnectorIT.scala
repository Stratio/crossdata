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
package com.stratio.crossdata.connector.mongodb

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.sql.crossdata.ExecutionType._
import org.apache.spark.sql.crossdata.exceptions.CrossdataException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoConnectorIT extends MongoWithSharedContext {

  //(id BIGINT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN)


  "The Mongo connector" should "execute natively a select *" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection ").collect(Native)
    result should have length 10
  }

  it should "execute natively a simple project" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT id FROM $Collection ").collect(Native)
    result should have length 10
    result(0) should have length 1
  }

  it should "execute natively an EqualTo filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE id = 1").collect(Native)
    result should have length 1
  }

  it should "execute natively an In filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE id IN ('1','5','9')").collect(Native)
    result should have length 3
  }

  it should "execute natively a LessThan filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE age < 13").collect(Native)
    result should have length 2
  }

  it should "execute natively a LessThanOrEqual filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE age <= 13").collect(Native)
    result should have length 3
  }

  it should "execute natively a GreaterThan filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE age > 13").collect(Native)
    result should have length 7
  }

  it should "execute natively a GreaterThanOrEqual filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE age >= 13").collect(Native)
    result should have length 8
  }

  it should "execute natively an IsNull filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE optionalField IS NULL").collect(Native)
    result should have length 4
  }

  it should "execute natively an IsNotNull filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE optionalField IS NOT NULL").collect(Native)
    result should have length 6
  }

  it should "execute natively a StringStartsWith filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE description LIKE 'descr%'").collect(Native)
    result should have length 10
  }

  it should "execute natively a StringEndsWith filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE description LIKE '%ion1'").collect(Native)
    result should have length 1
  }

  it should "execute natively a StringContains filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE description LIKE '%ion%'").collect(Native)
    result should have length 10
  }

  it should "execute natively an AND filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE id = 3 AND age = 13 AND description = 'description3' ").collect(Native)
    result should have length 1
  }

  it should "execute natively an OR filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE id = 3 OR id = 4 ").collect(Native)
    result should have length 2
  }

  it should "execute natively an AND filter in a complex query (nested filters)" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection WHERE id = 3 OR (age = 14 AND description = 'description4') ").collect(Native)
    result should have length 2
  }

  it should "execute natively a query with LIMIT 0" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Collection LIMIT 0").collect(Native)
    result should have length 0
  }


  // NOT SUPPORTED => JOIN
  it should "not execute natively a (SELECT * ...  ORDER BY _ )" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT * FROM $Collection CROSS JOIN $Collection").collect(Native)
    } should have message "The operation cannot be executed without Spark"
  }



  // IMPORT OPERATIONS

  it should "import all user collections" in {
    assumeEnvironmentIsUpAndRunning

    def tableCountInHighschool: Long = sql("SHOW TABLES").count()
    val initialLength = tableCountInHighschool

    //This crates a new collection in the database which will not be initially registered at the Spark
    val client = MongoClient(MongoHost, MongoPort)(Database)(UnregisteredCollection).insert(MongoDBObject("id" -> 1))

    // TODO import tables should return the tables registered
    sql(
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |host '$MongoHost:$MongoPort',
         |schema_samplingRatio  '0.1'
         |)
      """.stripMargin)

    // TODO We need to create an unregister table
    // TODO We have to modify the test when the new catalog is ready
    tableCountInHighschool should be > initialLength

  }

  it should "not import tables for sentences lacking required options:" in {
    assumeEnvironmentIsUpAndRunning
    an[Exception] shouldBe thrownBy(sql(s"IMPORT TABLES USING $SourceProvider"))
  }

}





