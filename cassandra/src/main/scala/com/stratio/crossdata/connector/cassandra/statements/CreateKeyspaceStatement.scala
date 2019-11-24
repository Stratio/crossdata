/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
