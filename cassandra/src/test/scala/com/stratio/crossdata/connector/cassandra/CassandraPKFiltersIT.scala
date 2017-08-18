package com.stratio.crossdata.connector.cassandra

import org.apache.log4j.Level
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class CassandraPKFiltersIT extends CassandraWithSharedContext {

  val uuid = java.util.UUID.randomUUID.toString.replace("-", "").substring(16)

  val FixedDate = "2015-06-23 10:30:00"

  override val Catalog = s"ks$uuid"
  override val Table = s"t$uuid"
  override val UnregisteredTable = ""
  override val schema = ListMap("date" -> "timestamp")
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
    val dataframe = sql(s"SELECT * FROM $Table WHERE ${pk(0)} = '$FixedDate'")
    val optimizedPlan = dataframe.queryExecution.optimizedPlan
    val schema = dataframe.schema
    val result = dataframe.collect(Native)
    schema.fieldNames should equal (Seq(pk(0)))
    result should have length 1
    result(0) should have length 1
  }

}





