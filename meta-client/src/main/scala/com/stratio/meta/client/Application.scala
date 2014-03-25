/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.client

import akka.actor.{ActorSelection, ActorSystem}
import akka.contrib.pattern.ClusterClient
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._
import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import akka.pattern.ask
import scala.annotation.tailrec
import com.stratio.meta.communication.{Reply, ACK, Connect}


object Application extends App{
  import scala.concurrent.duration._
  import akka.util.Timeout
  import ConsoleCommands._
  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem("ClientSystem")

  val metaConfig= ConfigFactory.load()
  var initialContacts:Set[ActorSelection] = Set[ActorSelection]()
  metaConfig.getStringList(Config.serverHostPropName).toList.foreach(host=>{
    initialContacts += system.actorSelection("akka.tcp://" + Config.clusterServeName + "@" + host + "/user/receptionist")

  })


  val clusterClient = system.actorOf(ClusterClient.props(initialContacts))

  val remoteClientActor=system.actorOf(RemoteClientActor.props(clusterClient))

  @tailrec
  def checkConnect(repeat:Int = 0):Boolean={
    println("Empezando intento " +repeat)
    var complete=true

    val futurePong= remoteClientActor.ask(Connect("PING"))(1 second).mapTo[ACK]
    try{
    val result =Await.result(futurePong,5 second)
    }catch{
      case ex:Exception => {
        println(ex.getMessage)
        complete=false
      }
    }


    if(repeat >= 5  || complete){
      complete
    }else{

      checkConnect(repeat+1)
    }
  }

  if(checkConnect()){
    println("CONEXION CORRECTA")
  }else{
    println("ERROR DE CONEXION")
    system.shutdown()
  }

  @tailrec
  private def commandLoop(): Unit = {
    Console.readLine() match {
      case QuitCommand  => return
      case msg:String            => {
        /*val future= (remoteClientActor ask Query(msg)).mapTo[Reply]
        future.onComplete(answer=>{
          if(answer.isSuccess){
            println(answer.get.msg)
          }
        })*/
      }
    }
    commandLoop()
  }

  commandLoop()
  system.shutdown()




}
object ConsoleCommands {
  val QuitCommand   = "quit"
  val Ask           = "ask"
}
