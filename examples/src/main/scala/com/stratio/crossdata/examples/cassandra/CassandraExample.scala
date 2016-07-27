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
package com.stratio.crossdata.examples.cassandra

import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}

object CassandraExample extends App with CassandraDefaultConstants {

  val (cluster, session) = prepareEnvironment()

  withCrossdataContext { xdContext =>
    xdContext.sql(s"""|CREATE TEMPORARY TABLE $Table
          |USING $SourceProvider
          |OPTIONS (
          |table '$Table',
          |keyspace '$Catalog',
          |cluster '$ClusterName',
          |pushdown "true",
          |spark_cassandra_connection_host '$CassandraHost'
          |)
      """.stripMargin.replaceAll("\n", " "))

    // Native queries
    xdContext.sql(s"SELECT comment as b FROM $Table WHERE id = 1").show(5)
    xdContext
      .sql(s"SELECT comment as b FROM $Table WHERE id IN(1,2,3,4,5,6,7,8,9,10) limit 2")
      .show(5)
    xdContext.sql(s"SELECT *  FROM $Table ").show(5)

    // Spark queries
    xdContext
      .sql(s"SELECT comment as b FROM $Table WHERE comment = 'Comment 5' AND id = 5")
      .show(5)

  }

  cleanEnvironment(cluster, session)

  private def withCrossdataContext(commands: XDContext => Unit) = {

    val sparkConf =
      new SparkConf().setAppName("CassandraExample").setMaster("local[4]")

    val sc = new SparkContext(sparkConf)
    try {
      val xdContext = new XDContext(sc)
      commands(xdContext)
    } finally {
      sc.stop()
    }
  }

}
