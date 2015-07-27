/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.examples

import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.{SQLContext}

trait DefaultConstants {
  val nIterations = 10000
  val cluster = "Test Cluster"
  val catalog = "highschool"
  val table = "students"
  val CassandraHost = "127.0.0.1"
}

object CassandraDriver extends App with DefaultConstants{

  val sparkConf = new SparkConf()
    .setAppName("CassandraExample")
    .setMaster("local[4]")
    .set("spark.cassandra.connection.host", CassandraHost)

  val sc = new SparkContext(sparkConf)
  val xdContext = new XDContext(sc)
  // Importing the SQL context gives access to all the SQL functions and implicit conversions.

/*  xdContext.sql(
    s"CREATE TEMPORARY TABLE $table USING org.apache.spark.sql.cassandra OPTIONS " +
      s"(keyspace \"$catalog\", table \"$table\", cluster \"$cluster\", pushdown \"true\")"
        .stripMargin)*/
  /*xdContext.sql(
    "CREATE TEMPORARY TABLE " + table + " USING org.apache.spark.sql.cassandra OPTIONS " +
      "(keyspace \"" + catalog + "\", table \"" + table + "\", " +
      "cluster \"" + cluster + "\", pushdown \"true\")".stripMargin)*/
  xdContext.sql(
    "CREATE TEMPORARY TABLE " + table + " USING com.stratio.crossdata.sql.sources.cassandra OPTIONS " +
      "(keyspace \"" + catalog + "\", table \"" + table + "\", " +
      "cluster \"" + cluster + "\", pushdown \"true\")".stripMargin)

  xdContext.sql(s"SELECT * FROM $table").collect().foreach(print)

  sc.stop()

}
