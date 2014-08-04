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

package com.stratio.meta.server.config

import com.hazelcast.config.{MapConfig, MaxSizeConfig, InMemoryFormat}
import com.hazelcast.core.{Hazelcast, HazelcastInstance, IMap}
import com.typesafe.config.Config
import scala.collection.JavaConversions._

object HazelcastConfig {

  val HAZELCAST_HOSTS_KEY = "config.hazelcast.hosts"
  val HAZELCAST_PORT_KEY = "config.hazelcast.port"
  val HAZELCAST_MAP_NAME_KEY = "config.hazelcast.map-name"
  val HAZELCAST_MAP_SIZE_KEY = "config.hazelcast.map-size"
  val HAZELCAST_MAP_BACKUP_KEY = "config.hazelcast.map-backup"

}

trait HazelcastConfig {

  def config: Config = ???

  /**
   * The addresses of the hosts running Hazelcast
   */
  lazy val hazelcastHosts: Array[String] = config.getStringList(HazelcastConfig.HAZELCAST_HOSTS_KEY).toList.toArray

  /**
   * The port used by Hazelcast
   */
  lazy val hazelcastPort: Int = config.getInt(HazelcastConfig.HAZELCAST_PORT_KEY)

  /**
   * The name of the Hazelcast map
   */
  lazy val hazelcastMapName: String = config.getString(HazelcastConfig.HAZELCAST_MAP_NAME_KEY)

  /**
   * The max number of entries per node in the Hazelcast map
   */
  lazy val hazelcastMapSize: Int = config.getInt(HazelcastConfig.HAZELCAST_MAP_SIZE_KEY)

  /**
   * The Hazelcast map's number of backups
   */
  lazy val hazelcastMapBackup: Int = config.getInt(HazelcastConfig.HAZELCAST_MAP_BACKUP_KEY)


}

