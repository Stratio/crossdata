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
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class CassandraPKFiltersIT extends CassandraWithSharedContext {

  val uuid = java.util.UUID.randomUUID.toString.replace("-", "").substring(16)

  val FixedDate = "'2015-06-23 10:30+0100'"

  override val Catalog = s"ks$uuid"
  override val Table = s"t$uuid"
  override val UnregisteredTable = ""
  override val schema = ListMap("date" -> "text")
  override val pk = "date" :: Nil
  override val indexedColumn = ""
  override val testData = List(List(FixedDate))

  override val defaultOptions = Map(
    "table"    -> Table,
    "keyspace" -> Catalog,
    "cluster"  -> ClusterName,
    "pushdown" -> "true",
    "spark_cassandra_connection_host" -> CassandraHost
  )

  // PRIMARY KEY date

  "The Cassandra connector" should "execute natively a query with a filter by a PK of Timestamp type" in {
    assumeEnvironmentIsUpAndRunning
    val dataframe = sql(s"SELECT * FROM $Table WHERE ${pk(0)} = $FixedDate")
    val schema = dataframe.schema
    val result = dataframe.collect(Native)
    schema.fieldNames should equal (Seq(pk(0)))
    result should have length 1
    result(0) should have length 1
  }

}





