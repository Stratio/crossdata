package com.stratio.meta2.server

import scala.annotation.tailrec

object MetaApplication extends App {
  val metaServer:MetaServer =new MetaServer
  @tailrec
  private def commandLoop(): Unit = {
    Console.readLine() match {
      case "quit" | "exit" => return
      case _ =>
    }
    commandLoop()
  }


  metaServer.init(null)
  metaServer.start()
  commandLoop()
  metaServer.stop()
  metaServer.destroy()
}
