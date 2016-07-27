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
package com.stratio.crossdata.connector.cassandra.statements

import com.stratio.crossdata.connector.cassandra.DefaultSource.CassandraDataSourceKeyspaceReplicationStringProperty

case class CreateKeyspaceStatement(options: Map[String, String]) {

  override def toString(): String = {
    val cqlCommand = StringBuilder.newBuilder
    cqlCommand.append(
        s"CREATE KEYSPACE $keyspace WITH REPLICATION = $replication")

    cqlCommand.toString()
  }

  lazy val keyspace: String = {
    options.get("keyspace").get
  }

  lazy val replication: String = {
    require(
        options.contains(CassandraDataSourceKeyspaceReplicationStringProperty),
        s"$CassandraDataSourceKeyspaceReplicationStringProperty required when use CREATE EXTERNAL TABLE command")
    options.get(CassandraDataSourceKeyspaceReplicationStringProperty).get
  }

}
