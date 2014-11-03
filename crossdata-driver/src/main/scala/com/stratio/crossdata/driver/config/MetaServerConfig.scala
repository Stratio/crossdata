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

package com.stratio.crossdata.driver.config

import com.typesafe.config.Config

import scala.collection.JavaConversions._

object CrossDataServerConfig {
  val CROSSDATA_SERVER_HOSTS_KEY = "config.cluster.hosts"
  val CROSSDATA_SERVER_CLUSTER_NAME_KEY = "config.cluster.name"
  val CROSSDATA_SERVER_ACTOR_NAME_KEY = "config.cluster.actor"
}

trait CrossDataServerConfig {
  lazy val clusterHosts: List[String] = config.getStringList(CrossDataServerConfig.CROSSDATA_SERVER_HOSTS_KEY).toList
  lazy val clusterName: String = config.getString(CrossDataServerConfig.CROSSDATA_SERVER_CLUSTER_NAME_KEY)
  lazy val clusterActor: String = config.getString(CrossDataServerConfig.CROSSDATA_SERVER_ACTOR_NAME_KEY)

  def config: Config = ???


}
