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
