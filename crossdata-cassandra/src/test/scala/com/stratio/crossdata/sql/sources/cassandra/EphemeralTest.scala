package com.stratio.crossdata.sql.sources.cassandra

import org.apache.spark.sql.crossdata.ExecutionType._

class EphemeralTest extends CassandraConnectorIT with CassandraWithSharedContext {

  "The Cassandra connector" should "be able to use native UDFs" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT F(id) FROM $Table WHERE F(F(id)) = name").collect(Native)
    println(result)
  }

}
