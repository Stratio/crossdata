package com.stratio.crossdata.server.discovery

import org.apache.curator.framework.state.ConnectionState

object ZkConnectionState {

  var state = ConnectionState.CONNECTED

  def isConnected(): Boolean = {
    (state.equals(ConnectionState.CONNECTED) || state.equals(ConnectionState.RECONNECTED))
  }

  def update(newState: ConnectionState) {
    state = newState
  }

}
