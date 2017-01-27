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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlCreateExternalTableIT extends PostgresqlWithSharedContext {

  "The Postgresql connector" should "execute natively create a External Table" in {

    val tableName = "newtable"

    val createTableQueryString =
      s"""|CREATE EXTERNAL TABLE $postgresqlSchema.$tableName (
          |id Integer,
          |name String,
          |booleanFile Boolean,
          |timeTime Timestamp
          |)
          |USING $SourceProvider
          |OPTIONS (
          |url '$url',
          |primary_key_string 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")

    try {
      //Experimentation
      sql(createTableQueryString).collect()

      //Expectations
      val table = xdContext.table(s" $postgresqlSchema.$tableName")
      table should not be null
      table.schema.fieldNames should contain("name")

      val resultSet = client.get._2.executeQuery(s"SELECT * FROM $postgresqlSchema.$tableName")
      resultSet.getMetaData.getColumnName(2) should be("name")

    } finally {
      client.get._2.execute(s"DROP TABLE $postgresqlSchema.$tableName")
    }
  }

  it should "execute natively create a External Table with no existing schema" in {
    val noExistingSchema = "newschema"
    val newTable = "othertable"

    val createTableQueryString =
      s"""|CREATE EXTERNAL TABLE $noExistingSchema.$newTable(
          |id Integer,
          |name String,
          |booleanFile Boolean,
          |timeTime Timestamp
          |)
          |USING $SourceProvider
          |OPTIONS (
          |url '$url',
          |primary_key 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")

    try {
      //Experimentation
      sql(createTableQueryString).collect()

      //Expectations
      val table = xdContext.table(s"$noExistingSchema.$newTable")
      table should not be null
      table.schema.fieldNames should contain("name")
    }finally {
      //AFTER
      client.get._2.execute(s"DROP TABLE $noExistingSchema.$newTable")
      client.get._2.execute(s"DROP SCHEMA $noExistingSchema")
    }
  }

  it should "throw an exception executing create External Table without specify schema" in {
    val newTable = "othertable"

    val createTableQueryString =
      s"""|CREATE EXTERNAL TABLE $newTable(
          |id Integer,
          |name String,
          |booleanFile Boolean,
          |timeTime Timestamp
          |)
          |USING $SourceProvider
          |OPTIONS (
          |url '$url',
          |primary_key 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")

    an [java.lang.IllegalArgumentException] shouldBe thrownBy(sql(createTableQueryString).collect())
  }

  it should "throw an exception executing create External Table when table already exists in Crossdata or Postgresql" in {
    val newTable = "othertable"

    val createTableQueryString =
      s"""|CREATE EXTERNAL TABLE $postgresqlSchema.$newTable(
          |id Integer,
          |name String,
          |booleanFile Boolean,
          |timeTime Timestamp
          |)
          |USING $SourceProvider
          |OPTIONS (
          |url '$url',
          |primary_key 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")

    sql(createTableQueryString).collect()
    // TABLE exists in crossdata
    an [org.apache.spark.sql.AnalysisException] shouldBe thrownBy(sql(createTableQueryString).collect())

    val newTable2= "anothertable"
    client.get._2.execute(s"CREATE TABLE $postgresqlSchema.$newTable2 (id text, age integer, primary key (id))")
    //TABLE exists in postgresql
    val createTableQueryString2 =
      s"""|CREATE EXTERNAL TABLE $postgresqlSchema.$newTable2(
          |id Integer,
          |name String,
          |booleanFile Boolean,
          |timeTime Timestamp
          |)
          |USING $SourceProvider
          |OPTIONS (
          |url '$url',
          |primary_key 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")

    an [RuntimeException] shouldBe thrownBy(sql(createTableQueryString2).collect())
  }

}
