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

