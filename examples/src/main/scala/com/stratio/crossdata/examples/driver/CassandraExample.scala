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
package com.stratio.crossdata.examples.driver

import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.examples.cassandra._

/**
 * Driver example - Cassandra
 */

sealed trait DefaultConstants {
  val ClusterName = "Test Cluster"
  val Catalog = "highschool"
  val Table = "students"
  val CassandraHost = "127.0.0.1"
  val SourceProvider = "cassandra"
  // Cassandra provider => org.apache.spark.sql.cassandra
  val CassandraOptions = Map(
    "cluster" -> ClusterName,
    "spark_cassandra_connection_host" -> CassandraHost
  )
}

object DriverExample extends App with DefaultConstants {

  val (cluster, session) = prepareEnvironment()

  var driver: Option[Driver] = None

  val driverConf = new DriverConf().
    setFlattenTables(false).
    setTunnelTimeout(30).
    setClusterContactPoint("127.0.0.1:13420", "127.0.0.1:13425")

  try {

    driver = Option(Driver.getOrCreate(driverConf))

    for {
      xdDriver <- driver
    } {
      xdDriver.importTables(SourceProvider, CassandraOptions).waitForResult()
      xdDriver.listTables().foreach(println(_))
      xdDriver.show(s"SELECT * FROM $Catalog.$Table")
    }

  } finally {
    driver.foreach(_.stop())
    cleanEnvironment(cluster, session)
  }

}
