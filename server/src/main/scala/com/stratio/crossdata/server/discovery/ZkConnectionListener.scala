package com.stratio.crossdata.server.discovery

import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.state.{ConnectionState, ConnectionStateListener}

class ZkConnectionListener extends ConnectionStateListener {

  override def stateChanged(client: CuratorFramework, newState: ConnectionState) = {
    ZkConnectionState.update(newState)
  }
}
