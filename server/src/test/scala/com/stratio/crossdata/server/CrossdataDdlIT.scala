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
package com.stratio.crossdata.server

import java.nio.file.Paths

import com.stratio.crossdata.server.config.ServerConfig
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class CrossdataDdlIT extends SharedXDContextTest with ServerConfig{

  override lazy val logger = Logger.getLogger(classOf[CrossdataDdlIT])

  val MongoHost: String = {
    Try(ConfigFactory.load().getStringList("mongo.hosts")).map(_.get(0)).getOrElse("127.0.0.1")
  }

  val CassandraHost: String = {
    Try(ConfigFactory.load().getStringList("cassandra.hosts")).map(_.get(0)).getOrElse("127.0.0.1")
  }
  val ClusterName = "Test Cluster"

  "Crossdata" should "support 'create table as select' statements for sources implementing CreatableRelationProvider " in {

    val tableCSV = "nnnTable"

    val keyspaceName = "nnnKeyspace"
    val tableName = "nnnCassTable"

    val mongoCollection = "nnnMongoTable"
    val mongoDatabase = "nnnMDatabase"

    try {

      sql(s"CREATE TEMPORARY TABLE $tableCSV USING com.databricks.spark.csv OPTIONS (path '${Paths.get(getClass.getResource("/cars.csv").toURI()).toString}', header 'true')")

      sql(s"CREATE TABLE $mongoCollection USING mongodb OPTIONS (host '$MongoHost', database '$mongoDatabase', collection '$mongoCollection') AS SELECT * FROM $tableCSV").collect()

      val csvResult = sql(s"SELECT * FROM $tableCSV").collect()
      csvResult should have length 8
      csvResult.head should have length 9

      val mongodbResult = sql(s"SELECT * FROM $mongoCollection").collect()
      mongodbResult.length shouldBe csvResult.length
      mongodbResult.head.length shouldBe csvResult.head.length

      // TODO Blocked by spark cassandra connector; in its implementation, the table is supposed to be already created in Cassandra
      /** sql(s"CREATE TABLE $tableName USING cassandra OPTIONS (spark_cassandra_connection_host '$CassandraHost', cluster '$ClusterName', keyspace '$keyspaceName', table '$tableName') AS SELECT * FROM $tableCSV").collect()
        * val cassResult = sql(s"SELECT * FROM $tableName").collect()
        * cassResult.length shouldBe csvResult.length
        * cassResult.head.length shouldBe csvResult.head.length
        */

    } finally {
      //sql(s"DROP TABLE $tableName")
      Try(sql(s"DROP TABLE $mongoCollection"))
      Try(sql(s"DROP TABLE $tableCSV"))
    }
  }

  it should "support 'create table as select' statements for HadoopProviders " in {
    val tableCSV = "nnnTable"
    val newTableCSV = "duplicateCSVTable"

    try {
      sql(s"CREATE TEMPORARY TABLE $tableCSV USING com.databricks.spark.csv OPTIONS (path '${Paths.get(getClass.getResource("/cars.csv").toURI()).toString}', header 'true')")

      sql(s"CREATE TABLE $newTableCSV USING com.databricks.spark.csv OPTIONS (path '/tmp/cars_copy', header 'true') AS SELECT * FROM $tableCSV").collect()

      val csvResult = sql(s"SELECT * FROM $tableCSV").collect()
      csvResult should have length 8
      csvResult.head should have length 9

      val newCsvTableResult = sql(s"SELECT * FROM $newTableCSV").collect()
      newCsvTableResult.length shouldBe csvResult.length
      newCsvTableResult.head.length shouldBe csvResult.head.length

    } finally {
      Try(sql(s"DROP TABLE $newTableCSV"))
      Try(sql(s"DROP TABLE $tableCSV"))
    }
  }


}
