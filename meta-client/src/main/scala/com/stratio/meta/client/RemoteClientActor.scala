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

package com.stratio.meta.client

import akka.actor.{Props, ActorRef, Actor}
import akka.contrib.pattern.ClusterClient
import com.stratio.meta.communication.Connect

object RemoteClientActor{
  def props(clusterClient:ActorRef) :Props= Props(new RemoteClientActor(clusterClient))
}

class RemoteClientActor(clusterClient:ActorRef) extends Actor{
  override def receive: Actor.Receive = {
    //case Query(query)=> clusterClient forward ClusterClient.Send("/user/echo", Query(query), localAffinity = true)
    case Connect(msg) => clusterClient forward ClusterClient.Send("/user/server-status",Connect(msg),localAffinity = true)
    case _ => println("BASURA")
  }
}
