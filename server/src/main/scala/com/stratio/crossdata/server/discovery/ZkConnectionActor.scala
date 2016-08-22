package com.stratio.crossdata.server.discovery

import akka.actor.Actor
import akka.event.Logging
import org.apache.curator.framework.CuratorFramework

//TODO: Props
//TODO: state using become



class ZkConnectionActor(val sdClient: CuratorFramework) extends Actor {

  val log = Logging(context.system, this)

  override def receive: Receive = {
    case other => log.info(s"other")
  }
}
