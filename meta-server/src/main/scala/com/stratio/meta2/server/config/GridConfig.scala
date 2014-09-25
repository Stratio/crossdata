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

package com.stratio.meta2.server.config

import java.util.concurrent.TimeUnit

import com.typesafe.config.Config

import scala.collection.JavaConversions._

object GridConfig {

  val GRID_LISTEN_ADDRESS_KEY = "config.grid.listen-address"
  val GRID_CONTACT_HOSTS_KEY = "config.grid.contact-hosts"
  val GRID_PORT_KEY = "config.grid.port"
  val GRID_MIN_INITIAL_MEMBERS_KEY = "config.grid.min-initial-members"
  val GRID_JOIN_TIMEOUT_KEY = "config.grid.join-timeout"
  val GRID_PERSISTENCE_PATH_KEY = "config.grid.persistence-path"

}

trait GridConfig {

  lazy val gridListenAddress: String = config.getString(GridConfig.GRID_LISTEN_ADDRESS_KEY)
  /**
   * The addresses of the hosts running grid
   */
  lazy val gridContactHosts: Array[String] = config.getStringList(GridConfig.GRID_CONTACT_HOSTS_KEY).toList.toArray
  /**
   * The port used by the grid
   */
  lazy val gridPort: Int = config.getInt(GridConfig.GRID_PORT_KEY)
  /**
   * The minimum number of grid members to be contacted
   */
  lazy val gridMinInitialMembers: Int = config.getInt(GridConfig.GRID_MIN_INITIAL_MEMBERS_KEY)
  /**
   * The timeout for connecting to other grid nodes
   */
  lazy val gridJoinTimeout: Long = config.getDuration(GridConfig.GRID_JOIN_TIMEOUT_KEY, TimeUnit.MILLISECONDS)
  /**
   * The grid files persistence path
   */
  lazy val gridPersistencePath: String = config.getString(GridConfig.GRID_PERSISTENCE_PATH_KEY)

  def config: Config = ???


}

