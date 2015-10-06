package com.stratio.crossdata.sql.sources.cassandra

class EphemeralTest extends CassandraConnectorIT with CassandraWithSharedContext {

  "The Cassandra connector" should "be able to use native UDFs" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT F(1) FROM $Table WHERE F(1) = name").collect()
    println(result)
  }

}
