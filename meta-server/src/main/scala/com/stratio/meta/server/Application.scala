package com.stratio.meta.server

import akka.actor.{ Props, ActorSystem}
import akka.contrib.pattern.ClusterReceptionistExtension
import com.stratio.meta.server.actors.ServerActor
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.server.config.ServerConfig


object Application extends App with ServerConfig{
  val engine = new Engine(engineConfig)

  if (args.nonEmpty) System.setProperty("akka.remote.netty.tcp.port", "13420")



  // Create an Akka system
  val system = ActorSystem("MetaServerCluster")


  val serverActor= system.actorOf(ServerActor.props(engine) ,"ServerActor")
  ClusterReceptionistExtension(system).registerService(serverActor)

}

