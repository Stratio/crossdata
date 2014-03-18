package com.stratio.meta.client

import akka.actor.{Props, ActorRef, Actor}
import akka.contrib.pattern.ClusterClient
import com.stratio.meta.communication.{Connect, Query}

object RemoteClientActor{
  def props(clusterClient:ActorRef) :Props= Props(new RemoteClientActor(clusterClient))
}

class RemoteClientActor(clusterClient:ActorRef) extends Actor{
  override def receive: Actor.Receive = {
    case Query(query)=> clusterClient forward ClusterClient.Send("/user/echo", Query(query), localAffinity = true)
    case Connect(msg) => clusterClient forward ClusterClient.Send("/user/server-status",Connect(msg),localAffinity = true)
    case _ => println("BASURA")
  }
}
