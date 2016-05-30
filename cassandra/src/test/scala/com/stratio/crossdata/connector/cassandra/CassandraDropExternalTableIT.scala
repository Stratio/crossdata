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
package com.stratio.crossdata.connector.cassandra

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.Seq

@RunWith(classOf[JUnitRunner])
class CassandraDropExternalTableIT extends CassandraWithSharedContext {

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    val createTableQueryString1 =
      s"""|CREATE EXTERNAL TABLE $Catalog.dropTable1 (
          |id Integer,
          |name String,
          |booleanFile boolean,
          |timeTime Timestamp,
          |binaryType Binary,
          |arrayType ARRAY<STRING>,
          |mapType MAP<INT, INT>,
          |decimalType DECIMAL
          |)
          |USING $SourceProvider
          |OPTIONS (
          |keyspace '$Catalog',
          |table 'dropTable1',
          |cluster '$ClusterName',
          |pushdown "true",
          |spark_cassandra_connection_host '$CassandraHost',
          |primary_key_string 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString1).collect()

    val createTableQueryString2 =
      s"""|CREATE EXTERNAL TABLE dropTable2 (
          |id Integer,
          |name String,
          |booleanFile boolean,
          |timeTime Timestamp,
          |binaryType Binary,
          |arrayType ARRAY<STRING>,
          |mapType MAP<INT, INT>,
          |decimalType DECIMAL
          |)
          |USING $SourceProvider
          |OPTIONS (
          |keyspace '$Catalog',
          |table 'drop_table_example',
          |cluster '$ClusterName',
          |pushdown "true",
          |spark_cassandra_connection_host '$CassandraHost',
          |primary_key_string 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString2).collect()

  }

  "The Cassandra connector" should "execute a DROP EXTERNAL TABLE" in {

    //Precondition
    xdContext.table(s"$Catalog.dropTable1") should not be null

    val cassandraTableName = "dropTable1"

    //DROP
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $Catalog.dropTable1"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table(s"$Catalog.dropTable1"))
    client.get._1.getMetadata.getKeyspace(Catalog).getTable(cassandraTableName) shouldBe null

  }

  "The Cassandra connector" should "execute a DROP EXTERNAL TABLE without specify database" in {

    //Precondition
    xdContext.table("dropTable2") should not be null

    val cassandraTableName = "drop_table_example"

    //DROP
    val dropExternalTableQuery = "DROP EXTERNAL TABLE dropTable2"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table("dropTable2"))
    client.get._1.getMetadata.getKeyspace(Catalog).getTable(cassandraTableName) shouldBe null

  }
  
}
