package com.stratio.crossdata.connector.cassandra.statements


import com.stratio.crossdata.connector.cassandra.DefaultSource.CassandraDataSourceKeyspaceReplicationStringProperty

case class CreateKeyspaceStatement(options: Map[String, String]) {


  override def toString(): String = {
    val cqlCommand = StringBuilder.newBuilder
    cqlCommand.append(s"CREATE KEYSPACE $keyspace WITH REPLICATION = $replication")

    cqlCommand.toString()
  }

  lazy val keyspace: String = {
    options.get("keyspace").get
  }


  lazy val replication: String = {
    require(options.contains(CassandraDataSourceKeyspaceReplicationStringProperty),
      s"$CassandraDataSourceKeyspaceReplicationStringProperty required when use CREATE EXTERNAL TABLE command")
    options.get(CassandraDataSourceKeyspaceReplicationStringProperty).get
  }

}
