package com.stratio.meta.server

import akka.actor.{ Props, ActorSystem}
import akka.contrib.pattern.ClusterReceptionistExtension


object Application extends App{
  if (args.nonEmpty) System.setProperty("akka.remote.netty.tcp.port", args(0))

  // Create an Akka system
  val system = ActorSystem("ClusterSystem")


  val echo= system.actorOf(Props[EchoActor],"echo")
  ClusterReceptionistExtension(system).registerService(echo)

  val serverStatus = system.actorOf(Props[ServerStatusActor],"server-status")
  ClusterReceptionistExtension(system).registerService(serverStatus)

}

