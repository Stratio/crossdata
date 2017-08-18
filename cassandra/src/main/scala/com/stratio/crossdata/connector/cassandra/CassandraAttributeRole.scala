package com.stratio.crossdata.connector.cassandra

object CassandraAttributeRole extends Enumeration {
  type CassandraAttributeRole = Value
  val PartitionKey, ClusteringKey, Indexed, NonIndexed, Function, Unknown = Value
}