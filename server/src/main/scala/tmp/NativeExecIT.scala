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
package tmp

import org.apache.spark.sql.crossdata.{ExecutionType, XDContext, XDDataFrame}
import org.apache.spark.{SparkConf, SparkContext}

object NativeExecIT extends App {

  val conf = new SparkConf().setAppName("CassandraNativeExecIT").setMaster("local[*]")
  val sc = new SparkContext(conf)
  val xdContext = new XDContext(sc)

  val createTable  =
    s"""IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (
       |cluster 'Test Cluster',
       |keyspace 'databasetest',
       |table 'tab2',
       |spark_cassandra_connection_host '127.0.0.1')""".stripMargin

  xdContext.sql(createTable).show()

  val query = s"""SELECT * FROM databasetest.tab2 WHERE key = 25"""

  val result = xdContext.sql(query).asInstanceOf[XDDataFrame].collect(ExecutionType.Native)

  result.foreach(println)

  xdContext.sparkContext.stop()

}
