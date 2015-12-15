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
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.ExecutionType._
import org.apache.spark.sql.crossdata.exceptions.CrossdataException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraConnectorIT extends CassandraWithSharedContext {

  // PRIMARY KEY id
  // CLUSTERING KEY age, comment
  // DEFAULT enrolled
  // SECONDARY_INDEX name

  "The Cassandra connector" should "execute natively a (SELECT *)" in {
    assumeEnvironmentIsUpAndRunning
    val dataframe = sql(s"SELECT * FROM $Table ")
    val schema = dataframe.schema
    val result = dataframe.collect(Native)
    schema.fieldNames should equal (Seq("id", "age", "comment", "enrolled", "name"))
    result should have length 10
    result(0) should have length 5
  }


  it should "execute natively a query with limit 0" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table LIMIT 0").collect(Native)
    result should have length 0
  }

  it should "execute natively a (SELECT column)" in {

    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT id FROM $Table ").collect(Native)
    result should have length 10
    result(0) should have length 1
  }

  it should "execute natively a (SELECT * ... WHERE PK = _ )" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id = 1").collect(Native)
    result should have length 1
  }

  it should "execute natively a (SELECT * ... WHERE LAST_PK_COLUMN IN (...) )" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id IN (1,5,9)").collect(Native)
    result should have length 3
  }

  it should "execute natively a (SELECT * ...  WHERE CK._1 = _ AND CK._2 = _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE age = 13 AND comment = 'Comment 3' ").collect(Native)
    result should have length 1
  }

  it should "execute natively a (SELECT * ...  WHERE PK = _ AND CK._1 = _ AND CK._2 = _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id = 3 AND age = 13 AND comment = 'Comment 3' ").collect(Native)
    result should have length 1
  }

  it should "execute natively a (SELECT * ...  WHERE LUCENE_SEC_INDEX = _ )" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(
      s"""
         |SELECT * FROM $Table
         |WHERE name =
         |'{ filter  :
         |  {type:"fuzzy", field:"comment", value:"Komment"}
         |}'
         """.stripMargin.replaceAll("\n", " ")).collect(Native)
    result should have length 10
  }


  // NOT SUPPORTED FILTERS

  it should "not execute natively a (SELECT * ...  WHERE LUCENE_SEC_INDEX < _ )" in {
    assumeEnvironmentIsUpAndRunning

    the [CrossdataException] thrownBy {
      sql(s"""
          |SELECT * FROM $Table
          |WHERE name >
          |'{ filter  :
          |  {type:"fuzzy", field:"comment", value:"Komment"}
          |}'
         """.stripMargin.replaceAll("\n", " ")).collect(Native)
    } should have message "The operation cannot be executed without Spark"
  }

  it should "not execute natively a (SELECT * ...  WHERE DEFAULT_COLUM = _ )" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT * FROM $Table WHERE enrolled = 'true'").collect(Native)
    } should have message "The operation cannot be executed without Spark"
  }

  it should "not execute natively a (SELECT * ...  WHERE PK > _ )" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT * FROM $Table WHERE id > 3").collect(Native)
    } should have message "The operation cannot be executed without Spark"
  }

  it should "not execute natively a (SELECT * ...  ORDER BY _ )" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT * FROM $Table ORDER BY age").collect(Native)
    } should have message "The operation cannot be executed without Spark"
  }

  // TODO test filter on PKs (=) and CKs(any) (right -> left)

}





