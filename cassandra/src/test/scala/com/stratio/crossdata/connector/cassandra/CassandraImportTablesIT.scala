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

import com.datastax.driver.core.{Cluster, Session}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraImportTablesIT extends CassandraWithSharedContext {

  it should "import all tables from a keyspace" in {
    assumeEnvironmentIsUpAndRunning

    def tableCountInHighschool: Long = ctx.sql("SHOW TABLES").count
    val initialLength = tableCountInHighschool

    val importQuery =
      s"""
          |IMPORT TABLES
          |USING $SourceProvider
          |OPTIONS (
          | cluster "$ClusterName",
          | spark_cassandra_connection_host '$CassandraHost'
          |)
      """.stripMargin

    val importedTables = ctx.sql(importQuery)

    importedTables.schema.fieldNames shouldBe Array("tableIdentifier")
    importedTables.collect.length should be > 0

    // TODO We need to create an unregister the table
    tableCountInHighschool should be > initialLength

  }

  it should "infer schema after import all tables from a Cassandra Database" in {
    assumeEnvironmentIsUpAndRunning

    val (cluster, session) = createOtherTables
    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         | cluster "$ClusterName",
         | spark_cassandra_connection_host '$CassandraHost'
         |)
      """.stripMargin

    try {
      sql(importQuery)

      ctx.dropTempTable(s"$Catalog.$Table")

      ctx.tableNames().size should be > 1
      ctx.table(s"$Catalog.$Table").schema should have length 5
    } finally {
      cleanOtherTables(cluster, session)
    }
  }

  it should "infer schema after import all tables from a keyspace" in {
    assumeEnvironmentIsUpAndRunning

    val (cluster, session) = createOtherTables

    val importQuery =
      s"""
      |IMPORT TABLES
      |USING $SourceProvider
      |OPTIONS (
      | cluster "$ClusterName",
      | keyspace "$Catalog",
      | spark_cassandra_connection_host "$CassandraHost"
      |)
    """.stripMargin

    try {
      ctx.dropAllTables()
      sql(importQuery)
      ctx.tableNames()  should  contain (s"$Catalog.$Table")
      ctx.tableNames()  should  not contain "NewKeyspace.NewTable"
    } finally {
      cleanOtherTables(cluster, session)
    }
  }

  it should "infer schema after import One table from a keyspace" in {
    assumeEnvironmentIsUpAndRunning
    ctx.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
          |OPTIONS (
          | cluster "$ClusterName",
          | keyspace "$Catalog",
          | table "$Table",
          | spark_cassandra_connection_host "$CassandraHost"
          |)
    """.stripMargin

    //Experimentation
    sql(importQuery)

    //Expectations
    ctx.tableNames() should contain(s"$Catalog.$Table")
    ctx.tableNames() should not contain "highschool.teachers"
  }

  it should "fail when infer schema using table without keyspace" in {
    assumeEnvironmentIsUpAndRunning
    ctx.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
          |OPTIONS (
          | cluster "$ClusterName",
          | table "$Table",
          | spark_cassandra_connection_host "$CassandraHost"
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
       | cluster "$ClusterName"
       |)
    """.stripMargin,
    s"""
       |IMPORT TABLES
       |USING $SourceProvider
       |OPTIONS (
       | spark_cassandra_connection_host '$CassandraHost'
       |)
     """.stripMargin
  )

  wrongImportTablesSentences.take(1) foreach { sentence =>
    it should s"not import tables for sentences lacking mandatory options: $sentence" in {
      assumeEnvironmentIsUpAndRunning
      an[Exception] shouldBe thrownBy(ctx.sql(sentence))
    }
  }


  def createOtherTables(): (Cluster, Session) ={
    val  (cluster, session) =  prepareClient.get

    session.execute(s"CREATE KEYSPACE NewKeyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}  AND durable_writes = true;")
    session.execute(s"CREATE TABLE NewKeyspace.NewTable (id int, coolstuff text, PRIMARY KEY (id))")

    (cluster, session)
  }

  def cleanOtherTables(cluster:Cluster, session:Session): Unit ={
    session.execute(s"DROP KEYSPACE NewKeyspace")

    session.close()
    cluster.close()
  }
}





