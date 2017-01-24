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

import java.sql.{Connection, Statement}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlImportTablesIT extends PostgresqlWithSharedContext {

  it should "import all tables from a Postgresql Database" in {
    assumeEnvironmentIsUpAndRunning

    def tableCountInHighschool: Long = xdContext.sql("SHOW TABLES").count
    val initialLength = tableCountInHighschool

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |url "$url"
         |)
      """.stripMargin

    val importedTables = xdContext.sql(importQuery)

    importedTables.schema.fieldNames shouldBe Array("tableIdentifier", "ignored")
    importedTables.collect.length should be > 0

    tableCountInHighschool should be > initialLength

  }

  it should "infer schema after import all tables from a Postgresql Database" in {
    assumeEnvironmentIsUpAndRunning

    val (conn, statement) = createOtherTables
    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |url "$url"
         |)
      """.stripMargin

    try {
      xdContext.dropTempTable(s"$postgresqlSchema.$Table")
      sql(importQuery)

      xdContext.tableNames().size should be > 1
      xdContext.table(s"$postgresqlSchema.$Table").schema should have length 5
    } finally {
      cleanOtherTables(conn, statement)
    }
  }

  it should "infer schema after import all tables from a postgresql schema" in {
    assumeEnvironmentIsUpAndRunning

    val (conn, statement) = createOtherTables

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |url "$url",
         |schema "highschool"
         |)
    """.stripMargin

    try {
      xdContext.dropAllTables()
      val importedTables = sql(importQuery)
      // imported tables shouldn't be ignored (schema is (tableName, ignored)
      importedTables.collect().forall( row => row.getBoolean(1)) shouldBe false
      // imported tables should be ignored after importing twice
      sql(importQuery).collect().forall( row => row.getBoolean(1)) shouldBe true
      xdContext.tableNames()  should  contain (s"$postgresqlSchema.$Table")
      xdContext.tableNames()  should  not contain "newschema.newtable"
    } finally {
      cleanOtherTables(conn, statement)
    }
  }

  it should "infer schema after import One table from a schema" in {
    assumeEnvironmentIsUpAndRunning
    xdContext.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |url "$url",
         |dbtable "$postgresqlSchema.$Table"
         |)
    """.stripMargin

    //Experimentation
    sql(importQuery)

    //Expectations
    xdContext.tableNames() should contain(s"$postgresqlSchema.$Table")
    xdContext.tableNames() should not contain "highschool.teachers"
  }

  it should "fail when infer schema using table without schema" in {
    assumeEnvironmentIsUpAndRunning
    xdContext.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         | url "$url",
         | dbtable "$Table"
         |)
    """.stripMargin

    //Experimentation
    an [IllegalArgumentException] should be thrownBy sql(importQuery)
  }

  val wrongImportTablesSentences = List(
    s"""
       |IMPORT TABLES
       |USING $SourceProvider
       |OPTIONS (
       | schema "highschool"
       |)
    """.stripMargin,
    s"""
       |IMPORT TABLES
       |USING $SourceProvider
       |OPTIONS (
       | dbtable "$postgresqlSchema$Table"
       |)
     """.stripMargin
  )

  wrongImportTablesSentences.take(1) foreach { sentence =>
    it should s"not import tables for sentences lacking mandatory options: $sentence" in {
      assumeEnvironmentIsUpAndRunning
      an[Exception] shouldBe thrownBy(xdContext.sql(sentence))
    }
  }


  def createOtherTables(): (Connection, Statement) ={
    val  (conn, statement) =  prepareClient.get

    statement.execute(s"CREATE SCHEMA newschema")
    statement.execute(s"CREATE TABLE newschema.newtable(id integer, coolstuff text, PRIMARY KEY (id))")

    (conn, statement)
  }

  def cleanOtherTables(conn: Connection, statement: Statement): Unit ={
    statement.execute(s"DROP SCHEMA newschema CASCADE")

    statement.close()
    conn.close()
  }


  it should "infer schema after import One table from a postgresql schema using API" in {
    assumeEnvironmentIsUpAndRunning
    xdContext.dropAllTables()

    val options = Map(
      "url" -> url,
      "dbtable" ->  s"$postgresqlSchema.$Table"
    )

    //Experimentation
    xdContext.importTables(SourceProvider, options)

    //Expectations
    xdContext.tableNames() should contain(s"$postgresqlSchema.$Table")
    xdContext.tableNames() should not contain "highschool.teachers"
  }
}