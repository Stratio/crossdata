/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.examples.driver

import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.examples.cassandra._


/**
 * Driver example - Join Kafka and Cassandra - Output to Kafka
 */
object StreamingSqlExample extends App with CassandraDefaultConstants with StreamingDefaultConstants{

  val (cluster, session) = prepareEnvironment()

  val driver = Driver.newSession(new DriverConf().setClusterContactPoint("127.0.0.1:13420"))

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
    driver.sql(importQuery).waitForResult()

    // Creates the ephemeral table (streaming input) receiving data from Kafka
    driver.sql(createEphemeralTable).waitForResult()

    // Adds a streaming query. It will be executed when the streaming process is running
    driver.sql(s"SELECT count(*) FROM $EphemeralTableName WITH WINDOW 5 SECS AS outputTopic")
   

    // Starts the streaming process associated to the ephemeral table
    driver.sql(s"START $EphemeralTableName")

    // WARNING: Data could be added to Kafka by using a Kafka console producer
    // Example: kafka-console-producer.sh --broker-list localhost:9092 --topic <input-topic>
    // Input events format: {"id": 1, "msg": "Hello world", "city": "Tolomango"}


    // WARNING: Then, you could start a Kafka consumer in order to read the processed data from queryAlias/outputTopic
    // Example: kafka-console-consumer.sh --zookeeper localhost:2181 --topic <query-alias>

    // Later, we can add a query to join batch and streaming sources, which output will be other Kafka topic
    // NOTE: In order to produce results, you should add ids matching the id's range of Cassandra table (1 to 10)
    driver.sql(
      s"""
         |SELECT name FROM $EphemeralTableName INNER JOIN $Catalog.$Table
         |ON $EphemeralTableName.id = $Table.id
         |WITH WINDOW 10 SECS AS joinTopic
      """.stripMargin)

    Thread.sleep(400 * 1000)

    // Stops the process. Once we want to stop receiving data, we can stop the process. The queries won't be deleted,
    // so if we restart the process the queries will be executed.
    driver.sql(s"STOP $EphemeralTableName").waitForResult()

  } finally {
    driver.sql(s"DROP ALL EPHEMERAL QUERIES").waitForResult()
    driver.sql(s"DROP TABLE $Catalog.$Table").waitForResult()
    driver.closeSession()
    Driver.shutdown()
    cleanEnvironment(cluster, session)
  }


}


trait StreamingDefaultConstants{
  val EphemeralTableName = "t"
  val InputTopic = "ephtable"
  val NumPartitionsToConsume = "1"
}
