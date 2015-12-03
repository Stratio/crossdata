/**
  * Copyright (C) 2015 Stratio (http://stratio.com)
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *          http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.stratio.crossdata.examples

import com.datastax.driver.core.{Session, Cluster}
import com.stratio.crossdata.driver.Driver


sealed trait DefaultConstants {
  val ClusterName = "Test Cluster"
  val Catalog = "highschool"
  val Table = "students"
  val CassandraHost = "127.0.0.1"
  val SourceProvider = "cassandra"
  // Cassandra provider => org.apache.spark.sql.cassandra
}

object DriverExample extends App with DefaultConstants {

  val (cluster, session) = CassandraExample.prepareEnvironment()
  val driver = Driver()

  CassandraExample.cleanEnvironment(cluster, session)
}
