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
