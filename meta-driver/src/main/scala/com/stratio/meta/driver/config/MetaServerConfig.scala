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

package com.stratio.meta.driver.config

import com.typesafe.config.Config
import scala.collection.JavaConversions._

object MetaServerConfig{
  val META_SERVER_HOSTS_KEY="server.cluster.hosts"
  val META_SERVER_CLUSTER_NAME_KEY="server.cluster.name"
  val META_SERVER_ACTOR_NAME_KEY="server.actor.name"
}

trait MetaServerConfig {
  def config: Config = ???

  lazy val clusterHosts: List[String] = config.getStringList(MetaServerConfig.META_SERVER_HOSTS_KEY).toList
  lazy val clusterName: String = config.getString(MetaServerConfig.META_SERVER_CLUSTER_NAME_KEY)
  lazy val actorName: String = config.getString(MetaServerConfig.META_SERVER_ACTOR_NAME_KEY)

  lazy val contactPoints: List[String]= {
    clusterHosts.toList.map(host=>"akka.tcp://" + clusterName + "@" + host + "/user/receptionist")
  }

}
