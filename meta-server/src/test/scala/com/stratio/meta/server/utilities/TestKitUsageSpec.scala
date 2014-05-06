package com.stratio.meta.server.utilities

/**
 * To generate unit test of proxy actor
 */
object TestKitUsageSpec {
  val config = """
    akka {
    loglevel = "WARNING"
    }
    akka.remote.netty.tcp.port=13331
               """
}