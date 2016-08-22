package com.stratio.crossdata.server.discovery

import akka.actor.ActorRef
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.state.{ConnectionState, ConnectionStateListener}

class ZkConnectionListener(val zkConnectionActor: ActorRef) extends ConnectionStateListener {
  override def stateChanged(client: CuratorFramework, newState: ConnectionState) = {
    zkConnectionActor.tell(newState, None)
  }
}
