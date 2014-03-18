package com.stratio.meta.server

import com.stratio.meta.communication.{Connect, ACK}
import akka.actor.Actor


class ServerStatusActor extends Actor{
  override def receive: Actor.Receive = {
    case Connect(msg) => sender ! ACK("PONG")
  }
}
