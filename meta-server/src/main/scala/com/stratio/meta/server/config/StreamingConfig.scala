/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.server.config;

import com.typesafe.config.Config

object StreamingConfig {
  val KAFKA_SERVER = "config.kafka.server"
  val KAFKA_PORT =  "config.kafka.port"
  val ZOOKEEPER_SERVER = "config.zookeeper.server"
  val ZOOKEEPER_PORT =  "config.zookeeper.port"
  val STREAMING_DURATION =  "config.streaming.duration"
  val STREAMING_GROUPID =  "config.streaming.groupId"
}

trait StreamingConfig {
  def config: Config = ???

  lazy val kafkaServer: String = config.getString(StreamingConfig.KAFKA_SERVER)
  lazy val kafkaPort: Int = config.getInt(StreamingConfig.KAFKA_PORT)
  lazy val zookeeperServer: String = config.getString(StreamingConfig.ZOOKEEPER_SERVER)
  lazy val zookeeperPort: Int = config.getInt(StreamingConfig.ZOOKEEPER_PORT)

  lazy val streamingDuration: Int = config.getInt(StreamingConfig.STREAMING_DURATION)
  lazy val streamingGroupId: String = config.getString(StreamingConfig.STREAMING_GROUPID)
}
