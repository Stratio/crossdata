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

package com.stratio.meta.driver

import akka.actor.{ ActorSelection, ActorSystem}
import com.stratio.meta.driver.config.DriverConfig
import akka.contrib.pattern.ClusterClient
import com.stratio.meta.driver.actor.ProxyActor
import com.stratio.meta.common.result.Result
import com.stratio.meta.common.ask.{Query, Connect}

class BasicDriver extends DriverConfig{
  val system = ActorSystem("MetaDriverSystem",config)
  lazy val initialContacts: Set[ActorSelection] = contactPoints.map(contact=> system.actorSelection(contact)).toSet
  lazy val clusterClientActor = system.actorOf(ClusterClient.props(initialContacts),"remote-client")
  lazy val proxyActor= system.actorOf(ProxyActor.props(clusterClientActor,actorName), "proxy-actor")

  def connect(user:String): Result = {
    println(contactPoints)
    retryPolitics.askRetry(proxyActor,new Connect(user))
  }


  def executeQuery(user:String, targetKs: String, query: String): Result = {
    retryPolitics.askRetry(proxyActor,new Query(targetKs,query,user))
  }

  def close() {
    system.shutdown()
  }



}
