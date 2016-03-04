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
 * Driver example - Join Kafka and Cassandra - Output to Kafka
 */
object StreamingSqlExample extends App with CassandraDefaultConstants with StreamingDefaultConstants{

  val (cluster, session) = prepareEnvironment()

  val driver = {
    val hosts = util.Arrays.asList("127.0.0.1:13420")
    Driver.getOrCreate(hosts)
  }

  val importQuery =
    s"""|IMPORT TABLES
        |USING $SourceProvider
        |OPTIONS (
        | cluster "$ClusterName",
        | spark_cassandra_connection_host '$CassandraHost'
        |)
      """.stripMargin

  val createEphemeralTable =
    s"""|CREATE EPHEMERAL TABLE $EphemeralTableName
        |OPTIONS (
        | receiver.kafka.topic '$InputTopic:$NumPartitionsToConsume',
        | receiver.kafka.groupId 'xd1'
        |)
      """.stripMargin

  try{
    // Imports tables from Cassandra cluster
    driver.syncQuery(SQLCommand(importQuery))

    // Creates the ephemeral table (streaming input) receiving data from Kafka
    driver.syncQuery(SQLCommand(createEphemeralTable))

    // Adds a streaming query. It will be executed when the streaming process is running
    driver.syncQuery(SQLCommand(s"SELECT count(*) FROM $EphemeralTableName WITH WINDOW 5 SECS AS outputTopic"))
   

    // Starts the streaming process associated to the ephemeral table
    driver.syncQuery(SQLCommand(s"START $EphemeralTableName"))

    // WARNING: Data could be added to Kafka by using a Kafka console producer
    // Example: kafka-console-producer.sh --broker-list localhost:9092 --topic <input-topic>
    // Input events format: {"id": 1, "msg": "Hello world", "city": "Tolomango"}


    // WARNING: Then, you could start a Kafka consumer in order to read the processed data from queryAlias/outputTopic
    // Example: kafka-console-consumer.sh --zookeeper localhost:2181 --topic <query-alias>

    // Later, we can add a query to join batch and streaming sources, which output will be other Kafka topic
    // NOTE: In order to produce results, you should add ids matching the id's range of Cassandra table (1 to 10)
    driver.syncQuery(SQLCommand(
      s"""
         |SELECT name FROM $EphemeralTableName INNER JOIN $Catalog.$Table
         |ON $EphemeralTableName.id = $Table.id
         |WITH WINDOW 10 SECS AS joinTopic
      """.stripMargin))

    Thread.sleep(400 * 1000)

    // Stops the process. Once we want to stop receiving data, we can stop the process. The queries won't be deleted,
    // so if we restart the process the queries will be executed.
    driver.syncQuery(SQLCommand(s"STOP $EphemeralTableName"))

  } finally {
    driver.syncQuery(SQLCommand(s"DROP ALL EPHEMERAL QUERIES"))
    driver.syncQuery(SQLCommand(s"DROP TABLE $Catalog.$Table"))
    driver.close()
    cleanEnvironment(cluster, session)
  }


}


trait StreamingDefaultConstants{
  val EphemeralTableName = "t"
  val InputTopic = "ephtable"
  val NumPartitionsToConsume = "1"
}
