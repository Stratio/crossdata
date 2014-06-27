/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
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