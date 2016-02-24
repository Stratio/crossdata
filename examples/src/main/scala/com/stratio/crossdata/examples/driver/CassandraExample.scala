/**
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

import java.util

import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.examples.cassandra._

/**
 * Driver example - Cassandra
 */
object CassandraExample extends App with CassandraDefaultConstants {

  val (cluster, session) = prepareEnvironment()

  val driver = {
    val host: java.util.List[String] = new util.ArrayList[String]()
    host.add("127.0.0.1:13420")
    Driver(host)
  }

  val importQuery =
    s"""
       |IMPORT TABLES
       |USING $SourceProvider
       |OPTIONS (
       | cluster "$ClusterName",
       | spark_cassandra_connection_host '$CassandraHost'
       |)
      """.stripMargin

  try{
    // Import tables from Cassandra cluster
    driver.syncQuery(SQLCommand(importQuery))
    // List tables
    driver.listTables().foreach(println(_))
    // SQL
    driver.syncQuery(SQLCommand(s"SELECT * FROM $Catalog.$Table")).resultSet.foreach(println)

  } finally {
    driver.close()
    cleanEnvironment(cluster, session)
  }

}
