package com.stratio.crossdata.sql.sources.cassandra

object CassandraColumnRole extends Enumeration{
  type CassandraColumnRole = Value
  val PartitionKey, ClusteringKey, Indexed, NonIndexed, Unknown = Value
}
