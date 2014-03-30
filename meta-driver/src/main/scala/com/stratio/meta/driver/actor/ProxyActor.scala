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

package com.stratio.meta.driver.actor

import akka.actor.{Actor, Props, ActorRef}
import akka.contrib.pattern.ClusterClient

object ProxyActor{
  def props(clusterClientActor: ActorRef, remoteActor: String): Props= Props(new ProxyActor(clusterClientActor,
    remoteActor))
  val INIT_PATH= "/user/"
  def remotePath(remoteActor: String)= INIT_PATH + remoteActor
}

class ProxyActor(clusterClientActor:ActorRef, remoteActor:String) extends Actor{
  override def receive: Actor.Receive = {
    case message => {
      clusterClientActor forward ClusterClient.Send(ProxyActor.remotePath(remoteActor),message,localAffinity = true)
    }
  }
}


