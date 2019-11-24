/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming.constants

object ApplicationConstants {

  val StreamingResourceConfig = "streaming-reference.conf"
  val ParentPrefixName = "crossdata-streaming"
  val ConfigPrefixName = "config"
  val SparkPrefixName = "spark"
  val ZookeeperPrefixName = "zookeeper"
  val KafkaPrefixName = "kafka"
  val SparkNameKey = "spark.app.name"
  val StopGracefully = true

  val DefaultZookeeperConfiguration = Map(
    "connectionString" -> "127.0.0.1:2181",
    "connectionTimeout" -> 1500,
    "sessionTimeout" -> 60000,
    "retryAttempts" -> 6,
    "retryInterval" -> 10000
  )

}
